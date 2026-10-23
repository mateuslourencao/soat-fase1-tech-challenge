locals {
  image_name = "soat-fase1-tech-challenge-app:dev"

  project_root = abspath("${path.module}/../..")

  image_source_files = concat(
    [
      "${local.project_root}/Dockerfile",
      "${local.project_root}/pom.xml",
    ],
    [for file in fileset(local.project_root, "src/**") : "${local.project_root}/${file}"],
  )

  manifest_source_files = concat(
    [
      "${local.project_root}/kustomization.yaml",
      "${local.project_root}/schema.sql",
      "${local.project_root}/scripts/install-metrics-server.ps1",
    ],
    [for file in fileset(local.project_root, "k8s/**") : "${local.project_root}/${file}"],
  )

  image_fingerprint    = sha256(join("", [for file in local.image_source_files : filesha256(file)]))
  manifest_fingerprint = sha256(join("", [for file in local.manifest_source_files : filesha256(file)]))
}

resource "kind_cluster" "oficina" {
  name           = var.cluster_name
  wait_for_ready = true

  kind_config {
    kind        = "Cluster"
    api_version = "kind.x-k8s.io/v1alpha4"

    node {
      role = "control-plane"

      extra_port_mappings {
        container_port = 30081
        host_port      = 8081
        protocol       = "TCP"
      }

      extra_port_mappings {
        container_port = 30025
        host_port      = 8025
        protocol       = "TCP"
      }
    }
  }
}

resource "terraform_data" "build_and_load_image" {
  triggers_replace = [local.image_fingerprint, kind_cluster.oficina.id]

  provisioner "local-exec" {
    working_dir = local.project_root
    interpreter = ["PowerShell", "-NoProfile", "-NonInteractive", "-Command"]
    command     = "docker build --tag ${local.image_name} .; if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }; kind load docker-image ${local.image_name} --name ${var.cluster_name}; if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }"
  }
}

resource "terraform_data" "install_metrics_server" {
  triggers_replace = [
    kind_cluster.oficina.id,
    filesha256("${local.project_root}/scripts/install-metrics-server.ps1"),
  ]

  depends_on = [kind_cluster.oficina]

  provisioner "local-exec" {
    working_dir = local.project_root
    interpreter = ["PowerShell", "-NoProfile", "-NonInteractive", "-ExecutionPolicy", "Bypass", "-Command"]
    command     = "& \"${local.project_root}/scripts/install-metrics-server.ps1\" -Context \"kind-${var.cluster_name}\"; if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }"
  }
}

resource "terraform_data" "deploy_kustomization" {
  triggers_replace = [
    local.image_fingerprint,
    local.manifest_fingerprint,
    kind_cluster.oficina.id,
  ]

  depends_on = [
    terraform_data.build_and_load_image,
    terraform_data.install_metrics_server,
  ]

  provisioner "local-exec" {
    working_dir = local.project_root
    interpreter = ["PowerShell", "-NoProfile", "-NonInteractive", "-Command"]
    command     = "kubectl --context kind-${var.cluster_name} apply -k .; if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }; kubectl --context kind-${var.cluster_name} rollout status deployment/oficina-db --timeout=180s; if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }; kubectl --context kind-${var.cluster_name} rollout status deployment/oficina-api --timeout=240s; if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }; kubectl --context kind-${var.cluster_name} rollout status deployment/mailhog --timeout=120s; if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }"
  }
}
