
	/* This class file is for SHP test pages to test FlashCards under Additional Resources
	 */

	package SHP;

	import org.openqa.selenium.Alert;
import org.testng.annotations.Test;

	import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

	import Database.Excel_Ops;
import ParentClasses.DriverScript;
import Reports.ReportUtil;
import Utility.Keywords;
import Utility.TestUtil;
import Utility.Variable_Conversions;

	public class SHPAddResFlashCards extends DriverScript{
		
		// All initialization
		public static Excel_Ops d = null;
		public static boolean completeRegression = false; // Sets to true for complete regression testing option chosen by user (in ControllerNew.xlsx)
		public static boolean  getQAText = false; // Sets to true to get text of question & answers when the option chosen by user (in ControllerNew.xlsx)
		

		//This method will be called by Driver Script parent class file using Java reflection, so the method name should be constant
		@Test
		public static String completeFlowTest(String sheetName) throws IOException, InterruptedException {

			//Initializing
			Keywords.dualOutput("Inside SHP CompleteflowTest" + sheetName, null);
			classResult = "Pass"; String result = null; getQAText = false;
			d= new Excel_Ops(System.getProperty("user.dir")+"/src/Config/"+currentDataXL);
			
			//Since the code is common for Complete regression, 
			//get QA Text and general flow regression, the following initialization is done
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
			methodResult = "Pass";
			
			driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);		
		
		    //Verify contents of SHP header 
			TestUtil.VerifySHPHeader();		
			//Verify shp footer
			TestUtil.VerifySHPFooter();	
		
			Keywords.clickLink("SHP_Syllabus_Add_Res_Link");		
			Thread.sleep(500L);
			String winHandleBefore = driver.getWindowHandle();	
			
			Keywords.clickLink("SHP_AddRes_FlashCards_Link");
			Thread.sleep(500L);
					
			//handling multiple windows
			for(String winHandleNew : driver.getWindowHandles()){
				driver.switchTo().window(winHandleNew);	
				System.out.println(winHandleNew);
			}
			
			Alert alert = driver.switchTo().alert();
			alert.accept();
			
			if(Keywords.verifyTitle(TestUtil.getStringValueinArray(OR, "SHP_AddRes_FlashCards_Title", "Value"))){
				ReportUtil.addStep("Verify Title "+TestUtil.getStringValueinArray(OR, "SHP_AddRes_FlashCards_Title", "Value"), "Title verified", "Pass", null);
			}
			
			FCHeaders();
			testFilters();
			testSubjects("first");
			//testGoToCards("Physics");

			driver.close();
			
			//Switching control back to main window
			driver.switchTo().window(winHandleBefore);
			
			//reporting
			if(methodResult.equals("Pass"))
				ReportUtil.addStep("Verify contents of Course Syllabus for "+currentTestName, "All contents are verified", "Pass", null);
			else
				ReportUtil.addStep("Verify contents of Course Syllabus for "+currentTestName, "Contents are verified but something went wrong", "Fail", null);
			
			return methodResult;
		} // end of mainMethod()
		
		//Method to validate FlashCard headers
		public static String FCHeaders() throws IOException, InterruptedException {
			
			submethodL1Result = "Pass";
			
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			Keywords.verifyObjectText("SHP_AddRes_FCHeader_FiltersLink");
			
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			Keywords.verifyObjectText("SHP_AddRes_FCHeader_SubjectsLink");
			
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			Keywords.verifyObjectText("SHP_AddRes_FCHeader_StatsLink");

			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			Keywords.verifyObjectText("SHP_AddRes_FCHeader_HelpLink");

			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			Keywords.verifyObjectText("SHP_AddRes_FCHeader_GoToLink");
			
			Keywords.checkContains("SHP_AddRes_FCHeader_Logo", "alt", "Logo on FlashCards");

			Thread.sleep(500L);
			
			return submethodL1Result;
		}
		
		//Method to test different flash card filters
		public static String testFilters() throws IOException, InterruptedException {
			
			submethodL1Result = "Pass";
			
			//click on filters link
			Keywords.clickLink("SHP_AddRes_FCHeader_FiltersLink");
			driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
			
			//Verify different status and text displayed on the page
			Keywords.verifyObjectText("SHP_AddRes_Filter_Title_Text");
			Keywords.verifyObjectText("SHP_AddRes_Filter_StatusTitle_Text");
			Keywords.elementDisplayed("SHP_AddRes_Filter_Know_CheckBox");
			Keywords.verifyObjectText("SHP_AddRes_Filter_Know_Text");
			Keywords.elementDisplayed("SHP_AddRes_Filter_Unsure_CheckBox");
			Keywords.verifyObjectText("SHP_AddRes_Filter_Unsure_Text");
			Keywords.elementDisplayed("SHP_AddRes_Filter_Unknown_CheckBox");
			Keywords.verifyObjectText("SHP_AddRes_Filter_Unknown_Text");
			Keywords.elementDisplayed("SHP_AddRes_Filter_Skip_CheckBox");
			Keywords.verifyObjectText("SHP_AddRes_Filter_Skip_Text");
			Keywords.verifyObjectText("SHP_AddRes_Filter_Reset_Text");
			Keywords.elementDisplayed("SHP_AddRes_Filter_Delete_Img");
			
			//Write reporting here if needed
				
			//Reset History
			Keywords.clickLink("SHP_AddRes_Filter_Reset_Text");
			
			return submethodL1Result;			
		} // end of testFilters()
		
		// This method is to test subject wise flashcards
		public static String testSubjects(String turn) throws IOException, InterruptedException {
			
			submethodL1Result = "Pass";
			int topicCount = 1; String textParamToVerify = null; int subTotalCards = 0;
			ArrayList<String> topicsList = new ArrayList<String>();
			topicsList = d.getDistinctValues(currentDatasheet, "Title");
			
			Keywords.clickLink("SHP_AddRes_FCHeader_SubjectsLink");
			driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
			
			Keywords.verifyObjectText("SHP_AddRes_Sub_Header_Text");
			
			while(topicCount < topicsList.size() ) {
				
				String objNamePrefix = TestUtil.getStringValueinArray(OR, "SHP_AddRes_Sub_Body_Start", "Key")+topicCount;
				int noofCards = d.getNoOfMatches(currentDatasheet, "Title", topicsList.get(topicCount-1));
				subTotalCards = noofCards + subTotalCards;
				
				//Verify checkbox
				Keywords.elementDisplayedCustom(objNamePrefix + TestUtil.getStringValueinArray(OR, "SHP_AddRes_Sub_CheckBox_End", "Key"));
				
				//Verify section label
				Keywords.verifyCustomObjectText(objNamePrefix + TestUtil.getStringValueinArray(OR, "SHP_AddRes_Sub_Topic_End", "Key"), topicsList.get(topicCount-1));
				
				//Verify number of cards
				Keywords.verifyCustomObjectText(objNamePrefix + TestUtil.getStringValueinArray(OR, "SHP_AddRes_Sub_CardsCount_End", "Key"), 
							d.getNoOfMatches(currentDatasheet, "Title", topicsList.get(topicCount-1))+" "+TestUtil.getStringValueinArray(OR, "SHP_AddRes_Sub_CardsCount_End", "Value"));
				
				//Logic to identify number of Known cards at the time of verification
				if (turn.equals("first"))
					textParamToVerify = String.valueOf(0);
				else
					textParamToVerify = String.valueOf(0);  //NEED TO IMPLEMENT YET
				
				//Verify number of known cards 
				Keywords.verifyCustomObjectText(objNamePrefix + TestUtil.getStringValueinArray(OR, "SHP_AddRes_Sub_KnownCount_End", "Key"), textParamToVerify);
				
				//Verify text KNOWN text
				Keywords.verifyCustomObjectText(objNamePrefix + TestUtil.getStringValueinArray(OR, "SHP_AddRes_Sub_Known_Text", "Key"), TestUtil.getStringValueinArray(OR, "SHP_AddRes_Sub_Known_Text", "Value"));
				
				//Select checkbox of respective section and verify the count.
				if (turn.equals("first")){
					if(Keywords.clickCheckBoxCustom2("xpath", objNamePrefix + TestUtil.getStringValueinArray(OR, "SHP_AddRes_Sub_CheckBox_End", "Key"),"","","").equals("Pass")) {
						Keywords.verifyObjectTextWithParameter("SHP_AddRes_Sub_TotalSubjects_Text", topicCount+ TestUtil.getStringValueinArray(OR, "SHP_AddRes_Sub_TotalSubjects_Text", "Key"));
						Keywords.verifyObjectTextWithParameter("SHP_AddRes_Sub_subTotalCount_Text", subTotalCards+ TestUtil.getStringValueinArray(OR, "SHP_AddRes_Sub_subTotalCount_Text", "Key"));
						Keywords.elementDisplayedCustom(objNamePrefix + TestUtil.getStringValueinArray(OR, "SHP_AddRes_Sub_Start_Button", "Key"));
					}
					else {
					//Need to report that there is a failure
					}
				}	
				topicCount++;
			} // end of while loop
			
			//Reset the checkboxes & GoToFunctionality
			if (turn.equals("first")){
				
				//Reset all checkboxes
				topicCount = 1;
				while(topicCount < topicsList.size() ) {
					String objNamePrefix = TestUtil.getStringValueinArray(OR, "SHP_AddRes_Sub_Body_Start", "Key")+topicCount;
					Keywords.clickCheckBoxCustom2("xpath", objNamePrefix + TestUtil.getStringValueinArray(OR, "SHP_AddRes_Sub_CheckBox_End", "Key"), "", "", "");
					topicCount++;
				}
				
				//GoTo Functionality
				topicCount = 1;
				while(topicCount < topicsList.size() ) {
					String objNamePrefix = TestUtil.getStringValueinArray(OR, "SHP_AddRes_Sub_Body_Start", "Key")+topicCount;
					Keywords.clickCheckBoxCustom2("xpath", objNamePrefix + TestUtil.getStringValueinArray(OR, "SHP_AddRes_Sub_CheckBox_End", "Key"), "", "", "");
					
					testGoToCards(topicsList.get(topicCount));
					
					topicCount++;
				}
				
			}

			Thread.sleep(500L);
			
			return submethodL1Result;
		}
		
		//Method to test GoToCards based on section title
		public static String testGoToCards(String currentTitle) throws IOException, InterruptedException {
			
			submethodL2Result = "Pass"; int fcCount = 1; int rowNum = 0; int tfcCount = 0; int rowLimit = 0;
			
			if(getQAText)
				rowNum = d.getRowCount(currentDatasheet)+1;
			else
				rowNum = d.getFirstRowInstance(currentDatasheet, "Title", currentTitle);
			
			Keywords.clickLink("SHP_AddRes_FCHeader_GoToLink");
			driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
			
			tfcCount = Integer.parseInt(Keywords.getObjectText("SHP_AddRes_FCGoTo_Tcard_Label"));
			rowLimit = rowNum + tfcCount;
			
			while(rowNum < rowLimit ) {
				
				if(!getQAText) {
					//Verify the following : Title, Current Flash Card number, Total Flashcards, Question Text
					Keywords.verifyObjectTextWithParameter("SHP_AddRes_FCGoTo_Title", d.getCellData(currentDatasheet, "Title", rowNum));
					Keywords.verifyObjectTextWithParameter("SHP_AddRes_FCGoTo_Ccard_Label", String.valueOf(fcCount)); // convert to String
					Keywords.verifyObjectTextWithParameter("SHP_AddRes_FCGoTo_Tcard_Label", String.valueOf(tfcCount)); // convert to String
					Keywords.verifyObjectTextWithParameter("SHP_AddRes_FCGoTo_Que_Text", d.getCellData(currentDatasheet, "Question_Text", rowNum));
				
					//verify the presence of buttons
					Keywords.elementDisplayed("SHP_AddRes_FCGoTo_Know_Button");
					Keywords.elementDisplayed("SHP_AddRes_FCGoTo_Unsure_Button");
					Keywords.elementDisplayed("SHP_AddRes_FCGoTo_DKnow_Img");
					Keywords.elementDisplayed("SHP_AddRes_FCGoTo_Skip_Img");
				} else {
					d.setCellData(currentDatasheet, "Title", rowNum,Keywords.getObjectText("SHP_AddRes_FCGoTo_Title"));
					d.setCellData(currentDatasheet, "Question_Text", rowNum,Keywords.getObjectText("SHP_AddRes_FCGoTo_Que_Text"));
				}
				
				//Verify the presence of flip-card image / button and also click on it
				if(Keywords.elementDisplayed("SHP_AddRes_FCGoTo_flip_Img").equals("Pass")){
					Keywords.clickButton("SHP_AddRes_FCGoTo_flip_Img");
					if(!getQAText)
						Keywords.verifyObjectTextWithParameter("SHP_AddRes_FCGoTo_Ans_Text", d.getCellData(currentDatasheet, "Answer_Text", rowNum));
					else
						d.setCellData(currentDatasheet, "Answer_Text", rowNum, Keywords.getObjectText("SHP_AddRes_FCGoTo_Ans_Text"));
				}
				
				//Switch - case logic to click on correct button choice
				if(!getQAText) {
					switch (d.getCellData(currentDatasheet, "Choice", rowNum)) {
					
						case "Know" :
							Keywords.clickButton("SHP_AddRes_FCGoTo_Know_Button");
							break;
						
						case "Unsure" :
							Keywords.clickButton("SHP_AddRes_FCGoTo_Unsure_Button");
							break;
						
						case "Don't Know" :
							Keywords.clickButton("SHP_AddRes_FCGoTo_DKnow_Img");
							break;
						
						case "Skip" :
							Keywords.clickButton("SHP_AddRes_FCGoTo_Skip_Img");
							break;
					
						case "" :
							Keywords.clickButton("SHP_AddRes_FCGoTo_Skip_Img");
							break;
					} //end of switch
				
					driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
				}
				rowNum++;
			}
			

			Thread.sleep(500L);
			
			return submethodL2Result;
		}
		
	}
