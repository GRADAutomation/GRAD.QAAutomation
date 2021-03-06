/* This class file is for testing Other links/sections under Additional Resources page of SHP
 */

package SHP;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;

import Database.Excel_Ops;
import ParentClasses.DriverScript;
import Reports.ReportUtil;
import Utility.Keywords;
import Utility.TestUtil;
import Utility.Variable_Conversions;

public class SHPAddtlResOthers extends DriverScript{
	
	// All initialization
	public static Excel_Ops d = null;
	
	//This method will be called by Driver Script parent class file using Java reflection, so the method name should be constant
	@Test
	public static String completeFlowTest(String sheetName) throws IOException, InterruptedException {

		//Initializing
		Keywords.dualOutput("Inside SHP CompleteflowTest" + sheetName, null);
		classResult = "Pass"; String result = null; 
		d= new Excel_Ops(System.getProperty("user.dir")+"/src/Config/"+currentDataXL);
		
		//Start the test by logging into SHP
		if (TestUtil.shpLogin().equals("Pass")) {
			
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);		
			Keywords.dualOutput("Login successful through Student Home Page", null);
			
			//Clicking on the Course
			Keywords.clickLinkText(currentTestName.trim());	
			Keywords.dualOutput("SHP Course Syllabus Page is launched", null);

			mainMethod();
		   }else{
			   Keywords.dualOutput("Error in logging in through Student Home Page", null);
			   result="fail";
			   fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+"AllResources.jpg";
			   TestUtil.takeScreenShot(screenshotPath+fileName);
			   ReportUtil.addStep( "SHP Course Syllabus"+sheetName+" ", "Error loading page", result,screenshotPath+fileName);
		   }
		return classResult;
	} // end of completeFlowTest
	
	//Method to spin through pages and perform different actions
	public static String mainMethod() throws IOException, InterruptedException{
		methodResult = "Pass";
		System.out.println("Inside main method");
		
		driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);		
	
	    //Verify contents of SHP header 
		TestUtil.VerifySHPHeader();
		
		//Verify shp footer
		TestUtil.VerifySHPFooter();
	
		String conPath;
		Keywords.clickLink("SHP_Syllabus_Add_Res_Link");
		Thread.sleep(500L);
		
		int count=1; int rowCount = 2;
		do{				
			do {				
				conPath = TestUtil.getStringValueinArray(OR, "SHP_Syllabus_AddRes_Con_Start", "Key") + (count) + TestUtil.getStringValueinArray(OR, "SHP_Syllabus_AddRes_Con_End", "Key");
				Thread.sleep(500L);
				
				String winHandleBefore = driver.getWindowHandle();				
				Keywords.clickbyXpath(conPath);
								
				//handling multiple windows
				for(String winHandleNew : driver.getWindowHandles()){
					driver.switchTo().window(winHandleNew);			
				}
				Thread.sleep(5000L);
			
				//Closing new window
				driver.close();
				//Switching control back to main window
				driver.switchTo().window(winHandleBefore);
				rowCount++; 
			} while (d.getCellData(currentDatasheet, "AVAILABLE", rowCount).equals("Y"));
			count++;
		}while(d.getCellData(currentDatasheet, "TITLE", rowCount-1).equals(d.getCellData(currentDatasheet, "TITLE", rowCount)));
			
		
		//reporting
		if(methodResult.equals("Pass"))
			ReportUtil.addStep("Verify contents of Course Syllabus for "+currentTestName, "All contents are verified", "Pass", null);
		else
			ReportUtil.addStep("Verify contents of Course Syllabus for "+currentTestName, "Contents are verified but something went wrong", "Fail", null);
		
		return methodResult;
	} // end of method
	
}
