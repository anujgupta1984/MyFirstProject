package edu.kheg.GOSIF;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import edu.kheg.utility.Project_Utility;
import edu.kheg.utility.Suite_Utility;

//GO_Online_SIF Class Inherits From GO_SIF_Base Class.
//So, GO_Online_SIF Class Is Child Class Of GO_SIF_Base Class And SuiteBase Class.

public class GO_Online_SIF extends GO_SIF_Base
{
		Project_Utility FilePath = null;
		String SheetName = null;
		String TestCaseName = null;	
		String ToRunColumnNameTestCase = null;
		String ToRunColumnNameTestData = null;
		String TestDataToRun[]=null;
		static boolean TestCasePass=true;
		static int DataSet=-1;	
		static boolean Testskip=false;
		static boolean Testfail=false;
		SoftAssert s_assert =null;	
		
		@BeforeTest
		public void checkCaseToRun() throws IOException{
			//Called init() function from SuiteBase class to Initialize .xls Files
			init();			
			//To set SuiteOne.xls file's path In FilePath Variable.
			FilePath = TestCaseListExcelOne;		
			TestCaseName = this.getClass().getSimpleName();	
			//SheetName to check CaseToRun flag against test case.
			SheetName = "TestCasesList";
			//Name of column In TestCasesList Excel sheet.
			ToRunColumnNameTestCase = "CaseToRun";
			//Name of column In Test Case Data sheets.
			ToRunColumnNameTestData = "DataToRun";
			//Bellow given syntax will Insert log In applog.log file.
			Add_Log.info(TestCaseName+" : Execution started.");
			
			//To check test case's CaseToRun = Y or N In related excel sheet.
			//If CaseToRun = N or blank, Test case will skip execution. Else It will be executed.
			if(!Suite_Utility.checkToRunUtility(FilePath, SheetName,ToRunColumnNameTestCase,TestCaseName)){
				Add_Log.info(TestCaseName+" : CaseToRun = N for So Skipping Execution.");
				//To report result as skip for test cases In TestCasesList sheet.
				Suite_Utility.WriteResultUtility(FilePath, SheetName, "Pass/Fail/Skip", TestCaseName, "SKIP");
				//To throw skip exception for this test case.
				throw new SkipException(TestCaseName+"'s CaseToRun Flag Is 'N' Or Blank. So Skipping Execution Of "+TestCaseName);
			}	
			//To retrieve DataToRun flags of all data set lines from related test data sheet.
			TestDataToRun = Suite_Utility.checkToRunUtilityOfData(FilePath, TestCaseName, ToRunColumnNameTestData);
		}
		
		//Accepts 4 column's String data In every Iteration.
		@Test(dataProvider="GOOnlineData")
		public void Regular_SIF(String AS, String POI, String Spec, String Mil, String Hed, String DTR)
		{
			
			DataSet++;
			
			//Created object of testng SoftAssert class.
			s_assert = new SoftAssert();
			
			//If found DataToRun = "N" for data set then execution will be skipped for that data set.
			if(!TestDataToRun[DataSet].equalsIgnoreCase("Y")){	
				Add_Log.info(TestCaseName+" : DataToRun = N for data set line "+(DataSet+1)+" So skipping Its execution.");
				//If DataToRun = "N", Set Testskip=true.
				Testskip=true;
				throw new SkipException("DataToRun for row number "+DataSet+" Is No Or Blank. So Skipping Its Execution.");
			}
			//To Initialize Firefox browser.
			loadWebBrowser();		
			
			//To navigate to URL. It will read site URL from Param.properties file
			try{
			driver.get(Param.getProperty("siteURL")+"/index.aspx?source=KU_Test&ve=60080");		
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			Select dropdown = new Select(driver.findElement(By.name("selAreaStudy")));
			dropdown.selectByValue(AS);
			Select dropdown1 = new Select(driver.findElement(By.name("selProgram")));
			dropdown1.selectByValue(POI);
			Select dropdown2 = new Select(driver.findElement(By.name("selSpecialization")));
			dropdown2.selectByValue(Spec);
			driver.findElement(By.linkText("Next Step")).click();
			driver.findElement(By.name("txtFirstName")).sendKeys(Project_Utility.RandomString());
			driver.findElement(By.name("txtLastName")).sendKeys(Project_Utility.RandomString());
			driver.findElement(By.name("txtAddress")).sendKeys(Project_Utility.RandomString());
			driver.findElement(By.name("txtCity")).sendKeys(Project_Utility.RandomString());
			Select dropdown3 = new Select(driver.findElement(By.name("selState")));
			dropdown3.selectByValue("Florida");
			driver.findElement(By.name("txtZip")).sendKeys("33301");
			driver.findElement(By.name("txtPhone")).sendKeys("9545151111");
			String email = Project_Utility.Email(); 
			driver.findElement(By.name("txtEmail")).sendKeys(email);
			Suite_Utility.WriteResultUtility(FilePath, TestCaseName, "Email", DataSet+1, email);
			Select dropdown4 = new Select(driver.findElement(By.name("selmilitaryType")));
			dropdown4.selectByValue(Mil);
			Select dropdown5 = new Select(driver.findElement(By.name("HighestEducation")));
			dropdown5.selectByValue(Hed);
			driver.findElement(By.id("RightProofContentDropZone_columnDisplay_ctl00_controlcolumn_ctl03_WidgetHost_WidgetHost_widget_LinkButton1")).click();
			
			new FluentWait<WebDriver>(driver)
			 .withTimeout(60, TimeUnit.SECONDS)
			 .pollingEvery(5,TimeUnit.SECONDS)
			 .ignoring(NoSuchElementException.class).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='MainContentDiv']/h1[2]")));
			
			Add_Log.info("Lead for Program " +POI + " from Area of Study " +AS + " having Specialization " +Spec + " has been submitted");
			}catch (NoSuchElementException e){
				Testfail = true;			
			}
			if(Testfail){
				//At last, test data assertion failure will be reported In testNG reports and It will mark your test data, test case and test suite as fail.
				s_assert.assertAll();		
			}
		}
		
		//@AfterMethod method will be executed after execution of @Test method every time.
		@AfterMethod
		public void reporterDataResults(){		
			if(Testskip){
				Add_Log.info(TestCaseName+" : Reporting test data set line "+(DataSet+1)+" as SKIP In excel.");
				//If found Testskip = true, Result will be reported as SKIP against data set line In excel sheet.
				Suite_Utility.WriteResultUtility(FilePath, TestCaseName, "Pass/Fail/Skip", DataSet+1, "SKIP");
			}
			else if(Testfail){
				Add_Log.info(TestCaseName+" : Reporting test data set line "+(DataSet+1)+" as FAIL In excel.");
				//To make object reference null after reporting In report.
				s_assert = null;
				//Set TestCasePass = false to report test case as fail In excel sheet.
				TestCasePass=false;	
				//If found Testfail = true, Result will be reported as FAIL against data set line In excel sheet.
				Suite_Utility.WriteResultUtility(FilePath, TestCaseName, "Pass/Fail/Skip", DataSet+1, "FAIL");			
			}else{
				Add_Log.info(TestCaseName+" : Reporting test data set line "+(DataSet+1)+" as PASS In excel.");
				//If found Testskip = false and Testfail = false, Result will be reported as PASS against data set line In excel sheet.
				Suite_Utility.WriteResultUtility(FilePath, TestCaseName, "Pass/Fail/Skip", DataSet+1, "PASS");
			}
			//At last make both flags as false for next data set.
			Testskip=false;
			Testfail=false;
		}
		
		//This data provider method will return 4 column's data one by one In every Iteration.
		@DataProvider
		public Object[][] GOOnlineData(){
			//To retrieve data from Excel sheet.
			//Last two columns (DataToRun and Pass/Fail/Skip) are Ignored programatically when reading test data.
			return Suite_Utility.GetTestDataUtility(FilePath, TestCaseName);
		}	
		
		//To report result as pass or fail for test cases In TestCasesList sheet.
		@AfterTest
		public void closeBrowser(){
			//To Close the web browser at the end of test.
			closeWebBrowser();
			if(TestCasePass){
				Add_Log.info(TestCaseName+" : Reporting test case as PASS In excel.");
				Suite_Utility.WriteResultUtility(FilePath, SheetName, "Pass/Fail/Skip", TestCaseName, "PASS");
			}
			else{
				Add_Log.info(TestCaseName+" : Reporting test case as FAIL In excel.");
				Suite_Utility.WriteResultUtility(FilePath, SheetName, "Pass/Fail/Skip", TestCaseName, "FAIL");			
			}
		}
	}
