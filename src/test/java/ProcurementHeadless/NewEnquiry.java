package ProcurementHeadless;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NewEnquiry {

	WebDriver driver;
	public NewEnquiry(WebDriver driver) {
		this.driver = driver;
	}
	public void procurement() {

	    try {
	        waitForLoadItemToDisappear();
	        try {
	        	driver.findElement(By.xpath("(//div[@data-bs-toggle='sidebar'])[1]")).click();
	        }catch(Exception e) {
	        	
	        }
	        // 📸 Screenshot before clicking
//	        captureAndSaveScreenshot(driver, "Procurement_Before_Click");

	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
	        JavascriptExecutor js = (JavascriptExecutor) driver;

	        By procurementLocator = By.xpath("//span[normalize-space()='Procurement']");

	        // Wait until element is present in DOM
	        WebElement procurement = wait.until(
	                ExpectedConditions.presenceOfElementLocated(procurementLocator));

	        // Wait until clickable (better than only visible)
	        procurement = wait.until(ExpectedConditions.elementToBeClickable(procurementLocator));

	        // Scroll into view (important for headless / hidden elements)
	        js.executeScript("arguments[0].scrollIntoView({block:'center'});", procurement);

	        // Small wait for smooth rendering (better than Thread.sleep)
	        wait.until(ExpectedConditions.visibilityOf(procurement));

	        // Try normal click first
	        try {
	            procurement.click();
	        } catch (Exception clickIssue) {
	            // Fallback → JS click if intercepted
	            js.executeScript("arguments[0].click();", procurement);
	        }

	        // 📸 Screenshot after clicking
//	        captureAndSaveScreenshot(driver, "Procurement_After_Click");

//	        System.out.println("✅ Successfully clicked on Procurement");

	    } catch (Exception e) {
	        throw new RuntimeException("❌ Failed to click on Procurement: " + e.getMessage(), e);
	    }
	}

//	 public static void captureAndSaveScreenshot(WebDriver driver, String fileName) throws IOException {
//
//	        // Take screenshot
//	        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
////	        TakesScreenshot ts = (TakesScreenshot)driver;
////	        ts.getScreenshotAs(OutputType.FILE);
//	        // Downloads folder path
//	        String downloadPath = System.getProperty("user.home") + "\\Downloads\\" + fileName + ".png";
//
//	        File dest = new File(downloadPath);
//	        Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
//	        System.out.println("Screenshot saved at: " + downloadPath);
//	    }
	 public static void captureAndSaveScreenshot(WebDriver driver, String fileName) {
		    try {
		        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		        String screenshotDir = System.getProperty("user.dir")
		                + File.separator + "screenshots";

		        File dir = new File(screenshotDir);
		        if (!dir.exists()) {
		            dir.mkdirs();   // create folder if not exists
		        }

		        File dest = new File(screenshotDir + File.separator + fileName + ".png");
		        Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);

		        System.out.println("Screenshot saved: " + dest.getAbsolutePath());

		    } catch (Exception e) {
		        e.printStackTrace();
		    }
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
	public void newEnquiry() throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
			WebElement newEnquiry = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href=\"/procurement/new-enquiry\"]")));
			newEnquiry.click();
		} catch (StaleElementReferenceException e) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement newEnquiry = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href=\"/procurement/new-enquiry\"]")));
			newEnquiry.click();
		} catch (Exception e) {
			throw new RuntimeException("Please click on new enquiry");
		}
	}
	public void location(String location) throws InterruptedException {
		waitForLoadItemToDisappear();
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			Actions action = new Actions(driver);
			WebElement locationField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ng-select[@formcontrolname=\"locationName\"]")));
			WebElement clearAll = driver.findElement(By.xpath("//ng-select[@formcontrolname=\"locationName\"]//span[@title=\"Clear all\"]"));
			action.moveToElement(clearAll).click().perform();
			Thread.sleep(500);
			action.moveToElement(locationField).sendKeys(location).sendKeys(Keys.ENTER).perform();
			action.sendKeys(Keys.TAB).perform();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofMillis(200));
			WebElement regNoError = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=' Please Select Location ']")));
			if (regNoError.isDisplayed()) {
				throw new RuntimeException("Please Select Location.");
			}
		} catch (TimeoutException e) {
		} catch (RuntimeException e) {
			throw e;
		}
		catch (Exception e) {
		}
	}
	public void Date(String date) throws InterruptedException {
		try {
			Actions action = new Actions(driver);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			WebElement dateInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@formcontrolname='todayDate']")));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dateInput);
			try {
				dateInput.click();
			}catch(ElementClickInterceptedException e) {
				//dateInput.click();
			}
			dateInput.sendKeys(Keys.chord(Keys.CONTROL, "a"));
			dateInput.sendKeys(Keys.BACK_SPACE);
			dateInput.sendKeys(date);
			WebElement emailField = driver.findElement(By.xpath("//Input[@placeholder=\"Email\"]"));
			action.moveToElement(emailField).click().perform();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(1));
			WebElement errorMsg = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=\" Please enter valid Date \"]")));
			if (errorMsg.isDisplayed()) {
				throw new RuntimeException("Please enter valid Date.");
			}
		} catch (TimeoutException e) {
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
		}
	}
	public void enquiryAssignTo(String enquiry_assign_to) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement typeField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ng-select[@formcontrolname=\"enquiryAssign\"]")));
			Actions action = new Actions(driver);
			typeField.click();
			// WebElement clearAll =
			// driver.findElement(By.xpath("//ng-select[@formcontrolname=\"enquiryAssign\"]//span[@title=\"Clear all\"]"));
			// action.moveToElement(clearAll).click().perform();
			action.moveToElement(typeField).sendKeys(enquiry_assign_to).perform();
			action.sendKeys(Keys.ENTER).perform();
			action.sendKeys(Keys.TAB).perform();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofMillis(200));
			try {
				WebElement regNoError = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=' Please select Enquiry Assign To ']")));
				if (regNoError.isDisplayed()) {
					throw new RuntimeException("Please select Enquiry Assign To.");
				}
			} catch (TimeoutException e) {
			}
		} catch (TimeoutException e) {
			throw new RuntimeException("Enquiry Assign To Field is not found");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
		}
	}
	public void registrationNo(String reg_no) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement regNoField =wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname=\"registerNumber\"]")));
			regNoField.sendKeys(reg_no);
			Actions action = new Actions(driver);
			action.sendKeys(Keys.TAB).perform();
			try {
				WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(5));
				WebElement closeBtn=wait2.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class=\"close\"]")));
				action.moveToElement(closeBtn).perform();
				closeBtn.click();
				throw new RuntimeException(reg_no+" This registration number is already registered.");
			} catch (TimeoutException e) {
			}
			try {
				driver.findElement(By.xpath("//div[text()=\"This Vehicle is already in Dealer Stock!\"]"));
				throw new RuntimeException(reg_no+" This Vehicle is already in Dealer Stock.");
			} catch (org.openqa.selenium.NoSuchElementException e) {
			}
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofMillis(200));
			try {
				WebElement regNoError = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=\" Please Provide Register Number Like DL8BH4516 OR 22BH1234AA \"]")));
				if (regNoError.isDisplayed()) {
					throw new RuntimeException("Enter a valid registration number.");
				}
			} catch (TimeoutException e) {
			}
		} catch (TimeoutException e) {
			throw new RuntimeException("Registration No field is not found");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
		}
	}
	public void mobileNo(String mobile_no,String customer_name,String email,String company_name,String otp) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement mobileNo = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//Input[@formcontrolname=\"mobileNumber\"]")));
			mobileNo.sendKeys(mobile_no);
			Actions action = new Actions(driver);
			action.sendKeys(Keys.TAB).perform();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(2));
			try {
				WebElement alertMessage = wait1.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(text(),\"There is a registered owner\")]")));
				WebElement okButton = driver.findElement(By.xpath("//button[text()=\"Yes\"]"));
				Thread.sleep(1000);
				okButton.click();
				
//				enterOTP(otp);
//				Thread.sleep(500);
				if (!customer_name.isBlank() || !email.isBlank() || !company_name.isBlank()) {
					WebDriverWait wait3 = new WebDriverWait(driver, Duration.ofSeconds(3));
					WebElement editBtn = wait3.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class=\"btn btn-success bi bi-pencil-square btn-circle\"]")));
					editBtn.click();			
					customerNameUpdated(customer_name);
					emailUpdated(email);
					companyNameUpdated(company_name);
					WebElement saveBtn=driver.findElement(By.xpath("//button[text()=\"Save Changes\"]"));
					action.moveToElement(saveBtn).click().perform();
					Thread.sleep(500);
					wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()=\"OK\"]"))).click();
				}
			} catch (TimeoutException e) {
				customerName(customer_name);
				email(email);
				companyName(company_name);
			}
			try {
				WebElement regNoError = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=\" Please Provide Valid Mobile Number \"]")));
				if (regNoError.isDisplayed()) {
					throw new RuntimeException("Invalid mobile No. Please enter a 10-digit number.");
				}
			} catch (TimeoutException e) {
			}
		} catch (TimeoutException e) {
			throw new RuntimeException("Mobile No Field not found");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
		}
	}
	public void enterOTP(String otp) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofMillis(200));

		try {
			WebElement otpModal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h4[text()=\"OTP Verification\"]")));
			try {
				WebElement otpField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder=\"Enter OTP\"]")));
				otpField.sendKeys(otp);
			}catch(TimeoutException e) {
				throw new RuntimeException("OTP field not found");
			}
			Actions action = new Actions(driver);
			action.sendKeys(Keys.TAB).perform();
			try {
				WebElement otpField = wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=\"Please enter 6 digit OTP\"]")));
				throw new RuntimeException("Please Enter 6 Digit OTP");
			}catch(TimeoutException e) {
				
			}
			try {
				WebElement verifyBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()=\"Verify OTP\"]")));
				verifyBtn.click();
			}catch(TimeoutException e) {
				throw new RuntimeException("Verify Button either not clickable or not found");
			}
			try {
				WebElement otpField = wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=\"OTP is incorrect\"]")));
				throw new RuntimeException("OTP is incorrect");
			}catch(TimeoutException e) {
			}
			try {
				WebElement successAlert = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id=\"swal2-html-container\"]")));
				String successALertText= successAlert.getText();
				if(successALertText.contains("Successfully")) {
					WebElement okBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()=\"OK\"]")));
					okBtn.click();
				}
			}catch(TimeoutException e) {
				throw new RuntimeException("Alert Not found");
			}
		}catch(TimeoutException e) {
			System.out.println("Waited for OTP but the OTP modal had not arrived");
			ExtentReportListener.log("Waited for OTP but the OTP modal had not arrived", "WARNING");
			//throw new RuntimeException("Waited for OTP but the OTP modal had not arrived");
		}
	}
	public void customerName(String customer_name) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement custField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//Input[@placeholder=\"Customer Name\"]")));
			custField.sendKeys(customer_name);
			Actions action = new Actions(driver);
			action.sendKeys(Keys.TAB).perform();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofMillis(200));
			try {
				WebElement errorMsg = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=\" Please Provide Valid Customer Name \"]")));
				if (errorMsg.isDisplayed()) {
					throw new RuntimeException("Please Enter Valid Customer Name.");
				}
			} catch (TimeoutException e) {
			}
		} catch (TimeoutException e) {
			throw new RuntimeException("Customer Name Field Not found");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
		}
	}
	public void customerNameUpdated(String customer_name) throws InterruptedException {
		if(customer_name.isEmpty() || customer_name.isBlank()){
			return;
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement custField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname=\"updateCustomerName\"]")));
			custField.clear();
			Thread.sleep(200);
			custField.sendKeys(customer_name);
			Actions action = new Actions(driver);
			action.sendKeys(Keys.TAB).perform();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofMillis(200));
			try {
				WebElement errorMsg = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=\" Please Provide Valid Customer Name \"]")));
				if (errorMsg.isDisplayed()) {
					throw new RuntimeException("Please Enter Valid Customer Name.");
				}
			} catch (TimeoutException e) {
			}
		} catch (TimeoutException e) {
			throw new RuntimeException("Customer Name Field Not found");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
		}
		Thread.sleep(500);
	}
	public void email(String email) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement emailField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//Input[@placeholder=\"Email\"]")));
			emailField.sendKeys(email);
			String emailRegex = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";
			if (email.matches(emailRegex)) {
				// System.out.println("Valid email address.");
			} else {
				// System.out.println("Invalid email address.");
				throw new RuntimeException("Please enter valid email address.");
				// You can throw an exception or fail the test here
			}
		} catch (TimeoutException e) {
			throw new RuntimeException("Email Field not found");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			System.out.println("Error in email :" + e.getMessage());
		}
	}
	public void emailUpdated(String email) throws InterruptedException {
		if(email.isEmpty() || email.isBlank()){
			return;
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement emailField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname=\"updateCustomerEmail\"]")));
			emailField.clear();
			emailField.sendKeys(email);
			String emailRegex = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";
			if (email.matches(emailRegex)) {
				// System.out.println("Valid email address.");
			} else {
				// System.out.println("Invalid email address.");
				throw new RuntimeException("Please enter valid email address.");
				// You can throw an exception or fail the test here
			}
		} catch (TimeoutException e) {
			throw new RuntimeException("Email Field not found");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			System.out.println("Error in email :" + e.getMessage());
		}
		Thread.sleep(500);
	}
	public void companyName(String company_name) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement companyCheckBox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@class=\"ckbox wd-16 mg-b-0\"]")));
			companyCheckBox.click();
			Thread.sleep(500);
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//Input[@placeholder=\"Company Name\"]")));
			element.sendKeys(company_name);
		} catch (TimeoutException e) {
			throw new RuntimeException("Company Name Field Not found");
		} catch (Exception e) {
			System.out.println("Error in Company Name :" + e.getMessage());
		}
	}
	public void companyNameUpdated(String company_name) throws InterruptedException {
		if(company_name.isEmpty() || company_name.isBlank()){
			return;
		}
		try {
			Thread.sleep(1000);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement companyCheckBox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//label[@class=\"ckbox wd-16 mg-b-0\"])[2]")));
			companyCheckBox.click();
			Thread.sleep(500);
			WebElement emailField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname=\"updateCompanyName\"]")));
			emailField.clear();
			Thread.sleep(200);
			emailField.sendKeys(company_name);

		} catch (TimeoutException e) {
			throw new RuntimeException("Company Name Field Not found");
		} catch (Exception e) {
//			System.out.println("Error in Company Name :" + e.getMessage());
		}
		Thread.sleep(500);
	}
	public void contactDetails(String location, String date, String enquiry_assign_to, String reg_no, String mobile_no,String customer_name, String email, String company_name,String otp) throws InterruptedException {
		newEnquiry();
		location(location);
		Date(date);
		enquiryAssignTo(enquiry_assign_to);
		registrationNo(reg_no);
		mobileNo(mobile_no,customer_name,email,company_name,otp);
//		customerName(customer_name);
//		email(email);
//		companyName(company_name);
	}
	public void source(String source, String new_car_rm, String new_car_model, String remarks, String reference_no)
			throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement sourceField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ng-select[@formcontrolname=\"sourceName\"]")));
			Actions action = new Actions(driver);
			WebElement clearAll = driver.findElement(By.xpath("//ng-select[@formcontrolname=\"sourceName\"]//span[@title=\"Clear all\"]"));
			action.moveToElement(clearAll).click().perform();
			action.moveToElement(sourceField).sendKeys(source).perform();
			action.sendKeys(Keys.ENTER).perform();
			action.sendKeys(Keys.TAB).perform();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofMillis(200));
			try {
				WebElement regNoError = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=' Please Select Source Name ']")));
				if (regNoError.isDisplayed()) {
					throw new RuntimeException("Please Select Source Name.");
				}
			} catch (TimeoutException e) {
				// No error message appeared, so proceed with the remaining steps
			}
			enquiry(remarks, reference_no);
			if (source.toUpperCase().contains("NEW CAR EXCHANGE")) {
				newCarRM(new_car_rm);
				newCarModel(new_car_model);
			}
		} catch (TimeoutException e) {
			throw new RuntimeException("Source Folder is not found");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
		}
	}
	public void newCarRM(String new_car_rm) throws InterruptedException {
		try {
			Thread.sleep(500);
			WebElement newCarRMField = driver.findElement(By.xpath("//ng-select[@formcontrolname=\"carRM\"]"));
			Actions action = new Actions(driver);
			try {
				WebElement modelField = driver.findElement(By.xpath("//ng-select[@formcontrolname=\"newCarModel\"]"));
				action.moveToElement(modelField).perform();
			} catch (NoSuchElementException e) {
			}
			action.moveToElement(newCarRMField).click().sendKeys(Keys.ENTER).perform();
			WebElement clearAll = driver.findElement(By.xpath("//ng-select[@formcontrolname=\"carRM\"]//span[@title=\"Clear all\"]"));
			action.moveToElement(clearAll).click().perform();
			
			Thread.sleep(200);
			action.sendKeys(new_car_rm).perform();
			Thread.sleep(200);
			action.sendKeys(Keys.ENTER).perform();
			action.sendKeys(Keys.TAB).perform();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofMillis(200));
			try {
				WebElement regNoError = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=' Please Enter Car RM ']")));
				if (regNoError.isDisplayed()) {
					throw new RuntimeException("Please Enter Car RM.");
				}
			} catch (TimeoutException e) {

			}
		} catch (TimeoutException e) {
			throw new RuntimeException("New Car Rm Field Not found");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
		}
	}
	public void newCarModel(String new_car_model) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement modelField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ng-select[@formcontrolname=\"newCarModel\"]")));
			Actions action = new Actions(driver);
			action.moveToElement(modelField).click().sendKeys(Keys.ENTER).perform();
			try {
				WebElement clearAll = driver.findElement(By.xpath("//ng-select[@formcontrolname=\"newCarModel\"]//span[@title=\"Clear all\"]"));
				action.moveToElement(clearAll).click().perform();
			}catch(org.openqa.selenium.NoSuchElementException e) {
			}
			action.sendKeys(new_car_model).sendKeys(Keys.ENTER).perform();
			action.sendKeys(Keys.TAB).perform();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofMillis(200));
			try {
				WebElement regNoError = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=' Please Enter Model Name ']")));
				if (regNoError.isDisplayed()) {
					throw new RuntimeException("Please Enter Model Name.");
				}
			} catch (TimeoutException e) {
			}
		} catch (TimeoutException e) {
			throw new RuntimeException("Enquiry Model Field Not Found");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
		}
	}
	public void remarks(String remarks) {
		try {
			WebElement remarksField = driver.findElement(By.xpath("//Input[@placeholder=\"Remarks\"]"));
			Actions action = new Actions(driver);
			action.moveToElement(remarksField).click().sendKeys(remarks).perform();
		} catch (NoSuchElementException e) {
			throw new RuntimeException("Remarks Field is not found");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			System.out.println("Error in Source :" + e.getMessage());
		}
	}
	public void referenceNumber(String reference_no) throws InterruptedException {
		try {
			WebElement referenceNo = driver.findElement(By.xpath("//Input[@placeholder=\"Reference Number\"]"));
			Actions action = new Actions(driver);
			action.moveToElement(referenceNo).click().sendKeys(reference_no).perform();
		} catch (NoSuchElementException e) {
			throw new RuntimeException("Reference Field is not Found");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			System.out.println("Error in Source :" + e.getMessage());
		}
	}
	public void enquiry(String remarks, String reference_no) throws InterruptedException {
		remarks(remarks);
		referenceNumber(reference_no);
	}
	public void make(String make) throws InterruptedException {
		try {
			JavascriptExecutor js = (JavascriptExecutor)driver;
			js.executeScript("window.scrollBy(0, 150);");
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofMillis(200));
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement makeField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ng-select[@formcontrolname=\"defaultMake\"]")));
			Thread.sleep(500);
			Actions action = new Actions(driver);
			try {
				WebElement clearAll = wait1.until(ExpectedConditions.elementToBeClickable(By.xpath("//ng-select[@formcontrolname=\"defaultMake\"]//span[@title=\"Clear all\"]")));
				action.moveToElement(clearAll).perform();
				Thread.sleep(200);
				action.moveToElement(clearAll).click().perform();

			}catch(TimeoutException e) {
			}
			Thread.sleep(500);
			action.moveToElement(makeField).sendKeys(make).sendKeys(Keys.ENTER).perform();
			Thread.sleep(100);
			action.sendKeys(Keys.TAB).perform();
			try {
				WebElement regNoError = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=' Please Select Make ']")));
				if (regNoError.isDisplayed()) {
					throw new RuntimeException("Please Select Make.");
				}
			} catch (TimeoutException e) {
			}
		} catch (TimeoutException e) {
			throw new RuntimeException("Make Field is not found");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public void model(String model) throws InterruptedException {
		try {
			Thread.sleep(1500);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement modelField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ng-select[@formcontrolname=\"defaultModel\"]")));
			Actions action = new Actions(driver);
			action.moveToElement(modelField).click().sendKeys(model).perform();
			Thread.sleep(500);
			action.sendKeys(Keys.ENTER).perform();
		} catch (TimeoutException e) {
			throw new RuntimeException("Model Field is not found");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	public void registrationYear(String registration_year) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement regYearField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ng-select[@formcontrolname=\"registrationYear\"]")));
			Actions action = new Actions(driver);
			action.moveToElement(regYearField).click().sendKeys(registration_year).perform();
			Thread.sleep(500);
			action.sendKeys(Keys.ENTER).perform();
		} catch (TimeoutException e) {
			throw new RuntimeException("Registration Year Field is not found");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	public void vehicleDetails(String make, String model, String registration_year) throws InterruptedException {
		make(make);
		model(model);
		registrationYear(registration_year);
	}
	public void newActionTask(String follow_up_type) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
		WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(5));
		try {
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ng-select[@formcontrolname=\"followUpType\"]")));
		}catch(TimeoutException e) {
		   WebElement opennextActionTask = wait2.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='ml-2']")));
		   Actions action = new Actions(driver);
		   action.moveToElement(opennextActionTask).perform();
		   JavascriptExecutor js = (JavascriptExecutor) driver;
		   js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", opennextActionTask);
		   js.executeScript("arguments[0].click();", opennextActionTask); 
		}	
		Thread.sleep(1000);
	}
	public void followUpType(String follow_up_type, String inspection_type, String dealer_site_location,String for_name, String date_time, String address, String state, String landmark, String city,String pincode, String google_loc_link) throws InterruptedException {
		if(follow_up_type.isBlank() || follow_up_type.isEmpty()) {
			System.out.println("Follow Up Type is Blank");
			return;
		}
		newActionTask(follow_up_type);
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement followUp =wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ng-select[@formcontrolname=\"followUpType\"]")));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", followUp);
			Thread.sleep(500);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", followUp);
			Thread.sleep(1000);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView(true);", followUp);
			followUp.click();
			Thread.sleep(200);
			Actions action = new Actions(driver);
			WebElement clearAll = driver.findElement(By.xpath("//ng-select[@formcontrolname=\"followUpType\"]//span[@title=\"Clear all\"]"));
			action.moveToElement(clearAll).click().perform();
			Thread.sleep(500);
			action.sendKeys(follow_up_type).sendKeys(Keys.ENTER).perform();
			waitForLoadItemToDisappear();
			Thread.sleep(500);
//			WebElement dateInput = driver.findElement(By.xpath("//input[@formcontrolname=\"planDateTime\"]"));
//			action.moveToElement(dateInput).click().perform();
			try {
				WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofMillis(200));
				WebElement regNoError = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=' Please Select Follow Up Type ']")));
				if (regNoError.isDisplayed()) {
					throw new RuntimeException("Please Select Follow Up Type.");
				}
			} catch (TimeoutException e) {
			}
			if (follow_up_type.equalsIgnoreCase("Inspection")) {
				if (inspection_type.toUpperCase().contains("DEALER SITE")) {
					FollowUpDateTime(date_time);
					inspectionLocation(dealer_site_location);
					forName(for_name);
				} else {
					Thread.sleep(500);
					js.executeScript("window.scrollBy(0, 150);");
					//WebElement customerBtn =wait1.until(ExpectedConditions.elementToBeClickable((By.xpath("//input[@value=\"C\"]"))));
					WebElement customerBtn =driver.findElement((By.xpath("//span[text()=\"Customer Site\"]")));
					customerBtn.click();
					Thread.sleep(1000);
					FollowUpDateTime(date_time);
					Thread.sleep(500);
					forName(for_name);
					customerSite(address, state, landmark, city, pincode, google_loc_link);
				}
			} else {
				nextActionTsk(for_name, date_time);
			}
		} catch (TimeoutException e) {
			//System.out.println(e.getMessage());
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			//System.out.println(e.getMessage());
		}
	}
	public void forName(String for_name) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ng-select[@formcontrolname=\"executiveForm\"]")));
			WebElement forNameField = driver.findElement(By.xpath("//ng-select[@formcontrolname=\"executiveForm\"]"));
			Actions action = new Actions(driver);
			action.moveToElement(forNameField).click().sendKeys(Keys.ENTER).perform();
			try {
				WebElement clearAll = driver.findElement(By.xpath("//ng-select[@formcontrolname=\"executiveForm\"]//span[@title=\"Clear all\"]"));
				action.moveToElement(clearAll).click().perform();
				Thread.sleep(1500);
			} catch (org.openqa.selenium.NoSuchElementException e) {
			}
			action.moveToElement(forNameField).click().perform();
			Thread.sleep(200);
			action.sendKeys(for_name).perform();
			Thread.sleep(500);
			action.sendKeys(Keys.ENTER).perform();
			Thread.sleep(200);
			action.sendKeys(Keys.TAB).perform();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofMillis(200));
			try {
				WebElement regNoError = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=' Please Select Employee ']")));
				if (regNoError.isDisplayed()) {
					throw new RuntimeException("Please Select Employee.");
				}
			} catch (TimeoutException e) {
			}
		} catch (TimeoutException e) {
			throw new RuntimeException("For name field is not found");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
		}
	}
	public void FollowUpDateTime(String followUp_date_time) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement dateInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@formcontrolname=\"planDateTime\"]")));
			Actions action = new Actions(driver);
			action.moveToElement(dateInput).click().perform();
			Thread.sleep(500);
			String[] splitDate = followUp_date_time.split(" ");
			if (splitDate.length >= 2) {
				String date = splitDate[0];
				String time = splitDate[1];
				String[] timeParts = time.split(":");
				if (timeParts.length >= 2) {
					String hours = timeParts[0];
					String minutes = timeParts[1];
					String modifiedMinutes = minutes.charAt(0) + minutes;
					String modifiedTime = hours + ":" + modifiedMinutes;
					dateInput.sendKeys(date);
					action.sendKeys(Keys.ARROW_LEFT).perform();
					action.sendKeys(Keys.ARROW_LEFT).perform();
					action.sendKeys(Keys.ARROW_LEFT).perform();
					action.sendKeys(Keys.ARROW_LEFT).perform();
					action.sendKeys(Keys.ARROW_LEFT).perform();
					dateInput.sendKeys(date);
					action.sendKeys(Keys.TAB).perform();
					Thread.sleep(500);
					dateInput.sendKeys(modifiedTime);

				} else {
				}
			} else {
			}
			driver.findElement(By.xpath("//label[text()=\"Registration Year \"]")).click();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofMillis(200));
			try {
				WebElement errorMsg = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=\" Please Enter Date & Time \"]")));
				if (errorMsg.isDisplayed()) {
					throw new RuntimeException("Please enter valid Follow Up Date in New Enquiry.");
				}
			}catch(TimeoutException e) {
			}
		} catch (TimeoutException e) {
			throw new RuntimeException("Follow Up Date & Time Field not found");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		Thread.sleep(500);
	}
	public void nextActionTsk(String for_name, String date_time) throws InterruptedException {
		FollowUpDateTime(date_time);
		forName(for_name);
	}
	public void inspectionLocation(String dealer_site_location) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement locationField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ng-select[@formcontrolname=\"workShopLocation\"]")));
			Actions action = new Actions(driver);
			// WebElement modelField =
			// driver.findElement(By.xpath("//ng-select[@formcontrolname=\"newCarModel\"]"));
			// action.moveToElement(modelField).perform();
			action.moveToElement(locationField).click().sendKeys(Keys.ENTER).perform();
			WebElement clearAll = driver.findElement(
					By.xpath("//ng-select[@formcontrolname=\"workShopLocation\"]//span[@title=\"Clear all\"]"));
			action.moveToElement(clearAll).click().perform();
			action.sendKeys(dealer_site_location).sendKeys(Keys.ENTER).perform();
			action.sendKeys(Keys.TAB).perform();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofMillis(200));
			try {
				WebElement regNoError = wait1.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath("//span[text()=' Please Select Location ']")));
				if (regNoError.isDisplayed()) {
					throw new RuntimeException("Please Select Inspection Location.");
				}
			} catch (TimeoutException e) {
			}
		} catch (TimeoutException e) {
			throw new RuntimeException("Inspection Location Field is not found");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
		}
	}
	public void saveChangesButton(String follow_up_type,String reg_no,int i) throws InterruptedException {
		try {
			Thread.sleep(1000);
			WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(3));
			if(!(follow_up_type.isBlank() || follow_up_type.isEmpty())) {
				try {
					wait2.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()=\"Save Changes\"]")));
				} catch (TimeoutException e) {
					JavascriptExecutor js = (JavascriptExecutor) driver;
					js.executeScript("window.scrollBy(0,250)");
					Thread.sleep(1000);
					throw new RuntimeException("Save Button Not Visible.");
				}
				WebElement saveChangesBtn = driver.findElement(By.xpath("//button[text()=\"Save Changes\"]"));
				Actions action = new Actions(driver);
				action.moveToElement(saveChangesBtn).perform();
				Thread.sleep(500);
				action.click().perform();
			}
			else
			{
				try {
					wait2.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()=\"Inspect Now\"]")));
				} catch (TimeoutException e) {
					JavascriptExecutor js = (JavascriptExecutor) driver;
					js.executeScript("window.scrollBy(0,250)");
					Thread.sleep(1000);
					throw new RuntimeException("Save Button Not Visible.");
				}
				WebElement saveChangesBtn = driver.findElement(By.xpath("//button[text()=\"Inspect Now\"]"));
				Actions action = new Actions(driver);
				action.moveToElement(saveChangesBtn).perform();
				Thread.sleep(500);
				action.click().perform();
			}
			waitForLoadItemToDisappear();
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			try {
				String saveAlert = driver.findElement(By.xpath("//div[@id=\"swal2-html-container\"]")).getText();
				if(saveAlert.toLowerCase().contains("success")) {
					WebElement OKButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()=\"OK\"]")));
					Actions action1 = new Actions(driver);
					action1.moveToElement(OKButton).click().perform();
					ExtentReportListener.log("Enquiry saved Successfully for Regitration No: "+reg_no+" for Row No."+i, "PASS");
					System.out.println("Record Saved Successfully for Regitration No: "+reg_no+" for Row No."+i);			
				}else {
					ExtentReportListener.log(saveAlert+" Regitration No: "+reg_no+" for Row No."+i, "FAIL");
					System.out.println(saveAlert+" for Regitration No: "+reg_no+" for Row No."+i);
					WebElement OKButton = driver.findElement(By.xpath("//button[text()=\"OK\"]"));
					Actions action1 = new Actions(driver);
					action1.moveToElement(OKButton).click().perform();
				}
			} catch (TimeoutException e) {
				WebElement OKButton = driver.findElement(By.xpath("//button[text()=\"OK\"]"));
				Actions action1 = new Actions(driver);
				action1.moveToElement(OKButton).click().perform();
				throw new RuntimeException("Please Enter All Mandatory Fields.");
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
		}
	}
	public void customerSite(String address, String state, String landmark, String city, String pincode,String google_loc_link) throws InterruptedException {
		address(address);
		state(state);
		landmark(landmark);
		city(city);
		pincode(pincode);
		googleLocLink(google_loc_link);
	}
	public void address(String address) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement addressField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//textarea[@formcontrolname=\"addressValue\"]")));
			addressField.sendKeys(address);
			Actions action = new Actions(driver);
			action.sendKeys(Keys.TAB).perform();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(1));
			try {
				WebElement errorMsg = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=\" Please Enter Address \"]")));
				if (errorMsg.isDisplayed()) {
					throw new RuntimeException("Please Enter Address.");
				}
			} catch (TimeoutException e) {
			}
		} catch (TimeoutException e) {
			throw new RuntimeException("Address Field Not found");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
		}
	}
	public void state(String state) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement stateField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ng-select[@formcontrolname=\"defaultState\"]")));
			Actions action = new Actions(driver);
			action.moveToElement(stateField).click().sendKeys(Keys.ENTER).perform();
			WebElement clearAll = driver.findElement(By.xpath("//ng-select[@formcontrolname=\"defaultState\"]//span[@title=\"Clear all\"]"));
			action.moveToElement(clearAll).click().perform();
			action.sendKeys(state).sendKeys(Keys.ENTER).perform();
			action.sendKeys(Keys.TAB).perform();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(1));
			try {
				WebElement regNoError = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=' Please Select State ']")));
				if (regNoError.isDisplayed()) {
					throw new RuntimeException("Please Select State.");
				}
			} catch (TimeoutException e) {

			}
		} catch (TimeoutException e) {
			throw new RuntimeException("State Field is not found");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
		}
	}
	public void landmark(String landmark) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement landmarkField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname=\"landmarkValue\"]")));
			landmarkField.sendKeys(landmark);
		} catch (StaleElementReferenceException e) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement landmarkField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname=\"landmarkValue\"]")));
			landmarkField.sendKeys(landmark);
		} catch (TimeoutException e) {
			throw new RuntimeException("Landmark Field is not found");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			System.out.println("Error in landmark Field: " + e.getMessage());
		}
	}
	public void city(String city) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement cityField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ng-select[@formcontrolname='defaultCity']")));
			Actions action = new Actions(driver);
			action.moveToElement(cityField).click().sendKeys(Keys.ENTER).perform();
			try {
				WebElement clearAll = driver.findElement(By.xpath("//ng-select[@formcontrolname=\"defaultCity\"]//span[@title=\"Clear all\"]"));
				action.moveToElement(clearAll).click().perform();
			} catch (org.openqa.selenium.NoSuchElementException e) {
			}
			action.moveToElement(cityField).click().perform();
			action.sendKeys(city).perform();
			Thread.sleep(200);
			action.sendKeys(Keys.ENTER).perform();
			action.sendKeys(Keys.TAB).perform();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(1));
			try {
				WebElement regNoError = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=' Please Select City ']")));
				if (regNoError.isDisplayed()) {
					throw new RuntimeException("Please Select City.");
				}
			} catch (TimeoutException e) {

			}
		} catch (TimeoutException e) {
			throw new RuntimeException("city  Field is not found");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			System.out.println("city Field: " + e.getMessage());
		}
	}
	public void pincode(String pincode) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement pincodeField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname=\"pincodeValue\"]")));
			pincodeField.sendKeys(pincode);
			Actions action = new Actions(driver);
			action.sendKeys(Keys.TAB).perform();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(1));
			try {
				WebElement errorMsg = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=\" The PIN code must be 6 digits not start with zero \"]")));
				if (errorMsg.isDisplayed()) {
					throw new RuntimeException("The PIN code must be 6 digits not start with zero.");
				}
			} catch (TimeoutException e) {
			}
		} catch (TimeoutException e) {
			throw new RuntimeException("Google Location Field is not found");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
		}
	}
	public void googleLocLink(String google_loc_link) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement googleLocField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname=\"googleLinkValue\"]")));
			googleLocField.sendKeys(google_loc_link);
		} catch (StaleElementReferenceException e) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement googleLocField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname=\"googleLinkValue\"]")));
			googleLocField.sendKeys(google_loc_link);
		} catch (TimeoutException e) {
			throw new RuntimeException("Google Location Field is not found");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			System.out.println("Error in Google Location  Field: " + e.getMessage());
		}
	}
	public void signout() throws InterruptedException {
	    try {
	    	System.out.println("Signout Called");
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement profile = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id=\"navbarSupportedContent-4\"]")));
	        profile.click();
	        Thread.sleep(500);	   
	        WebElement signout = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//a[text()=' Sign Out'])[2]")));
	        signout.click();
	        System.out.println("Signed out successfully");	
	    } catch (Exception e) {
	        System.out.println("Error in sign out: " + e.getMessage());
	        e.printStackTrace();
	        throw e;
	    }
	}
}
