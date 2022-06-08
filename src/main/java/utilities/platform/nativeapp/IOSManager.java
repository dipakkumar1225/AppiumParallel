package utilities.platform.nativeapp;

import io.appium.java_client.Setting;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import utilities.config.ConfigurationManager;
import utilities.config.ConfigurationProperties;
import utilities.platform.GenericDriverManager;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class IOSManager extends GenericDriverManager {

    ConfigurationProperties configurationProperties = ConfigurationManager.getConfiguration();
    private AndroidDriver androidDriver;
    @Override
    protected void createDriver(String automationName) {

        XCUITestOptions xcuiTestOptions = new XCUITestOptions()
                .setAutomationName(automationName)
                .clearSystemFiles()
                .setFullReset(false)
                .setNoReset(true)
                .setDeviceName(configurationProperties.iosUDID())
                .setPlatformName("14.4")
                .autoAcceptAlerts()
                .setApp(new File(configurationProperties.iosIPAPath()).getAbsolutePath())
                .setShowIosLog(configurationProperties.iosShowLog())
                .setShowXcodeLog(configurationProperties.iosShowXcodeLog())
                .setNewCommandTimeout(Duration.ofSeconds(200));
        try {
            androidDriver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), xcuiTestOptions);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        androidDriver.setSetting(Setting.WAIT_FOR_SELECTOR_TIMEOUT, 360000);
        androidDriver.setSetting(Setting.WAIT_FOR_IDLE_TIMEOUT, 0);
        setAppiumDriver(androidDriver);
    }
}
