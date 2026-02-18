package ProcurementHeadless;

import java.io.File;
import java.util.Optional;

import org.apache.poi.EncryptedDocumentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;

public class driverClass {
	WebDriver driver;
	public driverClass(WebDriver driver) { this.driver = driver; }
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
				throw new RuntimeException("ChromeDriver not found: " + chromeDriverPath);
			}

			System.setProperty("webdriver.chrome.driver", chromeDriverPath);

			ChromeOptions options = new ChromeOptions();

			options.addArguments("--headless=new", // ✅ new headless (better)
					"--disable-gpu", "--window-size=1920,1080", // ✅ required in headless
					"--disable-notifications", "--no-sandbox", "--disable-dev-shm-usage",
					"--force-device-scale-factor=1", "--high-dpi-support=1");

			driver = new ChromeDriver(options);

			// ❌ REMOVE maximize in headless (does nothing)
			// driver.manage().window().maximize();

			// ✅ Enable DevTools (useful for full page screenshot / network)
		}
			break;

		default:
			throw new IllegalStateException("Unexpected browser: " + browserName);
		}

		return driver;
	}
}
