package stepDefinitions.hooks;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import testutilities.assertion.CustomSoftAssertion;
import testutilities.context.TestContext;
import utilities.Miscellaneous;
import utilities.config.ConfigurationManager;
import utilities.config.ConfigurationProperties;
import utilities.image.ImageUtilities;
import utilities.listener.CucumberListener;

import java.util.Locale;

public class CustomHookAssertions {

    private final TestContext testContext;
    private static final Logger LOGGER = LogManager.getLogger(CustomHookAssertions.class.getName());
    private ConfigurationProperties configurationProperties = ConfigurationManager.getConfiguration();
    public CustomHookAssertions(TestContext context) {
        this.testContext = context;
    }

    public static CustomSoftAssertion softAssertion;

    @Before
    public void beforeScenario(Scenario scenario) {
        softAssertion = new CustomSoftAssertion(scenario);
    }

    @After
    public void takeScreenShotsOnSoftAssertion() {
        softAssertion.assertAll();
    }

    @AfterStep
    public void takeScreenShotsOnStepFailure(Scenario scenario) {
        switch (scenario.getStatus()) {
            case SKIPPED, PENDING, UNDEFINED, AMBIGUOUS, FAILED, UNUSED -> {
                try {
                    byte[] screenshot = ((TakesScreenshot) testContext.getAppiumDriver()).getScreenshotAs(OutputType.BYTES);
                    scenario.attach(ImageUtilities.resizeBytesImage(screenshot), "image/png", scenario.getName());
                    if (configurationProperties.appTargetExecutionEnvironment().toLowerCase(Locale.ROOT).equals("browserstack")) {
//                        Miscellaneous.setBrowserStackSetSessionName(testContext.getAppiumDriver(), scenario.getName());
                        Miscellaneous.setBrowserStackFailStatus(testContext.getAppiumDriver(), CucumberListener.stepName);
                        Miscellaneous.setBrowserStackTestLogType(testContext.getAppiumDriver(), CucumberListener.stepName,"error");
                    }
                } catch (Exception exception) {
                    LOGGER.info(exception.getCause().toString());
                }
            }
            case PASSED -> {
                if (configurationProperties.appTargetExecutionEnvironment().toLowerCase(Locale.ROOT).equals("browserstack")) {
//                    Miscellaneous.setBrowserStackSetSessionName(testContext.getAppiumDriver(), CucumberListener.stepName);
                    Miscellaneous.setBrowserStackPassStatus(testContext.getAppiumDriver(), CucumberListener.stepName);
                    Miscellaneous.setBrowserStackTestLogType(testContext.getAppiumDriver(), scenario.getName(),"info");
                }
            }
        }
    }
}
