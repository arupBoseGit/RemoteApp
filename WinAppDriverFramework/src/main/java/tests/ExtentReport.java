package tests;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.testng.annotations.Test;




public class ExtentReport {

	@Test
	public void ExtentReport() throws IOException {
		
			
		/* ExtentReports extent = new ExtentReports();
	     ExtentSparkReporter spark = new ExtentSparkReporter("index.html");//html file will be generated
	     ExtentSparkReporter failedspark = new ExtentSparkReporter("failed-tests-index.html").filter().statusFilter().as(new Status[] {Status.FAIL}).apply();
	     failedspark.config().setDocumentTitle("Failed Tests");
	     //extent.attachReporter(spark);
	     final File CONF = new File("config.json");
	//     ExtentSparkReporter spark = new ExtentSparkReporter("target/spark/spark.html");
	     spark.loadJSONConfig(CONF);
	     final File CONF = new File("config.xml");
	     spark.loadXMLConfig(CONF);
//	     spark.config().setTheme(Theme.DARK);
//	     spark.config().setDocumentTitle("RemoteApp automation report");
//	     spark.config().setReportName("Extent reports Demo");
	     extent.attachReporter(spark);
	     
	     ExtentTest test=extent.createTest("Login Test");
	     test.pass("Login test successfully");
	     test.info("login");
	     test.fail("Incorrect inpuit");
	     
	     extent.flush();
	     Desktop.getDesktop().browse(new File("index.html").toURI());//open the file the to the desktop
	     Desktop.getDesktop().browse(new File("failedtestsindex.html").toURI());*/
	}
}
