package ProcurementHeadless;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.List; 
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
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

public class ProcurementInspectionNew {

	WebDriver driver;	
	public ProcurementInspectionNew(WebDriver driver)	{
		this.driver=driver;
	}	
	int totalQuestionFilled=0;
	
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
	public void waitForLoadItemToDisappear() {
	    try {
	        // Wait up to 2 seconds for the load item to appear
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
	public void waitForVahanLoading() {
	    By loader = By.xpath("//div[contains(@class,'loading-text')]");

	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

	    try {

	        // STEP 1 → Wait until loader exists in DOM
	        wait.until(driver -> {
	            List<WebElement> list = driver.findElements(loader);
	            return list.size() > 0;
	        });
	        System.out.println("Vahan loader found in DOM.");

	        // STEP 2 → Wait until loader becomes visible (if it ever does)
	        try {
	            wait.until(ExpectedConditions.visibilityOfElementLocated(loader));
	            System.out.println("Vahan loader is visible now.");
	        } catch (Exception ignored) {
	            System.out.println("Loader did not become visible, continuing...");
	        }

	        // STEP 3 → Wait until loader disappears completely
	        wait.until(ExpectedConditions.invisibilityOfElementLocated(loader));
	        System.out.println("Vahan loader disappeared.");

	        waitForClickFetchedData();

	    } catch (Exception e) {
	        System.out.println("Loader did not appear — moving ahead.");
	    }
	}



	public void waitForClickFetchedData() throws InterruptedException {
		    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));

		    try {
		        // Wait for the popup to appear
		        WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(
		                By.xpath("//div[contains(@class,'swal2-popup')]")
		        ));

		        System.out.println("SweetAlert2 popup appeared.");

		        // Wait for the YES button
		        WebElement yesBtn = wait.until(ExpectedConditions.elementToBeClickable(
		                By.xpath("//button[contains(@class,'swal2-confirm')]")
		        ));

		        yesBtn.click();
		        System.out.println("Clicked YES button.");

		        // Wait until popup disappears
		        wait.until(ExpectedConditions.invisibilityOfElementLocated(
		                By.xpath("//div[contains(@class,'swal2-popup')]")
		        ));

		    } catch (TimeoutException e) {
		        System.out.println("Vahan popup not found. Continuing...");
		    }

	}
	public void inspectionInformation() throws InterruptedException	{
		try {
			waitForLoadItemToDisappear();
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
			WebElement inspInfo = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href=\"/procurement/inspector-information\"]")));
			inspInfo.click();
		}
		catch(StaleElementReferenceException e)	{
			WebElement inspInfo = driver.findElement(By.xpath("//a[@href=\"/procurement/inspector-information\"]"));
			inspInfo.click();
		}
		catch(NoSuchElementException e)	{
			throw new RuntimeException("Please click on  Inspection Information ");
		}catch(Exception e) {
			System.out.println("Errror in Inspection Info "+e.getMessage());
		}
		waitForLoadItemToDisappear();
	}
	public void clickAllPending() {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
	    for (int i = 1; i <= 4; i++) {
	        try {
	            WebElement btn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[normalize-space()='All Pending']")));
	            Thread.sleep(500);
	            btn.click();
	            return;
	        } catch (Exception e) {
	           // System.out.println("⚠️ Retry " + i + " due to: " + e.getClass().getSimpleName());
	            try { Thread.sleep(1000);
	            } catch (InterruptedException ignored) {
	            	
	            }
	        }
	    }
	}

	public void registrationNo(String registration_no) throws InterruptedException	{
		try {
			 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			 WebElement enterRegNo = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder=\"Type to filter\"]")));
			 enterRegNo.sendKeys(registration_no);			
		}
		catch(NoSuchElementException | TimeoutException e)	{		
			throw new RuntimeException("Registration no Search Field not Found in Inspection Information");
		}
		catch(RuntimeException e) {
			throw e;
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public void startInspection(String registration_no) throws InterruptedException	{
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			WebElement startIns = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()=\"Start Inspection\"]")));
			Actions actions = new Actions(driver);
			actions.moveToElement(startIns).click().perform();
		}
		catch(NoSuchElementException | TimeoutException e)	{		
			throw new RuntimeException(registration_no+" is not Found");
		}
		catch(Exception e)	{
			System.out.println(e.getMessage());
		}
	}
	public void enterRegistrationNo(String reg_no) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
		WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(1));
		WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofMillis(300));
		try {
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder=\"Registration Number\"]")));
			element.clear();
			element.sendKeys(reg_no);
			Actions action = new Actions(driver);
			action.sendKeys(Keys.TAB).perform();
			
//			Uncomment all later when needed of inspection to change the registration no
//			try {
//				WebElement regNoError = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[normalize-space(text())=\"Please provide a valid Registration Number like DL8BH4516 or 22BH1234AA.\"]")));
//				if (regNoError.isDisplayed()) {
//					throw new RuntimeException("Please provide a valid Registration Number like DL8BH4516 or 22BH1234AA.");
//				}
//			} catch (TimeoutException e) {
//			}
//			try {
//				WebElement alert = wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id=\"swal2-html-container\"]")));
//				String alertText= alert.getText();
//				if(alertText.contains("different from the previous one")) {
//					//WebElement Btn = driver.findElement(By.xpath("(//button[text()=\"No\"])[2]"));
//					WebElement Btn = driver.findElement(By.xpath("//button[text()=\"Yes\"]"));
//					Btn.click();
//					try {
//						WebElement alert2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id=\"swal2-html-container\"]")));
//						String alerttext2= alert2.getText();
//						if(alerttext2.contains("There is already")){
//							WebElement OKBtn = driver.findElement(By.xpath("//button[text()=\"OK\"]"));
//							OKBtn.click();
//						}
//					}catch(TimeoutException e) {
//					}
//				}else if(alertText.contains("There is already")){
//					WebElement Btn = driver.findElement(By.xpath("//button[text()=\"OK\"]"));
//					Btn.click();
//				}
//			}catch(TimeoutException e) {
//			}
		}catch(TimeoutException e) {
			//throw new RuntimeException("Registration No. field is not found on Inspection Type UI");
		}catch(RuntimeException e) {
			throw e;
		}catch(Exception e) {
		}
	}
	
	public List<String> checkQuestions() {
	    List<String> questionTexts = new ArrayList<>();
	    List<WebElement> questions = driver.findElements(By.xpath("//label[normalize-space(text())='Registration Number']/ancestor::div[@class='row']//label[normalize-space(text())!='Registration Number']"));

	    for (WebElement label : questions) {
	    	System.out.println(label);
	        String cleanText = label.getText().replace(" *", "").trim();
	        questionTexts.add(cleanText);
	    }
	    return questionTexts;
	}
	
	public void enterQuestion(String question_name,String value) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(1000));
		Actions action = new Actions(driver);
		if(question_name.isBlank() || question_name.isEmpty()) {
			return;
		}
		try {
//			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[normalize-space(text())='"+question_name+"']/following-sibling::div")));
			WebElement enterValue = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[normalize-space(text())='"+question_name+"']/following-sibling::div//ng-select[@bindvalue=\"id\"]")));
//			WebElement text = driver.findElement(By.xpath("//label[normalize-space(text())='Registration Number']/following-sibling::div//input[@type=\"text\"]"));
			action.moveToElement(enterValue).click().perform();
			Thread.sleep(300);
			action.sendKeys(value).perform();
			action.sendKeys(Keys.ENTER).perform();
		}
		catch(TimeoutException e) {
			//throw new RuntimeException("Transmission field is not found on Inspection Type UI");
		}catch(RuntimeException e) {
			throw e;
		}catch(Exception e) {
		}
	}
	public void inspectionType(String inspection_type) throws InterruptedException	 {
		 try {
			 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			 WebElement detailInsType = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space(text())='"+inspection_type+"']")));
//			 Thread.sleep(500);
			 Actions actions = new Actions(driver);
			 actions.moveToElement(detailInsType).click().perform();
//			 waitForLoadItemToDisappear();   
		 }		 	
		 catch(TimeoutException | NoSuchElementException e) {
			// throw new RuntimeException("Inspection Type "+inspection_type+" button is not visible or not found");
		 }catch(RuntimeException e) {
			 throw e;
		 }
		 catch(Exception e) {
			 System.out.println("Error in inspection type :" +e.getMessage());
		 }
	 }

	public void inspectionEntry(String reg_no,String inspection_type) throws InterruptedException, IOException	{
//		NewEnquiry.captureAndSaveScreenshot(driver, "Before inspection");

		inspectionInformation();
		Thread.sleep(2000);
	    try {
        	driver.findElement(By.xpath("(//div[@data-bs-toggle='sidebar'])[1]")).click();
        }catch(Exception e) {
        	
        }
//		NewEnquiry.captureAndSaveScreenshot(driver, "After inspection");

		clickAllPending();
//		NewEnquiry.captureAndSaveScreenshot(driver, "After All Pending");
		Thread.sleep(10000);
//		NewEnquiry.captureAndSaveScreenshot(driver, "After Waiting Pending");
		clickAllPending();

		clickAllPending();
//		NewEnquiry.captureAndSaveScreenshot(driver, "Click  Pending");

		Thread.sleep(1000);
		registrationNo(reg_no);
//		NewEnquiry.captureAndSaveScreenshot(driver, "Click  Reg");

		startInspection(reg_no);
		enterRegistrationNo(reg_no);
		//enterTransmission(transmission);
		//enterFuelType(fuel_type);
		//inspectionType(inspection_type);
		waitForLoadItemToDisappear();
	}
	//span[contains(text(),'Vehicle Details (')]
	public void waitForVehicleDetailsTab() throws InterruptedException	 {
		 try {
			 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
			 WebElement detailInsType = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Vehicle Details (')]")));
		 }		 	
		 catch(TimeoutException | NoSuchElementException e) {
//			 throw new RuntimeException("Inspection Type "+inspection_type+" button is not visible or not found");
		 }catch(RuntimeException e) {
			 throw e;
		 }
		 catch(Exception e) {
			 System.out.println("Error in inspection type :" +e.getMessage());
		 }
	 }

	public void tabName(String tab_name) throws InterruptedException {
		
    	Actions action = new Actions(driver);
        try {	
			    WebElement tab = driver.findElement(By.xpath("//span[contains(text(),'" + tab_name + "')]"));
			    JavascriptExecutor js = (JavascriptExecutor)driver;
			    js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", tab);
			    Thread.sleep(500);
//			    action.moveToElement(tab).perform();
			    js.executeScript("arguments[0].click();", tab);
//			    Thread.sleep(200);
//			    action.moveToElement(tab).click().perform();
        }  
        catch(org.openqa.selenium.ElementNotInteractableException | StaleElementReferenceException e) {
				WebElement tab = driver.findElement(By.xpath("//span[contains(text(),'" + tab_name + "')]"));
				JavascriptExecutor js = (JavascriptExecutor)driver;
				js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", tab);
				action.moveToElement(tab).perform();
				action.moveToElement(tab).click().perform();
//				tab.click();
        }
        catch(org.openqa.selenium.NoSuchElementException | TimeoutException  e)	 {   
        	//throw new RuntimeException(tab_name+" Not Found");
        }catch(NoSuchElementException e) {
        	//throw new RuntimeException(tab_name+" Not Found");
        }
        catch(RuntimeException e)  {
        	throw e;
        }     
        catch(Exception e)    {   
        }
        Thread.sleep(500);
	}
	// Upload Image using Sendkeys. No need to upload via dialog 
	public void imageType(String method_name, String filePath, String tab_name) {
		try {
	    	File f = new File(filePath);
	    	if (!f.exists()) {
	    		ExtentReportListener.log ("File not found at path: " + filePath+" for Question "+method_name,"WARNING");
	    	}
	        WebElement fileInput = driver.findElement(By.xpath("//label[contains(@for,'" + method_name + "')]/following::input[@type='file'][1]"));
	        // Use JavaScript to make input visible if it's hidden
	        ((JavascriptExecutor) driver).executeScript("arguments[0].style.display='block';", fileInput);
	        fileInput.sendKeys(filePath);  // upload file
	        totalQuestionFilled++;
	    } 
	    catch (org.openqa.selenium.NoSuchElementException e) {
	        //String updateMethod = method_name.replace("0", " ");
	        //throw new RuntimeException("Method '" + updateMethod + "' not found in " + tab_name);
	    } 
	    catch(RuntimeException e)	{
			throw e;
		}
	    catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Error uploading file: " + e.getMessage());
	    }
	}
	public void RadioType(String method_name, String condition, String tab_name) throws InterruptedException, ElementClickInterceptedException {
	    try {
	    	
	        List<WebElement> radioFields = driver.findElements(By.xpath("//label[contains(@for, '" + method_name + "')]"));	 
	        if (radioFields.isEmpty()) {
	        	return;
	        }
	        WebElement shortestElement = null;
	        String shortestForValue = null;
	        for (WebElement radioField : radioFields) {
	            String forValue = radioField.getAttribute("for");
	            //System.out.println("For Value: "+forValue);
	            if (forValue != null && !forValue.toLowerCase().contains("image")) {  
	                if (shortestForValue == null || forValue.length() < shortestForValue.length()) {
	                    shortestForValue = forValue;
	                    shortestElement = radioField;
	                }
	            }
	        }        
	        if(shortestElement==null) {
	        	return;
	        }
	        String match = shortestElement.getAttribute("for");	     
	        JavascriptExecutor js = (JavascriptExecutor) driver;
	        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", shortestElement);
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        // Thread.sleep(150);
	        List<WebElement> siblingDivs = driver.findElements(By.xpath("//label[@for='" + match + "']/following-sibling::div"));
	        
	        boolean isMatched = false; // Flag to track if a match is found
	        for (WebElement div : siblingDivs) {
	        	if(!div.isEnabled()) {
	        		return;
	        	}
	            String divText = div.getText().trim();
	            if (divText.equalsIgnoreCase(condition)) {
	                boolean clicked = false;
	                int attempts = 0;
	                while (!clicked && attempts < 3) {
	                    try {
	                        js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", div);
	                        Thread.sleep(200);
	                        wait.until(ExpectedConditions.elementToBeClickable(div)).click();
	                        clicked = true;
	                    } catch (ElementClickInterceptedException e) {
	                        attempts++;
	                        Thread.sleep(500);  // wait and retry
	                    } catch (ElementNotInteractableException e) {
	                        try {
	                            js.executeScript("arguments[0].click();", div); // force click via JS
	                            clicked = true;
	                        } catch (Exception ex) {
	                            System.out.println("JS click also failed: " + ex.getMessage());
	                            break;
	                        }
	                    } catch (Exception e) {
	                        System.out.println("Error " + e.getMessage());
	                        break;
	                    }
	                }
	                if (clicked) {
	                    isMatched = true;
	    	            totalQuestionFilled++;
	                    break;
	                }
	            }

	        }     
	        if (!isMatched) {
	            //String updateMethod = method_name.replace("0", " ");
	            //throw new RuntimeException(condition + " not found for " + updateMethod + " in " + tab_name);
	        }
	    } catch (TimeoutException e) {
	        //String updateMethod = method_name.replace("0", " ");
	        //throw new RuntimeException(updateMethod + " Question options not found in " + tab_name + " (timeout)");
	    } catch (org.openqa.selenium.NoSuchElementException e) {
	        //String updateMethod = method_name.replace("0", " ");
	       // throw new RuntimeException(updateMethod + " Question not found in " + tab_name);
	    } catch (RuntimeException e) {
	        throw e;
	    } catch (Exception e) {
	        //throw new RuntimeException("An unexpected error occurred: " + e.getMessage());
	    } finally {
	    }
	} 
	public void dropDownType(String method_name,String dropdown_value,String tab_name) throws InterruptedException	 {
		try {
			//waitForLoadItemToDisappear();
			WebElement element = driver.findElement(By.xpath("//select[contains(@aria-describedby,'"+method_name+"')]"));
			if(!element.isEnabled()) {
				return;
			}
			Actions action = new Actions(driver);
			action.moveToElement(element).perform();
			if(method_name.contains("Model")) {
				Thread.sleep(2000);
			}
			try{
				Select s = new Select(element);
				List<WebElement> options = s.getOptions();
				// Loop through and print each option
				for (WebElement option : options) {
				   // System.out.println(option.getText()); // Get text of each option
				}
				Thread.sleep(100);
				s.selectByVisibleText(dropdown_value);
		        totalQuestionFilled++;

			}
		 	catch(org.openqa.selenium.NoSuchElementException e)	{
		 		String updateMethod =method_name.replace("0", " ");
		 		//throw new RuntimeException(dropdown_value+" is not  present in dropdown for Question" +updateMethod+" in "+tab_name);
			}
		}
	 	catch(org.openqa.selenium.NoSuchElementException e)	{
	 		String updateMethod =method_name.replace("0", " ");
			//throw new RuntimeException(updateMethod+" Question not found in "+tab_name);
		}
		catch(RuntimeException e)	{
			throw e;
		}
		catch(Exception e)	{
			System.out.println(e.getMessage());
		}		
	 }
	 public void inputBasedType(String method_name,String input,String tab_name) throws InterruptedException	 {
		try {			
			WebElement element = driver.findElement(By.xpath("//input[contains(@aria-describedby,'"+method_name+"')]"));

			if(!element.isEnabled()) {
				return;
			}
			Actions action = new Actions(driver);
			action.moveToElement(element).perform();
			element.clear();
			Thread.sleep(100);
			element.sendKeys(input);
	        totalQuestionFilled++;

		}
	 	catch(org.openqa.selenium.NoSuchElementException e)		{
	 		String updateMethod =method_name.replace("0", " ");
			//throw new RuntimeException(updateMethod+" Question not found in "+tab_name);
			//throw new RuntimeException(method_name+" not found");
		}
		catch(RuntimeException e)	{
			throw e;
		}
		catch(Exception e)	{
			System.out.println(e.getMessage());
		}
	}
	public int getFilledQuestionCount() {
		    return totalQuestionFilled;
	}
 	public void executeMethod(String method_type,String method_name, String parameter,String tab_name) throws InterruptedException {
	  try {
 		switch (method_type.toLowerCase()) {
	        case "imagetype":
	        	imageType(method_name,parameter,tab_name);
	            break;
	        case "radiotype":
	            RadioType(method_name, parameter,tab_name);
	            break;
	        case "dropdowntype":
	        case "selecttype":
	            dropDownType(method_name, parameter,tab_name);
	            break;
	        case "inputbasedtype":
	            inputBasedType(method_name, parameter,tab_name);
	            break;
	        case "inputtype":
	            inputBasedType(method_name, parameter,tab_name);
	            break;
	        case "starratingtype":
	        	StarRating(method_name,parameter,tab_name);
	        	break;
	        case "starrating":
	        	StarRating(method_name,parameter,tab_name);
	        	break;
	        default:
	            throw new RuntimeException("No matching method type found for: " + method_type);
	    }
	  }
	  catch(RuntimeException e)  {
		  throw e;
	  }
	  catch(Exception e)  {  
	  }
	}	
 	public void StarRating(String method_name, String parameter,String tab_name) throws InterruptedException {
 	 try {
	        // Convert the index from String to int
	        int starIndex;
	        try {
	            starIndex = Integer.parseInt(parameter); // Convert String to int
	        } catch (NumberFormatException e) {
	            System.out.println("Invalid parameter passed. Please provide a number as a string.");
	            return;  // Exit if invalid
	        }
	        WebElement labelElement = driver.findElement(By.xpath("//label[contains(@for, '" + method_name + "')]"));

	        WebElement starRatingContainer = labelElement.findElement(By.xpath("following-sibling::formly-field-star-rating"));
	        WebElement divContainer = starRatingContainer.findElement(By.tagName("div"));
	        List<WebElement> starElements = divContainer.findElements(By.tagName("i"));
	        if (starIndex > 0 && starIndex <= starElements.size()) {
//	        	Actions action = new Actions(driver);
//	        	action.moveToElement(starElements.get(starIndex - 1)).perform();
//	        	Thread.sleep(500);
//	        	starElements.get(starIndex - 1).click();  // Subtract 1 since index is 0-based
	        	JavascriptExecutor js = (JavascriptExecutor) driver;
	        	js.executeScript("arguments[0].click();", starElements.get(starIndex - 1));
		        totalQuestionFilled++;
	        } else {
	            //System.out.println("Index out of bounds. Please pass a valid index between 1 and " + starElements.size());
	            throw new RuntimeException("You can not select rating more than 10 for "+method_name+" in "+tab_name);
	        }
 		}
 		catch(org.openqa.selenium.NoSuchElementException e)	{
 			//throw new RuntimeException(method_name+" not found");
 			//String updateMethod =method_name.replace("0", " ");
			//throw new RuntimeException(updateMethod+" Question not found in "+tab_name);
 		}
 		catch(RuntimeException e)	{
 			throw e;
 		}
 		catch(Exception e)	 { 			
 		}
     }
 	public void saveChangeBtn(String reg_no) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));
		WebDriverWait wait1 = new WebDriverWait(driver,Duration.ofSeconds(1));
		WebDriverWait wait2 = new WebDriverWait(driver,Duration.ofMinutes(60));

	   while (true) {
	        try {
	            WebElement nextBtn = driver.findElement(By.xpath("//button[text()=\" Next \"]"));
//	            Actions actions = new Actions(driver);
//	            actions.moveToElement(nextBtn).perform();
//	            Thread.sleep(500);
//	            actions.moveToElement(nextBtn).click().perform();
	            JavascriptExecutor js = (JavascriptExecutor)driver;
	            js.executeScript("arguments[0].click();", nextBtn);
	        } catch (org.openqa.selenium.NoSuchElementException e) {
	            break; // Break when the next button is not found
	        }
	    }
	    try {
			WebElement saveBtn = driver.findElement(By.xpath("//button[contains(text(),\"Save Change\")]"));	
			try {
				if(saveBtn.isEnabled()) {
					saveBtn.click();
				}
				wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//formly-validation-message[text()=\"This field is required\"]")));
				throw new RuntimeException("Please enter all mandatory fields");				
			}catch(TimeoutException e) {
			}
			
//			long startTime = System.currentTimeMillis();
//			System.out.println("STart time "+startTime/1000);

			// Wait until saveBtn is enabled
			wait2.until(driver -> saveBtn.isEnabled());

//			long endTime = System.currentTimeMillis();
//			System.out.println("End time "+endTime/1000);
//			long totalWaited = endTime - startTime;
//			System.out.println("Waited for " + totalWaited + " milliseconds (" + totalWaited/1000.0 + " seconds)");
			
			if (saveBtn.getAttribute("disabled") != null || !saveBtn.isEnabled()) {
				System.out.println("Please enter all mandatory fields");
				throw new RuntimeException("Please enter all mandatory fields");				
			}     
			else {
				Actions actions = new Actions(driver);
				actions.moveToElement(saveBtn).perform();
				Thread.sleep(1000);
				actions.moveToElement(saveBtn).click().perform();				
				waitForLoadItemToDisappear();
				String saveAlert = driver.findElement(By.xpath("//div[@id=\"swal2-html-container\"]")).getText();
				if(saveAlert.toLowerCase().contains("success") || saveAlert.toLowerCase().contains("partially") ) 
				{
					wait1.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()=\"OK\"]")));
					WebElement OkBtn = driver.findElement(By.xpath("//button[text()=\"OK\"]"));
					OkBtn.click();
					System.out.println(saveAlert+" for Registration No: "+reg_no);
					ExtentReportListener.log(saveAlert+" for Registration No: "+reg_no,"PASS");
				}else {
					System.out.println(saveAlert+" for Registration No: "+reg_no);
					ExtentReportListener.log(saveAlert+" for Registration No: "+reg_no,"FAIL");			
				}
			}			
	    }	    
	    catch(RuntimeException e) {
	    	throw e;
	    }
	    catch(Exception e) {
	    	System.out.println("Error: "+e.getMessage());
	    }
		waitForLoadItemToDisappear();			
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


// Line 137 comment bacause no  needed of registration no.
// Line 280 Inspection type comment waitforloaditemtodisapper for wait Vahan Data