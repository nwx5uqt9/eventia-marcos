@echo off
echo ========================================
echo PRUEBA DE LOGIN - Sistema Eventia
echo ========================================
echo.

echo [1] Probando conexion con el servidor...
curl -s http://localhost:8082/auth/check/admin > nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] El servidor no esta corriendo en el puerto 8082
    echo.
    echo Por favor, ejecuta primero:
    echo   cd C:\xampp\htdocs\ProyFinalMarcosWeb\eventia-marcos\Pagina_Eventos\Pagina_Eventos
    echo   .\mvnw.cmd spring-boot:run
    echo.
    pause
    exit /b 1
)
echo [OK] Servidor conectado!
echo.

echo [2] Probando login con usuario de prueba...
echo.
echo Datos de login:
echo   Username: admin
echo   Password: admin123
echo.

curl -X POST http://localhost:8082/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"admin\",\"password\":\"admin123\"}"

echo.
echo.
echo ========================================
echo.
echo Si ves "success":true, el login funciona!
echo Si ves "success":false, verifica la contraseña en la BD
echo.
pause

