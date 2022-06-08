package utilities.platform.mobileweb;

import io.appium.java_client.Setting;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.github.bonigarcia.wdm.WebDriverManager;
import utilities.config.ConfigurationManager;
import utilities.config.ConfigurationProperties;
import utilities.platform.AndroidGetDeviceInfo;
import utilities.platform.GenericDriverManager;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Locale;

public class AndroidWebAppManager extends GenericDriverManager {
    ConfigurationProperties configurationProperties = ConfigurationManager.getConfiguration();
    WebDriverManager webDriverManager;
    String webDriverPath;
    private AndroidDriver androidDriver;
    @Override
    protected void createDriver(String automationName) {

        String strDeviceUUID = configurationProperties.androidDeviceUDID().isEmpty() ? AndroidGetDeviceInfo.getConnectedDeviceNameOrUIID() : configurationProperties.androidDeviceUDID();

        String[] platformInfo = configurationProperties.androidPlatformAndVersion().split(" ");
        String strPlatformName = configurationProperties.androidPlatformAndVersion().isEmpty() ? "Android" : platformInfo.length == 1 ? platformInfo[0] : "Android";
        String strPlatformVersion = configurationProperties.androidPlatformAndVersion().isEmpty() ? AndroidGetDeviceInfo.getConnectedDevicePlatformVersion() : platformInfo.length == 2 ? platformInfo[1] : AndroidGetDeviceInfo.getConnectedDevicePlatformVersion();

        UiAutomator2Options uiAutomator2Options = new UiAutomator2Options()
                .setAutomationName(automationName)
                .clearSystemFiles()
                .clearDeviceLogsOnStart()
                .setDeviceName(strDeviceUUID)
                .setPlatformName(strPlatformName)
                .showChromedriverLog()
                .setPlatformVersion(strPlatformVersion)
                .setApp(new File(configurationProperties.androidApkFilePath()).getAbsolutePath())
                .setNewCommandTimeout(Duration.ofSeconds(200));
        uiAutomator2Options.setCapability(AndroidMobileCapabilityType.UNICODE_KEYBOARD, true);
        uiAutomator2Options.setCapability(AndroidMobileCapabilityType.RESET_KEYBOARD, true);
        switch (configurationProperties.appMobileBrowserType().toLowerCase(Locale.ROOT)){
            case "chrome", "chromedriver", "chromium", "chrome driver"-> {
                webDriverManager = WebDriverManager.chromedriver().browserVersion(configurationProperties.appMobileBrowserVersion());
                webDriverManager.setup();
                webDriverPath = webDriverManager.getDownloadedDriverPath();
                uiAutomator2Options.setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");
            }
        }

        try {
            androidDriver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), uiAutomator2Options);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        androidDriver.setSetting(Setting.IGNORE_UNIMPORTANT_VIEWS,true);
        androidDriver.setSetting(Setting.WAIT_FOR_SELECTOR_TIMEOUT, 360000);
        androidDriver.setSetting(Setting.WAIT_FOR_IDLE_TIMEOUT, 0);
        setAppiumDriver(androidDriver);
    }
}
