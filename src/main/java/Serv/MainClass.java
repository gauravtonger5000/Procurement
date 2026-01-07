package Serv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.aventstack.extentreports.ExtentTest;

public class MainClass {
	public MainClass(WebDriver driver) {
		this.driver = driver;
	}

	WebDriver driver;
	private String excelFilePath;
	private String extentReportPath; // Path to save Extent Reports
	private ExtentReportListener reportListener;

	@BeforeClass
	public void setUp() throws IOException, EncryptedDocumentException, InterruptedException {
		driverClass dc = new driverClass(driver);
		driver = dc.browserSel();

//		excelFilePath = System.getProperty("excelFilePath", "src/main/resources/OlxData.xlsx");
		excelFilePath = System.getProperty("excelFilePath", "C:\\Users\\ACS-90\\Downloads\\Serv.xlsx");

		extentReportPath = System.getProperty("extentReportPath",
				"C:\\Users\\ACS-90\\Downloads\\Serv_Report.html");

		if (excelFilePath == null || excelFilePath.isEmpty()) {
			throw new RuntimeException("Excel file path not provided!");
		}
		if (extentReportPath == null || extentReportPath.isEmpty()) {
			throw new RuntimeException("Extent Report path not provided!");
		}

		reportListener = new ExtentReportListener();
		reportListener.setupReport(extentReportPath);
	}

	@Test
	public void test() throws InterruptedException, FileNotFoundException, IOException {
		ExtentTest test = reportListener.startTest("Serv Report");
		Login_Page lp = new Login_Page(driver);
		ServPage sp = new ServPage(driver);
		File excelFile = new File(excelFilePath);
		FileInputStream fis = new FileInputStream(excelFile);
		Workbook workbook = new XSSFWorkbook(fis);
		DataFormatter formatter = new DataFormatter();
		Sheet sheet1 = workbook.getSheet("Credentials");
		Row row1 = sheet1.getRow(1);
		String username = formatter.formatCellValue(row1.getCell(1));
		String password = formatter.formatCellValue(row1.getCell(2));
		Sheet sheet2 = workbook.getSheet("Data");
		String url = formatter.formatCellValue(row1.getCell(0));
		driver.get(url);
		try {
			lp.login(username, password);
		}catch(Exception e) {
			System.out.println(e.getMessage());
			reportListener.log(e.getMessage(), "FAIL");
		}
		reportListener.flushReport();

		for (int i = 1; i <= sheet2.getLastRowNum(); i++) {
			Row row = sheet2.getRow(i);

//			String username = formatter.formatCellValue(row1.getCell(1));
//			String password = formatter.formatCellValue(row1.getCell(2));
			
			String subject = formatter.formatCellValue(row.getCell(0));
			String remarks = formatter.formatCellValue(row.getCell(1));
			String product_category = formatter.formatCellValue(row.getCell(2));
			String product = formatter.formatCellValue(row.getCell(3));
			String ticket_category = formatter.formatCellValue(row.getCell(4));
			String ticket_sub_category = formatter.formatCellValue(row.getCell(5));
			String assigned_to = formatter.formatCellValue(row.getCell(6));
			String sprint_month = formatter.formatCellValue(row.getCell(7));
			String priority = formatter.formatCellValue(row.getCell(8));


			try {
//				lp.login(username, password);
				sp.clickNewTicket();
				sp.clickInternal();
				sp.enterSubject(subject);
				sp.enterRemarks(remarks);
				sp.selectProductCategory(product_category);
				sp.selectProduct(product);
				sp.selectTicketCategory(ticket_category);
				sp.selectTicketSubCategory(ticket_sub_category);
				sp.selectAssignTo(assigned_to);
				sp.selectSprintMonth(sprint_month);
				sp.selectPriority(priority);
				//sp.clickSubmitButton();
				System.out.println("Ticket successfully created for Row No. "+i);
				test.pass("Ticket successfully created for Row No. "+i);
			} catch (Exception e) {
				System.out.println(e.getMessage() + " in Row No. " + i);
				reportListener.log(e.getMessage() + " in Row No. " + i, "FAIL");
				// ne.signout();
//				driver.get(url);
			}
			reportListener.flushReport();
		}
		System.out.println("Completed");
		workbook.close();
		// driver.quit();
	}

	@AfterClass
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
		reportListener.flushReport();
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		MainClass mainTest = new MainClass(null);
		try {
			mainTest.setUp(); // Browser launches here only ONCE
			mainTest.test();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

}
