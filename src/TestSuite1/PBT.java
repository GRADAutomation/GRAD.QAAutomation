package TestSuite1;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.apache.log4j.Logger;	

import Database.Excel_Ops;
import ParentClasses.DriverScript;
import Reports.ReportUtil;
import Utility.Keywords;
import Utility.TestUtil;
import Utility.Variable_Conversions;

public class PBT extends DriverScript{
	
	public static Excel_Ops d = null;
	public static Excel_Ops AllRes = null;
	public static Utility.Variable_Conversions vc = null;

	public static String completeFlowTest(String sheetName) throws IOException, InterruptedException{
		Keywords.dualOutput("Executing PBT Test: ",currentTestName);
		d= new Excel_Ops(System.getProperty("user.dir")+"\\src\\Config\\"+currentDataXL);
		AllRes= new Excel_Ops(System.getProperty("user.dir")+"\\src\\Config\\AllRes.xlsx");
		vc = new Variable_Conversions();
		classResult = "Pass";
		int rowNum = 2; int subItemId = 169060; String answer = null; String path = null;
		
		try {
			if (TestUtil.jasperLogin().equals("Pass")){
				
				if(!currentBrowser.contains("Safari"))
					driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				else
					Thread.sleep(12000L);
				
				if (!currentBrowser.equals("IE") && !currentBrowser.equals("Chrome"))
					Keywords.frameSwitch("main");
				
				int index = vc.strToDblToInt(AllRes.getCellData(currentProduct, "Index", AllRes.getFirstRowInstance(currentProduct, "TestName", currentTestName.trim())));
				
				//Select the testname on All Resources page
				if(!currentBrowser.contains("Safari"))
					Keywords.clickLinkText(currentTestName.trim());
				else
					Keywords.clickbyXpath("//*[@id='sequence"+String.valueOf(index)+"']/td[2]/a");
				
			    //click on Jasper Start Test Page
				if(!currentBrowser.contains("Safari"))
					driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				else
					Thread.sleep(5000L);
				//Verify contents of Jasper Start Test Page and click Start button

					if(currentDataXL.contains("RNC"))
						TestUtil.VerifyJasperStartTestPg(d.getCellData(currentDatasheet, "Start_Title", 2));
					else
						TestUtil.VerifyJasperStartTestPg(currentTestName);
				
				Keywords.clickButton("JspStart_Start_Button");
				
				//Verify Page Headers
				Thread.sleep(3000L);
				TestUtil.verifyTestPageHeaders(d.getCellData(currentDatasheet, "PageTitle", rowNum),false);
				
				//Verify Footers
				Keywords.checkContains("PBT_Footer_Help_Buton", "src", "Help Button");
				Keywords.checkContains("PBT_Footer_EndTest_Buton", "src", "End Test Button");
				
				driver.switchTo().defaultContent();
				driver.switchTo().frame("testMode");
				driver.switchTo().frame("sequenceContentFrame");
				
				Thread.sleep(500L);
				//Keywords.verifyCustomObjectText("PBT_Body_Title_Text", currentTestName);
				
				//answer all questions
            	do { 
              		//Reading the answers from input excel
            		answer = vc.strToDblToStr(d.getCellData(currentDatasheet, "InputAns", rowNum));
					path = "subitem"+ subItemId + ".0.singleAnswerMultipleChoice.answerChoiceRow"+answer+".state0";

					driver.findElement(By.id(path)).click();

					rowNum++; subItemId++;
		        
	            } while(!(d.getCellData(currentDatasheet, "InputAns", rowNum)).isEmpty());
	           	
            	//end the test and accept alert
			 	driver.switchTo().defaultContent();
            	Keywords.clickButton("PBT_Footer_EndTest_Buton");
            	Keywords.verifyText(TestUtil.closeAlertandgetText(),TestUtil.getStringValueinArray(OR,"QB_TP_Alert_Msg","Value"));

            	if(!currentBrowser.contains("Safari"))
					driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				else
					Thread.sleep(5000L);
            	
			 	driver.switchTo().defaultContent();

			 	if (!currentBrowser.equals("IE") && !currentBrowser.equals("Chrome"))
					driver.switchTo().frame("main");
			 	
			    if (driver.getPageSource().contains("Item Review"))
			    	Keywords.dualOutput("RevChapVerbReasonTest: Diagnostic QUIZ Successfully navigated",null);
			    
			    //Complete Item review page functionality
			    ReviewPg rw = new ReviewPg();
				rw.completeFlowTest(currentDatasheet);
			 
			}else{
				Keywords.dualOutput("RevChapVerbReasonTest: ERROR in loading MCAT Test Details page",null);
			}
			return classResult;
		} catch(Throwable t){
			// error
			Keywords.dualOutput("Error in PBT Test",null);
			ReportUtil.addStep("Verify PBT Test ", "Not Successful", "Fail", null);
			classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
			
			return classResult;
		}
	
	}
		

}