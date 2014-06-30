/* ParentClasses package contain DriverScript class files that reads Object Repository
 * and ControllerNew excels to drive the execution. OR excel will be read into JAVA
 * arrays to make the execution quicker, although it takes few mins to load at the beginning.
 * Once execution completes, Custom-Output folder will contain the output based on the date & time.
 */

package ParentClasses;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Reports.ReportUtil;
import Utility.ErrorUtil;
import Utility.Keywords;
import Utility.SendMail;
import Utility.TestUtil;
import Database.Excel_Ops;

public class DriverScript {
	// All initialization
	public static WebDriver driver = null;
	public static EventFiringWebDriver evfw=null; 
	public static Logger APPLICATION_LOGS = Logger.getLogger("devpinoyLogger"); //devpinoyLogger
	public static boolean loggedIn=false;
	public static Excel_Ops controller=null;
	public static Excel_Ops testdataxl=null;
	public static Excel_Ops objects=null;
	public static String fileName = null;
	public static boolean jasperLoggedin = false;
	public static String browserType = "Firefox";
	
	public static String classResult = "Pass";
	public static String methodResult = "Pass";
	public static String keywordResult = "Pass";
	public static String testUtilResult = "Pass";
	public static String submethodL1Result = "Pass";
	public static String submethodL2Result = "Pass";
	public static String screenshotPath= System.getProperty("user.dir")+"/Screenshots/";
	
	public static String environment = null;
	public static String execution_mode = null;
	public static String currentBrowser = null;
	public static String currentLoginID = null;
	public static String currentPassword = null;
	
	public static String[][] OR;
	
	public static String currentTCID;
	public static String currentTestName;
	public static String currentTestSuite;
	public static String currentPackage;
	public static String currentCaseName;
	public static String currentCaseDescription;
	public static String currentDataXL;
	public static String currentDatasheet;
	public static String testStatus;
	public static String currentScriptName;
	public static String currentMainTopic;
	public static String currentProduct; 
	public static String tcBrowser;
	public static boolean isRun=false;
	public static boolean clearhistory;
	
	
	@BeforeSuite
	public void initialize() throws IOException{

		APPLICATION_LOGS.debug("Starting the test suite");
		APPLICATION_LOGS.debug("Loading config files");
		
		// load the object repository
		APPLICATION_LOGS.debug("Loading Object Repository");
		objects=new Excel_Ops(System.getProperty("user.dir")+"\\src\\Config\\ObjectRepository.xlsx");
		OR=TestUtil.getExcelintoArray(objects,"OR");

		// initialize Input Control sheet - ControllerNew, load environment and execution modes
		controller=new Excel_Ops(System.getProperty("user.dir")+"\\src\\Config\\ControllerNew.xlsm");
		
		environment = controller.getCellData("Settings", "Value", controller.getFirstRowInstance("Settings", "Parameter","Environment"));
		execution_mode = controller.getCellData("Settings", "Value", controller.getFirstRowInstance("Settings", "Parameter","Execution_Mode"));
		//currentProduct = controller.getCellData("Settings", "Value", controller.getFirstRowInstance("Settings", "Parameter","Product")); 
	}
	
	@BeforeClass // Activities to be performed prior to beginning the class : Initializing reporting aspects
	public static void startTesting(){
		String TimeNow = TestUtil.now("yyMMddHHmmss"); 
		new File(System.getProperty("user.dir")+"//CustomOutput//"+TimeNow).mkdirs();
		ReportUtil.startTesting(System.getProperty("user.dir")+"/CustomOutput/"+TimeNow+"//index.html", 
                TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"), environment,"1.1.3");
		ReportUtil.startFailedTestReport(System.getProperty("user.dir")+"/CustomOutput/"+TimeNow+"//Failed.html",environment);		
		ReportUtil.startSuite("MCAT Test Suite");
		System.out.println("StartTesting completed");
	}
	
	//Dataprovider of TestNG reads browser related data from Settings sheet of Inputcontroller
	@Test(dataProvider="getData")
	public void testApp(String browser, String loginID, String password) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, IOException, InterruptedException {
		String startTime=null; currentBrowser = browser; currentLoginID = loginID; currentPassword = password;

		TestUtil.initializeDriver();
		evfw.manage().timeouts().implicitlyWait(180,TimeUnit.SECONDS );
		clearhistory = false;	
		
		//Reflection dynamic class & method loader
		ClassLoader myClassLoader=ClassLoader.getSystemClassLoader();
		Class myClass;
		Object whatInstance;
		Method myMethod;
		String returnValue = null;
		System.out.println("Reflection method loaded");
		
		// Modified to read data from Consolidated sheet instead of going through each and every product sheet
		for(int tcid=2; tcid<=controller.getRowCount("Consolidated");tcid++){
			
			currentProduct = controller.getCellData("Consolidated", "Product", tcid);
			currentTCID = controller.getCellData("Consolidated", "TCID", tcid);
			currentTestName = controller.getCellData("Consolidated", "TestName", tcid);
			currentTestSuite = controller.getCellData("Consolidated", "TestSuite", tcid);
			currentPackage = controller.getCellData("Consolidated", "Package", tcid);
			currentCaseName = controller.getCellData("Consolidated", "CaseName", tcid);
			currentCaseDescription = controller.getCellData("Consolidated", "Description", tcid);
			currentDataXL= controller.getCellData("Consolidated", "DataXL", tcid);
			currentDatasheet= controller.getCellData("Consolidated", "DataSheet", tcid);
			currentScriptName= controller.getCellData("Consolidated", "ScriptName", tcid);
			currentMainTopic= controller.getCellData("Consolidated", "MainTopic", tcid);
			tcBrowser= controller.getCellData("Consolidated", "Browser", tcid);
			
			// Full Length and Diagnosis test related data sheets are macro driven and hence .xlsm while others are all .xlsx
			if(currentDataXL.contains("FL") || currentDataXL.contains("Diag"))
				currentDataXL = currentDataXL + ".xlsm";
			else
				currentDataXL = currentDataXL + ".xlsx";
			
			if(clearhistory) // clears history of Jasper application if it's not already done so
				TestUtil.clearhistory();
			
			//Printing stack
			System.out.println(currentTCID);
			System.out.println(currentTestName);
			System.out.println(currentPackage);
			System.out.println(currentScriptName);
			System.out.println(currentCaseDescription);
			System.out.println(currentDatasheet);
			System.out.println(controller.getCellData("Consolidated", "Run", tcid));
			
			if (execution_mode.equals("Hub-Node-Mixed")){
				if(tcBrowser.equals(browser))
					isRun = true;
				else
					isRun = false;
			} else
				isRun = true;
			
			// initialize the start time of test
			if(controller.getCellData("Consolidated", "Run", tcid).equals("Y") && isRun){
				startTime=TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa");
				APPLICATION_LOGS.debug("Executing the test "+ currentCaseName + " under the Test name " + currentTestName);
				
				// Reflection API to ensure DriverScript holds controls while stub gets executed and hands-over control back to Driver
				try {
					myClass = myClassLoader.loadClass(currentPackage+"."+currentScriptName);
					whatInstance = myClass.newInstance();
					myMethod = myClass.getMethod("completeFlowTest", new Class[] {String.class});
					returnValue = (String) myMethod.invoke(whatInstance, new Object[] { currentDatasheet});
					
					System.out.println("The value returned from the method is:" + returnValue);
				}
				catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}	// end of catch
				
				
				if(returnValue.startsWith("Fail"))
					testStatus="Fail";
				else if(returnValue == null)
					testStatus = "Fail";
				else
					testStatus = returnValue;
				APPLICATION_LOGS.debug("***********************************"+currentTCID+" --- " +testStatus);
				ReportUtil.addTestCase(currentTCID, currentTestName, currentCaseDescription,startTime, TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"), testStatus, currentBrowser );
			}//end of if 
			else{
				APPLICATION_LOGS.debug("Skipping the test "+ currentTCID);
				testStatus="Skip";
				// report skipped
				APPLICATION_LOGS.debug("***********************************"+currentTCID+" --- " +testStatus);
				ReportUtil.addTestCase(currentTCID, currentTestName, currentCaseDescription,TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"), TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"), testStatus, currentBrowser );
			} // end of else
			
			//controller.setCellData("Consolidated", "Result", tcid, testStatus); //blocking this as it is not mandatory since detailed reporting is available
			testStatus=null; isRun = false;
		} // end of for loop
		ReportUtil.endSuite();
	} //end of class
	
	@DataProvider (parallel = false) // parallel = True ensure concurrent execution under multiple threads
	public Object[][] getData(){
		return TestUtil.getData(currentDatasheet);
	}
	
	@AfterClass // 
	public static void endScript(){
		
		ReportUtil.updateEndTime(TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"));
/*		
 * ENACBLE THIS to trigger emailing
		TestUtil.zip(System.getProperty("user.dir")+"\\CustomOutput");
    	String[] to={"Siva.Vanapalli@kaplan.com"};
        String[] cc={};
        String[] bcc={};
        
        
*/        
	}

}

	
	
