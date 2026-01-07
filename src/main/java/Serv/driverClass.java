package Serv;

import java.io.File;

import org.apache.poi.EncryptedDocumentException;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.devtools.DevTools;
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
			String userHome = System.getProperty("user.home");
			String downloadsFolderPath = userHome + File.separator + "Downloads";
			String chromeDriverPath = downloadsFolderPath + File.separator + "chromedriver.exe";
			File chromeDriverFile = new File(chromeDriverPath);
			if (!chromeDriverFile.exists()) {
				throw new RuntimeException("ChromeDriver not found in Downloads folder: " + chromeDriverPath);
			}
			System.setProperty("webdriver.chrome.driver", chromeDriverPath);
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--disable-gpu", "--window-size=1920,1080", "--disable-notifications");
			driver = new ChromeDriver(options);
			driver.manage().window().maximize();
			
		}
			break;
		default:
			throw new IllegalStateException("Unexpected browser: " + browserName);
		}
		return driver;
	}
}