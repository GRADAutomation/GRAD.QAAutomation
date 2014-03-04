package TestSuiteFPT;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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
		
		d= new Excel_Ops(System.getProperty("user.dir")+"\\src\\Config\\"+currentDataXL+".xlsx");
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
			Keywords.clickButton(TestUtil.getStringValueinArray(OR, "FPT_Product_Go_Button_begin", "Key")+currentDatasheet+TestUtil.getStringValueinArray(OR, "FPT_Product_Go_Button_end", "Key"));
			
			
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
