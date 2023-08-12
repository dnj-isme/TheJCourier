@echo off
set "batchPath=%~dp0"
set "sqlFile=%batchPath%create+insert.sql"
set "errorLogFile=%batchPath%log.txt"

C:\xampp\mysql\bin\mysql.exe -u root < "%sqlFile%" 2>"%errorLogFile%"

IF %ERRORLEVEL% NEQ 0 (
    echo There were errors during execution. Check "%errorLogFile%" for details.
) ELSE (
    echo Migration Completed!
)
