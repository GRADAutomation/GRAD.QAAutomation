package TestSuiteFPT;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;

import Database.Excel_Ops;
import ParentClasses.DriverScript;
import Reports.ReportUtil;
import Utility.Keywords;
import Utility.TestUtil;
import Utility.Variable_Conversions;

public class FPT_CBT_MCAT extends DriverScript{
	
	public static Excel_Ops d = null;
	public static Utility.Variable_Conversions vc = null;


	public static String completeFlowTest(String sheetName) throws IOException, InterruptedException{
		Keywords.dualOutput("Executing FPT CBT Test Script", null);
		
		d= new Excel_Ops(System.getProperty("user.dir")+"\\src\\Config\\"+currentDataXL);
		vc = new Variable_Conversions();
		classResult = "Pass";
		
		try {
			
			Keywords.navigatebyURL(controller.getCellData("Settings", "Login", controller.getFirstRowInstance("Settings", "Parameter","FPT_URL")));
			Keywords.clickLinkText("Log In");	
			if (!currentBrowser.equals("Ipad") && !currentBrowser.equals("Safari")) 
				driver.manage().timeouts().pageLoadTimeout(300, TimeUnit.SECONDS);
			
			//Login & click button
			Keywords.inputValue("KPT_LoginID_Text", d.getCellData(currentDatasheet, "Login", 2));
			Keywords.inputValue("KPT_Pwd_Text", d.getCellData(currentDatasheet, "Pwd", 2));
			Keywords.clickButton("KPT_Login_Button");
			
			if (!currentBrowser.equals("Ipad") && !currentBrowser.equals("Safari")) 
				driver.manage().timeouts().pageLoadTimeout(300, TimeUnit.SECONDS);
			Thread.sleep(3000L);
			//String xpath = "//ul[contains(@class,"+ "\"" + "MCAT" + "\""+")]//input[4]";
			Keywords.clickButton(TestUtil.getStringValueinArray(OR, "FPT_Product_Go_Button_begin", "Key")+ "\"" +currentDatasheet+ "\"" +TestUtil.getStringValueinArray(OR, "FPT_Product_Go_Button_end", "Key"));
			
			MCAT();
			
			
			return classResult;
		} catch(Throwable t){
			// error
			APPLICATION_LOGS.debug("Error with FPT"+currentDatasheet+" script, please verify");
			ReportUtil.addStep("FPT"+currentDatasheet+" script", "Not Successful", "Fail", null);
			classResult = "Fail";
			
			return classResult;
		}
	
	}
	
	public static void MCAT() throws IOException, InterruptedException {
		
		
		//Selecting MCAT Diagnostic Test
		Keywords.clickButton("JspStart_Start_Button");
        Thread.sleep(1500L);
        Keywords.clickButton("TstBeg_Footer_Next_Button");
        Keywords.clickButton("TstBeg_Footer_Next_Button");

        // Seccounter to keep track of # of sections, Row for row count 
        //and SubItemId for question # sequence in each page
        int Seccounter = 0; int Row = 2;
        
        do {
        	
        	do {
        		int SubItemId = 1;
        		
        		driver.switchTo().frame("sequenceContentFrame");
        		//driver.switchTo().activeElement();
        		Thread.sleep(1500); //This is essential as otherwise script is running faster than the pageloadtime.
            
        		do {
        			//Reading the answers from input excel
        			String Answer = vc.strToDblToStr((d.getCellData(currentDatasheet, "InputAns", Row)));
        			String path = "subitem"+SubItemId+".singleAnswerMultipleChoice.answerChoiceRow"+Answer+".state0";
        			driver.findElement(By.id(path)).click();
        			Row++; SubItemId++;
        		} while((d.getCellData(currentDatasheet, "Pg", Row-1)).equals(d.getCellData(currentDatasheet, "Pg", Row)));
        		
        		//After finishing each section navigating to other sections
        		if((d.getCellData(currentDatasheet, "Section", Row-1)).equals("verbReas"))
        			Keywords.clickButton("TstPg_Footer_VerbalButton");
        		else
        			Keywords.clickButton("TstPg_Footer_OtherButton");

        	} while((d.getCellData(currentDatasheet, "Section", Row-1)).equals(d.getCellData(currentDatasheet, "Section", Row)));
        	
        	//If Biological Section then once finished submit the test
        	if((d.getCellData("Sheet1", "Section", Row-1)).equals("bioSci")){
        		Keywords.clickButton("SecRw_Footer_EndButton_LastSec");
                TestUtil.closeAlertandgetText();
        	}
        	//All other sections, navigate to other sections
        	else{
            	driver.findElement(By.id("SecRw_Footer_EndButton")).click();
            	TestUtil.closeAlertandgetText();
            	Keywords.clickButton("SecBrk_Footer_NextButton");
            	Keywords.clickButton("SecBrk_Footer_NextButton");
        	}
        	Seccounter++;
        } while (Seccounter<3);
        
        Thread.sleep(3000);
        //Verifying if the result displayed is of MCAT Diagnostic Test  
        /*
        try {
            assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*MCAT Diagnostic Exam[\\s\\S]*$"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        System.out.println("MCATTest: MCAT Diagnostic Test Successfully navigated");
	*/	
	}

}
