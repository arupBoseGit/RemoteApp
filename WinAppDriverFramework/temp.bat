@echo off


call popd
call cd C:\Users\blackbox\Downloads\RemoteApp\WinAppDriverFramework
IF EXIST "test-output\Screenshots" rmdir /s /q "test-output\Screenshots"
REM mkdir %BUILD_NUMBER%
java -cp C:\Program Files\Java\jdk-17.0.1\lib*;C:\Program Files\Java\jdk-17.0.1\bin -Drelease=%LAST% -Dbrowser=%Browser% -Demerald=%emerald% -Demeraldse=%emeraldse% org.testng.TestNG testngSE.xml
REM call ant
REM call ant GenerateSeleniumReport
REM IF EXIST C:\Test_Workstation\SeleniumAutomation\Screenshots xcopy "C:\Test_Workstation\SeleniumAutomation\Screenshots" "C:\Test_Workstation\SeleniumAutomation\Screenshots\" /E
exit 0