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

public class EnquiryList {

	WebDriver driver;
	public EnquiryList(WebDriver driver) {
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
	public void enquiryList() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			WebElement element =wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href=\"/procurement/enquiry-list\"]")));
			element.click();
		} catch (StaleElementReferenceException e) {
			WebElement element =wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href=\"/procurement/enquiry-list\"]")));
			element.click();
		} catch (TimeoutException | NoSuchElementException e) {
			throw new RuntimeException("Please click on Enquiry List.");
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
		} catch (TimeoutException e) {
			throw new RuntimeException("Registration No Filter is not found in Enquiry List.");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public void clickSearchedVehicle(String registration_no) throws InterruptedException {
		try {
			Thread.sleep(500);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			WebElement clickSearchedElement =wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//datatable-body-row[@tabindex=\"-1\"]")));
			clickSearchedElement.click();
			waitForLoadItemToDisappear();
		} catch (TimeoutException e) {
			throw new RuntimeException("Registration No is not found in Enquiry List.");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public void enquiryListDetail(String reg_no) throws InterruptedException {
		procurement();
		enquiryList();
		searchRegistrationNo(reg_no);	
		clickSearchedVehicle(reg_no);
	}
	
}
