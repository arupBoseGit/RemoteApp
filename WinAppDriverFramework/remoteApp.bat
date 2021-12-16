@Echo off
SET LOGFILE=C:\Users\blackbox\Batchfile_log.log
call :Logit >> %LOGFILE%
exit /b O
:Logit
set projectpath=C:\Users\blackbox\git\RemoteApp\WinAppDriverFramework
cd %projectpath%
set classpath=%projectpath%\target;%projectpath%\pom.xml
java org.testng.TestNG %projectpath%\testng.xml
pause
