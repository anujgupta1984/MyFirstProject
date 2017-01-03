package edu.kheg.utility;

public class Suite_Utility 
{
	public static boolean checkToRunUtility(Project_Utility xls, String sheetName, String ToRun, String testSuite){
		
		boolean Flag = false;		
		if(xls.retrieveToRunFlag(sheetName,ToRun,testSuite).equalsIgnoreCase("y")){
			Flag = true;
		}
		else{
			Flag = false;
		}
		return Flag;		
	}
	
	public static String[] checkToRunUtilityOfData(Project_Utility xls, String sheetName, String ColName){		
		return xls.retrieveToRunFlagTestData(sheetName,ColName);		 	
	}
 
	public static Object[][] GetTestDataUtility(Project_Utility xls, String sheetName){
	 	return xls.retrieveTestData(sheetName);	
	}
 
	public static boolean WriteResultUtility(Project_Utility xls, String sheetName, String ColName, int rowNum, String Result){			
		return xls.writeResult(sheetName, ColName, rowNum, Result);		 	
	}
 
	public static boolean WriteResultUtility(Project_Utility xls, String sheetName, String ColName, String rowName, String Result){			
		return xls.writeResult(sheetName, ColName, rowName, Result);		 	
	}
	
}