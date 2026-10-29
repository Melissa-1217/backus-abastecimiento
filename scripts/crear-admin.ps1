# Ejecutar una sola vez desde la carpeta del proyecto.
$ErrorActionPreference = 'Stop'
$correoInicial = Read-Host 'Correo del administrador principal (ejemplo: admin@backus.demo)'
$claveInicial = Read-Host 'Elige su contraseña (mínimo 12 caracteres)' -AsSecureString
$env:BACKUS_CREAR_ADMIN = 'true'
$env:BACKUS_ADMIN_INICIAL_CORREO = $correoInicial
$env:BACKUS_ADMIN_INICIAL_PASSWORD = [System.Net.NetworkCredential]::new('', $claveInicial).Password
try {
    if (-not $env:DB_PASSWORD) {
        $claveBD = Read-Host 'Contraseña de PostgreSQL' -AsSecureString
        $env:DB_PASSWORD = [System.Net.NetworkCredential]::new('', $claveBD).Password
    }
    Push-Location (Split-Path $PSScriptRoot -Parent)
    try { & .\mvnw.cmd spring-boot:run } finally { Pop-Location }
} finally {
    Remove-Item Env:BACKUS_CREAR_ADMIN -ErrorAction SilentlyContinue
    Remove-Item Env:BACKUS_ADMIN_INICIAL_CORREO -ErrorAction SilentlyContinue
    Remove-Item Env:BACKUS_ADMIN_INICIAL_PASSWORD -ErrorAction SilentlyContinue
}
