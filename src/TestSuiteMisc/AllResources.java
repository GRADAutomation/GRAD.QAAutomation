package TestSuiteMisc;

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

public class AllResources extends DriverScript{
	
	public static Excel_Ops d = null;
	public static Utility.Variable_Conversions vc = null;
	public static String pathBegin = null;
	public static String pathEnd_Type = null;
	public static String pathEnd_Name = null;
	public static String pathEnd_Status = null;
	public static String pathEnd_Length = null;
	public static String pathEnd_Length2 = null;
	
	public static boolean rwPgVerification = false; // Sets to true during review mode validations
	public static boolean completeRegression = false; // Sets to true for complete regression testing option chosen by user (in ControllerNew.xlsx)
	public static boolean  getQAText = false; // Sets to true to get text of question & answers when the option chosen by user (in ControllerNew.xlsx)

	public static String completeFlowTest(String sheetName) throws IOException, InterruptedException{
		APPLICATION_LOGS.debug("Inside QB");
		d= new Excel_Ops(System.getProperty("user.dir")+"\\src\\Config\\"+currentDataXL);
		vc = new Variable_Conversions();
		classResult = "Pass";
		
		if(currentBrowser.equals("Chrome")){
			pathBegin = "html/body/div[1]/div[3]/div[2]/div[1]/div/section/table/tbody/tr[";
			pathEnd_Type = "]/td[1]/div";
			pathEnd_Name = "]/td[2]/a";
			pathEnd_Status = "]/td[3]/div";
			pathEnd_Length = "]/td[4]/div";
			pathEnd_Length2 = "]/td[4]/a";
		} else {		
			pathBegin = "//*[@id='sequence";
			pathEnd_Type = "']/td[1]/div";
			pathEnd_Name = "']/td[2]/a";
			pathEnd_Status = "']/td[3]/div";
			pathEnd_Length = "']/td[4]/div";
			pathEnd_Length2 = "']/td[4]/a";
		}

		//Since the code is common for Complete regression, get QA Text and general flow regression, the following initialization is done
		//to ensure right test will be carried out
		if (currentCaseName.equals("Complete_Regression"))
			completeRegression = true;
		if (currentCaseName.equals("getQAText"))
			getQAText = true;
		
		if (TestUtil.jasperLogin().equals("Pass")){
			if(!currentBrowser.contains("Safari"))
				driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			else
				Thread.sleep(10000L);
			//Keywords.frameSwitch("main");
			
			//Headers and Footers
			TestUtil.VerifyJasperHeader("All Resources");
			TestUtil.VerifyJasperFooter();
			
			if(getQAText)
				getText();
			else {
			//Validate Initial Study Plan text
				if(currentProduct.equals("MCAT2012")) {
					Keywords.clickLinkText("Initial Study Plan");
					driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
					Keywords.verifyObjectText("AlRes_InitialStudyPlan_Page_Text");
					Keywords.clickLink("AlRes_InitialStudyPlan_AllRes_Link");
				}
			
				//Validate the rest of the links
				driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				APPLICATION_LOGS.debug(validateAllItemLinks());
				APPLICATION_LOGS.debug(validateTopicLinks("TestArea"));
				APPLICATION_LOGS.debug(validateTopicLinks("MainTopic"));
				APPLICATION_LOGS.debug(validateTopicLinks("SubTopic"));
			}
		}
			
		return classResult;
	}
	
	public static void getText() throws IOException {
		
		int rowNum = 2; String typePath = null; String namePath = null; int rowID = 0; 
		int maxRows = 0; //should be 306 for MCAT
		if (currentProduct.equals("MCAT2012"))
			maxRows = 306;
		else if (currentProduct.equals("GMAT2013"))
			maxRows = 160;
		String statusPath = null; String lengthPath = null; String lengthPath2 = null;
		
		Keywords.frameSwitch("main");
		
		if(currentBrowser.equals("Chrome"))
			rowID = rowNum;
		else
			rowID = rowNum-2;
		
		while (rowNum <= maxRows){
			typePath = pathBegin+rowID+pathEnd_Type;
			namePath = pathBegin+rowID+pathEnd_Name;
			statusPath = pathBegin+rowID+pathEnd_Status;
			lengthPath = pathBegin+rowID+pathEnd_Length;
			lengthPath2 = pathBegin+rowID+pathEnd_Length2;
			
			d.setCellData(currentDatasheet, "Type", rowNum, Keywords.getCustomAttribute(typePath, "class"));
			d.setCellData(currentDatasheet, "TestName", rowNum, Keywords.getTextCustomElement(namePath, "TestName"));
			d.setCellData(currentDatasheet, "Status", rowNum, Keywords.getTextCustomElement(statusPath, "Status"));
			if(Keywords.elementDisplayedCustom(lengthPath))
				d.setCellData(currentDatasheet, "Length", rowNum, Keywords.getTextCustomElement(lengthPath, "Length"));
			else
				d.setCellData(currentDatasheet, "Length", rowNum, Keywords.getTextCustomElement(lengthPath2, "Length"));
			System.out.println("Completed for :"+rowID);
			d.setCellData(currentDatasheet, "Length", rowNum, String.valueOf(rowID));
			if (currentProduct.equals("GMAT2013"))
				if (rowID > 48 && rowID < 122) 
					rowID++;
			rowID++; rowNum++;
		}		
	}

	public static String validateAllItemLinks() throws IOException {
		methodResult = "Pass";
		int rowNum = 2; String typePath = null; String namePath = null; int rowID = 0;
		String statusPath = null; String lengthPath = null; String lengthPath2 = null;
		int maxRows = d.getRowCount(currentDatasheet);
		String typeClass = null; String length = null;
		
		//Keywords.frameSwitch("main");

		/* - Commenting this as Index column is being implemented 
		if(currentBrowser.equals("Chrome"))
			rowID = rowNum;
		else
			rowID = rowNum-2;
		 */
		
		while (rowNum <= maxRows){
			rowID = vc.strToDblToInt(d.getCellData(currentDatasheet, "Index", rowNum));
			
			if(currentBrowser.equals("Chrome"))
				rowID++;
			
			typePath = pathBegin+rowID+pathEnd_Type;
			namePath = pathBegin+rowID+pathEnd_Name;
			statusPath = pathBegin+rowID+pathEnd_Status;
			lengthPath = pathBegin+rowID+pathEnd_Length;
			lengthPath2 = pathBegin+rowID+pathEnd_Length2;
			typeClass = d.getCellData(currentDatasheet, "Type", rowNum);
			length = d.getCellData(currentDatasheet, "Length", rowNum);
			
			Keywords.verifyCustomAttributeValue(typePath, "class", typeClass, typeClass);
			Keywords.verifyCustomObjectText(namePath, d.getCellData(currentDatasheet, "TestName", rowNum));
			Keywords.verifyCustomObjectText(statusPath, d.getCellData(currentDatasheet, "Status", rowNum));
			
			if(length.equals("--"))
				Keywords.verifyCustomObjectText(lengthPath2, length);
			else
				Keywords.verifyCustomObjectText(lengthPath, length);
			
			System.out.println("Validation Completed for : "+rowID);
			APPLICATION_LOGS.debug("Validation Completed for : "+rowID);
			
			//if (rowID > 48 && rowID < 122) // this if is only for GMAT
			//	rowID++;
			//rowID++; 
			rowNum++;
		}
		
		return methodResult;
	}
	
	public static String validateTopicLinks(String topic) throws IOException {
		methodResult = "Pass";int rowNum = 2; String typePath = null; String namePath = null; int rowID = 0;
		String statusPath = null; String lengthPath = null; String lengthPath2 = null;
		int maxRows = d.getRowCount(currentDatasheet);
		String typeClass = null; String length = null; 
		String currentRowTopic = null; String previousRowTopic = null;
		
		//Keywords.frameSwitch("main");
		
		/* - Commenting this as Index column is being implemented 
		if(currentBrowser.equals("Chrome"))
			rowID = rowNum;
		else
			rowID = rowNum-2;
		 */
		
		while (rowNum <= maxRows){
			rowID = vc.strToDblToInt(d.getCellData(currentDatasheet, "Index", rowNum));
			if(currentBrowser.equals("Chrome"))
				rowID++;
			
			typePath = pathBegin+rowID+pathEnd_Type;
			namePath = pathBegin+rowID+pathEnd_Name;
			statusPath = pathBegin+rowID+pathEnd_Status;
			lengthPath = pathBegin+rowID+pathEnd_Length;
			lengthPath2 = pathBegin+rowID+pathEnd_Length2;
			typeClass = d.getCellData(currentDatasheet, "Type", rowNum);
			length = d.getCellData(currentDatasheet, "Length", rowNum);
			currentRowTopic = d.getCellData(currentDatasheet, topic, rowNum);
			previousRowTopic = d.getCellData(currentDatasheet, topic, (rowNum-1));
			
			if (!currentRowTopic.isEmpty()){
				//Click the main topic link only for the first time...
				if(!currentRowTopic.equals(previousRowTopic))
					Keywords.clickLinkText(currentRowTopic);
				
				Keywords.verifyCustomAttributeValue(typePath, "class", typeClass, typeClass);
				Keywords.verifyCustomObjectText(namePath, d.getCellData(currentDatasheet, "TestName", rowNum));
				Keywords.verifyCustomObjectText(statusPath, d.getCellData(currentDatasheet, "Status", rowNum));
			
				if(length.equals("--"))
					Keywords.verifyCustomObjectText(lengthPath2, length);
				else
					Keywords.verifyCustomObjectText(lengthPath, length);
			
				System.out.println("Validation Completed for : "+rowID);
				APPLICATION_LOGS.debug("----------Validation Completed for : "+rowID+"------------------");
			}
			
			//if (rowID > 48 && rowID < 122) // this if is only for GMAT
			//	rowID++;
			//rowID++; 
			rowNum++;
		}
		
		return methodResult;
		
		
	}
}
