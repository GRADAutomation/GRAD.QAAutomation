package TestSuiteMisc;

import org.testng.annotations.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import Database.Excel_Ops;
import ParentClasses.DriverScript;
import Reports.ReportUtil;
import Utility.Keywords;
import Utility.TestUtil;
import Utility.Variable_Conversions;

public class QuizBank extends DriverScript {

	public static Excel_Ops d = null;
	public static Excel_Ops AllRes = null;
	public static Utility.Variable_Conversions vc = null;
	
	@Test
	public static String completeFlowTest(String sheetName) throws IOException, InterruptedException{
		
		APPLICATION_LOGS.debug("Inside QB");
		d= new Excel_Ops(System.getProperty("user.dir")+"\\src\\Config\\"+currentDataXL);
		AllRes= new Excel_Ops(System.getProperty("user.dir")+"//src//Config//AllRes.xlsx");
		vc = new Variable_Conversions();
		classResult = "Pass";
		
		if (TestUtil.jasperLogin().equals("Pass")) {
			
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			
		//	if (!currentBrowser.equals("IE") && !currentBrowser.equals("Chrome"))
		//		Keywords.frameSwitch("main");
			
			driver.manage().timeouts().pageLoadTimeout(180, TimeUnit.SECONDS);
			
			int index = vc.strToDblToInt(AllRes.getCellData(currentProduct, "Index", AllRes.getFirstRowInstance(currentProduct, "TestName", currentTestName.trim())));
			
			if(currentBrowser.equals("Firefox")){
				Keywords.frameSwitch("main");
				Keywords.clickLinkText(currentTestName.trim());
			}else {
				Thread.sleep(12000L);
				Keywords.frameSwitch("defaultContent");
				Keywords.frameSwitch("main");
				Keywords.clickbyXpath("html/body/div[1]/div[3]/div[2]/div[1]/div/section/table/tbody/tr["+String.valueOf(index+1)+"]/td[2]/a");
			}
			
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			//Keywords.frameSwitch("main");
			
			//Labels method verifies all text on the page
			if(verifyLabels().equals("Pass")){
				APPLICATION_LOGS.debug("QuizBank first page: All labels/ fields text is validated successfully");
				ReportUtil.addStep("QuizBank first page: Verify all labels", "Validation successful", "Pass", null);
			} else {
				APPLICATION_LOGS.debug("QuizBank first page: Some / all the labels/ fields text is NOT correct");
				ReportUtil.addStep("QuizBank first page: Verify all labels", "NOT appeared as expected", "Fail", null);
			}
			
			//Check the functionality of Test Section 
			if(selectAll("Test Section").equals("Pass")){
				APPLICATION_LOGS.debug("All checkboxes under Test Section are functioning as expected");
				ReportUtil.addStep("Verify QuizBank checkbox functionality: names, id's and Select All", "Functioning as expected", "Pass", null);
			} else {
				APPLICATION_LOGS.debug("All / some checkboxes under Test Section are NOT functioning as expected");
				ReportUtil.addStep("Verify QuizBank checkbox functionality: names, id's and Select All", "NOT functioning as expected", "Fail", null);
			}
				
			//Check the functionality of Question Type 
			if(selectAll("Question Type").equals("Pass")){
				APPLICATION_LOGS.debug("All checkboxes under Question Type are functioning as expected");
				ReportUtil.addStep("Verify QuizBank checkbox functionality: names, id's and Select All", "Functioning as expected", "Pass", null);
			} else {
				APPLICATION_LOGS.debug("All / some checkboxes under Question Type are NOT functioning as expected");
				ReportUtil.addStep("Verify QuizBank checkbox functionality: names, id's and Select All", "NOT functioning as expected", "Fail", null);
			}
			
			//Validate correct # of questions on each sub-category - Takes input from QB spreadsheet 
			if(validateNofQuestions().equals("Pass")){
				APPLICATION_LOGS.debug("Available no of questions on each sub-category is correct");
				ReportUtil.addStep("Validate # of questions on each sub-category listed", "All are correct", "Pass", null);
			} else {
				APPLICATION_LOGS.debug("No of questions of some/ all sub-category sections are incorrect per QuizBank spreadsheet");
				ReportUtil.addStep("Validate # of questions on each sub-category listed", "Some/all incorrect", "Fail", null);
			}
			
			// THIS IS NOT NEEDED AS THE NUMBER OF QUESTIONS FUNCTIONALITY IS NOT WORKING CORRECTLY
			//User selections method - Takes input from QB spreadsheet 
/*			if(userSelections().equals("Pass")){
				APPLICATION_LOGS.debug("All selections have been made as per QuizBank spreadsheet");
				ReportUtil.addStep("Select options on QuizBank page as per data sheet", "All selections are made", "Pass", null);
			} else {
				APPLICATION_LOGS.debug("Some/ all selections are NOT made as per QuizBank spreadsheet");
				ReportUtil.addStep("Select options on QuizBank page as per data sheet", "Some/all selections are not made", "Fail", null);
			}
*/		
			//Finally SelectAll, that's the only way questions are being selected
			Keywords.clickCheckBox("QB_FS_SelectAll_TestSection");
			Keywords.clickCheckBox("QB_FS_SelectAll_QuestionType");
			Keywords.clickButton("QB_FS_Calculate_Button");
			
			//Make selections and hit Create Assignment
			Keywords.clearandinput("QB_FS_title_Input");
			Keywords.clearandinput("QB_FS_UserQueNum_Input");
			Keywords.clickButton("QB_FS_CreateAssign_Button");
			
			testPages();
		}
		return classResult;
	}
		
	//method to check the select all functionality
	public static String selectAll(String section) throws IOException{
		methodResult = "Pass"; String id = null; String chkbxName = null;
		
		int rowNum = d.getFirstRowInstance(currentDatasheet, "Section", section);
		
		//click on select all checkbox
		Keywords.clickCheckBox("QB_FS_SelectAll_"+section.replace(" ", ""));
		
		do{
			
			id = vc.strToDblToStr(d.getCellData(currentDatasheet, "id", rowNum));
			chkbxName = d.getCellData(currentDatasheet, "Text-to-Verify", rowNum);
			
			//verify if checkbox is selected
			if(Keywords.isCheckBoxCheckedWithParameters("id", id, "", "", ""))
				APPLICATION_LOGS.debug("Checkbox with id# "+id+" is selected as expected");
			else{
				APPLICATION_LOGS.debug("Checkbox with id# "+id+" is NOT selected which is a defect");
				
				methodResult = "Fail";
				fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_id"+id+".jpg";
				TestUtil.takeScreenShot(screenshotPath+fileName);
				ReportUtil.addStep("Verify if checkbox with id# "+ id+" is selected", "Checkbox is NOT selected", "Fail", screenshotPath+fileName);
			}
			
			//verify if checkbox text  is displayed as expected
			Keywords.verifyCustomObjectText("//*[@id='"+id+"']", chkbxName);
			
			rowNum++;
	
		} while (section.equals(d.getCellData(currentDatasheet, "Section", rowNum)));
		
		//click on select all checkbox again to revert changes
		Keywords.clickCheckBox("QB_FS_SelectAll_"+section.replace(" ", ""));
		
		return methodResult;
		
	}
	
	public static String userSelections() throws IOException{
		methodResult = "Pass"; 
		APPLICATION_LOGS.debug("Inside userSelections");
		int rowNum; String type; String objName; String id; String response;
		
		for (rowNum = 2; rowNum <= (d.getRowCount(currentDatasheet)); rowNum++) {
			type = d.getCellData(currentDatasheet, "Type", rowNum);
			objName = d.getCellData(currentDatasheet, "objName", rowNum);
			id = vc.strToDblToStr(d.getCellData(currentDatasheet, "id", rowNum));
			response = d.getCellData(currentDatasheet, "Response", rowNum);
			
			if(response.equals("Yes") || response.equals("Y") ) {
				if (!objName.equals("Custom")){
					if (type.equals("Radio"))
						Keywords.clickRadioButton(objName);
					else if (type.equals("CheckBox")) // NEED TO ADD LOGIC TO NOT TO CHECK IF ALREADY CHECKED.. DEPENDING ON ISSELECTED - AFTER DEBUGGING
						Keywords.clickCheckBox(objName);
					else if (type.equals("Input"))
						Keywords.clearandinput(objName);
				}else{
					if (type.equals("Radio"))
						Keywords.clickRadioButtonCustom(objName);
					else if (type.equals("CheckBox"))
						Keywords.clickCheckBoxCustom2("id",id,"","","");
					else if (type.equals("Input"))
						Keywords.clearandinput(objName);
				}	
			}	 // end of if
		} //end of for
		APPLICATION_LOGS.debug("Completed the method: userSelections");
		
		return methodResult;
	}
	
	public static String verifyLabels() throws IOException {
		methodResult = "Pass";
		
		//Check the labels in the first section : 01. Test Style, Timed/Tutor modes
		Keywords.verifyObjectText("QB_FS_01_Text");
		Keywords.verifyObjectText("QB_FS_TestStyle_Text");
		Keywords.verifyObjectText("QB_FS_TimedMode_Text");
		Keywords.verifyObjectText("QB_FS_TutorMode_Text");
		
		//Check the labels in the second section : 02. Question re-use mode, Unused / All Itesms
		Keywords.verifyObjectText("QB_FS_02_Text");
		Keywords.verifyObjectText("QB_FS_QueReUse_Text");
		Keywords.verifyObjectText("QB_FS_AllItems_Text");
		Keywords.verifyObjectText("QB_FS_Unused_Text");
		
		//Check the labels in the third section : 03. Select Question Content, Test Selection & Select All
		Keywords.verifyObjectText("QB_FS_03_Text");
		Keywords.verifyObjectText("QB_FS_SelQueContent_Text");
		Keywords.verifyObjectText("QB_FS_TestSection_Text");
		Keywords.verifyObjectText("QB_FS_QueType_Text");
		Keywords.verifyObjectText("QB_FS_SelectAll_TestSection");
		Keywords.verifyObjectText("QB_FS_SelectAll_QuestionType");
		
		//Check the labels in the fourth section : 04, Number of available questions, Available questions
		Keywords.verifyObjectText("QB_FS_04_Text");
		Keywords.verifyObjectText("QB_FS_NoofAvailQue_Text");
		Keywords.verifyObjectText("QB_FS_AvailQueBottom_Text"); // NEED TO DO withParameter
		
		//Check the labels in the fifth section : 05.,  Create Assignment, Input Title Text
		Keywords.verifyObjectText("QB_FS_05_Text");
		Keywords.verifyObjectText("QB_FS_CreateAssign_Text");
		Keywords.verifyObjectText("QB_FS_InputTitle_Text");
		Keywords.verifyObjectText("QB_FS_TitleMaxChars_Text");
		
		//Check the labels in the fifth section: Input # of questions, max & criteria fit
		Keywords.verifyObjectText("QB_FS_InputQue_Text");
		Keywords.verifyObjectText("QB_FS_Quefit_Text");
		
		//Check the labels in the fifth section: Create Assignment, Required fields
		Keywords.verifyObjectText("QB_FS_QueUserInput_Text");
		Keywords.verifyObjectText("QB_FS_FieldsReq_Text");
		Keywords.checkContains("QB_FS_CreateAssign_Button", "value", "Create Assignment button");
		
		return methodResult;
		
	}
		
	public static void getText() throws IOException{
		
		int rowNum = 4;
		for (int i=1;i<=3;i++){
			for (int j =1; j<=3; j++){
				int k =1;
				do{
					String objName = "html/body/div[1]/div[3]/div[2]/div[9]/div/div/div[1]/div[3]/div/section/div[1]/ul/li/ul/li["+i+"]/ul/li["+j+"]/ul/li["+k+"]/label";
					if(Keywords.elementDisplayedCustom(objName)){
						d.setCellData(currentDatasheet, "Subsection", rowNum, Keywords.getCustomObjectText(objName));
						d.setCellData(currentDatasheet, "id", rowNum, Keywords.getCustomAttribute(objName, "id"));
						rowNum++; k++;
					}
					else
						break;
				}while(k<20);
			}
		} // end of for main
		
	}
	
	public static String validateNofQuestions() throws IOException {
		methodResult = "Pass";
		int rowNum = 2; String queNum = null; String id = null; String subCategory = null;
		int maxRows = d.getRowCount(currentDatasheet);
		
		while (rowNum <= maxRows){
			subCategory = d.getCellData(currentDatasheet, "Sub-Category", rowNum);
			if(!subCategory.equals("")) {
				id = vc.strToDblToStr(d.getCellData(currentDatasheet, "id", rowNum));
				Keywords.clickCheckBoxCustom2("id", id, "", "", "");
				Keywords.clickButton("QB_FS_Calculate_Button");
				queNum = vc.strToDblToStr(d.getCellData(currentDatasheet, "Questions", rowNum));
				if (Keywords.verifyObjectTextWithParameter("QB_FS_AvailQueBottom_Text", "AVAILABLE QUESTIONS: "+queNum).equals("Pass"))
					APPLICATION_LOGS.debug("Sub-category ID# "+id+" with name- "+subCategory+" has correct # of questions");
				else{
					APPLICATION_LOGS.debug("Sub-category ID# "+id+" with name- "+subCategory+" does NOT have correct # of questions");
					fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_id"+id+".jpg";
					TestUtil.takeScreenShot(screenshotPath+fileName);
					ReportUtil.addStep("Verify if Sub-category ID# "+id+" with name- "+subCategory+" has correct # of questions", "Incorrect # of questions", "Fail", screenshotPath+fileName);
				}
				//d.setCellData(currentDatasheet, "Questions", rowNum, Keywords.getObjectText("QB_FS_NoofQuestions_Text"));
				Keywords.clickCheckBoxCustom2("id", id, "", "", "");
			}
			rowNum++;
		}
		return methodResult;	
		
	}
	
	public static void testPages() throws IOException, InterruptedException {
		driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
		Thread.sleep(2000L);
		if(Keywords.verifyObjectText("TstBeg_Footer_TD_Text").equals("Pass")){
			//Once in Test Directions page, click on Start button
			Keywords.clickButton("TstBeg_Body_Start_Img");
			
			//click REVIEW and go to Sub-Review page
			Thread.sleep(3000L);
			Keywords.frameSwitch("defaultContent");
			Keywords.clickButton("QB_TP_Review_button");
			driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
			Thread.sleep(2000L);
			
			//Verify review page title
			Keywords.verifyObjectText("QB_TP_ItemReview_Text");
			Keywords.clickButton("QB_TP_Exit_button");
			Keywords.verifyText(TestUtil.closeAlertandgetText(),TestUtil.getStringValueinArray(OR,"QB_TP_Alert_Msg","Value"));
			
			//Verify Item review page 
			driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
			Thread.sleep(2000L);
			Keywords.verifyObjectTextWithParameter("ItmRw_Body_ItmRwTest_Text",TestUtil.getStringValueinArray(OR,"QB_FS_title_Input","Value"));
			Keywords.clickLink("ItmRw_Nav_AllRes_Link");
			
			
		}
		
	}


}
