@echo off
rem ------------------------------------------------------------
rem parar.bat - Para o Tomcat (os dados sao gravados ao encerrar).
rem ------------------------------------------------------------

if "%CATALINA_HOME%"=="" (
    echo [ERRO] A variavel CATALINA_HOME nao esta definida.
    pause
    exit /b 1
)

call "%CATALINA_HOME%\bin\shutdown.bat"
echo Tomcat a parar...
pause
