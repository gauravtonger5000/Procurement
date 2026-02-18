package ProcurementHeadless;

import org.apache.poi.EncryptedDocumentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class driverClass {

    public static WebDriver browserSel() throws EncryptedDocumentException, InterruptedException {

        String browserName = System.getProperty("browser", "Web Chrome");
        WebDriver driver;

        switch (browserName) {

            case "Web Chrome":

                ChromeOptions options = new ChromeOptions();
                options.addArguments(
                        "--headless=new",
                        "--disable-gpu",
                        "--window-size=1920,1080",
                        "--disable-notifications",
                        "--no-sandbox",
                        "--disable-dev-shm-usage"
                );

                driver = new ChromeDriver(options);
                break;

            default:
                throw new IllegalStateException("Unexpected browser: " + browserName);
        }

        return driver;
    }
}
