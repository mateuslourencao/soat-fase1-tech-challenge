param(
    [string]$Context = "kind-oficina-local"
)

$ErrorActionPreference = "Stop"
$metricsServerManifest = "https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml"
$patchFile = Join-Path $env:TEMP "metrics-server-patch-$PID.json"

kubectl --context $Context apply -f $metricsServerManifest
if ($LASTEXITCODE -ne 0) {
    throw "Não foi possível instalar o Metrics Server no contexto '$Context'."
}

$deploymentJson = kubectl --context $Context get deployment metrics-server -n kube-system -o json
if ($LASTEXITCODE -ne 0) {
    throw "Não foi possível consultar o Deployment do Metrics Server no contexto '$Context'."
}

$deployment = $deploymentJson | ConvertFrom-Json
$containerArgs = @($deployment.spec.template.spec.containers[0].args)
if ($containerArgs -notcontains "--kubelet-insecure-tls") {
    Set-Content -Path $patchFile -Encoding Ascii -Value '[{"op":"add","path":"/spec/template/spec/containers/0/args/-","value":"--kubelet-insecure-tls"}]'
    try {
        kubectl --context $Context patch deployment metrics-server -n kube-system --type=json --patch-file $patchFile
        if ($LASTEXITCODE -ne 0) {
            throw "Não foi possível configurar o Metrics Server para o certificado do kubelet do Kind."
        }
    }
    finally {
        Remove-Item -LiteralPath $patchFile -ErrorAction SilentlyContinue
    }
}

kubectl --context $Context rollout status deployment/metrics-server -n kube-system --timeout=180s
if ($LASTEXITCODE -ne 0) {
    throw "O Deployment do Metrics Server não ficou pronto no contexto '$Context'."
}

for ($attempt = 1; $attempt -le 18; $attempt++) {
    $metrics = kubectl --context $Context top nodes 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Output $metrics
        exit 0
    }

    if ($attempt -eq 18) {
        Write-Error $metrics
        throw "O Metrics Server iniciou, mas não disponibilizou métricas no contexto '$Context' após 3 minutos."
    }

    Start-Sleep -Seconds 10
}
