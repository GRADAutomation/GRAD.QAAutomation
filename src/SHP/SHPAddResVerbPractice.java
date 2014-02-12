/* This class file is for Test Suite 1 package Testcases : MCAT Diagnostic, Full length, Topical and other tests.
 * This is framework driven class file.  
 * Developed by Siva Vanapalli
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

public class SHPAddResVerbPractice extends DriverScript{
	
	// All initialization
	public static int currentSection; 
	public static int noofSections;
	public static Excel_Ops d = null;
	public static Utility.Variable_Conversions vc = null;
	public static boolean rwPgVerification = false; // Sets to true during review mode validations
	public static boolean completeRegression = false; // Sets to true for complete regression testing option chosen by user (in ControllerNew.xlsx)
	public static boolean  getQAText = false; // Sets to true to get text of question & answers when the option chosen by user (in ControllerNew.xlsx)
	

	//This method will be called by Driver Script parent class file using Java reflection, so the method name should be constant
	@Test
	public static String completeFlowTest(String sheetName) throws IOException, InterruptedException {

		//Initializing
		APPLICATION_LOGS.debug("Inside SHP CompleteflowTest" + sheetName);
		classResult = "Pass"; String result = null; rwPgVerification = false; getQAText = false;
		
		//Since the code is common for Complete regression, 
		//get QA Text and general flow regression, the following initialization is done
		//to ensure right test will be carried out
		if (currentCaseName.equals("Complete_Regression"))
			completeRegression = true;
		if (currentCaseName.equals("getQAText"))
			getQAText = true;
		
		//Initialize excel instance and variable conversion instance
		d= new Excel_Ops(System.getProperty("user.dir")+"/src/Config/"+currentDataXL+".xlsx");
		vc = new Variable_Conversions();
		
		//Start the test by logging into SHP
		if (TestUtil.shpLogin().equals("Pass")) {
			
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);		
			APPLICATION_LOGS.debug("Login successful through Student Home Page");
			
			//Clicking on the Course
			Keywords.clickLinkText(currentMainTopic.trim());	
			APPLICATION_LOGS.debug("SHP Course Syllabus Page is launched");
			
			if(completeRegression || getQAText)
				mainMethod();
			
		   }else{
			   APPLICATION_LOGS.debug("Error in logging in through Student Home Page");
			   result="fail";
			   fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+"AllResources.jpg";
			   TestUtil.takeScreenShot(screenshotPath+fileName);
			   ReportUtil.addStep( "SHP Course Syllabus"+sheetName+" ", "Error loading page", result,screenshotPath+fileName);
		   }
		return classResult;
	} // end of function
	
	//Method to spin through pages and perform different actions
	public static String mainMethod() throws IOException, InterruptedException{
		methodResult = "Pass";
		System.out.println("Inside main method");
		
		driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);		
	
	    //Verify contents of SHP header 
		TestUtil.VerifySHPHeader();
		
		//Verify shp footer
		TestUtil.VerifySHPFooter();
		
		//Initializations
		String titlePath; 
	
		Keywords.clickLink("SHP_Syllabus_Add_Res_Link");
		
		
		Thread.sleep(500L);
		String winHandleBefore = driver.getWindowHandle();	
		
		int count=1; int rowCount = 2; int conCount = 1;
		
		if (currentTestName.equals("Math Refresher Practice"))
			Keywords.clickLink("SHP_AddRes_MathPractice");
		else if (currentTestName.equals("Verbal Edge Practice"))
			Keywords.clickLink("SHP_AddRes_VerbPractice");
		else if (currentTestName.equals("Foundation Review"))
			Keywords.clickLink("SHP_AddRes_FoundRev");
		
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
			do {
				
				titlePath = TestUtil.getStringValueinArray(OR, "SHP_VerbPrac_Title_Start", "Key") + conCount + TestUtil.getStringValueinArray(OR, "SHP_VerbPrac_Title_Mid", "Key") + count + TestUtil.getStringValueinArray(OR, "SHP_VerbPrac_Title_End", "Key");
				Thread.sleep(500L);
				if (getQAText){
					d.setCellData(currentDatasheet, "TITLE", rowCount, Keywords.getCustomObjectText(titlePath));
					d.setCellData(currentDatasheet, "URL", rowCount, Keywords.getCustomAttribute(titlePath, "href"));
				}
				
				if (completeRegression){
					Keywords.verifyText(d.getCellData(currentDatasheet, "TITLE", rowCount), Keywords.getCustomObjectText(titlePath));
					if(keywordResult.equals("Pass"))
						ReportUtil.addStep("Verify Title "+d.getCellData(currentDatasheet, "TITLE", rowCount), "Title verified", "Pass", null);
					else
						ReportUtil.addStep("Verify Title "+d.getCellData(currentDatasheet, "TITLE", rowCount), "Title incorrect", "Fail", null);
				}
					
				String winHandleNewBefore = driver.getWindowHandle();				

				Keywords.clickbyXpath(titlePath);
				for(String winHandleNewLatest : driver.getWindowHandles()){
					driver.switchTo().window(winHandleNewLatest);			
				}
				
				Keywords.verifyText(d.getCellData(currentDatasheet, "URL", rowCount), driver.getCurrentUrl());
				if(keywordResult.equals("Pass"))
					ReportUtil.addStep("Verify URL of the content "+d.getCellData(currentDatasheet, "TITLE", rowCount), "URL verified", "Pass", null);
				else
					ReportUtil.addStep("Verify URL of the content "+d.getCellData(currentDatasheet, "TITLE", rowCount), "URL is not correct", "Fail", null);
	
				Thread.sleep(5000L);
			
				//Closing new window
				driver.close();
				//Switching control back to main window
				driver.switchTo().window(winHandleNewBefore);
				rowCount++; count++;
			} while (d.getCellData(currentDatasheet, "CONTENT", rowCount-1).equals(d.getCellData(currentDatasheet, "CONTENT", rowCount)));
			conCount++; count = 1;
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
	} // end of function
	
}