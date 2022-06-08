package utilities.miscellanious;

import io.appium.java_client.AppiumDriver;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.logging.LogEntry;
import utilities.config.ConfigurationManager;
import utilities.config.ConfigurationProperties;

import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class AndroidLogCat {
 private static final Logger LOGGER = LogManager.getLogger(AndroidLogCat.class.getName());

    @SneakyThrows
    public static void captureLog(AppiumDriver driver, String testName) {
        ConfigurationProperties configurationProperties = ConfigurationManager.getConfiguration();
        if (configurationProperties.getExecutionPlatformEnvironment().equals("android") && !configurationProperties.appMobileApplicationType().equals("web")) {

            String strDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM_dd_yyyy"));
            String strTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH_mm_ss"));
            LOGGER.info(driver.getSessionId() + ": Saving device log...");
            List<LogEntry> logEntries = driver.manage().logs().get("logcat").getAll();
            File logFile = new File(System.getProperty("user.dir") + File.separator + "Android_Logcat" + File.separator + strDate + File.separator + strTime + "_" + testName + ".txt");
            logFile.getParentFile().mkdirs();

            PrintWriter log_file_writer = new PrintWriter(logFile);
            log_file_writer.println(logEntries);
            log_file_writer.flush();
            LOGGER.info(driver.getSessionId() + ": Saving device log - Done.");
        }

    }
}
