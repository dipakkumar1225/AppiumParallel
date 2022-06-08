package testutilities.context;

import activitiesobject.PageObjectManager;
import io.appium.java_client.AppiumDriver;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import stepDefinitions.hooks.InitDriver;
import utilities.config.ConfigurationManager;
import utilities.config.ConfigurationProperties;
import utilities.platform.nativeapp.AndroidNativeAppManager;

import java.util.Locale;

public class TestContext {
    private static final Logger LOGGER = LogManager.getLogger(TestContext.class.getName());
    @Getter @Setter
    private AppiumDriver appiumDriver;

    @Getter
    private final ScenarioContext scenarioContext;

    @Getter
    private final PageObjectManager pageObjectManager;

    @Getter
    private final ConfigurationProperties configurationProperties = ConfigurationManager.getConfiguration();

    public TestContext() {
        setAppiumDriver(InitDriver.appiumDriver);
        pageObjectManager = new PageObjectManager(getAppiumDriver());
        scenarioContext = new ScenarioContext();
    }

}
