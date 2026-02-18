package ProcurementHeadless;

import java.time.Duration;
import java.util.NoSuchElementException;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class InspectionVideoList {
	WebDriver driver;
	public InspectionVideoList(WebDriver driver)	{
		this.driver=driver;
	}	
	public void procurement() throws InterruptedException	{
		try {
			Actions action = new Actions(driver);
			waitForLoadItemToDisappear();
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
			WebElement procurement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Procurement']")));
			action.moveToElement(procurement).click().perform();
		}
		catch(ElementClickInterceptedException e)	{			
		}
		catch(NoSuchElementException | TimeoutException e)	{
			throw new RuntimeException("Procurement is not clicked or disabled");
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
	    }
	}
	public void inspectionVideoList() throws InterruptedException	{
		try {
			waitForLoadItemToDisappear();
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement inspInfo = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href=\"/procurement/inspection-videos-list\"]")));
			inspInfo.click();
		}
		catch(StaleElementReferenceException e)	{
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement inspInfo = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href=\"/procurement/inspection-videos-list\"]")));
			inspInfo.click();
		}
		catch(NoSuchElementException e)	{
			throw new RuntimeException("Please click on  Inspection Information ");
		}catch(Exception e) {
			
		}
		waitForLoadItemToDisappear(); 
	}
	public void clickAllPending() throws InterruptedException	{
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement allPending = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()=\"All Pending\"]")));
			allPending.click();
		}
		catch(Exception e)	{
			System.out.println("Error in All Pending Button "+e.getMessage());
		}
		Thread.sleep(500);
	}
	public void registrationNo(String reg_no) throws InterruptedException	{
		try {
			 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			 WebElement enterRegNo = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder=\"Type to filter\"]")));
			 enterRegNo.sendKeys(reg_no);	
		}
		catch(NoSuchElementException | TimeoutException e)	{		
			throw new RuntimeException("Registration no: "+reg_no+" is not Found in Inspection Information");
		}
		catch(RuntimeException e) {
			throw e;
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public void proceedFinalSubmission(String reg_no) throws InterruptedException	{
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
			WebElement startIns =wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()=\"Proceed Final Submission\"]")));
			Actions actions = new Actions(driver);
			actions.moveToElement(startIns).click().perform();
			Thread.sleep(300);
			waitForLoadItemToDisappear();
			String saveText=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id=\"swal2-html-container\"]"))).getText();
			WebElement OkBtn =wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()=\"OK\"]")));
			OkBtn.click();
			if(saveText.toLowerCase().contains("success")) {
				ExtentReportListener.log("Video List Successfully For Registration No. "+reg_no,"PASS");
				System.out.println(saveText+" For Registration No. "+reg_no);
			}
			else {
				throw new RuntimeException(saveText+" for Registration No. "+reg_no+" For Inspection Video List.");
			}
		}
		catch(NoSuchElementException | TimeoutException e)	{		
			throw new RuntimeException(reg_no+" is not Found in Inspection Video List");
		}catch(RuntimeException e) {
			throw e;
		}
		catch(Exception e)	{
			System.out.println(e.getMessage());
		}
		Thread.sleep(500);
	}
	public void inspectionVideoListEntry(String reg_no) throws InterruptedException {
		procurement();
		inspectionVideoList();
		clickAllPending();
	    registrationNo(reg_no);
	    Thread.sleep(500);  
	    try {
	    	proceedFinalSubmission(reg_no);
	    }catch(RuntimeException e) {
	    	ExtentReportListener.log("Registration No "+reg_no+" not found in Inspection Video List","INFO");
	    	System.out.println("Registration No "+reg_no+" not found in Inspection Video List");
	    	return;
	    } 
	}
 	public void signout() throws InterruptedException	{
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			WebElement profile = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id=\"navbarSupportedContent-4\"]")));	
			profile.click();
			WebElement signout = driver.findElement(By.xpath("(//a[text()=' Sign Out'])[2]"));
			Actions action = new Actions(driver);
			action.moveToElement(signout).click().perform();		
		}catch(StaleElementReferenceException e) {
			WebElement profile = driver.findElement(By.xpath("//div[@id=\"navbarSupportedContent-4\"]"));
			profile.click();
			WebElement signout = driver.findElement(By.xpath("(//a[text()=' Sign Out'])[2]"));
			Actions action = new Actions(driver);
			action.moveToElement(signout).click().perform();	
		}
		catch(Exception e)	{
			System.out.println("Error in sign out: "+e.getMessage());
		}		
	}
}