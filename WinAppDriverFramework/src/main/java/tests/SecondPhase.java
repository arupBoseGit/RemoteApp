package tests;

import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import org.testng.log4testng.Logger;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
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
	final static Logger log = Logger.getLogger(SecondPhase.class);
	//@Test(priority=1)//Launch dualHead and Extended Desktop
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
				ConnectionPage.DeleteConnection(firedrive, DualHeadDevices);
				UserPage.DeleteUser(firedrive, RAusername);
				cleanUpLogout();
			
			
		}
	
		//@Test (priority=2)//create a connection with wrong IP
		public void Test02_AI0052() throws Exception {
	//		Launch a connection in private mode
			printTestDetails("STARTING ", "Test02_AI0052", "");
			//ArrayList<Float> sharedfps = new ArrayList<Float>();
		//	Onedevices.clear();
			Onedevices = devicePool.getAllDevices("WrongIP.properties");
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
			System.out.println(connectionList);
	//		
			for (String connectionName : connectionList) {
			
			WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionName));
			  Actions a = new Actions(Windriver);
			  count++;
		      a.moveToElement(targetConnection).
		      doubleClick().
		      build().perform();
		      Thread.sleep(15000);
		      
		    //  Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);	
		      try {
//		      new WebDriverWait(Windriver, 60).until(ExpectedConditions.visibilityOf(Windriver.findElementByAccessibilityId("TitleBar")));
//		      
//		      Thread.sleep(1000);
		    	  WebElement windowsPopupOpenButton = Windriver.findElementByName("Unable to connect");
			        String text= windowsPopupOpenButton.getText();
					Windriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); 
					System.out.println("Alert Message is  "+text);
//		      System.out.println(Windriver.getWindowHandles().toArray().length);
//		      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[1]);	
//		      WebElement windowsPopupOpenButton = Windriver.findElementByAccessibilityId("TitleBar");
//	        String text= windowsPopupOpenButton.getText();
	        Windriver.findElementByName("OK").click();
	  		System.out.println("Alert Message is  "+text);
	  		softAssert.assertEquals(text, "Unable to connect");
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
	
	
			
			//@Test(priority=3) //shared connection terminate and ensure the other remain active
			public void Test3_SR0013() throws Exception {
				printTestDetails("STARTING ", "Test3_SR0013", "");
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
					

			
			//@Test(priority=4) //double click connection should Launch
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
				Thread.sleep(5000);
				ConnectionPage.DeleteConnection(firedrive, Onedevices);
				UserPage.DeleteUser(firedrive, RAusername);
				cleanUpLogout();
				}
			}
			
			
			
			
			
			// manage TX and Rx, launch a connection through boxilla and note down the FPS then launch connection through RA and measure the FPS
			//@Test(priority=5)
			public void Test5_RA_boxilla_Performance() throws Exception {
				
				printTestDetails("STARTING ", "Test5_RA_boxilla_Performance", "");
				float ActiveconnectionFPS = 0;
				
				Onedevices = devicePool.getAllDevices("Onedevice.properties");
				try {
				cleanUpLogin();
				ArrayList<String> connectionName= ConnectionPage.CreateConnection(firedrive, Onedevices, 1, "Private");//createprivateconnections(firedrive,Onedevices);
				ConnectionPage.launchConnection(firedrive,connectionName,"Test_RX_Dual_Pe");
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
					ConnectionPage.DeleteConnection(firedrive, Onedevices);
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
				ConnectionPage.DeleteConnection(firedrive, Onedevices);
				UserPage.DeleteUser(firedrive, RAusername);
				cleanUpLogout();
				}
				

			}		
		


			//@Test(priority=6) //Launch a Connection in private Mode  change it to share and record and compare the performance
			public void Test06_SR0015() throws Exception {
//				Launch a connection in private mode
				printTestDetails("STARTING ", "Test06_SR0015", "");
				ArrayList<Float> sharedfps = new ArrayList<Float>();
				
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
					
									
					for(int j=1;j<3;j++) {
						userpage.createUser(firedrive,Onedevices,"TestUser"+j,"TestUser"+j,"General");
						Thread.sleep(5000);
					}
						
						cleanUpLogout();
							Thread.sleep(10000);
							for(int k=1;k<3;k++) {
							RAlogin("TestUser"+k,"TestUser"+k);
							Thread.sleep(1500);
							WebElement availablesharedConnectionsList = getElement("availableConnectionsWinListBox");
							List<WebElement> availablesharedConnections = availablesharedConnectionsList.findElements(By.xpath("//ListItem"));
							connectionList.clear();
							for (WebElement connection : availablesharedConnections) {
								connectionList.add(connection.getText());
							}
							System.out.println("List is "+connectionList);
							for (String connectionName : connectionList) {
								  Actions a = new Actions(Windriver);
								  WebElement targetConnection = availablesharedConnectionsList.findElement(By.name(connectionName));
							      a.moveToElement(targetConnection).
							      doubleClick().
							      build().perform();
							      System.out.println("connection named "+connectionName+" has been launched");
							      Thread.sleep(60000);
							     
							}
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
							}
//							Record its performance tests from boxilla 
							System.out.println("Private FPS is "+fpsprivate);
							System.out.println("shared first FPS is "+sharedfps.get(0));
							System.out.println("Shared second FPS is "+sharedfps.get(1));
//							Compare results
							if (fpsprivate==sharedfps.get(0)) {
								if(sharedfps.get(0)==sharedfps.get(1));
								softAssert.assertTrue(true);
							}
							softAssert.assertTrue(true,"Difference in FPS values for shared and Private Connections");
							
							cleanUpLogin();
							ConnectionPage.DeleteConnection(firedrive, Onedevices);
							UserPage.DeleteUser(firedrive, RAusername);
							cleanUpLogout();
							softAssert.assertAll();
			}
			//@Test (priority=7)//Launch private in two separate user and check the error message
			public void Test7_SR0004() throws Exception {
				printTestDetails("STARTING ", "Test7_SR0004", "");
				WebDriverWait wait=new WebDriverWait(firedrive, 20);
				ArrayList<Device> ZeroUDevices = new ArrayList<Device>();
				ArrayList<String> DualHeadList = new ArrayList<String>();
				DiscoveryMethods discoveryMethods = new DiscoveryMethods();	
				AppliancePool applian = new AppliancePool();
				ArrayList<String> connectionZeroUList = new ArrayList<String>();
				ArrayList<Device> remotedevice=applian.getAllDevices("Onedevice.properties");
				System.out.println(remotedevice);
				
				cleanUpLogin();
				ZeroUDevices=ConnectionPage.Sharedconnection(firedrive, remotedevice, 2, "Private");
				userpage.createUser(firedrive,remotedevice,"TestUser","TestUser","General");
				cleanUpLogout();
						Thread.sleep(10000);
						RAlogin("TestUser","TestUser");
						Thread.sleep(1500);
						WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
						List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
						DualHeadList.clear();
						for (WebElement connection : availableConnections) {
							DualHeadList.add(connection.getText());
						}
						int countcon=0;
						System.out.println("List is "+DualHeadList);
						String firstconnectionName=null, secondConnectionName=null;
						for(int i=0;i<DualHeadList.size();i++) {
							if (i==0) {
								firstconnectionName=DualHeadList.get(i);
								System.out.println("Connection number 1 is "+firstconnectionName);
							} else {secondConnectionName=DualHeadList.get(i);
							System.out.println("Connection number 2 is "+secondConnectionName);}
						}
							
																			
							  Actions a = new Actions(Windriver);
							  WebElement firsttargetConnection = availableConnectionsList.findElement(By.name(firstconnectionName));
						      a.moveToElement(firsttargetConnection);
						      Windriver.findElement(By.name("Connect")).click();
						      log.info("Connection "+firstconnectionName+ " is launched");
						      Thread.sleep(5000);
						      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
						      WebElement secondtargetConnection = availableConnectionsList.findElement(By.name(secondConnectionName));
						      a.moveToElement(secondtargetConnection);
						      Windriver.findElement(By.name("Connect")).click();
						      log.info(" Trying to launch Connection "+secondConnectionName);
						      try {
							      new WebDriverWait(Windriver, 60).until(ExpectedConditions.visibilityOf(Windriver.findElementByAccessibilityId("TitleBar")));
							      Windriver.findElementByName("OK").click();
							      Thread.sleep(5000);
							      new WebDriverWait(Windriver, 60).until(ExpectedConditions.visibilityOf(Windriver.findElementByAccessibilityId("TitleBar")));
							     // System.out.println(Windriver.getWindowHandles().toArray().length);
							     // Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[1]);	
							      WebElement windowsPopupOpenButton = Windriver.findElementByAccessibilityId("TitleBar");
						        String text= windowsPopupOpenButton.getText();
						  		System.out.println("Alert Message is  "+text);
						  		softAssert.assertEquals(text, "ConnectionName is terminated.");
						  	//	Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
						  		closeApp();
								cleanUpLogin();
								Thread.sleep(2000);
								ConnectionPage.DeleteConnection(firedrive, remotedevice);
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
										ConnectionPage.DeleteConnection(firedrive, remotedevice);
										Thread.sleep(2000);
										UserPage.DeleteUser(firedrive, RAusername);
										cleanUpLogout();
							      }
			}
						      
						      
						      
						      
						      
						      
						      
						      
						      
						      
						      
		
				   
					
			
			
			
			
			
			
			
			
			//Launch PE dual head Tx(Apparently only SE is having DH)
			//@Test(priority=8)
			public void Test08_DC0003() throws Exception {
				printTestDetails("STARTING ", "Test08_DC0003", "");
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
			
			//@Test(priority=9)// To minimize the launched connection
			public void Test09_AI0074() throws Exception {
					printTestDetails("STARTING ", "Test09_AI0074", "");

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
				      Thread.sleep(20000);
				      String WindowName = Windriver.getWindowHandle().toString();
				      System.out.println("Page source is "+Windriver.getPageSource());
				      System.out.println(WindowName);
				      Thread.sleep(10000);
				      System.out.println(Windriver.getWindowHandles().toArray().length);
//				      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[1]);	
//				      DesiredCapabilities capabilities = new DesiredCapabilities();
//			    	  capabilities.setCapability("app", "Root");
//			    	  Windriver = new WindowsDriver<RemoteWebElement>(new URL("http://127.0.0.1:4723"),capabilities);
//			    	  String topWindow = Windriver.findElementByClassName("Static").getAttribute("10.211.130.214");
//			    	  int MAWinHandleInt = Integer.parseInt(topWindow);
//			    	  String MAWinHandleHex = Integer.toHexString(MAWinHandleInt);
//
//			    	  DesiredCapabilities caps = new DesiredCapabilities();
//			    	  caps.setCapability("appTopLevelWindow", MAWinHandleHex);
//			    	  Windriver = new WindowsDriver<RemoteWebElement>(new URL("http://127.0.0.1:4723"), caps);
				      
				      DesiredCapabilities desktopCapabilities = new DesiredCapabilities();
				  	desktopCapabilities.setCapability("platformName", "Windows");
				  	desktopCapabilities.setCapability("app", "Root");
				  	desktopCapabilities.setCapability("deviceName", "WindowsPC");
				  Windriver2 = new WindowsDriver<WindowsElement>(new URL("http://127.0.0.1:4723"), desktopCapabilities);

				  WindowsElement applicationWindow = null;
				   List<WindowsElement> openWindows = Windriver2.findElementsByClassName("Window");
				 
				  for (WindowsElement window : openWindows) {
				  					
				  	if (window.getAttribute("Name").startsWith(connectionName))
				  	{
				  		applicationWindow = window;
				  		break;
				  	}
				  }

				  // Attaching to existing Application Window
				   Object topLevelWindowHandle = applicationWindow.getAttribute("NativeWindowHandle");
				//  topLevelWindowHandle = int.parse((String) topLevelWindowHandle).ToString("X");

				  DesiredCapabilities capabilities = new DesiredCapabilities();
				  capabilities.setCapability("deviceName", "WindowsPC");
				  capabilities.setCapability("appTopLevelWindow", topLevelWindowHandle);
				  Windriver2 = new WindowsDriver<WindowsElement>(new URL("http://127.0.0.1:4723"), capabilities);
				     // Windriver.switchTo().window("10.211.130.214");
				    //  System.out.println("CurrentWindow Handle is "+Windriver.getWindowHandles().toArray());
				      a.moveToElement(Windriver2.findElementById("Minimize-Restore")).click();
					}
				   //   Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
				      try{
				    	 	//Windriver.findElementByAccessibilityId("Minimize-Restore").click();
						   	System.out.println("Minimize Option is clicked");
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
								
								}}		
				      
				      catch(Exception e) {
				    	 e.printStackTrace();
				      }
				      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
				      closeApp();
						cleanUpLogin();
						ConnectionPage.DeleteConnection(firedrive, Onedevices);
						UserPage.DeleteUser(firedrive, RAusername);
						cleanUpLogout();
					
					
				}
			
			//@Test(priority=10)//to maximize the launched connection
			public void Test10_AI0075() throws Exception {
					printTestDetails("STARTING ", "Test10_AI0075", "");

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
				      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
				   	Windriver.findElementById("Maximize-Restore").click();
				   	System.out.println("Minimize Option is clicked");
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
			
			//@Test(priority=11) //-  create a VM connection and launch
			public void Test11_VI0005() throws Exception {
				printTestDetails("STARTING ", "Test11_VI0005", "");
				ArrayList<String> RAVM = new ArrayList<String>();
				RAVM.add(VMIp);	
				cleanUpLogin();
				ConnectionPage.CreateRDPconnection(firedrive, VMIp, VMUsername, VMPassword, VMDomainName);
				userpage.createUser(firedrive,null,RAusername,RApassword,"General");
				userpage.Sharedconnectionassign(firedrive, RAusername, RAVM);
				cleanUpLogout();
				RAlogin(RAusername, RApassword);
//				getElement("menuLabel").click();
//			    System.out.println("Menu Label clicked");
//			    Windriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//			    Windriver.findElementByName("Settings").click();
//			    System.out.println("Settings clicked");
//			    List<WebElement> navigate=Windriver.findElementsByAccessibilityId("settingsNavigation");
//			    System.out.println(navigate);
//				getElement("settingsNavigation").click();
//				getElement("Auto").click();
//				getElement("applyButton").click();
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
			
			
			//@Test(priority=12) //Launch multiple connection and ensure mouse movement
			public void Test12_SR0010() throws Exception {
				printTestDetails("STARTING ", "Test12_SR0010", "");
				Onedevices = devicePool.getAllDevices("Onedevice.properties");
				cleanUpLogin();
				ArrayList sharedconnect = ConnectionPage.Sharedconnection(firedrive, Onedevices, 2,"Shared");
				userpage.createUser(firedrive,null,RAusername,RApassword,"General");
				userpage.Sharedconnectionassign(firedrive, RAusername, sharedconnect);
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
			      Thread.sleep(10000);
			      System.out.println(connectionName+" has been launched");
			      System.out.println("Active window size  "+Windriver.getWindowHandles().size());
			     // a.moveToElement(Windriver.findElementByAccessibilityId("Maximize-Restore"));
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
			
			//@Test(priority=13)//launch 4K connection and should pop up error message
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
				ConnectionPage.DeleteConnection(firedrive, Onedevices);
				UserPage.DeleteUser(firedrive, RAusername);
				cleanUpLogout();
}
			
			
			
			
			//@Test(priority=14)// Launch a TX connection with its compression mode set to optimized and VM connection with RDP enabled
			public void Test14_SR0001() throws Exception {
				printTestDetails("STARTING ", "Test14_SR0001", "");
				Onedevices = devicePool.getAllDevices("Onedevice.properties");
				cleanUpLogin();
				ArrayList<String> VMname= new ArrayList<String>();
				VMname.add(VMIp);
				VMname.add("10.211.130.214");
				ConnectionPage.createprivateconnections(firedrive, Onedevices);
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
						
			
			@Test(priority=15) //Log onto TX connection managed by a different boxilla
			public void Test15_SR0003() throws Exception {
				//login to another boxilla
				printTestDetails("STARTING ", "Test15_SR0003", "");
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
				}
				catch(Exception e) {
					e.printStackTrace();
				}
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
			
			
			@Test(priority=16) //Connect to a disconnected TX 
			public void Test16_AI0052() throws Exception {
				printTestDetails("STARTING ", "Test16_AI0052", "");

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
				ConnectionPage.DeleteConnection(firedrive, Onedevices);
				UserPage.DeleteUser(firedrive, RAusername);
				cleanUpLogout();
				}
			
			@Test(priority=17) //PE Optimised connection to launch
			public void Test17_DC0005() throws Exception {
				printTestDetails("STARTING ", "Test17_DC0005", "");

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

