param([string]$MavenCommand = 'mvn.cmd')

$ErrorActionPreference = 'Stop'
if (-not (Get-Command $MavenCommand -ErrorAction SilentlyContinue)) {
    $inspectionJetBrains = Join-Path $env:ProgramFiles 'JetBrains'
    $inspectionCandidates = @(Get-ChildItem -LiteralPath $inspectionJetBrains -Directory -ErrorAction SilentlyContinue |
        ForEach-Object { Join-Path $_.FullName 'plugins/maven/lib/maven3/bin/mvn.cmd' } |
        Where-Object { Test-Path -LiteralPath $_ })
    if ($inspectionCandidates.Count -eq 0) {
        throw 'Maven was not found. Install Maven or supply -MavenCommand with its full path.'
    }
    $MavenCommand = $inspectionCandidates[0]
}
Push-Location $PSScriptRoot
try {
    & $MavenCommand --batch-mode clean verify
    if ($LASTEXITCODE -ne 0) { throw 'Build or JUnit tests failed.' }
} finally {
    Pop-Location
}
