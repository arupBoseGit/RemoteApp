package tests;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import methods.AppliancePool;
import methods.Device;
import methods.Devices;
import methods.DiscoveryMethods;
import methods.SeleniumActions;
import pages.ConnectionPage;
import pages.UserPage;

public class SecondPhase extends TestBase{

	//@Test
	public void Test01_DC0002() throws Exception {
		printTestDetails("STARTING ", "Test01_DC0002", "");
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
			Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			//Thread.sleep(1500);
			WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
			//Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
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
			      System.out.println("connection named "+connectionName+" has been launched");
			      Thread.sleep(10000);
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
				cleanUpLogin();
				Thread.sleep(2000);
				ConnectionPage.DeleteConnection(firedrive, DualHeadDevices);
				UserPage.DeleteUser(firedrive, RAusername);
				cleanUpLogout();
			
			
		}
	
	//@Test - second phase create a connection in VM to launch
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
			
			//@Test //shared connection terminate and ensure the other remain active
			public void Test3_SR0013() throws Exception {
				printTestDetails("STARTING ", "Test3_SR0013", "");
				WebDriverWait wait=new WebDriverWait(firedrive, 20);
				ArrayList<Device> ZeroUDevices = new ArrayList<Device>();
				ArrayList<String> DualHeadList = new ArrayList<String>();
				DiscoveryMethods discoveryMethods = new DiscoveryMethods();	
				AppliancePool applian = new AppliancePool();
				ArrayList<String> connectionZeroUList = new ArrayList<String>();
				ArrayList<Device> remotedevice=applian.getAllDevices("Onedevice.properties");
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
					
				for(int j=1;j<3;j++) {
					userpage.createUser(firedrive,ZeroUDevices,"TestUser"+j,"TestUser"+j,"General");
					Thread.sleep(5000);
				}
					
					cleanUpLogout();
					
					for (int i=1;i<=2;i++) {
						Thread.sleep(10000);
						RAlogin("TestUser"+i,"TestUser"+i);
						//Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
						Thread.sleep(1500);
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
						      System.out.println("connection named "+connectionName+" has been launched");
						      Thread.sleep(10000);
						      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
						}
						
					}
						
					
					Thread.sleep(10000);
					RestAssured.useRelaxedHTTPSValidation();
					
				    String gerdetails = given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers("Content-Type", "application/json", "Accept","application/json")
				    		    		.when().get("https://"+boxillaManager+"/bxa-api/connections/kvm/active")
				    		    		.then().assertThat().statusCode(200)
				    		    		.extract().response().asString();
				    
				    System.out.println("Response is "+gerdetails);
				    Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);	
					closeApp();
					Thread.sleep(60000);
					RestAssured.useRelaxedHTTPSValidation();
					
				    String Secdetails = given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers("Content-Type", "application/json", "Accept","application/json")
				    		    		.when().get("https://"+boxillaManager+"/bxa-api/connections/kvm/active")
				    		    		.then().assertThat().statusCode(200)
				    		    		.extract().response().asString();
				    
				    System.out.println("Response is "+Secdetails);
				    JsonPath js = new JsonPath(Secdetails);
					int	countconnection=js.getInt("message.active_connections.size()");
					System.out.println("Number of Active connections are "+countconnection);
					System.out.println("Number of expected active connections to be 1");
					softAssert.assertEquals(countconnection,1," Number of active connection didn't match with the number of connections before changing credentials");	{
					Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
					closeApp();
				    cleanUpLogin();
				    for(int j=1;j<3;j++) {
						UserPage.DeleteUser(firedrive,"TestUser"+j);
						}
				    ConnectionPage.DeleteConnection(firedrive, ZeroUDevices);
				    cleanUpLogout();
				   
				   
					}
}
			
			//@Test
			public void Test4_AI0050() throws Exception {
				printTestDetails("STARTING ", "Test4_AI0050", "");

				Onedevices = devicePool.getAllDevices("Onedevice.properties");
				cleanUpLogin();
				ConnectionPage.createprivateconnections(firedrive,Onedevices);
				userpage.createUser(firedrive,Onedevices,RAusername,RApassword,"General");
				cleanUpLogout();
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
				
				for (String connectionName : connectionList) {
				Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
				WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionName));
				  Actions a = new Actions(Windriver);
				  count++;
			      a.moveToElement(targetConnection).
			      doubleClick().
			      build().perform();
			      System.out.println(connectionName+" has been launched");
			      }
			      Thread.sleep(10000);
			   	
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
				}
			}
			
			
			//@Test
			public void Test5_AI0050() throws Exception {
				printTestDetails("STARTING ", "Test4_AI0050", "");

				Onedevices = devicePool.getAllDevices("Onedevice.properties");
				cleanUpLogin();
				ConnectionPage.createprivateconnections(firedrive,Onedevices);
				userpage.createUser(firedrive,Onedevices,RAusername,RApassword,"General");
				cleanUpLogout();
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
				
				for (String connectionName : connectionList) {
				Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
				WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionName));
				  Actions a = new Actions(Windriver);
				  count++;
			      a.moveToElement(targetConnection).
			      doubleClick().
			      build().perform();
			      System.out.println(connectionName+" has been launched");
			      Thread.sleep(10000);
			      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
				System.out.println("Screentext is "+ Windriver.findElementByWindowsUIAutomation(connectionName).getText());
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
					softAssert.assertEquals(countconnection,connectionList.size()," Number of active connection didn't match with the number of connections before changing credentials");	{
					
					Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
					
//					for (String connectionName : connectionList) {
//				    	Windriver.findElement(By.name(connectionName)).click();
//				    	Windriver.findElement(By.name("Disconnect")).click();
//					    Thread.sleep(3000);
//					    System.out.println("connection "+connectionName+" is disconnected");
//					    Windriver.switchTo().window(Windriver.getWindowHandle());
//					    
//					
//			}
//				closeApp();
//				cleanUpLogin();
//				ConnectionPage.DeleteConnection(firedrive, Onedevices);
//				UserPage.DeleteUser(firedrive, RAusername);
//				cleanUpLogout();
				}
			}
			
			@Test
			public void Test06_CL0005a() throws Exception {
				printTestDetails("STARTING ", "Test06_CL0005a", "");

				Onedevices = devicePool.getAllDevices("Onedevice.properties");
				cleanUpLogin();
				SharedNames=ConnectionPage.Sharedconnection(firedrive,Onedevices,5);
				//UserPage.Sharedconnectionassign(firedrive, RAusername, SharedNames);
				userpage.createUser(firedrive,Onedevices,RAusername,RApassword,"General");
				cleanUpLogout();
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
				for(int i=1;i<6;i++) {
				ConnectionPage.DeleteConnection(firedrive, Onedevices);
				}
				UserPage.DeleteUser(firedrive, RAusername);
				}
			
			
			
}
//			"/Pane[@ClassName=\"#32769\"][@Name=\"Desktop 1\"]/Window[@ClassName=\"wCloudBB\"][@Name=\"10.211.130.215\"]/TitleBar[@AutomationId=\"TitleBar\"]"
//			title
//			\"]/Window[@ClassName=\"wCloudBB\"][@Name=\"10.211.130.215\"]/TitleBar[@AutomationId=\"TitleBar\"]"
//			maximize
//			=\"10.211.130.215\"]/TitleBar[@AutomationId=\"TitleBar\"]/Button[@Name=\"Maximise\"][@AutomationId=\"Maximize-Restore\"]"
//			minimize
//			=\"10.211.130.215\"]/TitleBar[@AutomationId=\"TitleBar\"]/Button[@Name=\"Minimise\"][@AutomationId=\"Minimize-Restore\"]"


