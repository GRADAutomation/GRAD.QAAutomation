package TestSuiteMisc;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import Database.Excel_Ops;
import ParentClasses.DriverScript;
import Utility.Keywords;
import Utility.TestUtil;
import Utility.Variable_Conversions;

public class CMSextract1 extends DriverScript {
	
	// All initialization
		public static Excel_Ops d = null;
		public static Excel_Ops AllRes = null;
		public static Utility.Variable_Conversions vc = null;

		
		//This method will be called by Driver Script parent class file using Java reflection, so the method name should be constant
		@Test
		public static String completeFlowTest(String sheetName) throws IOException, InterruptedException {

			//Initializing
			APPLICATION_LOGS.debug("Inside Suite 1 CompleteflowTest" + sheetName);
			classResult = "Pass"; 
			String temp;
			
			//Initialize excel instance and variable conversion instance
			d= new Excel_Ops(System.getProperty("user.dir")+"\\src\\Config\\"+currentDataXL);
			vc = new Variable_Conversions();
			
			TestUtil.jasperLogin();
			
			Thread.sleep(3000L);
			
			Keywords.clickLink("JspAdmn_ContentSearch");
			
			int totalRows = d.getRowCount(currentDatasheet); int rowNum = 2;
			
			do {
				Thread.sleep(3000L);
				String temp1 = d.getCellData(currentDatasheet, "ID", rowNum).trim();
				System.out.println(temp1);
				//Keywords.clearandinputWithParameter("JspCS_SearchCriteria",temp1);
				Keywords.clear("JspCS_SearchCriteria");
				Keywords.inputValue("JspCS_SearchCriteria",temp1);
				Keywords.clickButton("JspCS_Search_Button");
				Thread.sleep(1000L);
				Keywords.clickLink("JspCS_Results_First");
				
				String winHandleBefore = driver.getWindowHandle();
				for(String winHandleNew : driver.getWindowHandles()){
					driver.switchTo().window(winHandleNew);			
				}
				
				d.setCellData(currentDatasheet, "ExtID", rowNum, Keywords.getObjectText("JspCSR_ID"));
				d.setCellData(currentDatasheet, "Type", rowNum, Keywords.getObjectText("JspCSR_Type"));
				d.setCellData(currentDatasheet, "Ans", rowNum, Keywords.getObjectText("JspCSR_Ans"));
				d.setCellData(currentDatasheet, "Title", rowNum, Keywords.getObjectText("JspCSR_Title"));

			WebElement spans = driver.findElement(By.xpath("html/body/table[2]/tbody/tr/td"));
			List<WebElement> tables = spans.findElements(By.tagName("span"));
			System.out.println("Total ---"+tables.size());
			
			for(int i=0;i<tables.size();i++){
				temp = tables.get(i).getText();
				System.out.println("i == "+i+"----------   "+temp);
				if(temp.equals("question"))
					d.setCellData(currentDatasheet, "question", rowNum, "Y");
				else if(temp.equals("question-set"))
					d.setCellData(currentDatasheet, "question-set", rowNum, "Y");
				else if(temp.contains("UQType"))
					d.setCellData(currentDatasheet, "UQType", rowNum, tables.get(i+2).getText());
				else if(temp.contains("UQTopic"))
					d.setCellData(currentDatasheet, "UQTopic", rowNum, tables.get(i+2).getText());
				else if(temp.contains("difficulty"))
					d.setCellData(currentDatasheet, "difficulty", rowNum, tables.get(i+2).getText());
			}
			
			//System.out.println("Text ---"+spans.getText());
			
			
			if (rowNum <= totalRows) {
			driver.close();
			driver.switchTo().window(winHandleBefore);
			}
			
			rowNum++;
			
		} while (rowNum <= totalRows);
			
			return classResult;
		}

}
