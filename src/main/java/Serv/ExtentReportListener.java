package Serv;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportListener {
    private static ExtentReports extent;
    private static ExtentTest test;
    private static ExtentSparkReporter sparkReporter;

    public void setupReport(String reportPath) {
        // Initialize Spark reporter for HTML file 
        sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setTheme(Theme.DARK); // Set dark theme
        sparkReporter.config().setDocumentTitle("Automation Report");
        sparkReporter.config().setReportName("Test Report");
     
        // Initialize ExtentReports and attach the Spark reporter
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Project Name", "Procurement New Enquiry");
        extent.setSystemInfo("Host Name", "LocalHost");
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("User Name", "ACS-90");
    }

    public static ExtentTest startTest(String testName) {
        test = extent.createTest(testName);
        return test;
    }

    public static void log(String details, String status) {
        if (status.equalsIgnoreCase("PASS")) {
            test.pass(details);
        } else if (status.equalsIgnoreCase("FAIL")) {
            test.fail(details);
        }else if(status.equalsIgnoreCase("WARNING")) {
        	test.warning(details);
        }else if(status.equalsIgnoreCase("SKIP")) {
        	test.skip(details);
        }else if(status.equalsIgnoreCase("INFO")) {
        	test.info(details);
        }  
    }

    public void flushReport() {
        extent.flush();
    }
}
