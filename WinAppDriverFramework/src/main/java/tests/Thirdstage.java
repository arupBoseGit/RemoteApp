package tests;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import groovy.util.logging.Log4j;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import methods.AppliancePool;
import methods.Device;
import methods.Devices;
import methods.DiscoveryMethods;
import methods.SeleniumActions;
import pages.ConnectionPage;
import pages.UserPage;
import static io.restassured.RestAssured.given;

public class Thirdstage extends TestBase{

	
	final static Logger log = Logger.getLogger(SecondPhase.class);
	
	
	public class Reboot{
		String devices[];
	}
	//@Test(priority=1)//Launch dualHead without Extended Desktop for different users
	public void Test01_SR0018() throws Exception {
		printTestDetails("STARTING ", "Test01_SR0018", "");
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
		ConnectionPage.createprivateconnections(firedrive, remotedevice);
		for (int i=1;i<4;i++) {
			userpage.createUser(firedrive,DualHeadDevices,"TestUser"+i,"TestUser"+i,"General");
		}
			cleanUpLogout();
		for(int j=1;j<4;j++) {	
			RAlogin("TestUser"+j,"TestUser"+j);
			Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
			List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
			DualHeadList.clear();
			for (WebElement connection : availableConnections) {
				DualHeadList.add(connection.getText());
			}
			System.out.println("List is "+DualHeadList);
			for (String connectionName : DualHeadList) {
				  Actions a = new Actions(Windriver);
				  WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionName));
			      a.moveToElement(targetConnection).
			      doubleClick().
			      build().perform();
			      System.out.println("connection named "+connectionName+" has been launched by user "+"TestUser"+j);
			      Thread.sleep(30000);
			      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			      
			}
			closeApp();
		}
			     
			   
					RestAssured.useRelaxedHTTPSValidation();
				
				    String gerdetails = RestAssured.given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers("Content-Type", "application/json", "Accept","application/json")
				    		    		.when().get("https://"+boxillaManager+"/bxa-api/connections/kvm/active")
				    		    		.then().assertThat().statusCode(200)
				    		    		.extract().response().asString();
				    
				    System.out.println("Response is "+gerdetails);
				    
				    JsonPath js1 = new JsonPath(gerdetails);
				    int count=js1.getInt("message.size()");
				    int countconnection=js1.getInt("message.active_connections.size()");
				    System.out.println("connection size is "+countconnection);
				  softAssert.assertEquals(countconnection, 0," Active connection expected to be Zero but found "+countconnection);
				   
				
				cleanUpLogin();
				
				ConnectionPage.DeleteConnection(firedrive, remotedevice);
				for(int user=1;user<4;user++) {
				UserPage.DeleteUser(firedrive, "TestUser"+user);
				}
				cleanUpLogout();
			
			
		}
	//@Test
	public void Test02_SR0024() throws Exception {
		printTestDetails("STARTING ", "Test02_SR0024", "");
		ArrayList<String> RAVM = new ArrayList<String>();
		RAVM.add(VMIp);
		cleanUpLogin();
		ConnectionPage.CreateRDPconnection(firedrive, VMIp, VMUsername, VMPassword, VMDomainName);
		userpage.createUser(firedrive,null,RAusername,RApassword,"General");
		userpage.Sharedconnectionassign(firedrive, RAusername, RAVM);
		cleanUpLogout();
		RAlogin(RAusername, RApassword);
		WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
		List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
		connectionList.clear();
		int connectionNumber = 0;
		for (WebElement connection : availableConnections) {
		  System.out.println("connections number  "+connectionNumber+" is "+connection.getText());
		  connectionList.add(connection.getText());
		  connectionNumber++;
		}
		
		for (String connectionName : connectionList) {
		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionName));
		  Actions a = new Actions(Windriver);
		  a.moveToElement(targetConnection).
	      doubleClick().
	      build().perform();
		  Thread.sleep(90000);
	}
		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		RestAssured.useRelaxedHTTPSValidation();
		
	    String gerdetails = RestAssured.given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers("Content-Type", "application/json", "Accept","application/json")
	    		    		.when().get("https://"+boxillaManager+"/bxa-api/connections/kvm/active")
	    		    		.then().assertThat().statusCode(200)
	    		    		.extract().response().asString();
	    
	    System.out.println("Response is "+gerdetails);
	    
	    JsonPath js1 = new JsonPath(gerdetails);
	    int count=js1.getInt("message.size()");
	    int countconnection=js1.getInt("message.active_connections.size()");
	    System.out.println("connection size is "+countconnection);
	    Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
	    for (String DualName : connectionList) { 
	    	Windriver.findElement(By.name(DualName)).click();
	    	Windriver.findElement(By.name("Disconnect")).click();
		    Thread.sleep(3000);
		    System.out.println("connection "+DualName+" is disconnected");
		    Windriver.switchTo().window(Windriver.getWindowHandle());
	}
	    closeApp();
		cleanUpLogin();
		Thread.sleep(2000);
		ConnectionPage.DeleteSharedConnection(firedrive, RAVM);
		UserPage.DeleteUser(firedrive, RAusername);
		cleanUpLogout();
	}
	
	
	//@Test
	public void Test03_Performance() throws Exception {
		printTestDetails("STARTING ", "Test03_Performance", "");
		float ActiveconnectionFPS = 0;
		Onedevices = devicePool.getAllDevices("Onedevice.properties");
		cleanUpLogin();
		ConnectionPage.createprivateconnections(firedrive,Onedevices);
		userpage.createUser(firedrive,Onedevices,RAusername,RApassword,"General");
		cleanUpLogout();
		
			RAlogin(RAusername,RApassword);
			WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
			List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
			connectionList.clear();
			for (WebElement connection : availableConnections) {
			  System.out.println("connections number is "+connection.getText());
			  connectionList.add(connection.getText());
			
			}
			
			for (String connectionName : connectionList) {
			Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionName));
			  Actions a = new Actions(Windriver);
			  a.moveToElement(targetConnection).
		      doubleClick().
		      build().perform();
		      System.out.println(connectionName+" has been launched");
		      
		      Thread.sleep(90000);
		   	
		   	RestAssured.useRelaxedHTTPSValidation();
			String gerdetails = RestAssured.given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers("Content-Type", "application/json", "Accept","application/json")
								.when().get("https://"+boxillaManager+"/bxa-api/connections/kvm/active")
								.then().assertThat().statusCode(200)
								.extract().response().asString();
			 System.out.println("Active connection status"+gerdetails);
		     
				System.out.println("********************checking the connection status  ******************");
				JsonPath js1 = new JsonPath(gerdetails);
				int	RAcountconnection=js1.getInt("message.active_connections.size()");
				float ActiveRAconnectionFPS = js1.get("message.active_connections[0].fps");
				float droppedFPS = js1.get("message.active_connections[0].dropped_fps");
				System.out.println("Number of Active connections are "+RAcountconnection);
				System.out.println("Number of expected active connections to be "+connectionList.size());
				log.info("FPS of the launched connection "+connectionName+ " is "+ActiveRAconnectionFPS);
				log.info("Dropped FPS of the launched connection "+connectionName+ " is "+droppedFPS);
				softAssert.assertEquals(RAcountconnection, 1," Number of active connection didn't match with the number of connections before changing credentials");	
				}	
				Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
				
				for (String connectionName : connectionList) {
			    	Windriver.findElement(By.name(connectionName)).click();
			    	Windriver.findElement(By.name("Disconnect")).click();
				    Thread.sleep(3000);
				    System.out.println("connection "+connectionName+" is disconnected");
				    Windriver.switchTo().window(Windriver.getWindowHandle());
				    
				
		}
			
			closeApp();
			cleanUpLogin();
			ConnectionPage.DeleteConnection(firedrive, Onedevices);
			UserPage.DeleteUser(firedrive, RAusername);
			cleanUpLogout();
			softAssert.assertAll();
				}		
	
	
	//@Test
	public void Test04_SR0001() throws Exception {
		printTestDetails("STARTING ", "Test04_SR0001", "");
		float ActiveconnectionFPS = 0;
		
		Onedevices = devicePool.getAllDevices("Onedevice.properties");
		try {
		cleanUpLogin();
		ArrayList<String> connectionName=ConnectionPage.CreateConnection(firedrive, Onedevices, 1, "Shared");//Sharedconnection(firedrive, Onedevices, 1, "shared");
		ConnectionPage.launchConnection(firedrive,connectionName,"Test_RX_Dual_Pe");
		userpage.createUser(firedrive,Onedevices,RAusername,RApassword,"General");
		cleanUpLogout();
		Thread.sleep(90000);
		RestAssured.useRelaxedHTTPSValidation();
		String bxaActiveDetails = RestAssured.given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers("Content-Type", "application/json", "Accept","application/json")
							.when().get("https://"+boxillaManager+"/bxa-api/connections/kvm/active")
							.then().assertThat().statusCode(200)
							.extract().response().asString();
		 System.out.println("Active connection status"+bxaActiveDetails);
	     
			System.out.println("********************checking the connection status  ******************");
			JsonPath js = new JsonPath(bxaActiveDetails);
			int	countconnection=js.getInt("message.active_connections.size()");
			ActiveconnectionFPS = js.get("message.active_connections[0].fps");
			System.out.println("TX-RX Active connection FPS is : "+ActiveconnectionFPS);
			//
			
			int count=0;
			int connectionNumber=1;
			RAlogin(RAusername,RApassword);
			WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
			List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
			connectionList.clear();
			for (WebElement connection : availableConnections) {
			  System.out.println("connections number  "+connectionNumber+" is "+connection.getText());
			  connectionList.add(connection.getText());
			  connectionNumber++;
			}
			
			for (String RAconnectionName : connectionList) {
			Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			WebElement targetConnection = availableConnectionsList.findElement(By.name(RAconnectionName));
			  Actions a = new Actions(Windriver);
			  count++;
		      a.moveToElement(targetConnection).
		      doubleClick().
		      build().perform();
		      System.out.println(RAconnectionName+" has been launched through RemoteApp");
		      }
		      Thread.sleep(90000);
		   	
		   	RestAssured.useRelaxedHTTPSValidation();
			String gerdetails = RestAssured.given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers("Content-Type", "application/json", "Accept","application/json")
								.when().get("https://"+boxillaManager+"/bxa-api/connections/kvm/active")
								.then().assertThat().statusCode(200)
								.extract().response().asString();
			 System.out.println("Active connection status"+gerdetails);
		     
				System.out.println("********************checking the connection status  ******************");
				JsonPath js1 = new JsonPath(gerdetails);
				int	RAcountconnection=js1.getInt("message.active_connections.size()");
				float ActiveRAconnectionFPS = js1.get("message.active_connections[0].fps");
				System.out.println("RA-Tx Active connection FPS is : "+ActiveRAconnectionFPS);
				System.out.println("Number of Active connections are "+RAcountconnection);
				System.out.println("Number of expected active connections to be "+connectionList.size());
				softAssert.assertEquals(RAcountconnection, 2," Number of active connection didn't match with the number of connections before changing credentials");	
				
				Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
				
				for (String RAconnectionName : connectionList) {
			    	Windriver.findElement(By.name(RAconnectionName)).click();
			    	Windriver.findElement(By.name("Disconnect")).click();
				    Thread.sleep(3000);
				    System.out.println("connection "+RAconnectionName+" is disconnected");
				    Windriver.switchTo().window(Windriver.getWindowHandle());
				    
				
		}
			softAssert.assertEquals(ActiveconnectionFPS, ActiveRAconnectionFPS,"Mismatch in FPS values in TX-RX and RA-Tx connections");
			closeApp();
			cleanUpLogin();
			ConnectionPage.BreakboxillaConnection(firedrive);
			ConnectionPage.DeleteConnection(firedrive, Onedevices);
			UserPage.DeleteUser(firedrive, RAusername);
			cleanUpLogout();
			softAssert.assertAll();
				
		}
		catch(Exception e){
		e.printStackTrace();
		closeApp();
		cleanUpLogin();
		ConnectionPage.BreakboxillaConnection(firedrive);		
		ConnectionPage.DeleteConnection(firedrive, Onedevices);
		UserPage.DeleteUser(firedrive, RAusername);
		cleanUpLogout();
		}
		

	}
	
	//@Test //set smart sizing and launch VM connection
	public void Test05_VI0007() throws Exception {
		printTestDetails("STARTING ", "Test05_VI0007", "");
		setup();
		Windriver.manage().timeouts().implicitlyWait(3,TimeUnit.SECONDS);
		Windriver.findElementByAccessibilityId("DemoModeCheckBox").click();
		Windriver.findElementByName("Submit").click();
		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		getElement("menuLabel").click();
		Windriver.findElementByName("Settings").click();
		System.out.println("Clicking on the Connection Window");
		getElement("settingsNavigation").click();
		String resolution=getElement("windowResolutionComboBox").getText();
		System.out.println("Connection Window resolution is "+resolution);
		getElement("smartSizingCheckBox").click();
		softAssert.assertTrue(resolution.equalsIgnoreCase("Auto"), "Connection Window resolution is not Auto");
		
		
		
		
		
		
	}
	
	@Test
	public void Test06_SR0025() throws Exception {
		printTestDetails("STARTING ", "Test06_SR0025", "");
		float ActiveconnectionFPS = 0;
		Onedevices = devicePool.getAllDevices("Onedevice.properties");
		cleanUpLogin();
		ArrayList<String> RAconnectionName=ConnectionPage.CreateConnection(firedrive, Onedevices, 1, "Private");//createprivateconnections(firedrive,Onedevices);
		userpage.createUser(firedrive,Onedevices,RAusername,RApassword,"General");
		cleanUpLogout();
		
			RAlogin(RAusername,RApassword);
			WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
			List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
			connectionList.clear();
			for (WebElement connection : availableConnections) {
			  System.out.println("connections number is "+connection.getText());
			  connectionList.add(connection.getText());
			
			}
			
			for (String connectionName : connectionList) {
			Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionName));
			  Actions a = new Actions(Windriver);
			  a.moveToElement(targetConnection).
		      doubleClick().
		      build().perform();
		      System.out.println(connectionName+" has been launched");
		      
		      Thread.sleep(90000);
		      String[] device= {"Test_TX_Emerald_ZeroU"};
		      Reboot reboot = new Reboot();
		      reboot.devices= device;
				
		   	RestAssured.useRelaxedHTTPSValidation();
			String gerdetails = RestAssured.given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers("Content-Type", "application/json", "Accept","application/json")
								.when().get("https://"+boxillaManager+"/bxa-api/connections/kvm/active")
								.then().assertThat().statusCode(200)
								.extract().response().asString();
			 System.out.println("Active connection status"+gerdetails);
		     
//				System.out.println("********************checking the connection status  ******************");
//				JsonPath js1 = new JsonPath(gerdetails);
//				int	RAcountconnection=js1.getInt("message.active_connections.size()");
//				float ActiveRAconnectionFPS = js1.get("message.active_connections[0].fps");
//				float droppedFPS = js1.get("message.active_connections[0].dropped_fps");
//				System.out.println("Number of Active connections are "+RAcountconnection);
//				System.out.println("Number of expected active connections to be "+connectionList.size());
//				log.info("FPS of the launched connection "+connectionName+ " is "+ActiveRAconnectionFPS);
//				log.info("Dropped FPS of the launched connection "+connectionName+ " is "+droppedFPS);
//				softAssert.assertEquals(RAcountconnection, 1," Number of active connection didn't match with the number of connections before changing credentials");	
//				}	
			
		//	String Rebootdetails
			String response= given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers("Content-Type", "application/json", "Accept","application/json")
					.body(reboot).when().post("https://"+boxillaManager+"/bxa-api/devices/kvm/reboot")
					.then().assertThat().statusCode(200)
					.extract().response().asString();
			System.out.println("Reboot Transmitter status"+response);
				Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
				
//				for (String connectionName : connectionList) {
//			    	Windriver.findElement(By.name(connectionName)).click();
//			    	Windriver.findElement(By.name("Disconnect")).click();
//				    Thread.sleep(3000);
//				    System.out.println("connection "+connectionName+" is disconnected");
//				    Windriver.switchTo().window(Windriver.getWindowHandle());
//				    
//				
//		}
			
			closeApp();
			cleanUpLogin();
			ConnectionPage.DeleteConnection(firedrive, Onedevices);
			UserPage.DeleteUser(firedrive, RAusername);
			cleanUpLogout();
			softAssert.assertAll();
				}		
	}
	
	}
