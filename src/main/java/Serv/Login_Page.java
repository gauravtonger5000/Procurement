package Serv;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Login_Page {
	
    WebDriver driver;
    // we create the constructor of this class and pass the driver reference 
    public Login_Page(WebDriver driver)
    {
    	this.driver = driver;
    	PageFactory.initElements(driver, this);
    }    
 
    public void username(String username)
    {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    	try {
	    	WebElement usernameField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id=\"email\"]")));
	    	usernameField.sendKeys(username);
    	}catch(org.openqa.selenium.ElementClickInterceptedException e) {
    		WebElement usernameField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id=\"email\"]")));
	    	usernameField.sendKeys(username);
    	}catch(TimeoutException e) {
    		throw new RuntimeException("Username Field not found on login page");
    	}catch(RuntimeException e) {
    		throw e;
    	}
    }
    public void nextBtn() throws InterruptedException
    {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    	try {
	    	WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()=\"NEXT\"]")));
	    	btn.click();
    	}catch(org.openqa.selenium.ElementClickInterceptedException e) {
    		WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()=\"NEXT\"]")));
	    	btn.click();
    	}catch(TimeoutException e) {
    		throw new RuntimeException("Next button not found on login page");
    	}catch(RuntimeException e) {
    		throw e;
    	}
    }
    public void password(String password)
    {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    	try {
	    	WebElement passwordField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id=\"password\"]")));
	    	passwordField.sendKeys(password);
    	}catch(org.openqa.selenium.ElementClickInterceptedException e) {
    		WebElement passwordField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id=\"password\"]")));
	    	passwordField.sendKeys(password);
    	}catch(TimeoutException e) {
    		throw new RuntimeException("Password Field not found on login page");
    	}catch(RuntimeException e) {
    		throw e;
    	}
    }
  
    public void loginButton()
    {
    	try {
	    	WebElement loginBtn = driver.findElement(By.xpath("//button[@id=\"Login\"]"));
	    	loginBtn.click();
    	}catch(org.openqa.selenium.ElementClickInterceptedException e) {
    		WebElement loginBtn = driver.findElement(By.xpath("//button[@id=\"Login\"]"));
	    	loginBtn.click();
    	}
    }
  
    public void login(String username,String password) throws InterruptedException
    {
    	username(username);
    	nextBtn();
    	password(password);
    	loginButton();
    }
}
