/* This JAVA file is designed to automate Workshops, Review Notes, Lessons on Demand sections
 * Video file contents are not automatable and the pre-req for Workshops is to disable 
 * the flash video add-on respective browsers * 
 */

package TestSuite4;

import Database.Excel_Ops;
import ParentClasses.DriverScript;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import Reports.ReportUtil;
import TestSuite1.ReviewPg;
import Utility.Keywords;
import Utility.TestUtil;
import Utility.Variable_Conversions;


public class Presentations extends DriverScript{

	// All initialization
	public static int currentSection; 
	public static int noofSections;
	public static Excel_Ops d = null;
	public static Excel_Ops AllRes = null;

	public static Utility.Variable_Conversions vc = null;
	public static boolean rwPgVerification = false; // Sets to true during review mode validations
	public static boolean completeRegression = false; // Sets to true for complete regression testing option chosen by user (in ControllerNew.xlsx)
	public static boolean  getQAText = false; // Sets to true to get text of question & answers when the option chosen by user (in ControllerNew.xlsx)
	
	
	//This method will be called by Driver Script parent class file using Java reflection, so the method name should be constant
		@Test
		public static String completeFlowTest(String sheetName) throws IOException, InterruptedException {

			//Initializing
			Keywords.dualOutput("Inside Suite 4 CompleteflowTest", sheetName);
			classResult = "Pass"; String result = null; rwPgVerification = false; getQAText = false;
			
			//Since the code is common for Complete regression, get QA Text and general flow regression, the following initialization is done
			//to ensure right test will be carried out
			if (currentCaseName.equals("Complete_Regression"))
				completeRegression = true;
			if (currentCaseName.equals("getQAText"))
				getQAText = true;
		
			//Initialize excel instance and variable conversion instance
			d= new Excel_Ops(System.getProperty("user.dir")+"//src//Config//"+currentDataXL);
			AllRes= new Excel_Ops(System.getProperty("user.dir")+"//src//Config//AllRes.xlsx");

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
					
				int index = vc.strToDblToInt(AllRes.getCellData(currentProduct, "Index", AllRes.getFirstRowInstance(currentProduct, "TestName", currentTestName.trim())));
				
				if(currentBrowser.equals("Firefox"))
						Keywords.clickbyXpath("//*[@id='sequence"+String.valueOf(index)+"']/td[2]/a"); 
					else {
						Thread.sleep(3000L);
						Keywords.frameSwitch("defaultContent");
						Keywords.frameSwitch("main");
						Keywords.clickbyXpath("html/body/div[1]/div[3]/div[2]/div[1]/div/section/table/tbody/tr["+String.valueOf(index+1)+"]/td[2]/a");
					}
					
					//mainmethod is called for 
					if(completeRegression)
						mainMethod();
					if(!(currentDataXL.contains("LessonsOnDemand"))){
						ReviewPage rw = new ReviewPage();
						rw.completeFlowTest(currentDatasheet);	
					}
					
			   }else{
				   Keywords.dualOutput("Error in loading MCAT All resources page", null);
				   result="fail";
				   fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+"AllResources.jpg";
				   TestUtil.takeScreenShot(screenshotPath+fileName);
				   ReportUtil.addStep(sheetName+" Exam", "Error loading page", result,screenshotPath+fileName);
			   }
			
			} // end of if loop of Login
			//driver.quit();
			return classResult;
			} 	catch(Throwable t){
				// error
				Keywords.dualOutput("Error in PRESENTATIONS ",null);
				ReportUtil.addStep("Verify Presentations Test ", "Not Successful", "Fail", null);
				classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
				
				return classResult;
			}
		} // end of function
		
		
		public static String mainMethod() throws IOException, InterruptedException{
			methodResult = "Pass";
			int Row = 2; int totalRows = d.getRowCount(currentDatasheet);
			String chapName = ""; String reviewId = "";
			
			//Once Start Test Page is verified, click on Start button
			methodResult = "Pass";
			if (completeRegression ){

				Keywords.clickButton("JspStart_Start_Button");
				Keywords.dualOutput("Jasper Start Test Page is launched", null);
				ReportUtil.addStep("MCAT "+currentDatasheet+" Exam Launch","Launch Exam page","Pass", null);
				Thread.sleep(1000);
			}
			
			do {
				Thread.sleep(500);

				if (completeRegression ){

					if(!(currentDataXL.contains("LessonsOnDemand")))
						verifyTestPageFooters(Row);
					else
						verifyLODPageFooters(Row);
					
					//Verify Logo and Review ID 
					if(currentDataXL.contains("ReviewChapters") || currentDataXL.contains("Premier") ) 
						if(completeRegression)
							verifyAdditionalTestPageFooters(Row);
				}
		
				Thread.sleep(500);
				if(getQAText){		
				
					Thread.sleep(1500);
					String chapNamePath = "";
					if (!(currentDataXL.contains("LessonsOnDemand"))){ //blocking as this is not working per the functionality
						//driver.switchTo().defaultContent();
						//driver.switchTo().frame("testMode");
					
						//chapName = Keywords.getObjectText("VwPg_Footer_ChapName");
					}
					else{
						//chapName = Keywords.getObjectText("VwPg_Footer_ChapName_LOD");
						//System.out.println("Chapter name ====>" + chapName);
					}
					//d.setCellData(currentDatasheet, "Topic", Row, chapName );
					
					if (!(currentDataXL.contains("LessonsOnDemand"))){
						driver.switchTo().defaultContent();
						driver.switchTo().frame("testMode");
						reviewId = Keywords.getTextCustomElement(TestUtil.getStringValueinArray(OR, "VwPg_Footer_FeedbackID_Text", "Key"), "Review_ID" );
						d.setCellData(currentDatasheet, "Review_ID", Row, reviewId );
					}

				}
				
				
				if (completeRegression){
					if(!(currentDataXL.contains("LessonsOnDemand"))) 	{
						
						//Keywords.clickButton("TstPg_Footer_Next_Button_Suite2");
						if(Row < totalRows) {
							Keywords.clickButton("VwPg_Chapter_List");
							Keywords.clickbyXpath((TestUtil.getStringValueinArray(OR, "VwPg_Chapter_Start", "Key")+ (Row) + (TestUtil.getStringValueinArray(OR, "VwPg_Chapter_End", "Key"))));
							Thread.sleep(1000L);
						}
					}			
					else{					
						
						Thread.sleep(4000L);
						driver.switchTo().defaultContent();
						driver.switchTo().frame("testMode"); //delete this and above as appropriate
						Keywords.clickLink("VwPg_Header_Back_LOD");
						
						Thread.sleep(2000L);
						driver.switchTo().defaultContent();
						Keywords.frameSwitch("main");
						//String nextItem = TestUtil.getStringValueinArray(OR,"ItmRw_Body_ItmRw_LOD_Start","Key")+Row+TestUtil.getStringValueinArray(OR,"ItmRw_Body_ItmRw_LOD_End","Key");
						//System.out.println("nextItem ===>" + nextItem);
						Keywords.clickLinkText(d.getCellData(currentDatasheet, "Topic", Row));

					}
				}
				Row++;
				System.out.println(Row);
				Keywords.dualOutput("Row incremented to : " +Row, null);
				
			}while(Row <= totalRows);
			
			
			APPLICATION_LOGS.debug("Completed presentation - "+ d.getCellData(currentDatasheet, "PageTitle", Row-1));
			ReportUtil.addStep(("Completion presentation - "+d.getCellData(currentDatasheet, "PageTitle", Row-1)), "Completed All", "Pass", null);
			
			if(!(currentDataXL.contains("LessonsOnDemand"))) {
				Keywords.clickButton("VwPg_Footer_EndTest_Button");
				if(!currentBrowser.contains("Safari"))
					Keywords.verifyText(TestUtil.closeAlertandgetText(),TestUtil.getStringValueinArray(OR,"VwPg_Footer_EndTest_Button","Value"));

				APPLICATION_LOGS.debug("Presentations: Review Notes Chapter " + currentTestName + " Successfully navigated");
				ReportUtil.addStep("Display Review Page", "Review page displayed", "Pass",null);
			}

			return methodResult;
		}
			
	

		public static String verifyTestPageFooters(int rowNum) throws IOException, InterruptedException{
			Keywords.dualOutput("Executing Test Page Footers", null);
			submethodL1Result = "Pass";	int noofMatches;
				
			noofMatches = d.getNoOfMatches(currentDatasheet, "Section", d.getCellData(currentDatasheet,"Section", rowNum));
			Thread.sleep(3000L);
			//verify Chapter List attribute -- Unable to locate this, so commenting it out
			//Keywords.checkContains("VwPg_Chapter_List", "src","Chapters button");
		
			//verify the text CH
			Keywords.verifyObjectText("TstPg_Footer_CH");
			
			//verify 1 of 30
			String itemNum = vc.strToDblToStr(d.getCellData(currentDatasheet, "Item_Num", rowNum));
			Keywords.verifyObjectTextWithParameter("TstPg_Footer_QueNum_Text",itemNum + " of "+noofMatches);	

			//verify chapter name
			Keywords.verifyObjectTextWithParameter("VwPg_Footer_ChapName",d.getCellData(currentDatasheet, "Topic", rowNum));	

			//verify Review ID
			Keywords.verifyObjectTextWithParameter("VwPg_Footer_FeedbackID_Text",d.getCellData(currentDatasheet, "Review_ID", rowNum));	

			
			//verify back button
			if(currentDataXL.contains("ReviewChapters") || currentDataXL.contains("Premier") ) {
				if (rowNum == 2)
					Keywords.checkContains("VwPg_Footer_Back_Button_INACT", "src","BACK button");
				else
					Keywords.checkContains("VwPg_Footer_Back_Button", "src","BACK button");
			}else{
				if (rowNum == 2)
					Keywords.checkContains("VwPg_Footer_Back_Button_WS_INACT", "src","BACK button");
				else	
					Keywords.checkContains("VwPg_Footer_Back_Button_WS", "src","BACK button");
			}
			//Reporting
			if(submethodL1Result.equals("Pass"))
				ReportUtil.addStep("Verify the Footer", "All functionality has been validated", "Pass", null);
			else
				ReportUtil.addStep("Verify the Footer", "Some issue w/functionality verified", "Fail", screenshotPath+fileName);
		
			
			return submethodL1Result;
		}

		public static String verifyAdditionalTestPageFooters(int rowNum) throws IOException, InterruptedException{
			submethodL1Result = "Pass";
			String section = d.getCellData(currentDatasheet, "Section", rowNum);
			
			//verify Logo button
			if(!currentDataXL.contains("Premier"))
				Keywords.checkContains("VwPg_Footer_Logo", "src","LOGO");
			
			//Verify Feedback footers: Have feedback? Email us at KaplanMCATFeedback@kaplan.com
			Keywords.verifyObjectText("TstPg_Footer_Feedbackemail_Text_"+currentProduct);
			Keywords.verifyObjectText("TstPg_Footer_Feedbackmain_Text");
				
			//Reporting
			if(submethodL1Result.equals("Pass"))
				ReportUtil.addStep("Verify the following: Question #, Answer, Status", "All functionality has been validated", "Pass", null);
			else
				ReportUtil.addStep("Verify the following: EXIT button, Feedback text and email id", "Some issue w/functionality verified", "Fail", screenshotPath+fileName);
			System.out.println("Done with Additional review footers");
			return submethodL1Result;			
		}
		
		public static String verifyLODPageFooters(int rowNum) throws IOException, InterruptedException{
			Keywords.dualOutput("Executing Test Page Footers", null);
			System.out.println("Executing LOD Footers");
			submethodL1Result = "Pass";	
			
			driver.switchTo().defaultContent();
			Keywords.frameSwitch("testMode");
			
			//verify Chapter List -- Not working hence commenting
			//Keywords.verifyObjectText("VwPg_Chapters_LOD");
			Thread.sleep(3000L);
			
			//Verify play button 
			Keywords.checkContains("VwPg_Footer_Play_LOD", "class","Play");
			
			//verify Time  -- Not working hence commenting
			//Keywords.verifyObjectTextWithParameter("VwPg_Footer_Time_LOD",d.getCellData(currentDatasheet, "Time", rowNum));

			//verify chapter Name which is displayed like this 'CH. 2 OF 12 – INTEGERS'  -- Not working hence commenting
			//String chNametoVerify = "CH. "+rowNum+" OF "+d.getNoOfMatches(currentDatasheet, "PageTitle", currentTestName)+" – "+d.getCellData(currentDatasheet, "Topic", rowNum);
			//System.out.println(chNametoVerify);
			//Keywords.verifyObjectTextWithParameter("VwPg_Footer_CH_LOD",chNametoVerify);
			
			//Verify volume button
			Keywords.checkContains("VwPg_Footer_Volume_LOD", "class","Volume Button");
	/*		
			//verify CC button   -- Not working hence commenting
			Keywords.verifyObjectText("VwPg_Footer_CC_LOD");
			Keywords.mouseClick("VwPg_Footer_CC_LOD");
			Keywords.verifyObjectTextWithParameter("VwPg_Footer_CC_LOD",d.getCellData(currentDatasheet, "CC", rowNum));
			
			//verify Transcript button   -- Not working hence commenting
			Keywords.verifyObjectText("VwPg_Footer_Transcript_LOD");
			Keywords.clickButton("VwPg_Footer_Transcript_LOD");
			Keywords.verifyObjectTextWithParameter("VwPg_Footer_Transcript_LOD",d.getCellData(currentDatasheet, "Transcript", rowNum));
	*/		
			//Reporting
			if(submethodL1Result.equals("Pass"))
				ReportUtil.addStep("Verify the Footer", "All functionality has been validated", "Pass", null);
			else
				ReportUtil.addStep("Verify the Footer", "Some issue w/functionality verified", "Fail", screenshotPath+fileName);
		
			
			return submethodL1Result;
		}
}


/// Notes from browser validations...


/*					//String cssPath = "#sequence"+String.valueOf(AllRes.getFirstRowInstance("Input", "TestName", currentTestName.trim())-2)+" > td:nth-of-type(2) > a";
String cssPath = "html > body > div:nth-of-type(1) > div:nth-of-type(3) > div:nth-of-type(2) > div:nth-of-type(1) > div > section > table > tbody > tr:nth-of-type("+String.valueOf(AllRes.getFirstRowInstance("Input", "TestName", currentTestName.trim())-1)+") > td:nth-of-type(2) > a";
System.out.println(cssPath);
if(currentBrowser.contains("Chrome"))
	driver.findElement(By.cssSelector(cssPath));
else 
*/

/* Works for Chrome				
Thread.sleep(3000L);

Keywords.frameSwitch("defaultContent");
Keywords.frameSwitch("main");
Keywords.clickbyXpath("html/body/div[1]/div[3]/div[2]/div[1]/div/section/table/tbody/tr["+String.valueOf(AllRes.getFirstRowInstance("Input", "TestName", currentTestName.trim())-1)+"]/td[2]/a");
Thread.sleep(5000L);
*/

/* For firefox
//driver.switchTo().defaultContent();
driver.switchTo().frame("main");
//}
Thread.sleep(50000L);
Keywords.clickbyXpath("//*[@id='sequence"+String.valueOf(AllRes.getFirstRowInstance("Input", "TestName", currentTestName.trim())-2)+"']/td[2]/a");
Thread.sleep(2000L);
//end for firefox	*/
/*					
//Keywords.frameSwitch("main");
//Thread.sleep(3000L);
if(!currentBrowser.contains("Safari"))
driver.manage().timeouts().pageLoadTimeout(180, TimeUnit.SECONDS);
if(!currentBrowser.contains("Safari"))
Keywords.clickLinkText(currentTestName.trim());
else
Keywords.clickbyXpath("//*[@id='sequence"+String.valueOf(AllRes.getFirstRowInstance("Input", "TestName", currentTestName.trim())-2)+"']/td[2]/a");


*/	