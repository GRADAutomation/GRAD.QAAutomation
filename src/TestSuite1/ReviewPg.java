package TestSuite1;

/**
 * This is JUnit test case for testing MCAT Diagnostic Test REVIEW page functionality
 * @author Resmi
 *
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.testng.annotations.Test;
import org.apache.log4j.Logger;	

import Database.Excel_Ops;
import ParentClasses.DriverScript;
import Reports.ReportUtil;
import Utility.Keywords;
import Utility.TestUtil;
import Utility.Variable_Conversions;

public class ReviewPg extends DriverScript{

	Excel_Ops d = null; Excel_Ops AllRes = null;
	Utility.Variable_Conversions vc = null;
	public static boolean getQAText;
	public static boolean isDiagORFL;

	@Test
	public String completeFlowTest(String sheetName) throws IOException, InterruptedException {
		methodResult = "Pass";
			 
		Keywords.dualOutput("Inside ReviewPageTest", sheetName);
		d = new Excel_Ops(System.getProperty("user.dir")+"\\src\\Config\\"+currentDataXL);
		vc = new Variable_Conversions();
		AllRes= new Excel_Ops(System.getProperty("user.dir")+"\\src\\Config\\AllRes.xlsx");
		
		if (currentCaseName.equals("getQAText"))
			getQAText = true;
		
		isDiagORFL = TestUtil.isDiagORFL();
			
		Thread.sleep(10000L);
				
		TestUtil.VerifyJasperHeader("Item Review");
		TestUtil.VerifyJasperFooter();
		verifyItemReviewPgText();
		HeaderLinks();
		VerifyScoresandTabs();
		VerifyPassageQuestions();
	
		return methodResult;
	}
	

	public String verifyItemReviewPgText() throws IOException {
		submethodL1Result = "Pass";
		if(isDiagORFL || currentDatasheet.contains("SciAss"))
			if(!currentDatasheet.contains("FL"))
				Keywords.verifyObjectTextWithParameter("ItmRw_Body_ItmRw_Text", "Item Review: "+currentTestName);
			else
				Keywords.verifyObjectTextWithParameter("ItmRw_Body_ItmRw_Text", "Item Review: "+currentTestName.replace("Kaplan", "MCAT"));
		else 
			Keywords.verifyObjectTextWithParameter("ItmRw_Body_ItmRw_Text", "Item Review: "+d.getCellData(currentDatasheet, "PageTitle", 2));

		return submethodL1Result;
	}
	
	
	public String HeaderLinks() throws IOException, InterruptedException{
		submethodL1Result = "Pass";
		
		//Verify Item Review Page navigation link
		if(isDiagORFL || currentDatasheet.contains("SciAss"))
			Keywords.clickLinkandVerify("ItmRw_Nav_Review_Link", "Jsp_Header_Pgtxt");
		else
			Keywords.clickLinkandVerify("ItmRw_Nav_Review_Link_Others", "Jsp_Header_Pgtxt");

		//Verify Item Review Page Trends navigation link -- only for Diag or FL
		if(isDiagORFL || currentDatasheet.contains("SciAss")){
			if(Keywords.clickLinkandVerify("ItmRw_Nav_Trends_Link", "Jsp_Header_Pgtxt").equals("Pass")){
				if(TestUtil.gotoAllResandItemReview("Trends_Nav_AllRes_Link").equals("Pass"))
					ReportUtil.addStep("Verify Trends nav link", "Verified Successfully", "Pass", null);
				else
					ReportUtil.addStep("Verify Trends nav link", "Verification failed", "Fail", screenshotPath+fileName);
			}
		}	
		
		//Verify Item Review Page Performance Summary navigation link		
		if(Keywords.clickLinkandVerify("ItmRw_Nav_Performance_Link", "Jsp_Header_Pgtxt").equals("Pass")){
			//Keywords.clickLink("ItmRw_Nav_Performance_Link");
			//TestUtil.gotoAllResandItemReview("ItmRw_Nav_AllRes_Link");
			//if(TestUtil.gotoAllResandItemReview("PS_Header_Logo").equals("Pass")) // change to PS_Header_Logo
			if(Keywords.clickLinkandVerify("PS_Nav_Review_Link", "Jsp_Header_Pgtxt").equals("Pass"))
				ReportUtil.addStep("Verify Performance Summary nav link", "Verified Successfully", "Pass", null);
			else
				ReportUtil.addStep("Verify Item Review nav link", "Verification failed", "Fail", screenshotPath+fileName);
		}
		//Verify Item Review Page All Resources navigation link
		//TestUtil.gotoAllResandItemReview("ItmRw_Nav_AllRes_Link");
	
		return submethodL1Result;
		
	}
	
	public String VerifyScoresandTabs() throws IOException, InterruptedException{
		//Verify Scores
		submethodL1Result = "Pass";
		System.out.println("Inside Verify Scores and tabs");

		int totalMatches = d.getNoOfMatches("ItemReview", "SheetName", currentDatasheet);
		int rowNum = 2; String objName = null;
		
		//driver.switchTo().defaultContent();
		//Keywords.frameSwitch("main");
		if(isDiagORFL) {
			Keywords.verifyObjectText("ItmRw_Score_TotalScore_label");
			Keywords.verifyObjectText("ItmRw_Score_Percentile_label");
		}else {
			Keywords.verifyObjectText("ItmRw_Score_Correct_label");
			Keywords.verifyObjectTextWithParameter("ItmRw_Score_PercentageCorrect", "100%");			
		}
		
		if(isDiagORFL) {
		while (totalMatches > 0){
			if(d.getCellData("ItemReview", "SheetName", rowNum).equals(currentDatasheet)){
				totalMatches--;
				if (d.getCellData("ItemReview", "ItmRwSection", rowNum).equals("total")){
					Keywords.verifyObjectTextWithParameter("ItmRw_Score_TotalScore", vc.strToDblToStr(d.getCellData("ItemReview", "Score", rowNum)));
					Keywords.verifyObjectTextWithParameter("ItmRw_Score_Percentile", d.getCellData("ItemReview", "Percentile", rowNum));
				} else{
					objName = TestUtil.getStringValueinArray(OR,"ItmRw_Score_SubsecScore_Start","Key")+ d.getCellData("ItemReview", "ItmRwSection", rowNum);
					Keywords.verifyCustomObjectText(objName+TestUtil.getStringValueinArray(OR,"ItmRw_Score_SubsecScore_End","Key"), vc.strToDblToStr(d.getCellData("ItemReview", "Score", rowNum)));
					Keywords.verifyCustomObjectText(objName+TestUtil.getStringValueinArray(OR,"ItmRw_Score_SubsecPercentile_End","Key"), d.getCellData("ItemReview", "Percentile", rowNum));
					Keywords.verifyCustomObjectText(objName+TestUtil.getStringValueinArray(OR,"ItmRw_Score_SubsecLabel_End","Key"), d.getCellData("ItemReview", "SectionLabel", rowNum));

					driver.switchTo().defaultContent();
					Keywords.frameSwitch("main");
					
					objName = TestUtil.getStringValueinArray(OR,"ItmRw_Sectiontab_Start","Key")+ d.getCellData("ItemReview", "ItmRwSection", rowNum)+TestUtil.getStringValueinArray(OR,"ItmRw_Sectiontab_End","Key");
					Keywords.verifyCustomObjectText(objName, d.getCellData("ItemReview", "TabLabel", rowNum));
				}
			}
			rowNum++;
		} //end of while loop
		}		
		return submethodL1Result;
		
	}
	
	public String VerifyPassageQuestions() throws IOException{
		submethodL1Result = "Pass";
		System.out.println("Inside VerifyPassageQuestion method");
		int ScreenRow = 1; int td=0; String answerPath = null; int RowNum = 2; 
		String expectedResult = null; String actualResult = null; String Section = null;
		boolean markactindi = false; boolean markexpindi = false;
		int MaxRows = d.getRowCount(currentDatasheet);

		
		while(RowNum <= MaxRows) {
			Section = d.getCellData(currentDatasheet, "Section", RowNum);
			Keywords.dualOutput(Section, null);
			if(!Section.equals("trial")) {
				
			if(isDiagORFL || currentDatasheet.contains("SciAss")){	
				if (d.getCellData(currentDatasheet, "Item_Num", RowNum).equals("1.0")){
					ScreenRow = 1;
					Keywords.clickbyXpath(TestUtil.getStringValueinArray(OR,"ItmRw_Sectiontab_Start","Key")+ Section+TestUtil.getStringValueinArray(OR,"ItmRw_Sectiontab_End","Key"));
				} //end of first if
			}	//end of second if	
		
			td = determinetd(RowNum);
			
			//Compare answer
			expectedResult = d.getCellData(currentDatasheet, "Expected_Result", RowNum);
			answerPath = "//table[@id='test-analysis-table']/tbody/tr["+ScreenRow+"]/td["+td+"]/span"; //changed from /tbody//tr[" to /tbody/tr["
			actualResult = Keywords.getCustomAttribute(answerPath,"class");
		
			if(expectedResult.equals(actualResult)){
				Keywords.dualOutput("Question # "+ScreenRow+" from ",Section+" has correct response. Testcase is passed.");
				//d.setCellData(currentDatasheet, "Actual_Answer", RowNum, "Pass");
			}
			else{
				Keywords.dualOutput("Question # "+ScreenRow+" from ",Section+" has incorrect response. Testcase is FAILED.");
				//d.setCellData(currentDatasheet, "Actual_Answer", RowNum, "Fail");
			}
			
			//Topic validation 
			if(currentDataXL.contains("PBT"))
				answerPath = "//table[@id='test-analysis-table']/tbody/tr["+ScreenRow+"]/td["+(td+2)+"]"; //changed from /tbody//tr[" to /tbody/tr["
			else
				answerPath = "//table[@id='test-analysis-table']/tbody/tr["+ScreenRow+"]/td["+(td+2)+"]/a"; //changed from /tbody//tr[" to /tbody/tr["
			actualResult = Keywords.getTextCustomElement(answerPath, "Item Review Topic");
			
			if(!getQAText){
				expectedResult = d.getCellData(currentDatasheet, "Topic", RowNum);
				if(expectedResult.trim().equals(actualResult.trim())){
					Keywords.dualOutput("Question # "+ScreenRow+" from ",Section+" has correct topic. Testcase is passed.");
					//d.setCellData(currentDatasheet, "Actual_Topic", RowNum, "Pass");
				}
				else{
					Keywords.dualOutput("Question # "+ScreenRow+" from ",Section+" has incorrect topic. Testcase is FAILED.");
					//d.setCellData(currentDatasheet, "Actual_Topic", RowNum, "Fail");
				}
			}else
				d.setCellData(currentDatasheet, "Topic", RowNum, actualResult);
			
			
			//Mark Indicator validation : Change the logic to test positive scenario only (if a question is marked YES in the spreadsheet, only then compare, otherwise no
			// We are not checking negative case as isDisplayed or isEnabled or similar functions are taking nearly 5 to 8 seconds to validate if that's present otherwise throwing an error
			if(currentTestSuite.equals("Suite1") || currentDatasheet.contains("SciAss")){
				if(d.getCellData(currentDatasheet, "Mark", RowNum).equals("Y") || d.getCellData(currentDatasheet, "Mark", RowNum).equals("Yes"))
					markexpindi = true;
				else
					markexpindi = false;
			
				if(markexpindi) {
					answerPath = "//table[@id='test-analysis-table']/tbody/tr["+ScreenRow+"]/td["+(td+3)+"]/span"; //changed from /tbody//tr[" to /tbody/tr["
					markactindi = Keywords.elementDisplayedCustom(answerPath);
		
					if(markexpindi ==markactindi)
						Keywords.dualOutput("Question # "+ScreenRow+" from ",Section+" is correctly MARKED. Testcase is passed.");
					else
						Keywords.dualOutput("Question # "+ScreenRow+" from ",Section+" is incorrectly MARKED. Testcase is FAILED.");
				} //end of markexpindi
				else
					Keywords.dualOutput("Question # "+ScreenRow+" from ",Section+" is NOT set for MARKED in excel, so not validating");
			}
			}//end of if loop to skip trial section
			ScreenRow++; td = 0; RowNum++;
		} //end of while loop
		return submethodL1Result;
	}
	

	public int determinetd(int rowNum){
		
		//d = new Excel_Ops(System.getProperty("user.dir")+"\\src\\Config\\"+currentDataXL);
		int td = 1;
		if(currentTestSuite.equals("Suite2"))
			td = 1;
		else if (d.getCellData(currentDatasheet, "PsgQuestion", rowNum).equals("1.0"))
			td = 2;
		else if (d.getCellData(currentDatasheet, "Passage", rowNum).equals("No"))
			td = 2;
		else 
			td = 1;
		
		return td;
	}

	
	public String contentVerify() throws IOException, InterruptedException {
		submethodL1Result = "Pass";
		
		int row = 2; int count = 1;
		String markedPath;	String actualResult; String itemPath;
		
		do {
			driver.switchTo().defaultContent();
			driver.switchTo().frame("main");
			
			markedPath = TestUtil.getStringValueinArray(OR,"ItmRw_Body_ItmRw_Marked_Start","Key")+count+TestUtil.getStringValueinArray(OR,"ItmRw_Body_ItmRw_Marked_End","Key");
			actualResult = Keywords.getCustomAttribute(markedPath, "class");
				
			itemPath = TestUtil.getStringValueinArray(OR,"ItmRw_Body_ItmRw_Topic_Start","Key")+count+TestUtil.getStringValueinArray(OR,"ItmRw_Body_ItmRw_Topic_Mid","Key")+"2"+TestUtil.getStringValueinArray(OR,"ItmRw_Body_ItmRw_Topic_End","Key");
	
			Keywords.getTextCustomElement(itemPath, d.getCellData(currentDatasheet, "Topic", row));
			Keywords.clickbyXpath(itemPath);
			
			//verify 1 of 30
			int noofMatches = d.getNoOfMatches(currentDatasheet, "Section", d.getCellData(currentDatasheet,"Section", row));
			Keywords.verifyObjectTextWithParameter("TstPg_Footer_QueNum_Text",count + " of "+noofMatches);	

			//verify chapter name
			Keywords.verifyObjectTextWithParameter("VwPg_Footer_ChapName",d.getCellData(currentDatasheet, "Topic", row));	

			Keywords.clickButton("VwPg_Footer_EndTest_Button");
			Thread.sleep(1000L);
			Keywords.verifyText(TestUtil.closeAlertandgetText(),TestUtil.getStringValueinArray(OR,"VwPg_Footer_EndTest_Button","Value"));
			Thread.sleep(1000L);

			count++;row++;
			
		}while((d.getCellData(currentDatasheet, "PageTitle", row-1)).equals(d.getCellData(currentDatasheet, "PageTitle", row)));
		
		if(submethodL1Result.equals("Pass"))
			ReportUtil.addStep("Verify Item Review Page", "All oages navigated", "Pass", null);
		else
			ReportUtil.addStep("Verify Item Review Page", "Some pages not navigated", "Fail", screenshotPath+fileName);
				
		return submethodL1Result;	
		
		}
}

