package ProcurementAllPagesUpdatedHeadless;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Login_Page {
    WebDriver driver;
    public Login_Page(WebDriver driver)  {
    	this.driver = driver;
    	PageFactory.initElements(driver, this);
    }    
    public void username(String username)   {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    	try {
	    	WebElement usernameField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname=\"UserName\"]")));
	    	usernameField.sendKeys(username);
    	}catch(org.openqa.selenium.ElementClickInterceptedException e) {
    		WebElement usernameField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname=\"UserName\"]")));
	    	usernameField.sendKeys(username);
    	}
    }
    public void password(String password)   {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    	try {
	    	WebElement passwordField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname=\"Pwd\"]")));
	    	passwordField.sendKeys(password);
    	}catch(org.openqa.selenium.ElementClickInterceptedException e) {
    		WebElement passwordField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname=\"Pwd\"]")));
	    	passwordField.sendKeys(password);
    	}
    }
    public void passwordChange() {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
    	try {
	    	WebElement pwdAlert = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(text(),\"Your password has been changed after\")]")));
	    	String text =pwdAlert.getText();
    		ExtentReportListener.log(text, "Warning");
    		WebElement ignorePasswordChangeMsg = driver.findElement(By.xpath("//button[text()=\"Cancel\"]"));
			ignorePasswordChangeMsg.click();
    	}catch(TimeoutException e) {
    	}
    }
    public void loginButton()   {
    	try {
	    	WebElement loginBtn = driver.findElement(By.xpath("//button[@class=\"btn btn-block btn-primary\"]"));
	    	loginBtn.click();
    	}catch(org.openqa.selenium.ElementClickInterceptedException e) {
    		WebElement loginBtn = driver.findElement(By.xpath("//button[@class=\"btn btn-block btn-primary\"]"));
	    	loginBtn.click();
    	}
    }
    public void login(String username,String password)    {
    	username(username);
    	password(password);
    	loginButton();
    	passwordChange();
    }
    
}
