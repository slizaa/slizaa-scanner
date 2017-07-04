@echo off
set SCRIPT_DIR=%~dp0%
@if "%SLIZAA_ROOT%" == ""  (
  set SLIZAA_ROOT=%SCRIPT_DIR%\..
)
@if "%SLIZAA_OPTS%" == ""  (
  set SLIZAA_OPTS="-Xmx2048m -Xms256m"
)
set LIBS_DIR=%SLIZAA_ROOT%\libs
java %SLIZAA_OPTS% -cp %LIBS_DIR%\* org.slizaa.scanner.cmdline.Slizaa %*
