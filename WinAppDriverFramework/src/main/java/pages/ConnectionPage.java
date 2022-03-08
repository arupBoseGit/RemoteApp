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
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.log4testng.Logger;

import methods.AppliancePool;
import methods.Device;
import methods.Devices;
import methods.SeleniumActions;
import tests.SecondPhase;

public class ConnectionPage {

	final static Logger log = Logger.getLogger(ConnectionPage.class);
	private static WebElement element = null;

	 
	public static WebElement connections(WebDriver driver) {
		element = driver.findElement(By.xpath("(.//span[@class='list-group-item-value dropdown-btn'])[3]"));
		return element;
	}
	
	public static WebElement manage(WebDriver driver) {
		element = driver.findElement(By.xpath("//a[@href='/connections/manage']"));//(.//span[@class='list-group-item-value'])[12]
		return element;
	}
	
	public static WebElement newconnection(WebDriver driver) {
		element = driver.findElement(By.xpath(".//div[@id='new-connection']"));
		return element;
	}
	
	public static WebElement connectviaRDP(WebDriver driver) {
		element = driver.findElement(By.xpath("(.//label[@class='btn btn-primary'])[1]"));
		return element;
	}
	public static WebElement connectviaTx(WebDriver driver) {
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
	
	public static WebElement AdminUsername(WebDriver driver) {
		element = driver.findElement(By.xpath("	.//input[@id='username']"));
		return element;
	}
	
	public static WebElement AdminUserPassword(WebDriver driver) {
		element = driver.findElement(By.xpath("	.//input[@id='password']"));
		return element;
	}
	
	public static WebElement Domain(WebDriver driver) {
		element = driver.findElement(By.xpath("	.//input[@id='domain']"));
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
		element = driver.findElement(By.xpath(".//*[@id='input-c-type']/div/div/label[1]"));
		return element;
	}
	
	public static WebElement sharedconnectionType(WebDriver driver) {
		element = driver.findElement(By.xpath(".//*[@id='input-c-type']/div/div/label[2]"));
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
	
	public static WebElement Editconnection(WebDriver driver) {
		element = driver.findElement(By.xpath(".//a[@class='connection-edit']"));
		return element;
	}
			
	public static WebElement ConnectionViewer(WebDriver driver) {
		element = driver.findElement(By.xpath("(//*[text()[contains(.,'Viewer')]])[1]"));
		return element;
	}
	
	
	public static WebElement MakeConnection(WebDriver driver) {
		element = driver.findElement(By.xpath("//*[text()[contains(.,'Make Connection')]]"));
		return element;
	}
			
	public static WebElement InputID(WebDriver driver) {
		element = driver.findElement(By.xpath("(.//input[@id='source-name'])[2]"));
		return element;
	}
	
	public static WebElement destinationID(WebDriver driver) {
		element = driver.findElement(By.xpath("(.//input[@id='source-name'])[3]"));
		return element;
	}
			
	public static WebElement PrivatedestinationID(WebDriver driver) {
		element = driver.findElement(By.xpath(".//input[@id='destSelect']"));
		return element;
	}
	public static WebElement checkBox(WebDriver driver) {
		element = driver.findElement(By.xpath("(.//input[@type='checkbox'])[2]"));
		return element;
	}
			
	public static WebElement AddDestination(WebDriver driver) {
		element = driver.findElement(By.xpath("(//*[text()[contains(.,'Add Destination')]])[1]"));
		return element;
			}
					
	public static WebElement ActivateselectedTx(WebDriver driver) {
		element = driver.findElement(By.xpath("(//*[text()[contains(.,'Activate Selected')]])[1]"));
		return element;
			}
	
	public static WebElement ActivateselectedRx(WebDriver driver) {
		element = driver.findElement(By.xpath("(//*[text()[contains(.,'Activate')]])[4]"));
		return element;
			}
	
	public static WebElement HoverConnection(WebDriver driver,ArrayList connName) {
		element = driver.findElement(By.xpath("(//*[text()[contains(.,'"+connName+"')]])[1]"));
		return element;
			}
	public static WebElement ActivatesharedselectedRx(WebDriver driver) {
		element = driver.findElement(By.xpath("(//*[text()[contains(.,'Activate Selected')]])[3]"));
		return element;
			}
	public static int TableContent(WebDriver driver) {
		String countelement = driver.findElement(By.xpath("(.//div[@id=\"conntable_info\"]/b)[1]"));
		
		System.out.println(Integer.parseInt(countelement));
		return Integer.parseInt(countelement);
	}
	
	public static WebElement firstItemInSourceList(WebDriver driver, String connectionName) {
		element = driver.findElement(By.xpath("//td[text()='" + connectionName + "']"));
		return element;
	}
	
	public static WebElement DisconnectConnection(WebDriver driver) {
		element = driver.findElement(By.xpath("(.//span[@class='fa fa-times'])[1]"));
		return element;
	}
	
public static ArrayList Sharedconnection(WebDriver driver, ArrayList<Device> devicename, int number, String type) throws Exception  {
		
		WebDriverWait wait=new WebDriverWait(driver, 20);
		System.out.println("All connections are ");
		ArrayList<String> Sharedconnection = new 	ArrayList<String>();	
					for(Device deviceList : devicename) {
						System.out.println("Adding the connection "+deviceList.getIpAddress());
						for(int i=1;i<=number;i++) {
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
						if (type.equalsIgnoreCase("Private")) {
							privateconnectionType(driver).click();
						} else sharedconnectionType(driver).click();
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



public static ArrayList CreateConnection(WebDriver driver, ArrayList<Device> devicename, int number, String type) throws Exception  {
	
	WebDriverWait wait=new WebDriverWait(driver, 20);
	System.out.println("All connections are ");
	ArrayList<String> Sharedconnection = new 	ArrayList<String>();	
				for(Device deviceList : devicename) {
					System.out.println("Adding the connection "+deviceList.getIpAddress());
					for(int i=1;i<=number;i++) {
//					connections(driver).click();
//					driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
//					manage(driver).click();
//					driver.manage().timeouts().implicitlyWait(3,TimeUnit.SECONDS);
					
					SeleniumActions.seleniumClick(driver, LandingPage.connectionsTab);
					log.info("Navigate to Connections > Manage : Connections Tab clicked");
					new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(By.xpath( LandingPage.connectionsManage)));
					SeleniumActions.seleniumClick(driver, LandingPage.connectionsManage);
					log.info("Navigate to Connections > Manage : Connections > Manage Tab clicked");
					newconnection(driver).click();
					driver.manage().timeouts().implicitlyWait(4,TimeUnit.SECONDS);
					connectionName(driver).sendKeys(deviceList.getIpAddress()+"test"+i);
					Host(driver).sendKeys(deviceList.getIpAddress());
					optimised(driver).click();
					nextoption(driver).click();
					if (type.equalsIgnoreCase("Private")) {
						privateconnectionType(driver).click();
					} else sharedconnectionType(driver).click();
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
		
	
	public static void createprivateconnections(WebDriver driver, ArrayList<Device> devicename) throws Exception  {
	WebDriverWait wait=new WebDriverWait(driver, 20);
	System.out.println("All connections are ");
			
				for(Device deviceList : devicename) {
					System.out.println("Adding the connection "+deviceList.getIpAddress());
					SeleniumActions.seleniumClick(driver, LandingPage.connectionsTab);
					log.info("Navigate to Connections > Manage : Connections Tab clicked");
					new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(By.xpath( LandingPage.connectionsManage)));
					SeleniumActions.seleniumClick(driver, LandingPage.connectionsManage);
					log.info("Navigate to Connections > Manage : Connections > Manage Tab clicked");
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
			
	public static void CreateRDPconnection(WebDriver driver, String VMIp, String VMusername, String VMpassword, String VMdomain) throws Exception {
			
		SeleniumActions.seleniumClick(driver, LandingPage.connectionsTab);
		log.info("Navigate to Connections > Manage : Connections Tab clicked");
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(By.xpath( LandingPage.connectionsManage)));
		SeleniumActions.seleniumClick(driver, LandingPage.connectionsManage);
		log.info("Navigate to Connections > Manage : Connections > Manage Tab clicked");
		newconnection(driver).click();
		driver.manage().timeouts().implicitlyWait(4,TimeUnit.SECONDS);
		ConnectionPage.connectviaRDP(driver).click();
		ConnectionPage.connectionName(driver).sendKeys(VMIp);
		ConnectionPage.Host(driver).sendKeys(VMIp);
		ConnectionPage.AdminUsername(driver).sendKeys(VMusername);	
		ConnectionPage.AdminUserPassword(driver).sendKeys(VMpassword);
		ConnectionPage.nextoption(driver).click();
		ConnectionPage.Domain(driver).sendKeys(VMdomain);
		ConnectionPage.Audio(driver).click();
		ConnectionPage.nextoption(driver).click();
		driver.manage().timeouts().implicitlyWait(4,TimeUnit.SECONDS);
		ConnectionPage.Saveoption(driver).click();
		driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);	
		ConnectionPage.searchOption(driver).sendKeys(VMIp);
		System.out.println("Connection Name entered in search box");
		driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
		Thread.sleep(4000);
		String deviceApplianceTable = SeleniumActions.seleniumGetText(driver, Devices.applianceTable);
		Assert.assertTrue(deviceApplianceTable.contains(VMIp),
				"Table did not contain: " + VMIp + ", actual text: " + deviceApplianceTable);
	}

	public static void DeleteConnection(WebDriver driver, ArrayList<Device> devicename) throws Exception {
			
		SeleniumActions.seleniumClick(driver, LandingPage.connectionsTab);
		log.info("Navigate to Connections > Manage : Connections Tab clicked");
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(By.xpath( LandingPage.connectionsManage)));
		SeleniumActions.seleniumClick(driver, LandingPage.connectionsManage);
		log.info("Navigate to Connections > Manage : Connections > Manage Tab clicked");

		for(Device deviceList : devicename) {
			System.out.println("Deleting the connection "+deviceList.getIpAddress());
			searchOption(driver).clear();
			searchOption(driver).sendKeys(deviceList.getIpAddress());
			driver.manage().timeouts().implicitlyWait(3,TimeUnit.SECONDS);
			Optionicon(driver).click();
			Deleteoption(driver).click();
			driver.switchTo().alert().accept();
			System.out.println(deviceList.getIpAddress()+" is deleted");
			Thread.sleep(2000);
		
	}
	
}
	
	public static void DeleteSharedConnection(WebDriver driver, ArrayList<String> devicename) throws Exception {
		
		SeleniumActions.seleniumClick(driver, LandingPage.connectionsTab);
		log.info("Navigate to Connections > Manage : Connections Tab clicked");
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(By.xpath( LandingPage.connectionsManage)));
		SeleniumActions.seleniumClick(driver, LandingPage.connectionsManage);
		log.info("Navigate to Connections > Manage : Connections > Manage Tab clicked");
		for(String deviceList : devicename) {
			System.out.println("Deleting the connection "+deviceList);
			searchOption(driver).clear();
			searchOption(driver).sendKeys(deviceList);
			driver.manage().timeouts().implicitlyWait(3,TimeUnit.SECONDS);
			Optionicon(driver).click();
			Deleteoption(driver).click();
			driver.switchTo().alert().accept();
			System.out.println(deviceList+" is deleted");
			Thread.sleep(2000);
	}
	}
	
				 public static void launchPrivateConnection(WebDriver driver, ArrayList<String> connectionName, String receivername) throws InterruptedException {
					 for(String connection : connectionName) {
							 connections(driver).click();
							 ConnectionViewer(driver).click();
							 MakeConnection(driver).click();
							 InputID(driver).sendKeys(connection);
							 new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(firstItemInSourceList(driver, connection)));
							 firstItemInSourceList(driver, connection).click();
							 ActivateselectedTx(driver).click();
							 Thread.sleep(3000);
							// new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(AddDestination(driver)));
							 AddDestination(driver).click();
							 Thread.sleep(3000);
							// new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(destinationID(driver)));
							 PrivatedestinationID(driver).sendKeys(receivername);							
							// firstItemInSourceList(driver, receivername).click();
							 ActivateselectedRx(driver).click();
							 driver.navigate().refresh();
							 System.out.println("Connection Launched in Tx-Rx");
						 }
				 }
				 
				 public static void launchSharedConnection(WebDriver driver, ArrayList<String> connectionName, String receivername) throws InterruptedException {
					 for(String connection : connectionName) {
							 connections(driver).click();
							 ConnectionViewer(driver).click();
							 MakeConnection(driver).click();
							 InputID(driver).sendKeys(connection);
							 new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(firstItemInSourceList(driver, connection)));
							 firstItemInSourceList(driver, connection).click();
							 ActivateselectedTx(driver).click();
							 Thread.sleep(3000);
							// new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(AddDestination(driver)));
							 AddDestination(driver).click();
							 Thread.sleep(3000);
							 destinationID(driver).sendKeys(receivername);
							 new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(firstItemInSourceList(driver, receivername)));
							 firstItemInSourceList(driver, receivername).click();
							 ActivatesharedselectedRx(driver).click();
							 driver.navigate().refresh();
							 System.out.println("Connection Launched in Tx-Rx");
						 }
				 }
				 
				 public static void BreakboxillaConnection(WebDriver driver) throws InterruptedException {
					 new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(connections(driver)));
					 connections(driver).click();
					 ConnectionViewer(driver).click();
					 Thread.sleep(2000);
					 DisconnectConnection(driver).click();
					 
				 }
	
}

