@echo off
rem ------------------------------------------------------------
rem iniciar.bat - Compila o AgroGest, publica o WAR no Tomcat e arranca-o.
rem Requisitos: Maven no Path e variavel CATALINA_HOME definida.
rem ------------------------------------------------------------

if "%CATALINA_HOME%"=="" (
    echo [ERRO] A variavel CATALINA_HOME nao esta definida.
    pause
    exit /b 1
)

rem Vai para a pasta raiz do projecto (uma acima de scripts)
cd /d "%~dp0.."

echo [1/3] A compilar com Maven...
call mvn clean package
if errorlevel 1 (
    echo [ERRO] A compilacao falhou.
    pause
    exit /b 1
)

echo [2/3] A copiar o WAR para o Tomcat...
copy /Y "target\agrogest.war" "%CATALINA_HOME%\webapps\agrogest.war" >nul
rem Remove a versao antiga ja extraida para o Tomcat usar o WAR novo
if exist "%CATALINA_HOME%\webapps\agrogest" rmdir /s /q "%CATALINA_HOME%\webapps\agrogest"

echo [3/3] A arrancar o Tomcat...
call "%CATALINA_HOME%\bin\startup.bat"

echo.
echo Pronto. Abra no browser: http://localhost:8080/agrogest
pause
