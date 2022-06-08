package stepDefinitions.hooks;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.AutomationName;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.util.Strings;
import utilities.config.ConfigurationManager;
import utilities.config.ConfigurationProperties;
import utilities.enums.DriverType;
import utilities.listener.WebEventCapture;
import utilities.miscellanious.WriteProperties;
import utilities.platform.AppiumServer;
import utilities.platform.GenericDriverFactory;
import utilities.platform.GenericDriverManager;

import java.util.Locale;

public class InitDriver {

    private static final Logger LOGGER = LogManager.getLogger(InitDriver.class.getName());
    private static final AppiumServer APPIUM_SERVER = new AppiumServer();
    static ConfigurationProperties configuration = ConfigurationManager.getConfiguration();
    static GenericDriverManager genericDriverManager;
    public static AppiumDriver appiumDriver;
    public static EventFiringWebDriver eventDriver;
    private static WebEventCapture handler;

    @BeforeAll
    public static void doInitDriver() {

        String strAppiumServerAddress = Strings.isNullOrEmpty(configuration.appAppiumServerDefaultAddress()) ? null : configuration.appAppiumServerDefaultAddress();
        Integer intAppiumServerPortNumber = Strings.isNullOrEmpty(configuration.appAppiumServerDefaultPort()) ? null : Integer.parseInt(configuration.appAppiumServerDefaultPort());
        if (configuration.appTargetExecutionEnvironment().toLowerCase(Locale.ROOT).equals("local")){
            APPIUM_SERVER.startServer(strAppiumServerAddress, intAppiumServerPortNumber);
        }

        switch (configuration.getExecutionPlatformEnvironment().toLowerCase()) {
            case "ios", "mac", "macos", "iphone" -> {
                LOGGER.info("ENVIRONMENT NAME " + configuration.getExecutionPlatformEnvironment().toUpperCase());
                genericDriverManager = GenericDriverFactory.getMobileDriverManager(DriverType.IOS);
                appiumDriver = genericDriverManager.getMobileDriver(AutomationName.IOS_XCUI_TEST);
            }
            case "android" -> {
                LOGGER.info("PLATFORM NAME " + configuration.getExecutionPlatformEnvironment().toUpperCase());
                switch (configuration.appMobileApplicationType().toLowerCase(Locale.ROOT)) {
                    case "native" -> {
                        LOGGER.info("APPLICATION TYPE " + configuration.appMobileApplicationType().toUpperCase());
                        genericDriverManager = GenericDriverFactory.getMobileDriverManager(DriverType.ANDROID);
                        appiumDriver = genericDriverManager.getMobileDriver(AutomationName.ANDROID_UIAUTOMATOR2);
                    }
                    case "web" -> {
                        LOGGER.info("APPLICATION TYPE " + configuration.appMobileApplicationType().toUpperCase());
                        genericDriverManager = GenericDriverFactory.getMobileDriverManager(DriverType.ANDROID_WEB);
                        appiumDriver = genericDriverManager.getMobileDriver(AutomationName.ANDROID_UIAUTOMATOR2);
                        eventDriver = new EventFiringWebDriver(appiumDriver);
                        handler = new WebEventCapture();
                        eventDriver.register(handler);
                        eventDriver.get(configuration.appWebBaseURL());
                    }
                }
            }
        }
        WriteProperties.writeProperties(appiumDriver);
    }

    @SneakyThrows
    @AfterAll
    public static void tearDownDriver() {
        if (configuration.appDelayToQuit() != 0) {
            Thread.sleep(configuration.appDelayToQuit());
        }
        if (configuration.appMobileApplicationType().equals("web")) {
            eventDriver.unregister(handler);
        }
        LOGGER.info("TEARING DOWN DRIVER");
        genericDriverManager.quitDriver();
        LOGGER.info("DRIVER TEARED");

        if (configuration.appTargetExecutionEnvironment().toLowerCase(Locale.ROOT).equals("local")){
            APPIUM_SERVER.stopServer();
        }
    }
}


