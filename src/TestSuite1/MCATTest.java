/* This class file is for Test Suite 1 package Testcases : MCAT Diagnostic, Full length, Topical and other tests.
 * This is framework driven class file.  
 * Developed by Siva Vanapalli
 */

package TestSuite1;

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

public class MCATTest extends DriverScript{
	
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
	public static boolean trialSec = false;
	
	//This method will be called by Driver Script parent class file using Java reflection, so the method name should be constant
	@Test
	public static String completeFlowTest(String sheetName) throws IOException, InterruptedException {

		//Initializing
		Keywords.dualOutput("Inside Suite 1 CompleteflowTest",sheetName);
		classResult = "Pass"; String result = null; rwPgVerification = false; getQAText = false;
		
		//Since the code is common for Complete regression, get QA Text and general flow regression, the following initialization is done
		//to ensure right test will be carried out
		if (currentCaseName.equals("Complete_Regression"))
			completeRegression = true;
		if (currentCaseName.equals("getQAText"))
			getQAText = true;
		isDiagORFL = TestUtil.isDiagORFL();
		
		//Initialize excel instance and variable conversion instance
		if(currentDataXL.contains("FL") || currentDataXL.contains("Diag"))
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
				Keywords.dualOutput("MCAT All Resources page is launched", null);
			
				//Selecting MCAT Diagnostic Test
				
				int index = vc.strToDblToInt(AllRes.getCellData(currentProduct, "Index", AllRes.getFirstRowInstance(currentProduct, "TestName", currentTestName.trim())));
				
				if(!currentBrowser.contains("Safari"))
					driver.manage().timeouts().pageLoadTimeout(180, TimeUnit.SECONDS);

				if(!currentBrowser.contains("Safari"))
					Keywords.clickLinkText(currentTestName.trim());
				else
					Keywords.clickbyXpath("//*[@id='sequence"+String.valueOf(index)+"']/td[2]/a");
				
				//Run mainmethod except during getText mode
				if(!getQAText)
					mainMethod(index);
				ReviewPg rw = new ReviewPg();
				rw.completeFlowTest(currentDatasheet);	
				
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
			   Keywords.dualOutput("Error in loading MCAT All resources page", null);
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
			Keywords.dualOutput("Error in MCATTest", null);
			ReportUtil.addStep("Verify MCATTest ", "Not Successful", "Fail", null);
			classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
			
			return classResult;
		}
		
	} // end of function
	
	//Method to spin through pages and perform different actions
	public static String mainMethod(int index) throws IOException, InterruptedException{
		methodResult = "Pass";
		int maxrows=d.getRowCount(currentDatasheet);		
		
		//Verify contents of Jasper Start Test Page and click Start button
		if(completeRegression)
			TestUtil.VerifyJasperStartTestPg(currentTestName);
		
		//Once Start Test Page is verified, click on Start button
		if(!rwPgVerification){
			Keywords.clickButton("JspStart_Start_Button");
			Keywords.dualOutput("Jasper Start Test Page is launched", null);
			ReportUtil.addStep("MCAT "+currentDatasheet+" Exam Launch","Launch Exam page","Pass", null);
			Thread.sleep(1000);
		}
		
		//Verify contents of Test Directions Page and click Start button - ONLY on Diag or FL
		if(isDiagORFL){
			if(completeRegression)
				TestUtil.VerifyJasperTestDirectionsPg();		

			if(!rwPgVerification)
				Keywords.clickButton("TstBeg_Footer_Next_Button");
		}
		
		noofSections = d.getNoOfMatches(currentDatasheet, "Item_Num", "1"); //Retrieving no of sections based on Item_Num = 1
		int noofRows = d.getRowCount(currentDatasheet); //Retrieving no of rows

		// Seccounter to keep track of # of sections, Row for row count and SubItemId for question # sequence in each page
		int Seccounter = 0; int Row = 2; String section = null; String psgObj = null; 
        
		do {
			
			int itemNum = 1;  //itemNum to keep of Item_Num on each sections
			
			//Verify contents of Section start page and click Start/Dismiss button to proceed
			// This is applicable only for Diag, FL and SciAss tests
			if(isDiagORFL || currentDatasheet.contains("SciAss")){
				if(completeRegression)
					TestUtil.VerifyJasperSecStartPg(Seccounter);
			
				if(!rwPgVerification)
					if(currentTestSuite.equals("Suite1") && !trialSec)
						if(currentBrowser.contains("Safari"))
							Keywords.clickButton("SecBeg_Footer_Next_Button_Safari"); 
						else
							Keywords.clickButton("SecBeg_Footer_Next_Button");
					else
						Keywords.clickButton("SecBeg_Body_Dismiss_Button");//NEED TO CHECK IF THIS WORKS FOR DIAG/FL
			}
			
  			do {
  				if (!trialSec) { //Skip Q&A for trial sec
				int SubItemId = 1; 				
				int page = vc.strToDblToInt(d.getCellData(currentDatasheet, "Page", Row));
				Thread.sleep(1500); //This is essential as otherwise script is running faster than the pageloadtime.
				
				// Suite 1: Verify CH., review & exhibit buttons, and headers
				// Suite 2: Verify CH., Back, Help & exhibit buttons, and headers
				if ((completeRegression && (itemNum == 1))|| (rwPgVerification && (page == 1))){
					TestUtil.verifyTestPageHeaders(d.getCellData(currentDatasheet, "PageTitle", Row),rwPgVerification);
					verifyTestPageFooters(Row);
				}

				//Verify Time remaining and MARK buttons
				if(completeRegression && (itemNum == 1))
					verifyAdditionalTestPageFooters();
				
				//Verify exit and feedback footer buttons
				if(rwPgVerification && (page == 1))
					verifyAdditionalREVIEWFooters(Row);
				
				//Switch to sequenceContentFrame to locate item and questions - ONLY if not in review mode, as for review mode this will be done inside next DO loop
				if(!rwPgVerification){
					//driver.switchTo().frame("sequenceContentFrame"); //driver.switchTo().activeElement();
					if(!completeRegression){
						driver.switchTo().defaultContent();
						Keywords.frameSwitch("testMode");
					}
					Keywords.frameSwitch("sequenceContentFrame");
					Keywords.frameSwitch("activeElement");
				}
				
				if(!rwPgVerification)
					Keywords.dualOutput("Answering questions in section - ", d.getCellData(currentDatasheet, "Section", Row));
				
				do {
					
					//Switch to Sequence Content Frame, only for in review mode. This is needed as we are switching to testMode frame to validate reviewID on each row
					if(rwPgVerification){
						driver.switchTo().frame("sequenceContentFrame");
						driver.switchTo().activeElement();
					}
					
					//Reading the answers from input excel
					String ansPath = null; String correctAnsPath2 = null; String passage = null; String itemPath = null; String quePath = null; String explPath = null;
					String answer = vc.strToDblToStr(d.getCellData(currentDatasheet, "InputAns", Row));
					section = d.getCellData(currentDatasheet, "Section", Row);
					passage = d.getCellData(currentDatasheet, "Passage", Row);
					String tobeMarked = d.getCellData(currentDatasheet, "Mark", Row);
					String correctAns = vc.strToDblToStr(d.getCellData(currentDatasheet, "CorrectAns", Row));
					if((currentTestSuite.equals("Suite1") || currentTestSuite.equals("Suite2")) && TestUtil.isMultiQuePsg(Row) ){
						ansPath = "subitem"+SubItemId+".singleAnswerMultipleChoice.answerChoiceRow"+answer+".state0";
						correctAnsPath2 = TestUtil.getStringValueinArray(OR,"TstPg_Body_Item_Ans_Start","Key")+SubItemId+TestUtil.getStringValueinArray(OR,"TstPg_Body_Item_Ans_Middle","Key")+correctAns+TestUtil.getStringValueinArray(OR,"TstPg_Body_Item_Ans_End","Key");
						itemPath = TestUtil.getStringValueinArray(OR,"TstPg_Body_Item_Text_Start_Suite1","Key")+SubItemId+TestUtil.getStringValueinArray(OR,"TstPg_Body_Item_Text_End","Key");
						quePath = TestUtil.getStringValueinArray(OR,"TstPg_Body_Item_Text_Start_Suite1","Key")+SubItemId+TestUtil.getStringValueinArray(OR,"TstPg_Body_Question_Text_End","Key");
						explPath = TestUtil.getStringValueinArray(OR,"TstPg_Body_explanation_Text_Start_Suite1","Key")+SubItemId+TestUtil.getStringValueinArray(OR,"TstPg_Body_explanation_Text_End","Key");
						psgObj="TstPg_Body_Passage_Text_Suite1";
					}else{
						ansPath = "subitem0"+".singleAnswerMultipleChoice.answerChoiceRow"+answer+".state0";
						correctAnsPath2 = TestUtil.getStringValueinArray(OR,"TstPg_Body_Item_Ans_Start","Key")+"0"+TestUtil.getStringValueinArray(OR,"TstPg_Body_Item_Ans_Middle","Key")+correctAns+TestUtil.getStringValueinArray(OR,"TstPg_Body_Item_Ans_End","Key");
						itemPath = TestUtil.getStringValueinArray(OR,"TstPg_Body_Item_Text_subitem0","Key");
						quePath = TestUtil.getStringValueinArray(OR,"TstPg_Body_Que_Text_subitem0","Key");
						explPath = TestUtil.getStringValueinArray(OR,"TstPg_Body_expl_Text_subitem0","Key");
						psgObj="TstPg_Body_Passage_Text_Suite2";
					}
					
					String review_ID = d.getCellData(currentDatasheet, "Review_ID", Row);
					String explAnsPath = TestUtil.getStringValueinArray(OR,"TstPg_Body_Expl_CorctAns_Text_Start","Key")+review_ID+TestUtil.getStringValueinArray(OR,"TstPg_Body_Expl_CorctAns_Expl_End","Key");
					
					Keywords.dualOutput("Completed all initialization", null);
					
					if(completeRegression)					
						Keywords.verifyCustomObjectText(itemPath, "Item "+itemNum);
					
					//Verify the question and options text
					if(completeRegression){
						Keywords.verifyCustomObjectText(quePath, d.getCellData(currentDatasheet+"_QA", "Question", Row));
						int i = 1; String optPath = null;
						while(i<=5){
							if((currentTestSuite.equals("Suite1") || currentTestSuite.equals("Suite2")) && TestUtil.isMultiQuePsg(Row) )
								optPath = TestUtil.getStringValueinArray(OR,"TstPg_Body_Item_Ans_Start","Key")+SubItemId+TestUtil.getStringValueinArray(OR,"TstPg_Body_Item_Ans_Middle","Key")+i+TestUtil.getStringValueinArray(OR,"TstPg_Body_Item_Ans_End","Key");
							else
								optPath = TestUtil.getStringValueinArray(OR,"TstPg_Body_Item_Ans_Start","Key")+"0"+TestUtil.getStringValueinArray(OR,"TstPg_Body_Item_Ans_Middle","Key")+i+TestUtil.getStringValueinArray(OR,"TstPg_Body_Item_Ans_End","Key");
							Keywords.verifyCustomObjectText(optPath, d.getCellData(currentDatasheet+"_QA", "Opt_"+i, Row));
							if (i>3 && (index < 134 || index > 143))
								i++; // this is needed as the tests from 134 to 143 under TopicalQ have 5 options
							i++;
						}
					}
					
					// During getQAText, retrieve text and updated excel
					if(getQAText){						
						d.setCellData(currentDatasheet+"_QA", "Question", Row, Keywords.getCustomObjectText(quePath));
						d.setCellData(currentDatasheet+"_QA", "Section", Row, section);
						d.setCellData(currentDatasheet+"_QA", "Item_Num", Row, String.valueOf(itemNum));
						
						int i = 1; String optPath = null;
						while(i<=5){ //4 is the number of options
							if((currentTestSuite.equals("Suite1") || currentTestSuite.equals("Suite2")) && TestUtil.isMultiQuePsg(Row) )
								optPath = TestUtil.getStringValueinArray(OR,"TstPg_Body_Item_Ans_Start","Key")+SubItemId+TestUtil.getStringValueinArray(OR,"TstPg_Body_Item_Ans_Middle","Key")+i+TestUtil.getStringValueinArray(OR,"TstPg_Body_Item_Ans_End","Key");
							else
								optPath = TestUtil.getStringValueinArray(OR,"TstPg_Body_Item_Ans_Start","Key")+"0"+TestUtil.getStringValueinArray(OR,"TstPg_Body_Item_Ans_Middle","Key")+i+TestUtil.getStringValueinArray(OR,"TstPg_Body_Item_Ans_End","Key");
						
							d.setCellData(currentDatasheet+"_QA", "Opt_"+i, Row, Keywords.getCustomObjectText(optPath));
							
							if(Keywords.getCustomAttribute(optPath, "style").contains("border"))
								d.setCellData(currentDatasheet,"CorrectAns",Row,String.valueOf(i));
							if (i>3 && (index < 134 || index > 143))
								i++;  // this is needed as the tests from 134 to 143 under TopicalQ have 5 options
							i++;
						}
					}
					
					if(rwPgVerification){
						
						//Verify if correct answer is highlighted
						if(!getQAText){
							if(Keywords.getCustomAttribute(correctAnsPath2, "style").contains("border"))
								Keywords.dualOutput("Correct Answer is highlighted for question #",String.valueOf(itemNum));
							else{
								Keywords.dualOutput("Correct Answer is highlighted for question #",String.valueOf(itemNum));
								ReportUtil.addStep("Verify answer highlighted for Q# "+itemNum, "Incorrect answer is highlighted", "Fail", null);
							}
						}
						
						//Verify if explanation label is displayed
						Keywords.checkContainsCustom(explPath, "title", TestUtil.getStringValueinArray(OR,"TstPg_Body_explanation_Text_Start_"+currentTestSuite,"Value"), "Title of Explanation text");
						
						//Verifies whether answer explanations are displayed
						if(!getQAText)
							Keywords.elementDisplayedCustom(explAnsPath);

						//Click on explanation to hide the explanation	
						Keywords.clickbyXpath(explPath);
						
						//Verify feedback ID if getQAText = false, otherwise update excel with reviewID
						if(!getQAText)	{
							if(section.equals("verbReas") || (currentTestSuite.equals("Suite2") && !currentDatasheet.contains("SciAss")))	
								Keywords.verifyObjectTextWithParameter("TstPg_Footer_FeedbackIDText_Text","Please include ID "+review_ID);
							else if (section.equals("phySci") || section.equals("bioSci") )
								Keywords.verifyObjectTextWithParameter("TstPg_Footer_FeedbackIDText_OtherSec","Please include ID "+review_ID);	
						} else {
							if(section.equals("verbReas") || (currentTestSuite.equals("Suite2") && !currentDatasheet.contains("SciAss")))
								d.setCellData(currentDatasheet,"Review_ID",Row,Keywords.getObjectText("TstPg_Footer_FeedbackIDText_Text"));
							else if (section.equals("phySci") || section.equals("bioSci") )
								d.setCellData(currentDatasheet,"Review_ID",Row,Keywords.getObjectText("TstPg_Footer_FeedbackIDText_OtherSec"));
						}						
					} // End of rwPgVerification module
					
					if (!answer.equals("") && !rwPgVerification)
						TestUtil.findElement("id",ansPath,"","","").click();
					
					//Mark and check MARKED functionality -- NOT for review mode
					if(!rwPgVerification)
						if(tobeMarked.equals("Y") || tobeMarked.equals("Yes"))
							if(Keywords.clickButton("TstPg_Footer_Mark_Button").equals("Pass")){
								Keywords.checkContains("TstPg_Footer_MARKED_Button", "src", "MARKED button");
								driver.switchTo().frame("sequenceContentFrame");
								driver.switchTo().activeElement();
							}	
					
					Row++; SubItemId++; itemNum++;
					Keywords.dualOutput("Completed answering Q#: ",(itemNum-1)+" from section "+section);
					
				} while((d.getCellData(currentDatasheet, "Page", Row-1)).equals(d.getCellData(currentDatasheet, "Page", Row)));
				
				if(completeRegression)
					Keywords.verifyObjectTextWithParameter(psgObj, d.getCellData(currentDatasheet+"_QA", "Passage_Text", Row-1));
				
				if(getQAText)
					d.setCellData(currentDatasheet+"_QA", "Passage_Text", Row-1, Keywords.getObjectText(psgObj));
				
				Keywords.dualOutput("Completed answering questions in section - ", d.getCellData(currentDatasheet, "Section", Row-1));
				ReportUtil.addStep(("Completion of section - "+d.getCellData(currentDatasheet, "Section", Row-1)), "Answered All", "Pass", null);
					
				if(currentTestSuite.equals("Suite1")){
					if (rwPgVerification && !isDiagORFL){
						if((d.getCellData(currentDatasheet, "Section", Row-1)).equals("verbReas"))
							Keywords.clickButton("TstPg_Footer_NextButton_VerbalReview");
						else
							Keywords.clickButton("TstPg_Footer_NextButton_Review");
					}
					else if((d.getCellData(currentDatasheet, "Section", Row-1)).equals("verbReas"))
						Keywords.clickButton("TstPg_Footer_VerbalButton");
					else
						Keywords.clickButton("TstPg_Footer_OtherButton");
					
				}else{
					//Thread.sleep(3000L);
					Keywords.clickButton("TstPg_Footer_Next_Button_Suite2");
				}
					//Keywords.clickButton("TstPg_Footer_Next_Button_Suite2");
  			} // end of IF loop for trial section skip
  				

			} while((d.getCellData(currentDatasheet, "Section", Row-1)).equals(d.getCellData(currentDatasheet, "Section", Row)));
    	
  			if(completeRegression && currentTestSuite.equals("Suite1") && !trialSec)
  				verifySecReviewPg(section);
  			
  			//The next few lines is only for trial section -- SHOULD ENHACE based on trial SEC functionality to be modified in future
  			if (trialSec) // doing this as Row++ loop would not be executed with trialSec = TRUE
					Row++;
  			
  			if(d.getCellData(currentDatasheet, "Section", Row).equals("trial")) {
  				trialSec = true;
  				if(rwPgVerification) //during review, trial section should be skipped
  					Row++;
  			}
  			
  			if(currentTestSuite.equals("Suite1")){
  				if(Row > noofRows){
  					if(rwPgVerification)
  						Keywords.clickButton("SecRw_Footer_ExitButton_LastSec");
  						//Keywords.mouseClick("SecRw_Footer_ExitButton_LastSec");
  					else{
  						if(!trialSec)
  							Keywords.clickButton("SecRw_Footer_EndButton_LastSec");
  						else // No need of Q&A for Trial Sec
  							Keywords.clickButton("SecBeg_Footer_TrialSec_End_Button");
  						//Submitting the test..
  						if(!currentBrowser.contains("Safari"))
  							Keywords.verifyText(TestUtil.closeAlertandgetText(),TestUtil.getStringValueinArray(OR,"NxtSec_SubmitAlert_Text_"+currentTestSuite,"Value"));
  						//TestUtil.closeAlertandgetText().matches(TestUtil.getStringValueinArray(OR,"NxtSec_SubmitAlert_Text","Value"));
  					}
  				}
  				//All other sections, navigate to other sections
  				else{
  					//Thread.sleep(2000L);
  					if(currentBrowser.contains("Safari")){
  						Thread.sleep(3000L);
  						//Keywords.clickButton("SecRw_Footer_TimeRemaining_Safari");
  						Keywords.keysOperation("SecRw_Footer_EndButton_Safari","ENTER");
  					}
  					else
  						Keywords.clickButton("SecRw_Footer_EndButton");
  					
  					if(!currentBrowser.contains("Safari"))
  						Keywords.verifyText(TestUtil.closeAlertandgetText(),TestUtil.getStringValueinArray(OR,"NxtSec_Alert_Text","Value"));

  					Keywords.dualOutput("Moving to the next section in the ", currentDatasheet+ " exam");
  					Thread.sleep(1000);
				
  					if(completeRegression && isDiagORFL)
  						verifySecBreakPg();
					
  					Keywords.clickButton("SecBrk_Footer_NextButton");
				
  					if(rwPgVerification) // doing this only for review page verification as for others this would be completed at the beginning of the loop 
  						Keywords.clickButton("SecBeg_Footer_Next_Button");
  				}
  			} // end of if(currentTestSuite.equals("suite1"))
  			else{
  				if(currentDataXL.contains("SciAss")){
  					if(maxrows == (Row-1)){
  						Keywords.clickButton("TstPg_Footer_LastSec_Button_Suite2");
  						Thread.sleep(2000L);
  						Keywords.clickButton("TstPg_Footer_EndTest_Button_xpath");
  					}
  					else{	
  						Thread.sleep(2000L);
  						Keywords.clickButton("TstPg_Footer_NextSec_Button_Suite2");
  						if (rwPgVerification)
  							Keywords.clickButton("SecBeg_Body_Dismiss_Button");
  					}
  				}else if(currentDataXL.contains("Subject") || currentDataXL.contains("RNC")) {//other suite2 sections
   					if(!rwPgVerification){
  						Keywords.clickButton("TstPg_Footer_EndTest_Button");
  						//No alert present for suite2 other sections - is present for RNC
  						if(!currentBrowser.contains("Safari"))
  							Keywords.verifyText(TestUtil.closeAlertandgetText(),TestUtil.getStringValueinArray(OR,"NxtSec_SubmitAlert_Text_"+currentTestSuite,"Value"));
  					}else
  						Keywords.clickButton("TstPg_Footer_EndTest_Button_Review");
  				}else if(currentDataXL.contains("TopicalQ"))  {   //other suite2 sections
  					//Keywords.clickButton("TstPg_Footer_EndTest_Button");
  					if(!rwPgVerification){
  						Thread.sleep(2000L);
  						Keywords.clickButton("TstPg_Footer_EndTest_Button_xpath"); // this is good for Topical Quiz
  						//Keywords.verifyText(TestUtil.closeAlertandgetText(),TestUtil.getStringValueinArray(OR,"NxtSec_SubmitAlert_Text_"+currentTestSuite,"Value"));
  					}
  					else{
  						Thread.sleep(2000L);
  						Keywords.clickButton("TstPg_Footer_EndTest_Button_xpath");
  					}
  						
  				}
  			}
  	  				
			Seccounter++; 
		} while (Seccounter<noofSections);
		trialSec = false;
		
		if(methodResult.equals("Pass"))
			ReportUtil.addStep("Verify the text of questions and answers", "All Q&A text is correct", "Pass", null);
		else
			ReportUtil.addStep("Verify the text of questions and answers", "Some Q&A text mismatches", "Fail", null);
		
		Thread.sleep(3000);
	
		//result = Keywords.assertTrueofObject("ItmRw_Body");   Check with resmi on this.
		Keywords.dualOutput("MCATTest: MCAT Diagnostic Test Successfully navigated", null);
		ReportUtil.addStep("Diaplay Review Page", "Review page discplayed", "Pass",null);
		
		return methodResult;
		
	}
		
	public static String verifySecBreakPg() throws IOException{
		Keywords.dualOutput("Inside verifySecBreakPg()", null);
		submethodL1Result = "Pass";
		
		TestUtil.verifyTestPageHeaders(d.getCellData("Test_Directions", "Break_Title",d.getCellRowNum("Test_Directions", "Sheet_Name", currentDatasheet)),rwPgVerification);
		Keywords.verifyObjectText("SecBrk_Body_Msg_Text"); // "BREAK for all while This is an authorized break." for others
		//Keywords.checkContains("SecBrk_Body_NextSec_Button", "src", "NEXT Section button on Break Page"); - Not appearing on LF Tests
		
		//Reporting for complete regression mode
		if(submethodL1Result.equals("Pass"))
			ReportUtil.addStep("Verify Test break page: Title, Header, logo, message, button", "All functionality has been validated", "Pass", null);
		else
			ReportUtil.addStep("Verify Test break page: Title, Header, logo, message, button", "Some issue w/functionality verified", "Fail", screenshotPath+fileName);
				
		return submethodL1Result;
	}
	
	public static String verifySecReviewPg(String section) throws IOException, InterruptedException{
		submethodL1Result="Pass";
		Keywords.dualOutput("Inside verifySecReviewPg of section: ", section);
		String tempResult = null;
			
		//Click REVIEW ALL button if correct button is displayed
		if(Keywords.checkContains("SecRw_Footer_RwAll_Button", "src", "REVIEW ALL button").equals("Pass")){
			//click the REVIEW ALL button
			Keywords.dualOutput("Inside IF loop of 'SecRw_Footer_RwAll_Button'", null);
			if(Keywords.clickButton("SecRw_Footer_RwAll_Button").equals("Pass"))
				//Verify Review All functionality : Verify header title, first item #, click next and traverse to Item Review, look for text Item Review at bottom
				verifySecRwpgButtons(section,"ReviewAll");
		}
		//Click REVIEW Incomplete button if correct button is displayed
		if(Keywords.checkContains("SecRw_Footer_Rwincomp_Button", "src", "REVIEW Incomplete button").equals("Pass"))
			//click the REVIEW Incomplete button
			if(Keywords.clickButton("SecRw_Footer_Rwincomp_Button").equals("Pass"))
				//Verify Review Incomplete functionality : Verify header title, first item # on the incomplete page, click next only on the pages where incomplete questions present 
				verifySecRwpgButtons(section,"RwIncomp");
		
		//Click REVIEW marked button if correct button is displayed
		if(Keywords.checkContains("SecRw_Footer_RwMarked_Button", "src", "REVIEW Marked button").equals("Pass"))
			//click the REVIEW marked button
			if(Keywords.clickButton("SecRw_Footer_RwMarked_Button").equals("Pass"))
				//Verify Review marked functionality : Verify header title, first item # on the incomplete page, click next only on the pages where incomplete questions present 
				verifySecRwpgButtons(section,"RwMark");
		
		//Verify the text name of "Item Review" on the footer
		Keywords.verifyObjectTextWithParameter("TstPg_Footer_QueNum_Text", "Item Review");
			
		
		//Verify time remaining and presence of time
		if(isDiagORFL){
			Keywords.verifyObjectText("TstPg_Footer_TimeRe_Text");
			Keywords.elementDisplayed("TstPg_Footer_TimeRe_Time");
		}
		
		//Verify GoTobutton functionality: button attribute, text and functionality to go to question and come back to Review page
		Keywords.checkContains("SecRw_Body_Goto_Button", "src", "GoTo button on Sub-Review page");
			
		Keywords.verifyObjectText("SecRw_Body_Goto_Text");
		if(Keywords.inputValue("SecRw_Body_Goto_Input", "4").equals("Pass"))
			if(Keywords.clickButton("SecRw_Body_Goto_Button").equals("Pass")){
				Thread.sleep(1000);
				if(section.equals("verbReas"))
					tempResult=Keywords.clickButton("TstPg_Footer_Review_Button_Verbal");
				else
					tempResult=Keywords.clickButton("TstPg_Footer_Review_Button");
				
				if(tempResult.equals("Pass")){
					Thread.sleep(1000);
					Keywords.verifyObjectTextWithParameter("TstPg_Footer_QueNum_Text", "Item Review");
				}
				//else -- NEED TO THINK ABOUT THIS OTHERWISE CONTROL GETS STRUCK HERE...
			}
		
		//Verify the table header labels
		Keywords.verifyObjectText("SecRw_Body_Status_Text");
		Keywords.verifyObjectText("SecRw_Body_QN_Text");
		Keywords.verifyObjectText("SecRw_Body_Answer_Text");
		
		//Verify the table values - review answers
		verifySecRwpgAnswers(section);
		
		//Report the result
		if(submethodL1Result.equals("Pass"))
			ReportUtil.addStep("Verify section review page: Header logo & title, GoTo & footer buttons & functionality, Reviewing answers", "All functionality has been validated", "Pass", null);
		else
			ReportUtil.addStep("Verify section review page: Header logo & title, GoTo & footer buttons & functionality, Reviewing answers", "Some issue w/functionality verified", "Fail", null);

		return submethodL1Result;
	}
	
	public static String verifySecRwpgButtons(String section, String callingMethod) throws IOException, InterruptedException{ //callingMethod takes only the following: Incomplete, Marked, ReviewAll
		Keywords.dualOutput("Inside verifySecRwPgButtons of section: ",section);
		
		submethodL2Result = "Pass";
		int secFirstRow = d.getFirstRowInstance(currentDatasheet, "Section", section);
		int totalQue = d.getNoOfMatches(currentDatasheet, "Section", section);
		int secLastRow = secFirstRow + totalQue - 1;
		boolean rwAllIndi = false; boolean rwIncomp = false; boolean rwMark = false;
		int psgQue = 0; int doCounter = -1; int nxtpsgQue = 0;
		
		if(callingMethod.equals("ReviewAll"))
			rwAllIndi = true;
		
		do {
    		int SubItemId = 1; doCounter = secFirstRow;
    		
    		do {
    			psgQue = vc.strToDblToInt(d.getCellData(currentDatasheet, "PsgQuestion", doCounter));
    			String answer = vc.strToDblToStr((d.getCellData(currentDatasheet, "InputAns", doCounter)));
    			String mark = (d.getCellData(currentDatasheet, "Mark", doCounter));
    			if (answer == null && callingMethod.equals("RwIncomp"))
    				rwIncomp = true;
    			if ((mark.equals("Y") || mark.equals("Yes")) && callingMethod.equals("RwMark"))
    				rwMark = true;
    			
    			//this has to be validated only if reviewall or anyone is true and for the first q
    			if((doCounter == secFirstRow) && (rwAllIndi || rwIncomp || rwMark))
    				Keywords.verifyObjectTextWithParameter("TstPg_Header_Exam_Text", d.getCellData(currentDatasheet, "PageTitle", secFirstRow));

    			doCounter++;
    			nxtpsgQue = vc.strToDblToInt(d.getCellData(currentDatasheet, "PsgQuestion", doCounter));
    		} while(psgQue < nxtpsgQue);
    		
    		driver.switchTo().frame("sequenceContentFrame");
    		driver.switchTo().activeElement();
    		Thread.sleep(1000); //This is essential as otherwise script is running faster than the pageloadtime.
   		
    		do {
    			//Reading the answers from input excel
    			String itemNum = vc.strToDblToStr((d.getCellData(currentDatasheet, "Item_Num", secFirstRow)));
    			String itemPath = TestUtil.getStringValueinArray(OR,"TstPg_Body_Item_Text_Start","Key")+SubItemId+TestUtil.getStringValueinArray(OR,"TstPg_Body_Item_Text_End","Key");
    			
    			if(rwAllIndi || rwIncomp || rwMark) {
    				//verify ItemNum
    				Keywords.verifyCustomObjectText(itemPath, "Item "+itemNum);
    			}	
    			secFirstRow++; SubItemId++;
    		} while((d.getCellData(currentDatasheet, "Pg", secFirstRow-1)).equals(d.getCellData(currentDatasheet, "Pg", secFirstRow)));
    		
    		//After finishing each passage navigate to the next passage

			if(rwAllIndi || rwIncomp || rwMark) {
    		driver.switchTo().defaultContent();
    		driver.switchTo().frame("testMode");
    		if((d.getCellData(currentDatasheet, "Section", secFirstRow-1)).equals("verbReas"))
    			Keywords.clickButton("TstPg_Footer_VerbalButton");
    		else
    			Keywords.clickButton("TstPg_Footer_OtherButton");
			}
    		rwIncomp = false; rwMark = false;
 
    	} while(secFirstRow <= secLastRow);

		//Verify the text name of "Item Review" on the footer
		Keywords.verifyObjectTextWithParameter("TstPg_Footer_QueNum_Text", "Item Review");
		
		if (submethodL2Result.equals("Pass")) {
			Keywords.dualOutput(callingMethod+" functionality is validated and successful", null);
			ReportUtil.addStep("Validate "+callingMethod+" button functionality", "Successfully validated", "Pass",null);
		} else {
			Keywords.dualOutput(callingMethod+" functionality is NOT successful", null);
			ReportUtil.addStep("Validate "+callingMethod+" button functionality", "Somwthing wrong", "Fail",fileName);
		}
 
		return submethodL2Result;
	}
	
	public static String verifySecRwpgAnswers(String section) throws IOException, InterruptedException{
		Keywords.dualOutput("Inside verifySecRwpgAnswers of section: ",section);
		submethodL2Result = "Pass";
		Thread.sleep(2000L);
		
		//Verify the table values - review answers
		int secFirstRow = d.getFirstRowInstance(currentDatasheet, "Section", section);
		int totalQue = d.getNoOfMatches(currentDatasheet, "Section", section);
		int secLastRow = secFirstRow + totalQue - 1;
		int queNum = 1; String itemNum = null; String path = null; String answer = null; String pathbegin = null; String marked = null;
		
		do {
			answer = TestUtil.getAlphabet(vc.strToDblToInt(d.getCellData(currentDatasheet, "InputAns", secFirstRow)));
			
			//Verify status
			marked = d.getCellData(currentDatasheet, "Mark", secFirstRow);
			pathbegin = TestUtil.getStringValueinArray(OR,"SecRw_Body_Status_Start","Key")+queNum;
			if (marked.equals("Y") || marked.equals("Yes")){
				path = pathbegin+TestUtil.getStringValueinArray(OR,"SecRw_Body_Status_Marked","Key");
				Keywords.checkContainsCustom(path, "src", TestUtil.getStringValueinArray(OR,"SecRw_Body_Status_Marked","Value"),"Subreview Question# "+queNum+" marked");
			} else if(answer.equals("")){
				path = pathbegin+TestUtil.getStringValueinArray(OR,"SecRw_Body_Status_Incompl","Key");
				Keywords.checkContainsCustom(path, "src", TestUtil.getStringValueinArray(OR,"SecRw_Body_Status_Incompl","Value"),"Subreview Question# "+queNum+" incomplete");
			} else {
				path = pathbegin+TestUtil.getStringValueinArray(OR,"SecRw_Body_Status_Answered","Key");
				Keywords.dualOutput(path, null);
				Keywords.checkContainsCustom(path, "src", TestUtil.getStringValueinArray(OR,"SecRw_Body_Status_Answered","Value"),"Subreview Question# "+queNum+" answered");
			}
			
			//verify question number
			itemNum = vc.strToDblToStr(d.getCellData(currentDatasheet, "Item_Num", secFirstRow));
			path = pathbegin+TestUtil.getStringValueinArray(OR,"SecRw_Body_QN_Value","Key");
			Keywords.verifyCustomObjectText(path,itemNum);
			
			//verify answer choice
			path = pathbegin+TestUtil.getStringValueinArray(OR,"SecRw_Body_Answer_Value","Key");
			Keywords.verifyCustomObjectText(path,answer);
			
			if (submethodL2Result.equals("Pass")){
				Keywords.dualOutput("Question# ",queNum+ " in section "+section+" is validated successfully");
			}else {
				Keywords.dualOutput("Question# ",queNum+ " in section "+section+" is NOT successfully captured");
			}
			secFirstRow++; queNum++;
		} while (secFirstRow <= secLastRow);
		
		//Report the result
		if(submethodL2Result.equals("Pass"))
			ReportUtil.addStep("Verify the following from answers table: Question #, Answer, Status", "All functionality has been validated", "Pass", null);
		else
			ReportUtil.addStep("Verify the following from answers table: Question #, Answer, Status", "Some issue w/functionality verified", "Fail", null);
		
		return submethodL2Result;
	}
	
	public static String verifyTestPageFooters(int rowNum) throws IOException{
		Keywords.dualOutput("Executing Test Page Footers", null);
		submethodL1Result = "Pass";	int currentPassageBegin = -1;  int currentPassageend = -1; int noofQue; int noofMatches;
		
		//verify the text CH
		Keywords.verifyObjectText("TstPg_Footer_CH");
		
		noofMatches = d.getNoOfMatches(currentDatasheet, "Section", d.getCellData(currentDatasheet,"Section", rowNum));
		
		// Code below is to get additional parameters needed to validate CH 1 - 5 of 39
		if(currentTestSuite.contains("Suite1")){
			while(vc.strToDblToInt(d.getCellData(currentDatasheet, "PsgQuestion", rowNum+1)) != 1 && d.getCellData(currentDatasheet, "PsgQuestion", rowNum+1) != ""){
				Keywords.dualOutput("Inside while ", d.getCellData(currentDatasheet, "PsgQuestion", rowNum));
				rowNum++;
			}
			currentPassageend=vc.strToDblToInt(d.getCellData(currentDatasheet,"Item_Num", rowNum));
			noofQue = vc.strToDblToInt(d.getCellData(currentDatasheet, "PsgQuestion", rowNum));
			currentPassageBegin=currentPassageend-noofQue+1;
			noofMatches = d.getNoOfMatches(currentDatasheet, "Section", d.getCellData(currentDatasheet,"Section", rowNum));
			Keywords.dualOutput("Parameter to be compared",currentPassageBegin+" - "+currentPassageend+" of "+noofMatches);
		
			//verify the text CH 1 - 5 of 39
			Keywords.verifyObjectTextWithParameter("TstPg_Footer_QueNum_Text",currentPassageBegin+" - "+currentPassageend+" of "+noofMatches);
		}
		else
			Keywords.verifyObjectTextWithParameter("TstPg_Footer_QueNum_Text",vc.strToDblToInt(d.getCellData(currentDatasheet, "Item_Num", rowNum))+" of "+noofMatches);	

		
		//Verify REVIEW button -- Applicable only for Package 1
		if(currentTestSuite.equals("Suite1"))
			if(rwPgVerification && isDiagORFL)
				Keywords.checkContains("TstPg_Footer_Review_Button_ReviewM", "src","REVIEW button");
			else
				Keywords.checkContains("TstPg_Footer_Review_Button", "src","REVIEW button");
		
		//Verify EXHIBIT button -- NEED TO CONDITION IN BASED ON SECTION
		if (TestUtil.isExhibitApplicable(d.getCellData(currentDatasheet, "Section", rowNum)))
			if(rwPgVerification) {
				if(isDiagORFL) 
					Keywords.checkContains("TstPg_Footer_Exhibit_Button"+"_"+currentTestSuite, "src","EXHIBIT button");
				else {
					if(currentTestSuite.contains("Suite1"))
						Keywords.checkContains("TstPg_Footer_Exhibit_Button_ReviewM_Suite1", "src","EXHIBIT button");
					else
						Keywords.checkContains("TstPg_Footer_Exhibit_Button"+"_"+currentTestSuite, "src","EXHIBIT button");
				}
			}
			else
				Keywords.checkContains("TstPg_Footer_Exhibit_Button"+"_"+currentTestSuite, "src","EXHIBIT button");
	
		// HELP and BACK buttons -- only on Suite2
		if(currentTestSuite.contains("Suite2")){
			if(currentDataXL.contains("SciAss"))
				Keywords.checkContains("TstPg_Footer_Help_Button", "src","HELP button");
			else
				Keywords.checkContains("TstPg_Footer_Help_Button_Suite2", "src","HELP button");
			Keywords.checkContains("TstPg_Footer_Back_Button", "src","BACK button");
		}
		
		return submethodL1Result;
	}

	public static String verifyAdditionalTestPageFooters() throws IOException{
		submethodL1Result = "Pass";
		
		//verify time present
		if(isDiagORFL || currentDatasheet.contains("SciAss")){
			if (Keywords.elementDisplayed("TstPg_Footer_TimeRe_Time"))
				ReportUtil.addStep("Verify Time Remaining on footer", "Time Remaining is displayed correctly", "Pass", null);
			else
				ReportUtil.addStep("Verify Time Remaining on footer", "Time Remaining is NOT displayed", "Fail", screenshotPath+fileName);
		}
		
		//Verify MARK button 
		if(currentTestSuite.equals("Suite1"))
			Keywords.checkContains("TstPg_Footer_Mark_Button", "src","MARK button");
		
		//Verify Time Remaining text
		if(isDiagORFL)	
			Keywords.verifyObjectText("TstPg_Footer_TimeRe_Text");
		
		return submethodL1Result;		
	}

	public static String verifyAdditionalREVIEWFooters(int rowNum) throws IOException, InterruptedException{
		submethodL1Result = "Pass";
		
		String section = d.getCellData(currentDatasheet, "Section", rowNum);
		
		//verify EXIT button
		if(currentTestSuite.equals("Suite1") && !isDiagORFL) 
			Keywords.checkContains("TstPg_Footer_Exit_Button_Suite1_Review", "src","EXIT button");
		else
			Keywords.checkContains("TstPg_Footer_Exit_Button_"+currentTestSuite, "src","EXIT button");
		
		//Verify Feedback footers: Have feedback? Email us at KaplanMCATFeedback@kaplan.com
	
			Thread.sleep(500);
			if(section.equals("verbReas") || (currentTestSuite.equals("Suite2") && !currentDatasheet.contains("SciAss"))) {
				Keywords.verifyObjectText("TstPg_Footer_Feedbackmain_Text");
				Keywords.verifyObjectText("TstPg_Footer_Feedbackemail_Text_"+currentProduct); 
			}
			else if (section.equals("phySci") || section.equals("bioSci") ){
				Keywords.verifyObjectText("TstPg_Footer_Feedbackmain_OtherSec");
				Keywords.verifyObjectText("TstPg_Footer_Feedbackemail_OtherSec_"+currentProduct); 
			}
		
		//Reporting
		if(submethodL1Result.equals("Pass"))
			ReportUtil.addStep("Verify the following: Question #, Answer, Status", "All functionality has been validated", "Pass", null);
		else
			ReportUtil.addStep("Verify the following: EXIT button, Feedback text and email id", "Some issue w/functionality verified", "Fail", screenshotPath+fileName);
		return submethodL1Result;		
	}	
	
	public static String completeTrialSection() throws IOException{
		Keywords.dualOutput("Inside completeTrialSection()", null);
		submethodL1Result = "Pass";
	
		TestUtil.verifyTestPageHeaders(d.getCellData("Test_Directions", "Break_Title",d.getCellRowNum("Test_Directions", "Sheet_Name", currentDatasheet)),rwPgVerification);
	Keywords.verifyObjectText("SecBrk_Body_Msg_Text"); // "BREAK for all while This is an authorized break." for others
	//Keywords.checkContains("SecBrk_Body_NextSec_Button", "src", "NEXT Section button on Break Page"); - Not appearing on LF Tests
	
	//Reporting for complete regression mode
	if(submethodL1Result.equals("Pass"))
		ReportUtil.addStep("Verify Test break page: Title, Header, logo, message, button", "All functionality has been validated", "Pass", null);
	else
		ReportUtil.addStep("Verify Test break page: Title, Header, logo, message, button", "Some issue w/functionality verified", "Fail", screenshotPath+fileName);
			
	return submethodL1Result;
}
	
	
	
}


