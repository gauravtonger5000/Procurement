package ProcurementAllPagesUpdatedHeadless;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PriceDiscovery {

	WebDriver driver;

	public PriceDiscovery(WebDriver driver) {
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
//	        NewEnquiry.captureAndSaveScreenshot(driver, "Procurement_Before_Click_Price Discovery");

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
//	       NewEnquiry.captureAndSaveScreenshot(driver, "Procurement_After_Click");

//	        System.out.println("✅ Successfully clicked on Procurement in PD");

	    } catch (Exception e) {
	        throw new RuntimeException("❌ Failed to click on Procurement: " + e.getMessage(), e);
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
	public void priceDiscovery() throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement newEnquiry = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href=\"/procurement/inspection-form-details\"]")));
			newEnquiry.click();
		} catch (StaleElementReferenceException e) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement newEnquiry = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href=\"/procurement/inspection-form-details\"]")));
			newEnquiry.click();
		} catch (TimeoutException | NoSuchElementException e) {
			throw new RuntimeException("Please click on Price Discovery");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {

		}
	}
	public void searchRegistrationNo(String registration_no) throws InterruptedException {

		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement regNoField =wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder=\"Type to filter\"]")));
			Actions actions = new Actions(driver);
			actions.moveToElement(regNoField).perform();
			regNoField.sendKeys(registration_no);
			waitForLoadItemToDisappear();
		} catch (TimeoutException e) {
			throw new RuntimeException("Search Text Field is not found in Price Discovery");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public void clickSearchRegistrationNO(String registration_no) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement clickSearchedElement =wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//datatable-body-row[@tabindex=\"-1\"]")));
			clickSearchedElement.click();
			waitForLoadItemToDisappear();
		} catch (TimeoutException e) {
			throw new RuntimeException("Registration No is not found in Price Discovery");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public void pdDetails(String reg_no) throws InterruptedException {
		procurement();
		priceDiscovery();
		searchRegistrationNo(reg_no);
		clickSearchRegistrationNO(reg_no);
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
		// Thread.sleep(500);
	}
	public void clickAccept() throws InterruptedException {
		
		try {
			WebElement acceptBtn = driver.findElement(By.xpath("//label[text()=\"Accept\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(acceptBtn).perform();
			Thread.sleep(500);
			actions.moveToElement(acceptBtn).click().perform();
		} catch (StaleElementReferenceException e) {
			WebElement acceptBtn = driver.findElement(By.xpath("//label[text()=\"Accept\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(acceptBtn).perform();
			Thread.sleep(500);
			actions.moveToElement(acceptBtn).click().perform();
		} catch (org.openqa.selenium.NoSuchElementException e) {
			throw new RuntimeException("Accept  Not Found");
		} catch (RuntimeException e) {
			throw e;
		}
	}
	public void clickInspectAgain() throws InterruptedException {
		try {
			WebElement acceptBtn = driver.findElement(By.xpath("//label[text()=\"Inspect Again\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(acceptBtn).perform();
			Thread.sleep(500);
			actions.moveToElement(acceptBtn).click().perform();
		} catch (StaleElementReferenceException e) {
			WebElement acceptBtn = driver.findElement(By.xpath("//label[text()=\"Inspect Again\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(acceptBtn).perform();
			Thread.sleep(500);
			actions.moveToElement(acceptBtn).click().perform();
		} catch (org.openqa.selenium.NoSuchElementException e) {
			throw new RuntimeException("Inspect Again Not Found");
		} catch (RuntimeException e) {
			throw e;
		}
	}
	public void clickReject() throws InterruptedException {
		try {
			WebElement acceptBtn = driver.findElement(By.xpath("//label[text()=\"Reject\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(acceptBtn).perform();
			Thread.sleep(500);
			actions.moveToElement(acceptBtn).click().perform();
		} catch (StaleElementReferenceException e) {
			WebElement acceptBtn = driver.findElement(By.xpath("//label[text()=\"Accept\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(acceptBtn).perform();
			Thread.sleep(500);
			actions.moveToElement(acceptBtn).click().perform();
		} catch (org.openqa.selenium.NoSuchElementException e) {
			throw new RuntimeException("Reject Not Found");
		} catch (RuntimeException e) {
			throw e;
		}
	}
	public void clickPreAuction() throws InterruptedException {
		try {
			WebElement acceptBtn = driver.findElement(By.xpath("//label[text()=\"Pre-Auction\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(acceptBtn).perform();
			Thread.sleep(500);
			actions.moveToElement(acceptBtn).click().perform();
		} catch (StaleElementReferenceException e) {
			WebElement acceptBtn = driver.findElement(By.xpath("//label[text()=\"Pre-Auction\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(acceptBtn).perform();
			Thread.sleep(500);
			actions.moveToElement(acceptBtn).click().perform();
		} catch (org.openqa.selenium.NoSuchElementException e) {
			throw new RuntimeException("Pre-Auction Not Found");
		} catch (RuntimeException e) {
			throw e;
		}
	}

	public void enterMinPrice(String min_price) {
		try {
			WebElement minPrice = driver.findElement(By.xpath("//input[@formcontrolname=\"MinimumPrice\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(minPrice).perform();
			minPrice.clear();
			minPrice.sendKeys(min_price);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public void enterMaxPrice(String max_price) {
		try {
			WebElement maxPrice = driver.findElement(By.xpath("//input[@formcontrolname=\"MaximumPrice\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(maxPrice).perform();
			maxPrice.clear();
			maxPrice.sendKeys(max_price);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public void enterRefurbishmentPrice(String refurbishment_price) {
		try {
			WebElement refurbishmentPrice = driver.findElement(By.xpath("//input[@formcontrolname=\"ReimbursementCost\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(refurbishmentPrice).perform();
			refurbishmentPrice.clear();
			refurbishmentPrice.sendKeys(refurbishment_price);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public void rejectDetailsRemarks(String reject_remarks) throws InterruptedException {
		try {
			WebElement field = driver.findElement(By.xpath("//textarea[@formcontrolname=\"Remarks\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(field).perform();
			Thread.sleep(500);
			field.sendKeys(reject_remarks);
		} catch (StaleElementReferenceException e) {
			WebElement field = driver.findElement(By.xpath("//textarea[@formcontrolname=\"Remarks\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(field).perform();
			Thread.sleep(500);
			field.sendKeys(reject_remarks);
		}
	}
	public void inspectAgainDetailsRemarks(String inspect_again_remarks) throws InterruptedException {
		try {
			WebElement field = driver.findElement(By.xpath("//textarea[@formcontrolname=\"Remarks\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(field).perform();
			Thread.sleep(500);
			field.sendKeys(inspect_again_remarks);
		} catch (StaleElementReferenceException e) {
			WebElement field = driver.findElement(By.xpath("//textarea[@formcontrolname=\"Remarks\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(field).perform();
			Thread.sleep(500);
			field.sendKeys(inspect_again_remarks);
		}
	}
	public void expectedSellingPrice(String expected_selling_price) throws InterruptedException {
		try {
			WebElement field = driver.findElement(By.xpath("//input[@formcontrolname=\"ExptSellingPrice\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(field).perform();
			Thread.sleep(500);
			field.sendKeys(expected_selling_price);
		} catch (StaleElementReferenceException e) {
			WebElement field = driver.findElement(By.xpath("//input[@formcontrolname=\"ExptSellingPrice\"]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(field).perform();
			Thread.sleep(500);
			field.sendKeys(expected_selling_price);
		}
	}
	public void inspectionStatusEntry(String inspection_status, String min_price, String max_price,
			String refurbishment_price, String inspect_again_remarks, String reject_remarks,
			String expected_selling_price) throws InterruptedException {
		waitForLoadItemToDisappear();
		if (inspection_status.equalsIgnoreCase("Accept")) {
			clickAccept();
			enterMinPrice(min_price);
			enterMaxPrice(max_price);
			enterRefurbishmentPrice(refurbishment_price);
		} else if (inspection_status.equalsIgnoreCase("Inspect Again")) {
			clickInspectAgain();
			inspectAgainDetailsRemarks(inspect_again_remarks);
		} else if (inspection_status.equalsIgnoreCase("Reject")) {
			clickReject();
			rejectDetailsRemarks(reject_remarks);
		} else if (inspection_status.equalsIgnoreCase("Pre-Auction")) {
			clickPreAuction();
			expectedSellingPrice(expected_selling_price);
		}
	}
	public void saveChangesButton(String reg_no) throws InterruptedException {
		try {
//			Thread.sleep(1000);
			WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));
			try {
				wait2.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),\"Save Change\")]")));
			} catch (TimeoutException e) {
				throw new RuntimeException("Save Button Not Visible.");
			}
			WebElement saveChangesBtn = driver.findElement(By.xpath("//button[contains(text(),\"Save Change\")]"));
			Actions action = new Actions(driver);
			action.moveToElement(saveChangesBtn).perform();
			Thread.sleep(500);
			action.click().perform();
			waitForLoadItemToDisappear();
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			try {
				try {
					String saveBtnText=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id=\"swal2-html-container\"]"))).getText();
					System.out.println("PD Message: "+saveBtnText);
					Thread.sleep(300);
					if(saveBtnText.toLowerCase().contains("successfully") || saveBtnText.toLowerCase().contains("ready") ){
						ExtentReportListener.log(saveBtnText+" for Registration No. " + reg_no, "PASS");
						System.out.println(saveBtnText+" for Registration No. " + reg_no);
					}
					else  {
						ExtentReportListener.log(saveBtnText+" Registration No. " + reg_no, "FAIL");
						System.out.println(saveBtnText+" Registration No. " + reg_no);
					}
				}catch(TimeoutException e) {
					throw new RuntimeException("No Alert after Saving the form");
				}
				WebElement OKButton = driver.findElement(By.xpath("//button[text()=\"OK\"]"));
				Actions action1 = new Actions(driver);
				action1.moveToElement(OKButton).click().perform();		
//				Thread.sleep(1000);
			} catch (Exception e) {
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
		}
	}
	public void signout() throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			WebElement profile = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id=\"navbarSupportedContent-4\"]")));
			profile.click();
			WebElement signout = driver.findElement(By.xpath("(//a[text()=' Sign Out'])[2]"));
			Actions action = new Actions(driver);
			action.moveToElement(signout).click().perform();
		} catch (StaleElementReferenceException e) {
			WebElement profile = driver.findElement(By.xpath("//div[@id=\"navbarSupportedContent-4\"]"));
			profile.click();
			WebElement signout = driver.findElement(By.xpath("(//a[text()=' Sign Out'])[2]"));
			Actions action = new Actions(driver);
			action.moveToElement(signout).click().perform();
		} catch (Exception e) {
			System.out.println("Error in sign out: " + e.getMessage());
		}
	}
	public void priceDiscoveryEntry(String registration_no, String inspection_status, String min_price,
			String max_price, String refurbishment_price, String inspect_again_remarks, String reject_remarks,
			String expected_selling_price) throws InterruptedException {
		// procurement();
		// priceDiscovery();
		// searchRegistrationNo(registration_no);
		clickNextButton();
		inspectionStatusEntry(inspection_status, min_price, max_price, refurbishment_price, inspect_again_remarks,reject_remarks, expected_selling_price);
		saveChangesButton(registration_no);
//		Thread.sleep(1000);
		// signout();
	}
	public void checkTextContent(String method_name, String method_value) throws InterruptedException {
		try {
		
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement element = driver.findElement(By.xpath("//label[contains(text(), '" + method_name + "')]/following-sibling::label"));
			String text = element.getText().trim();
			if (method_name.contains("Image")) {
				return; // Ignore execution
			}
			if (method_name.contains("Rate")) {
				int filledStarCount = driver.findElements(By.cssSelector(".star-rating .fa-star.filled")).size();
				int expectedStars = Integer.parseInt(method_value.trim());
				// rate
				if (filledStarCount == expectedStars) {
					//ExtentReportListener.log(method_name + " in Enquiry View", "PASS");
					//System.out.println("PASSED: " + method_name + " in Enquiry View");
				} else {
					ExtentReportListener.log(method_name + " in Enquiry View", "FAIL");
					System.out.println("Failed " + method_name + " in Enquiry View");
				}
			} else {
				if (text.toLowerCase().contains(method_value.toLowerCase())) {
					//System.out.println("PASSED: " + method_name + " in Enquiry View");
					//ExtentReportListener.log(method_name + " in Enquiry View", "PASS");
				} else {
					System.out.println("FAILED: " + method_name + " in Enquiry View");
					ExtentReportListener.log(method_name + " in Enquiry View", "FAIL");
				}
			}
		} catch (TimeoutException e) {
		} catch (org.openqa.selenium.NoSuchElementException | NoSuchElementException e) {
		}
	}
}
