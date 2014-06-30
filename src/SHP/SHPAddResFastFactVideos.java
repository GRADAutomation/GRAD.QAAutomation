/* This class file is for SHP test pages to test Fast Fact Videos under Additional Resources tab
 */

package SHP;

import org.testng.annotations.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import Database.Excel_Ops;
import ParentClasses.DriverScript;
import Reports.ReportUtil;
import Utility.Keywords;
import Utility.TestUtil;
import Utility.Variable_Conversions;

public class SHPAddResFastFactVideos extends DriverScript{
	
	// All initialization
	public static Excel_Ops d = null;
	public static boolean completeRegression = false; // Sets to true for complete regression testing option chosen by user (in ControllerNew.xlsx)
	public static boolean  getQAText = false; // Sets to true to get text of question & answers when the option chosen by user (in ControllerNew.xlsx)
	

	//This method will be called by Driver Script parent class file using Java reflection, so the method name should be constant
	@Test
	public static String completeFlowTest(String sheetName) throws IOException, InterruptedException {

		//Initializing
		APPLICATION_LOGS.debug("Inside SHP CompleteflowTest" + sheetName);
		classResult = "Pass"; String result = null; getQAText = false;
		d= new Excel_Ops(System.getProperty("user.dir")+"/src/Config/"+currentDataXL);
		
		//Since the code is common for Complete regression, getQAText and general flow regression, the following initialization is done
		//to ensure right test will be carried out
		if (currentCaseName.equals("Complete_Regression"))
			completeRegression = true;
		if (currentCaseName.equals("getQAText"))
			getQAText = true;
	
		//Start the test by logging into SHP
		if (TestUtil.shpLogin().equals("Pass")) {
			
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);		
			Keywords.dualOutput("Login successful through Student Home Page", null);
			
			//Clicking on the Course
			Keywords.clickLinkText(currentMainTopic.trim());	
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
		methodResult = "Pass"; String titlePath; int count=1; int rowCount = 2; 
		
		driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);		
	
	    //Verify contents of SHP header 
		TestUtil.VerifySHPHeader();		
		//Verify shp footer
		TestUtil.VerifySHPFooter();	
		
		Keywords.clickLink("SHP_Syllabus_Add_Res_Link");		
		Thread.sleep(500L);
		
		String winHandleBefore = driver.getWindowHandle();	
		Keywords.clickLink("SHP_AddRes_FastFactVideos");
		Thread.sleep(500L);
				
		//handling multiple windows
		for(String winHandleNew : driver.getWindowHandles()){
			driver.switchTo().window(winHandleNew);			
		}
		
		/*Keywords.verifyObjectText("SHP_AddRes_MathPractice_Header");
		if(keywordResult.equals("Pass"))
			ReportUtil.addStep("Verify Title "+TestUtil.getStringValueinArray(OR, "SHP_AddRes_MathPractice_Header", "Value"), "Title verified", "Pass", null);
		else
			ReportUtil.addStep("Verify Title "+TestUtil.getStringValueinArray(OR, "SHP_AddRes_MathPractice_Header", "Value"), "Title incorrect", "Fail", null);*/

		do{								
				titlePath = TestUtil.getStringValueinArray(OR, "SHP_FastFact_Title_Start", "Key") + count + TestUtil.getStringValueinArray(OR, "SHP_FastFact_Title_End", "Key");
				Thread.sleep(500L);
				
				if (getQAText){
					d.setCellData(currentDatasheet, "TITLE", rowCount, Keywords.getCustomObjectText(titlePath));
				}
				
				if (completeRegression){
					Keywords.verifyText(d.getCellData(currentDatasheet, "TITLE", rowCount), Keywords.getCustomObjectText(titlePath));
					if(keywordResult.equals("Pass"))
						ReportUtil.addStep("Verify Title "+d.getCellData(currentDatasheet, "TITLE", rowCount), "Title verified", "Pass", null);
					else
						ReportUtil.addStep("Verify Title "+d.getCellData(currentDatasheet, "TITLE", rowCount), "Title incorrect", "Fail", null);
				}
			
				Keywords.clickbyXpath(titlePath);
				
				if (getQAText)
					d.setCellData(currentDatasheet, "URL", rowCount, driver.getCurrentUrl());

				if (completeRegression){

					Keywords.verifyText(d.getCellData(currentDatasheet, "URL", rowCount), driver.getCurrentUrl());
					if(keywordResult.equals("Pass"))
						ReportUtil.addStep("Verify URL of the content "+d.getCellData(currentDatasheet, "TITLE", rowCount), "URL verified", "Pass", null);
					else
						ReportUtil.addStep("Verify URL of the content "+d.getCellData(currentDatasheet, "TITLE", rowCount), "URL is not correct", "Fail", null);
				}
				
				Thread.sleep(5000L);
			
				driver.navigate().back(); 

				rowCount++; count++;
		}while(d.getCellData(currentDatasheet, "TOPIC", rowCount-1).equals(d.getCellData(currentDatasheet, "TOPIC", rowCount)));
		//Closing new window
		driver.close();
		
		//Switching control back to main window
		driver.switchTo().window(winHandleBefore);
		
		//reporting
		if(methodResult.equals("Pass"))
			ReportUtil.addStep("Verify contents of Course Syllabus for "+currentTestName, "All contents are verified", "Pass", null);
		else
			ReportUtil.addStep("Verify contents of Course Syllabus for "+currentTestName, "Contents are verified but something went wrong", "Fail", null);
		
		return methodResult;
	} // end of method
	
}