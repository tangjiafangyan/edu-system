@echo off
set JAVA_HOME=D:\jdk-17.0.19.10-hotspot
set CATALINA_HOME=D:\apache-tomcat-10.1.24
cd /d %CATALINA_HOME%
call "%CATALINA_HOME%\bin\startup.bat"
