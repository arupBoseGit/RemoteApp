package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;

import pages.Loginpage;

@Test
public class Boxillasetup {
	
	
	public WebDriver driver = new FirefoxDriver();
	
//	connections = .//span[@data-original-title="Connections"]
//	manage = (.//span[contains(text(),'Manage')])[1]
//	newconnection = .//div[@id='new-connection']	
//	connectionname = .//input[@id='connection-name']
//	host = .//input[@id='host']
	protected String userName="admin";
	public  String password="admin";
	public String boxillaManager="10.211.128.45";
	public void cleanUpLogin() {
		String url = "https://" + boxillaManager + "/";
			System.setProperty("webdriver.gecko.driver","C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setCapability("acceptInsecureCerts", true); // Accepting insecure content

			driver = new FirefoxDriver(caps);
			driver.manage().window().maximize();
			driver.get(url);
			
			try {
				
				Loginpage.username(driver).sendKeys(userName);

			
				Loginpage.password(driver).sendKeys(password);

				
				Loginpage.loginbtn(driver).click();
				
				
				
			} catch (Exception e) {
				System.out.println("Error in Clean up login method");
				e.printStackTrace();
				driver.quit();
			}
	}
	
}
