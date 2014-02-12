package Utility;
public class Variable_Conversions {
	public static String Input = null;
		
	public Variable_Conversions() {
		

	}
	
	// returns the row count in a sheet
	public String strToDblToStr(String Input){
		//System.out.println("Input value to be converted in the VC obj is: "+Input);
		if (!Input.equals("")){
			double FromString = Double.valueOf(Input);
			int Fromdouble = (int) FromString;
			String ToString = String.valueOf(Fromdouble);
			return ToString;
		}
		else
			return "";
		}
	
	// returns the row count in a sheet
	public int strToDblToInt(String Input){
		//System.out.println("Input value to be converted in the VC obj is: "+Input);
		if (!Input.equals("")){
			double FromString = Double.valueOf(Input);
			int Fromdouble = (int) FromString;
			return Fromdouble;
		}
		else
			return -1;
		}
		
	}
