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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import jdk.internal.org.jline.utils.Log;
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
	UserPage userpage = new UserPage();
	
	//@Test
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
		
			}
		

		
	
	
	//@Test
	public void Test02_CL0001() throws Exception {
		printTestDetails("STARTING ", "Test02_CL0001", "");
		setup();
		Windriver.manage().timeouts().implicitlyWait(3,TimeUnit.SECONDS);
		Windriver.findElementByName("Demo Mode").click();
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
		//UserPage.endTest("Test02_CL0001");
	}
	
	//@Test
	public void Test03_CL0002() {
		printTestDetails("STARTING ", "Test03_CL0002", "");
		setup();
		Windriver.manage().timeouts().implicitlyWait(3,TimeUnit.SECONDS);
		Windriver.findElementByName("Demo Mode").click();
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
//		getElement("closeLogInScreen").click();
//		getElement("exitMenuItems").click();
		closeRemoteApp();
		
		
	}
	
	//@Test
	public void Test04_CL0003() throws Exception {
		printTestDetails("STARTING ", "Test04_CL0003", "");
		setup();
		System.out.println("RemoteApp is opened");
		WebElement loginButton = getElement("logInButton");
		getElement("userNameTextBox").sendKeys(RAusername);
		System.out.println("Username Entered");
		getElement("passwordTextBox").sendKeys(RApassword);
		System.out.println("Password Entered");
		loginButton.click();
		System.out.println("Login button clicked");
		Thread.sleep(3000);
		cleanUpLogin();
		UserPage.currentUser(firedrive,RAusername,5);
		cleanUpLogout();
		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		closeApp();
		
	}
	
	//@Test
	public void Test05_SR0021() {
		printTestDetails("STARTING ", "Test05_SR0021", "");
		setup();
		WebElement loginButton = getElement("logInButton");
		getElement("userNameTextBox").sendKeys("User");
		System.out.println("User name entered");
		getElement("passwordTextBox").sendKeys("User");
		System.out.println("Password entered");
		loginButton.click();
		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
//		WebElement windowsPopupFileNameTextbox = Windriver.findElementByXPath("//Edit[@Name='File name:']");
//        windowsPopupFileNameTextbox.sendKeys("D:\\Data.txt");
     	   
        WebElement windowsPopupOpenButton = Windriver.findElementByName("Log In: Invalid Login Credentials");
        String text= windowsPopupOpenButton.getText();
     // capture alert message
        System.out.println("Alert Message is  "+text);
        Assert.assertEquals("Log In: Invalid Login Credentials", text);
        Windriver.findElementByName("OK").click();
  //      windowsPopupOpenButton.click();

        Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
        getElement("closeLogInScreen").click();
        System.out.println("RemoteApp closed");
		
	}
	
	//@Test
	public void Test06_CL0005a() throws Exception {
		
		List<String> SharedNames = new ArrayList<String>();
		SharedNames.add("10.211.130.114test1");
		SharedNames.add("10.211.130.114test2");
		SharedNames.add("10.211.130.114test3");
		SharedNames.add("10.211.130.114test4");
		SharedNames.add("10.211.130.114test5");
		
	//	SharedNames=ConnectionPage.Sharedconnection(firedrive,devices);
		
		printTestDetails("STARTING ", "Test06_CL0005a", "");
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
	      Thread.sleep(25000);
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
		
		}
		
	//@Test
	public void Test07_AI0004() throws Exception {
		printTestDetails("STARTING ", "Test07_AI0004", "");
		setup();
		boolean statusconnect = false;
		boolean statusSettings = false;
		boolean statusInfo = false;
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
		
	}
		
	//@Test
	public void Test08_AI0046() throws Exception {
		printTestDetails("STARTING ", "Test08_AI0046", "");
		RAlogin(RAusername,RApassword);
		getElement("menuLabel").click();
		Windriver.findElementByName("Information").click();
		System.out.println("Information tab is clicked");
		String Version=getElement("versionLabel").getText();
		System.out.println("The version of RemoteApp is "+Version);
		Assert.assertTrue(Version.contains("Version"), "RemoteApp does not contain Version on the Information section");
		String contact=getElement("blackBoxWebsiteLinkLabel").getText();
		System.out.println("Contact details of blackBox is "+contact);
		Assert.assertTrue(contact.contains("https://www.blackbox.com/en-us/support"), "RemoteApp does not have BlackBox contact details");
		closeApp();
		
	}
	//@Test
	public void Test09_AI0047() throws Exception {
		printTestDetails("STARTING ", "Test09_AI0047", "");
		RAlogin(RAusername,RApassword);
		getElement("menuLabel").click();
		Windriver.findElementByName("Information").click();
		System.out.println("Information tab is clicked");
		Assert.assertTrue(getElement("supportLabel").isDisplayed(),"Help information is not displayed");
		System.out.println("Help Information has been displayed");
		closeApp();
		
	}
	
	//@Test
	public  void Test10_VI0006() throws Exception{
		printTestDetails("STARTING ", "Test10_VI0006", "");
		RAlogin(RAusername,RApassword);
		getElement("menuLabel").click();
		Windriver.findElementByName("Settings").click();
		System.out.println("Clicking on the Connection Window");
		getElement("settingsNavigation").click();
		String resolution=getElement("windowResolutionComboBox").getText();
		System.out.println("Connection Window resolution is "+resolution);
		Assert.assertTrue(resolution.equalsIgnoreCase("Auto"), "Connection Window resolution is not Auto");
		closeApp();
		
	}
		
	//@Test
	public void  Test11_AI0007() throws Exception {
		printTestDetails("STARTING ", "Test11_AI0007", "");
		cleanUpLogin();
		userpage.createUser(firedrive,devices,"TestUser1","TestUser1","General");
		//userpage.ManageConnection(firedrive, devices, "TestUser1");
		cleanUpLogout();
		RAlogin("TestUser1","TestUser1");
		WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
		System.out.println("availableConnectionsList is "+availableConnectionsList.getText());
		List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
		System.out.println("list is "+availableConnections);
		for (WebElement connection : availableConnections) {
		boolean status = false;	
		for (Device deviceList : devices)
		 {
			System.out.println("Checking for connection  "+connection.getText()+" in the device list "+deviceList.getIpAddress());
		//	System.out.println("Devices name is "+devices.toString());
			if(connection.getText().equalsIgnoreCase(deviceList.getIpAddress())) {
				status=true;
				Assert.assertTrue(status,"Connection Name "+connection+" shown in RemoteApp has not been assigned to the user");
				System.out.println(" connection "+connection.getText()+" is assigned to the correct user");
				break;
			}
			
//			System.out.println("Connection name - "+connection+" has been assigned correctly o the User");
		}}
		closeApp();
		cleanUpLogin();
		UserPage.DeleteUser(firedrive,"TestUser1");
		cleanUpLogout();
		
	}
	
	//Test to ensure all the user(administrator, power and General) have same privileges
	//@Test
	public void Test12_AI0005() throws Exception {
		printTestDetails("STARTING ", "Test12_AI0005", "");
		cleanUpLogin();
		
		userpage.createUser(firedrive,devices,"TestUser1","TestUser1","General");
		userpage.createUser(firedrive,devices,"TestUser2","TestUser2","Power");
		userpage.createUser(firedrive,devices,"TestUser3","TestUser3","Administrator");
//		userpage.ManageConnection(firedrive, devices, "TestUser1");
//		userpage.ManageConnection(firedrive, devices, "TestUser2");
//		userpage.ManageConnection(firedrive, devices, "TestUser3");
		cleanUpLogout();
		for(int i=1;i<4;i++) {
			RAlogin("TestUser"+i,"TestUser"+i);
			WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
			System.out.println("availableConnectionsList is "+availableConnectionsList.getText());
			List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
			System.out.println("list is "+availableConnections);
			for (WebElement connection : availableConnections) {
			boolean status = false;	
			for (Device deviceList : devices)
			 {
				System.out.println("Checking for connection  "+connection.getText()+" in the device list "+deviceList.getIpAddress());
			//	System.out.println("Devices name is "+devices.toString());
				if(connection.getText().equalsIgnoreCase(deviceList.getIpAddress())) {
					status=true;
					Assert.assertTrue(status,"Connection Name "+connection+" shown in RemoteApp has not been assigned to the user");
					System.out.println(" connection "+connection.getText()+" is assigned to the correct user");
					break;
				}}}
			closeApp();}
			cleanUpLogin();
			for(int j=1;j<4;j++) {
			UserPage.DeleteUser(firedrive,"TestUser"+j);
			}
			cleanUpLogout();
			
	}
		
	//@Test
	public void Test13_AI0022() throws Exception {
		printTestDetails("STARTING ", "Test13_AI0022", "");
		cleanUpLogin();
		userpage.user(firedrive).click();
		firedrive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		userpage.manage(firedrive).click();
		userpage.searchOption(firedrive).sendKeys("admin");
		userpage.optionbutton(firedrive).click();
		userpage.EditUser(firedrive).click();
		userpage.NextButton(firedrive).click();
		userpage.RemoteAccess(firedrive).click();
		userpage.NextButton(firedrive).click();
		userpage.savebutton(firedrive).click();
		firedrive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		firedrive.navigate().refresh();
		cleanUpLogout();
		RAlogin("admin","admin");
		WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
		List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
		Assert.assertTrue(devices.equals(availableConnections),"All Connections are not shown in RemoteApp for  Administrator user");
		closeApp();
		
	}
	
	//@Test
	public void Test14_AI0032() throws Exception {
		printTestDetails("STARTING ", "Test14_AI0032", "");
		RAlogin(RAusername,RApassword);
		WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
		for (String connectionName : connectionList) {
			WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionName));
			
		     Actions a = new Actions(Windriver);
		      a.moveToElement(targetConnection).
		      doubleClick().
		      build().perform();
		      Thread.sleep(30000);
		      System.out.println(connectionName+" has been launched");
		      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			}
			Thread.sleep(60000);
			cleanUpLogin();
			userpage.user(firedrive).click();
			firedrive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			userpage.manage(firedrive).click();
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
			firedrive.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			cleanUpLogout();
			Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			RestAssured.useRelaxedHTTPSValidation();
			String gerdetails = RestAssured.given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers("Content-Type", "application/json", "Accept","application/json")
								.when().get("https://"+boxillaManager+"/bxa-api/connections/kvm/active")
								.then().assertThat().statusCode(200)
								.extract().response().asString();
			 System.out.println(gerdetails);
		     
				System.out.println("********************checking the connection status  ******************");
				JsonPath js = new JsonPath(gerdetails);
				int	countconnection=js.getInt("message.active_connections.size()");
				
		
	}
	
	//@Test
	public void Test14_AI0043() throws Exception {
		printTestDetails("STARTING ", "Test14_AI0043", "");
		RAlogin(RAusername,RApassword);
		WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
		for (String connectionName : connectionList) {
			WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionName));
			
		     Actions a = new Actions(Windriver);
		      a.moveToElement(targetConnection);
		      System.out.println("Cursor could move to connection "+connectionName);
		    //  Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			}
		
		
	}
	
	//@Test 
	public void Test15_CL0006a() throws Exception{
		List<String> SharedNames = new ArrayList<String>();
		SharedNames.add("10.211.130.114test1");
		SharedNames.add("10.211.130.114test2");
		SharedNames.add("10.211.130.114test3");
		SharedNames.add("10.211.130.114test4");
		
		
		printTestDetails("STARTING ", "Test15_CL0006a", "");
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
	      Thread.sleep(30000);
		}
		 System.out.println("**********checking launched connection - responses******************");
		   
		  RestAssured.useRelaxedHTTPSValidation();
	      String Connectiondetails = RestAssured.given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers("Content-Type", "application/json", "Accept","application/json")
							.when().get("https://"+boxillaManager+"/bxa-api/connections/kvm/active")
							.then().assertThat().statusCode(200)
							.extract().response().asString();
		
	      System.out.println(Connectiondetails);
	      
	      System.out.println("********************checking the connection size  ******************");
			JsonPath js = new JsonPath(Connectiondetails);
			int countconnection=js.getInt("message.active_connections.size()");
			System.out.println("message size is "+count);
			Assert.assertEquals(count, countconnection);
			System.out.println("Four Connections were supported");
		
		
	}
	@Test
	public void Test16_DC0001() throws Exception {
		printTestDetails("STARTING ", "Test16_DC0001", "");
		ArrayList<Device> SEDevices = new ArrayList<Device>();
		DiscoveryMethods discoveryMethods = new DiscoveryMethods();	
		AppliancePool applian = new AppliancePool();
		ArrayList<String> connectionSEList = new ArrayList<String>();
		ArrayList<Device> remotedevice=applian.getAllDevices("deviceSE.properties");
		System.out.println(remotedevice);
		SEDevices.addAll(remotedevice);
		cleanUpLogin();
		for(Device deviceList : SEDevices) {
			System.out.println("Adding the device "+deviceList);
			discoveryMethods.addDeviceToBoxilla(firedrive, deviceList.getMac(), deviceList.getIpAddress(),
					deviceList.getGateway(),deviceList.getNetmask(), deviceList.getDeviceName(), 10);
			}
			System.out.println("*************All Devices are Managed***************");

		
	
		ConnectionPage.createprivateconnections(firedrive,remotedevice);
		userpage.ManageConnection(firedrive,remotedevice,RAusername);
		RAlogin(RAusername,RApassword);
		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		Thread.sleep(1500);
		WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
		for (WebElement connection : availableConnections) {
		
			connectionSEList.add(connection.getText());
		
			}
		
			for (String connectionName : connectionSEList) {
			  Actions a = new Actions(Windriver);
		
			  WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionName));
		      a.moveToElement(targetConnection).
		      doubleClick().
		      build().perform();
		      Thread.sleep(30000);
			}
			  RestAssured.useRelaxedHTTPSValidation();
		    String  gerdetails = RestAssured.given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers("Content-Type", "application/json", "Accept","application/json")
								.when().get("https://"+boxillaManager+"/bxa-api/connections/kvm/active")
								.then().assertThat().statusCode(200)
								.extract().response().asString();
			
		      System.out.println(gerdetails);
		      for (String connectionName : connectionSEList) {
			    	Windriver.findElement(By.name(connectionName)).click();
			    	Windriver.findElement(By.name("Disconnect")).click();
				    Thread.sleep(5000);
				    System.out.println("connection "+connectionName+" is disconnected");
				    Windriver.switchTo().window(Windriver.getWindowHandle());
				    
				
		
		}
		 Windriver.switchTo().window(Windriver.getWindowHandle());
		 closeApp();
			
			softAssertion.assertAll();
			}
	
	
	//@Test
	public void Test17_DC0001() throws Exception {
		printTestDetails("STARTING ", "Test17_DC0001", "");
		ArrayList<Device> PEDevices = new ArrayList<Device>();
		DiscoveryMethods discoveryMethods = new DiscoveryMethods();	
		AppliancePool applian = new AppliancePool();
		ArrayList<String> connectionPEList = new ArrayList<String>();
		ArrayList<Device> remotedevice=applian.getAllDevices("devicePE.properties");
		System.out.println(remotedevice);
		PEDevices.addAll(remotedevice);
		cleanUpLogin();
		for(Device deviceList : PEDevices) {
			System.out.println("Adding the device "+deviceList);
			discoveryMethods.addDeviceToBoxilla(firedrive, deviceList.getMac(), deviceList.getIpAddress(),
					deviceList.getGateway(),deviceList.getNetmask(), deviceList.getDeviceName(), 10);
			}
			System.out.println("*************All Devices are Managed***************");

		
	
		ConnectionPage.createprivateconnections(firedrive,remotedevice);
		userpage.ManageConnection(firedrive,remotedevice,RAusername);
		RAlogin(RAusername,RApassword);
		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		Thread.sleep(1500);
		WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
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
			}
			  RestAssured.useRelaxedHTTPSValidation();
		    String  gerdetails = RestAssured.given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers("Content-Type", "application/json", "Accept","application/json")
								.when().get("https://"+boxillaManager+"/bxa-api/connections/kvm/active")
								.then().assertThat().statusCode(200)
								.extract().response().asString();
			
		      System.out.println(gerdetails);
		      for (String connectionName : connectionPEList) {
			    	Windriver.findElement(By.name(connectionName)).click();
			    	Windriver.findElement(By.name("Disconnect")).click();
				    Thread.sleep(5000);
				    System.out.println("connection "+connectionName+" is disconnected");
				    Windriver.switchTo().window(Windriver.getWindowHandle());
				    
				
		
		}
		 Windriver.switchTo().window(Windriver.getWindowHandle());
		 closeApp();
			
			softAssertion.assertAll();
			}
	
	
	//@Test
	public void Test18_DC0001() throws Exception {
		printTestDetails("STARTING ", "Test18_DC0001", "");
		ArrayList<Device> ZeroUDevices = new ArrayList<Device>();
		DiscoveryMethods discoveryMethods = new DiscoveryMethods();	
		AppliancePool applian = new AppliancePool();
		ArrayList<String> connectionZeroUList = new ArrayList<String>();
		ArrayList<Device> remotedevice=applian.getAllDevices("deviceZeroU.properties");
		System.out.println(remotedevice);
		ZeroUDevices.addAll(remotedevice);
		cleanUpLogin();
		for(Device deviceList : ZeroUDevices) {
			System.out.println("Adding the device "+deviceList);
			discoveryMethods.addDeviceToBoxilla(firedrive, deviceList.getMac(), deviceList.getIpAddress(),
					deviceList.getGateway(),deviceList.getNetmask(), deviceList.getDeviceName(), 10);
			}
			System.out.println("*************All Devices are Managed***************");

		
	
		ConnectionPage.createprivateconnections(firedrive,remotedevice);
		userpage.ManageConnection(firedrive,remotedevice,RAusername);
		RAlogin(RAusername,RApassword);
		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		Thread.sleep(1500);
		WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
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
		      Thread.sleep(30000);
			}
			  RestAssured.useRelaxedHTTPSValidation();
		    String  gerdetails = RestAssured.given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers("Content-Type", "application/json", "Accept","application/json")
								.when().get("https://"+boxillaManager+"/bxa-api/connections/kvm/active")
								.then().assertThat().statusCode(200)
								.extract().response().asString();
			
		      System.out.println(gerdetails);
		      for (String connectionName : connectionZeroUList) {
			    	Windriver.findElement(By.name(connectionName)).click();
			    	Windriver.findElement(By.name("Disconnect")).click();
				    Thread.sleep(5000);
				    System.out.println("connection "+connectionName+" is disconnected");
				    Windriver.switchTo().window(Windriver.getWindowHandle());
				    
				
		
		}
		 Windriver.switchTo().window(Windriver.getWindowHandle());
		 closeApp();
			
			softAssertion.assertAll();
			}
	
	//@Test
	public void Test19_SR0005() throws Exception {
		printTestDetails("STARTING ", "Test19_SR0005", "");
		ArrayList<Device> ZeroUDevices = new ArrayList<Device>();
		DiscoveryMethods discoveryMethods = new DiscoveryMethods();	
		AppliancePool applian = new AppliancePool();
		ArrayList<String> connectionZeroUList = new ArrayList<String>();
		ArrayList<Device> remotedevice=applian.getAllDevices("deviceZeroU.properties");
		System.out.println(remotedevice);
		ZeroUDevices.addAll(remotedevice);
		cleanUpLogin();
		for(Device deviceList : ZeroUDevices) {
			System.out.println("Adding the device "+deviceList);
			discoveryMethods.addDeviceToBoxilla(firedrive, deviceList.getMac(), deviceList.getIpAddress(),
					deviceList.getGateway(),deviceList.getNetmask(), deviceList.getDeviceName(), 10);
			}
			System.out.println("*************All Devices are Managed***************");

			ArrayList<String> Sharedconnection = new 	ArrayList<String>();	
			for(Device deviceList : remotedevice) {
				System.out.println("Adding the connection "+deviceList.getIpAddress());
				for(int i =1;i<=2;i++) {
					ConnectionPage.connections(firedrive).click();
					firedrive.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
					ConnectionPage.manage(firedrive).click();
				firedrive.manage().timeouts().implicitlyWait(3,TimeUnit.SECONDS);
				ConnectionPage.newconnection(firedrive).click();
				firedrive.manage().timeouts().implicitlyWait(4,TimeUnit.SECONDS);
				ConnectionPage.connectionName(firedrive).sendKeys(deviceList.getIpAddress()+"test"+i);
				ConnectionPage.Host(firedrive).sendKeys(deviceList.getIpAddress());
				ConnectionPage.optimised(firedrive).click();
				ConnectionPage.nextoption(firedrive).click();
				ConnectionPage.privateconnectionType(firedrive).click();
				ConnectionPage.Audio(firedrive).click();
				ConnectionPage.nextoption(firedrive).click();
				firedrive.manage().timeouts().implicitlyWait(4,TimeUnit.SECONDS);
				ConnectionPage.Saveoption(firedrive).click();
				firedrive.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
				//wait.until(ExpectedConditions.visibilityOfAllElements(ConnectionPage.connectiontable(firedrive)));
				Sharedconnection.add(deviceList.getIpAddress()+"test"+i);
				ConnectionPage.searchOption(firedrive).sendKeys(deviceList.getIpAddress()+"test"+i);
				System.out.println("Connection Name entered in search box");
				firedrive.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
				Thread.sleep(4000);
			//	Wait.until(ExpectedConditions.visibilityOfAllElements(connectiontable(driver)));
				String deviceApplianceTable = SeleniumActions.seleniumGetText(firedrive, Devices.applianceTable);
				Assert.assertTrue(deviceApplianceTable.contains(deviceList.getIpAddress()+"test"+i),
						"Table did not contain: " + deviceList.getIpAddress()+"test"+i + ", actual text: " + deviceApplianceTable);
			//	userpage.ManageConnection(firedrive,deviceList.getIpAddress()+"test"+i,RAusername);
			}
				UserPage.user(firedrive).click();
				firedrive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				UserPage.manage(firedrive).click();
				UserPage.searchOption(firedrive).sendKeys(RAusername);
				System.out.println("Username "+RAusername+" entered in search box");
				Thread.sleep(2000);
				String deviceApplianceTable = SeleniumActions.seleniumGetText(firedrive, Devices.applianceTable);
				Assert.assertTrue(deviceApplianceTable.contains(RAusername),
						"Device appliance table did not contain: " + RAusername + ", actual text: " + deviceApplianceTable);
				firedrive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				userpage.optionbutton(firedrive).click();
				firedrive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				userpage.ManageConnections(firedrive).click();
				firedrive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				userpage.Movebackward(firedrive).click();
				for(String connectedName : Sharedconnection) {
					firedrive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
					userpage.connectionfilter(firedrive).sendKeys(connectedName);
					firedrive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
					userpage.Moveforward(firedrive).click();
					firedrive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
					userpage.connectionfilter(firedrive).clear();
				}
				userpage.Connectionsave(firedrive).click();
				System.out.println("All connections have been assigned to User");
		//ConnectionPage.createprivateconnections(firedrive,remotedevice);
		Thread.sleep(3000);
		RAlogin(RAusername,RApassword);
		//Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		Thread.sleep(1500);
		WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
		for (WebElement connection : availableConnections) {
		
			connectionZeroUList.add(connection.getText());
		
			}
		int count=1;
			for (String connectionName : connectionZeroUList) {
			  Actions a = new Actions(Windriver);
		
			  WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionName));
		      a.moveToElement(targetConnection).
		      doubleClick().
		      build().perform();
		      System.out.println("connection named "+connectionName+" has been launched");
		      if(count==2) {
		    	  WebElement windowsPopupOpenButton = Windriver.findElementByName("Private/Shared Violation");
		          String text= windowsPopupOpenButton.getText();
		  		Windriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); 
		  		System.out.println("Alert Message is  "+text);
		  		Assert.assertTrue(text.equals("Incorrect Boxilla IP"),"No pop up Message  stating - Boxilla has not been configured");
		  		Windriver.findElementByName("OK").click();
		    	  
		      }
		      Thread.sleep(20000);
		      count++;
		      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			}
			
			for (String connectionName : connectionZeroUList) {
		    	Windriver.findElement(By.name(connectionName)).click();
		    	Windriver.findElement(By.name("Disconnect")).click();
			    Thread.sleep(5000);
			    System.out.println("connection "+connectionName+" is disconnected");
			    Windriver.switchTo().window(Windriver.getWindowHandle());
			    
			
	
	}
	 Windriver.switchTo().window(Windriver.getWindowHandle());
	 closeApp();
		
		softAssertion.assertAll();
		
	}
	}
	
	
	//@Test
	public void Test20_AI0038() throws Exception {
		printTestDetails("STARTING ", "Test20_AI0038", "");
		ArrayList<String> connectionZeroUList = new ArrayList<String>();
		RAlogin(RAusername,RApassword);
		//Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		Thread.sleep(1500);
		WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
		for (WebElement connection : availableConnections) {
			connectionZeroUList.add(connection.getText());
		}
			  Actions a = new Actions(Windriver);
			  WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionZeroUList.get(0)));
		      a.moveToElement(targetConnection).
		      doubleClick().
		      build().perform();
		      System.out.println("connection named "+connectionZeroUList.get(0)+" has been launched");
		      Thread.sleep(20000);
		      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		      closeApp();
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
			   if(countconnection==0) {
				   System.out.println("RemoteApp is closed and all the connectections are terminated");
				   Assert.assertTrue(true);
				   
			   }else Assert.assertFalse(true, +countconnection+" Connection is still active");
		     
		
	}
	
	//@Test
	public void Test21_AI0048() throws Exception {
		printTestDetails("STARTING ", "Test21_AI0048", "");
		ArrayList<Device> PEDevices = new ArrayList<Device>();
		DiscoveryMethods discoveryMethods = new DiscoveryMethods();	
		AppliancePool applian = new AppliancePool();
		ArrayList<String> connectionPEList = new ArrayList<String>();
		ArrayList<Device> remotedevice=applian.getAllDevices("devicePE.properties");
		System.out.println(remotedevice);
		PEDevices.addAll(remotedevice);
		cleanUpLogin();
		for(Device deviceList : PEDevices) {
			System.out.println("Adding the device "+deviceList);
			discoveryMethods.addDeviceToBoxilla(firedrive, deviceList.getMac(), deviceList.getIpAddress(),
					deviceList.getGateway(),deviceList.getNetmask(), deviceList.getDeviceName(), 10);
			}
			System.out.println("*************All Devices are Managed***************");

		
	
		ConnectionPage.createprivateconnections(firedrive,remotedevice);
		userpage.ManageConnection(firedrive,remotedevice,RAusername);
		RAlogin(RAusername,RApassword);
		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		Thread.sleep(1500);
		WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
		for (WebElement connection : availableConnections) {
		
			connectionPEList.add(connection.getText());
		
			}
					
			  Actions a = new Actions(Windriver);
			  WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionPEList.get(0)));
		      a.moveToElement(targetConnection).
		      doubleClick().
		      build().perform();
		      Thread.sleep(1000);
		     
		      WebElement windowsPopupOpenButton = Windriver.findElementByName("Connection Launch:"+connectionPEList.get(0));
	          String text= windowsPopupOpenButton.getText();
	  		Windriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); 
	  		System.out.println("Alert Message is  "+text);
	  		Assert.assertTrue(text.contains("Connection Launch:"),"No pop up Message  stating - Boxilla has not been configured");
	  		Windriver.findElementByName("OK").click();
		    Thread.sleep(30000);
			Windriver.findElement(By.name(connectionPEList.get(0))).click();
			Windriver.findElement(By.name("Disconnect")).click();
			 WebElement windowsdisconnectButton = Windriver.findElementByName("Connection Launch:"+connectionPEList.get(0));
	          String disconnecttext= windowsPopupOpenButton.getText();
	  		Windriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); 
	  		System.out.println("Alert Message is  "+text);
			
			Thread.sleep(5000);
				
		 Windriver.switchTo().window(Windriver.getWindowHandle());
		 closeApp();
		softAssertion.assertAll();
		
	
}
	//@Test
	public void Test22_SR0013() throws Exception {
		printTestDetails("STARTING ", "Test22_SR0013", "");
		WebDriverWait wait=new WebDriverWait(firedrive, 20);
		ArrayList<Device> ZeroUDevices = new ArrayList<Device>();
		DiscoveryMethods discoveryMethods = new DiscoveryMethods();	
		AppliancePool applian = new AppliancePool();
		ArrayList<String> connectionZeroUList = new ArrayList<String>();
		ArrayList<Device> remotedevice=applian.getAllDevices("deviceZeroU.properties");
		System.out.println(remotedevice);
		ZeroUDevices.addAll(remotedevice);
		cleanUpLogin();
		for(Device deviceList : ZeroUDevices) {
			System.out.println("Adding the device "+deviceList);
			discoveryMethods.addDeviceToBoxilla(firedrive, deviceList.getMac(), deviceList.getIpAddress(),
					deviceList.getGateway(),deviceList.getNetmask(), deviceList.getDeviceName(), 10);
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
			
		for(int j=1;j<4;j++) {
			userpage.createUser(firedrive,ZeroUDevices,"TestUser"+j,"TestUser"+j,"General");
			Thread.sleep(5000);
		}
			
			cleanUpLogout();
			
			for (int i=1;i<=3;i++) {
				Thread.sleep(10000);
				RAlogin("TestUser"+i,"TestUser"+i);
				Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
				Thread.sleep(1500);
				WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
				 Actions a = new Actions(Windriver);
				  WebElement targetConnection = availableConnectionsList.findElement(By.name("TestUser"+i));
			      a.moveToElement(targetConnection).
			      doubleClick().
			      build().perform();
			      Thread.sleep(10000);
			}
			Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			closeApp();
			Thread.sleep(10000);
			RestAssured.useRelaxedHTTPSValidation();
			
		    String gerdetails = given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers("Content-Type", "application/json", "Accept","application/json")
		    		    		.when().get("https://"+boxillaManager+"/bxa-api/connections/kvm/active")
		    		    		.then().assertThat().statusCode(200)
		    		    		.extract().response().asString();
		    
		    System.out.println("Response is "+gerdetails);
		    Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		    closeApp();
	}
	
	//@Test
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
		UserPage.endTest("Test23_AI0025a");
	}
	
	//@Test
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
		
	}
//	about:config
//	security.enterprise_roots.enabled
	
		//@Test
		public void Test25_AI0033() throws Exception {
			printTestDetails("STARTING ", "Test25_AI0033", "");
			
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
			userpage.password(firedrive).sendKeys("qwert!£$%/");
			userpage.confirmPassword(firedrive).sendKeys("qwert!£$%/");
			userpage.NextButton(firedrive).click();
			Assert.assertEquals(userpage.IncorrectPassword(firedrive), "Password can't contain certain characters. Invalid characters are: \"'/\\[]:;|=,+*?<>`");
			System.out.println("Showed error Message - Password can't contain certain characters. Invalid characters are: \\\"'/\\\\[]:;|=,+*?<>`\"");
			
		}
		
		//@Test
		public void Test26_VI0005() {
			printTestDetails("STARTING ", "Test26_VI0005", "");
			
			setup();
			Windriver.findElementByName("Demo Mode").click();
			Windriver.findElementByName("Submit").click();
			Windriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			System.out.println(Windriver.getWindowHandle());
			Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		    getElement("menuLabel").click();
		    System.out.println("Menu Label clicked");
		    Windriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		    Windriver.findElementByName("Settings").click();
		    System.out.println("Settings clicked");
					 
			getElement("kryptonDockableNavigator1").click();
			getElement("Auto").click();
			
			
			
		}
		@Test
		public void Test27_DC0002() throws Exception {
			printTestDetails("STARTING ", "Test27_DC0002", "");
			ArrayList<String> DualHeadList = new ArrayList<String>();
			WebDriverWait wait=new WebDriverWait(firedrive, 20);
			
			
			ArrayList<Device> DualHeadDevices = new ArrayList<Device>();
			DiscoveryMethods discoveryMethods = new DiscoveryMethods();	
			AppliancePool applian = new AppliancePool();
			ArrayList<String> connectionDualList = new ArrayList<String>();
			ArrayList<Device> remotedevice=applian.getAllDevices("DualHead.properties");
			System.out.println(remotedevice);
			DualHeadDevices.addAll(remotedevice);
			cleanUpLogin();
			for(Device deviceList : DualHeadDevices) {
				System.out.println("Adding the device "+deviceList);
				discoveryMethods.addDeviceToBoxilla(firedrive, deviceList.getMac(), deviceList.getIpAddress(),
						deviceList.getGateway(),deviceList.getNetmask(), deviceList.getDeviceName(), 10);
				}
				System.out.println("*************All Devices are Managed***************");
				
				
			
			
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
				ConnectionPage.extendDesktop(firedrive).click();
				ConnectionPage.Audio(firedrive).click();
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
				
				RAlogin(RAusername,RApassword);
				//Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
				Thread.sleep(1500);
				WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
				Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
				List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
				for (WebElement connection : availableConnections) {
					DualHeadList.add(connection.getText());
				}
				
				for (String connectionName : DualHeadList) {
					  Actions a = new Actions(Windriver);
					  WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionName));
				      a.moveToElement(targetConnection).
				      doubleClick().
				      build().perform();
				      System.out.println("connection named "+connectionName+" has been launched");
				      Thread.sleep(20000);
				      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
				}
				      
				    //  closeApp();
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
					  
					    for (String DualName : DualHeadList) { 
					    	Windriver.findElement(By.name(DualName)).click();
					    	Windriver.findElement(By.name("Disconnect")).click();
						    Thread.sleep(3000);
						    System.out.println("connection "+DualName+" is disconnected");
						    Windriver.switchTo().window(Windriver.getWindowHandle());
						    
						
				}
					closeApp();
					
				
			}
		
		
		//@Test
		public void Test28_SR0046() throws Exception {
			printTestDetails("STARTING ", "Test28_SR0046", "");
			ArrayList<String> viewList = new ArrayList<String>();
			WebDriverWait wait=new WebDriverWait(firedrive, 20);
			
			
			ArrayList<Device> DualHeadDevices = new ArrayList<Device>();
			DiscoveryMethods discoveryMethods = new DiscoveryMethods();	
			AppliancePool applian = new AppliancePool();
			ArrayList<String> connectionDualList = new ArrayList<String>();
			ArrayList<Device> remotedevice=applian.getAllDevices("deviceZeroU.properties");
			System.out.println(remotedevice);
			DualHeadDevices.add(remotedevice.get(0));
			cleanUpLogin();
			for(Device deviceList : DualHeadDevices) {
				System.out.println("Adding the device "+deviceList);
				discoveryMethods.addDeviceToBoxilla(firedrive, deviceList.getMac(), deviceList.getIpAddress(),
						deviceList.getGateway(),deviceList.getNetmask(), deviceList.getDeviceName(), 10);
				}
				System.out.println("*************Device is Managed***************");
				
				
			
			
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
				
				RAlogin(RAusername,RApassword);
				//Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
				Thread.sleep(1500);
				WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
				Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
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
				      Thread.sleep(20000);
				      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
				}
				      
				    //  closeApp();
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
		public void Test29_SR0036() throws Exception {
			printTestDetails("STARTING ", "Test29_SR0036", "");
			ArrayList<String> viewList = new ArrayList<String>();
			WebDriverWait wait=new WebDriverWait(firedrive, 20);
			
			
			ArrayList<Device> DualHeadDevices = new ArrayList<Device>();
			DiscoveryMethods discoveryMethods = new DiscoveryMethods();	
			AppliancePool applian = new AppliancePool();
			ArrayList<String> connectionDualList = new ArrayList<String>();
			ArrayList<Device> remotedevice=applian.getAllDevices("deviceZeroU.properties");
			System.out.println(remotedevice);
			DualHeadDevices.add(remotedevice.get(0));
			cleanUpLogin();
			for(Device deviceList : DualHeadDevices) {
				System.out.println("Adding the device "+deviceList);
				discoveryMethods.addDeviceToBoxilla(firedrive, deviceList.getMac(), deviceList.getIpAddress(),
						deviceList.getGateway(),deviceList.getNetmask(), deviceList.getDeviceName(), 10);
				}
				System.out.println("*************Device is Managed***************");
				
				
			
			
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
				
				RAlogin(RAusername,RApassword);
				//Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
				Thread.sleep(1500);
				WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
				Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
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
				      Thread.sleep(20000);
				      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
				}
				      
				    //  closeApp();
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
					unManageDevice(firedrive,DualHeadDevices);
					cleanUpLogout();
				
			}
		
		
	
		
			}
		
				
		
		
		
	
	

	
	
	
	
	

