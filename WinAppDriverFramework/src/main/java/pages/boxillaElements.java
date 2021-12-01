package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import tests.TestBase;

public class boxillaElements {
	private static WebElement element = null;
	private static String BoxilaUsernamexpath = ".//input[@id='login_login']";
	private static String BoxilaPasswordxpath = ".//input[@id='login_password']";
	private static String BoxillaLogin = ".//input[@value='Login']";

public static WebElement username(WebDriver driver) {
	element = driver.findElement(By.xpath(BoxilaUsernamexpath));
	return element;
}

public static WebElement password(WebDriver driver) {
	element = driver.findElement(By.xpath(BoxilaPasswordxpath));
	return element;
}

public static WebElement Login(WebDriver driver) {
	element = driver.findElement(By.xpath(BoxillaLogin));
	return element;
}



}
