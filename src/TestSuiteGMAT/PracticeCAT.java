/* This class file is for Test Suite 1 package Testcases : MCAT Diagnostic, Full length, Topical and other tests.
 * This is framework driven class file.  
 * Developed by Siva Vanapalli
 */

package TestSuiteGMAT;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
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

public class PracticeCAT extends DriverScript{
	
	// All initialization
	public static int currentSection; 
	public static int noofSections;
	public static Excel_Ops d = null;
	public static Excel_Ops AllRes = null;
	public static Utility.Variable_Conversions vc = null;
	public static boolean rwPgVerification = false; // Sets to true during review mode validations
	public static boolean completeRegression = false; // Sets to true for complete regression testing option chosen by user (in ControllerNew.xlsx)
	public static boolean  getQAText = false; // Sets to true to get text of question & answers when the option chosen by user (in ControllerNew.xlsx)
	public static boolean isDiagORFL = false;
	
	//This method will be called by Driver Script parent class file using Java reflection, so the method name should be constant
	@Test
	public static String completeFlowTest(String sheetName) throws IOException, InterruptedException {

		//Initializing
		APPLICATION_LOGS.debug("Inside Suite 1 CompleteflowTest" + sheetName);
		classResult = "Pass"; String result = null; rwPgVerification = false; getQAText = false;
		
		//Since the code is common for Complete regression, get QA Text and general flow regression, the following initialization is done
		//to ensure right test will be carried out
		if (currentCaseName.equals("Complete_Regression"))
			completeRegression = true;
		if (currentCaseName.equals("getQAText"))
			getQAText = true;
		
		//Initialize excel instance and variable conversion instance
		d= new Excel_Ops(System.getProperty("user.dir")+"\\src\\Config\\"+currentDataXL);
		AllRes= new Excel_Ops(System.getProperty("user.dir")+"\\src\\Config\\AllRes.xlsx");
		
		vc = new Variable_Conversions();

		try {
		//Start the test by logging into Jasper
		if (TestUtil.jasperLogin().equals("Pass")) {
			
			if(!currentBrowser.contains("Safari"))
				driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			else
				Thread.sleep(12000L);
			if (!currentBrowser.equals("IE") && !currentBrowser.equals("Chrome"))
				driver.switchTo().frame("main");
		
			//Proceed further only if MCAT Test Prep page is displayed
			if (Keywords.verifyTitle(currentTestName)){
				APPLICATION_LOGS.debug("MCAT All Resources page is launched");
			
				//Selecting MCAT Diagnostic Test
				
				int index = vc.strToDblToInt(AllRes.getCellData(currentProduct, "Index", AllRes.getFirstRowInstance(currentProduct, "TestName", currentTestName.trim())));
				
				if(!currentBrowser.contains("Safari"))
					driver.manage().timeouts().pageLoadTimeout(180, TimeUnit.SECONDS);

				if(!currentBrowser.contains("Safari"))
					Keywords.clickLinkText(currentTestName.trim());
				else
					Keywords.clickbyXpath("//*[@id='sequence"+String.valueOf(index)+"']/td[2]/a");
				
				//mainmethod is for 
				if(!getQAText)
					mainMethod(index);
			//	ReviewPg rw = new ReviewPg();
			//	rw.completeFlowTest(currentDatasheet);	
				
				//Once review page validations are completed, bring the control back to this class
				if(completeRegression || getQAText){
					//click on first tab to bring control back
					if(isDiagORFL || currentDatasheet.contains("SciAss"))
						Keywords.clickbyXpath(TestUtil.getStringValueinArray(OR,"ItmRw_Sectiontab_Start","Key")+ d.getCellData(currentDatasheet, "Section", 2)+TestUtil.getStringValueinArray(OR,"ItmRw_Sectiontab_End","Key"));
					if(Keywords.clickLink("ItmRw_QueTopic_First_"+currentTestSuite).equals("Pass")){
						rwPgVerification = true;
						completeRegression = false;
						mainMethod(index);
					}
				}
		   }else{
			   APPLICATION_LOGS.debug("Error in loading MCAT All resources page");
			   result="fail";
			   fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+"AllResources.jpg";
			   TestUtil.takeScreenShot(fileName);
			   ReportUtil.addStep(sheetName+" Exam", "Error loading page", result,screenshotPath+fileName);
		   }
		
		} // end of if loop of Login
		return classResult;
		//driver.quit();
		} catch(Throwable t){
			// error
			APPLICATION_LOGS.debug("Error in MCATTest");
			ReportUtil.addStep("Verify MCATTest ", "Not Successful", "Fail", null);
			classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
			
			return classResult;
		}
		
	} // end of function
	
	//Method to spin through pages and perform different actions
	public static String mainMethod(int index) throws IOException, InterruptedException{
		methodResult = "Pass";

		//Validate the details on Start Test Page
		TestUtil.VerifyJasperStartTestPg(currentTestName);
		Keywords.clickButton("JspStart_Start_Button");
		Thread.sleep(4000L);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		
		//click Next on 8 pages initially and then Dismiss button on 9th page
		for (int i=1; i<=8; i++){
			//driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			Thread.sleep(1000L);
			if(Keywords.elementDisplayed("TstPg_Footer_Next_Button_GMAT_Img")){
				System.out.println("Inside IF part");
				Keywords.clickButton("TstPg_Footer_Next_Button_GMAT_Img");
			}else {
				System.out.println("Inside ELSE part");
				Keywords.clickButton("TstPg_Footer_Next_Button_GMAT");
			}
		}		
		Keywords.clickButton("GMAT_SecBeg_Body_Dismiss_Button");
		
		//Analytical Section
		Keywords.input("GMAT_Anal_Input");
		//Thread.sleep(2000L);
		Keywords.clickButton("TstPg_Footer_Next_Button_GMAT");
		
		//IR Section
		Keywords.clickButton("TstPg_Footer_Next_Button_GMAT_Img");
		Keywords.clickButton("GMAT_SecBeg_Body_Dismiss_Button");
		int turnMultipleYesNo = 1; String previousIdentifier = null; String currentIdentifier = null;
		
		for (int i =0; i<12; i++) {
			//driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			Thread.sleep(1000L);
			Keywords.frameSwitch("defaultContent");
			Keywords.frameSwitch("testMode");
			Keywords.frameSwitch("sequenceContentFrame");
		if(Keywords.pageSourceContains("twoPartAnalysis")){
			System.out.println("twoPartAnalysis"); currentIdentifier = "twoPartAnalysis";
			Keywords.clickRadioButton("GMAT_IR_TwoPartAnal_Rbutton_Q1");
			Keywords.clickRadioButton("GMAT_IR_TwoPartAnal_Rbutton_Q2");
		} else if(Keywords.pageSourceContains("multipleYesNoQuestion")){
			System.out.println("multipleYesNoQuestion"); currentIdentifier = "multipleYesNoQuestion";
			if(previousIdentifier != null)
				if(!previousIdentifier.equals("multipleYesNoQuestion"))
					turnMultipleYesNo = 1; // subitem is resetting to 1 for future multipleYesNoQuestion types
			Keywords.clickbyXpath("//*[@id='subitem"+turnMultipleYesNo+".multipleYesNoQuestion.questionRow1.ans1.state1']");
			Keywords.clickbyXpath("//*[@id='subitem"+turnMultipleYesNo+".multipleYesNoQuestion.questionRow2.ans1.state1']");
			Keywords.clickbyXpath("//*[@id='subitem"+turnMultipleYesNo+".multipleYesNoQuestion.questionRow3.ans1.state1']");
			turnMultipleYesNo++;
		} else if(Keywords.pageSourceContains("multipleAnswerPickList")){
			System.out.println("multipleAnswerPickList"); currentIdentifier = "multipleAnswerPickList";
			//Keywords.clickRadioButton("GMAT_IR_multipleAnswerPickList_List_Q1");
			Keywords.selectfromSelectListByIndex("GMAT_IR_multipleAnswerPickList_List_Q1",2);
			//Keywords.clickRadioButton("GMAT_IR_multipleAnswerPickList_List_Q2");
			Keywords.selectfromSelectListByIndex("GMAT_IR_multipleAnswerPickList_List_Q2",2);
		} else if(Keywords.pageSourceContains("singleAnswerMultipleChoice")){
			System.out.println("singleAnswerMultipleChoice"); currentIdentifier = "singleAnswerMultipleChoice";
			Keywords.clickbyXpath("//*[@id='subitem"+turnMultipleYesNo+".singleAnswerMultipleChoice.answerChoiceRow1.state1']");
			turnMultipleYesNo++;
		}
		if (currentIdentifier != null)
			previousIdentifier = currentIdentifier; 
		currentIdentifier = null;
		
		Keywords.clickButton("TstPg_Footer_Next_Button_GMAT_IR");
		Keywords.acceptAlert();
		}
		
		//Quant Section
		Keywords.clickButton("GMAT_SecBeg_Body_NextSec_Button");
		Keywords.clickButton("GMAT_SecBeg_Body_Dismiss_Quant_Button");
		String correctAns = null; String correctCategory = null; 
		int categoryCounter = 0;
		
		for (int i =0; i<37; i++) {
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			if (i == 0)
				Thread.sleep(2000L);
			Keywords.clickLink("GMAT_MainTestPg_Debug_Link");
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			String winHandleBefore = driver.getWindowHandle();
			
			//Switch to New Window
			for(String winHandleNew : driver.getWindowHandles()){
				driver.switchTo().window(winHandleNew);
			}
			
			//Retrieve correct Answer & Category
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			correctCategory = Keywords.getCustomObjectText("html/body/table/tbody/tr["+(i+2)+"]/td[6]");
			correctAns = Keywords.getCustomObjectText("html/body/table/tbody/tr["+(i+2)+"]/td[8]");
			
			if (!correctCategory.contains("set")) { //some questions are set based which start with subitem1
				System.out.println("Insde set -- CorrectCat is: "+correctCategory+"   CategoryC is  :"+categoryCounter);
				categoryCounter=0;
			}			
			
				//Close the window and switch focus to old window
				driver.close();
				driver.switchTo().window(winHandleBefore);
				if(correctCategory.contains("set")){
					Keywords.clickCheckBoxCustom2("id", "subitem"+(categoryCounter+1)+".singleAnswerMultipleChoice.answerChoiceRow"+correctAns+".state1", "testMode", "sequenceContentFrame", "");
					categoryCounter++;
				} else
					Keywords.clickCheckBoxCustom2("id", "subitem0.singleAnswerMultipleChoice.answerChoiceRow"+correctAns+".state1", "testMode", "sequenceContentFrame", "");
				Keywords.clickButton("TstPg_Footer_Next_Button_GMAT_Quant");
				Keywords.acceptAlert();					
		}
		
		
		//Verbal Section
		Keywords.clickButton("GMAT_SecBeg_Body_NextSec_Button");
		Keywords.clickButton("GMAT_SecBeg_Body_Dismiss_Button");
		correctAns = null; categoryCounter = 0;
		
		for (int i =0; i<41; i++) {
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			if (i == 0)
				Thread.sleep(2000L);
			Keywords.clickLink("GMAT_MainTestPg_Debug_Link");
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			String winHandleBefore = driver.getWindowHandle();
			//Switch to New Window
			for(String winHandleNew : driver.getWindowHandles()){
				driver.switchTo().window(winHandleNew);
			}
				//Retrieve correct Answer & Category
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
				correctCategory = Keywords.getCustomObjectText("html/body/table/tbody/tr["+(i+2)+"]/td[6]");
				correctAns = Keywords.getCustomObjectText("html/body/table/tbody/tr["+(i+2)+"]/td[8]");

				if (!correctCategory.contains("set")) { //some questions are set based which start with subitem1
					System.out.println("Insde set -- CorrectCat is: "+correctCategory+"   CategoryC is  :"+categoryCounter);
					categoryCounter=0;
				}
				
				//Close the window and switch focus to old window
				driver.close();
				driver.switchTo().window(winHandleBefore);
				
				if(correctCategory.contains("set")){
					Keywords.clickCheckBoxCustom2("id", "subitem"+(categoryCounter+1)+".singleAnswerMultipleChoice.answerChoiceRow"+correctAns+".state1", "testMode", "sequenceContentFrame", "");
					categoryCounter++;
				} else
					Keywords.clickCheckBoxCustom2("id", "subitem0.singleAnswerMultipleChoice.answerChoiceRow"+correctAns+".state1", "testMode", "sequenceContentFrame", "");
				Keywords.clickButton("TstPg_Footer_Next_Button_GMAT_Quant");
				Keywords.acceptAlert();					
		}
		
		Keywords.clickButton("GMAT_SecBeg_Body_NextSec_Button");
		
		return methodResult;
	}	
	
	
	
} // end of class