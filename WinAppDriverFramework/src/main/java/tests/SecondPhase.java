package tests;

import static io.restassured.RestAssured.given;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.testng.log4testng.Logger;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import methods.AppliancePool;
import methods.Device;
import methods.Devices;
import methods.DiscoveryMethods;
import methods.SeleniumActions;
import pages.BoxillaHeaders;
import pages.ConnectionPage;
import pages.UserPage;

public class SecondPhase extends TestBase{
	final static Logger log = Logger.getLogger(SecondPhase.class);
	@Test(priority=1)//Launch dualHead and Extended Desktop
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
			cleanUpLogout();
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
			      Thread.sleep(30000);
			      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			}
			      
			    //  closeApp();
			      Thread.sleep(30000);
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
				UserPage.DeleteUser(firedrive, RAusername);
				cleanUpLogout();
			
			
		}
	
		@Test (priority=2)//create a connection with wrong IP
		public void Test02_AI0052() throws Exception {
	//		Launch a connection in private mode
			printTestDetails("STARTING ", "Test02_AI0052", "");
			SoftAssert softAssert = new SoftAssert();
			//ArrayList<Float> sharedfps = new ArrayList<Float>();
		//	Onedevices.clear();
			Onedevices = devicePool.getAllDevices("WrongIP.properties");
			cleanUpLogin();
			ArrayList connName=ConnectionPage.CreateConnection(firedrive, Onedevices, 1, "Private");
			userpage.createUser(firedrive,null,RAusername,RApassword,"General");
			userpage.Sharedconnectionassign(firedrive, RAusername, connName);
			
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
			System.out.println(connectionList);
	//		
			for (String connectionName : connectionList) {
			
			WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionName));
			  Actions a = new Actions(Windriver);
			  count++;
		      a.moveToElement(targetConnection).
		      doubleClick().
		      build().perform();
		      Thread.sleep(2000);
		      
		    //  Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);	
		      try {
		    	 
		      new WebDriverWait(Windriver, 60).until(ExpectedConditions.visibilityOf(Windriver.findElementByAccessibilityId("TitleBar")));
		    	  WebElement windowsPopupOpenButton =Windriver.findElementByAccessibilityId("TitleBar");
			        String text= windowsPopupOpenButton.getText();
					Windriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); 
					System.out.println("Alert Message is  "+text);
//		      System.out.println(Windriver.getWindowHandles().toArray().length);
//		      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[1]);	
//		      WebElement windowsPopupOpenButton = Windriver.findElementByAccessibilityId("TitleBar");
//	        String text= windowsPopupOpenButton.getText();
//	        Windriver.findElementByName("OK").click();
	  		System.out.println("Alert Message is  "+text);
//	  		softAssert.assertEquals(text, "Unable to connect");
	  		Thread.sleep(2000);
	  		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
	  		closeApp();
			cleanUpLogin();
			Thread.sleep(2000);
			ConnectionPage.DeleteConnection(firedrive, Onedevices);
			UserPage.DeleteUser(firedrive, RAusername);
			cleanUpLogout();
		    softAssert.assertAll();
			}
		      catch(Exception e)
		      {
		    	  e.printStackTrace();
		    	  closeApp();
					cleanUpLogin();
					Thread.sleep(2000);
					ConnectionPage.DeleteConnection(firedrive, Onedevices);
					UserPage.DeleteUser(firedrive, RAusername);
					cleanUpLogout();
		      }
		    	  }
		      }
	
	
			
			@Test(priority=3) //shared connection terminate and ensure the other remain active
			public void Test03_SR0013() throws Exception {
				printTestDetails("STARTING ", "Test03_SR0013", "");
				SoftAssert softAssert = new SoftAssert();
				WebDriverWait wait=new WebDriverWait(firedrive, 20);
				ArrayList<String> sharedList = new ArrayList<String>();
				ArrayList<String> DualHeadList = new ArrayList<String>();
				DiscoveryMethods discoveryMethods = new DiscoveryMethods();	
				AppliancePool applian = new AppliancePool();
				ArrayList<String> connectionZeroUList = new ArrayList<String>();
				ArrayList<Device> remotedevice=applian.getAllDevices("Onedevice.properties");
				System.out.println(remotedevice);
			//	ZeroUDevices.addAll(remotedevice);
	try {
				cleanUpLogin();
				userpage.createUser(firedrive,null,RAusername,RApassword,"General");
				sharedList=ConnectionPage.Sharedconnection(firedrive, remotedevice, 2,"shared");
				System.out.println(sharedList);
				userpage.Sharedconnectionassign(firedrive, RAusername, sharedList);
					
					cleanUpLogout();
						Thread.sleep(10000);
						RAlogin(RAusername,RApassword);
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
						     Thread.sleep(5000);
						     Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
						}
						
						//Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
					    for (String DualName : DualHeadList) { 
					    	Windriver.findElement(By.name(DualName)).click();
					    	Windriver.findElement(By.name("Disconnect")).click();
						    Thread.sleep(20000);
						    System.out.println("connection "+DualName+" is disconnected");
						    RestAssured.useRelaxedHTTPSValidation();
							
						    String gerdetails = RestAssured.given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers("Content-Type", "application/json", "Accept","application/json")
						    		    		.when().get("https://"+boxillaManager+"/bxa-api/connections/kvm/active")
						    		    		.then().assertThat().statusCode(200)
						    		    		.extract().response().asString();
						    
						    System.out.println("Response is "+gerdetails);
				       
						    
						    JsonPath js = new JsonPath(gerdetails);
							int	countconnection=js.getInt("message.active_connections.size()");
							System.out.println("Number of Active connections are "+countconnection);
							System.out.println("Number of expected active connections to be 1");
							softAssert.assertEquals(countconnection,"1"," Number of active connection didn't match with the number of connections before changing credentials");	
							Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
							break;
							}
							
							Windriver.findElementByAccessibilityId("menuLabel").click();
							Windriver.findElementByName("Close").click();
							System.out.println("RemoteApp Closed");
							Windriver.quit();
						    cleanUpLogin();
						    UserPage.DeleteUser(firedrive,RAusername);
						  //  ConnectionPage.DeleteConnection(firedrive, remotedevice);
						    ConnectionPage.connections(firedrive).click();
						    firedrive.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
						    ConnectionPage.manage(firedrive).click();
							firedrive.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
						    	
						    for(String  deviceList : sharedList) {
								System.out.println("Deleting the connection "+deviceList);
								ConnectionPage.searchOption(firedrive).clear();
								ConnectionPage.searchOption(firedrive).sendKeys(deviceList);
								firedrive.manage().timeouts().implicitlyWait(3,TimeUnit.SECONDS);
								//if(TableContent(driver)!=0) {
								ConnectionPage.Optionicon(firedrive).click();
								ConnectionPage.Deleteoption(firedrive).click();
								firedrive.switchTo().alert().accept();
								System.out.println(deviceList+" is deleted");
								Thread.sleep(2000);
						    }
						    cleanUpLogout(); 
	}catch(Exception e) {
		 cleanUpLogin();
		    UserPage.DeleteUser(firedrive,RAusername);
		  //  ConnectionPage.DeleteConnection(firedrive, remotedevice);
		    ConnectionPage.connections(firedrive).click();
		    firedrive.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
		    ConnectionPage.manage(firedrive).click();
			firedrive.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
		    	
		    for(String  deviceList : sharedList) {
				System.out.println("Deleting the connection "+deviceList);
				ConnectionPage.searchOption(firedrive).clear();
				ConnectionPage.searchOption(firedrive).sendKeys(deviceList);
				firedrive.manage().timeouts().implicitlyWait(3,TimeUnit.SECONDS);
				//if(TableContent(driver)!=0) {
				ConnectionPage.Optionicon(firedrive).click();
				ConnectionPage.Deleteoption(firedrive).click();
				firedrive.switchTo().alert().accept();
				System.out.println(deviceList+" is deleted");
				Thread.sleep(2000);
		    }
		    cleanUpLogout();
	}
				}
					

			
			@Test(priority=4) //double click connection should Launch
			public void Test04_AI0050() throws Exception {
				printTestDetails("STARTING ", "Test04_AI0050", "");
				SoftAssert softAssert = new SoftAssert();
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
				Thread.sleep(5000);
				ConnectionPage.DeleteConnection(firedrive, Onedevices);
				UserPage.DeleteUser(firedrive, RAusername);
				cleanUpLogout();
				}
			}
			
			
			
			
			
			// manage TX and Rx, launch a connection through boxilla and note down the FPS then launch connection through RA and measure the FPS
			@Test(priority=5)
			public void Test05_RA_boxilla_Performance() throws Exception {
				
				printTestDetails("STARTING ", "Test05_RA_boxilla_Performance", "");
				SoftAssert softAssert = new SoftAssert();
				float ActiveconnectionFPS = 0;
				ArrayList<String> connectionName=null;
				Onedevices = devicePool.getAllDevices("devicePE.properties");
				try {
				cleanUpLogin();
				  ArrayList<String> list = new ArrayList<String>();
			      list.add("Test_RX_Dual_Pe");
			      
			      JSONObject reqparam = new JSONObject();
			      reqparam.put("device_names", list);
			      
			      
			   	RestAssured.useRelaxedHTTPSValidation();

				 String response = given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers(BoxillaHeaders.getBoxillaHeaders())
							.when().contentType(ContentType.JSON)
							.body(reqparam.toJSONString())
							.post("https://" + boxillaManager + "/bxa-api/devices/kvm/reboot")
							.then().assertThat().statusCode(200)
							.extract().response().asString();  
							
						
				System.out.println("Reboot Transmitter status"+response);
				Thread.sleep(10000);
				connectionName= ConnectionPage.CreateConnection(firedrive, Onedevices, 1, "Private");//createprivateconnections(firedrive,Onedevices);
				ConnectionPage.launchPrivateConnection(firedrive,connectionName,"Test_RX_Dual_Pe");
				userpage.createUser(firedrive,Onedevices,RAusername,RApassword,"General");
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
					ConnectionPage.BreakboxillaConnection(firedrive);
					System.out.println(connectionName+"Connection has been disconnected");
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
					
					for (String RAconnectionName : connectionList) {
					Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
					WebElement targetConnection = availableConnectionsList.findElement(By.name(RAconnectionName));
					  Actions a = new Actions(Windriver);
					  count++;
				      a.moveToElement(targetConnection).
				      doubleClick().
				      build().perform();
				      System.out.println(connectionName+" has been launched");
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
						softAssert.assertEquals(RAcountconnection, 1," Number of active connection didn't match with the number of connections before changing credentials");	{
						
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
					ConnectionPage.DeleteSharedConnection(firedrive, connectionName);
					UserPage.DeleteUser(firedrive, RAusername);
					cleanUpLogout();
					softAssert.assertAll();
						}
				}
				catch(Exception e){
				e.printStackTrace();
				
				closeApp();
				cleanUpLogin();
				ConnectionPage.BreakboxillaConnection(firedrive);		
				ConnectionPage.DeleteSharedConnection(firedrive, connectionName);
				UserPage.DeleteUser(firedrive, RAusername);
				cleanUpLogout();
				}
				

			}		
		


			@Test(priority=6) //Launch a Connection in private Mode  change it to share and record and compare the performance
			public void Test06_SR0015() throws Exception {
//				Launch a connection in private mode
				printTestDetails("STARTING ", "Test06_SR0015", "");
				SoftAssert softAssert = new SoftAssert();
				ArrayList<Float> sharedfps = new ArrayList<Float>();
				
				Onedevices = devicePool.getAllDevices("Onedevice.properties");
				cleanUpLogin();
				ArrayList connName = ConnectionPage.CreateConnection(firedrive, Onedevices, 1, "Private");
				userpage.createUser(firedrive,null,RAusername,RApassword,"General");
				userpage.Sharedconnectionassign(firedrive, RAusername, connName);
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
				System.out.println(connectionList);
//				
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
			      Thread.sleep(90000);
//					Record its performance stats from boxilla  	
			   	RestAssured.useRelaxedHTTPSValidation();
				String gerdetails = RestAssured.given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers("Content-Type", "application/json", "Accept","application/json")
									.when().get("https://"+boxillaManager+"/bxa-api/connections/kvm/active")
									.then().assertThat().statusCode(200)
									.extract().response().asString();
				 System.out.println("Active connection status"+gerdetails);
			     
					System.out.println("********************checking the connection status  ******************");
					JsonPath js = new JsonPath(gerdetails);
					String ActiveconnectionName = js.get("message.active_connections[0].connection_name");
			    	float fpsprivate =js.get("message.active_connections[0].fps");
			    	System.out.println("For the connection named "+ActiveconnectionName+" the FPS value is "+fpsprivate);
			    	
//					Drop Connection
			    	Thread.sleep(2000);
			    	Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			    	closeApp();


//				Change the connection settings to shared
			    	cleanUpLogin();
			    	Thread.sleep(2000);
			    	System.out.println(connectionList.get(0));
			    	ConnectionPage.connections(firedrive).click();
			    	firedrive.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
					ConnectionPage.manage(firedrive).click();
					firedrive.manage().timeouts().implicitlyWait(3,TimeUnit.SECONDS);
					ConnectionPage.searchOption(firedrive).sendKeys(connectionList.get(0));
					ConnectionPage.Optionicon(firedrive).click();
					ConnectionPage.Editconnection(firedrive).click();
					ConnectionPage.nextoption(firedrive).click();
					ConnectionPage.sharedconnectionType(firedrive).click();
					ConnectionPage.nextoption(firedrive).click();
					firedrive.manage().timeouts().implicitlyWait(4,TimeUnit.SECONDS);
					ConnectionPage.Saveoption(firedrive).click();
					firedrive.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
					Thread.sleep(10000);
					cleanUpLogout();
					
					RAlogin(RAusername,RApassword);
					WebElement availablesharedConnectionsList = getElement("availableConnectionsWinListBox");
					List<WebElement> availablesharedConnections = availablesharedConnectionsList.findElements(By.xpath("//ListItem"));
					connectionList.clear();
					connectionNumber=1;
					for (WebElement connection : availablesharedConnections) {
					  System.out.println("connections number  "+connectionNumber+" is "+connection.getText());
					  connectionList.add(connection.getText());
					  connectionNumber++;
					}
					System.out.println(connectionList);
//					
					for (String connectionsharedName : connectionList) {
					Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
					WebElement targettoConnect = availablesharedConnectionsList.findElement(By.name(connectionsharedName));
					  Actions a = new Actions(Windriver);
					  count++;
				      a.moveToElement(targettoConnect).
				      doubleClick().
				      build().perform();
				      System.out.println(connectionsharedName+" has been launched");
				      }
				      Thread.sleep(90000);
						//	Record its performance stats from boxilla  	
						   	RestAssured.useRelaxedHTTPSValidation();
							String shareddetails = RestAssured.given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers("Content-Type", "application/json", "Accept","application/json")
												.when().get("https://"+boxillaManager+"/bxa-api/connections/kvm/active")
												.then().assertThat().statusCode(200)
												.extract().response().asString();
							 System.out.println("Active connection status"+shareddetails);
						     
								System.out.println("********************checking the connection status  ******************");
								JsonPath shared1 = new JsonPath(shareddetails);
								String Activeshared1connectionName = shared1.get("message.active_connections[0].connection_name");
								sharedfps.add((Float) shared1.get("message.active_connections[0].fps"));
						    	System.out.println("For the connection named "+Activeshared1connectionName+" the FPS value is "+sharedfps);
						    	
//								Drop Connection
						    	Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
						    	closeApp();
							
//							Record its performance tests from boxilla 
							System.out.println("Private FPS is "+fpsprivate);
							System.out.println("shared first FPS is "+sharedfps.get(0));
							
//							Compare results
							if (fpsprivate==sharedfps.get(0)) {
																							
							softAssert.assertTrue(true,"Difference in FPS values for shared and Private Connections");
			}
							cleanUpLogin();
							ConnectionPage.DeleteSharedConnection(firedrive, connName);
							UserPage.DeleteUser(firedrive, RAusername);
							cleanUpLogout();
							softAssert.assertAll();
			}

			@Test (priority=7)//Launch private in two separate user and check the error message
			public void Test_07_SR0004_PrivateViolation() throws Exception {
				printTestDetails("STARTING ", "Test_07_SR0004_PrivateViolation", "");
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
						      
						      
			
			
			
			//Launch PE dual head Tx
			@Test(priority=8)
			public void Test_08_DC0003_PETX() throws Exception {
				printTestDetails("STARTING ", "Test_08_DC0003_PETX", "");
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
					cleanUpLogout();
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
					      Thread.sleep(30000);
					      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
					}
					      
					    //  closeApp();
					      Thread.sleep(30000);
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
			
			@Test(priority=9)// To minimize the launched connection
			public void Test09_AI0074() throws Exception {
				printTestDetails("STARTING ", "Test09_AI0074", "");
				Onedevices = devicePool.getAllDevices("devicePE.properties");
				
				cleanUpLogin();
				ConnectionPage.createprivateconnections(firedrive, Onedevices);//Sharedconnection(firedrive, Onedevices, 1, "shared");
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
					
					for (String RAconnectionName : connectionList) {
					
					WebElement targetConnection = availableConnectionsList.findElement(By.name(RAconnectionName));
					  Actions a = new Actions(Windriver);
					  count++;
				      a.moveToElement(targetConnection).
				      doubleClick().
				      build().perform();
				      System.out.println(RAconnectionName+" has been launched through RemoteApp");
				      Thread.sleep(90000);
				      
				      }
				    
				    	//Getting window handle of launched connection
				    	 WebElement connectionWindow = (WebElement) Windriver2.findElementByClassName("wCloudBB");
				    	 String connectionWindowHandle = connectionWindow.getAttribute("NativeWindowHandle");
				    	 String connectionTopLevelWidnowHandle = Integer.toHexString(Integer.parseInt((connectionWindowHandle))); // Convert to Hex
				    	 
				    	 //Setting capabilities for connection window session
				    	 DesiredCapabilities connectionAppCapabilities = new DesiredCapabilities();
				    	 connectionAppCapabilities.setCapability("appTopLevelWindow", connectionTopLevelWidnowHandle);
				    	
				    	 WindowsDriver RASession;
				    	 
				    	 //attaching to connection session and doing stuff..
				    	try {
				    		RASession = new WindowsDriver(new URL("http://127.0.0.1:4723"), connectionAppCapabilities);
				    		//RASession.switchTo().window((String)RASession.getWindowHandles().toArray()[0]);
				    		
				    		WebElement minimise = RASession.findElementByName("Minimise");
				    		WebElement maximise = RASession.findElementByName("Maximise");
				    		WebElement close = RASession.findElementByName("Close");
				    		
				    		minimise.click();
				    		Thread.sleep(3000);
				    		System.out.println("Minimise  icon clicked on connection window");
				    		RASession.manage().window().maximize();
				    		Thread.sleep(2000);
				    		
				    		
				    		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
					    	Thread.sleep(2000);
					    	closeApp();
							cleanUpLogin();
							ConnectionPage.DeleteConnection(firedrive, Onedevices);
							UserPage.DeleteUser(firedrive, RAusername);
							cleanUpLogout();
				    		
				    		
				    	} catch (MalformedURLException e) {
				    		// TODO Auto-generated catch block
				    		e.printStackTrace();
				    		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
				    		closeApp();
				    		cleanUpLogin();
				    		ConnectionPage.DeleteConnection(firedrive, Onedevices);
				    		UserPage.DeleteUser(firedrive, RAusername);
				    		cleanUpLogout();
				    	}
				    	
				
				
				     }
			
			@Test(priority=10)//to maximize the launched connection
			public void Test10_AI0075() throws Exception {
				printTestDetails("STARTING ", "Test10_AI0075", "");
				float ActiveconnectionFPS = 0;
				
				Onedevices = devicePool.getAllDevices("devicePE.properties");
				
				cleanUpLogin();
				ConnectionPage.createprivateconnections(firedrive, Onedevices);//Sharedconnection(firedrive, Onedevices, 1, "shared");
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
					
					for (String RAconnectionName : connectionList) {
					
					WebElement targetConnection = availableConnectionsList.findElement(By.name(RAconnectionName));
					  Actions a = new Actions(Windriver);
					  count++;
				      a.moveToElement(targetConnection).
				      doubleClick().
				      build().perform();
				      System.out.println(RAconnectionName+" has been launched through RemoteApp");
				      Thread.sleep(90000);
				      
				      }
				    
				    	//Getting window handle of launched connection
				    	 WebElement connectionWindow = (WebElement) Windriver2.findElementByClassName("wCloudBB");
				    	 String connectionWindowHandle = connectionWindow.getAttribute("NativeWindowHandle");
				    	 String connectionTopLevelWidnowHandle = Integer.toHexString(Integer.parseInt((connectionWindowHandle))); // Convert to Hex
				    	 
				    	 //Setting capabilities for connection window session
				    	 DesiredCapabilities connectionAppCapabilities = new DesiredCapabilities();
				    	 connectionAppCapabilities.setCapability("appTopLevelWindow", connectionTopLevelWidnowHandle);
				    	
				    	 WindowsDriver RASession;
				    	 
				    	 //attaching to connection session and doing stuff..
				    	try {
				    		RASession = new WindowsDriver(new URL("http://127.0.0.1:4723"), connectionAppCapabilities);
				    		//RASession.switchTo().window((String)RASession.getWindowHandles().toArray()[0]);
				    		
				    		WebElement minimise = RASession.findElementByName("Minimise");
				    		WebElement maximise = RASession.findElementByName("Maximise");
				    		WebElement close = RASession.findElementByName("Close");
				    		
				    		maximise.click();
				    		Thread.sleep(3000);
				    		System.out.println("Maximise icon clicked on the connection Window");
				    		WebElement restore = RASession.findElementByName("Restore");
				    		restore.click();
				    		Thread.sleep(2000);
				    		
				    		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
					    	Thread.sleep(2000);
					    	closeApp();
							cleanUpLogin();
							ConnectionPage.DeleteConnection(firedrive, Onedevices);
							UserPage.DeleteUser(firedrive, RAusername);
							cleanUpLogout();
				    		
				    	} catch (MalformedURLException e) {
				    		// TODO Auto-generated catch block
				    		e.printStackTrace();
				    		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
					    	Thread.sleep(2000);
					    	closeApp();
							cleanUpLogin();
							ConnectionPage.DeleteConnection(firedrive, Onedevices);
							UserPage.DeleteUser(firedrive, RAusername);
							cleanUpLogout();
				    	}
				    
				
				     }
			
			@Test(priority=11) //-  create a VM connection and launch
			public void Test_11_VI0005_CreateVMlaunch() throws Exception {
				printTestDetails("STARTING ", "Test_11_VI0005_CreateVMlaunch", "");
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
				comboBoxElement.findElement(By.name("Auto")).click(); 

				getElement("applyButton").click();
				Thread.sleep(3000);
				
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
			
			
			@Test(priority=12) //Launch multiple connection and ensure mouse movement
			public void Test_12_SR0010_MouseMovement() throws Exception {
				printTestDetails("STARTING ", "Test_12_SR0010_MouseMovement", "");
				float ActiveconnectionFPS = 0;
				
				Onedevices = devicePool.getAllDevices("devicePE.properties");
				
				cleanUpLogin();
				ConnectionPage.createprivateconnections(firedrive, Onedevices);//Sharedconnection(firedrive, Onedevices, 1, "shared");
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
					
					for (String RAconnectionName : connectionList) {
					
					WebElement targetConnection = availableConnectionsList.findElement(By.name(RAconnectionName));
					  Actions a = new Actions(Windriver);
					  count++;
				      a.moveToElement(targetConnection).
				      doubleClick().
				      build().perform();
				      System.out.println(RAconnectionName+" has been launched through RemoteApp");
				      Thread.sleep(90000);
				      
				      }
				    
				    	//Getting window handle of launched connection
				    	 WebElement connectionWindow = (WebElement) Windriver2.findElementByClassName("wCloudBB");
				    	 String connectionWindowHandle = connectionWindow.getAttribute("NativeWindowHandle");
				    	 String connectionTopLevelWidnowHandle = Integer.toHexString(Integer.parseInt((connectionWindowHandle))); // Convert to Hex
				    	 
				    	 //Setting capabilities for connection window session
				    	 DesiredCapabilities connectionAppCapabilities = new DesiredCapabilities();
				    	 connectionAppCapabilities.setCapability("appTopLevelWindow", connectionTopLevelWidnowHandle);
				    	
				    	 WindowsDriver RASession;
				    	 
				    	 //attaching to connection session and doing stuff..
				    	try {
				    		RASession = new WindowsDriver(new URL("http://127.0.0.1:4723"), connectionAppCapabilities);
				    		//RASession.switchTo().window((String)RASession.getWindowHandles().toArray()[0]);
				    		
				    		WebElement minimise = RASession.findElementByName("Minimise");
				    		WebElement maximise = RASession.findElementByName("Maximise");
				    		WebElement close = RASession.findElementByName("Close");
				    		
				    		maximise.click();
				    		Thread.sleep(3000);
				    		WebElement restore = RASession.findElementByName("Restore");
				    		restore.click();
				    		System.out.println("Mouse could move over connection window");
				    		Thread.sleep(2000);
				    						    		
				    		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
					    	Thread.sleep(2000);
					    	closeApp();
							cleanUpLogin();
							ConnectionPage.DeleteConnection(firedrive, Onedevices);
							UserPage.DeleteUser(firedrive, RAusername);
							cleanUpLogout();
				    		
				    	} catch (MalformedURLException e) {
				    		// TODO Auto-generated catch block
				    		e.printStackTrace();
				    		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
					    	Thread.sleep(2000);
					    	closeApp();
							cleanUpLogin();
							ConnectionPage.DeleteConnection(firedrive, Onedevices);
							UserPage.DeleteUser(firedrive, RAusername);
							cleanUpLogout();
				    	}
				    
				
				     }
			
			@Test(priority=13)//launch 4K connection and should pop up error message
			public void Test13_CL0005() throws Exception {
				printTestDetails("STARTING ", "Test13_CL0005", "");
				Onedevices = devicePool.getAllDevices("device4K.properties");
				cleanUpLogin();
				ConnectionPage.createprivateconnections(firedrive, Onedevices);
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
			      }
				Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);  
				closeApp();
				cleanUpLogin();
				UserPage.DeleteUser(firedrive, RAusername);
				cleanUpLogout();
}
			
			
			
			
			@Test(priority=14)// Launch a TX connection with its compression mode set to optimized and VM connection with RDP enabled
			public void Test14_SR0001() throws Exception {
				printTestDetails("STARTING ", "Test14_SR0001", "");
				SoftAssert softAssert = new SoftAssert();
				Onedevices = devicePool.getAllDevices("Onedevice.properties");
				cleanUpLogin();
				ArrayList<String> VMname= new ArrayList<String>();
				VMname.add(VMIp);
				VMname.add("10.211.130.215");
				ConnectionPage.createprivateconnections(firedrive, Onedevices);
				ConnectionPage.CreateRDPconnection(firedrive, VMIp, VMUsername, VMPassword, VMDomainName);
				userpage.createUser(firedrive,null,RAusername,RApassword,"General");
				userpage.Sharedconnectionassign(firedrive, RAusername,VMname);
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
			      Thread.sleep(20000);
			      }
			      Thread.sleep(90000);
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
			      closeApp();
					cleanUpLogin();
					ConnectionPage.DeleteSharedConnection(firedrive, VMname);
					//ConnectionPage.DeleteConnection(firedrive, Onedevices);
					UserPage.DeleteUser(firedrive, RAusername);
					cleanUpLogout();
}
						
			
			//@Test(priority=15) //Log onto TX connection managed by a different boxilla
			public void Test15_SR0003() throws Exception {
				//login to another boxilla
				printTestDetails("STARTING ", "Test15_SR0003", "");
				SoftAssert softAssert = new SoftAssert();
				Onedevices = devicePool.getAllDevices("Onedevice.properties");
				int connectionNumber=1;
				cleanUpLogin();
				unManageDevice(firedrive,Onedevices);
				
				cleanUpLogout();
				DoubleLogin();
				System.out.println("Attempting to manage devices for RemoteApp");	
				DiscoveryMethods discoveryMethods = new DiscoveryMethods();		
			
				for(Device deviceList : Onedevices) {
				
				System.out.println("Adding the device "+deviceList);
				
				Thread.sleep(2000);
				discoveryMethods.addDeviceToBoxilla(firedrive, deviceList.getMac(), deviceList.getIpAddress(),
						deviceList.getGateway(),deviceList.getNetmask(), deviceList.getDeviceName(), 10);
				}
				System.out.println("*************All Devices are Managed***************");
				cleanUpLogout();
				cleanUpLogin();
				ConnectionPage.createprivateconnections(firedrive, Onedevices);
				userpage.createUser(firedrive,Onedevices,RAusername,RApassword,"General");
				cleanUpLogout();
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
				  a.moveToElement(targetConnection).
			      doubleClick().
			      build().perform();
				
				}
				try {
				  new WebDriverWait(Windriver, 60).until(ExpectedConditions.visibilityOf(Windriver.findElementByAccessibilityId("TitleBar")));
			      Windriver.findElementByName("OK").click();
//			      new WebDriverWait(Windriver, 60).until(ExpectedConditions.visibilityOf(Windriver.findElementByName("Unable to connect to 10.211.130.214. Please confirm 10.211.130.214 is powered up and connected to the network. (0x7)")));
			      Thread.sleep(1500);
			      System.out.println(Windriver.getWindowHandles().toArray().length);	
			  //    Windriver.switchTo().activeElement();
			   Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[1]);
			      
			     // WebElement windowsPopupOpenButton = Windriver.context("Unable to connect");//findElementByClassName("Static");//findElementByName("Unable to connect");
			//	WebElement windowsPopupOpenButton = Windriver.findElementById("TitleBar");
			    WebElement windowsPopupOpenButton = Windriver.findElementByName("Unable to connect");
				String text=windowsPopupOpenButton.getText();
			 //   Windriver.findElementByName("OK").click();
		  		//Windriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); 
		  		System.out.println("Alert Message is  "+text);
		  		softAssert.assertEquals(text, "Unable to connect");
		  		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		  		closeApp();
		  		DoubleLogin();
		  		unManageDevice(firedrive,Onedevices);
		  		cleanUpLogout();
				cleanUpLogin();
				Thread.sleep(2000);
				ConnectionPage.DeleteConnection(firedrive, Onedevices);
				UserPage.DeleteUser(firedrive, RAusername);
				for(Device deviceList : Onedevices) {
					System.out.println("Adding the device "+deviceList);
					discoveryMethods.addDeviceToBoxilla(firedrive, deviceList.getMac(), deviceList.getIpAddress(),
							deviceList.getGateway(),deviceList.getNetmask(), deviceList.getDeviceName(), 10);
					}
					System.out.println("*************All Devices are Managed***************");
				cleanUpLogout();
			    softAssert.assertAll();
				}
				catch(Exception e) {
					e.printStackTrace();
					Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			  		closeApp();
			  		DoubleLogin();
			  		unManageDevice(firedrive,Onedevices);
			  		cleanUpLogout();
					cleanUpLogin();
					Thread.sleep(2000);
					ConnectionPage.DeleteConnection(firedrive, Onedevices);
					UserPage.DeleteUser(firedrive, RAusername);
					for(Device deviceList : Onedevices) {
						System.out.println("Adding the device "+deviceList);
						discoveryMethods.addDeviceToBoxilla(firedrive, deviceList.getMac(), deviceList.getIpAddress(),
								deviceList.getGateway(),deviceList.getNetmask(), deviceList.getDeviceName(), 10);
						}
						System.out.println("*************All Devices are Managed***************");
					cleanUpLogout();
				}
		  		
				
				
			}
			
			
			@Test(priority=16) //Connect to a disconnected TX 
			public void Test16_AI0052() throws Exception {
				printTestDetails("STARTING ", "Test16_AI0052", "");
				SoftAssert softAssert = new SoftAssert();
				Onedevices = devicePool.getAllDevices("Onedevice.properties");
				cleanUpLogin();
				DiscoveryMethods discoveryMethods = new DiscoveryMethods();		
				
					for(Device deviceList : Onedevices) {
					System.out.println("Adding the device "+deviceList);
					discoveryMethods.addDeviceToBoxilla(firedrive, deviceList.getMac(), deviceList.getIpAddress(),
							deviceList.getGateway(),deviceList.getNetmask(), deviceList.getDeviceName(), 10);
					}
					System.out.println("*************All Devices are Managed***************");
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
			   	
			      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
					
					for (String connectionName : connectionList) {
				    	Windriver.findElement(By.name(connectionName)).click();
				    	Windriver.findElement(By.name("Disconnect")).click();
					    Thread.sleep(3000);
					    System.out.println("connection "+connectionName+" is disconnected");
					    Windriver.switchTo().window(Windriver.getWindowHandle());
					    WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionName));
						  Actions a = new Actions(Windriver);
						  count++;
					      a.moveToElement(targetConnection).
					      doubleClick().
					      build().perform();
					
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
					
				closeApp();
				cleanUpLogin();
				Thread.sleep(2000);
				ConnectionPage.DeleteConnection(firedrive, Onedevices);
				UserPage.DeleteUser(firedrive, RAusername);
				cleanUpLogout();
				}
			
			@Test(priority=17) //PE Optimised connection to launch
			public void Test17_DC0005() throws Exception {
				printTestDetails("STARTING ", "Test17_DC0005", "");
				SoftAssert softAssert = new SoftAssert();
				Onedevices = devicePool.getAllDevices("devicePE.properties");
				cleanUpLogin();
				
				DiscoveryMethods discoveryMethods = new DiscoveryMethods();		
				
				for(Device deviceList : Onedevices) {
				System.out.println("Adding the device "+deviceList);
				discoveryMethods.addDeviceToBoxilla(firedrive, deviceList.getMac(), deviceList.getIpAddress(),
						deviceList.getGateway(),deviceList.getNetmask(), deviceList.getDeviceName(), 10);
				}
				System.out.println("*************All Devices are Managed***************");
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
			      Thread.sleep(5000);
			      }
			      Thread.sleep(90000);
			   	
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
				Thread.sleep(3000);
				ConnectionPage.DeleteConnection(firedrive, Onedevices);
				Thread.sleep(3000);
				UserPage.DeleteUser(firedrive, RAusername);
				cleanUpLogout();
				}
			}
			
}		

