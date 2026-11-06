$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
    $inspectionJavac = 'javac.exe'
    $inspectionJava = 'java.exe'
    if ($env:JAVA_HOME) {
        $inspectionJavac = Join-Path $env:JAVA_HOME 'bin/javac.exe'
        $inspectionJava = Join-Path $env:JAVA_HOME 'bin/java.exe'
    }
    New-Item -ItemType Directory -Path 'target/classes' -Force | Out-Null
    $inspectionSources = @(Get-ChildItem -LiteralPath 'src/main/java' -Filter '*.java' -Recurse |
        ForEach-Object { $_.FullName })
    & $inspectionJavac --release 17 -encoding UTF-8 -d 'target/classes' @inspectionSources
    if ($LASTEXITCODE -ne 0) { throw 'Compilation failed. Select JDK 17 or newer.' }
    & $inspectionJava -cp 'target/classes' edu.assignment.inspection.Main
    if ($LASTEXITCODE -ne 0) { throw 'Demo failed.' }
} finally {
    Pop-Location
}
