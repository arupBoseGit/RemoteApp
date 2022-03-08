@echo off


call popd
call cd C:\Users\blackbox\git\RemoteApp\WinAppDriverFramework
IF EXIST "test-output\Screenshots" rmdir /s /q "test-output\Screenshots"
REM mkdir %BUILD_NUMBER%
 -Drelease=%LAST% -Dbrowser=%Browser% -Demerald=%emerald% -Demeraldse=%emeraldse% org.testng.TestNG testng.xml
REM call ant
REM call ant GenerateSeleniumReport
REM IF EXIST C:\Test_Workstation\SeleniumAutomation\Screenshots xcopy "C:\Test_Workstation\SeleniumAutomation\Screenshots" "C:\Test_Workstation\SeleniumAutomation\Screenshots\" /E
exit 0