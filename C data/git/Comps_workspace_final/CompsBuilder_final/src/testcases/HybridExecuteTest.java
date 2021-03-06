package testcases;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import exportExcel.POIexcel;
import operation.Readobject;
import operation.UIoperations;
import operation.Utility;

import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

import org.apache.log4j.Logger;

public class HybridExecuteTest 
{
String	FilepathforStatus="C:\\Users\\chaman.preet\\Documents\\C data\\git\\CompsBuilder_final\\TestCases - Copy.xlsx";
	static int rowflag=0;
	int excelflag=0;
	FileInputStream inputStream;
	public static ExtentHtmlReporter htmlReporter;
	public static ExtentReports extent;
	public static ExtentTest test;
	private Map<Object, String> sysprop;
	public static ExtentTest childTest;
	private static HashMap<String, WebDriver> driverObjMap=new HashMap<String, WebDriver>();
	static WebDriver webdriver;
	public static Logger APP_LOGS = Logger.getLogger("devpinoyLogger");
	
	@Test(dataProvider="hybridData")
	public static void testlogin(String testcasename,String keyword, String objectname,String objectType,String value,String runmode,  String browser,String Status) throws Exception
	{    
		String reportKeyword=new StringBuilder().append(testcasename).append(keyword).toString();
		if(runmode.equals("Y"))
		{
			if(testcasename!=null&&testcasename.length()!=0)
			{				
				APP_LOGS.info("\n\n" + "Started Executing test case-> " +testcasename+ "\n");
				test = extent.createTest(testcasename,"Test Case: "+testcasename+ " Running on Browser: "+browser );
				if(browser.equals("IE"))
				{
					DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
					capabilities.setCapability(CapabilityType.BROWSER_NAME, "IE");
					capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
					System.setProperty("webdriver.ie.driver", "C://Users//chaman.preet//Documents//C data//git//Comps_project//IEDriver//IEDriverServer.exe");
					webdriver=new InternetExplorerDriver(capabilities);
					webdriver.manage().window().maximize();
				}
				
				else if(browser.equals("Chrome"))
				{
					DesiredCapabilities capabilities = DesiredCapabilities.chrome();
					ChromeOptions options = new ChromeOptions();
					capabilities.setCapability(CapabilityType.BROWSER_NAME, "Chrome");
					 //capabilities.setCapability(ChromeOptions.CAPABILITY, options);
					System.setProperty("webdriver.chrome.driver", "C://Users//chaman.preet//Documents//C data//git//Comps_workspace_final//CompsBuilder_final//ChromeDriver//chromedriver.exe");
					webdriver=new ChromeDriver(capabilities);
					webdriver.manage().window().maximize();
				}
				
				else if(browser.equals("Firefox"))
				{
					FirefoxProfile fp = new FirefoxProfile();
					fp.setPreference("network.proxy.type", ProxyType.AUTODETECT.ordinal());
					System.setProperty("webdriver.gecko.driver", "C://Users//chaman.preet//Downloads//geckodriver.exe");
					//download start from here
					fp.setPreference("browser.download.folderList",2);
					fp.setPreference("browser.download.manager.showWhenStarting", false);
					fp.setPreference("browser.download.dir", "C:\\Users\\chaman.preet\\Downloads\\");
					fp.setPreference("browser.helperApps.neverAsk.openFile",
							"text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");
					fp.setPreference("browser.helperApps.neverAsk.saveToDisk",
							"text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");
					fp.setPreference("browser.helperApps.alwaysAsk.force", false);
					fp.setPreference("browser.download.manager.alertOnEXEOpen", false);
					fp.setPreference("browser.download.manager.focusWhenStarting", false);
					fp.setPreference("browser.download.manager.useWindow", false);
					fp.setPreference("browser.download.manager.showAlertOnComplete", false);
					fp.setPreference("browser.download.manager.closeWhenDone", false);
					// download end here 
					webdriver=new FirefoxDriver(fp);
					webdriver.manage().window().maximize();
				}
			}  //browser end 

			Readobject robject=new Readobject();
			Properties allobjects=robject.getobjectrepository();
			UIoperations Uoperation=new UIoperations(webdriver);
			Uoperation.perform(allobjects, keyword, objectname, objectType, value);
			UploadComps objupload=new UploadComps(webdriver);
			Download_comps downobj=new Download_comps(webdriver);
			TradingComps tradeobj=new TradingComps(webdriver);
			downobj.download(allobjects, keyword, objectname, objectType, value);
			objupload.upload(allobjects, keyword, objectname, objectType, value);
			tradeobj.trade(allobjects, keyword, objectname, objectType, value);
		} //if end 
		else if(testcasename.length()!=0&&testcasename!=null&&runmode.equals("N"))
		{
		test= extent.createTest(reportKeyword,"Test Case: "+ " Running on Browser: "+browser );

			throw new SkipException("test cases skipped ");

		}
		else
		{
			throw new SkipException("test cases skipped ");
		}
	}

	@DataProvider(name="hybridData")
	public Object[][] getDatafromDataprovider() throws IOException, IllegalArgumentException, InvocationTargetException, IllegalAccessException
	{
		Object[][] object=null;
		POIexcel file=new POIexcel();
		// main code working 
		XSSFSheet sheet=file.readexcel("C:\\Users\\chaman.preet\\Documents\\C data\\git\\CompsBuilder_final", "TestCases - Copy.xlsx", "Comps");
		int rowcount=sheet.getLastRowNum()-sheet.getFirstRowNum();
		System.out.println("row count is " +rowcount);
		int col_count=sheet.getRow(1).getPhysicalNumberOfCells();
		object=new Object[rowcount][col_count];
		for(int i=0;i<rowcount;i++)
		{
			XSSFRow row=sheet.getRow(i+1);
			for (int j = 0; j < row.getLastCellNum(); j++) {
				//Print excel data in console
				XSSFCell cell=row.getCell(j);
				object[i][j] = cell.toString();	
				//System.out.println("values are"+" " +object[i][j]);
			}
		}

		System.out.println("");
		return object;    
	}
	// main code working 
	public static WebDriver gerDriverDetails(String className){
		return driverObjMap.get(className);
	}
	public static HashMap<String, WebDriver> gerDriverObjMap(){
		return driverObjMap;
	}
	
	@AfterMethod
	public void screenshot(ITestResult result) throws IOException
	{
		try 
		{ // extent.endTest(test);
			String methodname1=result.getName().toString().trim();
			String methodname= result.getName()+ "-" + Arrays.toString(result.getParameters());
			String report=methodname.substring(0,30);
			String scrnshotname=methodname.substring(12,30 );
			if(ITestResult.SUCCESS==result.getStatus() ||  ITestResult.FAILURE==result.getStatus())
			{
				APP_LOGS.info("Test step is -> " +result.getName()+ "-" + Arrays.toString(result.getParameters()));

			}

			if(ITestResult.SUCCESS==result.getStatus())
			{
				APP_LOGS.info("PASS");
				FileInputStream inputStream;
				try {
					inputStream = new FileInputStream(FilepathforStatus);
					Workbook myworkbbok = null;
					myworkbbok = new XSSFWorkbook(inputStream);
					Sheet sheet = myworkbbok.getSheet("Comps");
					XSSFRow row = (XSSFRow) sheet.getRow(excelflag+1);
					XSSFCell cell1 = row.getCell(7);
					cell1.setCellValue("pass");
					inputStream.close();
					FileOutputStream outputStream = new FileOutputStream(FilepathforStatus);
					myworkbbok.write(outputStream);
					outputStream.close();
					excelflag++;

				} catch (Exception e1) {
					e1.printStackTrace();
				}
				//System.out.println("Screenshot taken for pass test cases  ");
				Utility.capturescreenshot(webdriver, scrnshotname,"C:\\Users\\chaman.preet\\Documents\\C data\\git\\CompsBuilder_final\\ScreenShotForPassTestCases\\");
				test.pass(MarkupHelper.createLabel(report+" Test Step PASSED", ExtentColor.GREEN));

			} 


			else if (ITestResult.FAILURE==result.getStatus())	
			{	
				Throwable cause = result.getThrowable();
				if (null != cause) {
					APP_LOGS.error(" **FAIL - " +cause.getMessage());}

				try {
					inputStream = new FileInputStream(FilepathforStatus);
					Workbook myworkbbok = null;
					myworkbbok = new XSSFWorkbook(inputStream);
					Sheet sheet = myworkbbok.getSheet("Comps");
					XSSFRow row = (XSSFRow) sheet.getRow(excelflag+1);
					XSSFCell cell1 = row.getCell(7);
					cell1.setCellValue("fail");
					inputStream.close();
					FileOutputStream outputStream = new FileOutputStream(FilepathforStatus);
					myworkbbok.write(outputStream);
					outputStream.close();
					excelflag++;
				} 
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
				Utility.capturescreenshot(webdriver, scrnshotname,"C:\\Users\\chaman.preet\\Documents\\C data\\git\\CompsBuilder_final\\ScreenShotForFailTestCases\\");
				test.fail(MarkupHelper.createLabel(report+" Test Step failed", ExtentColor.RED));

				String screenShotPath = Utility.capture(webdriver, scrnshotname);
			test.addScreenCaptureFromPath(screenShotPath);
			}
			else if (ITestResult.SKIP==result.getStatus())	
			{
				try {
					inputStream = new FileInputStream(FilepathforStatus);
					Workbook myworkbbok = null;
					myworkbbok = new XSSFWorkbook(inputStream);
					Sheet sheet = myworkbbok.getSheet("Comps");
					XSSFRow row = (XSSFRow) sheet.getRow(excelflag+1);
					XSSFCell cell1 = row.getCell(7);
					cell1.setCellValue("skip");
					inputStream.close();
					FileOutputStream outputStream = new FileOutputStream(FilepathforStatus);
					myworkbbok.write(outputStream);
					outputStream.close();
					excelflag++;
				} catch (Exception e) {
					e.printStackTrace();			}

				test.skip(MarkupHelper.createLabel(report+" Test Step SKIPPED", ExtentColor.ORANGE));
			}
			}
		
		catch (Exception e)
		{

			System.out.println("Exception while taking screenshot "+e.getMessage());
		} 


	}
	@BeforeSuite
	public void beforesuite() throws UnknownHostException
	{
		String username = System.getProperty("user.name");
		String OS=System.getProperty("os.name");
		String Hostname=InetAddress.getLocalHost().getHostName();
		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") +"/test-output/MyOwnReport.html");
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		htmlReporter.loadConfig((System.getProperty("user.dir") +"/extent-config.xml"));
		extent.setSystemInfo("OS", OS);
		extent.setSystemInfo("Host Name",Hostname);
		extent.setSystemInfo("Environment", "QA");
		extent.setSystemInfo("User Name", username);
		htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter.config().setDocumentTitle("CompsBuilder Automation Report ");
		htmlReporter.config().setReportName("Comps Regression Testing Report ");
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		htmlReporter.config().setTheme(Theme.STANDARD);
	}
	@AfterSuite
	public void aftersuite()
	{
		extent.flush();
	}


}


