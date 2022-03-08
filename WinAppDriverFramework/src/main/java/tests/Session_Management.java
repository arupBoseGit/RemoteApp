package tests;

import static io.restassured.RestAssured.given;

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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import io.appium.java_client.windows.WindowsDriver;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import methods.Device;
import methods.Devices;
import methods.SeleniumActions;
import pages.ConnectionPage;
import pages.LandingPage;
import pages.UserPage;

public class Session_Management extends TestBase {
	
	
	final static Logger log = Logger.getLogger(Firstphase.class);
	UserPage userpage = new UserPage();
	
	//@Test //Change the appliance setting to absolute
	public void Test01_SR0018_HIDAbsolute() throws Exception {
		printTestDetails("STARTING ", "Test01_SR0018_HIDAbsolute", "");
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
		
	//@Test //Authenticate user RA test
	public void Test02_SR0021_AuthenticateUser() throws Exception {
		printTestDetails("STARTING ", "Test02_SR0021_AuthenticateUser", "");
		setup();
		WebElement loginButton = getElement("logInButton");
		getElement("userNameTextBox").sendKeys("User");
		System.out.println("User name entered as -User");
		getElement("passwordTextBox").sendKeys("User");
		System.out.println("Password entered as -User");
		loginButton.click();
		System.out.println("Login button clicked");
		//Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		new WebDriverWait(firedrive, 60).until(ExpectedConditions.visibilityOf(Windriver.findElementByAccessibilityId("TitleBar")));  
        WebElement windowsPopupOpenButton = Windriver.findElementByAccessibilityId("TitleBar");
        String text= windowsPopupOpenButton.getText();
     // capture alert message
        System.out.println("Alert Message is  "+text);
       // Assert.assertEquals("Log In: Invalid Login Credentials", text);
   
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
	
	@Test //Launch the connection in View only mode
	public void Test03_SR0046_ViewOnly() throws Exception {
		printTestDetails("STARTING ", "Test03_SR0046_ViewOnly", "");
		
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
	
	
}
