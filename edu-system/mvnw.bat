@echo off
set JAVA_HOME=D:\jdk-17.0.19.10-hotspot
set MAVEN_HOME=D:\IntelliJ IDEA Community Edition 2025.2.5\plugins\maven\lib\maven3
set PATH=%MAVEN_HOME%\bin;%JAVA_HOME%\bin;%PATH%
cd /d C:\Users\guochenhao\Desktop\edu-system
call "%MAVEN_HOME%\bin\mvn.cmd" %*
