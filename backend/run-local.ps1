<#
.SYNOPSIS
    Runs the backend directly on the host (no Docker), against a local PostgreSQL on localhost:5432.

.DESCRIPTION
    Use this when you need the backend to reach the public internet directly from your machine
    (e.g. to test the futbolfantasy.com price scraper), instead of via the Docker/Podman network.

    Sets DB_URL to point at localhost instead of the "db" Docker service name, and passes
    -Djavax.net.ssl.trustStoreType=Windows-ROOT so the JVM trusts the same root certificates as
    Windows. This is required if your machine sits behind a TLS-inspecting corporate proxy or
    antivirus (you'll otherwise get SSLHandshakeException: PKIX path building failed).

.PARAMETER SkipTests
    Skip running the test suite (faster iteration). Defaults to $true.

.EXAMPLE
    .\run-local.ps1
    .\run-local.ps1 -SkipTests:$false
#>
param(
    [bool]$SkipTests = $true
)

$env:DB_URL = "jdbc:postgresql://localhost:5432/fantasy"

$mavenArgs = @("package")
if ($SkipTests) {
    $mavenArgs += "-DskipTests"
}
$mavenArgs += "spring-boot:run"
$mavenArgs += "-Dspring-boot.run.jvmArguments=-Djavax.net.ssl.trustStoreType=Windows-ROOT"

mvn @mavenArgs
