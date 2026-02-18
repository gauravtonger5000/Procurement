package ProcurementHeadless;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FollowUpNew {

	WebDriver driver;

	public FollowUpNew(WebDriver driver) {
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

	        System.out.println("✅ Successfully clicked on Procurement");

	    } catch (Exception e) {
	        throw new RuntimeException("❌ Failed to click on Procurement: " + e.getMessage(), e);
	    }
	}
	public void waitForLoadItemToDisappear() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("ngx-spinner")));
			WebDriverWait waitInfinite = new WebDriverWait(driver, Duration.ofSeconds(50));
			while (true) {
				try {
					waitInfinite.until(ExpectedConditions.invisibilityOfElementLocated(By.tagName("ngx-spinner")));
					break; // Exit the loop if the element is no longer visible
				} catch (TimeoutException e) {
				}
			}
		} catch (org.openqa.selenium.NoSuchElementException e) {
		} catch (TimeoutException e) {
		}
	}
	public void followUp() throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement followUp = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href=\"/procurement/follow-up-details\"]")));
			followUp.click();
		} catch (Exception e) {
			throw new RuntimeException("Please click on  Follow Up");
		}
		waitForLoadItemToDisappear();
	}
	public void clickAllPending() throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement allPending = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()=\"All Pending\"]")));
			allPending.click();
		} catch (StaleElementReferenceException e) {
			WebElement allPending = driver.findElement(By.xpath("//button[text()=\"All Pending\"]"));
			allPending.click();
		} catch (Exception e) {
			System.out.println("Error in All Pending Button " + e.getMessage());
		}
		Thread.sleep(1000);
	}
	public void registrationNo(String registration_no) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			WebElement enterRegNo = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder=\"Type to filter\"]")));
			enterRegNo.sendKeys(registration_no);
			Thread.sleep(500);
			WebElement clickSearchedElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//datatable-body-row[@tabindex=\"-1\"]")));
			clickSearchedElement.click();
			waitForLoadItemToDisappear();
		} catch (NoSuchElementException | TimeoutException e) {
			throw new RuntimeException("Registration no: " + registration_no + " is not available");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
		}
	}
	public void followUpEntry(String registration_no) throws InterruptedException {
		followUp();
		clickAllPending();
		registrationNo(registration_no);
		//checkGreen();
	}
	public void checkGreen() throws InterruptedException {
		Thread.sleep(2000);
		List<WebElement> steps = driver.findElements(By.xpath("//span[@class='circle ng-star-inserted']"));
		for (WebElement step : steps) {
			WebElement titleElement = step.findElement(By.xpath("./ancestor::li[1]//div[contains(@class,'aw-wizard-step-title')]"));
			String title = titleElement.getText();
			//System.out.println(title);
		}
		int size = steps.size();
		//System.out.println(size);
		if (size == 3) {
			checkTemporaryBooking();
		}
	}
	public void checkTemporaryBooking() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		WebElement typeField = driver.findElement(By.xpath("//ng-select[@formcontrolname=\"statusListName\"]"));
		Actions action = new Actions(driver);
		action.moveToElement(typeField).perform();
		Thread.sleep(200);
		typeField.click();
		try {
			WebElement tempElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='ng-dropdown-panel-items scroll-host']//span[normalize-space(text())='Temporary Booking']")));
			System.out.println("Temporary Booking is available");
		} catch (TimeoutException e) {
			throw new RuntimeException("Temporary Booking option is not found in Follow Up");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
		}
	}
	public void actionType(String action_type) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			WebElement typeField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ng-select[@formcontrolname=\"actionFollowUp\"]")));
			Actions action = new Actions(driver);
			WebElement clearAll = driver.findElement(By.xpath("//ng-select[@formcontrolname=\"actionFollowUp\"]//span[@title=\"Clear all\"]"));
			action.moveToElement(clearAll).click().perform();
			Thread.sleep(500);
			action.moveToElement(typeField).sendKeys(action_type).sendKeys(Keys.ENTER).perform();
			action.sendKeys(Keys.TAB).perform();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(1));
			try {
				WebElement regNoError = wait1.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath("//span[text()=' Please Select Type ']")));
				if (regNoError.isDisplayed()) {
					throw new RuntimeException("Please Select Action Type.");
				}
			} catch (TimeoutException e) {
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			System.out.println("Error in action type :" + e.getMessage());
		}
	}
	public void Date(String date) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement dateInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname=\"inspectionDate\"]")));
			dateInput.sendKeys(date);
			Actions action = new Actions(driver);
			action.sendKeys(Keys.TAB).perform();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(1));
			try {
				WebElement regNoError = wait1.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath("//span[text()=' Please Select date & Time ']")));
				if (regNoError.isDisplayed()) {
					throw new RuntimeException("Please Select date & Time.");
				}
			} catch (TimeoutException e) {
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			System.out.println("Error in Date  :" + e.getMessage());
		}
	}
	public void actionTypeandDate(String action_type, String date) throws InterruptedException {
		actionType(action_type);
		// Date(date);
	}
	public void status(String status, String remarks, String std_remarks, String price_offered,
			String customer_expected_price, String exp_close_date, String next_action_type,
			String inspection_type, String location, String next_action_date, String instruction, String for_name,
			String ins_for_name, String address, String state, String landmark, String city, String pincode,
			String google_loc_link) throws InterruptedException {
		try {
			status(status);
			waitForLoadItemToDisappear();
			if ("LOST".equalsIgnoreCase(status) || "Deferred".equalsIgnoreCase(status)) {
				standardRemarks(std_remarks);
				remarks(remarks);
			} else if ("TEMPORARY BOOKING".equalsIgnoreCase(status)) {
//				System.out.println("temporary booking");
//				standardRemarks(std_remarks);
////				finalPurchasePrice(final_purchase_price);
//				remarks(remarks);
////				tempDetails(source, final_purchase_price, new_car_variant, exchange_bonus_value, new_car_support, reference_no);
			} else {
				standardRemarks(std_remarks);
				ExpCloseDate(exp_close_date);
				remarks(remarks);
				priceOffered(price_offered);
				customerExpectedPrice(customer_expected_price);
				nextActionType(next_action_type, inspection_type, location, next_action_date, instruction, for_name,
						ins_for_name, address, state, landmark, city, pincode, google_loc_link);
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			System.out.println("Error in status  :" + e.getMessage());
		}
	}
	public void status(String status) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			WebElement typeField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ng-select[@formcontrolname=\"statusListName\"]")));
			Actions action = new Actions(driver);
			WebElement clearAll = driver.findElement(By.xpath("//ng-select[@formcontrolname=\"statusListName\"]//span[@title=\"Clear all\"]"));
			action.moveToElement(clearAll).click().perform();
			Thread.sleep(500);
			action.moveToElement(typeField).sendKeys(status).perform();
			Thread.sleep(500);
			action.sendKeys(Keys.ENTER).perform();
			action.sendKeys(Keys.TAB).perform();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(1));
			try {
				WebElement regNoError = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=' Please Select Status ']")));
				if (regNoError.isDisplayed()) {
					throw new RuntimeException("Please Select Status.");
				}
			}catch(TimeoutException e) {
				
			}
		}catch (TimeoutException e) {
			throw new RuntimeException("Status Field Not Found");
		}catch(RuntimeException e) {
			throw e;
		}
	}
	public void standardRemarks(String std_remarks) throws InterruptedException {
		try {
			Thread.sleep(200);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement typeField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ng-select[@formcontrolname=\"standardRemarks\"]")));
			Actions action = new Actions(driver);
			action.moveToElement(typeField).click().perform();
			action.sendKeys(std_remarks).sendKeys(Keys.ENTER).perform();
			action.sendKeys(Keys.TAB).perform();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(1));
			try {
				WebElement regNoError = wait1.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath("//span[text()=' Please Select Standard Remarks ']")));
				if (regNoError.isDisplayed()) {
					throw new RuntimeException("Please Select Standard Remarks.");
				}
			} catch (TimeoutException e) {
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			System.out.println("Error in standard remarks  :" + e.getMessage());
		}
	}
	public void ExpCloseDate(String exp_close_date) throws InterruptedException {
		try {
			Actions action = new Actions(driver);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement dateInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname=\"expectedCloseDate\"]")));
			action.moveToElement(dateInput).perform();
			dateInput.click();
			action.sendKeys(Keys.LEFT).sendKeys(Keys.LEFT).perform();
			dateInput.sendKeys(Keys.chord(Keys.CONTROL, "a"));
			dateInput.sendKeys(Keys.BACK_SPACE);
			dateInput.sendKeys(exp_close_date);

			WebElement remarksField = driver.findElement(By.xpath("//textarea[@formcontrolname=\"otherRemarks\"]"));
			action.moveToElement(remarksField).click().perform();

			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(1));
			try {
				WebElement regNoError = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=' Please Enter Date ']")));
				if (regNoError.isDisplayed()) {
					throw new RuntimeException("Please Enter Date.");
				}
			} catch (TimeoutException e) {
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public void remarks(String remarks) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement remarksField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//textarea[@formcontrolname=\"otherRemarks\"]")));
			remarksField.sendKeys(remarks);
			Actions action = new Actions(driver);
			action.sendKeys(Keys.TAB).perform();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(1));
			try {
				WebElement regNoError = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=' Please Enter Remarks ']")));
				if (regNoError.isDisplayed()) {
					throw new RuntimeException("Please Enter Remarks.");
				}
			} catch (TimeoutException e) {
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public void priceOffered(String price_offered) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
			WebElement priceOfferedField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname=\"offrePrice\"]")));
			priceOfferedField.clear();
			Thread.sleep(500);
			priceOfferedField.sendKeys(price_offered);
		} catch (TimeoutException e) {
			// System.out.println("Price offered field is not available");
		} catch (Exception e) {
			System.out.println("Error in  priceOffered  :" + e.getMessage());
		}
	}
	public void customerExpectedPrice(String customer_expected_price) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
			WebElement customerExpectedpriceField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname=\"customerExpectedPrice\"]")));
			customerExpectedpriceField.clear();
			Thread.sleep(500);
			customerExpectedpriceField.sendKeys(customer_expected_price);
		} catch (TimeoutException e) {
			// System.out.println("Customer expected price field is not available");
		} catch (Exception e) {
			System.out.println("Error in  Customer expected price  :" + e.getMessage());
		}
	}
	
	public void nextActionType(String next_action_type, String inspection_type, String location,
			String next_action_date, String instruction, String for_name, String ins_for_name, String address,
			String state, String landmark, String city, String pincode, String google_loc_link)
			throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement typeField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ng-select[@formcontrolname=\"nextFollowUp\"]")));
			Thread.sleep(500);
			WebElement clearAll = driver.findElement(By.xpath("//ng-select[@formcontrolname=\"nextFollowUp\"]//span[@title=\"Clear all\"]"));
			Actions action = new Actions(driver);
			action.moveToElement(clearAll).click().perform();
			Thread.sleep(500);
			action.moveToElement(typeField).sendKeys(next_action_type).sendKeys(Keys.ENTER).perform();
			action.sendKeys(Keys.TAB).perform();
			try {
				WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(1));
				WebElement regNoError = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=' Please Select Type ']")));
				if (regNoError.isDisplayed()) {
					throw new RuntimeException("Please Select Type.");
				}
			} catch (TimeoutException e) {
			}
			if (next_action_type.equalsIgnoreCase("Inspection")) {
				if (inspection_type.toUpperCase().contains("DEALER SITE".toUpperCase())) {
					waitForLoadItemToDisappear();
					nextActionDate(next_action_date);
					instruction(instruction);
					inspectionLocation(location);
					Thread.sleep(1000);
					inspectionforName(ins_for_name);
				} else {
					Thread.sleep(2000);
					WebElement customerBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()=\"Customer Site\"]")));
					action.moveToElement(customerBtn).click().perform();
					Thread.sleep(1000);
					forName(for_name);
					nextActionDate(next_action_date);
					instruction(instruction);
					customerSite(address, state, landmark, city, pincode, google_loc_link);
				}
			} else {
				forName(for_name);
				nextActionDate(next_action_date);
				instruction(instruction);
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			System.out.println("Error in next action type :" + e.getMessage());
		}
	}
	public void forName(String for_name) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement forNameField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ng-select[@formcontrolname=\"forValue\"]")));
			Actions action = new Actions(driver);
			try {
				action.moveToElement(forNameField).click().sendKeys(Keys.ENTER).perform();
				WebElement clearAll = driver.findElement(By.xpath("//ng-select[@formcontrolname=\"forValue\"]//span[@title=\"Clear all\"]"));
				action.moveToElement(clearAll).perform();
				action.click().perform();
			} catch (org.openqa.selenium.NoSuchElementException e) {
			}
			action.moveToElement(forNameField).sendKeys(for_name).sendKeys(Keys.ENTER).perform();
			action.sendKeys(Keys.TAB).perform();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(1));
			try {
				WebElement regNoError = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=' Please Select a Employee ']")));
				if (regNoError.isDisplayed()) {
					throw new RuntimeException("Please Select a Employee.");
				}
			} catch (TimeoutException e) {
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public void inspectionforName(String ins_for_name) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement forNameField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ng-select[@formcontrolname=\"executiveForm\"]")));
			WebElement clearAll = driver.findElement(By.xpath("//ng-select[@formcontrolname=\"executiveForm\"]//span[@title=\"Clear all\"]"));
			Actions action = new Actions(driver);
			action.moveToElement(clearAll).click().perform();
			action.moveToElement(forNameField).sendKeys(ins_for_name).sendKeys(Keys.ENTER).perform();
			action.sendKeys(Keys.TAB).perform();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(1));
			try {
				WebElement regNoError = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=' Please Select Employee ']")));
				if (regNoError.isDisplayed()) {
					throw new RuntimeException("Please Select Inspection Employee.");
				}
			} catch (TimeoutException e) {
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public void nextActionDate(String next_action_date) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement dateInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname=\"nextActionDate\"]")));
			Actions action = new Actions(driver);
			dateInput.clear();

			String[] splitDate = next_action_date.split(" ");
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
					action.sendKeys(Keys.TAB).perform();
					dateInput.sendKeys(modifiedTime);
					WebElement ee = driver.findElement(By.xpath("//textarea[@formcontrolname=\"nextInstructionValue\"]"));
					action.moveToElement(ee).click().perform();
					WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(1));
					try {
						WebElement regNoError = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=' Please Enter Date & Time ']")));
						if (regNoError.isDisplayed()) {
							throw new RuntimeException("Please Enter Valid Next Action date & Time.");
						}
					} catch (TimeoutException e) {
					}
				}
			} else {
				// System.out.println("Invalid time format: " + time);
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			System.out.println("Error in Date  :" + e.getMessage());
		}
	}
	public void instruction(String instruction) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement instructionField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//textarea[@formcontrolname=\"nextInstructionValue\"]")));
			instructionField.sendKeys(instruction);
			Actions action = new Actions(driver);
			action.sendKeys(Keys.TAB).perform();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(1));
			try {
				WebElement regNoError = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=' Please Enter Instruction ']")));
				if (regNoError.isDisplayed()) {
					throw new RuntimeException("Please Select Instruction.");
				}
			} catch (TimeoutException e) {
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public void nextAction(String next_action_type, String for_name, String next_action_date, String instruction) throws InterruptedException {
		// nextActionType(next_action_type);
		// forName(for_name);
		nextActionDate(next_action_date);
		instruction(instruction);
	}

	public void inspectionLocation(String dealer_site_location) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement locationField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ng-select[@formcontrolname=\"workShopLocation\"]")));
			Actions action = new Actions(driver);
			action.moveToElement(locationField).click().sendKeys(Keys.ENTER).perform();
			WebElement clearAll = driver.findElement(By.xpath("//ng-select[@formcontrolname=\"workShopLocation\"]//span[@title=\"Clear all\"]"));
			action.moveToElement(clearAll).click().perform();
			action.sendKeys(dealer_site_location).sendKeys(Keys.ENTER).perform();
			action.sendKeys(Keys.TAB).perform();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(1));
			WebElement regNoError = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=' Please Select Location ']")));
			if (regNoError.isDisplayed()) {
				throw new RuntimeException("Please Select Inspection Location.");
			}
		} catch (TimeoutException e) {
			// System.out.println("Error in Source :"+e.getMessage());
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
		}
	}
	public void saveChangesButtonTemp(String reg_no, int i) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement saveChangesBtn = driver.findElement(By.xpath("(//button[@type=\"submit\"])[2]"));
			Actions action = new Actions(driver);
			action.moveToElement(saveChangesBtn).perform();
			Thread.sleep(500);
			action.click().perform();
			try {
				WebElement saveAlert = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id=\"swal2-html-container\"]")));
				String alertText=saveAlert.getText();
				if(alertText.toLowerCase().contains("success")) {
					ExtentReportListener.log("Follow Up Record saved Successfully For Registration No. " + reg_no + " and Row No. " + i,"PASS");
					System.out.println("Follow Up Record Saved Successfully For Registration No. " + reg_no);
				}
				else {
					ExtentReportListener.log(alertText+" for Registration No. "+reg_no,"FAIL");
					System.out.println(alertText+" for Registration No. "+reg_no);
				}
				WebElement OKButton = driver.findElement(By.xpath("//button[text()=\"OK\"]"));
				Actions action1 = new Actions(driver);
				action1.moveToElement(OKButton).click().perform();
				Thread.sleep(500);
			} catch (TimeoutException e) {
				throw new RuntimeException("Save alert not found");
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
		}
	}
	public void saveChangesButton(String reg_no, int i) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement saveChangesBtn = driver.findElement(By.xpath("//button[@type=\"submit\"]"));
			
			Actions action = new Actions(driver);
			action.moveToElement(saveChangesBtn).perform();
			Thread.sleep(500);
			action.click().perform();
			try {
				WebElement saveAlert = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id=\"swal2-html-container\"]")));
				String alertText=saveAlert.getText();
				if(alertText.toLowerCase().contains("success")) {
					ExtentReportListener.log("Follow Up Record saved Successfully For Registration No. " + reg_no + " and Row No. " + i,"PASS");
					System.out.println("Follow Up Record Saved Successfully For Registration No. " + reg_no);
				}
				else {
					ExtentReportListener.log(alertText+" for Registration No. "+reg_no,"FAIL");
					System.out.println(alertText+" for Registration No. "+reg_no);
				}
				WebElement OKButton = driver.findElement(By.xpath("//button[text()=\"OK\"]"));
				Actions action1 = new Actions(driver);
				action1.moveToElement(OKButton).click().perform();
			} catch (TimeoutException e) {
				throw new RuntimeException("Save alert not found");
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
		}
	}
	public void customerSite(String address, String state, String landmark, String city, String pincode,String google_loc_link) throws InterruptedException {
		try {
			address(address);
			state(state);
			landmark(landmark);
			city(city);
			pincode(pincode);
			googleLocLink(google_loc_link);
		} catch (TimeoutException e) {
			System.out.println("Error in selcting radio button: " + e.getMessage());
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
		}
	}
	public void address(String address) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement addressField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//textarea[@formcontrolname=\"addressValue\"]")));
			addressField.sendKeys(address);
			Actions action = new Actions(driver);
			WebElement landmark = driver.findElement(By.xpath("//input[@formcontrolname=\"landmarkValue\"]"));
			action.moveToElement(landmark).click().perform();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(1));
			WebElement errorMsg = wait1.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//span[text()=\" Please Enter Address \"]")));

			if (errorMsg.isDisplayed()) {
				throw new RuntimeException("Please Enter Address.");
			}
		} catch (TimeoutException e) {
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
			WebElement landmark = driver.findElement(By.xpath("//input[@formcontrolname=\"landmarkValue\"]"));
			action.moveToElement(landmark).click().perform();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(1));

			WebElement regNoError = wait1.until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=' Please Select State ']")));
			if (regNoError.isDisplayed()) {
				throw new RuntimeException("Please Select State.");
			}
		} catch (TimeoutException e) {
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
			action.sendKeys(city).sendKeys(Keys.ENTER).perform();
			WebElement landmark = driver.findElement(By.xpath("//input[@formcontrolname=\"landmarkValue\"]"));
			action.moveToElement(landmark).click().perform();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(1));
			WebElement regNoError = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=' Please Select City ']")));
			if (regNoError.isDisplayed()) {
				throw new RuntimeException("Please Select City.");
			}
		} catch (TimeoutException e) {
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
		}
	}
	public void pincode(String pincode) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement pincodeField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname=\"pincodeValue\"]")));
			pincodeField.sendKeys(pincode);
			Actions action = new Actions(driver);
			WebElement landmark = driver.findElement(By.xpath("//input[@formcontrolname=\"landmarkValue\"]"));
			action.moveToElement(landmark).click().perform();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(1));
			WebElement errorMsg = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=\" The PIN code must be 6 digits not start with zero \"]")));
			if (errorMsg.isDisplayed()) {
				throw new RuntimeException("The PIN code must be 6 digits not start with zero.");
			}
		} catch (TimeoutException e) {
			// .out.println("Error in customerName :"+e.getMessage());
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
		} catch (Exception e) {
			System.out.println("Error in Google Location  Field: " + e.getMessage());
		}
	}
	public void signout() throws InterruptedException {
		Thread.sleep(1000);
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			WebElement profile = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id=\"navbarSupportedContent-4\"]")));
			profile.click();
			WebElement signout = driver.findElement(By.xpath("(//a[text()=' Sign Out'])[2]"));
			Actions action = new Actions(driver);
			action.moveToElement(signout).click().perform();
			// signout.click();
		} catch (Exception e) {
			System.out.println("Error in sign out: " + e.getMessage());
		}
	}
	public void submitBtn() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		try {
			WebElement submitBtn=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type=\"submit\"]")));
			JavascriptExecutor js = (JavascriptExecutor)driver;
			js.executeScript("arguments[0].click();", submitBtn);
			//submitBtn.click();
			waitForLoadItemToDisappear();
		} catch (TimeoutException e) {
			throw new RuntimeException("Save Button Not Visible.");
		}
	}
	
	public void tempDetails(String source,String final_purchase_price,String new_car_rm,String new_car_model,String new_car_variant,String exchange_bonus_value,String new_car_support,String reference_no,String service_advisor) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		submitBtn();
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h4[text()=\"Edit Enquiry\"]")));
			enterSource(source);
			//referenceNumber(reference_no);
			finalPurchasePrice(final_purchase_price);
			if("New Car Exchange".equalsIgnoreCase(source)) {
				newCarRM(new_car_rm);
				newCarModel(new_car_model);
				newCarVariant(new_car_variant);
				exchangeBonusValue(exchange_bonus_value);
				newCarSupport(new_car_support);
			}
			else if("Workshop".equalsIgnoreCase(source)){
				serviceAdvisor(service_advisor);
			}
		}catch(TimeoutException e) {
			throw new RuntimeException("Edit section not found");
		}catch(RuntimeException e) {
			throw e;
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
			action.sendKeys(new_car_rm).sendKeys(Keys.ENTER).perform();
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
	public void enterSource(String source) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement sourceField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ng-select[@formcontrolname=\"sourceName\"]")));
			Actions action = new Actions(driver);
			try {
				WebElement clearAll = driver.findElement(By.xpath("//ng-select[@formcontrolname=\"sourceName\"]//span[@title=\"Clear all\"]"));
				action.moveToElement(clearAll).click().perform();
			}catch(org.openqa.selenium.NoSuchElementException e) {
				action.moveToElement(sourceField).click().perform();

			}
			action.moveToElement(sourceField).sendKeys(source).perform();
			action.sendKeys(Keys.ENTER).perform();
			action.sendKeys(Keys.TAB).perform();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(1));
			try {
				WebElement regNoError = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=' Please Select Source Name ']")));
				if (regNoError.isDisplayed()) {
					throw new RuntimeException("Please Select Source Name.");
				}
			} catch (TimeoutException e) {
			}
		} catch (TimeoutException e) {
			throw new RuntimeException("Source Folder is not found");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
		}
	}
	
	public void finalPurchasePrice(String final_purchase_price) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
			WebElement final_purchase_priceField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname=\"FinalPurchasePrice\"]")));
			final_purchase_priceField.sendKeys(final_purchase_price);
		} catch (StaleElementReferenceException e) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement final_purchase_priceField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname=\"FinalPurchasePrice\"]")));
			final_purchase_priceField.sendKeys(final_purchase_price);
		} catch (Exception e) {
			System.out.println("Error in  finalPurchasePrice  :" + e.getMessage());
		}
		Thread.sleep(500);

	}
	
	public void newCarVariant(String new_car_variant) throws InterruptedException {
		try {
			Actions action = new Actions(driver);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ng-select[@formcontrolname=\"carVar\"]")));
			action.moveToElement(element).perform();
			Thread.sleep(200);
			element.click();
			Thread.sleep(500);
			action.moveToElement(element).sendKeys(new_car_variant).perform();
			action.sendKeys(Keys.ENTER).perform();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(1));
			try {
				WebElement regNoError = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space(text())=\"Please Enter Car Variant name\"]")));
				if (regNoError.isDisplayed()) {
					throw new RuntimeException("Please Enter New Car Variant.");
				}
			} catch (TimeoutException ee) {
			}
		} catch (StaleElementReferenceException e) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ng-select[@formcontrolname=\"carVar\"]")));
			element.sendKeys(new_car_variant);
		} catch (Exception e) {
			System.out.println("Error in  New Car Variant  :" + e.getMessage());
		}
		Thread.sleep(500);
	}
	public void exchangeBonusValue(String exchange_bonus_value) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder=\"Exchange Bonus Value\"]")));
			element.sendKeys(exchange_bonus_value);
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(1));
			try {
				WebElement regNoError = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space(text())=\"Exchange Bonus Value is required\"]")));
				if (regNoError.isDisplayed()) {
					throw new RuntimeException("Please Enter Exchange Bonus Value.");
				}
			} catch (TimeoutException ee) {
			}
		} catch (StaleElementReferenceException e) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder=\"Exchange Bonus Value\"]")));
			element.sendKeys(exchange_bonus_value);
		} catch (Exception e) {
			System.out.println("Error in  Exchange Bonus Value  :" + e.getMessage());
		}
	}
	public void newCarSupport(String new_car_support) throws InterruptedException {
		Actions action = new Actions(driver);
		action.sendKeys(Keys.TAB).perform();
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder=\"New Car Support\"]")));
			element.click();
			element.sendKeys(new_car_support);
		} catch (StaleElementReferenceException e) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder=\"New Car Support\"]")));
			element.sendKeys(new_car_support);
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(1));
			try {
				WebElement regNoError = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space(text())=\"New Car Adjustment Value is required\"]")));
				if (regNoError.isDisplayed()) {
					throw new RuntimeException("Please Enter New Car Support.");
				}
			} catch (TimeoutException ee) {
			}
		} catch (Exception e) {
			System.out.println("Error in  New Car Support Bonus Value  :" + e.getMessage());
		}
	}
	public void serviceAdvisor(String service_advisor) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement serviceAdvisorField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ng-select[@formcontrolname=\"carRM\"]")));
			Actions action = new Actions(driver);
			try {
				WebElement clearAll = driver.findElement(By.xpath("//ng-select[@formcontrolname=\"carRM\"]//span[@title=\"Clear all\"]"));
				action.moveToElement(clearAll).click().perform();
			}catch(org.openqa.selenium.NoSuchElementException e) {
				action.moveToElement(serviceAdvisorField).click().perform();

			}
			serviceAdvisorField.click();
			action.moveToElement(serviceAdvisorField).sendKeys(service_advisor).perform();
			action.sendKeys(Keys.ENTER).perform();
			action.sendKeys(Keys.TAB).perform();
			action.sendKeys(Keys.TAB).perform();

			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(1));
			try {
				WebElement regNoError = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=' Please Select Service Advisor ']")));
				if (regNoError.isDisplayed()) {
					throw new RuntimeException("Please Select Service Advisor.");
				}
			} catch (TimeoutException e) {
			}
		} catch (TimeoutException e) {
			System.out.println("Service Advisor Field Not found.");
//			throw new RuntimeException("Service Advisor Field is not found");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
		}
	}
}