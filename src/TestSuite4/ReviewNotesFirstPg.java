/* This JAVA class file is designed to test the Review Notes section first page
 * where the list of contents (Review Notes) will displayed and user can select desired ones
 * and create a play list to go through them
 */

package TestSuite4;

import Database.Excel_Ops;
import ParentClasses.DriverScript;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import Reports.ReportUtil;
import Utility.Keywords;
import Utility.TestUtil;
import Utility.Variable_Conversions;


public class ReviewNotesFirstPg extends DriverScript{

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
			d= new Excel_Ops(System.getProperty("user.dir")+"\\src\\Config\\"+currentDataXL);
			AllRes= new Excel_Ops(System.getProperty("user.dir")+"//src//Config//AllRes.xlsx");
			vc = new Variable_Conversions();
			
			try {
			//Start the test by logging into Jasper
			if (TestUtil.jasperLogin().equals("Pass")) {
				
				driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				if (!currentBrowser.equals("IE") && !currentBrowser.equals("Chrome"))
					driver.switchTo().frame("main");
			
				//Proceed further only if MCAT Test Prep page is displayed
				if (Keywords.verifyTitle(currentTestName)){
					Keywords.dualOutput("MCAT All Resources page is launched", null);
				
					//Selecting MCAT Diagnostic Test
					driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
					
					int index = vc.strToDblToInt(AllRes.getCellData(currentProduct, "Index", AllRes.getFirstRowInstance(currentProduct, "TestName", currentTestName.trim())));
					//Keywords.clickLinkText(currentTestName.trim());
					if(currentBrowser.equals("Firefox"))
						Keywords.clickbyXpath("//*[@id='sequence"+String.valueOf(index)+"']/td[2]/a");
					else {
						Thread.sleep(3000L);
						Keywords.frameSwitch("defaultContent");
						Keywords.frameSwitch("main");
						Keywords.clickbyXpath("html/body/div[1]/div[3]/div[2]/div[1]/div/section/table/tbody/tr["+String.valueOf(index+1)+"]/td[2]/a");
					}
					
					if(completeRegression || getQAText)
						mainMethod();
					
					
			   }else{
				   Keywords.dualOutput("Error in loading MCAT All resources page", null);
				   result="fail";
				   fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+"AllResources.jpg";
				   TestUtil.takeScreenShot(screenshotPath+fileName);
				   ReportUtil.addStep(sheetName+" Exam", "Error loading page", result,screenshotPath+fileName);
			   }
			
			} // end of if loop of Login
			return classResult;
			} catch(Throwable t){
				// error
				Keywords.dualOutput("Error in Reivew Notes First Page", null);
				ReportUtil.addStep("Verify PBT Test ", "Not Successful", "Fail", null);
				classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
				
				return classResult;
			}
		} // end of function
		
		
		public static String mainMethod() throws IOException, InterruptedException{
			methodResult = "Pass";
			
			Keywords.dualOutput("Topic Selection Page launched", null);
			ReportUtil.addStep(currentDatasheet+" Topic Selection Page ","","Pass", null);
			
			Thread.sleep(1000L);
			System.out.println(getText());
			
/*			Based on discussion with Imran and David, the following are not needed.
			System.out.println(playList());
			System.out.println(organize());
			System.out.println(TestUtil.VerifyJasperFooter());
			System.out.println(verifyHeader());
*/			
			
			return methodResult;
		}
			
	
		public static String getText() throws IOException, InterruptedException{
			methodResult = "Pass";

			int subCount = 1; int rowNum = 2; 

			String subject = ""; String topic = ""; String subTopic = "";String usage = "";
			do {
				int topicCount = 1;
				subject = Keywords.getCustomObjectText(TestUtil.getStringValueinArray(OR, "Topic_Main_Start", "Key")+subCount+TestUtil.getStringValueinArray(OR, "Topic_Main_End", "Key"));
				Keywords.clickbyXpath(TestUtil.getStringValueinArray(OR, "Topic_Main_Start", "Key")+subCount+TestUtil.getStringValueinArray(OR, "Topic_Main_End", "Key"));

				do {
					int subTopCount = 1;

					topic = Keywords.getCustomObjectText(TestUtil.getStringValueinArray(OR, "TopicSel_Topic_Start", "Key")+subCount+TestUtil.getStringValueinArray(OR, "TopicSel_Topic_Mid", "Key")+topicCount+TestUtil.getStringValueinArray(OR, "TopicSel_Topic_End", "Key"));
					//usage = Keywords.getCustomObjectText(TestUtil.getStringValueinArray(OR, "TopicSel_Usage_Start", "Key")+subCount+TestUtil.getStringValueinArray(OR, "TopicSel_Usage_Mid", "Key")+topicCount+TestUtil.getStringValueinArray(OR, "TopicSel_Usage_End", "Key"));
					//int last = usage.indexOf("of");
					Keywords.clickbyXpath(TestUtil.getStringValueinArray(OR, "TopicSel_Topic_Start", "Key")+subCount+TestUtil.getStringValueinArray(OR, "TopicSel_Topic_Mid", "Key")+topicCount+TestUtil.getStringValueinArray(OR, "TopicSel_Topic_End", "Key"));
					Thread.sleep(1500L);
					do {

						subTopic = Keywords.getCustomObjectText(TestUtil.getStringValueinArray(OR, "TopicSel_SubTopic_Start", "Key")+subTopCount+"]");
						d.setCellData(currentDatasheet, "SubTopic", rowNum, subTopic);
						d.setCellData(currentDatasheet, "Topic", rowNum, topic);
						d.setCellData(currentDatasheet, "Subject", rowNum, subject);
						
						subTopCount++;rowNum++;
					}while (d.getCellData(currentDatasheet, "Topic", rowNum-1).equals(d.getCellData(currentDatasheet, "Topic", rowNum)) );
					topicCount++;
				}while (d.getCellData(currentDatasheet, "Subject", rowNum-1).equals(d.getCellData(currentDatasheet, "Subject", rowNum)) );
				subCount++; 
			}while (subCount <=4 );
			
			return methodResult;
		}
		
		public static String playList() throws IOException, InterruptedException{
			methodResult = "Pass";
			
			int row =2;
			int totalRows = d.getRowCount(currentDatasheet);
			int noofYES = d.getNoOfMatches(currentDatasheet, "Response", "Y");
			int counter = 0;
			do {
				Thread.sleep(1000L);
				if (d.getCellData(currentDatasheet, "Response", row).equals("Y")){
					
					Keywords.clickbyXpath(TestUtil.getStringValueinArray(OR, "TopicSel_Topic_Start", "Key")+"1"+TestUtil.getStringValueinArray(OR, "TopicSel_Topic_Mid", "Key")+(row-1)+TestUtil.getStringValueinArray(OR, "TopicSel_Topic_End", "Key"));
					
					Keywords.clickbyXpath(TestUtil.getStringValueinArray(OR, "TopicSel_SubTopic_Start", "Key")+(row-1)+"]");
					counter++;
				}
				row++;
						}while (counter < noofYES);
			System.out.println("Before playlist");
			String playList = "PlayList";
			Keywords.inputValue("TopicSel_PlayList_Name", playList);
			
			Keywords.clickButton("TopicSel_Go_Link");
			System.out.println("After GO");
			
			int rowNum = 1;
			do {
				Thread.sleep(3000L);
				Keywords.verifyObjectTextWithParameter("TstPg_Header_Exam_Text", "Evolution"+playList);
				verifyTestPageFooters(rowNum);

				verifyAdditionalTestPageFooters(rowNum);
				
			}while(row <= 4);
			//	keywords.
			
			
			return methodResult;
		}
		
		public static String organize() throws IOException, InterruptedException{
			methodResult = "Pass";
			int row =2;
			int count = d.getRowCount(currentDatasheet);
		//	int respCount = d.getNoOfMatches(currentDatasheet, "Response", "Y");
			System.out.println("count ===>" + count);
			do {
				Thread.sleep(1000L);
				if (d.getCellData(currentDatasheet, "Response", row).equals("Y")){
					
					Keywords.clickbyXpath(TestUtil.getStringValueinArray(OR, "TopicSel_Topic_Start", "Key")+"1"+TestUtil.getStringValueinArray(OR, "TopicSel_Topic_Mid", "Key")+(row-1)+TestUtil.getStringValueinArray(OR, "TopicSel_Topic_End", "Key"));					
					Keywords.clickbyXpath(TestUtil.getStringValueinArray(OR, "TopicSel_SubTopic_Start", "Key")+(row-1)+"]");
				}
				row++;
				System.out.println("row ===>" + row);

			}while (row <= count);
						
			Keywords.clickbyXpath(TestUtil.getStringValueinArray(OR, "TopicSel_Org_Ele1", "Key"));

			Keywords.clickbyXpath(TestUtil.getStringValueinArray(OR, "TopicSel_Org_Up", "Key"));
			Keywords.clickbyXpath(TestUtil.getStringValueinArray(OR, "TopicSel_Org_Down", "Key"));
			Keywords.clickbyXpath(TestUtil.getStringValueinArray(OR, "TopicSel_Org_Clear", "Key"));
			Keywords.clickbyXpath(TestUtil.getStringValueinArray(OR, "TopicSel_Org_Remove", "Key"));
			
			return methodResult;
		
			
			
		}
		
		public static String verifyHeader() throws IOException{
			methodResult = "Pass";
			
			//Verify Logo
			Keywords.verifyObjectText("Jsp_Header_Logo");
			
			//Verify Item Review Page All Resources navigation link
			TestUtil.gotoAllResandItemReview("JspStart_Allres_link");
		
			//Verify other headers
			Keywords.verifyObjectText("TopicSel_Topic_Header");
			Keywords.verifyObjectText("TopicSel_Topic_Header_Num");
			Keywords.verifyObjectText("TopicSel_SubTopic_Header");
			Keywords.verifyObjectText("TopicSel_SubTopic_Header_Num");
			Keywords.verifyObjectText("TopicSel_Org_Header");
			Keywords.verifyObjectText("TopicSel_Org_Header_Num"); 	
			Keywords.verifyObjectText("TopicSel_PlayLst_Header");
			Keywords.verifyObjectText("TopicSel_PlayLst_Header_Num"); 
			
			//Verify text displayed
			Keywords.verifyObjectText("TopicSel_Header_Text_Left");
			Keywords.verifyObjectText("TopicSel_Header_Text_Right");

			


			return methodResult;
			
		}
		
		public static String verifyTestPageFooters(int rowNum) throws IOException{
			Keywords.dualOutput("Executing Test Page Footers", null);
			submethodL1Result = "Pass";	int noofMatches;
				
			//noofMatches = d.getNoOfMatches(currentDatasheet, "Section", d.getCellData(currentDatasheet,"Section", rowNum));

			//verify Chapter List
			Keywords.checkContains("VwPg_Chapter_List", "src","Chapters button");
		
			//verify the text CH
			Keywords.verifyObjectText("TstPg_Footer_CH");
			
			//verify 1 of 30
		//	String itemNum = vc.strToDblToStr(d.getCellData(currentDatasheet, "Item_Num", rowNum));
		//	Keywords.verifyObjectTextWithParameter("TstPg_Footer_QueNum_Text",itemNum + " of "+noofMatches);	

			//verify chapter name
			Keywords.verifyObjectTextWithParameter("VwPg_Footer_ChapName",d.getCellData(currentDatasheet, "Topic", rowNum));	

			//verify Review ID
		//	Keywords.verifyObjectTextWithParameter("VwPg_Footer_FeedbackID_Text",d.getCellData(currentDatasheet, "Review_ID", rowNum));	

			
			//verify back button
			if(currentDataXL.contains("ReviewChapters") ) {
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

		
}
