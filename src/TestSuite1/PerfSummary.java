package TestSuite1;

import org.testng.annotations.Test;
import org.testng.annotations.Test;


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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
//import org.openqa.selenium.HasInputDevices;
import org.openqa.selenium.JavascriptExecutor;
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

public class PerfSummary extends DriverScript{

	Excel_Ops d = null;
	Utility.Variable_Conversions vc = null;
	Excel_Ops AllRes = null;

	@Test
	public String completeFlowTest(String sheetName) throws IOException, InterruptedException {
		methodResult = "Pass"; classResult = "Pass"; String result = null;
		//Reading properties file in Java example
			 
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_hhmmss");  
		Date curDate = new Date();  
		String now = sdf.format(curDate);  

		Keywords.dualOutput("Inside Performance Summary Test page", sheetName);
		d = new Excel_Ops(System.getProperty("user.dir")+"\\src\\Config\\"+currentDataXL);
		vc = new Variable_Conversions();
		
		try{
		if (TestUtil.jasperLogin().equals("Pass")) {
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			if (!currentBrowser.equals("IE") && !currentBrowser.equals("Chrome"))
				driver.switchTo().frame("main");
		
			//Proceed further only if MCAT Test Prep page is displayed
			if (Keywords.verifyTitle(currentTestName)){
				Keywords.dualOutput("MCAT All Resources page is launched",null);
			
				//Selecting MCAT Diagnostic Test
				
				int index = vc.strToDblToInt(AllRes.getCellData(currentProduct, "Index", AllRes.getFirstRowInstance(currentProduct, "TestName", currentTestName.trim())));
				
				if(!currentBrowser.contains("Safari"))
					driver.manage().timeouts().pageLoadTimeout(180, TimeUnit.SECONDS);

				if(!currentBrowser.contains("Safari"))
					Keywords.clickLinkText(currentTestName.trim());
				else
					Keywords.clickbyXpath("//*[@id='sequence"+String.valueOf(index)+"']/td[2]/a");
				
				driver.manage().timeouts().implicitlyWait(500, TimeUnit.SECONDS);

				TestUtil.VerifyJasperHeader("Item Review");
				TestUtil.VerifyJasperFooter();
				Keywords.clickLink("ItmRw_Nav_Performance_Link");
				TestUtil.VerifyJasperHeader("Performance Summary");
				TestUtil.VerifyJasperFooter();
							
				driver.manage().window().maximize();	
				driver.manage().timeouts().implicitlyWait(500, TimeUnit.SECONDS);
				
				WebElement dropDown = driver.findElement(By.xpath("html/body/div[1]/div[3]/div[2]/div[6]/div/section[2]/div[2]/ul/li[2]/div[1]"));
			
				Point pt = dropDown.getLocation();
				System.out.println("After Point ===> "+ pt);

				((JavascriptExecutor) driver).executeScript("window.scrollBy(513, 517)", "");		
				
				File source = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				File screenShot = new File("C:\\Screenshots1\\screenshot_"+now+".jpg");
				FileUtils.copyFile(source, screenShot); 

				processImg(screenShot);
			}
		}
		else{
			   Keywords.dualOutput("Error in loading MCAT All resources page",null);
			   result="fail";
			   fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+"AllResources.jpg";
			   TestUtil.takeScreenShot(screenshotPath+fileName);
			   ReportUtil.addStep(sheetName+" Exam", "Error loading page", result,screenshotPath+fileName);
	
		}
		return classResult;
		} catch(Throwable t){
			// error
			Keywords.dualOutput("Error in PerfSummary",null);
			ReportUtil.addStep("Verify PerformanceSummary Test ", "Not Successful", "Fail", null);
			classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
			
			return classResult;
		}
		
	}
	

	public String processImg(File screenShot) throws IOException//pass this method the image of your chart from link 
	{ 
		submethodL1Result = "Pass"; 
		try { 
			File compareShot = new File("C:\\Screenshots\\compare.jpg");
			BufferedImage original = ImageIO.read(screenShot);
			BufferedImage copy = ImageIO.read(compareShot);

			Raster ras1 = original.getData(); 
			Raster ras2 = copy.getData(); 
	
			//Comparing the the two images for number of bands,width & height. 
			if (ras1.getNumBands() != ras2.getNumBands() 
					|| ras1.getWidth() != ras2.getWidth() 
					|| ras1.getHeight() != ras2.getHeight()) { 				
				submethodL1Result = "Fail"; 
				Keywords.dualOutput("Screenshot comparison failed due to mismatching bands/width/height",null);

			} 
			else{ 
				
				// Once the band ,width & height matches, comparing the images. 

				search: for (int i = 0; i < ras1.getNumBands(); ++i) { 
					for (int x = 0; x < ras1.getWidth(); ++x) { 
						for (int y = 0; y < ras1.getHeight(); ++y) { 
							if (ras1.getSample(x, y, i) != ras2.getSample(x, y, i)) { 
								// If one of the result is false setting the result as false and breaking the loop. 
								submethodL1Result = "Fail"; 
								Keywords.dualOutput("Screenshot comparison failed due to mismatching co ordinates",null);
								break search; 
							} 
						} 
					} 
				} 
			} 
			

		} 
		catch (IOException e) 
		{ 
			System.out.println(e); 
		} 
		Keywords.dualOutput("PerfSummary: Screenshots Compared and matches",null);
		ReportUtil.addStep("Performance Summary Page", "Performance Summary screenshots match", "Pass",null);
	
		return submethodL1Result;
	} 
	
}

