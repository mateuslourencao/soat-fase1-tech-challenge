param(
    [string]$Context = "kind-oficina-local",
    [int]$DurationSeconds = 180,
    [int]$WorkerReplicas = 12,
    [int]$ConcurrentRequestsPerPod = 8,
    [string]$TargetUrl = "http://oficina-api-svc:8081/v1/publico/ordensdeservico/1"
)

$ErrorActionPreference = "Stop"
$loadDeployment = "oficina-hpa-load"
$manifestPath = Join-Path $env:TEMP "$loadDeployment.yaml"

function Invoke-Kubectl {
    param([string[]]$Arguments)

    & kubectl --context $Context @Arguments
    if ($LASTEXITCODE -ne 0) {
        throw "kubectl falhou: $($Arguments -join ' ')"
    }
}

if ($DurationSeconds -lt 30) {
    throw "DurationSeconds deve ser pelo menos 30."
}
if ($WorkerReplicas -lt 1 -or $ConcurrentRequestsPerPod -lt 1) {
    throw "WorkerReplicas e ConcurrentRequestsPerPod devem ser maiores que zero."
}

$manifest = @'
apiVersion: apps/v1
kind: Deployment
metadata:
  name: oficina-hpa-load
spec:
  replicas: __WORKER_REPLICAS__
  selector:
    matchLabels:
      app: oficina-hpa-load
  template:
    metadata:
      labels:
        app: oficina-hpa-load
    spec:
      containers:
        - name: http-load
          image: busybox:1.36
          command: ["/bin/sh", "-c"]
          args:
            - |
              i=0
              while [ "$i" -lt "$WORKERS_PER_POD" ]; do
                (
                  while true; do
                    wget -q -O /dev/null --timeout=5 "$TARGET_URL" || true
                  done
                ) &
                i=$((i + 1))
              done
              wait
          env:
            - name: TARGET_URL
              value: "__TARGET_URL__"
            - name: WORKERS_PER_POD
              value: "__CONCURRENT_REQUESTS__"
          resources:
            requests:
              cpu: 10m
              memory: 16Mi
            limits:
              cpu: 100m
              memory: 64Mi
'@

$manifest = $manifest.Replace("__WORKER_REPLICAS__", [string]$WorkerReplicas)
$manifest = $manifest.Replace("__CONCURRENT_REQUESTS__", [string]$ConcurrentRequestsPerPod)
$manifest = $manifest.Replace("__TARGET_URL__", $TargetUrl)
[System.IO.File]::WriteAllText($manifestPath, $manifest, [System.Text.UTF8Encoding]::new($false))

try {
    Write-Host "Verificando metricas do cluster e da API..."
    Invoke-Kubectl -Arguments @("top", "pods", "-l", "app=oficina-api")
    Invoke-Kubectl -Arguments @("get", "hpa", "oficina-api")

    Write-Host "Criando carga temporaria: $WorkerReplicas pods x $ConcurrentRequestsPerPod requisicoes concorrentes por pod."
    Invoke-Kubectl -Arguments @("apply", "-f", $manifestPath)

    $remaining = $DurationSeconds
    while ($remaining -gt 0) {
        Write-Host "`nCarga restante: $remaining s"
        Invoke-Kubectl -Arguments @("get", "hpa", "oficina-api")
        Invoke-Kubectl -Arguments @("get", "deployment", "oficina-api", $loadDeployment)
        Start-Sleep -Seconds ([Math]::Min(10, $remaining))
        $remaining -= 10
    }
}
finally {
    Write-Host "Removendo a carga temporária..."
    & kubectl --context $Context delete deployment $loadDeployment --ignore-not-found=true --wait=false
    Remove-Item -LiteralPath $manifestPath -Force -ErrorAction SilentlyContinue
}

Write-Host "Teste de carga encerrado. O HPA pode levar alguns minutos para reduzir as replicas."
