/* This JAVA file is designed to automate the REview page functionality of Workshops / LOD / Review Notes
 * Most of the functions are copied from Reviews Note JAVA file of TestSuite1 * 
 */

package TestSuite4;

/**
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

public class ReviewPage extends DriverScript{
	
	//private StringBuffer verificationErrors = new StringBuffer();

	static Logger log = Logger.getLogger(ReviewPage.class.getName());
	Excel_Ops d = null; Excel_Ops AllRes = null;
	Utility.Variable_Conversions vc = null;
	public static boolean getQAText;

	@Test
	public String completeFlowTest(String sheetName) throws IOException, InterruptedException {
		methodResult = "Pass";
		//Reading properties file in Java example
			 
		Keywords.dualOutput("Inside ReviewPageTest", sheetName);
		d = new Excel_Ops(System.getProperty("user.dir")+"\\src\\Config\\"+currentDataXL);
		AllRes= new Excel_Ops(System.getProperty("user.dir")+"\\src\\Config\\AllRes.xlsx");

		vc = new Variable_Conversions();
		
		if (currentCaseName.equals("getQAText"))
			getQAText = true;
				
		//if (TestUtil.jasperLogin().equals("Pass")) {
		try {	
		if(!currentBrowser.contains("Safari"))
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		else
			Thread.sleep(10000L);

			//If MCAT 2012 Test Prep page proceed further
			if (Keywords.verifyTitle(currentTestName)){
				Keywords.dualOutput("MCAT All Resources page is launched", null);
				
				driver.switchTo().defaultContent();
				driver.switchTo().frame("main");
				if(!getQAText) {
					System.out.println(TestUtil.VerifyJasperHeader("Item Review"));
					System.out.println(TestUtil.VerifyJasperFooter());
					System.out.println(verifyItemReviewPgText());
				}
				System.out.println(contentVerify());
				if(!getQAText)
					System.out.println(HeaderLinks());
				
		   }else{
			   Keywords.dualOutput("Review Page of Workshops / LOD: ERROR in loading MCAT All resources page", null);
		}	
	//	} // Loop of Login
			
		}catch(Throwable t){
			// error
			Keywords.dualOutput("Error in Review page of Workshops / LOD", null);
			ReportUtil.addStep("Verify MCATTest ", "Not Successful", "Fail", null);
			classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
		}
		return methodResult;
	}
	

	public String verifyItemReviewPgText() throws IOException, InterruptedException {
		submethodL1Result = "Pass";
		
		Keywords.verifyObjectTextWithParameter("ItmRw_Body_ItmRw_Text_Suite4", currentTestName);
		
		Thread.sleep(3000L);
		
		Keywords.verifyObjectTextWithParameter("Jsp_Header_WelBack_Text", TestUtil.getStringValueinArray(OR, "Jsp_Header_WelBack_Text", "Value"));

	
		return submethodL1Result;
	}
	
	
	public String HeaderLinks() throws IOException, InterruptedException{
		submethodL1Result = "Pass";
		
		//Verify Item Review Page All Resources navigation link
		TestUtil.gotoAllResandItemReview("JspStart_Allres_link");
	
		return submethodL1Result;
		
	}	

	public int determinetd(int rowNum){
		
		//d = new Excel_Ops(System.getProperty("user.dir")+"/src/Config/"+currentDataXL);
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
			
			if (!getQAText) {
			markedPath = TestUtil.getStringValueinArray(OR,"ItmRw_Body_ItmRw_Marked_Start","Key")+count+"]/td[1]/span";
			Keywords.verifyCustomAttributeValue(markedPath, "class", "completed-True", "Review Page item of LOB/Shops/Notes");
			}
			
			itemPath = TestUtil.getStringValueinArray(OR,"ItmRw_Body_ItmRw_Marked_Start","Key")+count+"]/td[2]/a";
			if (!getQAText)
				Keywords.verifyCustomObjectText(itemPath, d.getCellData(currentDatasheet, "Topic-ItemRw", row));
	
			Keywords.clickbyXpath(itemPath);
			Thread.sleep(2000L);
			//verify chapter name
			driver.switchTo().defaultContent();
			Keywords.verifyObjectTextWithParameter("VwPg_Footer_ChapName",d.getCellData(currentDatasheet, "Topic", row));
			
			
			if(getQAText && !(currentDataXL.contains("LessonsOnDemand"))){
				driver.switchTo().defaultContent();
				driver.switchTo().frame("testMode");
				String reviewId = Keywords.getTextCustomElement(TestUtil.getStringValueinArray(OR, "VwPg_Footer_FeedbackID_Text", "Key"), "Review_ID" );
				d.setCellData(currentDatasheet, "Review_ID", row, reviewId );
			}
						
			driver.switchTo().defaultContent();

			Keywords.clickButton("VwPg_Footer_EndTest_Button");
			Thread.sleep(1000L);
			if(!currentBrowser.contains("Safari"))
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

