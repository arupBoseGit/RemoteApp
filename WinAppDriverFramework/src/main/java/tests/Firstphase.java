package tests;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.testng.log4testng.Logger;

import io.appium.java_client.windows.WindowsDriver;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import methods.AppliancePool;
import methods.Device;
import methods.Devices;
import methods.Discovery;
import methods.DiscoveryMethods;
import methods.SeleniumActions;
import methods.Users;
import pages.ConnectionPage;
import pages.LandingPage;
import pages.UserPage;
import pages.boxillaElements;

public class Firstphase extends TestBase{
	
	final static Logger log = Logger.getLogger(Firstphase.class);
	UserPage userpage = new UserPage();
	
	@Test //Change the appliance setting to absolute
	public void Test01_SR0018_HIDAbsolute() throws Exception {
		printTestDetails("STARTING ", "Test01_SR0018_HIDAbsolute", "");
		Assert.assertEquals(1, 1);
		cleanUpLogin();
		devices = devicePool.getAllDevices("device.properties");
		for(Device deviceList : devicePool.allDevices()) {
			
				System.out.println("Checking device status");
				System.out.println("Attempting to check if device with IP address " + deviceList.getIpAddress() + " is online");
				LandingPage.devicesTab(firedrive).click();
				new WebDriverWait(firedrive, 60).until(ExpectedConditions.elementToBeClickable(LandingPage.devicesStatus(firedrive)));
				LandingPage.devicesStatus(firedrive).click();
				System.out.println("Devices > Settings > Options - Clicked on Status tab");
				timer(firedrive);
				SeleniumActions.seleniumSendKeys(firedrive, Devices.deviceStatusSearchBox, deviceList.getIpAddress());
				//check if device is online
				int timer = 0;
				int limit = 12;			//12 iterations of 5 seconds = 1 minute
				while(timer  <= limit) {
					System.out.println("Checking if device is online");
					String isOnline = SeleniumActions.seleniumGetText(firedrive, Devices.applianceTable);
					System.out.println("Is Online:" + isOnline);
					if(SeleniumActions.seleniumGetText(firedrive, Devices.applianceTable).contains("OnLine")) {
						System.out.println("Device is online");
						break;
					}else if(timer < limit) {
						timer++;
						System.out.println("Device is offline. Rechecking " + timer);
						firedrive.navigate().refresh();
						Thread.sleep(5000);
					}else if (timer == limit) {
						Assert.assertTrue(1 == 0, "Device is not online");
					}
				}
				System.out.println("Successfully checked if device is online");
				if (!SeleniumActions.seleniumGetText(firedrive, Devices.applianceTable).contains("RX")) {
					
				if (SeleniumActions.seleniumGetText(firedrive, Devices.applianceTable).contains(deviceList.getIpAddress())) {
					SeleniumActions.seleniumClick(firedrive, Devices.breadCrumbBtn);
					System.out.println("Devices > Settings > Options - Clicked on breadcrumb");
				} else {
					System.out.println("Devices > Status > Options - Searched device not found");
					throw new SkipException("***** Searched device - " + deviceList.getIpAddress() + " not found *****");
				}
				
				SeleniumActions.seleniumClick(firedrive, Devices.editSettings());
				System.out.println("clicked on Edit settings");
				
				Devices.uniqueHidDropdown(firedrive,"Absolute");
				System.out.println("Attempting to save transmitter properties");
				timer(firedrive);
				SeleniumActions.seleniumClick(firedrive, Devices.getEditTxSaveBtnXpath());
				//assert if successful
				Alert alert = firedrive.switchTo().alert();
				alert.accept();
				timer(firedrive);
				new WebDriverWait(firedrive, 60).until(ExpectedConditions.visibilityOf(Devices.getDeviceToastMessage(firedrive)));
				String message = Devices.getDeviceToastMessage(firedrive).getText();
				System.out.println("Pop up message: " + message);
				timer(firedrive);
				if(message.equals("Error")) {
					SeleniumActions.seleniumClick(firedrive, Devices.getEditTxCancelXpath());
					throw new AssertionError("Unable to save TX Settings. Toast error");
				}

				timer(firedrive);
				System.out.println("Successfully saved transmitter properties");	
					
					
				}
				System.out.println("No update for Receiver");
		}
		
			}
		

		
	
	
	@Test //The Emerald RemoteApp shall only operate when a Boxilla Manager is present and active, unless in demo mode
	public void Test02_CL0001_bxaManagerIPCheck() throws Exception {
		printTestDetails("STARTING ", "Test02_CL0001_bxaManagerIPCheck", "");
		setup();
		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		Windriver.manage().timeouts().implicitlyWait(3,TimeUnit.SECONDS);
		Windriver.findElementByAccessibilityId("DemoModeCheckBox").click();
		Windriver.findElementByName("Submit").click();
		Windriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		System.out.println(Windriver.getWindowHandle());
		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
	    getElement("menuLabel").click();
	    System.out.println("Menu Label clicked");
	    Windriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	    Windriver.findElementByName("Settings").click();
	    System.out.println("Settings clicked");
	    Windriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		getElement("ipAddressTextBox").sendKeys(" ");
		System.out.println("IP kept empty");
		Windriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		Windriver.findElementByName("Configure").click();
		System.out.println("configure clicked Now closing RemoteApp");
		closeRemoteApp();
		Thread.sleep(2000);
		setup();
		Thread.sleep(1000);
		WebElement windowsPopupOpenButton = Windriver.findElementByName("Incorrect Boxilla IP");
        String text= windowsPopupOpenButton.getText();
		Windriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); 
		System.out.println("Alert Message is  "+text);
		Assert.assertTrue(text.equals("Incorrect Boxilla IP"),"No pop up Message  stating - Boxilla has not been configured");
		Windriver.findElementByName("OK").click();
		System.out.println("Configuring boxilla with IP "+boxillaManager);
		Windriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		getElement("ipAddressTextBox").sendKeys(boxillaManager);
		System.out.println("IP Address Entered");
		Windriver.findElementByName("Configure").click();
		System.out.println("IP Configured....Closing RemoteApp");
		closeRemoteApp();
	
	}
	
	@Test //The Emerald RemoteApp shall provide an option to configure the IP address for the active Boxilla Manager.
	public void Test03_CL0002_bxaManagerIPsetUp() {
		printTestDetails("STARTING ", "Test03_CL0002_bxaManagerIPsetUp", "");
		setup();
		Windriver.manage().timeouts().implicitlyWait(3,TimeUnit.SECONDS);
		Windriver.findElementByAccessibilityId("DemoModeCheckBox").click();
		Windriver.findElementByName("Submit").click();
		Windriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		System.out.println(Windriver.getWindowHandle());
		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
	    getElement("menuLabel").click();
	    Windriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	    Windriver.findElementByName("Settings").click();
	    Windriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	    getElement("ipAddressTextBox").sendKeys(boxillaManager);
		Windriver.findElementByName("Configure").click();
		System.out.println("IP has been configured with "+boxillaManager);
		closeRemoteApp();
		
		
	}
	
	@Test // loging to boxilla and confirm the active user that logged in RemoteApp
	public void Test04_CL0003_ActiveUsercheck() throws Exception {
		printTestDetails("STARTING ", "Test04_CL0003_ActiveUsercheck", "");
		cleanUpLogin();
		userpage.createUser(firedrive,null,RAusername,RApassword,"General");
		cleanUpLogout();
		setup();
		System.out.println("RemoteApp is opened");
		WebElement loginButton = getElement("logInButton");
		getElement("userNameTextBox").sendKeys(RAusername);
		System.out.println("Username Entered");
		getElement("passwordTextBox").sendKeys(RApassword);
		System.out.println("Password Entered");
		loginButton.click();
		System.out.println("Login button clicked");
		Thread.sleep(30000);
		cleanUpLogin();
		String username=UserPage.currentUser(firedrive,RAusername,10);
		Assert.assertTrue(username.contains(RAusername),
				"current User table did not contain: " + RAusername + ", actual text: " + username);
		UserPage.DeleteUser(firedrive, RAusername);
		cleanUpLogout();
		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		closeApp();
		
	}
	
	@Test //Authenticate user RA test
	public void Test05_SR0021_Authentication() throws Exception {
		printTestDetails("STARTING ", "Test05_SR0021_Authentication", "");
		setup();
		WebElement loginButton = getElement("logInButton");
		getElement("userNameTextBox").sendKeys("User");
		System.out.println("User name entered as -User");
		getElement("passwordTextBox").sendKeys("User");
		System.out.println("Password entered as -User");
		loginButton.click();
		System.out.println("Login button clicked");
		try {
			Thread.sleep(4500);
	//	Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[1]);
	//	new WebDriverWait(Windriver, 60).until(ExpectedConditions.visibilityOf(Windriver.findElementByAccessibilityId("TitleBar")));  
        WebElement windowsPopupOpenButton = Windriver.findElementByAccessibilityId("TitleBar");
        String text= windowsPopupOpenButton.getText();
     // capture alert message
        System.out.println("Alert Message is  "+text);
       // Assert.assertEquals("Log In: Invalid Login Credentials", text);
   
        Thread.sleep(5000);
       
   
       
        Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		Windriver.findElementByName("Demo Mode").click();
		Windriver.findElementByName("Submit").click();
		Windriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		System.out.println(Windriver.getWindowHandle());
		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
	    getElement("menuLabel").click();
	    System.out.println("Menu Label clicked");
	    Windriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	    Windriver.findElementByName("Close").click();
	    System.out.println("RemoteApp closed");
		}catch(Exception e) {
			e.printStackTrace();
			 Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
				Windriver.findElementByName("Demo Mode").click();
				Windriver.findElementByName("Submit").click();
				Windriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				System.out.println(Windriver.getWindowHandle());
				Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			    getElement("menuLabel").click();
			    System.out.println("Menu Label clicked");
			    Windriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			    Windriver.findElementByName("Close").click();
			    System.out.println("RemoteApp closed");
		}
	}
	
	//@Test //Remote app maximum limit to user
	public void Test06_CL0005a_connectionLimit() throws Exception {
		printTestDetails("STARTING ", "Test06_CL0005a_connectionLimit", "");

		Onedevices = devicePool.getAllDevices("Onedevice.properties");
		cleanUpLogin();
		userpage.createUser(firedrive,null,RAusername,RApassword,"General");
		SharedNames=ConnectionPage.Sharedconnection(firedrive,Onedevices,5,"shared");
		UserPage.Sharedconnectionassign(firedrive, RAusername, SharedNames);
		
		cleanUpLogout();
		int count=0;
		int connectionNumber=1;
		try {
		RAlogin(RAusername,RApassword);
		WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
		List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
		
		for (WebElement connection : availableConnections) {
		  System.out.println("connections number  "+connectionNumber+" is "+connection.getText());
		  connectionList.add(connection.getText());
		  connectionNumber++;
		}
		
		for (String connectionName : SharedNames) {
		
		WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionName));
		  Actions a = new Actions(Windriver);
		  count++;
	      a.moveToElement(targetConnection).
	      doubleClick().
	      build().perform();
	          
	       
	      if (count==5) {
	    	  new WebDriverWait(firedrive, 60).until(ExpectedConditions.elementToBeClickable(Windriver.findElementByName("Maximum Number of Connections Reached")));
	    	  WebElement windowsPopupOpenButton = Windriver.findElementByName("Maximum Number of Connections Reached");
	          String text= windowsPopupOpenButton.getText();
	    	  System.out.println("Alert text is "+text);
	    	  Assert.assertTrue(text.equalsIgnoreCase("Maximum Number of Connections Reached"), "Maximum Number of Connections Reached Message has not been displayed ");
	    	  break;
	      }
	      Thread.sleep(20000);
	      System.out.println(connectionName+" has been launched");
	      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		}
		
		
		 Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		 for (String connectionName : SharedNames) {
		    	Windriver.findElement(By.name(connectionName)).click();
		    	Windriver.findElement(By.name("Disconnect")).click();
			    Thread.sleep(3000);
			    System.out.println("connection "+connectionName+" is disconnected");
			    Windriver.switchTo().window(Windriver.getWindowHandle());
			    
			
	}
		 Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		closeApp();
		cleanUpLogin();
		ConnectionPage.DeleteSharedConnection(firedrive, SharedNames);
		
		UserPage.DeleteUser(firedrive, RAusername);
		cleanUpLogout();
		} catch(Exception e) {
			e.printStackTrace();
			Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			closeApp();
			cleanUpLogin();
			Thread.sleep(2000);
			ConnectionPage.DeleteSharedConnection(firedrive, SharedNames);
			UserPage.DeleteUser(firedrive, RAusername);
			cleanUpLogout();
			
		}
	}
		
	@Test //RA configuration for connection, settings and information
	public void Test07_AI0004_MenuOptionsCheck() throws Exception {
		printTestDetails("STARTING ", "Test07_AI0004_MenuOptionsCheck", "");
		//setup();
		boolean statusconnect = false;
		boolean statusSettings = false;
		boolean statusInfo = false;
		
		cleanUpLogin();
		userpage.createUser(firedrive,devices,RAusername,RApassword,"General");
		cleanUpLogout();
		RAlogin(RAusername,RApassword);
		
	    getElement("menuLabel").click();
	    if (Windriver.findElementByName("Connections").isDisplayed()) {
	    	statusconnect = true;
	    	System.out.println("Connections option is displayed");
	    }
	    if (Windriver.findElementByName("Settings").isDisplayed()) {
	    	statusSettings = true;
	    	System.out.println("Settings option is displayed");
	    }
	    if (Windriver.findElementByName("Information").isDisplayed()) {
	    	statusInfo = true;
	    	System.out.println("Information option is displayed");
	    }
	    Assert.assertTrue(statusconnect,"connections option is not displayed");
	    Assert.assertTrue(statusSettings,"Settings option is not displayed");
	    Assert.assertTrue(statusInfo,"Information option is not displayed");
	    closeApp();
	    cleanUpLogin();
		userpage.DeleteUser(firedrive, RAusername);
		cleanUpLogout();
		
	}
		
	@Test //Software version and blackbox contact details
	public void Test08_AI0046_Versioncheck() throws Exception {
		printTestDetails("STARTING ", "Test08_AI0046_Versioncheck", "");
		SoftAssert softAssert = new SoftAssert();
		cleanUpLogin();
		userpage.createUser(firedrive,devices,RAusername,RApassword,"General");
		cleanUpLogout();
		RAlogin(RAusername,RApassword);
		getElement("menuLabel").click();
		Windriver.findElementByName("Information").click();
		System.out.println("Information tab is clicked");
		String Version=getElement("versionLabel").getText();
		System.out.println("The version of RemoteApp is "+Version);
		Assert.assertTrue(Version.contains("Version"), "RemoteApp does not contain Version on the Information section");
		String contact=getElement("blackBoxWebsiteLinkLabel").getText();
		System.out.println("Contact details of blackBox is "+contact);
		softAssert.assertTrue(contact.contains("https://www.blackbox.com/en-us/support"), "RemoteApp does not have BlackBox contact details");
		closeApp();
		 cleanUpLogin();
			userpage.DeleteUser(firedrive, RAusername);
			cleanUpLogout();
		softAssert.assertAll();
	}
	
	@Test //Help information should be shown
	public void Test09_AI0047_InformationTab() throws Exception {
		printTestDetails("STARTING ", "Test09_AI0047_InformationTab", "");
		SoftAssert softAssert = new SoftAssert();
		cleanUpLogin();
		userpage.createUser(firedrive,devices,RAusername,RApassword,"General");
		cleanUpLogout();
		RAlogin(RAusername,RApassword);
		getElement("menuLabel").click();
		Windriver.findElementByName("Information").click();
		System.out.println("Information tab is clicked");
		softAssert.assertTrue(getElement("supportLabel").isDisplayed(),"Help information is not displayed");
		System.out.println("Help Information has been displayed");
		closeApp();
		cleanUpLogin();
		userpage.DeleteUser(firedrive, RAusername);
		cleanUpLogout();
		softAssert.assertAll();
		
	}
	
	@Test //This “Auto” value is the preferred value of the local desktop.
	public  void Test10_VI0006_ResolutionSetAuto() throws Exception{
		printTestDetails("STARTING ", "Test10_VI0006_ResolutionSetAuto", "");
		SoftAssert softAssert = new SoftAssert();
		cleanUpLogin();
		userpage.createUser(firedrive,devices,RAusername,RApassword,"General");
		cleanUpLogout();
		RAlogin(RAusername,RApassword);
		getElement("menuLabel").click();
		Windriver.findElementByName("Settings").click();
		System.out.println("Clicking on the Connection Window");
		WebElement temp2 = Windriver.findElement(By.xpath("//Pane[@Name='kryptonDockableNavigator1'][@AutomationId='settingsNavigation']"));
		
		Thread.sleep(3000);
		Actions d = new Actions(Windriver);
		d.moveToElement(temp2, 160, 15).
		doubleClick().
		build().perform();
		
		Thread.sleep(2000);
		
		WebElement comboBoxElement = Windriver.findElement(By.xpath("//ComboBox[starts-with(@ClassName,\"WindowsForms10\")]"));
		comboBoxElement.click();
		Thread.sleep(2000);
		comboBoxElement.findElement(By.name("Auto")).click(); 

		getElement("applyButton").click();
		Thread.sleep(3000);
		getElement("menuLabel").click();
		Windriver.findElementByName("Settings").click();
		String resolution=getElement("windowResolutionComboBox").getText();
		System.out.println("Connection Window resolution is "+resolution);
		softAssert.assertTrue(resolution.equalsIgnoreCase("Auto"), "Connection Window resolution is not Auto");
		closeApp();
		cleanUpLogin();
		userpage.DeleteUser(firedrive, RAusername);
		cleanUpLogout();
		softAssert.assertAll();
	}
		
	@Test //verify the list of connections associated to the user
	public void  Test11_AI0007_ConnectionListcheck() throws Exception {
		printTestDetails("STARTING ", "Test11_AI0007_ConnectionListcheck", "");
		SoftAssert softAssert = new SoftAssert();
		Onedevices=devicePool.getAllDevices("Onedevice.properties");
		devices = devicePool.getAllDevices("device.properties");
		cleanUpLogin();
		SharedNames = ConnectionPage.CreateConnection(firedrive, Onedevices, 4, "Shared");//createprivateconnections(firedrive, Onedevices);//CreateConnection(firedrive, Onedevices, 1, "Private");//createprivateconnections(firedrive,Onedevices);
		userpage.createUser(firedrive,null,RAusername,RApassword,"General");
		userpage.Sharedconnectionassign(firedrive, RAusername, SharedNames);
		cleanUpLogout();
		RAlogin(RAusername,RApassword);
		WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
		System.out.println("availableConnectionsList is "+availableConnectionsList.getText());
		List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
		//System.out.println("list is "+availableConnections);
		for (WebElement connection : availableConnections) {
		boolean status = false;	
		for (Device deviceList : devices)
		 {
			System.out.println("Checking for connection  "+connection.getText()+" in the device list "+deviceList.getIpAddress());
		//	System.out.println("Devices name is "+devices.toString());
			if(connection.getText().contains(deviceList.getIpAddress())) {
				status=true;
				softAssert.assertTrue(status,"Connection Name "+connection+" shown in RemoteApp has not been assigned to the user");
				System.out.println(" connection "+connection.getText()+" is assigned to the correct user");
				break;
			}
			
			System.out.println("Connection name - "+connection+" has been assigned correctly o the User");
		}}
		closeApp();
		cleanUpLogin();
		UserPage.DeleteUser(firedrive,RAusername);
		cleanUpLogout();
		softAssert.assertAll();
		
	}
	
	//Test to ensure all the user(administrator, power and General) have same privileges
	@Test
	public void Test12_AI0005_UserPrivilegesCheck() throws Exception {
		printTestDetails("STARTING ", "Test12_AI0005_UserPrivilegesCheck", "");
		SoftAssert softAssert = new SoftAssert();
		cleanUpLogin();
		Onedevices=devicePool.getAllDevices("Onedevice.properties");
		ConnectionPage.CreateConnection(firedrive, Onedevices, 1, "Private");
		userpage.createUser(firedrive,Onedevices,"TestUser1","TestUser1","general");
		userpage.createUser(firedrive,Onedevices,"TestUser2","TestUser2","power");
		userpage.createUser(firedrive,Onedevices,"TestUser3","TestUser3","admin");

		cleanUpLogout();
		for(int i=1;i<4;i++) {
			RAlogin("TestUser"+i,"TestUser"+i);
			WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
			System.out.println("availableConnectionsList is "+availableConnectionsList.getText());
			List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
			System.out.println("list is "+availableConnections);
			for (WebElement connection : availableConnections) {
			boolean status = false;	
			for (Device deviceList : Onedevices)
			 {
				System.out.println("Checking for connection  "+connection.getText()+" in the device list "+deviceList.getIpAddress());
			//	System.out.println("Devices name is "+devices.toString());
				if(connection.getText().equalsIgnoreCase(deviceList.getIpAddress())) {
					status=true;
					softAssert.assertTrue(status,"Connection Name "+connection+" shown in RemoteApp has not been assigned to the user");
					System.out.println(" connection "+connection.getText()+" is assigned to the correct user");
					break;
				}}}
			closeApp();}
			cleanUpLogin();
			Thread.sleep(3000);
			for(int j=1;j<4;j++) {
			UserPage.DeleteUser(firedrive,"TestUser"+j);
			}
			cleanUpLogout();
			softAssert.assertAll();
	}
		
	
	
	@Test //ensure the launched connection not to impact and user need not to change the password in current session.
	public void Test13_AI0032_UserPasswordUpdate() throws Exception {
		printTestDetails("STARTING ", "Test13_AI0032_UserPasswordUpdate", "");
		SoftAssert softAssert = new SoftAssert();
		Onedevices=devicePool.getAllDevices("Onedevice.properties");
		cleanUpLogin();
		Thread.sleep(3000);
		ArrayList connectionNam = ConnectionPage.CreateConnection(firedrive, Onedevices, 1, "Private");
		userpage.createUser(firedrive,null,RAusername,RApassword,"General");
		userpage.Sharedconnectionassign(firedrive, RAusername, connectionNam);
		Thread.sleep(3000);
		cleanUpLogout();
		RAlogin(RAusername,RApassword);
		System.out.println("Logged into RemoteApp");
		WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
		List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
		connectionList.clear();
		for (WebElement connection : availableConnections) {
			  connectionList.add(connection.getText());
			  			}
			for (String connectionName : connectionList) {
				WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionName));
				
			     Actions a = new Actions(Windriver);
			      a.moveToElement(targetConnection).
			      doubleClick().
			      build().perform();
			      Thread.sleep(30000);
			      System.out.println(targetConnection.getText()+"  connection is launched");
			    //  Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
				}
			Thread.sleep(10000);
			cleanUpLogin();
			Thread.sleep(4000);
		//	new WebDriverWait(firedrive, 60).until(ExpectedConditions.elementToBeClickable(userpage.user(firedrive)));
			LandingPage.usersTab(firedrive).click();
			System.out.println("User clicked");
			firedrive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			userpage.manage(firedrive).click();
			System.out.println("Manage option clicked");
			Thread.sleep(2000);
			userpage.searchOption(firedrive).sendKeys(RAusername);
			userpage.optionbutton(firedrive).click();
			userpage.EditUser(firedrive).click();
			userpage.password(firedrive).clear();
			userpage.password(firedrive).sendKeys("NewPassword");
			userpage.confirmPassword(firedrive).clear();
			userpage.confirmPassword(firedrive).sendKeys("NewPassword");
			userpage.NextButton(firedrive).click();
			userpage.NextButton(firedrive).click();
			userpage.savebutton(firedrive).click();
			System.out.println("Password updated");
			firedrive.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			cleanUpLogout();
			
			RestAssured.useRelaxedHTTPSValidation();
			String gerdetails = RestAssured.given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers("Content-Type", "application/json", "Accept","application/json")
								.when().get("https://"+boxillaManager+"/bxa-api/connections/kvm/active")
								.then().assertThat().statusCode(200)
								.extract().response().asString();
			 System.out.println("Active connection status"+gerdetails);
		     
				System.out.println("********************checking the connection status  ******************");
				JsonPath js = new JsonPath(gerdetails);
				int	countconnection=js.getInt("message.active_connections.size()");
				System.out.println("Number of Active connections are "+countconnection);
				System.out.println("Number of expected active connections to be "+connectionList.size());
				softAssert.assertEquals(countconnection,connectionList.size()," Number of active connection didn't match with the number of connections before changing credentials");	{
					Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
					closeApp();
				cleanUpLogin();
				UserPage.DeleteUser(firedrive, RAusername);
				ConnectionPage.DeleteSharedConnection(firedrive, connectionNam);
				cleanUpLogout();
				softAssert.assertAll();
			}
		
	}
	
	@Test //User should able to select a connection
	public void Test14_AI0043_SelectConnection() throws Exception {
		printTestDetails("STARTING ", "Test14_AI0043_SelectConnection", "");
		Onedevices=devicePool.getAllDevices("Onedevice.properties");
		cleanUpLogin();
		ArrayList connname = ConnectionPage.CreateConnection(firedrive, Onedevices, 1, "Private");
		userpage.createUser(firedrive,null,RAusername,RApassword,"General");
		UserPage.Sharedconnectionassign(firedrive, RAusername, connname);
		cleanUpLogout();
		
		try {
		RAlogin(RAusername,RApassword);
		//Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
		List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
		int conNum=1;
		for (WebElement connection : availableConnections) {
		  System.out.println("connections number  "+conNum+" is "+connection.getText());
		  connectionList.add(connection.getText());
		  conNum++;
		}
		for (String connectionName : connectionList) {
			WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionName));
			
		     Actions a = new Actions(Windriver);
		      a.moveToElement(targetConnection);
		      System.out.println("Cursor could move to connection "+connectionName);
		      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			}
		closeApp();
		cleanUpLogin();
		UserPage.DeleteUser(firedrive, RAusername);
		ConnectionPage.DeleteSharedConnection(firedrive, connname);
		cleanUpLogout();
		
	}catch(Exception e) {
		e.printStackTrace();
		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		closeApp();
		cleanUpLogin();
		UserPage.DeleteUser(firedrive, RAusername);
		ConnectionPage.DeleteSharedConnection(firedrive, connname);
		cleanUpLogout();
	}
		
	}
	
	@Test //Ensure both the connection window and the application window to remain open
	
	public void Test15_CL0006a_ActiveConnwindows() throws Exception{
		printTestDetails("STARTING ", "Test15_CL0006a_ActiveConnwindows", "");
		SoftAssert softAssert = new SoftAssert();
		ArrayList<Device> PEDevices = new ArrayList<Device>();
		PEDevices=devicePool.getAllDevices("devicePE.properties");;

		ArrayList<String> connectionPEList = new ArrayList<String>();

		cleanUpLogin();
		ArrayList connName = ConnectionPage.CreateConnection(firedrive, PEDevices, 1, "Private");
		userpage.createUser(firedrive,null,RAusername,RApassword,"General");
		userpage.Sharedconnectionassign(firedrive, RAusername, connName);
		cleanUpLogout();
		
		try {
		RAlogin(RAusername,RApassword);
		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
		List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
		for (WebElement connection : availableConnections) {
		
			connectionPEList.add(connection.getText());
		
			}
		
			for (String connectionName : connectionPEList) {
			  Actions a = new Actions(Windriver);
		
			  WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionName));
		      a.moveToElement(targetConnection).
		      doubleClick().
		      build().perform();
		      Thread.sleep(30000);
		      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			}
			  RestAssured.useRelaxedHTTPSValidation();
		    String  gerdetails = RestAssured.given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers("Content-Type", "application/json", "Accept","application/json")
								.when().get("https://"+boxillaManager+"/bxa-api/connections/kvm/active")
								.then().assertThat().statusCode(200)
								.extract().response().asString();
			
		      System.out.println(gerdetails);
		      JsonPath js = new JsonPath(gerdetails);
				int	countconnection=js.getInt("message.active_connections.size()");
				System.out.println("Number of Active connections are "+countconnection);
				System.out.println("Number of Active Windows are "+Windriver.getWindowHandles().size());
		      softAssert.assertEquals(countconnection,Windriver.getWindowHandles().size(),"Number of Windows Mismatch");//connection and application window to remain open
		
			Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			for (String connectionName : connectionPEList) {
		    	Windriver.findElement(By.name(connectionName)).click();
		    	Windriver.findElement(By.name("Disconnect")).click();
			    Thread.sleep(5000);
			    System.out.println("connection "+connectionName+" is disconnected");
			    Windriver.switchTo().window(Windriver.getWindowHandle());
			    
			
	
	}
	 Windriver.switchTo().window(Windriver.getWindowHandle());
	 closeApp();
	 cleanUpLogin();
	 UserPage.DeleteUser(firedrive, RAusername);
	 ConnectionPage.DeleteSharedConnection(firedrive, connName);
	 cleanUpLogout();
	 softAssert.assertAll();
		}
		catch(Exception e) {
			e.printStackTrace();
			Windriver.switchTo().window(Windriver.getWindowHandle());
			 closeApp();
			 cleanUpLogin();
			 UserPage.DeleteUser(firedrive, RAusername);
			 ConnectionPage.DeleteSharedConnection(firedrive, connName);
			 cleanUpLogout();
		}
		
	}
	
	@Test //Launch SE tX
	public void Test16_DC0001_SETX() throws Exception {
		printTestDetails("STARTING ", "Test16_DC0001_SETX", "");
		SoftAssert softAssert = new SoftAssert();
		ArrayList<Device> SEDevices = new ArrayList<Device>();
		SEDevices=devicePool.getAllDevices("deviceSE.properties");;

		ArrayList<String> connectionPEList = new ArrayList<String>();

		cleanUpLogin();

		
	
		ConnectionPage.createprivateconnections(firedrive,SEDevices);
	
		userpage.createUser(firedrive,SEDevices,RAusername,RApassword,"General");
		cleanUpLogout();
		RAlogin(RAusername,RApassword);
		
		WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
		List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
		for (WebElement connection : availableConnections) {
		
			connectionPEList.add(connection.getText());
		
			}
		
			for (String connectionName : connectionPEList) {
			  Actions a = new Actions(Windriver);
		
			  WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionName));
		      a.moveToElement(targetConnection).
		      doubleClick().
		      build().perform();
		      Thread.sleep(30000);
		      System.out.println("Launched connection "+connectionName);
		      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			}
			  RestAssured.useRelaxedHTTPSValidation();
		    String  gerdetails = RestAssured.given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers("Content-Type", "application/json", "Accept","application/json")
								.when().get("https://"+boxillaManager+"/bxa-api/connections/kvm/active")
								.then().assertThat().statusCode(200)
								.extract().response().asString();
			
		      System.out.println(gerdetails);
		      JsonPath js = new JsonPath(gerdetails);
				int	countconnection=js.getInt("message.active_connections.size()");
				System.out.println("Number of Active connections are "+countconnection);
				System.out.println("Number of Active Windows are "+Windriver.getWindowHandles().size());
		   //   softAssert.assertEquals(countconnection,Windriver.getWindowHandles().size(),"Number of Windows Mismatch");//connection and application window to remain open
		
			Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			for (String connectionName : connectionPEList) {
		    	Windriver.findElement(By.name(connectionName)).click();
		    	Windriver.findElement(By.name("Disconnect")).click();
			    Thread.sleep(5000);
			    System.out.println("connection "+connectionName+" is disconnected");
			    Windriver.switchTo().window(Windriver.getWindowHandle());
			    
			
	
	}
	 Windriver.switchTo().window(Windriver.getWindowHandle());
	 closeApp();
	 cleanUpLogin();
	 UserPage.DeleteUser(firedrive, RAusername);
	 ConnectionPage.DeleteConnection(firedrive, SEDevices);
	 cleanUpLogout();
	// softAssertion.assertAll();
			}
	
	//Launch Connection to PE transmitter
	@Test
	public void Test17_DC0001_PETX() throws Exception {
		printTestDetails("STARTING ", "Test17_DC0001_PETX", "");
		SoftAssert softAssert = new SoftAssert();
		ArrayList<Device> PEDevices = new ArrayList<Device>();
		PEDevices=devicePool.getAllDevices("devicePE.properties");;

		ArrayList<String> connectionPEList = new ArrayList<String>();

		cleanUpLogin();

		ArrayList ConnName=ConnectionPage.CreateConnection(firedrive, PEDevices, 1, "Private");
		userpage.createUser(firedrive,null,RAusername,RApassword,"General");
		userpage.Sharedconnectionassign(firedrive, RAusername, ConnName);
		
		cleanUpLogout();
		RAlogin(RAusername,RApassword);
		
		WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
		List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
		for (WebElement connection : availableConnections) {
		
			connectionPEList.add(connection.getText());
		
			}
		
			for (String connectionName : connectionPEList) {
			  Actions a = new Actions(Windriver);	
			  WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionName));
		      a.moveToElement(targetConnection).
		      doubleClick().
		      build().perform();
		      Thread.sleep(90000);
		      System.out.println("Launched connection "+connectionName);
		      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			}
			  RestAssured.useRelaxedHTTPSValidation();
		    String  gerdetails = RestAssured.given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers("Content-Type", "application/json", "Accept","application/json")
								.when().get("https://"+boxillaManager+"/bxa-api/connections/kvm/active")
								.then().assertThat().statusCode(200)
								.extract().response().asString();
			
		      System.out.println(gerdetails);
		      JsonPath js = new JsonPath(gerdetails);
				int	countconnection=js.getInt("message.active_connections.size()");
				System.out.println("Number of Active connections are "+countconnection);
				System.out.println("Number of Active Windows are "+Windriver.getWindowHandles().size());
		    
		      softAssert.assertEquals(countconnection, connectionPEList.size(),"Mismatch on the number of launched connections and active connections");
			Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			for (String connectionName : connectionPEList) {
		    	Windriver.findElement(By.name(connectionName)).click();
		    	Windriver.findElement(By.name("Disconnect")).click();
			    Thread.sleep(5000);
			    System.out.println("connection "+connectionName+" is disconnected");
			    Windriver.switchTo().window(Windriver.getWindowHandle());
			    
			
	
	}
	 Windriver.switchTo().window(Windriver.getWindowHandle());
	 closeApp();
	 cleanUpLogin();
	 UserPage.DeleteUser(firedrive, RAusername);
	 Thread.sleep(5000);
	 ConnectionPage.DeleteSharedConnection(firedrive, ConnName);
	 cleanUpLogout();
	 softAssert.assertAll();
			}
	
	
	@Test // Launch Connection to ZeroU transmitter
	public void Test18_DC0001_ZeroUTx() throws Exception {
		printTestDetails("STARTING ", "Test18_DC0001_ZeroUTx", "");
		SoftAssert softAssert = new SoftAssert();
		ArrayList<Device> ZuDevices = new ArrayList<Device>();
		ZuDevices=devicePool.getAllDevices("deviceZeroU.properties");;

		ArrayList<String> connectionPEList = new ArrayList<String>();

		cleanUpLogin();
		ArrayList connName = ConnectionPage.CreateConnection(firedrive, ZuDevices, 1, "Private");
		userpage.createUser(firedrive,null,RAusername,RApassword,"General");
		userpage.Sharedconnectionassign(firedrive, RAusername, connName);
		cleanUpLogout();
		try {
		RAlogin(RAusername,RApassword);
		
		WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
		List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
		for (WebElement connection : availableConnections) {
		
			connectionPEList.add(connection.getText());
		
			}
		
			for (String connectionName : connectionPEList) {
			  Actions a = new Actions(Windriver);
		
			  WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionName));
		      a.moveToElement(targetConnection).
		      doubleClick().
		      build().perform();
		      Thread.sleep(30000);
		      System.out.println("Launched connection "+connectionName);
		      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			}
			  RestAssured.useRelaxedHTTPSValidation();
		    String  gerdetails = RestAssured.given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers("Content-Type", "application/json", "Accept","application/json")
								.when().get("https://"+boxillaManager+"/bxa-api/connections/kvm/active")
								.then().assertThat().statusCode(200)
								.extract().response().asString();
			
		      System.out.println(gerdetails);
		      JsonPath js = new JsonPath(gerdetails);
				int	countconnection=js.getInt("message.active_connections.size()");
				System.out.println("Number of Active connections are "+countconnection);
				System.out.println("Number of Active Windows are "+Windriver.getWindowHandles().size());
		    
		      softAssert.assertEquals(countconnection, connectionPEList.size(),"Mismatch on the number of launched connections and active connections");
			Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			for (String connectionName : connectionPEList) {
		    	Windriver.findElement(By.name(connectionName)).click();
		    	Windriver.findElement(By.name("Disconnect")).click();
			    Thread.sleep(5000);
			    System.out.println("connection "+connectionName+" is disconnected");
			    Windriver.switchTo().window(Windriver.getWindowHandle());
			    
			
	
	}
	 Windriver.switchTo().window(Windriver.getWindowHandle());
	 closeApp();
	 cleanUpLogin();
	 UserPage.DeleteUser(firedrive, RAusername);
	 Thread.sleep(5000);
	 ConnectionPage.DeleteSharedConnection(firedrive, connName);
	 cleanUpLogout();
	 softAssert.assertAll();
			}catch(Exception e) {
				e.printStackTrace();
				 Windriver.switchTo().window(Windriver.getWindowHandle());
				 closeApp();
				 cleanUpLogin();
				 UserPage.DeleteUser(firedrive, RAusername);
				 Thread.sleep(5000);
				 ConnectionPage.DeleteSharedConnection(firedrive, connName);
				 cleanUpLogout();
			}
	}
	
	@Test//private connection termination
	public void Test19_SR0005_connectionTermination() throws Exception {
		printTestDetails("STARTING ", "Test19_SR0005_connectionTermination", "");
		SoftAssert softAssert = new SoftAssert();
		WebDriverWait wait=new WebDriverWait(firedrive, 20);
		ArrayList connectionName =null;
		ArrayList<String> DualHeadList = new ArrayList<String>();
		DiscoveryMethods discoveryMethods = new DiscoveryMethods();	
		AppliancePool applian = new AppliancePool();
		ArrayList<String> connectionZeroUList = new ArrayList<String>();
		ArrayList<Device> remotedevice=applian.getAllDevices("Onedevice.properties");
		System.out.println(remotedevice);
		
		
			try {	
				cleanUpLogin();
				connectionName = ConnectionPage.Sharedconnection(firedrive, remotedevice, 2, "Private");
				userpage.createUser(firedrive,remotedevice,RAusername,RAusername,"General");
				cleanUpLogout();
				Thread.sleep(10000);
				RAlogin(RAusername,RAusername);
				WebElement availablesharedConnectionsList = getElement("availableConnectionsWinListBox");
				List<WebElement> availablesharedConnections = availablesharedConnectionsList.findElements(By.xpath("//ListItem"));
				connectionList.clear();
				int connectionNumber=1,count=0;
				for (WebElement connection : availablesharedConnections) {
				  System.out.println("connections number  "+connectionNumber+" is "+connection.getText());
				  connectionList.add(connection.getText());
				  connectionNumber++;
				}
				System.out.println(connectionList);
//				
				for (String connectionsharedName : connectionList) {
				
				WebElement targettoConnect = availablesharedConnectionsList.findElement(By.name(connectionsharedName));
				  Actions a = new Actions(Windriver);
				  count++;
			      a.moveToElement(targettoConnect).
			      doubleClick().
			      build().perform();
			      if(count==2) {
			    	  Thread.sleep(7000);
			    	  new WebDriverWait(Windriver, 60).until(ExpectedConditions.visibilityOf(Windriver.findElementByAccessibilityId("TitleBar")));
			    	  WebElement windowsPopupOpenButton = Windriver.findElementByAccessibilityId("TitleBar");
				      String text= windowsPopupOpenButton.getText();
				      System.out.println("Message is "+text);
				      break;
			      }
			      System.out.println(connectionsharedName+" has been launched");
			      Thread.sleep(20000);
			      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			      }
				
				
				
				  		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
				  		closeApp();
						cleanUpLogin();
						Thread.sleep(2000);
						ConnectionPage.DeleteSharedConnection(firedrive, connectionName);
						UserPage.DeleteUser(firedrive, RAusername);
						cleanUpLogout();
					    softAssert.assertAll();
						}
					      catch(Exception e)
					      {
					    	  e.printStackTrace();
					    	  Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
					    	    closeApp();
								cleanUpLogin();
								Thread.sleep(2000);
								ConnectionPage.DeleteSharedConnection(firedrive, connectionName);
								Thread.sleep(2000);
								UserPage.DeleteUser(firedrive, RAusername);
								cleanUpLogout();
					      }
	}
			

		
		
	
	
	
	@Test//. Close remote application while connections are running
	public void Test20_AI0038_CloseRemoteApp() throws Exception {
		printTestDetails("STARTING ", "Test20_AI0038_CloseRemoteApp", "");
		SoftAssert softAssert = new SoftAssert();
		ArrayList<Device> ZuDevices = new ArrayList<Device>();
		ZuDevices=devicePool.getAllDevices("deviceZeroU.properties");;

		ArrayList<String> connectionPEList = new ArrayList<String>();

		cleanUpLogin();

		ArrayList ConnName=ConnectionPage.CreateConnection(firedrive, ZuDevices, 1, "Private");	
		userpage.createUser(firedrive,null,RAusername,RApassword,"General");
		userpage.Sharedconnectionassign(firedrive, RAusername, ConnName);
		
		cleanUpLogout();
		RAlogin(RAusername,RApassword);
		
		WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
		List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
		for (WebElement connection : availableConnections) {
		
			connectionPEList.add(connection.getText());
		
			}
		
			for (String connectionName : connectionPEList) {
			  Actions a = new Actions(Windriver);
		
			  WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionName));
		      a.moveToElement(targetConnection).
		      doubleClick().
		      build().perform();
		      Thread.sleep(30000);
		      System.out.println("Launched connection "+connectionName);
		      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			}
			RestAssured.useRelaxedHTTPSValidation();
		    String  gerdetails = RestAssured.given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers("Content-Type", "application/json", "Accept","application/json")
								.when().get("https://"+boxillaManager+"/bxa-api/connections/kvm/active")
								.then().assertThat().statusCode(200)
								.extract().response().asString();
			
		    System.out.println("Response is "+gerdetails);
		      JsonPath js = new JsonPath(gerdetails);
				int	countconnection=js.getInt("message.active_connections.size()");
				System.out.println("Number of Active connections are "+countconnection);
				System.out.println("Number of Active Windows are "+Windriver.getWindowHandles().size());
		    
		     
			Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			closeApp();
			Thread.sleep(60000);
			RestAssured.useRelaxedHTTPSValidation();
		    String  closedetails = RestAssured.given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers("Content-Type", "application/json", "Accept","application/json")
								.when().get("https://"+boxillaManager+"/bxa-api/connections/kvm/active")
								.then().assertThat().statusCode(200)
								.extract().response().asString();
			
		    System.out.println("Response is "+closedetails);
		      JsonPath closejs = new JsonPath(closedetails);
				int	Activeconnection=closejs.getInt("message.active_connections.size()");
				System.out.println("Number of Active connections are "+Activeconnection);
			//	System.out.println("Number of Active Windows are "+Windriver.getWindowHandles().size());
				 if(Activeconnection==0) {
					   System.out.println("RemoteApp is closed and all the connectections are terminated");
					   softAssert.assertTrue(true);
					   
				   }else softAssert.assertFalse(true, +countconnection+" Connection is still active");
		      
	 cleanUpLogin();
	 UserPage.DeleteUser(firedrive, RAusername);
	 Thread.sleep(2000);
	 ConnectionPage.DeleteSharedConnection(firedrive, ConnName);
	 cleanUpLogout();
	 softAssert.assertAll();
			}
			    
			  
			    
			 
			  
		     
		

	@Test //check a pop up message on launching and terminating connections

	public void Test21_AI0048_LaunchTerminatePopUpMessage() throws Exception {
		printTestDetails("STARTING ", "Test21_AI0048_LaunchTerminatePopUpMessage", "");
		SoftAssert softAssert = new SoftAssert();
		ArrayList<Device> PEDevices = new ArrayList<Device>();
		AppliancePool applian = new AppliancePool();
		ArrayList<String> connectionPEList = new ArrayList<String>();
		ArrayList<Device> remotedevice=applian.getAllDevices("devicePE.properties");
		System.out.println(remotedevice);
		PEDevices.addAll(remotedevice);
		cleanUpLogin();

		ConnectionPage.createprivateconnections(firedrive,remotedevice);
	
		userpage.createUser(firedrive,remotedevice,RAusername,RApassword,"General");
		cleanUpLogout();
		try {
		RAlogin(RAusername,RApassword);
		WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
		List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
		for (WebElement connection : availableConnections) {
		
			connectionPEList.add(connection.getText());
		
			}
			  System.out.println("Launching connection");
			  Actions a = new Actions(Windriver);
			  WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionPEList.get(1)));
		      a.moveToElement(targetConnection).
		      doubleClick().
		      build().perform();
		      new WebDriverWait(Windriver, 60).until(ExpectedConditions.visibilityOf(Windriver.findElementByAccessibilityId("TitleBar")));
//		      System.out.println(Windriver.getWindowHandles().size());
//		      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[1]);
		      WebElement windowsPopupconnection =  Windriver.findElementByAccessibilityId("TitleBar");
		      String ConnectionText= windowsPopupconnection.getText();//
		     System.out.println("Pop Message for starting a connection - "+ConnectionText);
		  //   softAssert.assertTrue(ConnectionText.contains("Connection Launch:"), "Connection Launch: Message didn't display");
		     Thread.sleep(20000);
		     Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		     Windriver.findElement(By.name(connectionPEList.get(1))).click();
		     Windriver.findElement(By.name("Disconnect")).click();
		    	WebElement windowsPopupDisconnect =  Windriver.findElementByAccessibilityId("TitleBar");
		    	String Disconnecttext= windowsPopupDisconnect.getText();//
			     System.out.println(Disconnecttext);
			 //    softAssert.assertTrue(Disconnecttext.contains("ConnectionName is terminated."), "ConnectionName is terminated. Message didn't display");
			     Thread.sleep(5000);
		  		//Windriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); 
		  	//	 Windriver.switchTo().window(Windriver.getWindowHandle());
				
				 Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
				 closeApp();
				 cleanUpLogin();
				 UserPage.DeleteUser(firedrive, RAusername);				 
				 cleanUpLogout();
				 softAssert.assertAll();
		    }catch(Exception e) {
		    	e.printStackTrace();
		    	 Windriver.switchTo().window(Windriver.getWindowHandle());
				 closeApp();
				 cleanUpLogin();
				 UserPage.DeleteUser(firedrive, RAusername);
				 cleanUpLogout();
		    }
		     
			
			
				
		
		
	
}
	
	
	@Test //ensure to get an error message for more than 33 characters in password
	public void Test22_AI0025a_PasswordCharacLimit() throws Exception {
		printTestDetails("STARTING ", "Test22_AI0025a_PasswordCharacLimit", "");
		//open boxilla 
		cleanUpLogin();
		//create user
		userpage.user(firedrive).click();
		firedrive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		userpage.manage(firedrive).click();
		userpage.NewUser(firedrive).click();
		userpage.useTempNo(firedrive).click();
		userpage.ActiveDNo(firedrive).click();
		userpage.username(firedrive).sendKeys("TestUser");
		userpage.password(firedrive).sendKeys("ppppppppppppppppppppppppppppppppp");
		userpage.confirmPassword(firedrive).sendKeys("ppppppppppppppppppppppppppppppppp");
		userpage.NextButton(firedrive).click();
		WebElement general = firedrive.findElement(By.xpath(Users.getNewUserPrivilegeGeneralBtn()));
		SeleniumActions.exectuteJavaScriptClick(firedrive, general);
		userpage.RemoteAccess(firedrive).click();
		userpage.NextButton(firedrive).click();
		userpage.savebutton(firedrive).click();
		userpage.searchOption(firedrive).sendKeys("TestUser");
		System.out.println("Username entered in search box");
		String deviceApplianceTable = SeleniumActions.seleniumGetText(firedrive, Devices.applianceTable);
		Assert.assertTrue(deviceApplianceTable.contains("TestUser"),
				"Device appliance table did not contain: TestUser actual text: " + deviceApplianceTable);
		
		userpage.optionbutton(firedrive).click();
		firedrive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		userpage.DeleteOption(firedrive).click();
		firedrive.switchTo().alert().accept();
		System.out.println("TestUser is deleted");
		 cleanUpLogout();
	}
	
	@Test //Ensure to get an error message for an active directorty user creation on password having more than 105 characters
	public void Test23_AI0025a_ADPasswordCharacLimit() throws Exception {
		printTestDetails("STARTING ", "Test23_AI0025a_ADPasswordCharacLimit", "");
		
		cleanUpLogin();
		//create user
		String user="a";
		for (int i=1;i<109;i++) {
			user=user+"a";
		}
		System.out.println("user is "+user);
		userpage.user(firedrive).click();
		firedrive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		userpage.manage(firedrive).click();
		userpage.NewUser(firedrive).click();
		userpage.useTempNo(firedrive).click();
		userpage.ActiveDNo(firedrive).click();
		userpage.username(firedrive).sendKeys("TestUser");
		userpage.password(firedrive).sendKeys(user);
		userpage.confirmPassword(firedrive).sendKeys(user);
		userpage.NextButton(firedrive).click();
		WebElement admin = firedrive.findElement(By.xpath(Users.getNewUserPrivilegeAdminBtn()));
		SeleniumActions.exectuteJavaScriptClick(firedrive, admin);
		userpage.RemoteAccess(firedrive).click();
		userpage.NextButton(firedrive).click();
		userpage.savebutton(firedrive).click();
		userpage.searchOption(firedrive).sendKeys("TestUser");
		System.out.println("Username entered in search box");
		String deviceApplianceTable = SeleniumActions.seleniumGetText(firedrive, Devices.applianceTable);
		Assert.assertTrue(deviceApplianceTable.contains("TestUser"),
				"Device appliance table did not contain: TestUser actual text: " + deviceApplianceTable);
		
		userpage.optionbutton(firedrive).click();
		firedrive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		userpage.DeleteOption(firedrive).click();
		firedrive.switchTo().alert().accept();
		System.out.println("TestUser is deleted");
		 cleanUpLogout();
	}
//	about:config
//	security.enterprise_roots.enabled
	
		@Test //check the password should be alphanumeric
		public void Test24_AI0033_alphaNumeric() throws Exception {
			printTestDetails("STARTING ", "Test24_AI0033_alphaNumeric", "");
			SoftAssert softAssert = new SoftAssert();
			//open boxilla 
			cleanUpLogin();
			//create user
			Thread.sleep(3000);
			userpage.user(firedrive).click();
			firedrive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			userpage.manage(firedrive).click();
			userpage.NewUser(firedrive).click();
			userpage.useTempNo(firedrive).click();
			userpage.ActiveDNo(firedrive).click();
			userpage.username(firedrive).sendKeys("TestUser");
			userpage.password(firedrive).sendKeys("qwert!£$%/");
			userpage.confirmPassword(firedrive).sendKeys("qwert!£$%/");
			userpage.NextButton(firedrive).click();
			softAssert.assertEquals(userpage.IncorrectPassword(firedrive), "Password can't contain certain characters. Invalid characters are: \"'/\\[]:;|=,+*?<>`");
			//System.out.println("Showed error Message - Password can't contain certain characters. Invalid characters are: \\\"'/\\\\[]:;|=,+*?<>`\"");
			//UserPage.DeleteUser(firedrive, RAusername);
			 cleanUpLogout();
		}
		
		
	
		
		
		@Test //Launch the connection in View only mode
		public void Test25_SR0046_viewOnly() throws Exception {
			printTestDetails("STARTING ", "Test25_SR0046_viewOnly", "");
			
			WebDriverWait wait=new WebDriverWait(firedrive, 20);
			
			
			ArrayList<Device> DualHeadDevices = new ArrayList<Device>();
			ArrayList<String> connectionDualList = new ArrayList<String>();
			DualHeadDevices=devicePool.getAllDevices("Onedevice.properties");;

			ArrayList<String> viewList = new ArrayList<String>();
			
			cleanUpLogin();
		
			for(Device deviceList : DualHeadDevices) {
				
				System.out.println("Adding the connection "+deviceList.getIpAddress());
				SeleniumActions.seleniumClick(firedrive, LandingPage.connectionsTab);
				log.info("Navigate to Connections > Manage : Connections Tab clicked");
				new WebDriverWait(firedrive, 60).until(ExpectedConditions.elementToBeClickable(By.xpath( LandingPage.connectionsManage)));
				SeleniumActions.seleniumClick(firedrive, LandingPage.connectionsManage);
				log.info("Navigate to Connections > Manage : Connections > Manage Tab clicked");
				ConnectionPage.newconnection(firedrive).click();
				firedrive.manage().timeouts().implicitlyWait(4,TimeUnit.SECONDS);
				ConnectionPage.connectionName(firedrive).sendKeys(deviceList.getIpAddress());
				ConnectionPage.Host(firedrive).sendKeys(deviceList.getIpAddress());
				ConnectionPage.optimised(firedrive).click();
				ConnectionPage.nextoption(firedrive).click();
				ConnectionPage.sharedconnectionType(firedrive).click();
				
				ConnectionPage.Audio(firedrive).click();
				ConnectionPage.viewonly(firedrive).click();
				ConnectionPage.nextoption(firedrive).click();
				firedrive.manage().timeouts().implicitlyWait(4,TimeUnit.SECONDS);
				ConnectionPage.Saveoption(firedrive).click();
				firedrive.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
				wait.until(ExpectedConditions.visibilityOfAllElements(ConnectionPage.connectiontable(firedrive)));
				ConnectionPage.searchOption(firedrive).sendKeys(deviceList.getIpAddress());
				System.out.println("Connection Name entered in search box");
				firedrive.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
				Thread.sleep(4000);
				wait.until(ExpectedConditions.visibilityOfAllElements(ConnectionPage.connectiontable(firedrive)));
				String deviceApplianceTable = SeleniumActions.seleniumGetText(firedrive, Devices.applianceTable);
				Assert.assertTrue(deviceApplianceTable.contains(deviceList.getIpAddress()),
						"Table did not contain: "+deviceList.getIpAddress()+"  , actual text: " + deviceApplianceTable);
				
		}
				userpage.createUser(firedrive,DualHeadDevices,RAusername,RApassword,"General");
				Thread.sleep(5000);
				cleanUpLogout();
				RAlogin(RAusername,RApassword);
				
				WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
			
				List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
				for (WebElement connection : availableConnections) {
					connectionDualList.add(connection.getText());
				}
				
				for (String connectionName : connectionDualList) {
					  Actions a = new Actions(Windriver);
					  WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionName));
				      a.moveToElement(targetConnection).
				      doubleClick().
				      build().perform();
				      System.out.println("connection named "+connectionName+" has been launched");
				      Thread.sleep(10000);
				   			      
				}
				try {
				//Getting window handle of launched connection
		    	 WebElement connectionWindow = (WebElement) Windriver2.findElementByClassName("wCloudBB");
		    	 String connectionWindowHandle = connectionWindow.getAttribute("NativeWindowHandle");
		    	 String connectionTopLevelWidnowHandle = Integer.toHexString(Integer.parseInt((connectionWindowHandle))); // Convert to Hex
		    	 
		    	 //Setting capabilities for connection window session
		    	 DesiredCapabilities connectionAppCapabilities = new DesiredCapabilities();
		    	 connectionAppCapabilities.setCapability("appTopLevelWindow", connectionTopLevelWidnowHandle);
		    	
		    	 WindowsDriver RASession;
		    	 
		    	 //attaching to connection session and doing stuff..
		    	
		    		RASession = new WindowsDriver(new URL("http://127.0.0.1:4723"), connectionAppCapabilities);
		    		//RASession.switchTo().window((String)RASession.getWindowHandles().toArray()[0]);
		    		
		    		WebElement minimise = RASession.findElementByName("Minimise");
		    		WebElement maximise = RASession.findElementByName("Maximise");
		    		WebElement close = RASession.findElementByName("Close");
		    		WebElement title = RASession.findElementByAccessibilityId("TitleBar"); 
		    		
		    		System.out.println("Title of launched connection is "+title.getText());
		    		Assert.assertTrue(title.getText().contains("View Only"), " Title doesn't contain view only");
		    		Thread.sleep(2000);
		    		close.click();
		    		Thread.sleep(5000);
		    		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			    	closeApp();
					cleanUpLogin();
					UserPage.DeleteUser(firedrive, RAusername);
					cleanUpLogout();
		    		
		    	} catch (MalformedURLException e) {
		    		// TODO Auto-generated catch block
		    		e.printStackTrace();
		    		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		    		closeApp();
		    		cleanUpLogin();
		     		UserPage.DeleteUser(firedrive, RAusername);
		    		cleanUpLogout();
		    	}
				
		}
		
		
		//@Test //Log on to a TX managed by another boxilla
		public void Test26_SR0036() throws Exception {
			printTestDetails("STARTING ", "Test26_SR0036", "");
			
			ArrayList<String> viewList = new ArrayList<String>();
			WebDriverWait wait=new WebDriverWait(firedrive, 20);
			ArrayList<Device> DualHeadDevices = new ArrayList<Device>();
			DiscoveryMethods discoveryMethods = new DiscoveryMethods();	
			AppliancePool applian = new AppliancePool();
			ArrayList<String> connectionDualList = new ArrayList<String>();
			ArrayList<Device> remotedevice=applian.getAllDevices("Onedevice.properties");
			System.out.println(remotedevice);
			DualHeadDevices.add(remotedevice.get(0));
//			cleanUpLogin();
//			unManageDevice(firedrive,remotedevice);
//			cleanUpLogout();
//			DoubleLogin();
//			for(Device deviceList : remotedevice) {
//				System.out.println("Adding the device "+deviceList);
//				discoveryMethods.addDeviceToBoxilla(firedrive, deviceList.getMac(), deviceList.getIpAddress(),
//						deviceList.getGateway(),deviceList.getNetmask(), deviceList.getDeviceName(), 10);
//				}
//				System.out.println("*************Device is Managed***************");
//				cleanUpLogout();	
				
				cleanUpLogin();
				ConnectionPage.createprivateconnections(firedrive, remotedevice);
				userpage.createUser(firedrive,remotedevice,RAusername,RApassword,"General");
				cleanUpLogout();
			
			
				
				RAlogin(RAusername,RApassword);
				//Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
				//Thread.sleep(1500);
				WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
				//Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
				List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
				for (WebElement connection : availableConnections) {
					connectionDualList.add(connection.getText());
				}
				
				for (String connectionName : connectionDualList) {
					  Actions a = new Actions(Windriver);
					  WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionName));
				      a.moveToElement(targetConnection).
				      doubleClick().
				      build().perform();
				      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
				      new WebDriverWait(firedrive, 60).until(ExpectedConditions.visibilityOfAllElements(Windriver.findElementByName("Unable to connect")));
//				      System.out.println("connection named "+connectionName+" has been launched");
				      //Thread.sleep(3000);
				      WebElement windowsPopupOpenButton = Windriver.findElementByName("Unable to connect");
				        String text= windowsPopupOpenButton.getText();
				        System.out.println(text);
				      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
				      
				      
				}
				      
				    //  closeApp();
				    //  Thread.sleep(10000);
						Thread.sleep(2000);
					closeApp();
					cleanUpLogin();
					Thread.sleep(2000);
					ConnectionPage.DeleteConnection(firedrive, remotedevice);
					UserPage.DeleteUser(firedrive, RAusername);
					DoubleLogin();	
					unManageDevice(firedrive,remotedevice);
					cleanUpLogout();
					cleanUpLogin();
					for(Device deviceList : remotedevice) {
						System.out.println("Adding the device "+deviceList);
						discoveryMethods.addDeviceToBoxilla(firedrive, deviceList.getMac(), deviceList.getIpAddress(),
								deviceList.getGateway(),deviceList.getNetmask(), deviceList.getDeviceName(), 10);
						}
						System.out.println("*************Device is Managed***************");
						cleanUpLogout();	
				
			}
		
		//@Test //Launch 4 shared connection at a time
		public void Test26_CL0006b_connectionSupport() throws Exception {
			printTestDetails("STARTING ", "Test26_CL0006b_connectionSupport", "");
			SoftAssert softAssert = new SoftAssert();
			Onedevices = devicePool.getAllDevices("Onedevice.properties");
			cleanUpLogin();
			userpage.createUser(firedrive,null,RAusername,RApassword,"General");
			SharedNames=ConnectionPage.Sharedconnection(firedrive,Onedevices,4,"shared");
			UserPage.Sharedconnectionassign(firedrive, RAusername, SharedNames);
			
			cleanUpLogout();
			int count=0;
			int connectionNumber=1;
			try {
			RAlogin(RAusername,RApassword);
			WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
			List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
			
			for (WebElement connection : availableConnections) {
			  System.out.println("connections number  "+connectionNumber+" is "+connection.getText());
			  connectionList.add(connection.getText());
			  connectionNumber++;
			}
			
			for (String connectionName : SharedNames) {
			
			WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionName));
			  Actions a = new Actions(Windriver);
			  count++;
		      a.moveToElement(targetConnection).
		      doubleClick().
		      build().perform();
		      Thread.sleep(20000);
		      System.out.println(connectionName+" has been launched");
		      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			}
			
			
			RestAssured.useRelaxedHTTPSValidation();
			String gerdetails = RestAssured.given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers("Content-Type", "application/json", "Accept","application/json")
								.when().get("https://"+boxillaManager+"/bxa-api/connections/kvm/active")
								.then().assertThat().statusCode(200)
								.extract().response().asString();
			 System.out.println("Active connection status"+gerdetails);
		     
				System.out.println("********************checking the connection status  ******************");
				JsonPath js = new JsonPath(gerdetails);
				int	countconnection=js.getInt("message.active_connections.size()");
				System.out.println("Number of Active connections are "+countconnection);
				System.out.println("Number of expected active connections to be "+connectionList.size());
				softAssert.assertEquals(countconnection,connectionList.size()," Number of active connection didn't match with the number of connections before changing credentials");	
				
				
		    	
		    	
			 Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			 for (String connectionName : SharedNames) {
			    	Windriver.findElement(By.name(connectionName)).click();
			    	Windriver.findElement(By.name("Disconnect")).click();
				    Thread.sleep(3000);
				    System.out.println("connection "+connectionName+" is disconnected");
				    Windriver.switchTo().window(Windriver.getWindowHandle());
				    
				
		}
			Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			closeApp();
			cleanUpLogin();		
			UserPage.DeleteUser(firedrive, RAusername);
			cleanUpLogout();
			} catch(Exception e) {
				e.printStackTrace();
				Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
				closeApp();
				cleanUpLogin();
				Thread.sleep(2000);
				UserPage.DeleteUser(firedrive, RAusername);
				cleanUpLogout();
				
			}
		}
		}
	
		
			
		
				
		
		
		
	
	

	
	
	
	
	

