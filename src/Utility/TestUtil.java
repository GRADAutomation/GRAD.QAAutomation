package Utility;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.android.AndroidDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.sun.jna.Platform;

import Database.Excel_Ops;
import ParentClasses.DriverScript;
import Reports.ReportUtil;
import TestSuiteMisc.KeywordDriven;
import Utility.Keywords;

//Updated on 12/30/13 to reflect GMAT product selection

public class TestUtil extends DriverScript{

	private static boolean acceptNextAlert = true;
	
	// returns current date and time
	public static String now(String dateFormat) {
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
	    return sdf.format(cal.getTime());

	}
	
	//
	public static String getStringValueinArray(String[][] SearchArray, String objName, String key){
		//Keywords.dualOutput("Inside getStringValueinArray : ",objName);
		int keyposition = getColumnIndexValueinArray(SearchArray, key); //Column index of desired value
		int objNameposition = getRowIndexValueinArray(SearchArray,objName,getColumnIndexValueinArray(SearchArray,"ObjName")); //Row index of desired value
		return SearchArray[objNameposition][keyposition];
				
	}
	
	//Returns the key value index in the first row of the array
	public static int getColumnIndexValueinArray(String[][] SearchArray, String key){
		int cols=SearchArray[0].length; //First Row in the String will have headers
		int j = 0; int keyindex = -1;
		
		//Search the column position in the first row
			do {
				if(SearchArray[0][j].equals(key)){
					keyindex = j;
					break;
				}else
					j++;
			} while(j < cols);
			return keyindex;
	}
	
	public static Object[][] getData(String sheetName){
		// return test data;
		// read test data from xls
		Object[][] testData = null;
		//Keywords.dualOutput("Inside getdata", null);
		int noofbrowsers = controller.getNoOfMatches("Settings", "Value", "Yes");
		//Keywords.dualOutput(noofbrowsers, null);
		int rowCount = controller.getRowCount("Settings");
		//Keywords.dualOutput(rowCount, null);
		int begRow = controller.getFirstRowInstance("Settings", "Parameter", "BROWSERS");
		//Keywords.dualOutput(begRow, null);
		if (execution_mode.equals("Standalone")){
			testData =new Object[1][3];
			//Keywords.dualOutput("Inside standalone", null);
		}
		else if (execution_mode.equals("Hub-Node-Parallel") || execution_mode.equals("Hub-Node-Serial")){
			testData =new Object[noofbrowsers][3];
			//Keywords.dualOutput("Inside Hub-Node-Parallel init", null);
		}
		else if (execution_mode.equals("Hub-Node-Mixed")){
			testData =new Object[8][3];
			//Keywords.dualOutput("Inside Hub-Node-mixed init", null);
		}
			
		int i = 0;
		for( int rowNum = begRow+1; rowNum <= rowCount ; rowNum++){	
			//Keywords.dualOutput("Inside for", null);
			//Keywords.dualOutput(execution_mode, null);
			
			if (execution_mode.equals("Hub-Node-Parallel") || execution_mode.equals("Hub-Node-Serial")){
				//Keywords.dualOutput("Inside HNP", null);
				if (controller.getCellData("Settings", "Value", rowNum).equals("Yes")){
					testData[i][0]= controller.getCellData("Settings", "Parameter", rowNum);
					testData[i][1]= controller.getCellData("Settings", "Login", rowNum);
					testData[i][2]= controller.getCellData("Settings", "Pwd", rowNum);
					//Keywords.dualOutput("testData[i][0]", null);
					i++;
				}
			}
			else if (execution_mode.equals("Standalone")){
				if (controller.getCellData("Settings", "Value", rowNum).equals("Yes")){
					testData[0][0]= controller.getCellData("Settings", "Parameter", rowNum);
					testData[0][1]= controller.getCellData("Settings", "Login", rowNum);
					testData[0][2]= controller.getCellData("Settings", "Pwd", rowNum);
					return testData;
				}
			}
			else if (execution_mode.equals("Hub-Node-Mixed")){
				testData[i][0]= controller.getCellData("Settings", "Parameter", rowNum);
				testData[i][1]= controller.getCellData("Settings", "Login", rowNum);
				testData[i][2]= controller.getCellData("Settings", "Pwd", rowNum);
				i++;
			}
		}
		//Keywords.dualOutput(testData, null);
		return testData;
	}
	
	//Few changes made to Ipad URL on 12/9
	public static void initializeDriver() throws IOException{
		String url = null;
		//load browser
		Keywords.dualOutput("Browser chosen is: ", browserType);
		if(execution_mode.equals("Standalone")){	
			if(currentBrowser.equals("Firefox")){
				ProfilesIni allProfs = new ProfilesIni();
				FirefoxProfile myProf = allProfs.getProfile("Selenium");
				DesiredCapabilities dc=new DesiredCapabilities();
				dc.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,UnexpectedAlertBehaviour.ACCEPT);
				driver = new FirefoxDriver(dc);			
			} else if(currentBrowser.equals("IE")){
				String path = controller.getCellData("Settings", "Value", controller.getFirstRowInstance("Settings", "Parameter", "Driver_Path"))+"\\IEDriverServer.exe";
				Keywords.dualOutput("IE Path: ",path);
				System.setProperty("webdriver.ie.driver", path);
		        DesiredCapabilities capab = DesiredCapabilities.internetExplorer();

		        capab.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,UnexpectedAlertBehaviour.ACCEPT);
		        driver = new InternetExplorerDriver(capab);
			} else if(currentBrowser.equals("Chrome")){
				String path = controller.getCellData("Settings", "Value", controller.getFirstRowInstance("Settings", "Parameter", "Driver_Path"))+"\\ChromeDriver.exe";
				Keywords.dualOutput("Chrome Path: ",path);
				System.setProperty("webdriver.chrome.driver", path);
				
		        DesiredCapabilities capab = DesiredCapabilities.chrome();

		        capab.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,UnexpectedAlertBehaviour.ACCEPT);
		        driver = new ChromeDriver(capab);
			} else if(currentBrowser.contains("Safari")){
				
				if(Platform.isWindows() || Platform.isMac()) {
					DesiredCapabilities capab = DesiredCapabilities.safari();
					capab.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,UnexpectedAlertBehaviour.ACCEPT);
					driver = new SafariDriver(capab);
				}
			} else if(currentBrowser.equals("Android")){		
		        DesiredCapabilities capab = DesiredCapabilities.android();
		        capab.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,UnexpectedAlertBehaviour.ACCEPT);
		        driver = new AndroidDriver(capab);
			} else if(currentBrowser.equals("Ipad")){		
		        DesiredCapabilities capab = DesiredCapabilities.ipad();
		        url = "http://"+controller.getCellData("Settings", "Value", controller.getFirstRowInstance("Settings", "Parameter", "Ipad_Ip"))+"/wd/hub";
		        driver = new RemoteWebDriver(new URL(url), capab);
			}
		} else //if(execution_mode.equals("Hub-Node-Parallel") || execution_mode.equals("Hub-Node-Serial")) 
		{
			DesiredCapabilities capab = null;
			if(currentBrowser.equals("Firefox")){
				ProfilesIni allProfs = new ProfilesIni();
				FirefoxProfile myProf = allProfs.getProfile("Selenium");
				capab= DesiredCapabilities.firefox();
				capab.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,UnexpectedAlertBehaviour.ACCEPT);			
			} else if(currentBrowser.equals("IE")){
				
				//System.setProperty("webdriver.ie.driver", controller.getCellData("Settings", "Value", controller.getFirstRowInstance("Settings", "Parameter", "Driver_Path"))+"\\IEDriverServer.exe");
		        capab = DesiredCapabilities.internetExplorer();
		        capab.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,UnexpectedAlertBehaviour.ACCEPT);
		   
			} else if(currentBrowser.equals("Chrome")){
				
				//System.setProperty("webdriver.chrome.driver", controller.getCellData("Settings", "Value", controller.getFirstRowInstance("Settings", "Parameter", "Driver_Path"))+"\\ChromeDriver.exe");
		        capab = DesiredCapabilities.chrome();
		        capab.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,UnexpectedAlertBehaviour.ACCEPT);
		        
			} else if(currentBrowser.contains("Safari")){
				
				if(Platform.isWindows() || Platform.isMac()) {
					capab = DesiredCapabilities.safari();
					capab.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,UnexpectedAlertBehaviour.ACCEPT);			
				}
			} else if(currentBrowser.equals("Android")){		
		        capab = DesiredCapabilities.android();
		        capab.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,UnexpectedAlertBehaviour.ACCEPT);
		        
			} else if(currentBrowser.equals("Ipad")){		
		        capab = DesiredCapabilities.ipad();
		        //String url = "http://"+controller.getCellData("Settings", "Value", controller.getFirstRowInstance("Settings", "Parameter", "Ipad_Ip"))+"/wd/hub";
		        //driver = new RemoteWebDriver(new URL(url), capab);
			}
			
			if (currentBrowser.equals("Ipad"))
				url = "http://"+controller.getCellData("Settings", "Value", controller.getFirstRowInstance("Settings", "Parameter", "Ipad_Ip"))+"/wd/hub";
			else
				url = "http://"+controller.getCellData("Settings", "Value", controller.getFirstRowInstance("Settings", "Parameter", "Hub_Ip"))+":4444/wd/hub";
			driver = new RemoteWebDriver(new URL(url), capab);
			driver = new Augmenter().augment(driver);
		}
			
		Keywords.dualOutput("Started the driver", null);
		
		// EventFiringWebDriver
		evfw = new EventFiringWebDriver(driver);
		
	}
	
	//Returns the key value index in the given column of the array
	public static int getRowIndexValueinArray(String[][] SearchArray, String key, int colIndex){
			int rows=SearchArray.length; //First Row in the String will have headers
			int i = 0; int keyindex = -1;
			
			//Search the column position in the first row
					do{
						if(SearchArray[i][colIndex].equals(key)){
							keyindex = i;
							break;
						}else
							i++;
					}while(i<rows);
				return keyindex;
		}
		
		//locates the element based on property and key
	public static WebElement findElement(String property, String key, String frame1, String frame2, String frame3) throws IOException{
		Keywords.dualOutput("Inside findElement", null);
			if (!frame1.equals("") ){
				evfw.switchTo().defaultContent();
				evfw.switchTo().frame(frame1);
			}
			if (!frame2.equals("")){
				evfw.switchTo().frame(frame2);
			}
			if (!frame3.equals("")){
				evfw.switchTo().frame(frame3);
			}
			
			try{
				if (property.equals("xpath"))
					return evfw.findElement(By.xpath(key));
				else if (property.equals("id"))
					return evfw.findElement(By.id(key));
				else if (property.equals("linkText"))
					return evfw.findElement(By.linkText(key));
				else if (property.equals("name"))
					return evfw.findElement(By.name(key));
				else if (property.equals("partialLinkText"))
					return evfw.findElement(By.partialLinkText(key));
				else if (property.equals("tagName"))
					return evfw.findElement(By.tagName(key));
				else if (property.equals("className"))
					return evfw.findElement(By.className(key));
				else if (property.equals("cssSelector"))
					return evfw.findElement(By.cssSelector(key));
				else
					return null;
			}
			catch (UnhandledAlertException UAE){
				Alert alert = driver.switchTo().alert();
				alert.accept();
				
				WebElement ele = findElement(property, key, frame1, frame2, frame3);
				if(ele ==null)
					return null;
				else
					return ele;
			}
			catch(Throwable t){
	    		//report error
				Keywords.dualOutput("Inside Find Element catch block", null);
	    		ErrorUtil.addVerificationFailure(t);
	    		fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+key+".jpg";
				TestUtil.takeScreenShot(fileName);
	    		Keywords.dualOutput("Error while selecting the object: ", key+ t.getLocalizedMessage());;
	    		
	    		return null;
	    	}
		}
	
		@SuppressWarnings("null")
	public static WebElement findElement(String objName) throws IOException{
			Keywords.dualOutput("Inside findElememnt - only objName as parameter", objName);
			String property = getStringValueinArray(OR,objName,"Property");
			String key = getStringValueinArray(OR,objName,"Key");
			//String labelValue = TestUtil.getStringValueinArray(OR,objName,"Value");
			String frame1 = getStringValueinArray(OR,objName,"Frame1");
			String frame2 = getStringValueinArray(OR,objName,"Frame2");
			String frame3 = getStringValueinArray(OR,objName,"Frame3");
			
			if (!frame1.equals("") ){
				evfw.switchTo().defaultContent();
				evfw.switchTo().frame(frame1);
			}
			if (!frame2.equals("")){
				evfw.switchTo().frame(frame2);
			}
			if (!frame3.equals("")){
				evfw.switchTo().frame(frame3);
			}
			if(!currentBrowser.contains("Safari"))
				driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			try{
				if (property.equals("xpath"))
					return evfw.findElement(By.xpath(key));
				else if (property.equals("id"))
					return evfw.findElement(By.id(key));
				else if (property.equals("linkText"))
					return evfw.findElement(By.linkText(key));
				else if (property.equals("name"))
					return evfw.findElement(By.name(key));
				else if (property.equals("partialLinkText"))
					return evfw.findElement(By.partialLinkText(key));
				else if (property.equals("tagName"))
					return evfw.findElement(By.tagName(key));
				else if (property.equals("className"))
					return evfw.findElement(By.className(key));
				else if (property.equals("cssSelector"))
					return evfw.findElement(By.cssSelector(key));
				else{
					Keywords.dualOutput("Executing else part", null);
					return evfw.findElement(By.xpath(key));
				}
			}
			catch (UnhandledAlertException e){
				Alert alert = driver.switchTo().alert();
				alert.accept();
				
				WebElement ele = findElement(objName);
				if(ele ==null)
					return null;
				else
					return ele;
					
			}
			catch(Throwable t){
	    		//report error
				System.out.println("Inside Find Element catch block");
				fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
				TestUtil.takeScreenShot(fileName);
	    		Keywords.dualOutput("Error locating the findElement: ", key+ t.getLocalizedMessage());;
	    		return null;
	    	}
   	
	    }

		public static WebElement findCustomElement(String objName) throws IOException{
			Keywords.dualOutput("Inside findCustomElememnt", objName);
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			WebElement we = null; int tries = 0;
			
			while (tries < 2) {
				tries +=1;
				try{
				we = evfw.findElement(By.xpath(objName));
				break; // found webelement
			}
				catch ( StaleElementReferenceException e )  {						
					Keywords.dualOutput("Element not found after try #" ,  String.valueOf(tries));  // will try again
		        }
				
				catch (UnhandledAlertException e){
				Keywords.dualOutput("Executing UnhandledAlertException", null);
				Alert alert = driver.switchTo().alert();
				alert.accept();
				
				we = findCustomElement(objName);
			}
			catch(Throwable t){
	    		//report error
/*				takeScreenShot(objName);
	    		ErrorUtil.addVerificationFailure(t);
*/				System.out.println("Inside Find Element catch block");
				//String temp = objName.replaceAll("*", "").replaceAll("//", "");
				fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
				TestUtil.takeScreenShot(fileName);
	    		Keywords.dualOutput("Error locating the findCustomElement: ", objName+ t.getLocalizedMessage());;
	    		we = null;
	    	}
			}
			return we;
   	
	    }
		
	public static boolean isSkip(String TestName, String ScriptName){
		
		for(int i=2; i<=controller.getRowCount(currentProduct);i++ ){
	    	if(controller.getCellData(currentProduct, "TestName", i).equals(TestName)){
	    		if(controller.getCellData(currentProduct, "ScriptName", i).equals(ScriptName)) {
	    			if(controller.getCellData(currentProduct, "Include", i).equals("Y"))
		    			  return false;
		    		  else
		    			  return true;
	    		}
	    	}
				
	      }
		return false;
	}
	
	// screenshots
	public static void takeScreenShot(String fileName) throws IOException{
		//File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	    //FileUtils.copyFile(scrFile, new File(config.getProperty("screenShotsPath")+"\\"+fileName+".jpg"));
		Keywords.dualOutput("Inside take screenshot", null);
		fileName = Keywords.formatScreenshotName(fileName);
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		Keywords.dualOutput(fileName, null);
	    try {
			FileUtils.copyFile(scrFile, new File(screenshotPath+fileName));
			System.out.println("In try");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Inside take screenshot - before catch");
			e.printStackTrace();
			System.out.println("Inside take screenshot - after catch");
		}
	    
	}
	
	public static String[][] getExcelintoArray(Excel_Ops Tempexcel,String sheetName) throws IOException{
		// returns String Array testData by reading from a specific sheet of a given excel
		
		int rows = Tempexcel.getRowCount(sheetName);
		int cols = Tempexcel.getColumnCount(sheetName);
		Keywords.dualOutput("total rows -- ", String.valueOf(rows));
		Keywords.dualOutput("total cols -- ",String.valueOf(cols));
		if(rows <1){
			String[][] testData1 =new String[1][1];
			return testData1;
		}
	    String[][] testData = new String[rows][cols];
		
		for(int rowNum = 0 ; rowNum < rows ; rowNum++){
			for(int colNum=0 ; colNum< cols; colNum++){
				testData[rowNum][colNum]=Tempexcel.getCellData(sheetName, colNum+1, rowNum+1);
			}
		}
		return testData;
	}
		
	// make zip of reports
	public static void zip(String filepath){
	 	try
	 	{
	 		File inFolder=new File(filepath);
	 		File outFolder=new File("Reports.zip");
	 		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outFolder)));
	 		BufferedInputStream in = null;
	 		byte[] data  = new byte[1000];
	 		String files[] = inFolder.list();
	 		for (int i=0; i<files.length; i++)
	 		{
	 			in = new BufferedInputStream(new FileInputStream
	 			(inFolder.getPath() + "/" + files[i]), 1000);  
	 			out.putNextEntry(new ZipEntry(files[i])); 
	 			int count;
	 			while((count = in.read(data,0,1000)) != -1)
	 			{
	 				out.write(data, 0, count);
	 			}
	 			out.closeEntry();
	 		}
	 		out.flush();
	 		out.close();
	 	
	 	}
	 	catch(Exception e){
	 		e.printStackTrace();
	 	} 
	}

	public static String closeAlertandgetText() {
	    try {
	      Alert alert = driver.switchTo().alert();
	      String alertText = alert.getText();
	      
	      if (acceptNextAlert) 
	        alert.accept();
	      else 
	        alert.dismiss();
	      
	      return alertText;
	    } 
	 //   catch(Exception e){
	 	//	e.printStackTrace();
	 //	} 
	    
	    finally {
	      acceptNextAlert = true;
	    }
	  }
	
	
	public static String jasperLogin() throws IOException {
		testUtilResult = "Pass";
		
		if(!currentBrowser.contains("Safari"))
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		try{
			Keywords.navigatebyURL(controller.getCellData("Settings", "Login", controller.getFirstRowInstance("Settings", "Parameter","Environment")));

		    if(Keywords.verifyTitle("JspLog_Title")){
		    	Keywords.dualOutput("Login:  Login Page sucessfully launched", null);

		    	//Logging into application
				Keywords.clearandinput("JspLog_KecUsername");
				Keywords.clearandinput("JspLog_KecPassword");
				//Keywords.clearandinput("JspLog_JasperUsername");
				//Keywords.clearandinput("JspLog_JasperPassword");
				
				Keywords.clearandinputWithParameter("JspLog_JasperUsername",currentLoginID);
				Keywords.clearandinputWithParameter("JspLog_JasperPassword",currentPassword);
				
				//Keywords.selectfromSelectList("JspLog_JasperProduct");
				new Select(findElement("JspLog_JasperProduct")).selectByVisibleText(currentProduct);
				
				Thread.sleep(2000L);
				if(currentBrowser.contains("Safari") || execution_mode.contains("Hub"))
					Keywords.clickButton("JspLog_Login");
				else
					Keywords.mouseClick("JspLog_Login");
				
				//Keywords.clickButton("JspLog_Login");
				
				if (testUtilResult.equals("Pass")){
					ReportUtil.addStep("Jasper Login", "Login Completed", testUtilResult,null);
					jasperLoggedin = true;
				}
				else
					ReportUtil.addStep("Jasper Login", "Login is not Successful", testUtilResult,screenshotPath+fileName);
								
			}else{
				Keywords.dualOutput("Login: Error loading login page", null);
				
				fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+"JspLog_Title.jpg";
				TestUtil.takeScreenShot(fileName);
				ReportUtil.addStep("Jasper Login", "Login page is not launched", testUtilResult,screenshotPath+fileName);
			}
		}
		catch(Exception error){
			System.out.println("error"+ error);
        }
        finally{
        	//driver.close();
        }
		Keywords.dualOutput("Jasper Login step is completed", null);
		ReportUtil.addStep("Jasper Login", "Login Completed", testUtilResult,null);
		return testUtilResult;
	}	
	
	//Verify navigating to All Resources Page and come back to Item Review Page
	public static String gotoAllResandItemReview(String AllResObjName) throws IOException{
		Keywords.dualOutput("Clicking on All Resources Page Link", null);
		try{
			//driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			Thread.sleep(2000);
			
			//Verify navigating to All Resources Page and come back to Item Review Page
			Keywords.clickLinkandVerify(AllResObjName, "Jsp_Header_Pgtxt");

			Keywords.clickLinkandVerify(currentTestName, "Jsp_Header_Pgtxt");

		}catch(Throwable t){
			// error
			Keywords.dualOutput("Error while navigating to All Resources page and Item Review Page - ",AllResObjName);
			
			fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+AllResObjName+".jpg";
			takeScreenShot(screenshotPath+fileName);
			ReportUtil.addStep("Navigate back to Item Review Page", "Navigation not successful", "Fail",screenshotPath+fileName);
			return "Fail -"+ t.getMessage(); 
		
		}
				
		return "Pass";
	}
	

	public static String VerifyJasperHeader(String pageText) throws IOException{
		testUtilResult = "Pass";
		// Verify Header Logo
		Keywords.verifyObjectText("Jsp_Header_Logo");
		
		// Verify Header page title
		Keywords.verifyObjectTextWithParameter("Jsp_Header_Pgtxt",pageText); //changed from ItmRw_Header_ItmRw to Jsp_Header_Pgtext
		
		// Verify 'Powered by Text'
		Keywords.verifyObjectText("Jsp_Header_PoweredBy_Text");
		
		if(testUtilResult.equals("Pass")){
			Keywords.dualOutput(pageText+" page headers have been validated", null);
			ReportUtil.addStep("Verify "+pageText+" page headers: Logo, page title, Powered-By", "Header has shown as expected", "Pass", null);
		} else {
			Keywords.dualOutput("Something wrong while validing ", pageText+" page headers");
			ReportUtil.addStep("Verify "+pageText+" page headers: Logo, page title, Powered-By", "NOT functioning as expected", "Fail", null);
		}
		
		return testUtilResult;
		
		// No need to add to ReportUtil as this method will be called from another
	}

	//Modified -- Siva @12/7
	public static String VerifyJasperStartTestPg(String pageTitle) throws IOException{
		
		VerifyJasperFooter();
		testUtilResult = "Pass";
		
		// Verify Header Logo
		Keywords.verifyObjectText("Jsp_Header_Logo");
		
		//Verify if AllresLink is displayed
		Keywords.elementDisplayed("JspStart_Allres_link");
		
		// Updated on 1/21
		/* Commenting this portion of code as linktext on all resources may not work on all browsers
		//Verify All Resources link and come back to Test Start Page
		Keywords.clickLink("JspStart_Allres_link");
		
		if(!currentBrowser.contains("Safari"))	
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			
		Keywords.clickLinkText(currentTestName.trim()); 
		*/
		// Verify Exam title
			Keywords.verifyObjectTextWithParameter("JspStart_Testtitle_Text",pageTitle); //changed from ItmRw_Header_ItmRw to Jsp_Header_Pgtext
		
		//Verify the message
		if(currentDataXL.contains("TopicalQ"))
			Keywords.verifyObjectText("JspStart_Message_Text_Quiz");
		else
			Keywords.verifyObjectText("JspStart_Message_Text");
		
		//Add a step to results
		if(testUtilResult.equals("Pass"))
			ReportUtil.addStep("Validate "+currentTestName+" Start Page contents", "Contents are good", "Pass", null);
		else
			ReportUtil.addStep("Validate "+currentTestName+" Start Page contents", "Something wrong with content", "Fail", screenshotPath+fileName);
		
		return testUtilResult;
	}
	

	public static String VerifyJasperTestDirectionsPg() throws IOException{
		Keywords.dualOutput("Executing Jasper Test Direction Page contents", null);
		
		testUtilResult = "Pass";
		Excel_Ops d= new Excel_Ops(System.getProperty("user.dir")+"\\src\\Config\\"+currentDataXL+".xlsx");
		Variable_Conversions vc = new Variable_Conversions();
		int rowNum = d.getCellRowNum("Test_Directions", "Sheet_Name", currentDatasheet);
		int noofSections = vc.strToDblToInt(d.getCellData("Test_Directions", "Sections", rowNum));
		String objName = null;
		
		//Verify Headers : Logo & Text ::: Hard-coding reviewmode parameter to false since TestDirections comes only during test mode, not review mode
		verifyTestPageHeaders(d.getCellData("Test_Directions", "Title", rowNum).trim(), false);
		
		// Verify Footer: Test Directions
		Keywords.verifyObjectText("TstBeg_Footer_TD_Text");
		
		//Verify Test Directions & Text
		Keywords.verifyObjectText("TstBeg_Body_TD_Text");
		Keywords.verifyObjectTextWithParameter("TstBeg_Body_TDContent_Text",d.getCellData("Test_Directions", "Test_Directions",rowNum));
		
		//Verify Test Structure & Contents
		Keywords.verifyObjectText("TstBeg_Body_TS_Text");
		while(noofSections>0){
			objName = getStringValueinArray(OR,"TstBeg_Body_TSContent_Text_Start","Key")+noofSections+"]";
			Keywords.verifyCustomObjectText(objName,d.getCellData("Test_Directions", "Test_Structure"+noofSections,rowNum));
			noofSections--;
		}
	
		//Verify Test Procedures & Text
		Keywords.verifyObjectText("TstBeg_Body_TP_Text");
		Keywords.verifyObjectTextWithParameter("TstBeg_Body_TPContent_Text_First",d.getCellData("Test_Directions", "Test_Procedures",rowNum));
	
		//Verify Test Start message
		//Keywords.verifyObjectText("TstBeg_Body_mesage_Text");
		Keywords.verifyObjectTextWithParameter("TstBeg_Body_mesage_Text",d.getCellData("Test_Directions", "Start_Text",rowNum));
		
		//Verify Start button image in the body
		//Keywords.verifyAttributeValue("TstBeg_Body_Start_Img", "src","Start button on Test Directions page");
		if(currentTestName.contains("Diagnostic"))
			Keywords.elementDisplayed("TstBeg_Body_Start_Img");
		
		//Add a step to results
		if(testUtilResult.equals("Pass"))
			ReportUtil.addStep("Validate "+currentTestName+" Test Direction Page contents", "Contents are good", "Pass", null);
		else
			ReportUtil.addStep("Validate "+currentTestName+" Test Direction Page contents", "Something wrong with content", "Fail", screenshotPath+fileName);
		
		return testUtilResult;
	}


	public static String VerifyJasperSecStartPg(int secCounter) throws IOException{

		testUtilResult = "Pass";
		Excel_Ops d= new Excel_Ops(System.getProperty("user.dir")+"\\src\\Config\\"+currentDataXL+".xlsx");
		int rowNum = d.getCellRowNum("Test_Directions", "Sheet_Name", currentDatasheet);
		String currentSec = d.getCellData("Test_Directions", "Section"+(secCounter+1),rowNum);
		
		//Verify Headers : Logo & Text ::: Hard-coding review mode parameter to false since TestDirections comes only during test mode, not review mode
		if(isDiagORFL())
			verifyTestPageHeaders(d.getCellData("Test_Directions", "Title", rowNum).trim(), false);
		
		//Verify Section Name
		Keywords.verifyObjectTextWithParameter("SecBeg_Body_SecName_Text",currentSec);
		Keywords.dualOutput("current setion is           ",currentSec);
		
		//Verify Dismiss button image in the body
		//Keywords.verifyAttributeValue("SecBeg_Body_Dismiss_Button", "src","Dismiss button on section begin page");
		if(currentTestName.contains("Diagnostic"))
			Keywords.elementDisplayed("SecBeg_Body_Dismiss_Button");
		
		//Add a step to results
		if(testUtilResult.equals("Pass"))
			ReportUtil.addStep("Validate "+currentSec+" section begin page contents", "Contents are good", "Pass", null);
		else
			ReportUtil.addStep("Validate "+currentSec+" Test Direction Page contents", "Something wrong with content", "Fail", screenshotPath+fileName);
		
		return testUtilResult;
	}
	
	public static String verifyTestPageHeaders(String title, boolean rwMode) throws IOException{
		Keywords.dualOutput("Executing TestUtil - Test Page Headers method", null);
		submethodL1Result = "Pass";
		
		if(currentDataXL.equals("LessonsOnDemand"))
			Keywords.verifyObjectTextWithParameter("TstPg_Header_Exam_Text_LOD", title);
		else
			Keywords.verifyObjectTextWithParameter("TstPg_Header_Exam_Text", title);
		
		if(!(currentTestSuite.equals("Suite4")))
			Keywords.checkContains("TstPg_Header_Logo_"+currentTestSuite, "src",title+" page logo");
		
		return submethodL1Result;		
	}
	
	public static String VerifyJasperFooter() throws IOException{
		methodResult = "Pass";
		//Verify Jasper Footer Logo
		Keywords.verifyObjectText("Jsp_Footer_Logo").equals("Pass");
		
		//Verify Jasper Footer Twitter Text
		Keywords.verifyObjectText("Jsp_Footer_Twitter_Text");
		
		//Verify Jasper Footer Facebook Text
		Keywords.verifyObjectText("Jsp_Footer_FB_Text");
		
		//Add a step to results
		if(methodResult.equals("Pass")){
			Keywords.dualOutput("Page footer contents have been validated", null);
			ReportUtil.addStep("Validate Page Footer", "Page footer is verified successfully", "Pass", null);
		}
		else {
			Keywords.dualOutput("Something wrong with Page footer", null);
			ReportUtil.addStep("Validate Page Footer", "Something wrong with Page footer", "Fail", screenshotPath+fileName);
		}
		return methodResult;
	}
	
	public static boolean isDiagORFL(){
		if (currentTestName.contains("Diagnostic") || currentTestName.contains("Full-Length"))
			return true;
		else
			return false;
	}
	
	
	public static Boolean isExhibitApplicable(String section) throws IOException{
		
		Boolean result = false;
		
		switch (section){
			
			case "phySci":
				result = true;
				break;
			case "verbReas":
				result = false;
				break;
			case "bioSci":
				result = true;	
				break;
			case "bio":
				result = true;	
				break;
			case "gchem":
				result = true;	
				break;
			case "Ochem":
				result = true;	
				break;
			case "physics":
				result = true;	
				break;
		}
		return result;
	}
	
	
	public static String getAlphabet(int number) throws IOException{
		
		String alphabet = null;
		
		switch (number){
			
			case 1:
				alphabet = "A";
				break;
			case 2:
				alphabet = "B";
				break;
			case 3:
				alphabet = "C";	
				break;
			case 4:
				alphabet = "D";
				break;
			case 5:
				alphabet = "E";	
				break;
			case -1:
				alphabet = "";	
				break;
		}
		return alphabet;
	}
	
	public static int getNumberfromAlphabet(String alphabet) throws IOException{
		
		int number = 0;
		
		switch (alphabet){
			
			case "A":
				number = 1;
				break;
			case "B":
				number = 2;
				break;
			case "C":
				number = 3;	
				break;
			case "D":
				number = 4;
				break;
			case "E":
				number = 5;	
				break;
			case "":
				number = -1;	
				break;
		}
		return number;
	}


	public static boolean isMultiQuePsg (int rowNum){
		
		boolean result = false;
		Excel_Ops d= new Excel_Ops(System.getProperty("user.dir")+"\\src\\Config\\"+currentDataXL+".xlsx");
		Variable_Conversions vc = new Variable_Conversions();
		int psgQueNum = vc.strToDblToInt(d.getCellData(currentDatasheet, "PsgQuestion", rowNum));
		if( psgQueNum> 1)
			result = true;
		else if (vc.strToDblToInt(d.getCellData(currentDatasheet, "PsgQuestion", rowNum+1)) >1)
			result = true;
		else
			result = false;
		
		return result;
	}
	
	//Added by Resmi
	//Logging in through SHP
		public static String shpLogin() throws IOException {
			testUtilResult = "Pass";
			if(!currentBrowser.contains("Safari"))
				driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			try{
				Keywords.navigate("SHP_Login_Prev_URL");

			    if(Keywords.verifyTitle("SHP_Login_Prev_Title")){
			    	Keywords.dualOutput("Login:  Login Page sucessfully launched", null);

			    	//Logging into application
					Keywords.clearandinput("SHP_Login_Prev_Email");
					Keywords.clearandinput("SHP_Login_Prev_Pass");
					Keywords.mouseClick("SHP_Login_Prev_Login");
					
					if (testUtilResult.equals("Pass")){
						ReportUtil.addStep("Student Home Page Login", "Login Completed", testUtilResult,null);
						jasperLoggedin = true;
					}
					else
						ReportUtil.addStep("student Home page Login", "Login is not Successful", testUtilResult,screenshotPath+fileName);
									
				}else{
					Keywords.dualOutput("Login: Error loading login page", null);
					
					fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+"JspLog_Title.jpg";
					TestUtil.takeScreenShot(fileName);
					ReportUtil.addStep("Student Home Page Login", "Login page is not launched", testUtilResult,screenshotPath+fileName);
				}
			}
			catch(Exception error){
				System.out.println("error" + error);
	        }
	        finally{
	        	//driver.close();
	        }
			Keywords.dualOutput("Student Home Page Login step is completed", null);
			ReportUtil.addStep("Student Home Page Login", "Login Completed", testUtilResult,null);
			return testUtilResult;
		}	
		
		public static String VerifySHPHeader() throws IOException{
			testUtilResult = "Pass";
			// Verify Header Logo
			Keywords.verifyObjectText("SHP_Syllabus_Header_Logo");
					
			// Verify 'Online Center'
			Keywords.verifyObjectText("SHP_Syllabus_Online_Center");
			
			// Verify 'My Account'
			Keywords.verifyObjectText("SHP_Syllabus_My_Acct");

			// Verify 'Help'
			Keywords.verifyObjectText("SHP_Syllabus_Help");

			// Verify 'Log Out'
			Keywords.verifyObjectText("SHP_Syllabus_LogOut");

			// Verify 'Test Name'
			Keywords.verifyObjectTextWithParameter("SHP_Syllabus_Test_Header", currentTestName);
			
			// Verify 'Course Syllabus'
			Keywords.verifyObjectText("SHP_Syllabus_Course_header");
			
			// Verify 'All Resources'
			Keywords.verifyObjectText("SHP_Syllabus_AllRes");
			
			// Verify 'Refresh Page'
			Keywords.verifyObjectText("SHP_Syllabus_Refresh");

			return testUtilResult;
			
			// No need to add to ReportUtil as this method will be called from another
		}
		
		public static String VerifySHPFooter() throws IOException{
			testUtilResult = "Pass";
			// Verify 'Affliates'
			Keywords.verifyObjectText("SHP_Syllabus_Affliates");
					
			// Verify 'Corporate Programs'
			Keywords.verifyObjectText("SHP_Syllabus_CorpProgs");
			
			// Verify 'Work for Kaplan'
			Keywords.verifyObjectText("SHP_Syllabus_WorkKap");

			// Verify 'Press/Media'
			Keywords.verifyObjectText("SHP_Syllabus_Media");

			// Verify 'teach for Kaplan'
			Keywords.verifyObjectText("SHP_Syllabus_TeachKap");
			
			// Verify 'Contact Us'
			Keywords.verifyObjectText("SHP_Syllabus_Contact");

			// Verify 'Support'
			Keywords.verifyObjectText("SHP_Syllabus_Support");

			// Verify 'Notes'
			Keywords.verifyObjectText("SHP_Syllabus_Notes");
			
			// Verify 'Terms and Conditions'
			Keywords.verifyObjectText("SHP_Syllabus_Terms");

			// Verify 'California Privacy Policy'
			Keywords.verifyObjectText("SHP_Syllabus_CalPolicy");



			return testUtilResult;
			
			// No need to add to ReportUtil as this method will be called from another
		}
	
	
	
	
}
