package utilities.platform;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class GenericDriverManager {
    private static final Logger LOGGER = LogManager.getLogger(GenericDriverManager.class.getName());
    private ThreadLocal<AppiumDriver> appiumDriver = new ThreadLocal<>();

    protected abstract void createDriver(String automationName);

    public AppiumDriver getMobileDriver(String automationName) {
        LOGGER.info("INITIATING DRIVER");
        if (appiumDriver.get() == null) {
            createDriver(automationName);
        }
        LOGGER.info("DRIVER INITIATED\n");
        return appiumDriver.get();
    }

    public void setAppiumDriver(AppiumDriver driver1){
        appiumDriver.set(driver1);
    }

    public void quitDriver() {
        if (appiumDriver.get() instanceof AndroidDriver) {
            ((AndroidDriver)(appiumDriver.get())).removeApp("io.appium.settings");
            ((AndroidDriver)(appiumDriver.get())).removeApp("io.appium.uiautomator2.server");
            ((AndroidDriver)(appiumDriver.get())).removeApp("io.appium.uiautomator2.server.test");
        }

        if (appiumDriver != null) {
            appiumDriver.get().quit();
        }
        appiumDriver = null;

    }
}
