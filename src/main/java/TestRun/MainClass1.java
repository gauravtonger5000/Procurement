package TestRun;

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

public class MainClass1 {
	public MainClass1() {
	}
	WebDriver driver;
	private String excelFilePath;
	private String extentReportPath; // Path to save Extent Reports
	private ExtentReportListener reportListener;

	@BeforeClass
    public void setUp() throws IOException, EncryptedDocumentException, InterruptedException {
        driverClass dc = new driverClass(driver);
        driver = dc.browserSel();

        excelFilePath = System.getProperty("excelFilePath", "C:\\Users\\ACS-90\\Downloads\\ProcurementNew.xlsx");
        extentReportPath = System.getProperty("extentReportPath", "C:\\Users\\ACS-90\\Downloads\\Procurement_Report.html");

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
	public void NewEnquiry() throws InterruptedException, FileNotFoundException, IOException {
		Login_Page loginPage = new Login_Page(driver);
		NewEnquiry ne = new NewEnquiry(driver);
		ExtentTest test = reportListener.startTest("Procurement Page");

		File excelFile = new File(excelFilePath);
		FileInputStream fis = new FileInputStream(excelFile);
		Workbook workbook = new XSSFWorkbook(fis);
		DataFormatter formatter = new DataFormatter();

		Sheet sheet1 = workbook.getSheet("Credentials");
		Row row1 = sheet1.getRow(1);
		String url = formatter.formatCellValue(row1.getCell(0));
		driver.get(url);
		Sheet sheet2 = workbook.getSheet("New Enquiry");
		Sheet inspectionSheet = workbook.getSheet("Inspection Information");
		Sheet sheet4 = workbook.getSheet("Follow Up");

		for (int i = 1; i <= 1; i++) {
			Row row = sheet2.getRow(i);
			Row row7 = sheet4.getRow(i);

			String username = formatter.formatCellValue(row.getCell(0));
			String password = formatter.formatCellValue(row.getCell(1));
			try {
				loginPage.login(username, password);
			
				reportListener.flushReport();
			} catch (Exception e) {
				System.out.println(e.getMessage()+" in Row No. "+i);
				reportListener.log(e.getMessage()+" in Row No. "+i, "FAIL");
				// ne.signout();
				driver.get(url);
			}
			reportListener.flushReport();
		}
		System.out.println("Completed");
		workbook.close();
		//driver.quit();
	}

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        reportListener.flushReport();
    }
	
}
