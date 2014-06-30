/*
 * This is for generating customized functional reporting. The following three reports will be generated:
 * 	a. Failed Cases Report - exclusively for failed cases
 * 	b. Index.html Report - consolidate report that will have execution time and status of each test
 * 	c. Detailed step-wise Report - One HTML page for each test case executed
 */

package Reports;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;

import Utility.TestUtil;

public class ReportUtil {
	// Initialization
	public static int scriptNumber=1;
	public static String indexResultFilename;
	public static String failedFilename;
	public static String currentDir;
	public static String currentSuiteName;
	public static double passNumber;
	public static double failNumber;
	public static boolean newTest=true;
	public static ArrayList<String> description=new ArrayList<String>();;
	public static ArrayList<String> keyword=new ArrayList<String>();;
	public static ArrayList<String> teststatus=new ArrayList<String>();;
	public static ArrayList<String> screenShotPath=new ArrayList<String>();;

	// To initialize all reporting aspects at the beginning of the testing
	public static void startTesting(String filename,String testStartTime,String env,String rel)
	  {
		indexResultFilename = filename;
		currentDir = indexResultFilename.substring(0,indexResultFilename.lastIndexOf("//"));
		
		FileWriter fstream =null;
		 BufferedWriter out =null;
	      try{
	    
	    // Create file 
	    fstream = new FileWriter(filename);
	     out = new BufferedWriter(fstream);

        String RUN_DATE = TestUtil.now("dd.MMMMM.yyyy").toString();
	    
	    String ENVIRONMENT = env;
	    String RELEASE = rel;
	    
	    out.newLine();
	  
	    out.write("<html>\n");
	    out.write("<HEAD>\n");
	    out.write(" <TITLE>Automation Test Results</TITLE>\n");
	     out.write("</HEAD>\n");
	     
	     out.write("<body>\n");
	     out.write("<h4 align=center><FONT COLOR=660066 FACE=AriaL SIZE=6><b><u> Automation Test Results</u></b></h4>\n");
	     out.write("<table  border=1 cellspacing=1 cellpadding=1 >\n");
	     out.write("<tr>\n");

	           out.write("<h4> <FONT COLOR=660000 FACE=Arial SIZE=4.5> <u>Test Details :</u></h4>\n");
	           out.write("<td width=150 align=left bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2.75><b>Run Date</b></td>\n");
	           out.write("<td width=150 align=left><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>"+RUN_DATE+"</b></td>\n");
	     out.write("</tr>\n");
	     out.write("<tr>\n");
	           
	           out.write("<td width=150 align=left bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2.75><b>Run StartTime</b></td>\n");

	           out.write("<td width=150 align=left><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>"+testStartTime+"</b></td>\n");
	     out.write("</tr>\n");
	     out.write("<tr>\n");
  
	           out.write("<td width=150 align= left  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2.75><b>Run EndTime</b></td>\n");
	           out.write("<td width=150 align= left ><FONT COLOR=#153E7E FACE= Arial  SIZE=2.75><b>END_TIME</b></td>\n");
	     out.write("</tr>\n");
	     out.write("<tr>\n");
         
	           out.write("<td width=150 align= left  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2.75><b>Environment</b></td>\n");
	           out.write("<td width=150 align= left ><FONT COLOR=#153E7E FACE= Arial  SIZE=2.75><b>"+ENVIRONMENT+"</b></td>\n");
	     out.write("</tr>\n");
	     out.write("<tr>\n");
	           
	           out.write("<td width=150 align= left  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2.75><b>Release</b></td>\n");
	           out.write("<td width=150 align= left ><FONT COLOR=#153E7E FACE= Arial  SIZE=2.75><b>"+RELEASE+"</b></td>\n");
	     out.write("</tr>\n");

	     out.write("</table>\n");

	     out.close();
	    }catch (Exception e){//Catch exception if any
	      System.err.println("Error: " + e.getMessage());
	    }finally{
	    	
		    fstream=null;
		    out=null;
	    }
	  }
	
	// Set-up for Failed cases HTML Report
	public static void startFailedTestReport(String filename, String env)
	  {
		failedFilename = filename;
		currentDir = failedFilename.substring(0,failedFilename.lastIndexOf("//"));
		
		FileWriter fstream =null;
		 BufferedWriter out =null;
		 String ENVIRONMENT = env;
	      try{
   
	     fstream = new FileWriter(filename);
	     out = new BufferedWriter(fstream);

      String RUN_DATE = TestUtil.now("dd.MMMMM.yyyy").toString();
	    
	    out.newLine();
	  
	    out.write("<html>\n");
	    out.write("<HEAD>\n");
	    out.write(" <TITLE>Automation Test Results - FAILED cases </TITLE>\n");
	     out.write("</HEAD>\n");
	     
	     out.write("<body>\n");
	     out.write("<h4 align=center><FONT COLOR=660066 FACE=AriaL SIZE=6><b><u> Automation Test Results - FAILED cases</u></b></h4>\n");
	     out.write("<table  border=1 cellspacing=1 cellpadding=1 >\n");
	     out.write("<tr>\n");

	           out.write("<h4> <FONT COLOR=660000 FACE=Arial SIZE=4.5> <u>Test Details :</u></h4>\n");
	           out.write("<td width=150 align=left bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2.75><b>Run Date</b></td>\n");
	           out.write("<td width=150 align=left><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>"+RUN_DATE+"</b></td>\n");
	     out.write("</tr>\n");
	     out.write("<tr>\n");
	           
	     out.write("<td width=150 align= left  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2.75><b>Environment</b></td>\n");
	           out.write("<td width=150 align= left ><FONT COLOR=#153E7E FACE= Arial  SIZE=2.75><b>"+ENVIRONMENT+"</b></td>\n");
	     out.write("</tr>\n");
	     out.write("<tr>\n");

	     out.write("</table>\n");

	    out.close();
	    }catch (Exception e){//Catch exception if any
	      System.err.println("Error: " + e.getMessage());
	    }finally{
	    	
		    fstream=null;
		    out=null;
	    }
	  }
	
	// Activities to be performed at the beginning of the suite
    public static void startSuite(String suiteName){

	    FileWriter fstream =null;
		BufferedWriter out =null;
		currentSuiteName = suiteName.replaceAll(" ", "_");
		
		FileWriter fstreamF =null;
		BufferedWriter outF =null;
		
	    try{

	    fstream = new FileWriter(indexResultFilename,true);
	  	out = new BufferedWriter(fstream);
	      
    	out.write("<h4> <FONT COLOR=660000 FACE= Arial  SIZE=4.5> <u>"+suiteName+" Report :</u></h4>\n");
        out.write("<table  border=1 cellspacing=1 cellpadding=1 width=100%>\n");
    	out.write("<tr>\n");
        out.write("<td width=8%  align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>Test Case#</b></td>\n");
        out.write("<td width=26% align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>Test Case Name</b></td>\n");
        out.write("<td width=25% align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>Description</b></td>\n");
        out.write("<td width=6% align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>Browser</b></td>\n");
        out.write("<td width=5% align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>Status</b></td>\n");
        out.write("<td width=15% align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>Run Start Time</b></td>\n");
        out.write("<td width=15% align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>Run End Time</b></td>\n");

        out.write("</tr>\n");
        out.close();
        
        //For Failed cases Report
        
        fstreamF = new FileWriter(failedFilename,true);
	  	outF = new BufferedWriter(fstreamF);
	      
    	outF.write("<h4> <FONT COLOR=660000 FACE= Arial  SIZE=4.5> <u>"+suiteName+" FAILED cases Report :</u></h4>\n");
        outF.write("<table  border=1 cellspacing=1 cellpadding=1 width=100%>\n");
    	outF.write("<tr>\n");
        outF.write("<td width=6%  align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>Test Case#</b></td>\n");
        outF.write("<td width=27% align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>Test Case Name</b></td>\n");
        outF.write("<td width=8% align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>Browser</b></td>\n");
        outF.write("<td width=34% align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>Exptected</b></td>\n");
        outF.write("<td width=15% align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>Actual</b></td>\n");
        outF.write("<td width=10% align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>Screenshot</b></td>\n");

        outF.write("</tr>\n");
        outF.close();
        
        
        
	    }catch(Exception e){
		      System.err.println("Error: " + e.getMessage());
	    }finally{
	    	
		    fstream=null;
		    out=null;
		    
		    fstreamF=null;
		    outF=null;
	    }
	}
    
    public static void endSuite(){
    	 FileWriter fstream =null;
 		BufferedWriter out =null;
 		
 		FileWriter fstreamF =null;
 		BufferedWriter outF =null;
 		
 	    try{
 	    fstream = new FileWriter(indexResultFilename,true);
 	  	out = new BufferedWriter(fstream);
        out.write("</table>\n");
        out.close();
        
        fstreamF = new FileWriter(failedFilename,true);
 	  	outF = new BufferedWriter(fstreamF);
        outF.write("</table>\n");
        outF.close();
        
 	    }catch(Exception e){
		      System.err.println("Error: " + e.getMessage());
	    }finally{
	    	
		    fstream=null;
		    out=null;
		    
		    fstreamF=null;
		    outF=null;
	    }

    }
	
    //To keep add a test case once a particular test case is done
	public static void addTestCase(String TCID, String testCaseName, String testCaseDescription, String testCaseStartTime,String testCaseEndTime,String status,String browser){
		newTest=true;
		FileWriter fstream=null;
		BufferedWriter out=null;
		
		FileWriter fstreamF=null;
		BufferedWriter outF=null;
		
		try {
			newTest=true;
			
			fstreamF = new FileWriter(failedFilename,true);
			 outF = new BufferedWriter(fstreamF);
			
			// build the keywords page
		   if(status.equalsIgnoreCase("Skipped") || status.equalsIgnoreCase("Skip")){
			   System.out.println("skipped");
		   }else{
				File f = new File(currentDir+"//"+browser+"_"+currentSuiteName+"_"+TCID+"_"+testCaseName.replaceAll(" ", "_")+".html");
				f.createNewFile();
				fstream = new FileWriter(currentDir+"//"+browser+"_"+currentSuiteName+"_"+TCID+"_"+testCaseName.replaceAll(" ", "_")+".html");
				out = new BufferedWriter(fstream);
				out.write("<html>");
				out.write("<head>");
				out.write("<title>");
				out.write(testCaseName + " Detailed Reports");
				out.write("</title>");
				out.write("</head>");
				out.write("<body>");
			
			
				out.write("<h4> <FONT COLOR=660000 FACE=Arial SIZE=4.5> Detailed Report :</h4>");
					out.write("<table  border=1 cellspacing=1    cellpadding=1 width=100%>");
				out.write("<tr> ");
					//out.write("<td align=center width=6%  align=center bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2><b>Browser</b></td>");
					out.write("<td align=center width=10%  align=center bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2><b>Step/Row#</b></td>");
					out.write("<td align=center width=40% align=center bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2><b>Description</b></td>");
					out.write("<td align=center width=25% align=center bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2><b>Actual</b></td>");
					out.write("<td align=center width=10% align=center bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2><b>Result</b></td>");
					out.write("<td align=center width=15% align=center bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2><b>Screen Shot</b></td>");
					out.write("</tr>");
		 	 
				if(description!=null){
		 		 for(int i=0;i<description.size();i++){
		 			 out.write("<tr> ");
		 			 
		 			// out.write("<td align=center width=6%><FONT COLOR=#153E7E FACE=Arial SIZE=1><b>"+currentBrowser+"</b></td>");
		 			 out.write("<td align=center width=10%><FONT COLOR=#153E7E FACE=Arial SIZE=1><b>TS"+(i+1)+"</b></td>");
		 			 out.write("<td align=center width=40%><FONT COLOR=#153E7E FACE=Arial SIZE=1><b>"+description.get(i)+"</b></td>");
		 			 out.write("<td align=center width=25%><FONT COLOR=#153E7E FACE=Arial SIZE=1><b>"+keyword.get(i)+"</b></td>");
		 			if(teststatus.get(i).startsWith("Pass"))
		 			     out.write("<td width=10% align= center  bgcolor=#BCE954><FONT COLOR=#153E7E FACE=Arial SIZE=2><b>"+teststatus.get(i)+"</b></td>\n");
		 			else if(teststatus.get(i).startsWith("Fail"))
		 			  	 out.write("<td width=10% align= center  bgcolor=Red><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b>"+teststatus.get(i)+"</b></td>\n");
		 			     
		 			// out.write("<td align=center width=20%><FONT COLOR=#153E7E FACE=Arial SIZE=1><b>"+teststatus.get(i)+"</b></td>");
		 			 if(screenShotPath.get(i) != null)
		 			 out.write("<td align=center width=15%><FONT COLOR=#153E7E FACE=Arial SIZE=1><b><a href="+screenShotPath.get(i)+" target=_blank>Screen Shot</a></b></td>");
		 			 else
		 				out.write("<td align=center width=15%><FONT COLOR=#153E7E FACE=Arial SIZE=1><b>&nbsp;</b></td>");	 
		 			 out.write("</tr>");
		 			 
		 			 
		 			 //For failed cases report
		 			if(teststatus.get(i).startsWith("Fail")) { 
		 				outF.write("<td width=6% align= center ><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b>"+TCID+"</b></td>\n");
		 				outF.write("<td width=27% align= center ><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b>"+testCaseName+"</b></td>\n");
		 				outF.write("<td width=8% align= center ><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b>"+browser+"</b></td>\n");
		 				outF.write("<td width=34% align= center ><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b>"+description.get(i)+"</b></td>\n");
		 				outF.write("<td width=15% align= center ><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b>"+keyword.get(i)+"</b></td>\n");

		 				if(screenShotPath.get(i) != null)
		 					outF.write("<td align=center width=10%><FONT COLOR=#153E7E FACE=Arial SIZE=1><b><a href="+screenShotPath.get(i).replaceAll("//", "/")+" target=_blank>Screen Shot</a></b></td>");
		 				else
		 					outF.write("<td align=center width=15%><FONT COLOR=#153E7E FACE=Arial SIZE=1><b>&nbsp;</b></td>");	 
		 				outF.write("</tr>");
		 			}
		 		 }
		 	 }
		 	 out.close();
		 	outF.close();
		   }
			
			fstream = new FileWriter(indexResultFilename,true);
			out = new BufferedWriter(fstream);
			
			 fstream = new FileWriter(indexResultFilename,true);
			 out = new BufferedWriter(fstream);
			// out.newLine();
			
			 out.write("<tr>\n");
			 //System.out.println(currentSuitePath);
		     out.write("<td width=8% align= center ><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b>"+TCID+"</b></td>\n");
		    
		     if(status.equalsIgnoreCase("Skipped") || status.equalsIgnoreCase("Skip"))
		    	 out.write("<td width=26% align= center ><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b>"+testCaseName+"</b></td>\n");
		     else
		    	 out.write("<td width=26% align= center ><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b><a href=file:///"+currentDir+"/"+browser+"_"+currentSuiteName+"_"+TCID+"_"+testCaseName.replaceAll(" ", "_")+".html>"+testCaseName+"</a></b></td>\n"); 
		     
		     out.write("<td width=25% align= center ><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b>"+testCaseDescription+"</b></td>\n");
		     out.write("<td width=6% align= center ><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b>"+browser+"</b></td>\n");
		     //tcid++;
		     if(status.startsWith("Pass"))
		     out.write("<td width=5% align= center  bgcolor=#BCE954><FONT COLOR=#153E7E FACE=Arial SIZE=2><b>"+status+"</b></td>\n");
		     else if(status.startsWith("Fail"))
		    	 out.write("<td width=6% align= center  bgcolor=Red><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b>"+status+"</b></td>\n");
		     else if(status.equalsIgnoreCase("Skipped") || status.equalsIgnoreCase("Skip"))
			     out.write("<td width=6% align= center  bgcolor=yellow><FONT COLOR=153E7E FACE=Arial SIZE=2><b>"+status+"</b></td>\n");
		     
		     out.write("<td width=15% align= center ><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b>"+testCaseStartTime+"</b></td>\n");
		     out.write("<td width=15% align= center ><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b>"+testCaseEndTime+"</b></td>\n");

		     out.write("</tr>\n");
		     
		     scriptNumber++;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		description= new ArrayList<String>();
		keyword= new ArrayList<String>();
		teststatus= new ArrayList<String>();
		screenShotPath = new ArrayList<String>();
		newTest=false;
	}
	
	// Test steps during test cases - as many as possible : both pass and fail
	public static void addStep(String desc,String key,String stat,String path){
		
		description.add(desc);
		keyword.add(key);
		teststatus.add(stat);
		screenShotPath.add(path);
		
}

	public static void updateEndTime(String endTime) {
		StringBuffer buf = new StringBuffer();
		try{

		    FileInputStream fstream = new FileInputStream(indexResultFilename);

		    DataInputStream in = new DataInputStream(fstream);
		        BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String strLine;
		    
		    while ((strLine = br.readLine()) != null)   {
		    	if(strLine.indexOf("END_TIME") !=-1){
		    	 strLine=strLine.replace("END_TIME", endTime);
		     }
		     buf.append(strLine);	     
		    }

		    // Closing input streams
		    in.close();
		    System.out.println(buf);
		    FileOutputStream fos=new FileOutputStream(indexResultFilename);
			 DataOutputStream   output = new DataOutputStream (fos);	 
	    	 output.writeBytes(buf.toString());
	    	 fos.close();
		    
		    }catch (Exception e){//Catch exception if any
		      System.err.println("Error: " + e.getMessage());
		    }
	
	}
	

}
