package pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import methods.AppliancePool;
import methods.Device;
import methods.Devices;
import methods.SeleniumActions;

public class ConnectionPage {

	
	private static WebElement element = null;

	 
	public static WebElement connections(WebDriver driver) {
		element = driver.findElement(By.xpath("(.//span[@class='list-group-item-value dropdown-btn'])[3]"));
		return element;
	}
	
	public static WebElement manage(WebDriver driver) {
		element = driver.findElement(By.xpath("(.//span[@class='list-group-item-value'])[12]"));
		return element;
	}
	
	public static WebElement newconnection(WebDriver driver) {
		element = driver.findElement(By.xpath(".//div[@id='new-connection']"));
		return element;
	}
	
	public static WebElement connectvia(WebDriver driver) {
		element = driver.findElement(By.xpath("(.//label[@class='btn btn-primary active'])[1]"));
		return element;
	}
	
	public static WebElement template(WebDriver driver) {
		element = driver.findElement(By.xpath("(.//label[@class='btn btn-primary active'])[2]"));
		return element;
	}

	
	public static WebElement connectionName(WebDriver driver) {
		element = driver.findElement(By.xpath("	.//input[@id='connection-name']"));
		return element;
	}
	
	public static WebElement Host(WebDriver driver) {
		element = driver.findElement(By.xpath("	.//input[@id='host']"));
		return element;
	}
			
	public static WebElement optimised(WebDriver driver) {
		element = driver.findElement(By.xpath("	(.//label[@class='btn btn-primary active'])[4]"));
		return element;
	}
	public static WebElement nextoption(WebDriver driver) {
		element = driver.findElement(By.xpath(".//button[contains(text(),'Next')]"));
		return element;
	}
	
	public static WebElement privateconnectionType(WebDriver driver) {
		element = driver.findElement(By.xpath("(.//label[@class='btn btn-primary active'])[5]"));
		return element;
	}
	
	public static WebElement sharedconnectionType(WebDriver driver) {
		element = driver.findElement(By.xpath("(.//label[@class='btn btn-primary'])[7]"));
		return element;
	}
	
	public static WebElement extendDesktop(WebDriver driver) {
		element = driver.findElement(By.xpath("(.//label[contains(text(),'Extended Desktop')])[1]"));
		return element;
	}
	
	public static WebElement Audio(WebDriver driver) {
		element = driver.findElement(By.xpath("(.//label[contains(text(),'Audio')])[1]"));
		return element;
	}
	
	public static WebElement viewonly(WebDriver driver) {
		element = driver.findElement(By.xpath("(.//label[contains(text(),'View Only')])[1]"));
		return element;
	}
	
	public static WebElement Saveoption(WebDriver driver) {
		element = driver.findElement(By.xpath(".//button[@class='btn btn-primary wizard-pf-save']"));
		return element;
	}
	
	public static WebElement searchOption(WebDriver driver) {
		element = driver.findElement(By.xpath("(.//input[@type='search'])[1]"));
		return element;
	}
	
	public static WebElement connectiontable(WebDriver driver) {
		element = driver.findElement(By.xpath("(.//div[@class='bb-table'])[1]"));
		return element;
	}
	
	public static WebElement Optionicon(WebDriver driver) {
		element = driver.findElement(By.xpath(".//button[@id='dropdownKebab']"));
		return element;
	}
	
	public static WebElement Deleteoption(WebDriver driver) {
		element = driver.findElement(By.xpath(".//a[@class='connection-delete']"));
		return element;
	}
	
	

		
	@SuppressWarnings("deprecation")
	public static void createprivateconnections(WebDriver driver, ArrayList<Device> devicename) throws Exception  {
	WebDriverWait wait=new WebDriverWait(driver, 20);
	System.out.println("All connections are ");
			
				for(Device deviceList : devicename) {
					System.out.println("Adding the connection "+deviceList.getIpAddress());
					connections(driver).click();
					driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
					manage(driver).click();
					driver.manage().timeouts().implicitlyWait(3,TimeUnit.SECONDS);
					newconnection(driver).click();
					driver.manage().timeouts().implicitlyWait(4,TimeUnit.SECONDS);
					connectionName(driver).sendKeys(deviceList.getIpAddress());
					Host(driver).sendKeys(deviceList.getIpAddress());
					optimised(driver).click();
					nextoption(driver).click();
					privateconnectionType(driver).click();
					Audio(driver).click();
					nextoption(driver).click();
					driver.manage().timeouts().implicitlyWait(4,TimeUnit.SECONDS);
					Saveoption(driver).click();
					driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
					wait.until(ExpectedConditions.visibilityOfAllElements(connectiontable(driver)));
					searchOption(driver).sendKeys(deviceList.getIpAddress());
					System.out.println("Connection Name entered in search box");
					driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
					Thread.sleep(3000);
					wait.until(ExpectedConditions.visibilityOfAllElements(connectiontable(driver)));
					String deviceApplianceTable = SeleniumActions.seleniumGetText(driver, Devices.applianceTable);
					Assert.assertTrue(deviceApplianceTable.contains(deviceList.getIpAddress()),
							"Table did not contain: " + deviceList.getIpAddress() + ", actual text: " + deviceApplianceTable);
				}
				
	}	
			
		

	public static void DeleteConnection(WebDriver driver, ArrayList<Device> devicename) throws Exception {
			
		connections(driver).click();
		driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
		manage(driver).click();
		driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
//		for(String deviceList : SharedNames) {
//			System.out.println("Deleting the connection "+deviceList);
//			searchOption(driver).clear();
//			searchOption(driver).sendKeys(deviceList);
//			driver.manage().timeouts().implicitlyWait(3,TimeUnit.SECONDS);
//			Optionicon(driver).click();
//			Deleteoption(driver).click();
//			driver.switchTo().alert().accept();
//			System.out.println(deviceList+" is deleted");
//			Thread.sleep(2000);
//	}
		for(Device deviceList : devicename) {
			System.out.println("Deleting the connection "+deviceList.getIpAddress());
			searchOption(driver).clear();
			searchOption(driver).sendKeys(deviceList.getIpAddress());
			driver.manage().timeouts().implicitlyWait(3,TimeUnit.SECONDS);
			if(Optionicon(driver)!=null) {
			Optionicon(driver).click();
			Deleteoption(driver).click();
			driver.switchTo().alert().accept();
			System.out.println(deviceList.getIpAddress()+" is deleted");
			Thread.sleep(2000);
			}
			System.out.println(" connection named - "+deviceList.getIpAddress()+" is not in the list");
	}
	
}
	public static ArrayList Sharedconnection(WebDriver driver, ArrayList<Device> devicename) throws Exception  {
		WebDriverWait wait=new WebDriverWait(driver, 20);
		System.out.println("All connections are ");
		ArrayList<String> Sharedconnection = new 	ArrayList<String>();	
					for(Device deviceList : devicename) {
						System.out.println("Adding the connection "+deviceList.getIpAddress());
						for(int i =1;i<=5;i++) {
						connections(driver).click();
						driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
						manage(driver).click();
//						driver.manage().timeouts().implicitlyWait(3,TimeUnit.SECONDS);
						newconnection(driver).click();
						driver.manage().timeouts().implicitlyWait(4,TimeUnit.SECONDS);
						connectionName(driver).sendKeys(deviceList.getIpAddress()+"test"+i);
						Host(driver).sendKeys(deviceList.getIpAddress());
						optimised(driver).click();
						nextoption(driver).click();
						sharedconnectionType(driver).click();
						Audio(driver).click();
						nextoption(driver).click();
						driver.manage().timeouts().implicitlyWait(4,TimeUnit.SECONDS);
						Saveoption(driver).click();
						driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
						wait.until(ExpectedConditions.visibilityOfAllElements(connectiontable(driver)));
						Sharedconnection.add(deviceList.getIpAddress()+"test"+i);
						searchOption(driver).sendKeys(deviceList.getIpAddress()+"test"+i);
						System.out.println("Connection Name entered in search box");
						driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
						Thread.sleep(4000);
						wait.until(ExpectedConditions.visibilityOfAllElements(connectiontable(driver)));
						String deviceApplianceTable = SeleniumActions.seleniumGetText(driver, Devices.applianceTable);
						Assert.assertTrue(deviceApplianceTable.contains(deviceList.getIpAddress()+"test"+i),
								"Table did not contain: " + deviceList.getIpAddress()+"test"+i + ", actual text: " + deviceApplianceTable);
					}
						break;
					}
					System.out.println(Sharedconnection);
					return Sharedconnection;
					
		}	
}

