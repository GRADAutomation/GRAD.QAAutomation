/* This class file is for Test Suite 1 package Testcases : MCAT Diagnostic, Full length, Topical and other tests.
 * This is framework driven class file.  
 * Developed by Siva Vanapalli
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

public class SHPCourseSyllabus extends DriverScript{
	
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
		d= new Excel_Ops(System.getProperty("user.dir")+"/src/Config/"+currentDataXL);
		vc = new Variable_Conversions();
		
		//Start the test by logging into SHP
		if (TestUtil.shpLogin().equals("Pass")) {
			
			driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);		
			APPLICATION_LOGS.debug("Login successful through Student Home Page");
			
			//Clicking on the Course
			Keywords.clickLinkText(currentTestName.trim());	
			APPLICATION_LOGS.debug("SHP Course Syllabus Page is launched");

			//mainmethod is for 
				if(completeRegression)
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
		
		driver.manage().timeouts().implicitlyWait(300, TimeUnit.SECONDS);		

		String today = "";
	    Date date = new Date();
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);

	    
	    Date before = cal.getTime();
	    SimpleDateFormat formatter = new SimpleDateFormat("dd");
	    today = formatter.format(date);
		
	    System.out.println("today --->" + today);
	    
		driver.manage().timeouts().implicitlyWait(300, TimeUnit.SECONDS);		

	    //Verify contents of SHP header 
		TestUtil.VerifySHPHeader();
		
		//Verify shp footer
		TestUtil.VerifySHPFooter();
		
		//Keywords.clickbyXpath("SHP_Syllabus_Link");
		
		Thread.sleep(5000L);
		
		//Initializations
		int rowNum = 2; int titleCount = 1; 
		String titlePath; String contentPath; String toolTip;String iconPath; String statusPath;String calPath;
		do{
			System.out.println("Inside first do method");
			
			driver.manage().timeouts().implicitlyWait(300, TimeUnit.SECONDS);		
			
			//Clicking on each Topic Title
			
			titlePath = TestUtil.getStringValueinArray(OR,"SHP_Syllabus_Title_Start","Key")+ titleCount + TestUtil.getStringValueinArray(OR,"SHP_Syllabus_Title_End","Key");
			Keywords.clickbyXpath(titlePath);
			APPLICATION_LOGS.debug("Navigating through each Topic");

			int contentCount =1;
			
			//navigating through each content based on the classification (ORGANIZATION in xls)
			do {
				System.out.println("Inside second do method");
				int td =2; int colCount = 3; 
				
				//Navigating through each content
				do{
					System.out.println("Inside third do method "+ td);
					
					//handling the static contents
					if (d.getCellData(currentDatasheet, "TYPE", rowNum).equals("Static")){
						contentPath = TestUtil.getStringValueinArray(OR,"SHP_Syllabus_Content_Start","Key")+ titleCount + TestUtil.getStringValueinArray(OR,"SHP_Syllabus_Content_Mid1","Key")+ contentCount + TestUtil.getStringValueinArray(OR,"SHP_Syllabus_Content_Mid2","Key") + td + TestUtil.getStringValueinArray(OR,"SHP_Syllabus_Content_End1","Key");
						//verifying the content with the input xls
						driver.manage().timeouts().implicitlyWait(300, TimeUnit.SECONDS);		

						Keywords.verifyCustomObjectText(contentPath, d.getCellData(currentDatasheet, "ASSIGNMENT_NAME", rowNum));					
					}
					else if (d.getCellData(currentDatasheet, "TYPE", rowNum).equals("Ignore")){
						//continue;
						
						System.out.println("I am here in ignore");
					}
					//handling non static links (videos and pdfs)
					else {
						contentPath = TestUtil.getStringValueinArray(OR,"SHP_Syllabus_Content_Start","Key")+ titleCount + TestUtil.getStringValueinArray(OR,"SHP_Syllabus_Content_Mid1","Key")+ contentCount + TestUtil.getStringValueinArray(OR,"SHP_Syllabus_Content_Mid2","Key") + td + TestUtil.getStringValueinArray(OR,"SHP_Syllabus_Content_End1","Key") + TestUtil.getStringValueinArray(OR,"SHP_Syllabus_Content_End2","Key");
						driver.manage().timeouts().implicitlyWait(300, TimeUnit.SECONDS);		

						//Keywords.verifyCustomObjectText(contentPath, d.getCellData(currentDatasheet, "ASSIGNMENT_NAME", rowNum));
						
						//Clicking on each non static link and navigating to the respective page
						APPLICATION_LOGS.debug("Navigating to the link "  + d.getCellData(currentDatasheet, "ASSIGNMENT_NAME", rowNum));
						String winHandleBefore = driver.getWindowHandle();
					
						Keywords.clickbyXpath(contentPath);
	
						//handling multiple windows
						for(String winHandleNew : driver.getWindowHandles()){
							driver.switchTo().window(winHandleNew);
						}
						
						Thread.sleep(3000L);

						if (d.getCellData(currentDatasheet, "TYPE", rowNum).equals("Jasper")){
							//verifying the title on each page
							if (driver.getPageSource().contains(d.getCellData(currentDatasheet, "ASSIGNMENT_NAME", rowNum))){
								  APPLICATION_LOGS.debug("navigated to the correct page " + d.getCellData(currentDatasheet, "ASSIGNMENT_NAME", rowNum));

							}else{
								APPLICATION_LOGS.debug("Wrong page is opened"  + d.getCellData(currentDatasheet, "ASSIGNMENT_NAME", rowNum));
								fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+"AllResources.jpg";
								TestUtil.takeScreenShot(screenshotPath+fileName);
								ReportUtil.addStep("SHP Course Syllabus "+currentTestName+" ", "Error navigating to the link "+ d.getCellData(currentDatasheet, "ASSIGNMENT_NAME", rowNum), "Fail",screenshotPath+fileName);
							}
							
				
						}
					
						//Closing new window
						driver.close();
						//Switching control back to main window
						driver.switchTo().window(winHandleBefore);
					}
						
					
					//Validating the status
				/*	statusPath = TestUtil.getStringValueinArray(OR, "SHP_Syllabus_Status_Start", "Key") + contentCount + TestUtil.getStringValueinArray(OR, "SHP_Syllabus_Status_Mid", "Key") + (colCount+1) + TestUtil.getStringValueinArray(OR, "SHP_Syllabus_Status_End", "Key");
					if (getQAText){
						if (Keywords.getCustomAttribute(statusPath, "class").equals("status status-red"))
							d.setCellData(currentDatasheet, "STATUS", rowNum, "Not Complete");
						else
							d.setCellData(currentDatasheet, "STATUS", rowNum, "Complete");
					}
					
					if (completeRegression){
						if (Keywords.getCustomAttribute(statusPath, "class").equals("status status-red"))
							Keywords.verifyText(d.getCellData(currentDatasheet, "STATUS", rowNum), "Not Complete");
						else
							Keywords.verifyText(d.getCellData(currentDatasheet, "STATUS", rowNum), "Complete");*/

					//}
					td=1;contentCount++; rowNum++; colCount =2;

				}while(d.getCellData(currentDatasheet, "ORGANIZATION", rowNum-1).equals(d.getCellData(currentDatasheet, "ORGANIZATION", rowNum)));
			}while(d.getCellData(currentDatasheet, "SESSION_NAME", rowNum-1).equals(d.getCellData(currentDatasheet, "SESSION_NAME", rowNum)));
			
			titleCount++;
		}while(d.getCellData(currentDatasheet, "COURSE_NAME", rowNum-1).equals(d.getCellData(currentDatasheet, "COURSE_NAME", rowNum)));

		
		//reporting
		if(methodResult.equals("Pass"))
			ReportUtil.addStep("Verify contents of Course Syllabus for "+currentTestName, "All contents are verified", "Pass", null);
		else
			ReportUtil.addStep("Verify contents of Course Syllabus for "+currentTestName, "Contents are verified but something went wrong", "Fail", null);
		
		return methodResult;
	} // end of function
	
}
