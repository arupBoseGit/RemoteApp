package tests;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;


import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import methods.AppliancePool;
import methods.Device;
import methods.Devices;
import methods.Discovery;
import methods.DiscoveryMethods;
import methods.SeleniumActions;
import pages.ConnectionPage;
import pages.LandingPage;
import pages.UserPage;
import pages.boxillaElements;

public class Firstphase extends TestBase{
	
	final static Logger log = Logger.getLogger(Firstphase.class);
	UserPage userpage = new UserPage();
	
	@Test //Change the appliance setting to absolute
	public void Test01_SR0018() throws Exception {
		printTestDetails("STARTING ", "Test01_SR0018", "");
		cleanUpLogin();
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
	public void Test02_CL0001() throws Exception {
		printTestDetails("STARTING ", "Test02_CL0001", "");
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
	public void Test03_CL0002() {
		printTestDetails("STARTING ", "Test03_CL0002", "");
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
	public void Test04_CL0003() throws Exception {
		printTestDetails("STARTING ", "Test04_CL0003", "");
		cleanUpLogin();
		userpage.createUser(firedrive,devices,RAusername,RApassword,"General");
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
	public void Test05_SR0021() throws Exception {
		printTestDetails("STARTING ", "Test05_SR0021", "");
		setup();
		WebElement loginButton = getElement("logInButton");
		getElement("userNameTextBox").sendKeys("User");
		System.out.println("User name entered as -User");
		getElement("passwordTextBox").sendKeys("User");
		System.out.println("Password entered as -User");
		loginButton.click();
		System.out.println("Login button clicked");
		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
    	   
        WebElement windowsPopupOpenButton = Windriver.findElementByName("Log In: Invalid Login Credentials");
        String text= windowsPopupOpenButton.getText();
     // capture alert message
        System.out.println("Alert Message is  "+text);
        Assert.assertEquals("Log In: Invalid Login Credentials", text);
   
        Thread.sleep(5000);
        Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
   
       
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
	
	//@Test Remote app maximum limit to user
	public void Test06_CL0005a() throws Exception {
		printTestDetails("STARTING ", "Test06_CL0005a", "");

		Onedevices = devicePool.getAllDevices("Onedevice.properties");
		cleanUpLogin();
		SharedNames=ConnectionPage.Sharedconnection(firedrive,Onedevices,5,"shared");
		UserPage.Sharedconnectionassign(firedrive, RAusername, SharedNames);
		int count=0;
		int connectionNumber=1;
		RAlogin(RAusername,RApassword);
		WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
		List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
		
		for (WebElement connection : availableConnections) {
		  System.out.println("connections number  "+connectionNumber+" is "+connection.getText());
		  connectionList.add(connection.getText());
		  connectionNumber++;
		}
		
		for (String connectionName : SharedNames) {
		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionName));
		  Actions a = new Actions(Windriver);
		  count++;
	      a.moveToElement(targetConnection).
	      doubleClick().
	      build().perform();
	          
	       
	      if (count==5) {
	    	  WebElement windowsPopupOpenButton = Windriver.findElementByName("Maximum Number of Connections Reached");
	          String text= windowsPopupOpenButton.getText();
	    	  System.out.println("Alert text is "+text);
	    	  Assert.assertTrue(text.equalsIgnoreCase("Maximum Number of Connections Reached"), "Maximum Number of Connections Reached Message has not been displayed ");
	    	  break;
	      }
	      Thread.sleep(10000);
	      System.out.println(connectionName+" has been launched");
		}
		
		
		 Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		 for (String connectionName : SharedNames) {
		    	Windriver.findElement(By.name(connectionName)).click();
		    	Windriver.findElement(By.name("Disconnect")).click();
			    Thread.sleep(3000);
			    System.out.println("connection "+connectionName+" is disconnected");
			    Windriver.switchTo().window(Windriver.getWindowHandle());
			    
			
	}
		closeApp();
		
		ConnectionPage.DeleteSharedConnection(firedrive, SharedNames);
		
		UserPage.DeleteUser(firedrive, RAusername);
		}
		
	@Test //RA configuration for connection, settings and information
	public void Test07_AI0004() throws Exception {
		printTestDetails("STARTING ", "Test07_AI0004", "");
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
	public void Test08_AI0046() throws Exception {
		printTestDetails("STARTING ", "Test08_AI0046", "");
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
	public void Test09_AI0047() throws Exception {
		printTestDetails("STARTING ", "Test09_AI0047", "");
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
	public  void Test10_VI0006() throws Exception{
		printTestDetails("STARTING ", "Test10_VI0006", "");
		cleanUpLogin();
		userpage.createUser(firedrive,devices,RAusername,RApassword,"General");
		cleanUpLogout();
		RAlogin(RAusername,RApassword);
		getElement("menuLabel").click();
		Windriver.findElementByName("Settings").click();
		System.out.println("Clicking on the Connection Window");
		getElement("settingsNavigation").click();
		String resolution=getElement("windowResolutionComboBox").getText();
		System.out.println("Connection Window resolution is "+resolution);
		softAssert.assertTrue(resolution.equalsIgnoreCase("Auto"), "Connection Window resolution is not Auto");
		closeApp();
		cleanUpLogin();
		userpage.DeleteUser(firedrive, RAusername);
		cleanUpLogout();
		softAssert.assertAll();
	}
		
	@Test
	public void  Test11_AI0007() throws Exception {
		printTestDetails("STARTING ", "Test11_AI0007", "");
		Onedevices=devicePool.getAllDevices("Onedevice.properties");
		cleanUpLogin();
		ConnectionPage.createprivateconnections(firedrive,Onedevices);
		userpage.createUser(firedrive,Onedevices,"TestUser1","TestUser1","General");
		cleanUpLogout();
		RAlogin("TestUser1","TestUser1");
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
			if(connection.getText().equalsIgnoreCase(deviceList.getIpAddress())) {
				status=true;
				softAssert.assertTrue(status,"Connection Name "+connection+" shown in RemoteApp has not been assigned to the user");
				System.out.println(" connection "+connection.getText()+" is assigned to the correct user");
				break;
			}
			
//			System.out.println("Connection name - "+connection+" has been assigned correctly o the User");
		}}
		closeApp();
		cleanUpLogin();
		UserPage.DeleteUser(firedrive,"TestUser1");
		ConnectionPage.DeleteConnection(firedrive, Onedevices);
		cleanUpLogout();
		softAssert.assertAll();
		
	}
	
	//Test to ensure all the user(administrator, power and General) have same privileges
	@Test
	public void Test12_AI0005() throws Exception {
		printTestDetails("STARTING ", "Test12_AI0005", "");
		cleanUpLogin();
		Onedevices=devicePool.getAllDevices("Onedevice.properties");
		userpage.createUser(firedrive,Onedevices,"TestUser1","TestUser1","General");
		userpage.createUser(firedrive,Onedevices,"TestUser2","TestUser2","Power");
		userpage.createUser(firedrive,Onedevices,"TestUser3","TestUser3","Administrator");

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
		
	
	
	//@Test //ensure the launched connection not to impact and user need not to change the password in current session.
	public void Test14_AI0032() throws Exception {
		printTestDetails("STARTING ", "Test14_AI0032", "");
		Onedevices=devicePool.getAllDevices("Onedevice.properties");
		cleanUpLogin();
		Thread.sleep(3000);
		ConnectionPage.createprivateconnections(firedrive,Onedevices);
		userpage.createUser(firedrive,Onedevices,RAusername,RApassword,"General");
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
			userpage.user(firedrive).click();
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
				ConnectionPage.DeleteConnection(firedrive, Onedevices);
				cleanUpLogout();
				softAssert.assertAll();
			}
		
	}
	
	@Test
	public void Test15_AI0043() throws Exception {
		printTestDetails("STARTING ", "Test15_AI0043", "");
		Onedevices=devicePool.getAllDevices("Onedevice.properties");
		cleanUpLogin();
		ConnectionPage.createprivateconnections(firedrive,Onedevices);
		userpage.createUser(firedrive,Onedevices,RAusername,RApassword,"General");
		cleanUpLogout();
		RAlogin(RAusername,RApassword);
		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
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
		    //  Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			}
		closeApp();
		cleanUpLogin();
		UserPage.DeleteUser(firedrive, RAusername);
		ConnectionPage.DeleteConnection(firedrive, Onedevices);
		cleanUpLogout();
		
	}
	
	//@Test //Multiple connection will fail
	
	public void Test16_CL0006a() throws Exception{
		printTestDetails("STARTING ", "Test16_CL0006a", "");
		ArrayList<Device> PEDevices = new ArrayList<Device>();
		PEDevices=devicePool.getAllDevices("devicePE.properties");;

		ArrayList<String> connectionPEList = new ArrayList<String>();

		cleanUpLogin();

		
	
		ConnectionPage.createprivateconnections(firedrive,PEDevices);
	
		userpage.createUser(firedrive,PEDevices,RAusername,RApassword,"General");
		cleanUpLogout();
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
	 ConnectionPage.DeleteConnection(firedrive, PEDevices);
	 cleanUpLogout();
	 softAssertion.assertAll();
			
		
	}
	@Test
	public void Test17_DC0001() throws Exception {
		printTestDetails("STARTING ", "Test17_DC0001", "");
		ArrayList<Device> SEDevices = new ArrayList<Device>();
		SEDevices=devicePool.getAllDevices("deviceSE.properties");;

		ArrayList<String> connectionPEList = new ArrayList<String>();

		cleanUpLogin();

		
	
		ConnectionPage.createprivateconnections(firedrive,SEDevices);
	
		userpage.createUser(firedrive,SEDevices,RAusername,RApassword,"General");
		cleanUpLogout();
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
	public void Test18_DC0001() throws Exception {
		printTestDetails("STARTING ", "Test18_DC0001", "");
		ArrayList<Device> PEDevices = new ArrayList<Device>();
		PEDevices=devicePool.getAllDevices("devicePE.properties");;

		ArrayList<String> connectionPEList = new ArrayList<String>();

		cleanUpLogin();

		
	
		ConnectionPage.createprivateconnections(firedrive,PEDevices);
	
		userpage.createUser(firedrive,PEDevices,RAusername,RApassword,"General");
		cleanUpLogout();
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
	 ConnectionPage.DeleteConnection(firedrive, PEDevices);
	 cleanUpLogout();
	 softAssert.assertAll();
			}
	
	
	@Test
	public void Test19_DC0001() throws Exception {
		printTestDetails("STARTING ", "Test19_DC0001", "");
		ArrayList<Device> ZuDevices = new ArrayList<Device>();
		ZuDevices=devicePool.getAllDevices("deviceZeroU.properties");;

		ArrayList<String> connectionPEList = new ArrayList<String>();

		cleanUpLogin();

		
	
		ConnectionPage.createprivateconnections(firedrive,ZuDevices);
	
		userpage.createUser(firedrive,ZuDevices,RAusername,RApassword,"General");
		cleanUpLogout();
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
	 ConnectionPage.DeleteConnection(firedrive, ZuDevices);
	 cleanUpLogout();
	 softAssertion.assertAll();
			}
	
	
	//@Test//private already in use
	public void Test20_SR0005() throws Exception {
		printTestDetails("STARTING ", "Test20_SR0005", "");
		ArrayList<Device> ZeroUDevices = new ArrayList<Device>();
		DiscoveryMethods discoveryMethods = new DiscoveryMethods();	
		AppliancePool applian = new AppliancePool();
		ArrayList<String> connectionZeroUList = new ArrayList<String>();
		ArrayList<Device> remotedevice=applian.getAllDevices("Onedevice.properties");
		ZeroUDevices.add(remotedevice.get(0));
		cleanUpLogin();
		for(Device deviceList : ZeroUDevices) {
			System.out.println("Adding the device "+deviceList);
			discoveryMethods.addDeviceToBoxilla(firedrive, deviceList.getMac(), deviceList.getIpAddress(),
					deviceList.getGateway(),deviceList.getNetmask(), deviceList.getDeviceName(), 10);
			}
			System.out.println("*************All Devices are Managed***************");
			ConnectionPage.createprivateconnections(firedrive,remotedevice);
			userpage.ManageConnection(firedrive,remotedevice,RAusername);
			Thread.sleep(3000);
			RAlogin(RAusername,RApassword);
			Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
			List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
			for (WebElement connection : availableConnections) {
		
			connectionZeroUList.add(connection.getText());
		
			}
		
			for (String connectionName : connectionZeroUList) {
			  Actions a = new Actions(Windriver);
		
			  WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionName));
		      a.moveToElement(targetConnection).
		      doubleClick().
		      build().perform();
		      System.out.println("connection named "+connectionName+" has been launched");
		      Thread.sleep(10000);
			}
		     // RAlogin(RAusername,RApassword);
		      setup();
		      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[1]);
		      WebElement loginButton = getElement("logInButton");
				getElement("userNameTextBox").sendKeys(RAusername);
				System.out.println("Username Entered");
				getElement("passwordTextBox").sendKeys(RApassword);
				System.out.println("Password Entered");
				loginButton.click();
				Thread.sleep(2000);
				Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		      for (String connectionName1 : connectionZeroUList) {
				  Actions a1 = new Actions(Windriver);
			
				  WebElement targetConnection1 = availableConnectionsList.findElement(By.name(connectionName1));
			      a1.moveToElement(targetConnection1).
			      doubleClick().
			      build().perform();
			      new WebDriverWait(firedrive, 60).until(ExpectedConditions.elementToBeClickable(Windriver.findElementByName("Private/Shared Violation")));
			      WebElement windowsPopupOpenButton = Windriver.findElementByName("Private/Shared Violation");
		          String text= windowsPopupOpenButton.getText();
		  		Windriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); 
		  		System.out.println("Alert Message is  "+text);
		  	//	Assert.assertTrue(text.equals("Incorrect Boxilla IP"),"No pop up Message  stating - Boxilla has not been configured");
		  	//	Windriver.findElementByName("OK").click();
		    	  
		      }
		      Thread.sleep(20000);
		    
		      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		      closeApp();
		      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		      closeApp();
		      softAssertion.assertAll();
				
	
			}
			
//			for (String connectionName : connectionZeroUList) {
//		    	Windriver.findElement(By.name(connectionName)).click();
//		    	Windriver.findElement(By.name("Disconnect")).click();
//			    Thread.sleep(5000);
//			    System.out.println("connection "+connectionName+" is disconnected");
//			    Windriver.switchTo().window(Windriver.getWindowHandle());
//			    
//			
//	
//	}
	
		
		
	
	
	
	@Test//. Close remote application while connections are running
	public void Test21_AI0038() throws Exception {
		printTestDetails("STARTING ", "Test21_AI0038", "");
		ArrayList<Device> ZuDevices = new ArrayList<Device>();
		ZuDevices=devicePool.getAllDevices("deviceZeroU.properties");;

		ArrayList<String> connectionPEList = new ArrayList<String>();

		cleanUpLogin();

		
	
		ConnectionPage.createprivateconnections(firedrive,ZuDevices);
	
		userpage.createUser(firedrive,ZuDevices,RAusername,RApassword,"General");
		cleanUpLogout();
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
	 Thread.sleep(5000);
	 ConnectionPage.DeleteConnection(firedrive, ZuDevices);
	 cleanUpLogout();
	 softAssert.assertAll();
			}
			    
			  
			    
			 
			  
		     
		

	@Test
	public void Test22_AI0048() throws Exception {
		printTestDetails("STARTING ", "Test22_AI0048", "");
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
		     System.out.println(ConnectionText);
		     softAssert.assertTrue(ConnectionText.contains("Connection Launch:"), "Connection Launch: Message didn't display");
		     Thread.sleep(20000);
		     Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		     Windriver.findElement(By.name(connectionPEList.get(1))).click();
		     Windriver.findElement(By.name("Disconnect")).click();
		     System.out.println(Windriver.getWindowHandles().size());
		     Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[1]);
		  //   new WebDriverWait(Windriver, 60).until(ExpectedConditions.visibilityOf(Windriver.findElementByName("ConnectionName is terminated")));
		    try {
		    	WebElement windowsPopupDisconnect =  Windriver.findElementByAccessibilityId("TitleBar");
		    	String Disconnecttext= windowsPopupDisconnect.getText();//
			     System.out.println(Disconnecttext);
			     softAssert.assertTrue(Disconnecttext.contains("ConnectionName is terminated."), "ConnectionName is terminated. Message didn't display");
			     Thread.sleep(5000);
		  		//Windriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); 
		  		 Windriver.switchTo().window(Windriver.getWindowHandle());
				 closeApp();
				 cleanUpLogin();
				 UserPage.DeleteUser(firedrive, RAusername);
				 Thread.sleep(5000);
				 ConnectionPage.DeleteConnection(firedrive, remotedevice);
				 cleanUpLogout();
				 softAssertion.assertAll();
		    }catch(Exception e) {
		    	e.printStackTrace();
		    	 Windriver.switchTo().window(Windriver.getWindowHandle());
				 closeApp();
				 cleanUpLogin();
				 UserPage.DeleteUser(firedrive, RAusername);
				 Thread.sleep(5000);
				 ConnectionPage.DeleteConnection(firedrive, remotedevice);
				 cleanUpLogout();
		    }
		     
			
			
				
		
		
	
}
	
	
	@Test
	public void Test23_AI0025a() throws Exception {
		printTestDetails("STARTING ", "Test23_AI0025a", "");
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
		userpage.UserType("General", firedrive).click();
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
	
	@Test
	public void Test24_AI0025a() throws Exception {
		printTestDetails("STARTING ", "Test24_AI0025a", "");
		
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
		userpage.UserType("Administrator", firedrive).click();
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
	
		@Test
		public void Test25_AI0033() throws Exception {
			printTestDetails("STARTING ", "Test25_AI0033", "");
			
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
		
		
	
		
		
	@Test
		public void Test26_SR0046() throws Exception {
			printTestDetails("STARTING ", "Test26_SR0046", "");
			
			WebDriverWait wait=new WebDriverWait(firedrive, 20);
			
			
			ArrayList<Device> DualHeadDevices = new ArrayList<Device>();
			ArrayList<String> connectionDualList = new ArrayList<String>();
			DualHeadDevices=devicePool.getAllDevices("Onedevice.properties");;

			ArrayList<String> viewList = new ArrayList<String>();
			
			cleanUpLogin();
		
			for(Device deviceList : DualHeadDevices) {
				
				ConnectionPage.connections(firedrive).click();
				firedrive.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
				ConnectionPage.manage(firedrive).click();
				firedrive.manage().timeouts().implicitlyWait(3,TimeUnit.SECONDS);
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
				      Thread.sleep(15000);
				   //   Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
				      //"/Pane[@ClassName=\"#32769\"][@Name=\"Desktop 1\"]/Window[@ClassName=\"wCloudBB\"][@Name=\"10.211.130.215(View Only)\"]/TitleBar[@AutomationId=\"TitleBar\"]"
				//    System.out.println( Windriver.findElementById("TitleBar").getText());
				      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
				      
				}
				      
				
				      Thread.sleep(10000);
						RestAssured.useRelaxedHTTPSValidation();
					
					    String gerdetails = given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers("Content-Type", "application/json", "Accept","application/json")
					    		    		.when().get("https://"+boxillaManager+"/bxa-api/connections/kvm/active")
					    		    		.then().assertThat().statusCode(200)
					    		    		.extract().response().asString();
					    
					    System.out.println("Response is "+gerdetails);
					    
					    JsonPath js1 = new JsonPath(gerdetails);
					    int count=js1.getInt("message.size()");
					    int countconnection=js1.getInt("message.active_connections.size()");
					    System.out.println("connection size is "+countconnection);
					  
					    for (String DualName : connectionDualList) { 
					    	Windriver.findElement(By.name(DualName)).click();
					    	Windriver.findElement(By.name("Disconnect")).click();
						    Thread.sleep(3000);
						    System.out.println("connection "+DualName+" is disconnected");
						    Windriver.switchTo().window(Windriver.getWindowHandle());
						    
						
				}
					closeApp();
					cleanUpLogin();
					Thread.sleep(2000);
					ConnectionPage.DeleteConnection(firedrive, DualHeadDevices);
					UserPage.DeleteUser(firedrive, RAusername);
					cleanUpLogout();
				
			}
		
		
		//@Test
		public void Test27_SR0036() throws Exception {
			printTestDetails("STARTING ", "Test27_SR0036", "");
			
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
		
		
	
		
			}
		
				
		
		
		
	
	

	
	
	
	
	

