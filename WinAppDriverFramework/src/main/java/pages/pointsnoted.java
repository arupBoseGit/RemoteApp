package pages;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import methods.Device;
import methods.Devices;
import methods.SeleniumActions;
import tests.TestBase;
import tests.TestBase.Callback;

public class pointsnoted {

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
}
