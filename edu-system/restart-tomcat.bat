@echo off
set JAVA_HOME=D:\jdk-17.0.19.10-hotspot
set CATALINA_HOME=D:\apache-tomcat-10.1.24
cd /d %CATALINA_HOME%
call "%CATALINA_HOME%\bin\shutdown.bat"
timeout /t 5 /nobreak >nul
rmdir /s /q "%CATALINA_HOME%\webapps\edu-system" 2>nul
del /f "%CATALINA_HOME%\webapps\edu-system.war" 2>nul
copy "C:\Users\guochenhao\Desktop\edu-system\target\edu-system.war" "%CATALINA_HOME%\webapps\"
call "%CATALINA_HOME%\bin\startup.bat"
echo Done!
