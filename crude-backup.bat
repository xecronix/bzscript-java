@echo off
setlocal
set "CRUDE_EXE=C:\CRUDE\crude.exe"
if not exist "%CRUDE_EXE%" set "CRUDE_EXE=C:\dev\crude\crude.exe"
if not exist "%CRUDE_EXE%" (
  echo Crude executable not found. Edit CRUDE_EXE in this script to its installed location.
  exit /b 1
)
rem Keep snapshots outside this workspace to avoid recursive copies.
"%CRUDE_EXE%" "%~dp0." "C:\CRUDE"
exit /b %errorlevel%
