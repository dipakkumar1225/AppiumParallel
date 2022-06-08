package utilities.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.events.WebDriverEventListener;
import utilities.config.ConfigurationManager;
import utilities.config.ConfigurationProperties;

public class WebEventCapture implements WebDriverEventListener {

    ConfigurationProperties configurationProperties = ConfigurationManager.getConfiguration();
    private static final Logger LOGGER = LogManager.getLogger(WebEventCapture.class.getName());

    public WebEventCapture() {
    }


    @Override
    public void beforeAlertAccept(WebDriver webDriver) {

    }

    @Override
    public void afterAlertAccept(WebDriver webDriver) {

    }

    @Override
    public void afterAlertDismiss(WebDriver webDriver) {

    }

    @Override
    public void beforeAlertDismiss(WebDriver webDriver) {

    }

    @Override
    public void beforeNavigateTo(String s, WebDriver webDriver) {
        LOGGER.info("BEFORE NAVIGATED TO " + webDriver.getCurrentUrl());

    }

    @Override
    public void afterNavigateTo(String s, WebDriver webDriver) {
        LOGGER.info("AFTER NAVIGATED TO " + webDriver.getCurrentUrl());
        /*NgWebDriver ngWebDriver = new NgWebDriver((JavascriptExecutor) webDriver);
        ngWebDriver.waitForAngularRequestsToFinish();*/
    }

    @Override
    public void beforeNavigateBack(WebDriver webDriver) {

    }

    @Override
    public void afterNavigateBack(WebDriver webDriver) {

    }

    @Override
    public void beforeNavigateForward(WebDriver webDriver) {

    }

    @Override
    public void afterNavigateForward(WebDriver webDriver) {

    }

    @Override
    public void beforeNavigateRefresh(WebDriver webDriver) {

    }

    @Override
    public void afterNavigateRefresh(WebDriver webDriver) {

    }

    @Override
    public void beforeFindBy(By by, WebElement webElement, WebDriver webDriver) {
        boolean isDelayBetweenElement = configurationProperties.isDelayBetweenElement();
        LOGGER.info("DELAY BETWEEN ELEMENT " + by.toString() + " IS " + isDelayBetweenElement);
        if (isDelayBetweenElement) {
            try {
                LOGGER.info("DELAY BETWEEN ELEMENT (in seconds) IS\t" + configurationProperties.appWebElementWaitBeforeDuration());
                Thread.sleep(configurationProperties.appWebElementWaitBeforeDuration());
            } catch (Exception exception) {
                LOGGER.error("EXCEPTION WHILE APPLYING DELAY:\t" + exception.getMessage());
            }
        }
    }

    @Override
    public void afterFindBy(By by, WebElement webElement, WebDriver webDriver) {
        boolean isHighlightWebElement = configurationProperties.appWebElementIsHighlight();
        LOGGER.info("IS HIGHLIGHT WEB ELEMENT:\t" + isHighlightWebElement);
        if (isHighlightWebElement) {
            JavascriptExecutor js = (JavascriptExecutor) webDriver;
            js.executeScript("arguments[0].setAttribute('style','background: yellow; outline: dashed red;');", webElement);
            js.executeScript("arguments[0].style.outline=''", webElement, "");
            js.executeScript("arguments[0].style.background=''", webElement, "");
        }
    }

    @Override
    public void beforeClickOn(WebElement webElement, WebDriver webDriver) {

    }

    @Override
    public void afterClickOn(WebElement webElement, WebDriver webDriver) {

    }

    @Override
    public void beforeChangeValueOf(WebElement webElement, WebDriver webDriver, CharSequence[] charSequences) {

    }

    @Override
    public void afterChangeValueOf(WebElement webElement, WebDriver webDriver, CharSequence[] charSequences) {

    }

    @Override
    public void beforeScript(String s, WebDriver webDriver) {

    }

    @Override
    public void afterScript(String s, WebDriver webDriver) {

    }

    @Override
    public void beforeSwitchToWindow(String s, WebDriver webDriver) {

    }

    @Override
    public void afterSwitchToWindow(String s, WebDriver webDriver) {

    }

    @Override
    public void onException(Throwable throwable, WebDriver webDriver) {

    }

    @Override
    public <X> void beforeGetScreenshotAs(OutputType<X> outputType) {

    }

    @Override
    public <X> void afterGetScreenshotAs(OutputType<X> outputType, X x) {

    }

    @Override
    public void beforeGetText(WebElement webElement, WebDriver webDriver) {

    }

    @Override
    public void afterGetText(WebElement webElement, WebDriver webDriver, String s) {

    }
}
