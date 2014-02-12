package Utility;
//http://www.vogella.de/articles/JavaRegularExpressions/ar01s05.html
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
//import org.apache.commons.lang.StringUtils;



import Utility.ErrorUtil;
import Utility.TestUtil;
import ParentClasses.DriverScript;
import Reports.ReportUtil;

public class Keywords extends DriverScript{

	// This method navigates to the URL given for the obj name in the OR spreadsheet
	public static String navigate(String objName) throws IOException{
		dualOutput("Executing Navigate for the object: ",objName);
		keywordResult = "Pass";
		try{
		evfw.navigate().to(TestUtil.getStringValueinArray(OR,objName,"Value"));
		}catch(Throwable t){
			// report error
			dualOutput("Error while navigating",t.getMessage());
			ErrorUtil.addVerificationFailure(new Throwable("COULD NOT NAVIGATE"));
			fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_navigate.jpg";
			TestUtil.takeScreenShot(fileName);
			ReportUtil.addStep("Navigate to: "+objName, "Error while navigating", "Fail", screenshotPath+fileName);
			classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
			return keywordResult;
		}
		
		return keywordResult;
		
	}
	
	// This method navigates to the URL specified as the parameter
	public static String navigatebyURL(String URL) throws IOException{ 
		dualOutput("Executing Navigate by URL to: ", URL);
		keywordResult = "Pass";
		try{
		evfw.navigate().to(URL);
		}catch(Throwable t){
			// report error
			dualOutput("Error while navigating", t.getMessage());
			ErrorUtil.addVerificationFailure(new Throwable("COULD NOT NAVIGATE"));
			fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_navigate.jpg";
			TestUtil.takeScreenShot(fileName);
			ReportUtil.addStep("Navigate to: "+URL, "Error while navigating", "Fail", screenshotPath+fileName);
			classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
			return keywordResult;
		}
		
		return keywordResult;
		
	}
	
	// This method clicks on the link specified
	public static String clickLink(String objName) throws IOException{
		dualOutput("Executing clickLink: ", objName);
		keywordResult = "Pass";
		try{
		TestUtil.findElement(objName).click();
		}catch(Throwable t){
			// report error
			dualOutput("Error while clicking on link -", objName + t.getMessage());
			ErrorUtil.addVerificationFailure(new Throwable("Error while clicking on link"));
			fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
			TestUtil.takeScreenShot(fileName);
			ReportUtil.addStep("Click the link: "+objName, "Error clicking link", "Fail", screenshotPath+fileName);
			classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
			return keywordResult;
		}
		
		return keywordResult;
	}
	
	// This method clicks on the radio button 
	public static String clickRadioButton(String objName) throws IOException{
		dualOutput("Executing clickRadioButton: ", objName);
		keywordResult = "Pass";
		try{
		TestUtil.findElement(objName).click();
		}catch(Throwable t){
			// report error
			dualOutput("Error while clicking on radio button -", objName + t.getMessage());
			ErrorUtil.addVerificationFailure(new Throwable("Error while clicking on radio button"));
			fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
			TestUtil.takeScreenShot(fileName);
			ReportUtil.addStep("Click radio button: "+objName, "Error clicking radio button", "Fail", screenshotPath+fileName);
			classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
			return keywordResult;
		}
		
		return keywordResult;
	}
	
	// This method clicks on the radio button based on the xpath specified as the parameter
	public static String clickRadioButtonCustom(String objName) throws IOException{
		dualOutput("Executing clickRadioButtonCustom (based on Xpath): ", objName);
		keywordResult = "Pass";
		try{
		TestUtil.findCustomElement(objName).click();
		}catch(Throwable t){
			// report error
			dualOutput("Error while clicking on radio button -", objName + t.getMessage());
			fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
			TestUtil.takeScreenShot(fileName);
			ReportUtil.addStep("Click radio button: "+objName, "Error clicking radio button", "Fail", screenshotPath+fileName);
			classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
			return keywordResult;
		}
		
		return keywordResult;
	}
	
	// This method clicks on LinkText based on LinkText
	public static String clickLinkText(String linkText) throws IOException{
		dualOutput("Executing Linktext: ", linkText);
		keywordResult = "Pass";
		
		try{
		evfw.findElement(By.linkText(linkText)).click();
		}catch(Throwable t){
			// report error
			dualOutput("Error while clicking on link -", linkText + t.getMessage());
			fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_linktext.jpg";
			TestUtil.takeScreenShot(fileName);
			ReportUtil.addStep("Click the link: "+linkText, "Error clicking link", "Fail", screenshotPath+fileName);
			classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
			return keywordResult;
		}
		
		return keywordResult;
	}
	
	// This method is a non-keyword specific method which clicks on a link and verify whether the desired page is displayed
	public static String clickLinkandVerify(String objNametoClick, String objNametoVerify) throws IOException, InterruptedException{
		dualOutput("Executing clickLink and Verify: ", objNametoVerify);
		keywordResult = "Pass"; String token1 = null; String token2 = null;
		
		if (objNametoClick.equals(currentTestName)){
			token1 = clickLinkText(currentTestName);
		}
		else
			token1 = clickLink(objNametoClick);
		
		if (objNametoClick.equals("PS_Header_Logo")||objNametoClick.equals("ItmRw_Nav_Performance_Link"))
			Thread.sleep(2000);
		else	
			Thread.sleep(1000);
		if (!objNametoClick.equals(currentTestName))
			token2 = verifyObjectTextWithParameter(objNametoVerify,TestUtil.getStringValueinArray(OR,objNametoClick,"Value"));
		else
			token2 = token1;
		
		if (token1.equals("Pass") & token2.equals("Pass")){
			keywordResult = "Pass";
			ReportUtil.addStep("Verify Text: "+objNametoClick, "Verfified Successfully", "Pass", null);
		}
		else {
			keywordResult = "Fail";
			dualOutput("Error while clicking Link and Verifying -", null);
			objNametoClick.replaceAll("*", "").replaceAll("//", "");
			fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objNametoClick+".jpg";
			TestUtil.takeScreenShot(fileName);
			ReportUtil.addStep("Verify Text: "+objNametoClick, "Could NOT clickLinkandVerify", "Fail", screenshotPath+fileName);
			classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
			
			return keywordResult; 
		}
		
		return keywordResult;
	}
	
	//This method verifies the text of the object in reference (text to be compared is given in the OR excel along with the obj name)
	public static String verifyObjectText(String objName) throws IOException{
		dualOutput("Executing verifyObjectText ", objName);
		keywordResult = "Pass"; String actualText = null;
		
		String expectedText = TestUtil.getStringValueinArray(OR,objName,"Value");	
		
		try{
			actualText=TestUtil.findElement(objName).getText();
			dualOutput(expectedText, null);
			dualOutput(actualText, null);
			Assert.assertEquals(expectedText.trim(), actualText.trim());
			ReportUtil.addStep("Verify Text: "+ expectedText, "Verfified Successfully", "Pass", null);		
		}catch(Throwable t){
			// error
			dualOutput("Error in text - ", objName);
			dualOutput("Actual - ", actualText);
			dualOutput("Expected -", expectedText);
			fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
			TestUtil.takeScreenShot(fileName);
			ReportUtil.addStep("Verify Text: "+ expectedText, "Not Successful", "Fail", screenshotPath+fileName);
			classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
			
			return keywordResult; 
			
		}
		return keywordResult;
	}
	
	//This method verifies the text of the object in reference with the expected text given as the parameter
	public static String verifyObjectTextWithParameter(String objName, String expectedText) throws IOException{
		dualOutput("Executing verifyObjectTextWithParameter: ",expectedText);
		keywordResult = "Pass"; String actualText=null;
		
		try{
			actualText=TestUtil.findElement(objName).getText();
			dualOutput(expectedText, null);
			dualOutput(actualText, null);
			Assert.assertEquals(expectedText.trim(), actualText.trim());
			ReportUtil.addStep("Verify Text: "+ expectedText, "Verfified Successfully", "Pass", null);
					
		}catch(Throwable t){
			// error
			dualOutput("Error in text - ",objName);
			dualOutput("Actual - ",actualText);
			dualOutput("Expected -", expectedText);
			
			fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
			TestUtil.takeScreenShot(fileName);
			classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
			ReportUtil.addStep("Verify Text: "+ expectedText, "Couldn't locate text", "Fail", screenshotPath+fileName);
			return keywordResult; 
			
		}
		return keywordResult;
	}
	
	//This method verifies the text of the object in reference with the expected text given as the parameter	
	public static String verifyCustomObjectText(String objName, String expectedText) throws IOException{
		dualOutput("Executing verifyCustomObjectText: ", expectedText);
		keywordResult = "Pass"; String actualText= null;
		
		try{		
			actualText=getCustomObjectText(objName);
			dualOutput(expectedText, null);
			dualOutput(actualText, null);
			
			Assert.assertEquals(expectedText.trim(), actualText.trim());
			dualOutput("Verified Custom ObjectText: ", expectedText);
			ReportUtil.addStep("Verify Text: "+ expectedText, "Verfified Successfully", "Pass", null);
		}catch(Throwable t){
			// error
			dualOutput("Error in text - ", objName);
			dualOutput("Actual - ", actualText);
			dualOutput("Expected -",  expectedText);

			fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
			TestUtil.takeScreenShot(fileName);
			ReportUtil.addStep("Verify Text: "+ expectedText, "Couldn't locate text", "Fail", screenshotPath+fileName);
			classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
			
			return keywordResult; 
			
		}
		return keywordResult;
	}
	
	//This method extracts the text based on the xpath provided as the parameter
	public static String getCustomObjectText(String objName) throws IOException{
		dualOutput("Executing getCustomObjectText", null);
		keywordResult = "Pass"; String resulttext = null;

		try{
			resulttext = TestUtil.findCustomElement(objName).getText();
		}catch(Throwable t){
			// error
			dualOutput("Error retrieving text - ", objName);
			fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
			TestUtil.takeScreenShot(fileName);
			ReportUtil.addStep("Get text ", "Couldn't locate text", "Fail", screenshotPath+fileName);
			classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
			
			return resulttext; 
			
		}
		return resulttext;
	}
	
	//This method extracts the text of the object in reference
	public static String getObjectText(String objName) throws IOException{
		dualOutput("Executing getCustomObjectText", null);
		keywordResult = "Pass";

		try{
			String actualText=TestUtil.findElement(objName).getText();
			if(keywordResult.equals("Pass"))
					keywordResult = actualText;
		}catch(Throwable t){
			// error
			dualOutput("Error retrieving text - ", objName);
			fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
			TestUtil.takeScreenShot(fileName);
			ReportUtil.addStep("Get text ", "Couldn't locate text", "Fail", screenshotPath+fileName);
			classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
			
			return keywordResult; 
			
		}
		return keywordResult;
	}
	
	//This method extracts the value of the attribute (attribute should be passed as the parameter and objname should be xpath)
	public static String getCustomAttribute(String objName, String attributeParameter) throws IOException{
		dualOutput("Executing Get Attribute: ", objName);
		dualOutput("Executing Get Attribute: ", objName);
		keywordResult = "Pass";

		try{
			keywordResult=TestUtil.findCustomElement(objName).getAttribute(attributeParameter);
		}catch(Throwable t){
			// error
			dualOutput("Error in getting attribute value - ", objName); 
			fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
			TestUtil.takeScreenShot(fileName);
			ReportUtil.addStep("GetAttribute("+attributeParameter+") of "+objName, "Couldn't retrieve attribute", "Fail", screenshotPath+fileName);
			classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
			
			return keywordResult; 
		}
		return keywordResult;
	}
	
	//This method extracts the value of the attribute (attribute should be passed as the parameter and objname can be of any identifier)
	public static String getAttribute(String objName, String attributeParameter) throws IOException{
		dualOutput("Executing Get Attribute: ", objName);
		keywordResult = "Pass";

		try{
			keywordResult = TestUtil.findElement(objName).getAttribute(attributeParameter);
		}catch(Throwable t){
			// error
			dualOutput("Error in getting attribute value - ", objName); 
			fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
			TestUtil.takeScreenShot(fileName);
			ReportUtil.addStep("GetAttribute("+attributeParameter+") of "+objName, "Couldn't retrieve attribute", "Fail", screenshotPath+fileName);
			classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
			
			return keywordResult; 
		}
		return keywordResult;
	}
	
	//This method verified the value of the attribute (attribute should be passed as the parameter and objname should be xpath only)
	public static String verifyCustomAttributeValue(String objName, String attributeParameter, String expectedValue, String whatAttribute) throws IOException{
		dualOutput("Executing verifyCustomAttributeValue: ", objName);
		keywordResult = "Pass";
		
		String attributeValue = getCustomAttribute(objName,attributeParameter);

		dualOutput(expectedValue, null);
		dualOutput(attributeValue, null);
		
		try{
			Assert.assertEquals(expectedValue.trim(), attributeValue.trim());
			ReportUtil.addStep("Verify attribute value of: "+ whatAttribute, "Verfified Successfully", "Pass", null);		
		}catch(Throwable t){
			// error
			dualOutput("Error in attribute value of object - ", objName);
			dualOutput("Actual - ", attributeValue);
			dualOutput("Expected -", expectedValue);
			fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
			TestUtil.takeScreenShot(fileName);
			ReportUtil.addStep("Verify attribute value of: "+ whatAttribute, "Not Successful", "Fail", screenshotPath+fileName);
			classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
			
			return keywordResult;
		}
		return keywordResult;
	}
	
	//This method verified the value of the attribute (both objname and attribute comes from OR excel)
	public static String verifyAttributeValue(String objName, String attributeParameter, String whatAttribute) throws IOException{
		dualOutput("Executing verifyAttributeValue: ", objName);
		dualOutput("Executing verifyAttributeValue: ", objName);
		keywordResult = "Pass";
		
		String attributeValue = getAttribute(objName,attributeParameter);
		String expectedValue = TestUtil.getStringValueinArray(OR,objName,"Value");

		dualOutput(expectedValue, null);
		dualOutput(attributeValue, null);
		dualOutput("Expected value is: " ,expectedValue);
		dualOutput("Actual value is: " ,attributeValue);
		
		try{
			Assert.assertEquals(expectedValue.trim(), attributeValue.trim());
			ReportUtil.addStep("Verify attribute value of: "+ whatAttribute, "Verfified Successfully", "Pass", null);		
		}catch(Throwable t){
			// error
			dualOutput("Error in attribute value of object - ", objName);
			dualOutput("Actual - ", attributeValue);
			dualOutput("Expected -", expectedValue);
			dualOutput("Inside Catchblock of verifyAttributeValue", null);
			fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
			TestUtil.takeScreenShot(fileName);
			ReportUtil.addStep("Verify attribute value of: "+ whatAttribute, "Not Successful", "Fail", screenshotPath+fileName);
			classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
			
			return keywordResult;
		}
		dualOutput("verifyAttributeValue - return value is: ", keywordResult);
		return keywordResult;
	}
	
	//Compares two strings and outputs pass /fail
	public static String verifyText(String actualText, String expectedText) throws IOException{
		dualOutput("Executing verifyText: ", expectedText);
		keywordResult = "Pass";
		
		dualOutput(expectedText, null);
		dualOutput(actualText, null);
		try{
			Assert.assertEquals(expectedText.trim(), actualText.trim());
					
		}catch(Throwable t){
			// error
			dualOutput("Error in text - ", null);
			dualOutput("Actual - ", actualText);
			dualOutput("Expected -", expectedText);
			
			fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+"verifyText"+".jpg";
			TestUtil.takeScreenShot(fileName);
			ReportUtil.addStep("Verify Text: "+expectedText, "Error with text validation", "Fail", screenshotPath+fileName);
			classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
			
			return keywordResult; 
			
		}
		return keywordResult;
	}
	
	//This method compares Title of the page with expected title and outputs pass / fail
	//need to change
	public static Boolean verifyTitle(String expectedTitle) throws IOException{
		dualOutput("Verifying the title match: ", expectedTitle);
		keywordResult = "Pass";
		
		try{
			evfw.getTitle().matches(expectedTitle);
			ReportUtil.addStep("Verify Title: "+expectedTitle, "Verified Successfully", "Pass", null);		
		}catch(Throwable t){
			// error
			dualOutput("Actual - ", evfw.getTitle());
			dualOutput("Expected -", expectedTitle.trim());
			
			fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_Title.jpg";
			TestUtil.takeScreenShot(fileName);
			ReportUtil.addStep("Verify Title: "+expectedTitle, "Error with title validation", "Fail", screenshotPath+fileName);
			classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
			
			return false; 			
		}
		return true;		
	}
	
	// This method clicks on the button corresponds to the object in reference
	public static String clickButton(String objName) throws IOException{
		dualOutput("Executing clickButton Keyword: ", objName);
		keywordResult = "Pass";
		
		try{
			TestUtil.findElement(objName).click();
			}catch(Throwable t){
				// report error
				dualOutput("Error while clicking on Button -", objName + t.getMessage());
				fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
				TestUtil.takeScreenShot(fileName);
				ReportUtil.addStep("Click the button: "+objName, "Error clicking button", "Fail", screenshotPath+fileName);
				classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
				
				return keywordResult;
			}
			
			return keywordResult;
	}
	
	//This method extracts the text of the element based on xpath only
	public static String getTextCustomElement(String objName, String whatObject) throws IOException{
		dualOutput("Executing get text custom element Keyword: ", whatObject);
		String resultText = null;	keywordResult = "Pass";	
		try{
			resultText = TestUtil.findCustomElement(objName).getText();
			}catch(Throwable t){
				// report error
				dualOutput("Error while retrieving text of: ", whatObject + t.getMessage());
				
				fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+"Custom_Elmnt"+".jpg";
				TestUtil.takeScreenShot(fileName);
				ReportUtil.addStep("Get text of: "+whatObject, "Error retrieving text", "Fail", screenshotPath+fileName);
				classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
				return keywordResult;
			}
			
			return resultText;
	}
	
	//This method clicks on the object with xpath specified
	public static String clickbyXpath(String objName) throws IOException{
		dualOutput("Executing clickbyXpath Keyword: ", objName);
		keywordResult = "Pass";
				
		try{
			TestUtil.findCustomElement(objName).click();
			dualOutput("Object at xpath: ", objName+" is clicked");
			}catch(Throwable t){
				// report error
				dualOutput("Error while clicking on Button -", objName + t.getMessage());
				fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
				TestUtil.takeScreenShot(fileName);
				ReportUtil.addStep("Click: "+objName, "Error clicking the object", "Fail", screenshotPath+fileName);
				classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
				
				return keywordResult;
			}
			
			return keywordResult;
	}
	
	//This method takes care of switching frames - including activeElement and default Content
	public static String frameSwitch(String objName) throws IOException{
		dualOutput("Switching to frame", objName);
		keywordResult = "Pass";
				
		try{
			
			if (objName.equals("activeElement"))
				evfw.switchTo().activeElement();
			else if (objName.equals("defaultContent"))
				evfw.switchTo().defaultContent();
			else
				evfw.switchTo().frame(objName);
			}
		catch (UnhandledAlertException e){
			Alert alert = driver.switchTo().alert();
			alert.accept();
			
			String ele = frameSwitch(objName);
			if(ele.equals("Pass"))
				return "Pass";
			else
				return "Fail";
				
		}
		
		catch(Throwable t) {
				// report error
				dualOutput("Error while switching to frame -", objName + t.getMessage());
				fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
				TestUtil.takeScreenShot(fileName);
				ReportUtil.addStep("Switch to frame: "+objName, "Error switching frame", "Fail", screenshotPath+fileName);
				classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
				
				return keywordResult;
			}
			
			return keywordResult;
	}
	
	//This method takes care of selecting a value from the selectlist - objname and value to be selected come from OR excel
	public static String selectfromSelectList(String objName) throws IOException{
		dualOutput("Executing selectfromSelectList Keyword: ", objName);
		keywordResult = "Pass";
		String valueToChoose = TestUtil.getStringValueinArray(OR,objName,"Value");
		
		try{
			new Select(TestUtil.findElement(objName)).selectByVisibleText(valueToChoose);
			}catch(Throwable t){
				// report error
				dualOutput("Error while Selecting from droplist -", objName + t.getMessage());
				
				//objName.replaceAll("*", "").replaceAll("//", "");
				fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
				TestUtil.takeScreenShot(fileName);
				ReportUtil.addStep("Choose from selectList: "+valueToChoose, "Error during selecting", "Fail", screenshotPath+fileName);
				classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
				
				return keywordResult;
			}
			
			return keywordResult;
	}
	
	//This method takes care of selecting a value from the selectlist - value should be passed as a parameter
	public static String selectfromSelectList(String objName, String valueToChoose) throws IOException{
		dualOutput("Executing selectfromSelectList Keyword: ", objName);
		keywordResult = "Pass";
		
		try{
			new Select(TestUtil.findElement(objName)).selectByVisibleText(valueToChoose);
			}catch(Throwable t){
				// report error
				dualOutput("Error while Selecting from droplist -", objName + t.getMessage());
				fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
				TestUtil.takeScreenShot(fileName);
				ReportUtil.addStep("Choose from selectList: "+valueToChoose, "Error during selecting", "Fail", screenshotPath+fileName);
				classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
				
				return keywordResult;
			}
			
			return keywordResult;
	}
	
	//Added on 1/21	
	//This method takes care of selecting a value from the selectlist - index instead of visibletext - need to input as parameter
	public static String selectfromSelectListByIndex(String objName, int index) throws IOException{
		dualOutput("Executing selectfromSelectListByIndex Keyword: ", objName);
		keywordResult = "Pass";
		
		try{
			new Select(TestUtil.findElement(objName)).selectByIndex(index);
			}catch(Throwable t){
				// report error
				dualOutput("Error while Selecting from droplist -", objName + t.getMessage());
				fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
				TestUtil.takeScreenShot(fileName);
				ReportUtil.addStep("Choose from selectList: "+index, "Error during selecting", "Fail", screenshotPath+fileName);
				classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
				
				return keywordResult;
			}
			
			return keywordResult;
	}
	
	//This method inputs value into a text field - value to be entered comes from OR excel
	public static String input(String objName) throws IOException{
		dualOutput("Executing select Keyword: ", objName);
		keywordResult = "Pass";
		String texttobeentered=TestUtil.getStringValueinArray(OR,objName,"Value");
		
		try{
			TestUtil.findElement(objName).sendKeys(texttobeentered);
			}catch(Throwable t){
				// report error
				dualOutput("Error while Selecting from droplist -", objName + t.getMessage());
				fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
				TestUtil.takeScreenShot(fileName);
				ReportUtil.addStep("Enter text: "+texttobeentered, "Error entering the text", "Fail", screenshotPath+fileName);
				classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
				
				return keywordResult;
			}
			
			return keywordResult;
	}
	
	//This method inputs value into a text field - value to be inputted should be passed as a parameter
	public static String inputValue(String objName, String value) throws IOException{
		dualOutput("Executing select Keyword: ", objName);
		keywordResult = "Pass";
		
		try{
			TestUtil.findElement(objName).sendKeys(value);
			}catch(Throwable t){
				// report error
				dualOutput("Error while Selecting from droplist -", objName + t.getMessage());
				fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
				TestUtil.takeScreenShot(fileName);
				ReportUtil.addStep("Enter text in field: "+objName, "Error entering the text", "Fail", screenshotPath+fileName);
				classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
				
				return keywordResult;
			}
			
			return keywordResult;
	}
	
	//This method inputs value into a text field - value to be inputted should be passed as a parameter
	public static String customInputValue(String objName, String value) throws IOException{
		dualOutput("Executing select Keyword: ", objName);
		keywordResult = "Pass";
		
		try{
			TestUtil.findCustomElement(objName).sendKeys(value);
			}catch(Throwable t){
				// report error
				dualOutput("Error while Selecting from droplist -", objName + t.getMessage());
				fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
				TestUtil.takeScreenShot(fileName);
				ReportUtil.addStep("Enter text in field: "+objName, "Error entering the text", "Fail", screenshotPath+fileName);
				classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
				
				return keywordResult;
			}
			
			return keywordResult;
	}
	
	//This method clicks on a checkbox correspnds to the objname specified
	public static String clickCheckBox(String objName) throws IOException{
		dualOutput("Executing clickCheckBox Keyword: ", objName);
		keywordResult = "Pass";
		
		try{
			TestUtil.findElement(objName).click();
			}catch(Throwable t){
				// report error
				dualOutput("Error while clicking on checkbox -", objName + t.getMessage());
				fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
				TestUtil.takeScreenShot(fileName);
				ReportUtil.addStep("Click Checkbox: "+objName, "Error entering the text", "Fail", screenshotPath+fileName);
				classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
				
				return keywordResult;
			}
		return keywordResult;
	}
	
	//This method clicks on a checkbox correspnds to the objname AND other parameters specified
	public static String clickCheckBoxCustom2(String identifier, String objName, String frame1, String frame2, String frame3) throws IOException{

		dualOutput("Executing clickCheckBoxCustom2 Keyword: ", objName);
		keywordResult = "Pass";
		
		try{
			TestUtil.findElement(identifier, objName, frame1, frame2, frame3).click();
			}catch(Throwable t){
				// report error
				dualOutput("Error while clicking on checkbox with identifier ", identifier+" : "+ objName + t.getMessage());
				fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
				TestUtil.takeScreenShot(fileName);
				ReportUtil.addStep("Click Checkbox: "+objName, "Error entering the text", "Fail", screenshotPath+fileName);
				classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
				
				return keywordResult;
			}
		return keywordResult;
	}
	
	//This method takes care of Wait times
	public static String Wait(Long data) throws NumberFormatException, InterruptedException, IOException{
		dualOutput("Executing wait Keyword", null);
		keywordResult = "Pass";
			
	    Thread.sleep(data);
	    
	    return "Pass";
	}
	
	//This method clicks on an object with mouse movement
	public static String mouseClick(String objName) throws IOException{
		dualOutput("Executing mouse click: ", objName);
		
		try{
			WebElement login = TestUtil.findElement(objName);
			//dualOutput(login.getText());
			Actions builder = new Actions(evfw);
			builder.moveToElement(login).click(login);
			builder.perform();
		}catch(Throwable t){
			// report error
			dualOutput("Error while clicking on link -", objName + t.getMessage());
			keywordResult = "Pass";
			fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
			TestUtil.takeScreenShot(fileName);
			ReportUtil.addStep("Mouse click: "+objName, "Error clicking the object", "Fail", screenshotPath+fileName);
			classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
			
			return keywordResult;
		}
		return keywordResult;
	}
	
	//This method clicks on an object passed as the parameter (objname should be xpath)
	public static String mouseClickbyXpath(String objName) throws IOException{
		dualOutput("Executing mouse click: ", objName);
		
		try{
			WebElement login = TestUtil.findCustomElement(objName);
			//dualOutput(login.getText());
			Actions builder = new Actions(evfw);
			builder.moveToElement(login).click(login);
			builder.perform();
		}catch(Throwable t){
			// report error
			dualOutput("Error while clicking on link -", objName + t.getMessage());
			keywordResult = "Pass";
			fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
			TestUtil.takeScreenShot(fileName);
			ReportUtil.addStep("Mouse click: "+objName, "Error clicking the object", "Fail", screenshotPath+fileName);
			classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
			
			return keywordResult;
		}
		return keywordResult;
	}
	
	//This method clears any existing values from the input field specified as the object 
	public static String clear(String objName) throws IOException{
		dualOutput("Clearning the element: ", objName);
		keywordResult = "Pass";
		
		try{
			TestUtil.findElement(objName).clear();
		}catch(Throwable t){
			// report error
			dualOutput("Error while clearning -", objName + t.getMessage());
			fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
			TestUtil.takeScreenShot(fileName);
			ReportUtil.addStep("Clear the text from: "+objName, "Error clearing the text", "Fail", screenshotPath+fileName);
			classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
			
			return keywordResult;
		}
		return keywordResult;
	}
	/* -- OLDER VERSION
	public static Boolean elementDisplayed(String objName) throws IOException{
		dualOutput("Verifying if object displayed: " + objName);
	
		try{
			dualOutput(TestUtil.findElement(objName).isDisplayed());
			dualOutput("Element with objectname -"+ objName + " - is displayed");
		}catch(Throwable t){
		// report error
		dualOutput("Could not verify if object displayed or not -"+ objName + t.getMessage());
		
		//objName.replaceAll("*", "").replaceAll("//", "");
		fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
		TestUtil.takeScreenShot(fileName);
		ReportUtil.addStep("Verify presence of: "+objName, "Element is not displayed", "Fail", screenshotPath+fileName);
				
		return false;
	}
	return true;
}
	*/
	//Updating on 1/21 as it's not working as intended -- NEED to check for MCAT
	//This method checks if an element exists on the given page
	public static Boolean elementDisplayed(String objName) throws IOException{
		dualOutput("Verifying if object displayed: ", objName);
		try {
			if(TestUtil.findElement(objName).isDisplayed()){
				dualOutput("true	"+"Element with objectname -", objName + " - is displayed");
				return true;
			}else {
				dualOutput("false	"+"Element with objectname -", objName + " - is NOT displayed");
				ReportUtil.addStep("Verify presence of: "+objName, "Element is not displayed", "Fail", null);
				
				return false;
			}
		} catch(Throwable t){
			// report error
			dualOutput("Could not verify if object displayed or not -", objName + t.getMessage());		
			fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
			TestUtil.takeScreenShot(fileName);
			ReportUtil.addStep("Verify presence of: "+objName, "Element is not displayed", "Fail", screenshotPath+fileName);
			return false;
		}
	}
	
	//This method checks if an element exists on the given page (objname should be xpath)
	public static Boolean elementDisplayedCustom(String objName) throws IOException{
		dualOutput("Verifying if object displayed: ", objName);
	
		try{
			if(TestUtil.findCustomElement(objName).isDisplayed()) {
				dualOutput("Element with objectname -", objName + " - is displayed");
				return true;
			} else {
				dualOutput("Element with objectname -", objName + " - is NOT displayed");
				return false;
			}
		}catch(Throwable t){
		// report error
		dualOutput("Could not verify if object displayed or not -", objName + t.getMessage());		
		fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
		TestUtil.takeScreenShot(fileName);
		ReportUtil.addStep("Verify presence of: "+objName, "Element is not displayed", "Fail", screenshotPath+fileName);

		return false;
	}
}
	
	//This method checks if an element is enabled on the given page (objname should be xpath)
	public static Boolean elementenabledCustom(String objName) throws IOException{
		dualOutput("Verifying if object enabled: ", objName);
	
		try{
			TestUtil.findCustomElement(objName).isEnabled();
			dualOutput("Element with objectname -", objName + " - is enabled");
		}catch(Throwable t){
		// report error
		dualOutput("Could not verify if object enabled or not -", objName + t.getMessage());
		fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
		TestUtil.takeScreenShot(fileName);
		ReportUtil.addStep("Verify presence of: "+objName, "Element is not enabled", "Fail", screenshotPath+fileName);

		return false;
	}
	return true;
}
	//This method checks if the checkbox is selected -- all parameters should be provided
	public static Boolean isCheckBoxSelectedWithParameters(String identifier, String objName, String frame1, String frame2, String frame3) throws IOException{
		dualOutput("Verifying if checkbox with id# ", objName+" is selected");
		boolean result;
	
		try{
			result=TestUtil.findElement(identifier,objName,frame1,frame2,frame3).isSelected();
			
			if(result)
				dualOutput("checkbox with id# -", objName + " - is selected");
			else
				dualOutput("checkbox with id# -", objName + " - is NOT selected");

		}catch(Throwable t){

		dualOutput("Error while verifying checkbox with id# ", objName+" is selected " + t.getMessage());
		
		fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_id"+objName+".jpg";
		TestUtil.takeScreenShot(fileName);
		ReportUtil.addStep("Verify if checkbox with id# "+ objName+" is selected", "Error while verifying", "Fail", screenshotPath+fileName);
		
		return false;
	}
	return result;
}
	//This is a custom method for QB ONLY
	public static Boolean isCheckBoxCheckedWithParameters(String identifier, String objName, String frame1, String frame2, String frame3) throws IOException{
		dualOutput("Verifying if checkbox with id# ", objName+" is checked");
		boolean result = false; String treeAttributeResult = null;
	
		try{
			treeAttributeResult = TestUtil.findElement(identifier,objName,frame1,frame2,frame3).getAttribute("class");
			dualOutput(treeAttributeResult, null);
	
			if(treeAttributeResult.contains("tree-checked")){
				dualOutput("checkbox with id# -", objName + " - is checked");
				result = true;
			}
			else
				dualOutput("checkbox with id# -", objName + " - is NOT checked");

		}catch(Throwable t){

		dualOutput("Error while verifying checkbox with id# ", objName+" is checked " + t.getMessage());
		fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_id"+objName+".jpg";
		TestUtil.takeScreenShot(fileName);
		ReportUtil.addStep("Verify if checkbox with id# "+ objName+" is checked", "Error while verifying", "Fail", screenshotPath+fileName);
		
		return false;
	}
	return result;
}
	//This method clears existing values and inputs a string into the input text field - values comes from OR excel
	public static String clearandinput(String objName) throws IOException{
		dualOutput("Executing clear and input Keyword: ", objName);
		keywordResult = "Pass";
		
		try{
			TestUtil.findElement(objName).clear();
			TestUtil.findElement(objName).sendKeys(TestUtil.getStringValueinArray(OR,objName,"Value"));
			}catch(Throwable t){
				// report error
				dualOutput("Error while clearing and inputting text -", objName + t.getMessage());
				fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
				TestUtil.takeScreenShot(fileName);
				ReportUtil.addStep("Clear and enter text @: "+objName, "Error clearing the text", "Fail", screenshotPath+fileName);
				classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
				
				return keywordResult;
			}
			
			return keywordResult;
	}
	
		// this method verified whether the substring is part of the main string -- both objname and value comes from OR excel
		public static String checkContains(String objName, String attributeParameter, String whatAttribute) throws IOException{
			dualOutput("Executing checkContains: ",objName);
			dualOutput("Executing checkContains: ",objName);
			keywordResult = "Pass";
			
			String attributeValue = getAttribute(objName,attributeParameter).trim();
			String expectedValue = TestUtil.getStringValueinArray(OR,objName,"Value").trim();

			dualOutput("Expected value is: ", expectedValue);
			dualOutput("Actual value is: ", attributeValue);
			
			try{
				if(StringUtils.containsIgnoreCase(attributeValue, expectedValue));
					ReportUtil.addStep("Verify attribute value of: "+ whatAttribute, "Verfified Successfully", "Pass", null);
		
			}catch(Throwable t){
				// error
				dualOutput("Error in attribute value of object - ", objName);
				dualOutput("Actual - ", attributeValue);
				dualOutput("Expected -", expectedValue);
				
				fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
				TestUtil.takeScreenShot(screenshotPath+fileName);
				ReportUtil.addStep("Verify attribute value of: "+ whatAttribute, "Not Successful", "Fail", screenshotPath+fileName);
				classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
				
				return keywordResult;
			}
			//dualOutput("verifyAttributeValue - return value is: ", keywordResult);
			return keywordResult;
		}
		
		//This method clears any existing text from input text field and input value passed as the parameter
		public static String clearandinputWithParameter(String objName, String parameter) throws IOException{
			dualOutput("Executing clear and input with parameter: ", objName);
			keywordResult = "Pass";
			
			try{
				TestUtil.findElement(objName).clear();
				TestUtil.findElement(objName).sendKeys(parameter);
				}catch(Throwable t){
					// report error
					dualOutput("Error while clearing and inputting text -", objName + t.getMessage());
					fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
					TestUtil.takeScreenShot(fileName);
					ReportUtil.addStep("Clear and enter text @: "+objName, "Error clearing the text", "Fail", screenshotPath+fileName);
					classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
					
					return keywordResult;
				}
				
				return keywordResult;
		}
		
		// this method verified whether the substring is part of the main string -- works with Xpath only
		public static String checkContainsCustom(String objName, String attributeParameter, String expectedValue, String whatAttribute) throws IOException{
			dualOutput("Executing verifyCustomAttributeValue: ", objName);
			keywordResult = "Pass";
			
			String attributeValue = getCustomAttribute(objName,attributeParameter).trim();

			dualOutput(expectedValue, null);
			dualOutput(attributeValue, null);
			
			try{
				if(StringUtils.containsIgnoreCase(attributeValue, expectedValue));
					ReportUtil.addStep("Verify attribute value of: "+ whatAttribute, "Verfified Successfully", "Pass", null);
	
			}catch(Throwable t){
				// error
				dualOutput("Error in attribute value of object - ", objName);
				dualOutput("Actual - ", attributeValue);
				dualOutput("Expected -", expectedValue);
				fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
				TestUtil.takeScreenShot(fileName);
				ReportUtil.addStep("Verify attribute value of: "+ whatAttribute, "Not Successful", "Fail", screenshotPath+fileName);
				classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
				
				return keywordResult;
			}
			return keywordResult;
		}
		
		//This method performs Keypad operations like TAB, Shift ..etc
		public static String keysOperation(String objName, String systemKey) throws IOException{
			dualOutput("Executing clickeysOperation of: ", systemKey+" on object: "+objName);
			keywordResult = "Pass"; systemKey = "Keys."+systemKey;
			
			try{
				TestUtil.findElement(objName).sendKeys(systemKey);
				}catch(Throwable t){
					// report error
					dualOutput("Error while performing ", systemKey+ " action on  -"+ objName + t.getMessage());
					fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_"+objName+".jpg";
					TestUtil.takeScreenShot(fileName);
					ReportUtil.addStep("Click the button: "+objName, "Error clicking button", "Fail", screenshotPath+fileName);
					classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
					
					return keywordResult;
				}
				
				return keywordResult;
		}
		
		//Added on 1/21
		//This method verifies whether the page source contains the string in reference
		public static boolean pageSourceContains(String identifier) throws IOException{
			dualOutput("Verifying if the pagesource contain the identifier : ", identifier);
			
			if(driver.getPageSource().contains(identifier)) {
				dualOutput("Returning TRUE for identifier : ", identifier);
				return true;
			} else {
				dualOutput("Returning FALSE for identifier : ", identifier);
				return false;
			} 
		}	
		
		//Added on 1/21
		//This method accepts Alerts when present, throws exception when not present
		public static boolean acceptAlert() throws IOException{
			dualOutput("Accept the Alert present", null);
			try {
				Alert alert = driver.switchTo().alert();
				alert.accept();
				return true;
			} catch(Throwable t){
				// error
				dualOutput("There is NO ALERT present", null);
				fileName=currentTCID.replaceAll(" ", "")+"_"+currentTestName.replaceAll(" ", "")+"_alert.jpg";
				TestUtil.takeScreenShot(fileName);
				ReportUtil.addStep("Accept alert", "Not Successful", "Fail", screenshotPath+fileName);
				classResult = "Fail"; methodResult = "Fail"; keywordResult = "Fail"; testUtilResult = "Fail"; submethodL1Result = "Fail"; submethodL2Result = "Fail";
				
				return false;
			}
		}
		
		//This method formats the screenshots' path - removes different characters so that the JPEG name is clean
		public static String formatScreenshotName(String fileName) throws IOException{
			dualOutput("Formatting the fileName: ", fileName);
			String newFileName = ""; int strSize = fileName.length(); int counter = 0; String temp = null;
			do{
				temp = fileName.substring(counter, counter+1);
				if(!temp.equals("~") && !temp.equals("`") && !temp.equals("!") && !temp.equals("@") && !temp.equals("#") && !temp.equals("$") && !temp.equals("%") && !temp.equals("^") && !temp.equals("&")
						&& !temp.equals("*") && !temp.equals("(") && !temp.equals(")") && !temp.equals("+") && !temp.equals("=") && !temp.equals("[") && !temp.equals("]")
						&& !temp.equals("{") && !temp.equals("}") && !temp.equals("|") && !temp.equals(";") && !temp.equals(":") && !temp.equals("'")
						&& !temp.equals("<") && !temp.equals(">") && !temp.equals(",") && !temp.equals("?")  && !temp.equals("/")) 
					newFileName = newFileName + temp;
					
				//dualOutput(newFileName);
				counter++;
						
			} while (counter < strSize);			
			//dualOutput(newFileName);			
			return newFileName;					
		}
		//This method takes care of outputting to APP LOG as well as System console		
		public static void dualOutput(String textToPrint, String Parameter) throws IOException{
			if((Parameter == null) || Parameter.equals("")) { //modified on 1/23
				APPLICATION_LOGS.debug(textToPrint);
				System.out.println(textToPrint);
			}
			else {
				APPLICATION_LOGS.debug(textToPrint+Parameter);
				System.out.println(textToPrint+Parameter);
			}				
				
		}
		
}
