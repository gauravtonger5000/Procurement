package ProcurementHeadless;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

public class MainClass {
	WebDriver driver;

	// Uncomment for run as Normal Java
//	public MainClass(WebDriver driver ) {
//		this.driver = driver;
//	}
	
	// Use for TestNG
	public MainClass() {
	}
	@BeforeClass
	public void init() throws IOException, EncryptedDocumentException, InterruptedException {
		driver = driverClass.browserSel(); // Your existing driver factory
		setup();
	}

	private String excelFilePath;
	private String extentReportPath; // Path to save Extent Reports
	private ExtentReportListener reportListener;

	public void setup() throws IOException {
//	    Get the Excel file path from system properties
		excelFilePath = System.getProperty("excelFilePath", "./src/main/resources\\ProcurementHL.xlsx");
		extentReportPath = System.getProperty("extentReportPath",
				"C:\\Users\\ACS-90\\Downloads\\Procurement Report Headless.html"); // Get the Extent Report path
//    	excelFilePath = System.getProperty("excelFilePath", "");
//    	extentReportPath = System.getProperty("extentReportPath", ""); // Get the Extent Report path    

		if (excelFilePath == null || excelFilePath.isEmpty()) {
			throw new RuntimeException("Please provide Excel file path");
		}
		if (extentReportPath == null || extentReportPath.isEmpty()) {
			throw new RuntimeException("Extent Report path not provided");
		}
		reportListener = new ExtentReportListener();
		reportListener.setupReport(extentReportPath);
//		ExtentReportListener.log("Excel file path: ", excelFilePath);
//		ExtentReportListener.log("Extent Report path: ", extentReportPath);
	}

	@Test
	public void NewEnquiry() throws InterruptedException, FileNotFoundException, IOException {
		Login_Page loginPage = new Login_Page(driver);
		NewEnquiry ne = new NewEnquiry(driver);
		EnquiryList el = new EnquiryList(driver);
		ProcurementInspectionNew pi = new ProcurementInspectionNew(driver);
		FollowUpNew follow = new FollowUpNew(driver);
		InspectionVideoList insVid = new InspectionVideoList(driver);
		InspectionList il = new InspectionList(driver);
		PriceDiscovery pd = new PriceDiscovery(driver);
		ExtentTest test = reportListener.startTest("Procurement Page");

		File excelFile = new File(excelFilePath);
		FileInputStream fis = new FileInputStream(excelFile);
		Workbook workbook = new XSSFWorkbook(fis);
		DataFormatter formatter = new DataFormatter();

		Sheet sheet1 = workbook.getSheet("Credentials");
		Row row1 = sheet1.getRow(1);
		String url = formatter.formatCellValue(row1.getCell(0));
		driver.get(url);
		String otp = formatter.formatCellValue(row1.getCell(1));

		Sheet sheet2 = workbook.getSheet("New Enquiry");
		Sheet inspectionSheet = workbook.getSheet("Inspection Information");
		Sheet sheet4 = workbook.getSheet("Follow Up");
		Sheet sheet5 = workbook.getSheet("Price Discovery");

		for (int i = 1; i <= sheet2.getLastRowNum(); i++) {
			Row row = sheet2.getRow(i);
			Row row7 = sheet4.getRow(i);
			String username = formatter.formatCellValue(row.getCell(0));
			String password = formatter.formatCellValue(row.getCell(1));

			String status = formatter.formatCellValue(row7.getCell(5));
			String location = formatter.formatCellValue(row.getCell(2));
			String date = formatter.formatCellValue(row.getCell(3));
			String enquiry_assign_to = formatter.formatCellValue(row.getCell(4));
			String reg_no = formatter.formatCellValue(row.getCell(5)).trim();
			String mobile_no = formatter.formatCellValue(row.getCell(6));
			String customer_name = formatter.formatCellValue(row.getCell(7));
			String email = formatter.formatCellValue(row.getCell(8));
			String company_name = formatter.formatCellValue(row.getCell(9));
			String source = formatter.formatCellValue(row.getCell(10));
			String remarks = formatter.formatCellValue(row.getCell(11));
			String reference_no = formatter.formatCellValue(row.getCell(12));
			String make = formatter.formatCellValue(row.getCell(13));
			String model = formatter.formatCellValue(row.getCell(14));
			String registration_year = formatter.formatCellValue(row.getCell(15));
			String follow_up_type = formatter.formatCellValue(row.getCell(16));
			String for_name = formatter.formatCellValue(row.getCell(17));
			String date_time = formatter.formatCellValue(row.getCell(18));
			String new_car_rm = formatter.formatCellValue(row.getCell(19));
			String new_car_model = formatter.formatCellValue(row.getCell(20));
			String inspection_type = formatter.formatCellValue(row.getCell(21));
			String address = formatter.formatCellValue(row.getCell(22));
			String state = formatter.formatCellValue(row.getCell(23));
			String landmark = formatter.formatCellValue(row.getCell(24));
			String city = formatter.formatCellValue(row.getCell(25));
			String pincode = formatter.formatCellValue(row.getCell(26));
			String google_loc_link = formatter.formatCellValue(row.getCell(27));
			String dealer_site_location = formatter.formatCellValue(row.getCell(28));
			String OTP = formatter.formatCellValue(row.getCell(29));
			Row insRow = inspectionSheet.getRow(1);
			try {

//==================================New Enquiry Page Started=====================================================================

				loginPage.login(username, password);
				ne.procurement();
				ne.contactDetails(location, date, enquiry_assign_to, reg_no, mobile_no, customer_name, email,
						company_name, OTP);
				ne.source(source, new_car_rm, new_car_model, remarks, reference_no);
				ne.vehicleDetails(make, model, registration_year);
				ne.followUpType(follow_up_type, inspection_type, dealer_site_location, for_name, date_time, address,
						state, landmark, city, pincode, google_loc_link);
				ne.saveChangesButton(follow_up_type, reg_no, i);
				// ne.procurement();
				// ne.signout();
				reportListener.flushReport();
				driver.get(url);

				/*****************************************
				 * Enquiry List Page Started
				 ****************************************************/

//				driver.get(url);
//				loginPage.login(username, password);
//				el.enquiryListDetail(reg_no);
//				driver.get(url);
//				reportListener.flushReport();
//				
				/*****************************************
				 * Inspection Information Page Started
				 ****************************************************/

				if (follow_up_type.toLowerCase().contains("inspection")) {
//					driver.get(url);
//					Row row6 = inspectionSheet.getRow(2);
//					String username3 = formatter.formatCellValue(row6.getCell(0));
//					String password3 = formatter.formatCellValue(row6.getCell(1));
//					loginPage.login(username3, password3);
//					
//					SuperAdminPage sp = new SuperAdminPage(driver);
//					sp.adminDetails();
//					Thread.sleep(1000);
//					
//					List<String> expectedQuestions  = sp.getAllInspectionQuestions();
//					for (String title : expectedQuestions ) {
//					   // System.out.println(title);
//					}		        

					driver.get(url);

					String username1 = formatter.formatCellValue(insRow.getCell(0));
					String password1 = formatter.formatCellValue(insRow.getCell(1));
					String inspectiontype = formatter.formatCellValue(insRow.getCell(6));
					loginPage.login(username1, password1);
					pi.procurement();
					pi.inspectionEntry(reg_no, inspectiontype);

//				    List<String> actualQuestions = pi.checkQuestions();
//				    for (String expected : expectedQuestions) {
//				        if (actualQuestions.contains(expected)) {
//				            System.out.println("Matched: " + expected);
//				            ExtentReportListener.log("Matched"+ expected, "PASS");
//				        } else {
//				            System.out.println("Not Matched: " + expected);
//				            ExtentReportListener.log("Not Matched "+expected, "FAIL");
//				        }
//				    }
					reportListener.flushReport();
					for (int k = 1; k <= 1; k++) {
						Row row5 = inspectionSheet.getRow(k);
						String question_name = formatter.formatCellValue(row5.getCell(4));
						String value = formatter.formatCellValue(row5.getCell(5));
						pi.enterQuestion(question_name, value);
					}
					pi.inspectionType(inspectiontype);
//					When Vahan Setup is On then Comment this,otherwise don't comment
					pi.waitForLoadItemToDisappear();

//					Uncomment when VAHAN Setup is on
//					pi.waitForVahanLoading();   // Use wait here for Vahan Fetching
					pi.waitForVehicleDetailsTab();
					String previousTabName = "";

					for (int j = 1; j <= inspectionSheet.getLastRowNum(); j++) {
						Row inspectionRow = inspectionSheet.getRow(j);
						String tab_name = formatter.formatCellValue(inspectionRow.getCell(7));
						String method_type = formatter.formatCellValue(inspectionRow.getCell(8));
						String method_name = formatter.formatCellValue(inspectionRow.getCell(9));
						String parameter = formatter.formatCellValue(inspectionRow.getCell(10)).trim();

						// if(Integer.parseInt(id) == i)
						// {
						if (!tab_name.equals(previousTabName)) {
							pi.tabName(tab_name);
							previousTabName = tab_name; // Update the previous tab name
						}
						pi.executeMethod(method_type, method_name, parameter, tab_name);
						// }
					}

					int totalquestion = pi.getFilledQuestionCount();
//					System.out.println("Total Questions Filled: "+totalquestion);

					Thread.sleep(500);
					pi.saveChangeBtn(reg_no);
					reportListener.flushReport();

// 					Uncomment when we use Video List in menu
//					driver.get(url);
//					loginPage.login(username1, password1);
//					insVid.inspectionVideoListEntry(reg_no);
					driver.get(url);
					reportListener.flushReport();

					/******************************
					 * Inspection List Page Started
					 *************************************************************/
//					
//					Thread.sleep(500);
//					loginPage.login(username1, password1);
//					il.inspectionListDetail(reg_no);
//					il.inspectionListSearchContent(customer_name, source, reg_no, inspectiontype, new_car_rm, date);
//					Thread.sleep(500);
//					il.clickSearchedVehicle(registration_year);
//					il.contactDetailText(customer_name, mobile_no, email, address, company_name, date, location, source, new_car_rm, remarks);
//					String previousTabName2 = "";
//					for (int j = 1; j <= inspectionSheet.getLastRowNum(); j++) {
//						Row inspectionRow = inspectionSheet.getRow(j);
//						String tab_name = formatter.formatCellValue(inspectionRow.getCell(7));
//						String method_type = formatter.formatCellValue(inspectionRow.getCell(8));
//						String method_name = formatter.formatCellValue(inspectionRow.getCell(9));
//						String method_value = formatter.formatCellValue(inspectionRow.getCell(10)).trim();
//						if (!tab_name.equals(previousTabName2)) {
//							pi.tabName(tab_name);
//							previousTabName2 = tab_name; // Update the previous tab name
//						}
//						il.checkTextContent(method_name, method_value);
//					}
//					driver.get(url);
//					reportListener.flushReport();

					/*********************************************
					 * Price Discovery Page Started
					 ****************************************************/
					Row row4 = sheet5.getRow(i);
					String usernameNew = formatter.formatCellValue(row4.getCell(0));
					String passwordNew = formatter.formatCellValue(row4.getCell(1));
					// String registration_no1 = formatter.formatCellValue(row4.getCell(2));
					String inspection_status = formatter.formatCellValue(row4.getCell(3));

					String min_price = formatter.formatCellValue(row4.getCell(4));
					String max_price = formatter.formatCellValue(row4.getCell(5));
					String refurbishment_price = formatter.formatCellValue(row4.getCell(6));
					String inspect_again_remarks = formatter.formatCellValue(row4.getCell(7));
					String reject_remarks = formatter.formatCellValue(row4.getCell(8));
					String expected_selling_price = formatter.formatCellValue(row4.getCell(9));
					loginPage.login(usernameNew, passwordNew);
					pd.pdDetails(reg_no);
					String previousTabName1 = "";
					int grandTotal = 0;
					for (int j = 1; j <= inspectionSheet.getLastRowNum(); j++) {
						Row inspectionRow = inspectionSheet.getRow(j);
						String tab_name = formatter.formatCellValue(inspectionRow.getCell(7));
						String method_type = formatter.formatCellValue(inspectionRow.getCell(8));
						String method_name = formatter.formatCellValue(inspectionRow.getCell(9));
						String method_value = formatter.formatCellValue(inspectionRow.getCell(10)).trim();
						if (!tab_name.equals(previousTabName1)) {

							pi.tabName(tab_name);
							previousTabName1 = tab_name; // Update the previous tab name
//							Thread.sleep(500);
							List<WebElement> labels = driver
									.findElements(By.xpath("//label[@class='col-5 form-label']"));

							for (WebElement label : labels) {
								// System.out.println("Question: " + label.getText().trim());
							}

//							Uncomment to check all the questions available in PD
//							int totalQuestions= labels.size();
//							System.out.println(tab_name+": " +totalQuestions);
//							grandTotal += totalQuestions;
						}
						pd.checkTextContent(method_name, method_value);
					}
//					System.out.println("Total Questions: "+grandTotal);
					Thread.sleep(500);
					pd.priceDiscoveryEntry(reg_no, inspection_status, min_price, max_price, refurbishment_price,
							inspect_again_remarks, reject_remarks, expected_selling_price);
					// Thread.sleep(500);
					driver.get(url);
					reportListener.flushReport();
					if (status.toLowerCase().contains("temporary booking")) {
						Row row5 = sheet4.getRow(i);
						String username2 = formatter.formatCellValue(row5.getCell(0));
						String password2 = formatter.formatCellValue(row5.getCell(1));
						String action_type = formatter.formatCellValue(row5.getCell(3));
						String follow_up_date = formatter.formatCellValue(row5.getCell(4));
						String status1 = formatter.formatCellValue(row5.getCell(5));
						String std_remarks = formatter.formatCellValue(row5.getCell(6));
						String follow_up_remarks = formatter.formatCellValue(row5.getCell(8));

						String final_purchase_price = formatter.formatCellValue(row5.getCell(22));
						String new_car_variant = formatter.formatCellValue(row5.getCell(25));
						String exchange_bonus_value = formatter.formatCellValue(row5.getCell(26));
						String new_car_support = formatter.formatCellValue(row5.getCell(27));
						String service_advisor = formatter.formatCellValue(row5.getCell(28));

						driver.get(url);
						loginPage.login(username2, password2);
						follow.procurement();
						follow.followUpEntry(reg_no);
						follow.actionTypeandDate(action_type, follow_up_date);
						follow.status(status1);
						follow.standardRemarks(std_remarks);
						follow.remarks(follow_up_remarks);
						follow.tempDetails(source, final_purchase_price, new_car_rm, new_car_model, new_car_variant,
								exchange_bonus_value, new_car_support, reference_no, service_advisor);
						follow.saveChangesButtonTemp(reg_no, i);
						driver.get(url);
					}
//					
///****************************************Follow Up Discovery Page Start****************************************************/
//					
				} else if (follow_up_type.toLowerCase().contains("call")
						|| follow_up_type.toLowerCase().contains("visit")) {

					Row row5 = sheet4.getRow(i);
					String username2 = formatter.formatCellValue(row5.getCell(0));
					String password2 = formatter.formatCellValue(row5.getCell(1));
					String action_type = formatter.formatCellValue(row5.getCell(3));
					String follow_up_date = formatter.formatCellValue(row5.getCell(4));
					String status1 = formatter.formatCellValue(row5.getCell(5));
					String std_remarks = formatter.formatCellValue(row5.getCell(6));
					String exp_close_date = formatter.formatCellValue(row5.getCell(7));
					String follow_up_remarks = formatter.formatCellValue(row5.getCell(8));
					String next_action_type = formatter.formatCellValue(row5.getCell(9));
					String follow_up_inspection_type = formatter.formatCellValue(row5.getCell(10));
					String follow_up_location = formatter.formatCellValue(row5.getCell(11));
					String next_action_date = formatter.formatCellValue(row5.getCell(12));
					String instruction = formatter.formatCellValue(row5.getCell(13));
					String follow_up_for_name = formatter.formatCellValue(row5.getCell(14));
					String follow_up_address = formatter.formatCellValue(row5.getCell(15));
					String follow_up_state = formatter.formatCellValue(row5.getCell(16));
					String follow_up_landmark = formatter.formatCellValue(row5.getCell(17));
					String follow_up_city = formatter.formatCellValue(row5.getCell(18));
					String follow_up_pincode = formatter.formatCellValue(row5.getCell(19));
					String follow_up_google_loc_link = formatter.formatCellValue(row5.getCell(20));
					String ins_for_name = formatter.formatCellValue(row5.getCell(21));
					String final_purchase_price = formatter.formatCellValue(row5.getCell(22));
					String price_offered = formatter.formatCellValue(row5.getCell(23));
					String customer_expected_price = formatter.formatCellValue(row5.getCell(24));

					driver.get(url);
					loginPage.login(username2, password2);
					follow.procurement();
					follow.followUpEntry(reg_no);
					follow.waitForLoadItemToDisappear();
					follow.actionTypeandDate(action_type, follow_up_date);
					follow.status(status1, follow_up_remarks, std_remarks, price_offered, customer_expected_price,
							exp_close_date, next_action_type, follow_up_inspection_type, follow_up_location,
							next_action_date, instruction, follow_up_for_name, ins_for_name, follow_up_address,
							follow_up_state, follow_up_landmark, follow_up_city, follow_up_pincode,
							follow_up_google_loc_link);
					follow.saveChangesButton(reg_no, i);
					// Thread.sleep(1000);
					// follow.signout();
					driver.get(url);
				} else {
					System.out.println("No Follow Up type matched");
				}
			} catch (Exception e) {
				NewEnquiry.captureAndSaveScreenshot(driver, "Error");
				System.out.println(e.getMessage() + " in Row No. " + i);
				ExtentReportListener.log(e.getMessage() + " in Row No. " + i, "FAIL");
				// ne.signout();
				driver.get(url);
			}
			reportListener.flushReport();
		}
		System.out.println("Completed");
		workbook.close();
		// driver.quit();
	}

	@AfterClass
	public void tearDown() {
		if (reportListener != null)
			reportListener.flushReport();

		if (driver != null)
			driver.quit();
	}
	
	
	// Uncomment to run as a Normal JAVA
//	public static void main(String[] args) throws InterruptedException, IOException {
//		WebDriver driver = null;
//		driverClass dc = new driverClass(driver);
//// 		Call browserSel() to initialize WebDriver
//		driver = driverClass.browserSel();
//		MainClass mainTest = new MainClass(driver);
//		try {
//			mainTest.setup();
//			mainTest.NewEnquiry();
//		} catch (Exception e) {
//			System.out.println("Error: " + e.getMessage());
//		}
//	}
}
