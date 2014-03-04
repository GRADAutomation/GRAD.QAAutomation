package TestSuiteFPT;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import Database.Excel_Ops;
import ParentClasses.DriverScript;
import Reports.ReportUtil;
import Utility.Keywords;
import Utility.TestUtil;
import Utility.Variable_Conversions;

public class FPT_Onsite extends DriverScript{
	
	public static Excel_Ops d = null;
	public static Utility.Variable_Conversions vc = null;


	public static String completeFlowTest(String sheetName) throws IOException, InterruptedException{
		Keywords.dualOutput("Executing FPT DAT Test Script", null);
		
		d= new Excel_Ops(System.getProperty("user.dir")+"\\src\\Config\\"+currentDataXL+".xlsx");
		vc = new Variable_Conversions();
		classResult = "Pass";
		
		try {
			
			Keywords.navigatebyURL(controller.getCellData("Settings", "Login", controller.getFirstRowInstance("Settings", "Parameter","FPT_URL")));
			if(!currentBrowser.contains("Safari"))
				driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			else
				Thread.sleep(3000L);
			
			String EID_Value = vc.strToDblToStr(d.getCellData("Input", "EID_Value", d.getFirstRowInstance("Input", "Sheet_Name", currentDatasheet)));
			//The following two are for DAT
			String perceptualAbilityScore = "Perceptual Ability : 30";	String academicAverageScore = "Academic Average : 29";
			//The following is for OAT
			String totalScienceScore = "Total Science : 400";
			//The following 6 variables are for PCAT
			String verbalAbilityScore = "Verbal Ability : 500"; String readingCompScore = "Reading Comp : 515"; String biologyScore = "Biology : 500";
			String quantAbilityScore = "Quant. Ability : 525"; String chemistryScore = "Chemistry : 530"; String overallScore = "Overall Score : 514";
			//The following are for LSAT
			String scaleScore = "Scaled Score : 180";
			//The following are for MCAT
			String totalScore = "Total Score : 45"; String psScore = "Physical Sciences : 15";
			String vrScore = "Verbal Reasoning : 15"; String bioScore = "Biological Sciences : 15";
			//The following are for GRE
			String quantGRE = "Quantitative Score : 150"; String verbalGRE = "Verbal Score : 145";
			
			// Enter Enrollment ID or without Enrollment ID Page =======================================================================
			// Test without EID
				if (EID_Value.equals(""))  {
								
					// Click on Don't Know My Enrollment ID button
					Keywords.clickButton("FPT_Login_EnrollmentID_Button");

					// Entering the details and clicking Enroll button
					Keywords.inputValue("FPT_Login_Fname_Input", d.getCellData("Input", "Fname", d.getFirstRowInstance("Input", "Sheet_Name", currentDatasheet)));
					Keywords.inputValue("FPT_Login_Lname_Input", d.getCellData("Input", "Lname", d.getFirstRowInstance("Input", "Sheet_Name", currentDatasheet)));
					Keywords.clear("FPT_Login_Email_Input");
					Keywords.inputValue("FPT_Login_Email_Input", d.getCellData("Input", "Email", d.getFirstRowInstance("Input", "Sheet_Name", currentDatasheet)));
					Keywords.selectfromSelectList("FPT_Login_Product_SelectList",d.getCellData("Input", "Product_Selection", d.getFirstRowInstance("Input", "Sheet_Name", currentDatasheet)));
					Keywords.clickButton("FPT_Login_Enroll_Button");
				}
				else {
					// Test with EID
					Keywords.inputValue("FPT_Login_EnrollmentID_Input", EID_Value);
					
					// Click on the Enter button
					Keywords.dualOutput(" with EID ", EID_Value);
					
					if (!currentBrowser.equals("Ipad") && !currentBrowser.equals("Safari")) // added by Siva 12/13/13
						driver.manage().timeouts().pageLoadTimeout(4, TimeUnit.SECONDS);
					Keywords.clickButton("FPT_Login_EnrollmentID_Enter_button");
				}

				Thread.sleep(3000L);

			
			if (!currentBrowser.equals("Ipad") && !currentBrowser.equals("Safari")) // added by Siva 12/13/13
				driver.manage().timeouts().pageLoadTimeout(300, TimeUnit.SECONDS);
			
			int rowNum = 2; int secCounter = 1;
			int noofSections = vc.strToDblToInt(d.getCellData("Input", "Sections", d.getFirstRowInstance("Input", "Sheet_Name", currentDatasheet)));
			
			do {				
				if (!currentBrowser.equals("Ipad") && !currentBrowser.equals("Safari")) // added by Siva 12/13/13
					driver.manage().timeouts().pageLoadTimeout(300, TimeUnit.SECONDS);
				
				//Check for "DAT Practice Test"
				Keywords.verifyTitle(d.getCellData("Input", "TestTitle", d.getFirstRowInstance("Input", "Sheet_Name", currentDatasheet)));
			
				// Get Section Title & verify the same
				String currentSecTitle = d.getCellData("Input", "Title"+secCounter, d.getFirstRowInstance("Input", "Sheet_Name", currentDatasheet));
				String currentSection = d.getCellData("Input", "Section"+secCounter, d.getFirstRowInstance("Input", "Sheet_Name", currentDatasheet));
				if(Keywords.verifyObjectTextWithParameter("FPT_SecPg_Heading", currentSecTitle).equals("Pass")){
					Keywords.dualOutput("On correct page: ",currentSecTitle);
					ReportUtil.addStep("FPT "+currentDatasheet+" script - "+currentSecTitle, "On Correct Section", "Pass", null);
				}
				else {
					Keywords.dualOutput("On In-correct page, supposed to be on: ",currentSecTitle);
					fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+currentSecTitle+".jpg";
					TestUtil.takeScreenShot(fileName);
					ReportUtil.addStep("FPT "+currentDatasheet+" script - "+currentSecTitle, "On In-Correct Section", "Fail", screenshotPath+fileName);
				}
				
				Keywords.frameSwitch("defaultContent");
				Keywords.frameSwitch("activeElement");
				int noofQueinSection = d.getNoOfMatches(currentDatasheet, "Section", currentSection);
						
				// Verify Q-id & Fill in the bubble	and/or enter text	 Note: GRE text inputting works only for the first text box if there are multiple to be entered sequentially
				if(!currentSection.equals("Writing")) {
					for (int x = 1; x <= noofQueinSection; x++)  { 
						
						//Verify Q id
						Keywords.verifyCustomObjectText("//*[@id='grid"+secCounter+"']/table/tbody/tr[" + x + "]/td[1]/span", vc.strToDblToStr(d.getCellData(currentDatasheet, "Qid", rowNum)));
						
						//Input text
						if(d.getCellType(currentDatasheet, "InputAns", rowNum).equals("String")) {
							//Keywords.keysOperation("//*[@id='grid"+secCounter+"']/table/tbody/tr[" + (x+1) + "]/td[1]/span", "TAB");
							Keywords.customInputValue("//*[@id='grid"+secCounter+"']/table/tbody/tr[" + x + "]/td[2]/input", d.getCellData(currentDatasheet, "InputAns", rowNum));
						} else{
							String temp = vc.strToDblToStr(d.getCellData(currentDatasheet, "InputAns", rowNum));
							if (temp.equals("1") || temp.equals("2") || temp.equals("3") || temp.equals("4") || temp.equals("5")) // sometimes GRE would input numbers also in the textboxes, hence this if loop
								Keywords.clickbyXpath("//*[@id='grid"+secCounter+"']/table/tbody/tr[" + x + "]/td[2]/fieldset/p[" + temp  + " ]/label/span");
							else
								Keywords.customInputValue("//*[@id='grid"+secCounter+"']/table/tbody/tr[" + x + "]/td[2]/input", temp);
						}
							
						rowNum++;
					}
					Keywords.dualOutput("    - Bubble has been filled in.", null );		
				}
				
				//Verify Previous button
				if (secCounter != 1)
					Keywords.elementDisplayed("FPT_SecPg_Previous_Button");
				
				// Click Next button for the next Section
				if (secCounter < noofSections) {
					Keywords.clickButton("FPT_SecPg_Next_Button");
					Keywords.frameSwitch("defaultContent");
				}
				secCounter++;
			} while (secCounter <= noofSections);

			
	     // Click Score My Test button
			Keywords.clickButton("FPT_ScoreTest_Button");
				
			if (!currentBrowser.equals("Ipad") && !currentBrowser.equals("Safari")) // added by Siva 12/13/13
				driver.manage().timeouts().pageLoadTimeout(600, TimeUnit.SECONDS);
			Keywords.frameSwitch("defaultContent");
			
			
		 // Submit Score Test Page
			Keywords.verifyObjectTextWithParameter("FPT_ScoreTest_Header", d.getCellData("Input", "scoreTitle", d.getFirstRowInstance("Input", "Sheet_Name", currentDatasheet)));
			
		// Click Yes button to Submit for scoring
			Keywords.clickButton("FPT_ScoreTest_Yes_Button");	
			
			if (!currentBrowser.equals("Ipad") && !currentBrowser.equals("Safari")) // added by Siva 12/13/13
				driver.manage().timeouts().pageLoadTimeout(2, TimeUnit.SECONDS);
			  
		// Enter keycode practice smarter	
			Keywords.dualOutput("On Enter Keyword Page ", null);
			
			if(Keywords.verifyObjectTextWithParameter("FPT_ScoreKeyword_Header", d.getCellData("Input", "KeywordTitle", d.getFirstRowInstance("Input", "Sheet_Name", currentDatasheet))).equals("Pass")){
				Keywords.inputValue("FPT_EnterKeyword_Input", "practice smarter");
				Keywords.dualOutput("    - Keyword been entered", null);
				
				Keywords.clickButton("FPT_EnterKeyword_Go_Button");
			} 				 
			
		// Scores Page
			Thread.sleep(1500L);
			Keywords.frameSwitch("defaultContent");
			Keywords.verifyObjectTextWithParameter("FPT_Scores_Header", d.getCellData("Input", "scoresTitle", d.getFirstRowInstance("Input", "Sheet_Name", currentDatasheet)));

			// Get Scores
			if (!currentBrowser.equals("Ipad") && !currentBrowser.equals("Safari")) // added by Siva 12/13/13
				driver.manage().timeouts().pageLoadTimeout(600, TimeUnit.SECONDS);
			Keywords.frameSwitch("defaultContent");
			    
			// Verify Scores
			if(currentDatasheet.equals("DAT") || currentDatasheet.equals("CDAT")) {
				Keywords.verifyObjectTextWithParameter("FPT_Scores_PercpAbilityPerc_Text", perceptualAbilityScore); 
				Keywords.verifyObjectTextWithParameter("FPT_Scores_AcademicAvgPerc_Text", academicAverageScore);
			} else if(currentDatasheet.equals("OAT"))
				Keywords.verifyObjectTextWithParameter("FPT_Scores_TotalSciPerc_Text", totalScienceScore);
			else if(currentDatasheet.equals("LSAT"))
				Keywords.verifyObjectTextWithParameter("FPT_Scores_ScaldScorPerc_Text", scaleScore);
			else if(currentDatasheet.equals("MCAT")) {
				Keywords.verifyObjectTextWithParameter("FPT_Scores_PhySciPerc_Text", psScore); 
				Keywords.verifyObjectTextWithParameter("FPT_Scores_VerbReasPerc_Text", vrScore);
				Keywords.verifyObjectTextWithParameter("FPT_Scores_BioSciPerc_Text", bioScore); 
				Keywords.verifyObjectTextWithParameter("FPT_Scores_TotalScorePerc_Text", totalScore);
			} else if(currentDatasheet.equals("PCAT")) {
				Keywords.verifyObjectTextWithParameter("FPT_Scores_VerbAbilPerc_Text", verbalAbilityScore); 
				Keywords.verifyObjectTextWithParameter("FPT_Scores_RadingCompPerc_Text", readingCompScore);
				Keywords.verifyObjectTextWithParameter("FPT_Scores_BioPerc_Text", biologyScore); 
				Keywords.verifyObjectTextWithParameter("FPT_Scores_QuantAbilPerc_Text", quantAbilityScore);
				Keywords.verifyObjectTextWithParameter("FPT_Scores_ChemPerc_Text", chemistryScore); 
				Keywords.verifyObjectTextWithParameter("FPT_Scores_OverallScorPerc_Text", overallScore);
			} else if(currentDatasheet.equals("GRE")) {
				Keywords.verifyObjectTextWithParameter("FPT_Scores_QuantGRE_Text", quantGRE); 
				Keywords.verifyObjectTextWithParameter("FPT_Scores_VerbGRE_Text", verbalGRE);
			}			
			return classResult;
		} catch(Throwable t){
			// error
			APPLICATION_LOGS.debug("Error with FPT"+currentDatasheet+" script, please verify");
			ReportUtil.addStep("FPT"+currentDatasheet+" script", "Not Successful", "Fail", null);
			classResult = "Fail";
			
			return classResult;
		}
	
	}
		

}
