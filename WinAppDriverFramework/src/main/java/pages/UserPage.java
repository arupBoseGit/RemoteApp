package pages;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import methods.Device;
import methods.Devices;
import methods.Discovery;
import methods.SeleniumActions;
import tests.TestBase;

public class UserPage {
	
		private static WebElement element = null;
		

				
	public static WebElement user(WebDriver driver) {
		element=driver.findElement(By.xpath("(.//span[@class='list-group-item-value dropdown-btn'])[4]"));
		return element;
	}

	public static WebElement manage(WebDriver driver) {
		element = driver.findElement(By.xpath("(.//span[contains(text(),'Manage')])[2]"));
		return element;
	}

	public static WebElement Active(WebDriver driver) {
		element = driver.findElement(By.xpath("(.//span[contains(text(),'Active')])[2]"));
		return element;
	}

	public static WebElement NewUser(WebDriver driver) {
		element = driver.findElement(By.xpath(".//div[@id='new-user']"));
		return element;
	}

	public static WebElement useTempNo(WebDriver driver) {
		element = driver.findElement(By.xpath("(.//label[@class=\"btn btn-primary active\"])[2]"));
		return element;
	}
	public static WebElement ActiveDNo(WebDriver driver) {
		element = driver.findElement(By.xpath(".//label[@id=\"ldap-disabled-label\"]"));
		return element;
	}
	public static WebElement username(WebDriver driver) {
		element = driver.findElement(By.xpath(".//input[@id='username']"));
		return element;
	}
	public static WebElement password(WebDriver driver) {
		element = driver.findElement(By.xpath(".//input[@id='password']"));
		return element;
	}
	public static WebElement confirmPassword(WebDriver driver) {
		element = driver.findElement(By.xpath(".//input[@id='confirm-password']"));
		return element;
	}
	public static WebElement NextButton(WebDriver driver) {
		element = driver.findElement(By.xpath(".//button[@class=\"btn btn-primary wizard-pf-next\"]"));
		return element;
	}
	public static WebElement UserType(String usertype,WebDriver driver) {
		if (usertype.equalsIgnoreCase("Administrator")) {
		element = driver.findElement(By.xpath("(.//label[@class='btn btn-primary active'])[4]"));
			}
		else if (usertype.equalsIgnoreCase("Power")) {
			element = driver.findElement(By.xpath("(.//label[@class='btn btn-primary'])[5]"));
		}
		else if (usertype.equalsIgnoreCase("General")) {
			element = driver.findElement(By.xpath("(.//label[@class='btn btn-primary'])[4]"));
		}
			
		return element;
	}
	
	public static String IncorrectPassword(WebDriver driver) {
		element = driver.findElement(By.xpath(".//span[@id='password-help-block']"));
		String message = element.getText();
		return message;
	}
	
	public static WebElement Autoconnect(WebDriver driver) {
		element = driver.findElement(By.xpath(".//input[@id='auto-connect-name']"));
		return element;
	}
	
	public static WebElement RemoteAccess(WebDriver driver) {
		element = driver.findElement(By.xpath(".//div[@id='input-remote-access']//label"));
		return element;
	}
	
	public static WebElement savebutton(WebDriver driver) {
		element = driver.findElement(By.xpath(".//button[@class=\"btn btn-primary wizard-pf-save\"]"));
		return element;
	}
	
	public static WebElement searchOption(WebDriver driver) {
		element = driver.findElement(By.xpath(".//input[@type='search']"));
		return element;
	}
	
	public static WebElement optionbutton(WebDriver driver) {
		element = driver.findElement(By.xpath(".//button[@id='dropdownKebab']"));
		return element;
	}
	
	public static WebElement DeleteOption(WebDriver driver) {
		element = driver.findElement(By.xpath(".//li[@class='user-delete']"));
		return element;
	}
	
	public static WebElement ManageConnections(WebDriver driver) {
		element = driver.findElement(By.xpath(".//li[@class='user-manage-connections']"));
		return element;
	}
	
	public static WebElement EditUser(WebDriver driver) {
		element = driver.findElement(By.xpath(".//li[@class='user-edit']"));
		return element;
	}
	
	public static WebElement connectionfilter(WebDriver driver) {
		element = driver.findElement(By.xpath("(.//input[@class='filter form-control'])[3]"));
		return element;
	}
	
	public static WebElement Moveforward(WebDriver driver) {
		element =driver.findElement(By.xpath("(.//button[@class='btn moveall btn-default'])[2]"));
		return element;
	}
	
	public static WebElement Movebackward(WebDriver driver) {
		element =driver.findElement(By.xpath("(.//button[@class='btn removeall btn-default'])[2]"));
		return element;
	}
	
	public static WebElement SelectForward(WebDriver driver) {
		element = driver.findElement(By.xpath("(.//button[@title='Move selected'])[2]"));
		return element;
	}
	
	public static WebElement Connectionsave(WebDriver driver) {
		element = driver.findElement(By.xpath(".//button[@id='btn-connections-save']"));
		return element;
	}
	
	public static String ActiveUser(WebDriver driver) {
		return ".//div[@data-react-class='ActiveUsers']";
		
	}
	
	public void createUser(WebDriver drive,ArrayList<Device> devicename, String user, String pass,String type) throws Exception {
			
			drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			System.out.println(drive.getCurrentUrl());
			user(drive).click();
			drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			manage(drive).click();
			NewUser(drive).click();
			useTempNo(drive).click();
			ActiveDNo(drive).click();
			username(drive).sendKeys(user);
			password(drive).sendKeys(pass);
			confirmPassword(drive).sendKeys(pass);
			NextButton(drive).click();
			UserType(type, drive).click();
			RemoteAccess(drive).click();
			NextButton(drive).click();
			savebutton(drive).click();
			drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			drive.navigate().refresh();
			searchOption(drive).sendKeys(user);
			System.out.println("Username entered in search box");
			String deviceApplianceTable = SeleniumActions.seleniumGetText(drive, Devices.applianceTable);
			Assert.assertTrue(deviceApplianceTable.contains(user),
					"Device appliance table did not contain: " + user + ", actual text: " + deviceApplianceTable);
			ManageConnection(drive,devicename, user);
//			TestBase testbase = new TestBase();
//			testbase.cleanUpLogout(drive);
			//callback.cleanUp();
//			drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//			optionbutton(drive).click();
//			drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//			ManageConnections(drive).click();
//			for(Device connectedName : devicename) {
//				drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//				connectionfilter(drive).sendKeys(connectedName.getIpAddress());
//				drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//				Moveforward(drive).click();
//				drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//				connectionfilter(drive).clear();
//			}
//			Connectionsave(drive).click();
//			
			
		}
	public void ManageConnection(WebDriver drive,ArrayList<Device> devicename, String user) throws Exception {
//		TestBase testbase = new TestBase();
//		testbase.cleanUpLogin();
		Thread.sleep(2000);
		user(drive).click();
		drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		manage(drive).click();
		searchOption(drive).sendKeys(user);
		System.out.println("Username "+user+" entered in search box");
		Thread.sleep(2000);
		String deviceApplianceTable = SeleniumActions.seleniumGetText(drive, Devices.applianceTable);
		Assert.assertTrue(deviceApplianceTable.contains(user),
				"Device appliance table did not contain: " + user + ", actual text: " + deviceApplianceTable);
		drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		optionbutton(drive).click();
		drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		ManageConnections(drive).click();
		drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		Movebackward(drive).click();
		for(Device connectedName : devicename) {
			drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			connectionfilter(drive).sendKeys(connectedName.getIpAddress());
			drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			Moveforward(drive).click();
			drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			connectionfilter(drive).clear();
		}
		Connectionsave(drive).click();
	}
	
	public static void Sharedconnectionassign(WebDriver drive, String user, ArrayList<String> names) {
		user(drive).click();
		drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		manage(drive).click();
		drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		searchOption(drive).sendKeys(user);
		optionbutton(drive).click();
		drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		ManageConnections(drive).click();
		drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		Movebackward(drive).click();
		for(String connectedName : names) {
			drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			connectionfilter(drive).sendKeys(connectedName);
			drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			Moveforward(drive).click();
			drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			connectionfilter(drive).clear();
			System.out.println("Connection assigned "+connectedName);
		}
		Connectionsave(drive).click();
	}
	
	public static void DeleteUser(WebDriver drive, String user) {
		System.out.println("Navigated to User Tab to Delete username "+user);
		try {
			Thread.sleep(3000);
			user(drive).click();
			drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			manage(drive).click();
			searchOption(drive).sendKeys(user);
			optionbutton(drive).click();
			DeleteOption(drive).click();
			drive.switchTo().alert().accept();
			System.out.println(user+" is deleted");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			user(drive).click();
			drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			manage(drive).click();
			searchOption(drive).sendKeys(user);
			optionbutton(drive).click();
			DeleteOption(drive).click();
			drive.switchTo().alert().accept();
			System.out.println(user+" is deleted");
		}
		
	}
	
	public static String  currentUser(WebDriver drive, String username, int retry) throws Exception {
		String tableText;
		System.out.println("Validating the active user in boxilla");
		Thread.sleep(2000);
		user(drive).click();
		drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		Active(drive).click();
		drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);	
		
		do {
			System.out.println(" Attempt Number "+retry);
			tableText = SeleniumActions.seleniumGetText(drive, ActiveUser(drive));
			System.out.println("table text:" + tableText);
			if(tableText.contains(username)) {
				System.out.println("Found Active User:" + tableText);
				retry=0;
				
						}
			else {
				System.out.println("Expected User not found. Retrying.....");
				drive.navigate().refresh();
				Thread.sleep(3000);
				retry--;
			}
		}while(retry>0);
			
		return tableText;
	}
				
	public static void Remoteaccess(WebDriver drive) {
		
		user(drive).click();
		drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		manage(drive).click();
		searchOption(drive).sendKeys("admin");
		optionbutton(drive).click();
		EditUser(drive).click();
		NextButton(drive).click();
		RemoteAccess(drive).click();
		NextButton(drive).click();
		savebutton(drive).click();
		drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		drive.navigate().refresh();
		
	}
	

	
	public static void startTest(String testname) {
		System.out.println("******************************************************************");
		System.out.println("************* Starting Test "+testname+" **************");
		System.out.println("******************************************************************");
	}
	public static void endTest(String testname) {
		System.out.println("******************************************************************");
		System.out.println("************* Finishing Test "+testname+" **************");
		System.out.println("******************************************************************");
	}

		
	}


