@echo off
chcp 65001 >nul
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" --default-character-set=utf8mb4 -u root -p123456 edu_system < "C:\Users\guochenhao\Desktop\edu-system\sql\seed.sql"
echo Seed data import complete.
