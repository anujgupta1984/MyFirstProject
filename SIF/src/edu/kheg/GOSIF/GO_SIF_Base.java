package edu.kheg.GOSIF;

import java.io.IOException;

import org.testng.SkipException;
import org.testng.annotations.BeforeSuite;

import edu.kheg.SuiteBase.SuiteBase;
import edu.kheg.utility.Project_Utility;
import edu.kheg.utility.Suite_Utility;

public class GO_SIF_Base extends SuiteBase
{
	Project_Utility FilePath = null;
	String SheetName = null;
	String SuiteName = null;
	String ToRunColumnName = null;	
	
	//This function will be executed before SuiteOne's test cases to check SuiteToRun flag.
	@BeforeSuite
	public void checkSuiteToRun() throws IOException{		
		//Called init() function from SuiteBase class to Initialize .xls Files
		init();			
		//To set TestSuiteList.xls file's path In FilePath Variable.
		FilePath = TestSuiteListExcel;
		SheetName = "SuitesList";
		SuiteName = "GOTestData";
		ToRunColumnName = "SuiteToRun";
		
		//Bellow given syntax will Insert log In applog.log file.
		Add_Log.info("Execution started for GO_SIF_Base.");
		
		//If SuiteToRun !== "y" then SuiteOne will be skipped from execution.
		if(!Suite_Utility.checkToRunUtility(FilePath, SheetName,ToRunColumnName,SuiteName)){			
			Add_Log.info("SuiteToRun = N for "+SuiteName+" So Skipping Execution.");
			//To report SuiteOne as 'Skipped' In SuitesList sheet of TestSuiteList.xls If SuiteToRun = N.
			Suite_Utility.WriteResultUtility(FilePath, SheetName, "Skipped/Executed", SuiteName, "Skipped");
			//It will throw SkipException to skip test suite's execution and suite will be marked as skipped In testng report.
			throw new SkipException(SuiteName+"'s SuiteToRun Flag Is 'N' Or Blank. So Skipping Execution Of "+SuiteName);
		}
		//To report SuiteOne as 'Executed' In SuitesList sheet of TestSuiteList.xls If SuiteToRun = Y.
		Suite_Utility.WriteResultUtility(FilePath, SheetName, "Skipped/Executed", SuiteName, "Executed");		
	}		
}