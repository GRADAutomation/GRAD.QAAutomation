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
	
	//public static Properties config =null;
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
	
	// From DriverClass
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
	
	
	@BeforeSuite
	public void initialize() throws IOException{
		// loading all the configurations from a property file
		APPLICATION_LOGS.debug("Starting the test suite");
		APPLICATION_LOGS.debug("Loading config files");
		
		// load the object repository
		APPLICATION_LOGS.debug("Loading Object XPATHS");
		objects=new Excel_Ops(System.getProperty("user.dir")+"\\src\\Config\\ObjectRepository.xlsx");
		OR=TestUtil.getExcelintoArray(objects,"OR");

		// initialize Input Control sheet
		controller=new Excel_Ops(System.getProperty("user.dir")+"\\src\\Config\\ControllerNew.xlsx");
		
		environment = controller.getCellData("Settings", "Value", controller.getFirstRowInstance("Settings", "Parameter","Environment"));
		execution_mode = controller.getCellData("Settings", "Value", controller.getFirstRowInstance("Settings", "Parameter","Execution_Mode"));
		currentProduct = controller.getCellData("Settings", "Value", controller.getFirstRowInstance("Settings", "Parameter","Product")); 
		System.out.println(environment);
		System.out.println(execution_mode);
		//evfw.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS );		
	}
	
	@BeforeClass
	public static void startTesting(){
		String TimeNow = TestUtil.now("yyMMddHHmmss"); 
		new File(System.getProperty("user.dir")+"//CustomOutput//"+TimeNow).mkdirs();
		ReportUtil.startTesting(System.getProperty("user.dir")+"/CustomOutput/"+TimeNow+"//index.html", 
                TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"), environment,"1.1.3");
		ReportUtil.startFailedTestReport(System.getProperty("user.dir")+"/CustomOutput/"+TimeNow+"//Failed.html",environment);		
		ReportUtil.startSuite("MCAT Test Suite");
		System.out.println("StartTesting completed");
	}
	
	@Test(dataProvider="getData")
	public void testApp(String browser, String loginID, String password) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, IOException {
		String startTime=null; currentBrowser = browser; currentLoginID = loginID; currentPassword = password;
		System.out.println(currentBrowser);
		TestUtil.initializeDriver();
		evfw.manage().timeouts().implicitlyWait(180,TimeUnit.SECONDS );
		
		//Reflection dynamic class & method loader
		ClassLoader myClassLoader=ClassLoader.getSystemClassLoader();
		Class myClass;
		Object whatInstance;
		Method myMethod;
		String returnValue = null;
		System.out.println("Reflection method loaded");
		
		for(int tcid=2; tcid<=controller.getRowCount(currentProduct);tcid++){
			
			currentTCID = controller.getCellData(currentProduct, "TCID", tcid);
			currentTestName = controller.getCellData(currentProduct, "TestName", tcid);
			currentTestSuite = controller.getCellData(currentProduct, "TestSuite", tcid);
			currentPackage = controller.getCellData(currentProduct, "Package", tcid);
			currentCaseName = controller.getCellData(currentProduct, "CaseName", tcid);
			currentCaseDescription = controller.getCellData(currentProduct, "Description", tcid);
			currentDataXL= controller.getCellData(currentProduct, "DataXL", tcid);
			currentDatasheet= controller.getCellData(currentProduct, "DataSheet", tcid);
			currentScriptName= controller.getCellData(currentProduct, "ScriptName", tcid);
			currentMainTopic= controller.getCellData(currentProduct, "MainTopic", tcid);
			tcBrowser= controller.getCellData(currentProduct, "Browser", tcid);
			
			//Printing stack
			System.out.println(currentTCID);
			System.out.println(currentTestName);
			System.out.println(currentPackage);
			System.out.println(currentScriptName);
			System.out.println(currentCaseDescription);
			System.out.println(currentDatasheet);
			System.out.println(controller.getCellData(currentProduct, "Run", tcid));
			
			if (execution_mode.equals("Hub-Node-Mixed")){
				if(tcBrowser.equals(browser))
					isRun = true;
				else
					isRun = false;
			} else
				isRun = true;
			
			// initialize the start time of test
			if(controller.getCellData(currentProduct, "Run", tcid).equals("Y") && isRun){
				startTime=TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa");
				APPLICATION_LOGS.debug("Executing the test "+ currentCaseName + " under the Test name " + currentTestName);
				// Reflection API
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
			
			controller.setCellData(currentProduct, "Result", tcid, testStatus);
			testStatus=null; isRun = false;
			
		} // end of for loop
		ReportUtil.endSuite();
	} //end of class
	
	@DataProvider (parallel = false)
	public Object[][] getData(){
		return TestUtil.getData(currentDatasheet);
	}
	
	@AfterClass
	public static void endScript(){
		
		ReportUtil.updateEndTime(TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"));
/*		
		TestUtil.zip(System.getProperty("user.dir")+"\\CustomOutput");
    	String[] to={"Siva.Vanapalli@kaplan.com"};
        String[] cc={};
        String[] bcc={};
        
        
*/        
	}

}

	
	