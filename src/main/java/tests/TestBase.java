package tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.asserts.SoftAssert;

import io.appium.java_client.windows.WindowsDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import methods.AppliancePool;
import methods.Device;
import methods.Devices;
import methods.DiscoveryMethods;
import methods.SeleniumActions;
import methods.Switch;
import methods.SystemAll;
import pages.ConnectionPage;
import pages.LandingPage;
import pages.UserPage;
import pages.boxillaElements;

public class TestBase{
	
	
	 public WindowsDriver Windriver;
	 public WebDriver firedrive;
	 public static Properties prop = new Properties();
	 private Properties deviceProperties = new Properties();
	 protected Device txSingle, rxSingle, txDual, rxDual, txEmerald, rxEmerald, shTx, dhTx, shRx, dhRx;
	 public static int waitTime=30;
	 
	 public String singleTxName;
	 public String singleRxName;
	 protected String txIp = prop.getProperty("txIP");
	 protected String txIpDual = prop.getProperty("txIPDual");
	 public String dualTxName;
	
	 String boxillaManager;
	 public static String RAusername;
	 public static String RApassword;
	 public String AutomationUsername;
	 public String AutomationPassword;
	 public static String boxillaUsername;
	 public static String boxillaPassword;
	 public static String deviceUserName, devicePassword;
	 protected AppliancePool devicePool = new AppliancePool();
	 boolean fpsstatus=true;
	 boolean Tmstatus=true;
	 public static String url;
	 private static int testCounter;
	 private static long splitTime;
	 private static long startTime;
	 SoftAssert softAssertion= new SoftAssert();
	 ArrayList<String> connectionList = new ArrayList<String>();
	 ArrayList<Device> devices;
	 ArrayList<String> SharedNames;
		 UserPage userpage = new UserPage();
	
	
	
	 @BeforeSuite
	 public void login() throws InterruptedException {
		 loadProperties();
		 devices = devicePool.getAllDevices("device.properties");
		 boxillaUsername = prop.getProperty("boxillaUsername");
		 boxillaPassword = prop.getProperty("boxillaPassword");
		 RAusername = prop.getProperty("RAusername");
		 RApassword = prop.getProperty("RAusername");
		 AutomationUsername = prop.getProperty("AutomationUsername");
		 AutomationPassword = prop.getProperty("AutomationPassword");
		 deviceUserName = prop.getProperty("deviceUserName");
		 devicePassword = prop.getProperty("devicePassword");
		 boxillaManager=prop.getProperty("boxillaManager");
		 System.out.println("loaded username is "+boxillaUsername);
		 System.out.println("loaded password is "+boxillaPassword);
		 
		 try {
			 System.out.println("Attempting to manage devices");
			 System.out.println("BoxillaManager is "+boxillaManager);
			 cleanUpLogin();
				enableNorthboundAPI(firedrive);
		//		Managedevices();
		//		ConnectionPage.createprivateconnections(firedrive,devices);
		//	SharedNames=ConnectionPage.Sharedconnection(firedrive,devices);
		//	userpage.createUser(firedrive,devices,RAusername,RApassword,"General");
			//	userpage.ManageConnection(firedrive,devices,RAusername);
			//	UserPage.Sharedconnectionassign(firedrive, RAusername, SharedNames);
		      
		    
				

		 }
		 catch(Exception e) {
				Utilities.captureScreenShot(firedrive, this.getClass().getName() + "_beforeClass", "Before Class");
				e.printStackTrace();
				cleanUpLogout();
			}
			cleanUpLogout();
			
	 }
	 
	 public interface Callback {
		 public void cleanUp();
	 }
	
	 public void cleanUpLogin() throws Exception {
		// splitTime = System.currentTimeMillis();
		 String url = "https://"+boxillaManager+"/";
		System.setProperty("webdriver.gecko.driver", "C:\\Users\\blackbox\\eclipse-workspace\\geckodriver.exe");
			
			FirefoxOptions ffoptions = new FirefoxOptions();
			
			firedrive = new FirefoxDriver(ffoptions);
			   DesiredCapabilities handleError = new DesiredCapabilities();
				handleError.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
				handleError.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				ffoptions.merge(handleError);
			firedrive.get(url);
			
			
			boxillaElements.username(firedrive).sendKeys(boxillaUsername);
			boxillaElements.password(firedrive).sendKeys(boxillaPassword);
			boxillaElements.Login(firedrive).click();
			System.out.println("Logged In to boxilla");
			
	 }
	 private void getApplianceVersion(String deviceIp) {
			Ssh ssh = new Ssh(deviceUserName, devicePassword, deviceIp);
			ssh.loginToServer();
			String applianceBuild = ssh.sendCommand("cat /VERSION");
			System.out.println("removing all logs before starting tests");
			ssh.sendCommand("rm /usr/local/syslog.log*");
			ssh.disconnect();
			System.out.println("Appliance build: " + applianceBuild);
		}
		


	 public void setup() {
		
		DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("app", "C:\\Program Files (x86)\\BlackBox\\EmeraldRA\\EmeraldRA.exe");
        capabilities.setCapability("platformName", "Windows");
        capabilities.setCapability("deviceName", "WindowsPC");
      try {
    	  Thread.sleep(1000);
    	  Windriver = new WindowsDriver(new URL("http://127.0.0.1:4723"), capabilities);
       
      }
      catch(Exception e){
        e.printStackTrace();
      } 
      Windriver.manage().timeouts().implicitlyWait(3,TimeUnit.SECONDS);
      
    }
	
	public WebDriver getdriver() {
		return firedrive;
	}
	
	public void CleanUp(){

		Windriver.quit();
		if(Windriver.findElementByName("Cancel").isDisplayed()) {
			Windriver.findElementByName("Cancel").click();
		}
	  
	}
	
	public void RAlogin(String username, String Password) throws Exception {
		setup();
		WebElement loginButton = getElement("logInButton");
		getElement("userNameTextBox").sendKeys(username);
		System.out.println("Username Entered");
		getElement("passwordTextBox").sendKeys(Password);
		System.out.println("Password Entered");
		loginButton.click();
		Thread.sleep(2000);
		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
	}
	

public void closeRemoteApp() {
//	Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
	
	if((getElement("closeLogInScreen").isDisplayed())){
		getElement("closeLogInScreen").click();
	}
	if(getElement("menuLabel").isDisplayed()) {
		getElement("menuLabel").click();
		Windriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		Windriver.findElementByName("Close").click();
		Windriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}
	System.out.println("RemoteApp Closed");
	//Windriver.close();
}

public void closeApp() {
	System.out.println("Closing RemoteApp....");
	getElement("menuLabel").click();
	Windriver.findElementByName("Close").click();
	System.out.println("RemoteApp Closed");
}
//	public WindowsDriver getSession() {
//	  return AppSession;
//	}
	public WebElement getElement(String name) {
		   return Windriver.findElementByAccessibilityId(name);
		}

	/**
	 * Will load the property file into memory for use in test cases
	 */
	public void loadProperties() {
		System.out.println("Loading properties");
		try {
			InputStream in = new FileInputStream("C:\\Users\\abose\\eclipse-workspace\\WinAppDriverFramework\\test.properties");
			prop.load(in);
			in.close();
			System.out.println("Properties loaded successfully");
		} catch (IOException e) {
			System.out.println("Properties file failed to load");
		}
	}

	/**
	 * 
	 * @return the property file object
	 */
	public Properties getProp() {
		return prop;
	}

	
	
	public void Managedevices() throws InterruptedException {
		
		System.out.println("Attempting to manage devices for RemoteApp");	
		DiscoveryMethods discoveryMethods = new DiscoveryMethods();		
	
		for(Device deviceList : devicePool.allDevices()) {
		System.out.println("Adding the device "+deviceList);
		discoveryMethods.addDeviceToBoxilla(firedrive, deviceList.getMac(), deviceList.getIpAddress(),
				deviceList.getGateway(),deviceList.getNetmask(), deviceList.getDeviceName(), 10);
		}
		System.out.println("*************All Devices are Managed***************");

	}
		
	public void timer(WebDriver driver) throws InterruptedException { // Method for thread sleep
		Thread.sleep(2000);
	}

	public void unManageDevice(WebDriver driver, ArrayList<Device> dualHeadDevices) throws InterruptedException {
		for (Device ipaddress:dualHeadDevices) {
		navigateToOptions(driver, ipaddress.getIpAddress());
		timer(driver);
		SeleniumActions.seleniumClick(driver, Devices.unManageTab);
		System.out.println("UnManage Device -  Clicked on Unmanage Tab");
		Alert alert = driver.switchTo().alert();
		alert.accept();
		int counter = 0;
		System.out.println("Waiting for spinner to appear.");
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Switch.spinnerXpath)));
		System.out.println("The spinner has appeared, waiting for spinner to disappear.");
		new WebDriverWait(driver, 60).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Switch.spinnerXpath)));
		Thread.sleep(3000);
		SeleniumActions.seleniumSendKeys(driver, Devices.deviceStatusSearchBox, ipaddress.getDeviceName());
		System.out.println("UnManage Device - Device name entered in search box");
		timer(driver);
		String deviceApplianceTable = SeleniumActions.seleniumGetText(driver, Devices.applianceTable);
		Assert.assertFalse(deviceApplianceTable.contains(ipaddress.getDeviceName()),
				"Device appliance table did not contain: " + dualHeadDevices + ", actual text: " + dualHeadDevices);
	}
	}
	public void navigateToOptions(WebDriver driver, String ipAddress) throws InterruptedException {
		checkDeviceOnline(driver, ipAddress);
		timer(driver);
		if (SeleniumActions.seleniumGetText(driver, Devices.applianceTable).contains(ipAddress)) {
			SeleniumActions.seleniumClick(driver, Devices.breadCrumbBtn);
			System.out.println("Devices > Status > Options - Clicked on breadcrumb");
		} else {
			System.out.println("Devices > Status > Options - Searched device not found");
			throw new SkipException("***** Searched device - " + ipAddress + " not found *****");
		}

	}
	
	public void checkDeviceOnline(WebDriver driver, String ipAddress) throws InterruptedException {
		System.out.println("Attempting to check if device with IP address " + ipAddress + " is online");
		timer(driver);
		LandingPage.devicesTab(driver).click();
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(LandingPage.devicesStatus(driver)));
		LandingPage.devicesStatus(driver).click();
		System.out.println("Devices > Status > Options - Clicked on Status tab");
		timer(driver);
		SeleniumActions.seleniumSendKeys(driver, Devices.deviceStatusSearchBox, ipAddress);
		//check if device is online
		int timer = 0;
		int limit = 12;			//12 iterations of 5 seconds = 1 minute
		while(timer  <= limit) {
			System.out.println("Checking if device is online");
			String isOnline = SeleniumActions.seleniumGetText(driver, Devices.applianceTable);
			System.out.println("Is Online:" + isOnline);
			if(SeleniumActions.seleniumGetText(driver, Devices.applianceTable).contains("OnLine")) {
				System.out.println("Device is online");
				break;
			}else if(timer < limit) {
				timer++;
				System.out.println("Device is offline. Rechecking " + timer);
				driver.navigate().refresh();
				Thread.sleep(5000);
			}else if (timer == limit) {
				Assert.assertTrue(1 == 0, "Device is not online");
			}
		}
		System.out.println("Successfully checked if device is online");
	}


		
	public static int getWaitTime() {
		return waitTime;
	}
	
		
	public void cleanUpLogout() {
		try {
			Thread.sleep(1000);
			firedrive.get(url);
			Thread.sleep(2000);
			LandingPage.logoutDropdown(firedrive).click();
			Thread.sleep(2000);
			LandingPage.logoutbtn(firedrive).click();
			Thread.sleep(2000);
			firedrive.quit();
		} catch (Exception e) {
			// TODO: handle exception
			firedrive.quit();
		}
}
	
	
	
	public void enableNorthboundAPI(WebDriver driver) throws InterruptedException {
		navigateToSystemSettings(driver);
		Thread.sleep(2000);
		SeleniumActions.seleniumClick(driver, SystemAll.restApiTab);
		
		boolean isOff = SeleniumActions.seleniumIsDisplayed(driver, SystemAll.restApiSwitchOff);
		System.out.println("Is off:"  + isOff);
		if(isOff) {
			System.out.println("Northbound API is disabled. Enabling");
			SeleniumActions.seleniumClick(driver, SystemAll.restApiSwitchOff);
		}else {
			System.out.println("Northbound API is already enabled. Doing nothing");
		}
	}
		public void navigateToSystemSettings(WebDriver driver) throws InterruptedException {
			driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
			LandingPage.systemTab(driver).click();
			System.out.println("System Dropdown clicked");
			driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
			LandingPage.systemSettings(driver).click();
		
		}
		
		
		@AfterSuite
		public void closeoff() throws Exception {
			 String url = "https://"+boxillaManager+"/";
				System.setProperty("webdriver.gecko.driver", "C:\\Users\\\\abose\\eclipse-workspace\\geckodriver.exe");
				DesiredCapabilities caps = new DesiredCapabilities();
				caps.setCapability("acceptInsecureCerts", true);
				firedrive = new FirefoxDriver(caps);
				firedrive.get(url);
				boxillaElements.username(firedrive).sendKeys(boxillaUsername);
				boxillaElements.password(firedrive).sendKeys(boxillaPassword);
				boxillaElements.Login(firedrive).click();
				ConnectionPage.DeleteConnection(firedrive,devices);
				UserPage.DeleteUser(firedrive,RAusername);
				cleanUpLogout();
				firedrive.close();
		}
		
		
		
		
		
		@AfterMethod(alwaysRun = true)
		public void logout(ITestResult result) throws InterruptedException {
			System.out.println("In log out method");
			// Taking screen shot on failure
			//String url = "https://" + boxillaManager + "/";
			String results = "";
			//print result
			if(ITestResult.FAILURE == result.getStatus())
				results = "FAIL";
			
			if(ITestResult.SKIP == result.getStatus())
				results = "SKIP";
			
			if(ITestResult.SUCCESS == result.getStatus())
				results = "PASS";
			
			if (ITestResult.FAILURE == result.getStatus() || ITestResult.SKIP == result.getStatus()) {
				Throwable failReason = result.getThrowable();
				System.out.println("FAIL REASON:" + failReason.toString());
				String screenShotName = result.getName() + Utilities.getDateTimeStamp();
				Utilities.captureScreenShot(firedrive, screenShotName, result.getName());
				try {
					String gifName = "";
					if(ITestResult.SKIP == result.getStatus()) {
						gifName = result.getName() + "_skip";
					}else {
						gifName = result.getName() + "_fail";
					}
//					 List<JavaScriptError> jsErrors = JavaScriptError.readErrors(firedrive); 
//					System.out.println("************* JAVA SCRIPT ERRORS **************");
//					for(JavaScriptError e : jsErrors) {
//						System.out.println(e.getErrorMessage() + "Line Number:" + e.getLineNumber());
//						
//					}
					System.out.println("*************** END JAVA SRIPT ERRORS *************");
					
//					GifSequenceWriter.createGif(SeleniumActions.screenshotList, gifName);
//					clearGif();
//				Utilities.captureLog(boxillaManager, boxillaUsername, boxillaPassword,
//						 "./test-output/Screenshots/LOG_" + result.getName() + Utilities.getDateTimeStamp() + ".txt");
				}catch(Exception e) {
					System.out.println("Error when trying to capture log file. Catching error and continuing");
					e.printStackTrace();
				}
				
				//collectLogs(result);
			}
			try {

				firedrive.get(url);

				LandingPage.logoutDropdown(firedrive).click();

				LandingPage.logoutbtn(firedrive).click();
				firedrive.quit();
				long endTime = System.currentTimeMillis();
				long duration = endTime - startTime;
				//System.out.println("Regression running for : " + getTimeFromMilliSeconds(duration));
				long singleTestTime = endTime - splitTime;
				System.out.println(result.getName() + " took : " + getTimeFromMilliSeconds(singleTestTime));
				
			} catch (Exception e) {
				// TODO: handle exception
				firedrive.quit();
			}
			printTestDetails("FINISHING", result.getName(), results);
			System.out.println("Tests Completed:" + ++testCounter);
		}

		public void printSuitetDetails(boolean end) {
			String text = "";
			if(end) {
				text = "FINISHING";
			}else {
				text = "STARTING";
			}
			System.out.println(System.getProperty("line.separator"));
			System.out.println(System.getProperty("line.separator"));
			System.out.println("***************************************************************************************");
			System.out.println("*                                                                                     *");
			System.out.println("                         " + text + " SUITE " + this.getClass().getSimpleName());
			System.out.println("*                                                                                     *");
			System.out.println("***************************************************************************************");
			System.out.println(System.getProperty("line.separator"));
			System.out.println(System.getProperty("line.separator"));
		}

		/**
		 * Utility method used to bring the test details
		 * @param end 
		 * @param testName
		 * @param result
		 */
		public void printTestDetails(String end, String testName, String result) {
			System.out.println(System.getProperty("line.separator"));
			System.out.println(System.getProperty("line.separator"));
			System.out.println("***************************************************************************************");
			System.out.println("                         " + end + " TEST " + testName + ":" + result);
			System.out.println("***************************************************************************************");
			System.out.println(System.getProperty("line.separator"));
			System.out.println(System.getProperty("line.separator"));
		}
		
		public String getTimeFromMilliSeconds(long time) {
			return new SimpleDateFormat("mm:ss").format(new Date(time));
			
		}
	}
	

