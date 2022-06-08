package activitiesobject;

import com.github.javafaker.Faker;
import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.config.ConfigurationManager;
import utilities.config.ConfigurationProperties;

import java.time.Duration;
import java.util.List;
import java.util.Set;

public class BaseScreen {
    protected ConfigurationProperties configurationProperties = ConfigurationManager.getConfiguration();
    private static final Logger LOGGER = LogManager.getLogger(BaseScreen.class.getName());

    @Getter(AccessLevel.PROTECTED)
    private final AppiumDriver baseMobileDriver;

    @Getter(AccessLevel.PROTECTED)
    private final WebDriverWait webDriverWait;

    protected BaseScreen(AppiumDriver appiumDriver) {
        this.baseMobileDriver = appiumDriver;
        AppiumFieldDecorator appiumFieldDecorator = new AppiumFieldDecorator(appiumDriver);
        PageFactory.initElements(appiumFieldDecorator, this);
        webDriverWait = new WebDriverWait(appiumDriver, Duration.ofSeconds(60));
    }

    protected static Faker getFakerDetails() {
        return new Faker();
    }

    protected By convertMobileElementToBy(WebElement webElement) {
        By by = null;
        String[] data = webElement.toString().replaceAll("(.*\\{)", "").replaceAll("(\\}.*)", "").split(": ", 2);
        String locatorType = data[0];
        String locatorPath = data[1];

        switch (locatorType.trim()) {
            case "By.id" -> by = new By.ById(locatorPath);
            case "By.xpath" -> by = new By.ByXPath(locatorPath);
            case "By.className" -> by = new By.ByClassName(locatorPath);
            case "AppiumBy.iOSClassChain" -> by = AppiumBy.iOSClassChain(locatorPath);
            case "AppiumBy.androidUIAutomator" -> by = AppiumBy.androidUIAutomator(locatorPath);
            default -> System.out.println("Locator \"" + locatorType + "\" is not handled.");
        }
        return by;
    }

    protected boolean isElementPresent(By by) {
        List<?> mobileElements = getBaseMobileDriver().findElements(by);
        return mobileElements.size() != 0;
    }

    protected boolean isElementPresent(WebElement webElement) {
        try {
            webElement.isDisplayed();
            return true;
        } catch (NoSuchElementException | StaleElementReferenceException exception) {
            return false;
        }
    }

    protected String getContext() {
        return (String) getBaseMobileDriver().execute(DriverCommand.GET_CURRENT_CONTEXT_HANDLE).getValue();
    }

    protected Set<String> getContexts() {
        return ((AndroidDriver) getBaseMobileDriver()).getContextHandles();
    }

    protected String switchToWebContext() {
        for (String context : getContexts()) {
            if (context.contains("WEBVIEW_")) {
                System.out.println("Context name: " + context);
                ((AndroidDriver) getBaseMobileDriver()).context(context);
            }
        }
        return null;
    }

    protected void switchToNativeContext() {
        getBaseMobileDriver().execute(DriverCommand.SWITCH_TO_CONTEXT, ImmutableMap.of("name", "NATIVE_APP"));
    }


}
