	package tests;
	import static io.restassured.RestAssured.*;
	import org.testng.annotations.Test;
	import static org.hamcrest.Matcher.*;
	import java.util.ArrayList;
	import java.util.List;
	import java.util.concurrent.TimeUnit;
	
	import javax.json.Json;
	import javax.ws.rs.core.Application;
	
	import org.json.simple.JSONArray;
	import org.json.simple.JSONObject;
	import org.json.simple.parser.JSONParser;
	import org.json.simple.parser.ParseException;
	import org.openqa.selenium.By;
	import org.openqa.selenium.WebDriver;
	import org.openqa.selenium.WebElement;
	import org.openqa.selenium.firefox.FirefoxDriver;
	import org.openqa.selenium.interactions.Actions;
	import org.openqa.selenium.remote.DesiredCapabilities;
	import org.testng.Assert;
	import org.testng.annotations.Test;
	import org.testng.asserts.SoftAssert;
	
	import io.restassured.RestAssured;
	import io.restassured.http.ContentType;
	import io.restassured.path.json.JsonPath;
	import io.restassured.response.Response;
	
	import methods.Device;
	import methods.Devices;
	import methods.SeleniumActions;
	import pages.ConnectionPage;
	import pages.UserPage;
	import pages.boxillaElements;
	
	
	public class RemoteAppTest extends TestBase{
		int countconnection;
		String gerdetails;
	//Poc: 1) Configure BXA Settings.(creating user and managing connections is through boxilla)
		
		
		@Test(priority=1)
		public void Test01_configuration() throws InterruptedException {
		UserPage.startTest("Test01_configuration");
		setup();
		Windriver.manage().timeouts().implicitlyWait(3,TimeUnit.SECONDS);
		Windriver.findElementByName("Demo Mode").click();
		Windriver.findElementByName("Submit").click();
		Thread.sleep(2000);
		System.out.println(Windriver.getWindowHandle());
		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
	    System.out.println(Windriver.getWindowHandle());
	    getElement("menuLabel").click();
	    Windriver.findElementByName("Settings").click();
		getElement("ipAddressTextBox").clear();
		getElement("ipAddressTextBox").sendKeys(boxillaManager);
		Windriver.findElementByName("Configure").click();
		System.out.println("IP has been configured with "+boxillaManager);
		closeRemoteApp();
		UserPage.endTest("Test01_configuration");
		}
	
		@Test(priority=2)
		public void Test02_launchconnection() throws InterruptedException {
		UserPage.startTest("Test02_launchconnection");
		setup();
		int count=0;
		int connectionNumber=1;
		WebElement loginButton = getElement("logInButton");
		getElement("userNameTextBox").sendKeys(RAusername);
		getElement("passwordTextBox").sendKeys(RApassword);
		loginButton.click();
	    Thread.sleep(3000);
		System.out.println(Windriver.getWindowHandle());
		Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
	 //   System.out.println(Windriver.getWindowHandle());
		Thread.sleep(1500);
		WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
		List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
		
		for (WebElement connection : availableConnections) {
		  System.out.println("connections number  "+connectionNumber+" is "+connection.getText());
		  connectionList.add(connection.getText());
		  connectionNumber++;
		}
	//	for(int i=1;i<=1000;i++) {
		for (String connectionName : connectionList) {
		WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionName));
	//	System.out.print("connection number is "+i+"  ");
	     Actions a = new Actions(Windriver);
	      a.moveToElement(targetConnection).
	      doubleClick().
	      build().perform();
	      Thread.sleep(30000);
	      count++;
	      System.out.println(connectionName+" has been launched");
	      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		}
		Thread.sleep(15000);
	
		  System.out.println("**********checking launched connection - responses******************");
	   
		  RestAssured.useRelaxedHTTPSValidation();
	      gerdetails = RestAssured.given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers("Content-Type", "application/json", "Accept","application/json")
							.when().get("https://"+boxillaManager+"/bxa-api/connections/kvm/active")
							.then().assertThat().statusCode(200)
							.extract().response().asString();
		
	      System.out.println(gerdetails);
	     
				System.out.println("********************checking the connection size  ******************");
				JsonPath js = new JsonPath(gerdetails);
				countconnection=js.getInt("message.active_connections.size()");
				System.out.println("message size is "+count);
				    System.out.println("connection size is "+countconnection);
				    for (int j=0;j<countconnection;j++) {
				    	String ActiveconnectionName = js.get("message.active_connections["+j+"].connection_name");
				    	float fps =js.get("message.active_connections["+j+"].fps");
				    	System.out.println("For the connection named "+ActiveconnectionName+" the FPS value is "+fps);
				    	if(fps<2.0) {
							fpsstatus = false;
							softAssertion.assertTrue(fpsstatus, "Test Failed : FPS value is either less than 40 or null ");
							}
							else softAssertion.assertTrue(fpsstatus, "Test Passed : FPS value is "+fps);
			}
			    
			   	Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			 
			   for (String connectionName : connectionList) {
			    	Windriver.findElement(By.name(connectionName)).click();
			    	Windriver.findElement(By.name("Disconnect")).click();
				    Thread.sleep(5000);
				   // System.out.println(Windriver.getWindowHandles());
				    System.out.println("connection "+connectionName+" is disconnected");
				    Windriver.switchTo().window(Windriver.getWindowHandle());
				    
				
		
		}
		 Windriver.switchTo().window(Windriver.getWindowHandle());
		 closeApp();
			UserPage.endTest("Test02_launchconnection");
			softAssertion.assertAll();
			}
	
	//	@Test(priority=3)
		public void Test02_sharedconnection() throws InterruptedException {
			UserPage.startTest("Test02_sharedconnection");
			setup();
			int count=0;
			int connectionNumber=1;
			WebElement loginButton = getElement("logInButton");
			getElement("userNameTextBox").sendKeys(RAusername);
			getElement("passwordTextBox").sendKeys(RApassword);
			loginButton.click();
		    Thread.sleep(3000);
		//	System.out.println(Windriver.getWindowHandle());
			Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
		//    System.out.println(Windriver.getWindowHandle());
			WebElement availableConnectionsList = getElement("availableConnectionsWinListBox");
		//	List<WebElement> availableConnections = availableConnectionsList.findElements(By.xpath("//ListItem"));
			
//			for (String connection : SharedNames) {
//			  System.out.println("connections number  "+connectionNumber+" is "+connection.getText());
//			  connectionList.add(connection.getText());
//			  connectionNumber++;
//			}
			for (String connectionName : SharedNames) {
			WebElement targetConnection = availableConnectionsList.findElement(By.name(connectionName));
			
		     Actions a = new Actions(Windriver);
		      a.moveToElement(targetConnection).
		      doubleClick().
		      build().perform();
		      Thread.sleep(30000);
		      count++;
		      System.out.println(connectionName+" has been launched");
		      Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
			}
			Thread.sleep(30000);
		
			  System.out.println("**********checking launched connection - responses******************");
		   
			  RestAssured.useRelaxedHTTPSValidation();
		      gerdetails = RestAssured.given().auth().preemptive().basic(AutomationUsername, AutomationPassword).headers("Content-Type", "application/json", "Accept","application/json")
								.when().get("https://"+boxillaManager+"/bxa-api/connections/kvm/active")
								.then().assertThat().statusCode(200)
								.extract().response().asString();
			
		      System.out.println(gerdetails);
		     
					System.out.println("********************checking the connection size  ******************");
					JsonPath js = new JsonPath(gerdetails);
					countconnection=js.getInt("message.active_connections.size()");
					System.out.println("message size is "+count);
					    System.out.println("connection size is "+countconnection);
					    for (int i=0;i<countconnection;i++) {
					    	String ActiveconnectionName = js.get("message.active_connections["+i+"].connection_name");
					    	float fps =js.get("message.active_connections["+i+"].fps");
					    	System.out.println("For the connection named "+ActiveconnectionName+" the FPS value is "+fps);
					    	if(fps<3.0) {
								fpsstatus = false;
								softAssertion.assertTrue(fpsstatus, "Test Failed : FPS value is either less than 40 or null ");
								}
								else softAssertion.assertTrue(fpsstatus, "Test Passed : FPS value is "+fps);
				}
				    
				   	Windriver.switchTo().window((String)Windriver.getWindowHandles().toArray()[0]);
				 
				  	for (String connectionName : SharedNames) {
				    	Windriver.findElement(By.name(connectionName)).click();
				    	Windriver.findElement(By.name("Disconnect")).click();
					    Thread.sleep(5000);
					   // System.out.println(Windriver.getWindowHandles());
					    System.out.println("connection "+connectionName+" is disconnected");
					    Windriver.switchTo().window(Windriver.getWindowHandle());
					    
					
			}
				
				UserPage.endTest("Test03_sharedconnection");
				softAssertion.assertAll();
				}
	
	
	
	
		//@Test(priority=3)
		public void Test03_termination() throws Exception {
		UserPage.startTest("Test03_termination");
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
		   System.out.println("All the connectections are terminated");
		   Assert.assertTrue(true);
		   
	   }else Assert.assertFalse(true, +countconnection+" Connection is still active");
	   UserPage.endTest("Test03_termination");
	}
	
		}
	
	
	
	
	
	
