package TestRun;

import java.io.File;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.EncryptedDocumentException;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class driverClass
{
	WebDriver driver;
	public driverClass(WebDriver driver) {
		this.driver = driver;
	}
	public static WebDriver browserSel() throws EncryptedDocumentException, InterruptedException {
		String browserName = System.getProperty("browser", "Web Chrome");
		WebDriver driver = null;
		switch (browserName) {
		case "Web Chrome": {
		
			String chromeDriverPath ="src/main/resources/drivers/chromedriver.exe";
			System.setProperty("webdriver.chrome.driver", chromeDriverPath);
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--disable-gpu", "--window-size=1920,1080", "--disable-notifications","--headless");
			driver = new ChromeDriver(options);
			driver.manage().window().maximize();
			
//	
		}
			break;
		case "Mobile Chrome": {
			String userHome = System.getProperty("user.home");
			String downloadsFolderPath = userHome + File.separator + "Downloads";
			String chromeDriverPath = downloadsFolderPath + File.separator + "chromedriver.exe";
			File chromeDriverFile = new File(chromeDriverPath);
			if (!chromeDriverFile.exists()) {
				throw new RuntimeException("ChromeDriver not found in Downloads folder: " + chromeDriverPath);
			}
			System.setProperty("webdriver.chrome.driver", chromeDriverPath);
			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("mobileEmulation", Map.of("deviceName", "iPhone X"));
			options.addArguments("--disable-gpu", "--disable-notifications");
			driver = new ChromeDriver(options);
		}
			break;
		case "Web Firefox": {
			String userHome = System.getProperty("user.home");
			String downloadsFolderPath = userHome + File.separator + "Downloads";
			String geckoDriverPath = downloadsFolderPath + File.separator + "geckodriver.exe";
			System.setProperty("webdriver.gecko.driver", geckoDriverPath);
			File firefoxDriverFile = new File(geckoDriverPath);
			if (!firefoxDriverFile.exists()) {
				throw new RuntimeException("Firefox Driver not found in Downloads folder: " + geckoDriverPath);
			}
			FirefoxOptions options = new FirefoxOptions();
			options.addArguments("--disable-gpu", "--window-size=1920,1080", "--disable-notifications");
			driver = new FirefoxDriver(options);
			driver.manage().window().maximize();
		}
			break;
		case "Mobile Firefox": {
			String userHome = System.getProperty("user.home");
			String downloadsFolderPath = userHome + File.separator + "Downloads";
			String geckoDriverPath = downloadsFolderPath + File.separator + "geckodriver.exe";
			System.setProperty("webdriver.gecko.driver", geckoDriverPath);
			File firefoxDriverFile = new File(geckoDriverPath);
			if (!firefoxDriverFile.exists()) {
				throw new RuntimeException("Firefox Driver not found in Downloads folder: " + geckoDriverPath);
			}
			FirefoxOptions options = new FirefoxOptions();
			options.addPreference("general.useragent.override","Mozilla/5.0 (iPhone; CPU iPhone OS 13_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1");

			// Disable GPU and notifications
			options.addArguments("--disable-gpu", "--disable-notifications");

			// Create Firefox driver with options
			driver = new FirefoxDriver(options);

			// Set the window size to simulate iPhone X dimensions (375x812)
			driver.manage().window().setSize(new Dimension(375, 812));
		}
			break;
		case "Web Edge": {
			String userHome = System.getProperty("user.home");
			String downloadsFolderPath = userHome + File.separator + "Downloads";
			String edgeDriverPath = downloadsFolderPath + File.separator + "msedgedriver.exe";
			File edgeDriverFile = new File(edgeDriverPath);
			if (!edgeDriverFile.exists()) {
				throw new RuntimeException("EdgeDriver not found in Downloads folder: " + edgeDriverPath);
			}
			System.setProperty("webdriver.edge.driver", edgeDriverPath);
			EdgeOptions option2 = new EdgeOptions();
			option2.addArguments("--disable-gpu", "--window-size=1920,1080", "--disable-notifications");
			driver = new EdgeDriver(option2);
			driver.manage().window().maximize();
		}
			break;
		case "Mobile Edge": {
			String userHome = System.getProperty("user.home");
			String downloadsFolderPath = userHome + File.separator + "Downloads";
			String edgeDriverPath = downloadsFolderPath + File.separator + "msedgedriver.exe";
			File edgeDriverFile = new File(edgeDriverPath);
			if (!edgeDriverFile.exists()) {
				throw new RuntimeException("EdgeDriver not found in Downloads folder: " + edgeDriverPath);
			}
			System.setProperty("webdriver.edge.driver", edgeDriverPath);
			EdgeOptions options = new EdgeOptions();
			options.addArguments("--disable-gpu", "--disable-notifications");
			options.addArguments("user-agent=Mozilla/5.0 (iPhone; CPU iPhone OS 13_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Edg/91.0.864.59 Safari/537.36");
			driver = new EdgeDriver(options);
			driver.manage().window().setSize(new Dimension(375, 812)); // iPhone X resolution
		}
			break;
		default:
			throw new IllegalStateException("Unexpected browser: " + browserName);
		}
		return driver;
	}
}