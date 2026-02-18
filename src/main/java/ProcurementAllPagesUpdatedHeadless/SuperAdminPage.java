package ProcurementAllPagesUpdatedHeadless;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SuperAdminPage {

	WebDriver driver;

	public SuperAdminPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void admin() throws InterruptedException {
		waitForLoadItemToDisappear();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement admin = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Admin']")));
		admin.click();
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
	public void inspectionType() throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
			WebElement newEnquiry = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href=\"/admin/inspection-type\"]")));
			newEnquiry.click();
		} catch (StaleElementReferenceException e) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement newEnquiry = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href=\"/admin/inspection-type\"]")));
			newEnquiry.click();
		} catch (Exception e) {
			throw new RuntimeException("Please click on new enquiry");
		}
	}
	public void clickQuestion() throws InterruptedException {
		waitForLoadItemToDisappear();
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			Actions action = new Actions(driver);
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//i[@title=\"Question Link\"])[2]")));
			action.moveToElement(element).click().perform();
			Thread.sleep(500);
		} catch (TimeoutException e) {
		} catch (RuntimeException e) {
			throw e;
		}
		catch (Exception e) {
		}
	}
	public List<String> getAllInspectionQuestions() throws InterruptedException {
		Thread.sleep(1000);
	    List<String> columnTitles = new ArrayList<>();
	    List<WebElement> columnsAfterSeqNo = driver.findElements(By.xpath("//datatable-header-cell[.//span[normalize-space()='Seq No']]/following-sibling::datatable-header-cell"));

	    for (WebElement col : columnsAfterSeqNo) {
	        String title = col.getAttribute("title");
	        if (title == null || title.isEmpty()) {
	            title = col.getText();
	        }
//	        System.out.println("Question: "+title);
	        columnTitles.add(title.trim());
	    }
	    return columnTitles;
	}
	
	public void adminDetails() throws InterruptedException {
		admin();
		waitForLoadItemToDisappear();
		inspectionType();
		clickQuestion();
		getAllInspectionQuestions();
	}
	
}
	
