package ProcurementHeadless;

import org.apache.poi.EncryptedDocumentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class driverClass {

    public static WebDriver browserSel() throws EncryptedDocumentException, InterruptedException {

        String browserName = System.getProperty("browser", "chrome");
        String headless = System.getProperty("headless", "false");   // default = false (LOCAL)

        WebDriver driver;

        switch (browserName.toLowerCase()) {

            case "chrome":

            	  // 👉 Path of downloaded chromedriver.exe
                System.setProperty("webdriver.chrome.driver",
                        System.getProperty("user.dir") + "\\src\\main\\resources\\drivers\\chromedriver.exe");

                ChromeOptions options = new ChromeOptions();
                // 👉 Only run headless when passed true
                if (headless.equalsIgnoreCase("true")) {
                    options.addArguments("--headless=new");
                    System.out.println("Running HEADLESS (Jenkins/CI)");
                } else {
                    System.out.println("Running NORMAL (Local)");
                }

                options.addArguments(
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
