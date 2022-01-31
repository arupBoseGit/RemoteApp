//package pages;
//
//import java.util.ArrayList;
//import java.util.concurrent.TimeUnit;
//
//import org.openqa.selenium.WebDriver;
//import org.testng.Assert;
//
//import methods.Device;
//import methods.Devices;
//import methods.SeleniumActions;
//import tests.TestBase;
//import tests.TestBase.Callback;
//
//public class pointsnoted {

//	userpage.createUser(firedrive,devices,RAusername,RApassword,"General", new Callback() {
//		
//		@Override
//		public void cleanUp() {
//			cleanUpLogout();
//			
//		}
//		 public interface Callback {
//			 public void cleanUp();
//		 }
//		 
//		 public void createUser(WebDriver drive,ArrayList<Device> devicename, String user, String pass,String type, TestBase.Callback callback) throws Exception {
//				
//				drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//				System.out.println(drive.getCurrentUrl());
//				user(drive).click();
//				drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//				manage(drive).click();
//				NewUser(drive).click();
//				useTempNo(drive).click();
//				ActiveDNo(drive).click();
//				username(drive).sendKeys(user);
//				password(drive).sendKeys(pass);
//				confirmPassword(drive).sendKeys(pass);
//				NextButton(drive).click();
//				UserType(type, drive).click();
//				RemoteAccess(drive).click();
//				NextButton(drive).click();
//				savebutton(drive).click();
//				drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//				drive.navigate().refresh();
//				searchOption(drive).sendKeys(user);
//				System.out.println("Username entered in search box");
//				String deviceApplianceTable = SeleniumActions.seleniumGetText(drive, Devices.applianceTable);
//				Assert.assertTrue(deviceApplianceTable.contains(user),
//						"Device appliance table did not contain: " + user + ", actual text: " + deviceApplianceTable);
//				//testbase.cleanUpLogout();
//				callback.cleanUp();
//}
	
	
	// second winappdriver
    
    
		
    
	
	
//System.out.println("Launching Second RemoteApp Session");
//DesiredCapabilities capabilities1 = new DesiredCapabilities();
//capabilities1.setCapability("app", "C:\\Program Files\\Git\\EmeraldRA\\EmeraldRA.exe");
//capabilities1.setCapability("platformName", "Windows");
//capabilities1.setCapability("deviceName", "WindowsPC");
//
//
//
//try {
//Thread.sleep(1000);
//Windriver2 = new WindowsDriver(new URL("http://127.0.0.1:4723"), capabilities1);
//Assert.assertNotNull(Windriver2);
//System.out.println("Second Session Launched");
//
//}
//catch(Exception e){
//System.out.println("Exception has occured ");
//e.printStackTrace();
//} 
//
//Set<String> allWindowHandles = Windriver2.getWindowHandles();
//System.out.println("All Window handles are"+allWindowHandles);
//// System.out.println(Windriver2.context("Emerald Remote App Log-In");
//System.out.println("current window is "+Windriver2.getWindowHandles().size());
//Windriver2.switchTo().window((String)Windriver2.getWindowHandles().toArray()[0]);
////   System.out.println(Windriver2.getAutomationName().toString());
////     System.out.println(Windriver2.getWindowHandle().toString());
//Windriver2.switchTo().window("Emerald Remote App Log-In");
//WebElement loginButton = Windriver2.findElementByAccessibilityId("logInButton");
//Windriver2.findElementByAccessibilityId("userNameTextBox").click();
//Windriver2.findElementByAccessibilityId("userNameTextBox").sendKeys("TestUser2");
//System.out.println("Username Entered");
//Windriver2.findElementByAccessibilityId("passwordTextBox").click();
//Windriver2.findElementByAccessibilityId("passwordTextBox").sendKeys("TestUser2");
//System.out.println("Password Entered");
//loginButton.click();
//Thread.sleep(2000);
//Windriver2.switchTo().window((String)Windriver2.getWindowHandles().toArray()[0]);
//
//WebElement availableConnectionsList2 = Windriver2.findElementByAccessibilityId("availableConnectionsWinListBox");
//List<WebElement> availableConnections2 = availableConnectionsList2.findElements(By.xpath("//ListItem"));
//DualHeadList.clear();
//for (WebElement connection2 : availableConnections2) {
//	DualHeadList.add(connection2.getText());
//}
//System.out.println("List is "+DualHeadList);
//for (String connectionName : DualHeadList) {
//	  Actions a = new Actions(Windriver2);
//	  WebElement targetConnection = availableConnectionsList2.findElement(By.name(connectionName));
//    a.moveToElement(targetConnection).
//    doubleClick().
//    build().perform();
//    new WebDriverWait(Windriver2, 60).until(ExpectedConditions.visibilityOf(Windriver2.findElementByAccessibilityId("TitleBar")));
//    Windriver2.findElementByName("OK").click();
//    Thread.sleep(1000);
//    System.out.println(Windriver2.getWindowHandles().toArray().length);
//    Windriver2.switchTo().window((String)Windriver2.getWindowHandles().toArray()[1]);	
////    WebElement windowsPopupOpenButton =
////    new WebDriverWait(Windriver2, 60).until(ExpectedConditions.elementToBeClickable(Windriver2.findElementByName("Private/Shared Violation")));
//    WebElement windowsPopupOpenButton = Windriver2.findElementByName("Private/Shared Violation");
//    String text= windowsPopupOpenButton.getText();
//    Windriver2.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); 
//	System.out.println("Alert Message is  "+text);
// 
//}
//
//
//Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);	
//closeApp();
//Thread.sleep(60000);
//
//Windriver2.switchTo().window((String)Windriver2.getWindowHandles().toArray()[0]);
//Windriver2.findElementByAccessibilityId("menuLabel").click();
//Windriver2.findElementByName("Close").click();
//System.out.println("RemoteApp Closed");
//Windriver2.quit();

//			System.out.println("Launching Second RemoteApp Session");
//					DesiredCapabilities capabilities1 = new DesiredCapabilities();
//			        capabilities1.setCapability("app", "C:\\Program Files\\Git\\EmeraldRA\\EmeraldRA.exe");
//			        capabilities1.setCapability("platformName", "Windows");
//			        capabilities1.setCapability("deviceName", "WindowsPC");
//			      
//			       
//			        
//			        WindowsDriver<RemoteWebElement> Windriver3 = null;   
//			      try {
//			    	  Thread.sleep(1000);
////			    	  DesiredCapabilities capabilities = new DesiredCapabilities();
////			    	  capabilities.setCapability("app", "Root");
////			    	  Windriver3 = new WindowsDriver<WindowsElement>(new URL("http://127.0.0.1:4724"),capabilities);
////			    	  String topWindow = Windriver3.findElementByClassName("Static").getAttribute("EMERALD Remote App Log-In");
////			    	  int MAWinHandleInt = Integer.parseInt(topWindow);
////			    	  String MAWinHandleHex = Integer.toHexString(MAWinHandleInt);
////
////			    	  DesiredCapabilities caps = new DesiredCapabilities();
////			    	  caps.setCapability("appTopLevelWindow", MAWinHandleHex);
////			    	  Windriver3 = new WindowsDriver<WindowsElement>(new URL("http://127.0.0.1:4724"), caps);
//
//
//
//			    	  
//
//			    	   Windriver3 = new WindowsDriver<RemoteWebElement>(new URL("http://127.0.0.1:4723"), capabilities1);
//			    	   Windriver3.manage().timeouts().implicitlyWait(10,TimeUnit.MINUTES);
//			    	   Assert.assertNotNull(Windriver3);
//			    	   System.out.println("Second Session Launched");
//			    	   System.out.println("current window 2 is "+Windriver3.getWindowHandles().toArray().length);
//			    	   Windriver3.switchTo().window((String)Windriver3.getWindowHandles().toArray()[0]);
//			    	   WebElement loginButton = Windriver3.findElementByAccessibilityId("logInButton");
//					   Windriver3.findElementByAccessibilityId("userNameTextBox").click();
//					   Windriver3.findElementByAccessibilityId("userNameTextBox").sendKeys("TestUser2");
//					   System.out.println("Username Entered");
//					   Windriver3.findElementByAccessibilityId("passwordTextBox").click();
//				 		Windriver3.findElementByAccessibilityId("passwordTextBox").sendKeys("TestUser2");
//				 		System.out.println("Password Entered");
//				 		loginButton.click();
//				 		Thread.sleep(2000);
//				 		Windriver3.switchTo().window((String)Windriver3.getWindowHandles().toArray()[0]);
//
//				      WebElement availableConnectionsList2 = Windriver3.findElementByAccessibilityId("availableConnectionsWinListBox");
//						List<WebElement> availableConnections2 = availableConnectionsList2.findElements(By.xpath("//ListItem"));
//						DualHeadList.clear();
//						for (WebElement connection2 : availableConnections2) {
//							DualHeadList.add(connection2.getText());
//						}
//						System.out.println("List is "+DualHeadList);
//						for (String connectionName : DualHeadList) {
//							  Actions a = new Actions(Windriver3);
//							  WebElement targetConnection = availableConnectionsList2.findElement(By.name(connectionName));
//						      a.moveToElement(targetConnection).
//						      doubleClick().
//						      build().perform();
//						      System.out.println("connection named "+connectionName+" has been launched");
//						      Thread.sleep(10000);
//						      Windriver3.switchTo().window((String)Windriver3.getWindowHandles().toArray()[0]);
//						}
//						
//						Thread.sleep(10000);
//						RestAssured.useRelaxedHTTPSValidation();
//						
//					    String gerdetails = given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers("Content-Type", "application/json", "Accept","application/json")
//					    		    		.when().get("https://"+boxillaManager+"/bxa-api/connections/kvm/active")
//					    		    		.then().assertThat().statusCode(200)
//					    		    		.extract().response().asString();
//					    
//					    System.out.println("Response is "+gerdetails);
//			       
//			      }
//			      catch(Exception e){
//			    	  System.out.println("Exception has occured ");
//			        e.printStackTrace();
//			      } 
//
////			      Set<String> allWindowHandles = Windriver2.getWindowHandles();
////			      System.out.println("All Window handles are"+allWindowHandles);
//			  //    System.out.println("current window 1 is "+Windriver.getWindowHandles().toArray().length);
//			    
//			      //Windriver2.switchTo().window((String)Windriver2.getWindowHandles().toArray()[0]);
////			      Windriver2.findElementByAccessibilityId("menuLabel").click();
////					Windriver2.findElementByName("Close").click();
////			      Thread.sleep(5000);
//			     
//			 		
//				    
//				    Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);	
//					closeApp();
//					Thread.sleep(60000);
//					RestAssured.useRelaxedHTTPSValidation();
//					
//				    String Secdetails = given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers("Content-Type", "application/json", "Accept","application/json")
//				    		    		.when().get("https://"+boxillaManager+"/bxa-api/connections/kvm/active")
//				    		    		.then().assertThat().statusCode(200)
//				    		    		.extract().response().asString();
//				    
//				    System.out.println("Response is "+Secdetails);
//				    JsonPath js = new JsonPath(Secdetails);
//					int	countconnection=js.getInt("message.active_connections.size()");
//					System.out.println("Number of Active connections are "+countconnection);
//					System.out.println("Number of expected active connections to be 1");
//					softAssert.assertEquals(countconnection,"1"," Number of active connection didn't match with the number of connections before changing credentials");	{
//					Windriver3.switchTo().window((String)Windriver3.getWindowHandles().toArray()[0]);
//					Windriver3.findElementByAccessibilityId("menuLabel").click();
//					Windriver3.findElementByName("Close").click();
//					System.out.println("RemoteApp Closed");
//					Windriver3.quit();
//				    cleanUpLogin();
//				    for(int j=1;j<3;j++) {
//						UserPage.DeleteUser(firedrive,"TestUser"+j);
//						}
//				    ConnectionPage.DeleteConnection(firedrive, ZeroUDevices);
//				    cleanUpLogout();
//				   
//				   
//					}
//}
