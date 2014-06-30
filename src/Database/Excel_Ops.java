/* This class file is designed to take care of all excel related operations
 * Just need to create an object for this class in the desired class file. 
 */


package Database;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;


public class Excel_Ops {
	public static String filename = null;
	public  String path;
	public  FileInputStream fis = null;
	public  FileOutputStream fileOut =null;
	private XSSFWorkbook workbook = null;
	private XSSFSheet sheet = null;
	private XSSFRow row   =null;
	private XSSFCell cell = null;
	
	public Excel_Ops(String path) {
		
		this.path=path;
		try {
			fis = new FileInputStream(path);
			workbook = new XSSFWorkbook(fis);
			sheet = workbook.getSheetAt(0);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	
	// returns the row count in a sheet
	public int getRowCount(String sheetName){
		
		int index = workbook.getSheetIndex(sheetName);
		if(index==-1)
			return 0;
		else{
		sheet = workbook.getSheetAt(index);
		int number=sheet.getLastRowNum()+1;

		return number;
		}
		
	}

	// returns number of columns in a given row 
		public int getColumnCount(String sheetName,int RowNum){
			int index = workbook.getSheetIndex(sheetName);
			if(index==-1){
				System.out.println("Sheet with name "+sheetName+" does not exists");
				return 0;
			}else{
				sheet = workbook.getSheetAt(index);
				row = sheet.getRow(RowNum-1);
			
				int NoOfColumns = 0;
			
				while(row.getCell(NoOfColumns) != null){
					NoOfColumns++;
				}
				return NoOfColumns;			
			}
			
		}
	
	// returns the data from a cell
	public String getCellData(String sheetName,String colName,int rowNum){
		try{

			fis = new FileInputStream(path); 
			workbook = new XSSFWorkbook(fis);
			
			if(rowNum <=0)
				return "";
			
			int index = workbook.getSheetIndex(sheetName);
			int col_Num=-1;
			if(index==-1)
				return "";
		
			sheet = workbook.getSheetAt(index);
			row=sheet.getRow(0);
			for(int i=0;i<row.getLastCellNum();i++){
				//System.out.println(row.getCell(i).getStringCellValue().trim());
				if(row.getCell(i).getStringCellValue().trim().equals(colName.trim()))
					col_Num=i;
			}
			if(col_Num==-1)
				return "";
		
			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(rowNum-1);
			if(row==null)
				return "";
			cell = row.getCell(col_Num);
		
			if(cell==null)
				return "";
			//System.out.println(cell.getCellType());
			if(cell.getCellType()==Cell.CELL_TYPE_STRING)
				return cell.getStringCellValue();
			else if(cell.getCellType()==Cell.CELL_TYPE_FORMULA){
			//System.out.println(cell.getCellFormula());
				FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
				CellValue cellValue = evaluator.evaluate(cell);
				String cellText = null;
				//System.out.println(cellValue);
				switch (cellValue.getCellType()) {
				case Cell.CELL_TYPE_BOOLEAN:
					cellText  = String.valueOf(cellValue.getBooleanValue());
					break;
				case Cell.CELL_TYPE_NUMERIC:
					cellText  = String.valueOf(cellValue.getNumberValue());
					break;
				case Cell.CELL_TYPE_STRING:
					cellText  = cellValue.getStringValue();
					break;
				case Cell.CELL_TYPE_BLANK:
					cellText = "";
					break;
				case Cell.CELL_TYPE_ERROR:
					break;
				case Cell.CELL_TYPE_FORMULA: 
					break;
				}
				fis.close();
				return cellText;
			}
			else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC){
			  
				String cellText  = String.valueOf(cell.getNumericCellValue());
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
				  // format in form of MM/DD/YYYY
					double d = cell.getNumericCellValue();
					Calendar cal =Calendar.getInstance();
					cal.setTime(HSSFDateUtil.getJavaDate(d));
				  // 	cellText = (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
					cellText = (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR);
					
				}
				fis.close();
				return cellText;
			}else if(cell.getCellType()==Cell.CELL_TYPE_BLANK){
				fis.close();
				return ""; 
			}
			else{
				fis.close();
				return String.valueOf(cell.getBooleanCellValue());
			}
		
			}
			catch(Exception e){
			
				e.printStackTrace();
				return "row "+rowNum+" or column "+colName +" does not exist in xls";
			}
		}
	
	// returns the data from a cell - colNum must be based on the first col as 1 and rowNum must be based on first row as 1
	public String getCellData(String sheetName,int colNum,int rowNum){
		try{
			
			fis = new FileInputStream(path); 
			workbook = new XSSFWorkbook(fis);

			if(rowNum <=0)
				return "";
		
			int index = workbook.getSheetIndex(sheetName);

			if(index==-1)
				return "";
			
			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(rowNum-1);
			if(row==null)
				return "";
			cell = row.getCell(colNum-1);
			if(cell==null)
				return "";
		
			if(cell.getCellType()==Cell.CELL_TYPE_STRING)
				return cell.getStringCellValue();
			else if(cell.getCellType()==Cell.CELL_TYPE_FORMULA){
					FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
					CellValue cellValue = evaluator.evaluate(cell);
					String cellText = null;

					switch (cellValue.getCellType()) {
					case Cell.CELL_TYPE_BOOLEAN:
						cellText  = String.valueOf(cellValue.getBooleanValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						cellText  = String.valueOf(cellValue.getNumberValue());
						break;
					case Cell.CELL_TYPE_STRING:
						cellText  = cellValue.getStringValue();
						break;
					case Cell.CELL_TYPE_BLANK:
						cellText = "";
						break;
					case Cell.CELL_TYPE_ERROR:
						break;
					case Cell.CELL_TYPE_FORMULA: 
						break;
					}
					fis.close();
					return cellText;
				}
				else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC){
		  
					String cellText  = String.valueOf(cell.getNumericCellValue());
					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						double d = cell.getNumericCellValue();
						Calendar cal =Calendar.getInstance();
						cal.setTime(HSSFDateUtil.getJavaDate(d));
						cellText = (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR);
					}
					fis.close();
					return cellText;

				}else if(cell.getCellType()==Cell.CELL_TYPE_BLANK)
					return "";
				else 
					return String.valueOf(cell.getBooleanCellValue());
			}
			catch(Exception e){
			
				e.printStackTrace();
				return "row "+rowNum+" or column "+colNum +" does not exist  in xls";
			}
		}
	
	// returns true if data is set successfully else false
	public boolean setCellData(String sheetName,String colName,int rowNum, String data){
		try{
		fis = new FileInputStream(path); 
		workbook = new XSSFWorkbook(fis);

		if(rowNum<=0)
			return false;
		
		int index = workbook.getSheetIndex(sheetName);
		int colNum=-1;
		if(index==-1)
			return false;
		
		
		sheet = workbook.getSheetAt(index);
		

		row=sheet.getRow(0);
		for(int i=0;i<row.getLastCellNum();i++){
			//System.out.println(row.getCell(i).getStringCellValue().trim());
			if(row.getCell(i).getStringCellValue().trim().equals(colName))
				colNum=i;
		}
		if(colNum==-1)
			return false;

		sheet.autoSizeColumn(colNum); 
		row = sheet.getRow(rowNum-1);
		if (row == null)
			row = sheet.createRow(rowNum-1);
		
		cell = row.getCell(colNum);	
		if (cell == null)
	        cell = row.createCell(colNum);

	    cell.setCellValue(data);
	    
	    fis.close();

	    fileOut = new FileOutputStream(path);

		workbook.write(fileOut);

	    fileOut.close();	

		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	// returns true if data is set successfully else false
	public boolean setCellData(String sheetName,String colName,int rowNum, String data,String url){
		try{
		fis = new FileInputStream(path); 
		workbook = new XSSFWorkbook(fis);

		if(rowNum<=0)
			return false;
		
		int index = workbook.getSheetIndex(sheetName);
		int colNum=-1;
		if(index==-1)
			return false;
		
		
		sheet = workbook.getSheetAt(index);

		row=sheet.getRow(0);
		for(int i=0;i<row.getLastCellNum();i++){
			if(row.getCell(i).getStringCellValue().trim().equalsIgnoreCase(colName))
				colNum=i;
		}
		
		if(colNum==-1)
			return false;
		sheet.autoSizeColumn(colNum); //ashish
		row = sheet.getRow(rowNum-1);
		if (row == null)
			row = sheet.createRow(rowNum-1);
		
		cell = row.getCell(colNum);	
		if (cell == null)
	        cell = row.createCell(colNum);
			
	    cell.setCellValue(data);
	    XSSFCreationHelper createHelper = workbook.getCreationHelper();

	    CellStyle hlink_style = workbook.createCellStyle();
	    XSSFFont hlink_font = workbook.createFont();
	    hlink_font.setUnderline(XSSFFont.U_SINGLE);
	    hlink_font.setColor(IndexedColors.BLUE.getIndex());
	    hlink_style.setFont(hlink_font);
	    //hlink_style.setWrapText(true);

	    XSSFHyperlink link = createHelper.createHyperlink(XSSFHyperlink.LINK_FILE);
	    link.setAddress(url);
	    cell.setHyperlink(link);
	    cell.setCellStyle(hlink_style);
	      
	    fileOut = new FileOutputStream(path);
		workbook.write(fileOut);

	    fileOut.close();	
	    
	    workbook = new XSSFWorkbook(new FileInputStream(path));
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	
	// returns true if sheet is created successfully else false
	public boolean addSheet(String  sheetname){		
		
		FileOutputStream fileOut;
		try {
			 workbook.createSheet(sheetname);	
			 fileOut = new FileOutputStream(path);
			 workbook.write(fileOut);
		     fileOut.close();		    
		} catch (Exception e) {			
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	// returns true if sheet is removed successfully else false if sheet does not exist
	public boolean removeSheet(String sheetName){		
		int index = workbook.getSheetIndex(sheetName);
		if(index==-1)
			return false;
		
		FileOutputStream fileOut;
		try {
			workbook.removeSheetAt(index);
			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
		    fileOut.close();		    
		} catch (Exception e) {			
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	// returns true if column is created successfully
	public boolean addColumn(String sheetName,String colName){
		
		try{				
			fis = new FileInputStream(path); 
			workbook = new XSSFWorkbook(fis);
			int index = workbook.getSheetIndex(sheetName);
			if(index==-1)
				return false;
			
		XSSFCellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		
		sheet=workbook.getSheetAt(index);
		
		row = sheet.getRow(0);
		if (row == null)
			row = sheet.createRow(0);

		if(row.getLastCellNum() == -1)
			cell = row.createCell(0);
		else
			cell = row.createCell(row.getLastCellNum());
	        
	        cell.setCellValue(colName);
	        cell.setCellStyle(style);
	        
	        fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
		    fileOut.close();		    

		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		return true;
		
		
	}
	// removes a column and all the contents
	public boolean removeColumn(String sheetName, int colNum) {
		try{
		if(!isSheetExist(sheetName))
			return false;
		fis = new FileInputStream(path); 
		workbook = new XSSFWorkbook(fis);
		sheet=workbook.getSheet(sheetName);
		XSSFCellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
		style.setFillPattern(HSSFCellStyle.NO_FILL);
		
	    
	
		for(int i =0;i<getRowCount(sheetName);i++){
			row=sheet.getRow(i);	
			if(row!=null){
				cell=row.getCell(colNum);
				if(cell!=null){
					cell.setCellStyle(style);
					row.removeCell(cell);
				}
			}
		}
		fileOut = new FileOutputStream(path);
		workbook.write(fileOut);
	    fileOut.close();
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
		
	}
  // find whether sheets exists	
	public boolean isSheetExist(String sheetName){
		int index = workbook.getSheetIndex(sheetName);
		if(index==-1){
			index=workbook.getSheetIndex(sheetName.toUpperCase());
				if(index==-1)
					return false;
				else
					return true;
		}
		else
			return true;
	}
	
	// returns number of columns in a sheet	
	public int getColumnCount(String sheetName){
		// check if sheet exists
		if(!isSheetExist(sheetName))
		 return -1;
		
		sheet = workbook.getSheet(sheetName);
		row = sheet.getRow(0);
		
		if(row==null)
			return -1;
		
		return row.getLastCellNum();
		
		
		
	}
	
	//String sheetName, String testCaseName,String keyword ,String URL,String message
	public boolean addHyperLink(String sheetName,String screenShotColName,String testCaseName,int index,String url,String message){
	
		url=url.replace('\\', '/');
		if(!isSheetExist(sheetName))
			 return false;
		
	    sheet = workbook.getSheet(sheetName);
	    
	    for(int i=2;i<=getRowCount(sheetName);i++){
	    	if(getCellData(sheetName, 0, i).equalsIgnoreCase(testCaseName)){
	    		//System.out.println("**caught "+(i+index));
	    		setCellData(sheetName, screenShotColName, i+index, message,url);
	    		break;
	    	}
	    }


		return true; 
	}
	
	//returns the row number of matching cell in given column
	public int getCellRowNum(String sheetName,String colName,String cellValue){
		
		int returnValue = -1;
		for(int i=2;i<getRowCount(sheetName);i++){
	    	if(getCellData(sheetName,colName, i).equalsIgnoreCase(cellValue)){
	    		returnValue = i;
	    		break;
	    	}
	    }
		return returnValue;

	}
	
	//returns the number of matching cells (elements) in a given column
	public int getNoOfMatches(String sheetName,String ColumnName,String ToBeMatched){
		int rowNum = 0; int NoOfMatches = 0; 
		int index = workbook.getSheetIndex(sheetName);
		//System.out.println("Index is"+index);
		int colIndex = getColumnPosition(sheetName,ColumnName);
		String tempholder = null;
		//System.out.println("ColNum is"+colIndex);
		if(index==-1){
			System.out.println("The sheet with the name "+sheetName+" does not exist");
			return 0;
		}	
		else{
			sheet = workbook.getSheetAt(index);

			int MaxRows = getRowCount(sheetName);

			while(rowNum<MaxRows) {

				row = sheet.getRow(rowNum);
				cell = row.getCell(colIndex-1);
				
				if(cell.getCellType() == 0)
					tempholder = String.valueOf((int) cell.getNumericCellValue());
				else if (cell.getCellType() == 1)
					tempholder = cell.getStringCellValue();
				else
					tempholder = "";

  		  		if (tempholder.equals(ToBeMatched)){
  		  			NoOfMatches++;
  		  		}
  		  			
  		  		rowNum++;
			}
		}
		return NoOfMatches;
	}
	
	//returns the given column number (order number) 
	public int getColumnPosition(String sheetName,String Column){
		
		int rowNum = 0; int columnNum = 0; int position = 0;
		int index = workbook.getSheetIndex(sheetName);

		if(index==-1){
			System.out.println("The sheet with the name "+sheetName+" does not exist");
			return 0;
		}	
		else{
		sheet = workbook.getSheetAt(index);
		row = sheet.getRow(rowNum);

		int NoOfColumns = getColumnCount(sheetName,1);
		
		while(columnNum<NoOfColumns){
			cell = row.getCell(columnNum);
			if(cell.getStringCellValue().equals(Column)){
				position = columnNum+1;
				break;
			}	
			else
				columnNum++;
		}
		return position;		
		}
	}
	
	//Returns the first instance of the element in a given column
	public int getFirstRowInstance(String sheetName,String column,String element){
		
		int rowNum = 0; int position = 0;
		int index = workbook.getSheetIndex(sheetName);

		if(index==-1){
			System.out.println("The sheet with the name "+sheetName+" does not exist");
			return 0;
		}	
		else{
			sheet = workbook.getSheetAt(index);
			
			int columnNum = getColumnPosition(sheetName,column);
			int MaxRows = getRowCount(sheetName);
		
			while(rowNum<MaxRows){
				row = sheet.getRow(rowNum);
				cell = row.getCell(columnNum-1);
				if(cell.getStringCellValue().equals(element)){
					position = rowNum+1;
					break;
				}	
				else
					rowNum++;
			}
			if(position == MaxRows){
				System.out.println("No such element found");
				return position;
			}
			else
				return position;		
			}
		}
	
	//returns array list of distinct values in a columns
	public ArrayList<String> getDistinctValues(String sheetName,String column) {
		
		int rowNum = 1;
		int index = workbook.getSheetIndex(sheetName);
		ArrayList<String> al = new ArrayList<String>();

		if(index==-1){
			System.out.println("The sheet with the name "+sheetName+" does not exist");
			//return 0;
		}	
		else{
			sheet = workbook.getSheetAt(index);
			
			int columnNum = getColumnPosition(sheetName,column);
			int MaxRows = getRowCount(sheetName);
			
			while(rowNum<MaxRows){
				row = sheet.getRow(rowNum);
				cell = row.getCell(columnNum-1);
				
				if(!al.contains(cell.getStringCellValue()))
					al.add(cell.getStringCellValue());
				
				rowNum++;
			}
			
		}
		
		return al;
		
	}
	
	//returns cell type
	public String getCellType(String sheetName,String colName,int rowNum){
		try{

			fis = new FileInputStream(path); 
			workbook = new XSSFWorkbook(fis);
			System.out.println("Inside getCellType");
			if(rowNum <=0)
				return "";
			
			int index = workbook.getSheetIndex(sheetName);
			int col_Num=-1;
			if(index==-1)
				return "";
		
			sheet = workbook.getSheetAt(index);
			row=sheet.getRow(0);
			for(int i=0;i<row.getLastCellNum();i++){
				if(row.getCell(i).getStringCellValue().trim().equals(colName.trim()))
					col_Num=i;
			}
			if(col_Num==-1)
				return "";
			System.out.println("Completed Col_Num");
			
			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(rowNum-1);
			if(row==null)
				return "";
			cell = row.getCell(col_Num);
		
			if(cell==null)
				return "";
			
			if(cell.getCellType()==Cell.CELL_TYPE_STRING)
				return "String";
			else if(cell.getCellType()==Cell.CELL_TYPE_FORMULA)
				return "Formula";
			else if(cell.getCellType()==Cell.CELL_TYPE_BOOLEAN)
				return "Boolean";
			else if(cell.getCellType()==Cell.CELL_TYPE_ERROR)
				return "Error";
			else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC)
				return "Numeric";
			else
				return "";
		} catch(Exception e){
			e.printStackTrace();
			return "";
		}	
					
		}
	
}
