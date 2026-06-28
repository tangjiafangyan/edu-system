@echo off
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p123456 < "C:\Users\guochenhao\Desktop\edu-system\sql\schema.sql"
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p123456 < "C:\Users\guochenhao\Desktop\edu-system\sql\seed.sql"
echo Database setup complete.
