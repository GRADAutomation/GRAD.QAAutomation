package TestSuite1;

/**
 * This is JUnit test case for testing MCAT Diagnostic Test REVIEW page functionality
 * @author Resmi
 *
 */
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
//import org.openqa.selenium.HasInputDevices;
//import org.openqa.selenium.Mouse;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.remote.Augmenter;
import org.testng.annotations.Test;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;	

import Database.Excel_Ops;
import ParentClasses.DriverScript;
import Reports.ReportUtil;
import Utility.Keywords;
import Utility.TestUtil;
import Utility.Variable_Conversions;
import Utility.ChartsUtil;

public class TopOpportunities extends DriverScript{
	
	//private StringBuffer verificationErrors = new StringBuffer();

	static Logger log = Logger.getLogger(ReviewPg.class.getName());
	Excel_Ops d = null;
	Utility.Variable_Conversions vc = null;

	@Test
	public String completeFlowTest(String sheetName) throws IOException, InterruptedException {
		methodResult = "Pass"; classResult = "Pass"; String result = null;
		//Reading properties file in Java example
			 
		APPLICATION_LOGS.debug("Inside ReviewPageTest" + sheetName);
		d = new Excel_Ops(System.getProperty("user.dir")+"\\src\\Config\\"+currentDataXL);
		vc = new Variable_Conversions();
		
		try{
			
		if (TestUtil.jasperLogin().equals("Pass")) {
			Thread.sleep(2000);
						
			driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
			//driver.get("http://jasperstg.kaptest.com/apps/delivery/smartReportHome.aspx");
			if (!currentBrowser.equals("IE") && !currentBrowser.equals("Chrome"))
				driver.switchTo().frame("main");
		
			Keywords.clickLinkText(currentTestName.trim());
			
			driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);				
			TestUtil.VerifyJasperHeader("Item Review");
			TestUtil.VerifyJasperFooter();
			Keywords.clickLink("ItmRw_Nav_Performance_Link");
			TestUtil.VerifyJasperHeader("Performance Summary");
			TestUtil.VerifyJasperFooter();
			Thread.sleep(100L);
			Keywords.clickLink("PerfSum_RecNextSteps_Link");
			TestUtil.VerifyJasperHeader("Recommended Next Steps");
			TestUtil.VerifyJasperFooter();
			
			Keywords.verifyCustomObjectText(TestUtil.getStringValueinArray(OR, "PerfSummary_RecNextStep_Header", "Key"), TestUtil.getStringValueinArray(OR, "PerfSummary_RecNextStep_Header", "Value"));

	
			Keywords.verifyCustomObjectText(TestUtil.getStringValueinArray(OR, "PerfSummary_RecNextStep_1", "Key"), TestUtil.getStringValueinArray(OR, "PerfSummary_RecNextStep_1", "Value"));
			Keywords.verifyCustomObjectText(TestUtil.getStringValueinArray(OR, "PerfSummary_RecNextStep_2", "Key"), TestUtil.getStringValueinArray(OR, "PerfSummary_RecNextStep_2", "Value"));
			Keywords.verifyCustomObjectText(TestUtil.getStringValueinArray(OR, "PerfSummary_RecNextStep_2", "Key"), TestUtil.getStringValueinArray(OR, "PerfSummary_RecNextStep_3", "Value"));

		   }else{
			   APPLICATION_LOGS.debug("Error in loading MCAT All resources page");
			   result="fail";
			   fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+"AllResources.jpg";
			   TestUtil.takeScreenShot(screenshotPath+fileName);
			   ReportUtil.addStep(sheetName+" Exam", "Error loading page", result,screenshotPath+fileName);
			}	
		
		return classResult;
		} catch(Throwable t){
			// error
			APPLICATION_LOGS.debug("Error in MCATTest");
			ReportUtil.addStep("Verify Top Opportunities Test ", "Not Successful", "Fail", null);
			classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
			
			return classResult;
		}
	}
	
}

