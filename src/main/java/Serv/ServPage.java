package Serv;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ServPage {
	WebDriver driver;

	public ServPage(WebDriver driver) {
		this.driver = driver;
	}

	public void clickNewTicket() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
//			WebElement newTicketBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space(text())='New Ticket']")));
			WebElement newTicketBtn = wait.until(
					ExpectedConditions.elementToBeClickable(By.xpath("//span[normalize-space(text())='New Ticket']")));

			newTicketBtn.click();
		} catch (ElementClickInterceptedException e) {
			WebElement newTicketBtn = wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("//span[normalize-space(text())='New Ticket']")));
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", newTicketBtn);
		} catch (TimeoutException e) {
			throw new RuntimeException("New Ticket Button not found");
		} catch (RuntimeException e) {
			throw e;
		}

	}

	public void clickInternal() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			WebElement element = wait.until(
					ExpectedConditions.elementToBeClickable(By.xpath("//label[normalize-space(text())=\"Internal\"]")));
			element.click();
		} catch (ElementClickInterceptedException e) {
			WebElement element = wait.until(
					ExpectedConditions.elementToBeClickable(By.xpath("//label[normalize-space(text())=\"Internal\"]")));
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
		} catch (TimeoutException e) {
			throw new RuntimeException("internal radio Button not found");
		} catch (RuntimeException e) {
			throw e;
		}

	}

	public void enterSubject(String subject) {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		JavascriptExecutor js = (JavascriptExecutor) driver;

		try {
			// 1️⃣ Click the LABEL (this avoids interception)
			WebElement label = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
					"//mat-label[normalize-space()='Name of the interface - Problem that is occuring in brief']")));
			label.click();

			// 2️⃣ Now get the INPUT (already focused)
			WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(By
					.xpath("//mat-label[normalize-space()='Name of the interface - Problem that is occuring in brief']"
							+ "/ancestor::mat-form-field//input")));

			// 3️⃣ Type without clicking input
			input.clear();
			input.sendKeys(subject);

			// 4️⃣ Trigger blur for validation
			input.sendKeys(Keys.TAB);

		} catch (TimeoutException e) {
			throw new RuntimeException("❌ Subject input field not found");
		}
	}

	public void enterRemarks(String remarks) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		try {
//			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//trix-editor[@trix-id=\"1\"]")));
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//trix-editor[@role=\"textbox\"])[1]")));
		
			try {
				element.sendKeys(remarks);
			} catch (ElementClickInterceptedException e) {
				element.sendKeys(remarks);
			}

		} catch (TimeoutException e) {
			throw new RuntimeException("Remarks Field is not found");

		} catch (RuntimeException e) {
			throw e;
		}
	}

	public void selectProductCategory(String product_category) {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofMillis(200));

		JavascriptExecutor js = (JavascriptExecutor) driver;

		try {
			WebElement dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By
					.xpath("//mat-label[normalize-space()='Product Category']/ancestor::mat-form-field//mat-select")));

			js.executeScript("arguments[0].click();", dropdown);

			wait.until(ExpectedConditions.presenceOfElementLocated(By.className("cdk-overlay-pane")));
			try {
//				WebElement option = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
//						"//mat-option//span[normalize-space()='" + product_category + "']/ancestor::mat-option")));
				WebElement option = wait.until(ExpectedConditions
						.elementToBeClickable(By.xpath("//mat-option//span[" + "translate(normalize-space(.),"
								+ "'ABCDEFGHIJKLMNOPQRSTUVWXYZ'," + "'abcdefghijklmnopqrstuvwxyz')" + " = "
								+ "translate(normalize-space('" + product_category + "')," + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ',"
								+ "'abcdefghijklmnopqrstuvwxyz')" + "]/ancestor::mat-option")));
				js.executeScript("arguments[0].click();", option);
			} catch (TimeoutException e) {
				throw new RuntimeException("Product Category " + product_category + " is not available");
			}
			Actions action = new Actions(driver);
			action.sendKeys(Keys.TAB).perform();
			try {
				wait1.until(ExpectedConditions.visibilityOfElementLocated(
						By.xpath("//mat-error[normalize-space(text())=\"Product Category Is Required\"]")));
				throw new RuntimeException("Product Category Is Required");
			} catch (TimeoutException e) {
			}
		} catch (TimeoutException e) {
			throw new RuntimeException("❌ Product Category '" + product_category + "' not Found");
		}
	}

//	public void selectProduct(String productCategory) {
//
//		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
//		JavascriptExecutor js = (JavascriptExecutor) driver;
//
//		try {
//			WebElement input = wait.until(ExpectedConditions.elementToBeClickable(
//					By.xpath("//mat-label[normalize-space()='Product']/ancestor::mat-form-field//input")));
//
//			js.executeScript("arguments[0].focus();", input);
////			input.clear();
//			input.sendKeys(productCategory);
//			Actions action = new Actions(driver);
//			action.sendKeys(Keys.TAB).perform();
//			try {
//				WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofMillis(200));
//				wait1.until(ExpectedConditions.visibilityOfElementLocated(
//						By.xpath("//mat-error[normalize-space(text())=\"Product Is Required\"]")));
//				throw new RuntimeException("Product Category Is Required");
//			} catch (TimeoutException e) {
//			}
//		} catch (TimeoutException e) {
//			throw new RuntimeException("❌ Product Category Field not found.");
//		}catch(RuntimeException e) {
//			throw e;
//		}
//	}

	public void selectProduct(String product) {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		JavascriptExecutor js = (JavascriptExecutor) driver;

		try {
			WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//mat-label[normalize-space()='Product']/ancestor::mat-form-field//input")));

			js.executeScript("arguments[0].click();", dropdown);

			wait.until(ExpectedConditions.presenceOfElementLocated(By.className("cdk-overlay-pane")));
			try {
//				WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
//						By.xpath("//mat-option//span[normalize-space()='" + product + "']/ancestor::mat-option")));
				WebElement option = wait.until(ExpectedConditions
						.elementToBeClickable(By.xpath("//mat-option//span[" + "translate(normalize-space(.),"
								+ "'ABCDEFGHIJKLMNOPQRSTUVWXYZ'," + "'abcdefghijklmnopqrstuvwxyz')" + " = "
								+ "translate(normalize-space('" + product + "')," + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ',"
								+ "'abcdefghijklmnopqrstuvwxyz')" + "]/ancestor::mat-option")));
				js.executeScript("arguments[0].click();", option);
			} catch (TimeoutException e) {
				throw new RuntimeException("Product  " + product + " is not available");
			}
			Actions action = new Actions(driver);
			action.sendKeys(Keys.TAB).perform();
			try {
				WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofMillis(200));
				wait1.until(ExpectedConditions.visibilityOfElementLocated(
						By.xpath("//mat-error[normalize-space(text())=\"Product Category Is Required\"]")));
				throw new RuntimeException("Product Category Is Required");
			} catch (TimeoutException e) {
			}
		} catch (TimeoutException e) {
			throw new RuntimeException("❌ Product Category Field not found.");
		} catch (RuntimeException e) {
			throw e;
		}
	}

	public void selectTicketCategory(String ticket_category) {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		JavascriptExecutor js = (JavascriptExecutor) driver;

		try {
			// 1️⃣ Click mat-select using JS (MOST IMPORTANT FIX)
			WebElement dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//mat-label[normalize-space()='Ticket Category']/ancestor::mat-form-field//mat-select")));

			js.executeScript("arguments[0].click();", dropdown);

			// 2️⃣ Wait for overlay panel to appear
			wait.until(ExpectedConditions.presenceOfElementLocated(By.className("cdk-overlay-pane")));

			try {
				// 3️⃣ Click the option (BTT)
//				WebElement option = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
//						"//mat-option//span[normalize-space()='" + ticket_category + "']/ancestor::mat-option")));
				WebElement option = wait.until(ExpectedConditions
						.elementToBeClickable(By.xpath("//mat-option//span[" + "translate(normalize-space(.),"
								+ "'ABCDEFGHIJKLMNOPQRSTUVWXYZ'," + "'abcdefghijklmnopqrstuvwxyz')" + " = "
								+ "translate(normalize-space('" + ticket_category + "')," + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ',"
								+ "'abcdefghijklmnopqrstuvwxyz')" + "]/ancestor::mat-option")));
				js.executeScript("arguments[0].click();", option);
			} catch (TimeoutException e) {
				throw new RuntimeException("Ticket Category " + ticket_category + " is not available");
			}
			Actions action = new Actions(driver);
			action.sendKeys(Keys.TAB).perform();
			try {
				WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofMillis(200));
				wait1.until(ExpectedConditions.visibilityOfElementLocated(
						By.xpath("//mat-error[normalize-space(text())=\"Ticket Category Is Required\"]")));
				throw new RuntimeException("Ticket Category Is Required");
			} catch (TimeoutException e) {
			}
		} catch (TimeoutException e) {
			throw new RuntimeException("❌ Ticket Category '" + ticket_category + "' not selectable");
		} catch (RuntimeException e) {
			throw e;
		}
	}

	public void selectTicketSubCategory(String ticket_sub_category) {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		JavascriptExecutor js = (JavascriptExecutor) driver;

		try {
			// 1️⃣ Click mat-select using JS (MOST IMPORTANT FIX)
			WebElement dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
					"(//mat-label[normalize-space()='Ticket Sub Category']/ancestor::mat-form-field//mat-select)[1]")));

			js.executeScript("arguments[0].click();", dropdown);

			// 2️⃣ Wait for overlay panel to appear
			wait.until(ExpectedConditions.presenceOfElementLocated(By.className("cdk-overlay-pane")));

			// 3️⃣ Click the option (BTT)
			try {
//				WebElement option = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
//						"//mat-option//span[normalize-space()='" + ticket_sub_category + "']/ancestor::mat-option")));
				WebElement option = wait.until(ExpectedConditions
						.elementToBeClickable(By.xpath("//mat-option//span[" + "translate(normalize-space(.),"
								+ "'ABCDEFGHIJKLMNOPQRSTUVWXYZ'," + "'abcdefghijklmnopqrstuvwxyz')" + " = "
								+ "translate(normalize-space('" + ticket_sub_category + "')," + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ',"
								+ "'abcdefghijklmnopqrstuvwxyz')" + "]/ancestor::mat-option")));
				js.executeScript("arguments[0].click();", option);
			} catch (TimeoutException e) {
				throw new RuntimeException("Ticket Sub Category  " + ticket_sub_category + " is not available");
			}
			Actions action = new Actions(driver);
			action.sendKeys(Keys.TAB).perform();
			try {
				WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofMillis(200));
				wait1.until(ExpectedConditions.visibilityOfElementLocated(
						By.xpath("//mat-error[normalize-space(text())=\"Ticket Sub Category Is Required\"]")));
				throw new RuntimeException("Ticket Sub Category Is Required");
			} catch (TimeoutException e) {
			}
		} catch (TimeoutException e) {
			throw new RuntimeException("❌ Ticket Sub Category '" + ticket_sub_category + "' not selectable");
		} catch (RuntimeException e) {
			throw e;
		}
	}

	public void selectAssignTo(String assign_to) {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		JavascriptExecutor js = (JavascriptExecutor) driver;

		try {
			// 1️⃣ Click mat-select using JS (MOST IMPORTANT FIX)
			WebElement dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//mat-label[normalize-space()='Assign To']/ancestor::mat-form-field//mat-select")));

			js.executeScript("arguments[0].click();", dropdown);

			// 2️⃣ Wait for overlay panel to appear
			wait.until(ExpectedConditions.presenceOfElementLocated(By.className("cdk-overlay-pane")));

			// 3️⃣ Click the option (BTT)
			try {
//				WebElement option = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//mat-option//span[normalize-space()='" + assign_to + "']/ancestor::mat-option")));
				WebElement option = wait.until(ExpectedConditions
						.elementToBeClickable(By.xpath("//mat-option//span[" + "translate(normalize-space(.),"
								+ "'ABCDEFGHIJKLMNOPQRSTUVWXYZ'," + "'abcdefghijklmnopqrstuvwxyz')" + " = "
								+ "translate(normalize-space('" + assign_to + "')," + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ',"
								+ "'abcdefghijklmnopqrstuvwxyz')" + "]/ancestor::mat-option")));
				js.executeScript("arguments[0].click();", option);
			} catch (TimeoutException e) {
				throw new RuntimeException("Assign to " + assign_to + " is not available");
			}
			Actions action = new Actions(driver);
			action.sendKeys(Keys.TAB).perform();
			try {
				WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofMillis(200));
				wait1.until(ExpectedConditions.visibilityOfElementLocated(
						By.xpath("//mat-error[normalize-space(text())=\"Assign To Is Required\"]")));
				throw new RuntimeException("Assign To Is Required");
			} catch (TimeoutException e) {
			}
		} catch (TimeoutException e) {
			throw new RuntimeException("❌ Assign To '" + assign_to + "' not selectable");
		} catch (RuntimeException e) {
			throw e;
		}
	}

	public void selectSprintMonth(String sprint_month) {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		JavascriptExecutor js = (JavascriptExecutor) driver;

		try {
			// 1️⃣ Click Sprint Month dropdown using mat-label
			WebElement dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By
					.xpath("//mat-label[normalize-space()='Sprint Month']" + "/ancestor::mat-form-field//mat-select")));

			js.executeScript("arguments[0].click();", dropdown);

			// 2️⃣ Wait for overlay panel to open
			wait.until(ExpectedConditions.presenceOfElementLocated(By.className("cdk-overlay-pane")));

			// 3️⃣ Select Sprint Month option
			try {
//	            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
//	                    By.xpath("//mat-option//span[normalize-space()='" + sprint_month + "']/ancestor::mat-option")));
				WebElement option = wait.until(ExpectedConditions
						.elementToBeClickable(By.xpath("//mat-option//span[" + "translate(normalize-space(.),"
								+ "'ABCDEFGHIJKLMNOPQRSTUVWXYZ'," + "'abcdefghijklmnopqrstuvwxyz')" + " = "
								+ "translate(normalize-space('" + sprint_month + "')," + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ',"
								+ "'abcdefghijklmnopqrstuvwxyz')" + "]/ancestor::mat-option")));

				js.executeScript("arguments[0].click();", option);

				js.executeScript("arguments[0].click();", option);

			} catch (TimeoutException e) {
				throw new RuntimeException("Sprint Month '" + sprint_month + "' is not available");
			}

			// 4️⃣ Move focus out (important for validation trigger)
			new Actions(driver).sendKeys(Keys.TAB).perform();

			// 5️⃣ Optional validation check
			try {
				WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofMillis(300));
				wait1.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath("//mat-error[contains(text(),'Sprint Month')]")));
				throw new RuntimeException("Sprint Month is required");
			} catch (TimeoutException e) {
				// No error → selection successful
			}

		} catch (TimeoutException e) {
			throw new RuntimeException("❌ Sprint Month '" + sprint_month + "' not selectable");
		} catch (RuntimeException e) {
			throw e;
		}
	}
	public void clickEditPencilIcon() throws InterruptedException {

	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    JavascriptExecutor js = (JavascriptExecutor) driver;

	    try {
	        WebElement pencilIcon = wait.until(
	                ExpectedConditions.elementToBeClickable(
	                        By.xpath("//i[@class=\"fa fa-pencil text-10 cursor text-danger\"]")));

	        // Scroll into view (safe)
	        js.executeScript("arguments[0].scrollIntoView({block:'center'});", pencilIcon);
	        Thread.sleep(1000);
	        // Click using JS to avoid interception
	        js.executeScript("arguments[0].click();", pencilIcon);

	    } catch (TimeoutException e) {
	        throw new RuntimeException("❌ Edit (pencil) icon not found or not clickable");
	    }
	}

	public void selectPriority(String priority) throws InterruptedException {
		clickEditPencilIcon();
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
	    JavascriptExecutor js = (JavascriptExecutor) driver;

	    try {
	        // 1️⃣ Click Priority dropdown using mat-label
	        WebElement dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(
	                By.xpath("//mat-label[normalize-space()='Priority']" +
	                         "/ancestor::mat-form-field//mat-select")));

	        js.executeScript("arguments[0].click();", dropdown);

	        // 2️⃣ Wait for overlay panel
	        wait.until(ExpectedConditions.presenceOfElementLocated(
	                By.className("cdk-overlay-pane")));

	        // 3️⃣ Select Priority (case & space insensitive)
	        try {
	            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
	                    By.xpath("//mat-option//span[" +
	                            "translate(normalize-space(.)," +
	                            "'ABCDEFGHIJKLMNOPQRSTUVWXYZ'," +
	                            "'abcdefghijklmnopqrstuvwxyz')" +
	                            " = " +
	                            "translate(normalize-space('" + priority + "')," +
	                            "'ABCDEFGHIJKLMNOPQRSTUVWXYZ'," +
	                            "'abcdefghijklmnopqrstuvwxyz')" +
	                            "]/ancestor::mat-option")));

	            js.executeScript("arguments[0].click();", option);

	        } catch (TimeoutException e) {
	            throw new RuntimeException("Priority '" + priority + "' is not available");
	        }

	        // 4️⃣ Move focus out to trigger validation
	        new Actions(driver).sendKeys(Keys.TAB).perform();

	    } catch (TimeoutException e) {
	        throw new RuntimeException("❌ Priority dropdown not selectable");
	    } catch (RuntimeException e) {
	        throw e;
	    }
	}
	public void clickSubmitButton() {

	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    JavascriptExecutor js = (JavascriptExecutor) driver;

	    try {
	        WebElement submitBtn = wait.until(
	                ExpectedConditions.elementToBeClickable(
	                        By.xpath("//button[@type='submit']")));

	        // Scroll into view (safe for small screens)
	        js.executeScript("arguments[0].scrollIntoView({block:'center'});", submitBtn);

	        // Click using JS to avoid interception
	        js.executeScript("arguments[0].click();", submitBtn);

	    } catch (TimeoutException e) {
	        throw new RuntimeException("❌ Submit button not found or not clickable");
	    }
	    catch (RuntimeException e) {
	        throw e;
	    }
	}
	public void enterInputById(String inputId, String value) {

	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    JavascriptExecutor js = (JavascriptExecutor) driver;

	    try {
	        WebElement input = wait.until(
	                ExpectedConditions.visibilityOfElementLocated(
	                        By.id(inputId)));

	        // Scroll into view
	        js.executeScript("arguments[0].scrollIntoView({block:'center'});", input);

	        // Focus input using JS (avoids label interception)
	        js.executeScript("arguments[0].focus();", input);

	        // Clear safely
	        input.sendKeys(Keys.CONTROL + "a");
	        input.sendKeys(Keys.DELETE);

	        // Enter value
	        input.sendKeys(value);

	        // Trigger Angular validation
	        input.sendKeys(Keys.TAB);

	    } catch (TimeoutException e) {
	        throw new RuntimeException("❌ Input field with id '" + inputId + "' not found");
	    }
	    catch (RuntimeException e) {
	        throw e;
	    }
	}

}
