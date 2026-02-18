package ProcurementAllPagesUpdatedHeadless;
import java.time.Duration;
import java.util.NoSuchElementException;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class InspectionList {

	WebDriver driver;

	public InspectionList(WebDriver driver) {
		this.driver = driver;
	}
	public void procurement() throws InterruptedException {
		waitForLoadItemToDisappear();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Procurement']")));
		WebElement procurement = driver.findElement(By.xpath("//span[text()='Procurement']"));
		procurement.click();
		Thread.sleep(1000);
	}
	public void waitForLoadItemToDisappear() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("ngx-spinner")));
			// Start a loop to wait indefinitely for the load item to disappear
			WebDriverWait waitInfinite = new WebDriverWait(driver, Duration.ofSeconds(50));
			while (true) {
				try {
					waitInfinite.until(ExpectedConditions.invisibilityOfElementLocated(By.tagName("ngx-spinner")));
					// System.out.println("waited for load item to disappeared");
					break; // Exit the loop if the element is no longer visible
				} catch (TimeoutException e) {
					// Continue waiting until the element disappears
				}
			}
		} catch (org.openqa.selenium.NoSuchElementException e) {
			// System.out.println("Load item did not appear");
		} catch (TimeoutException e) {
			// System.out.println("Timed out waiting for the load item to appear");
		} catch (Exception e) {
		}
	}
	public void inspectionList() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			WebElement element =wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href=\"/procurement/inspection-list\"]")));
			element.click();
		} catch (StaleElementReferenceException e) {
			WebElement element =wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href=\"/procurement/inspection-list\"]")));
			element.click();
		} catch (TimeoutException | NoSuchElementException e) {
			throw new RuntimeException("Please click on Inspection List.");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
		}
	}
	public void searchRegistrationNo(String registration_no) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement regNoField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder=\"Type to filter\"]")));
			Actions actions = new Actions(driver);
			actions.moveToElement(regNoField).perform();
			regNoField.sendKeys(registration_no);
//			Thread.sleep(500);
//			WebElement clickSearchedElement =wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//datatable-body-row[@tabindex=\"-1\"]")));
//			clickSearchedElement.click();
//			waitForLoadItemToDisappear();
		} catch (TimeoutException e) {
			throw new RuntimeException("Registration No is not found in Inspection List.");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public void clickSearchedVehicle(String registration_no) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement clickSearchedElement =wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//datatable-body-row[@tabindex=\"-1\"]")));
			clickSearchedElement.click();
			waitForLoadItemToDisappear();
		} catch (TimeoutException e) {
			throw new RuntimeException("Registration No is not found in Inspection List.");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public void clickNextButton() throws InterruptedException {
		while (true) {
			try {
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2)); // Increased wait time
				WebElement nextBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Next']")));
				Actions actions = new Actions(driver);
				actions.moveToElement(nextBtn).perform();
				Thread.sleep(500);
				actions.click(nextBtn).perform();
			} catch (StaleElementReferenceException e) {
				// System.out.println("StaleElementReferenceException encountered,
				// retrying...");
			} catch (TimeoutException e) {
				// System.out.println("Next button not found, exiting loop...");
				break; // Exit loop when button is not found
			}
		}
	}
	public void inspectionListDetail(String reg_no) throws InterruptedException {
		procurement();
		inspectionList();
		searchRegistrationNo(reg_no);		
	}
	
	public void name(String customer_name) {
		WebElement eqnuiryDateField = driver.findElement(By.xpath("//datatable-body-cell[@tabindex=\"-1\"][1]/div[@class=\"datatable-body-cell-label\"]/span"));
		String Text = eqnuiryDateField.getAttribute("title");
//	    System.out.println("Customer Name: "+Text);
		if (Text.toLowerCase().contains(customer_name.toLowerCase())) {
//			System.out.println("PASSED: Customer Name in Enquiry List");
//			ExtentReportListener.log("Customer Name in Enquiry List", "PASS");
		} else {
			System.out.println("FAILED: Customer Name in Inspeaction List");
			ExtentReportListener.log("Customer Name in Inspeaction List", "FAIL");
		}
	}
	public void source(String source) {
		WebElement eqnuiryDateField = driver.findElement(By.xpath("//datatable-body-cell[@tabindex=\"-1\"][2]/div[@class=\"datatable-body-cell-label\"]/span"));
		String Text = eqnuiryDateField.getAttribute("title");
//		System.out.println("Customer Name: "+Text);
		if (Text.toLowerCase().contains(source.toLowerCase())) {
//			System.out.println("PASSED: Source in Enquiry List");
//			ExtentReportListener.log("Source in Enquiry List", "PASS");
		} else {
			System.out.println("FAILED: Source in Inspeaction List");
			ExtentReportListener.log("Source in Inspeaction List", "FAIL");
		}
	}
	public void registrationNo(String reg_no) {
		WebElement eqnuiryDateField = driver.findElement(By.xpath("//datatable-body-cell[@tabindex=\"-1\"][4]/div[@class=\"datatable-body-cell-label\"]/span"));
		String Text = eqnuiryDateField.getAttribute("title");
//		System.out.println("Reg No: "+Text);
		if (Text.toLowerCase().contains(reg_no.toLowerCase())) {
//			System.out.println("PASSED: Registration No in Inspeaction List");
//			ExtentReportListener.log("Registration No in Inspeaction List", "PASS");
		} else {
			System.out.println("FAILED: Registration No. in Inspeaction List");
			ExtentReportListener.log("Registration No. in Inspeaction List", "FAIL");
		}
	}
	public void inspeactionType(String inspeaction_type) {
		WebElement eqnuiryDateField = driver.findElement(By.xpath("//datatable-body-cell[@tabindex=\"-1\"][5]/div[@class=\"datatable-body-cell-label\"]/span"));
		String Text = eqnuiryDateField.getAttribute("title");
//		System.out.println("Inspeaction Type: "+Text);
//		System.out.println(inspeaction_type);
		if (Text.toLowerCase().contains(inspeaction_type.toLowerCase())) {
//			System.out.println("PASSED: Inspeaction Type in Enquiry List");
//			ExtentReportListener.log("Inspeaction Type in Enquiry List", "PASS");
		} else {
			System.out.println("FAILED: Inspeaction Type in Inspeaction List");
			ExtentReportListener.log("Inspeaction Type in Inspeaction List", "FAIL");
		}
	}

	public void newCarRM(String new_car_rm) {
		WebElement eqnuiryDateField = driver.findElement(By.xpath("//datatable-body-cell[@tabindex=\"-1\"][6]/div[@class=\"datatable-body-cell-label\"]/span"));
		String Text = eqnuiryDateField.getAttribute("title");
		// System.out.println("New Car RM: "+Text);
		if (Text.toLowerCase().contains(new_car_rm.toLowerCase())) {
//			System.out.println("PASSED: New Car RM in Inspeaction List");
//			ExtentReportListener.log("New Car RM in Inspeaction List ", "PASS");
		} else {
			System.out.println("FAILED: New Car RM in Inspeaction List.");
			ExtentReportListener.log("New Car RM in Inspeaction List.", "FAIL");
		}
	}
	public void date(String date) {
		WebElement eqnuiryDateField = driver.findElement(By.xpath("//datatable-body-cell[@tabindex=\"-1\"][7]/div[@class=\"datatable-body-cell-label\"]/span"));
		String Text = eqnuiryDateField.getAttribute("title");
//		 System.out.println("New Car RM: "+Text);
		if (Text.toLowerCase().contains(date.toLowerCase())) {
//			System.out.println("PASSED: Date in Inspeaction List");
//			ExtentReportListener.log("Date in Inspeaction List ", "PASS");
		} else {
			System.out.println("FAILED: Date in Inspection List in Search Box Field.");
			ExtentReportListener.log("Date in Inspection List in Search Box Field", "FAIL");
		}
	}
	
	public void inspectionListSearchContent(String customer_name,String source,String reg_no,String inspeaction_type,String new_car_rm,String date) {
		name(customer_name);
		source(source);
		registrationNo(reg_no);
		inspeactionType(inspeaction_type);
		newCarRM(new_car_rm);
		date(date);
	}
	
	public void nameMobileEnquiryView(String customer_name, String mobile_no) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement nameMobileElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[normalize-space(text())='Name']/parent::div/following-sibling::div/label")));
			String extractedText = nameMobileElement.getText().trim();
//			System.out.println("Name Mobile"+extractedText);
//			System.out.println(customer_name);
//			System.out.println(mobile_no);
			String Name = extractedText.split("/")[0].trim();//
			String mobileNoText = extractedText.split("/")[1].trim(); //
			String extractedMobile = mobileNoText.substring(mobileNoText.lastIndexOf("/") + 1).trim();
			if (Name.equalsIgnoreCase(customer_name)) {
//				System.out.println("PASSED: Name in Contact Detail");
//				ExtentReportListener.log("Name in Contact Detail", "PASS");
			} else {
				System.out.println("FAILED: Name in Contact Detail");
				ExtentReportListener.log("Name in Contact Detail", "FAIL");
			}
			if (extractedMobile.equals(mobile_no)) {
//				System.out.println("PASSED: Mobile No in Contact Detail");
//				ExtentReportListener.log("Mobile No in Contact Detail", "PASS");
			} else {
				System.out.println("FAILED: Mobile No in Inspection List.");
				ExtentReportListener.log("Mobile No in Inspection List.", "FAIL");
			}
		} catch (org.openqa.selenium.NoSuchElementException e) {
		}
	}
	public void emailText(String email) {
		try {
			WebElement EMAILFieldtext = driver.findElement(By.xpath("//label[text()=' Email']/parent::div/following-sibling::div/label"));
			String extractedText = EMAILFieldtext.getText().trim();
			// System.out.println(extractedText);
			if (extractedText.equalsIgnoreCase(email)) {
//				System.out.println("PASSED: EMAIL in Contact Detail");
//				ExtentReportListener.log("EMAIL in Contact Detail", "PASS");
			} else {
				System.out.println("FAILED: EMAIL in Inspection List.");
				ExtentReportListener.log("EMAIL in Inspection List.", "FAIL");
			}
		} catch (TimeoutException e) {
		} catch (org.openqa.selenium.NoSuchElementException e) {
		}
	}
	public void addressText(String address) {
		try {
			WebElement nameMobileElement = driver.findElement(By.xpath("//label[text()=' Address']/parent::div/following-sibling::div/label"));
			String extractedText = nameMobileElement.getText().trim();
//			System.out.println(extractedText);
//			System.out.println(address);
			if (extractedText.contains(address)) {
//				System.out.println("PASSED: Address in Contact Detail");
//				ExtentReportListener.log("Address in Contact Detail", "PASS");
			} else {
				System.out.println("FAILED: Address in Inspection List.");
				ExtentReportListener.log("Address in Inspection List.", "FAIL");
			}
		} catch (TimeoutException e) {
		} catch (org.openqa.selenium.NoSuchElementException e) {
		}
	}
	public void companyText(String company_name) {
		try {
			WebElement element = driver.findElement(By.xpath("//label[text()=' Company']/parent::div/following-sibling::div/label"));
			String extractedText = element.getText().trim();
			// System.out.println(extractedText);
			if (extractedText.equalsIgnoreCase(company_name)) {
//				System.out.println("PASSED: Company in Contact Detail");
//				ExtentReportListener.log("Company in Contact Detail", "PASS");
			} else {
				System.out.println("FAILED: Company in Inspection List.");
				ExtentReportListener.log("Company in Inspection List.", "FAIL");
			}
		} catch (TimeoutException e) {
		} catch (org.openqa.selenium.NoSuchElementException e) {
		}
	}
	public void dateText(String date) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[text()=' Date ']/parent::div/following-sibling::div/label")));
			String extractedText = element.getText().trim();
//			System.out.println(extractedText);
//			System.out.println(date);
			String day = extractedText.split("-")[0];
//			System.out.println(day);
			if (date.contains(day)) {
//				System.out.println("PASSED: Date in Lead Detail");
//				ExtentReportListener.log("Date in Lead Detail", "PASS");
			} else {
				System.out.println("❌ FAILED: Date in Lead Detail Section in Inspection List.");
				ExtentReportListener.log("Date in Lead Detail Section in Inspection List.", "FAIL");
			}
		} catch (TimeoutException e) {
		} catch (org.openqa.selenium.NoSuchElementException e) {
		}
	}
	public void locationText(String location) throws InterruptedException {
		try {
			Thread.sleep(1000);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[label/div[text()='Location/ RA']]/following-sibling::div/label")));
			String extractedText = element.getText().trim();
			String text = extractedText.split("/")[0].trim();
//			System.out.println(extractedText);
//			System.out.println(location);
			if (text.equalsIgnoreCase(location)) {
//				System.out.println("PASSED: Location in Lead Detail");
//				ExtentReportListener.log("Location in Lead Detail", "PASS");
			} else {
				System.out.println("❌ FAILED: Location in Inspection List.");
				ExtentReportListener.log("Location in Inspection List.", "FAIL");
			}
		} catch (TimeoutException e) {
		} catch (org.openqa.selenium.NoSuchElementException e) {
		}
	}
	public void sourceText(String source) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[text()=' Source ']/parent::div/following-sibling::div/label")));
			String text = element.getText().trim();
			if (text.equalsIgnoreCase(source)) {
//				System.out.println("PASSED: Source in Lead Detail");
//				ExtentReportListener.log("Source in Lead Detail", "PASS");
			} else {
				System.out.println("❌ FAILED: Source in Inspection List.");
				ExtentReportListener.log("Source in Inspection List.", "FAIL");
			}
		} catch (TimeoutException e) {
		} catch (org.openqa.selenium.NoSuchElementException e) {
		}
	}
	public void newCarRMText(String new_car_rm) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[text()=' New Car RM ']/parent::div/following-sibling::div/label")));
			String text = element.getText().trim();
//			System.out.println(text);
			if (text.toLowerCase().contains(new_car_rm.toLowerCase())) {
//				System.out.println("PASSED: New Car RM in Lead Detail");
//				ExtentReportListener.log("New Car RM in Lead Detail", "PASS");
			} else {
				System.out.println("FAILED: New Car RM in Inspection List.");
				ExtentReportListener.log("New Car RM in Inspection List.", "FAIL");
			}
		} catch (TimeoutException e) {
		} catch (org.openqa.selenium.NoSuchElementException e) {
		}
	}
	public void remarksText(String remarks) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[text()=' Remarks ']/parent::div/following-sibling::div/label")));
			String text = element.getText().trim();
			if (text.equalsIgnoreCase(remarks)) {
//				System.out.println("PASSED: Remarks in Lead Detail");
//				ExtentReportListener.log("Remarks in Lead Detail", "PASS");
			} else {
				System.out.println("❌ FAILED: Remarks in Inspection List.");
				ExtentReportListener.log("Remarks in Inspection List.", "FAIL");
			}
		} catch (TimeoutException e) {
		} catch (org.openqa.selenium.NoSuchElementException e) {
		}
	}
	public void contactDetailText(String customer_name, String mobile_no, String email, String address, String company_name,String date, String location, String source, String new_car_rm, String remarks)throws InterruptedException {
		Thread.sleep(1000);
		nameMobileEnquiryView(customer_name, mobile_no);
		//addressText(address);
		emailText(email);
		companyText(company_name);
		dateText(date);
		locationText(location);
		sourceText(source);
		newCarRMText(new_car_rm);
		remarksText(remarks);
	}

	public void registrationNoText(String reg_no) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[text()=' Registration No/ Year ']/parent::div/following-sibling::div/label")));
			String text = element.getText().trim();
			System.out.println("Reg No "+text);
			String RegNoText = text.split("/")[0].replaceAll(" ", "");
			if (text.equalsIgnoreCase(reg_no)) {
//				System.out.println("PASSED: Registration No in Vehicle Deatil");
//				ExtentReportListener.log("Registration No in Vehicle Deatil", "PASS");
			} else {
				System.out.println("FAILED: Registration No in Inspection List.");
				ExtentReportListener.log("Registration No in Inspection List.", "FAIL");
			}
		} catch (TimeoutException e) {
		} catch (org.openqa.selenium.NoSuchElementException e) {
		}
	}

	public void makeModelText(String make,String model) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[text()=' Make/ Model ']/parent::div/following-sibling::div/label")));
			String text = element.getText().trim();
			String makeText = text.split("/")[0].trim();
			String modelText = text.split("/")[1].trim();
			System.out.println("Make Model"+text);
			if (makeText.toUpperCase().contains(make.toUpperCase())) {
//				System.out.println("PASSED: Make in Inspection List.");
//				ExtentReportListener.log("Make in Inspection List.", "PASS");
			} else {
				System.out.println("FAILED: Make in Inspection List.");
				ExtentReportListener.log("Make in Inspection List.", "FAIL");
			}
			if (modelText.toUpperCase().contains(model.toUpperCase())) {
//				System.out.println("PASSED: Model in Inspection List.");
//				ExtentReportListener.log("Model in Inspection List.", "PASS");
			} else {
				System.out.println("FAILED: Model in Inspection List.");
				ExtentReportListener.log("Model in Inspection List.", "FAIL");
			}
		} catch (TimeoutException e) {
		} catch (org.openqa.selenium.NoSuchElementException e) {
		}
	}

	public void vehicleDetailText(String reg_no, String make,String model) throws InterruptedException {
		registrationNoText(reg_no);
		makeModelText(make,model);
	}
	public void checkTextContent(String method_name, String method_value) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement element = driver.findElement(By.xpath("//label[contains(text(), '" + method_name + "')]/following-sibling::label"));
			String text = element.getText().trim();
			if (method_name.contains("Image")) {
				return;
			}
			if (method_name.contains("Rate")) {
				int filledStarCount = driver.findElements(By.cssSelector(".star-rating .fa-star.filled")).size();
				int expectedStars = Integer.parseInt(method_value.trim());		
				if (filledStarCount == expectedStars) {
					//ExtentReportListener.log(method_name + " in Inspection List.", "PASS");
					//System.out.println("PASSED: " + method_name + " in Inspection List.");
				} else {
					ExtentReportListener.log(method_name + " in Inspection List.", "FAIL");
					System.out.println("Failed " + method_name + " in Inspection List.");
				}
			} else {
				if (text.toLowerCase().contains(method_value.toLowerCase())) {
//					System.out.println("PASSED: " + method_name + " in Inspection List");
//					ExtentReportListener.log(method_name + " in Inspection List", "PASS");
				} else {
					System.out.println("FAILED: " + method_name + " in Inspection List");
					ExtentReportListener.log(method_name + " in Inspection List", "FAIL");
				}
			}
		} catch (TimeoutException e) {
		} catch (org.openqa.selenium.NoSuchElementException | NoSuchElementException e) {
		}
	}
}
