package utilities.platform.nativeapp;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.Setting;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import utilities.HttpClientHelper;
import utilities.config.ConfigurationManager;
import utilities.config.ConfigurationProperties;
import utilities.platform.AndroidGetDeviceInfo;
import utilities.platform.GenericDriverManager;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class AndroidNativeAppManager extends GenericDriverManager {
    ConfigurationProperties configurationProperties = ConfigurationManager.getConfiguration();
    private static final Logger LOGGER = LogManager.getLogger(AndroidNativeAppManager.class.getName());

    private AndroidDriver androidDriver;

    @Override
    protected void createDriver(String automationName) {

        List<String> pkgActivityList = AndroidGetDeviceInfo.getPackageAndLauncherName();
        String strPackageName = configurationProperties.androidAppPackage().isEmpty() ? pkgActivityList.get(0) : configurationProperties.androidAppPackage();
        LOGGER.info("PACKAGE NAME : " + strPackageName);
        String strLauncherActivityName = configurationProperties.androidAppActivity().isEmpty() ? pkgActivityList.get(1) : configurationProperties.androidAppActivity();
        LOGGER.info("LAUNCHER ACTIVITY NAME : " + strLauncherActivityName);

        switch (configurationProperties.appTargetExecutionEnvironment().toLowerCase(Locale.ROOT)) {
            case "local" -> {
                LOGGER.info("EXECUTION ENVIRONMENT NAME : " + configurationProperties.appTargetExecutionEnvironment().toUpperCase());
                String strDeviceUUID = configurationProperties.androidDeviceUDID().isEmpty() ? AndroidGetDeviceInfo.getConnectedDeviceNameOrUIID() : configurationProperties.androidDeviceUDID();
                String[] platformInfo = configurationProperties.androidPlatformAndVersion().split(" ");
                String strPlatformName = configurationProperties.androidPlatformAndVersion().isEmpty() ? "Android" : platformInfo.length == 1 ? platformInfo[0] : "Android";
                String strPlatformVersion = configurationProperties.androidPlatformAndVersion().isEmpty() ? AndroidGetDeviceInfo.getConnectedDevicePlatformVersion() : platformInfo.length == 2 ? platformInfo[1] : AndroidGetDeviceInfo.getConnectedDevicePlatformVersion();

                UiAutomator2Options uiAutomator2Options = new UiAutomator2Options()
                        .setAutomationName(automationName)
                        .clearSystemFiles()
                        .clearDeviceLogsOnStart()
                        .setFullReset(false)
                        .setNoReset(true)
                        .autoGrantPermissions()
                        .setDeviceName(strDeviceUUID)
                        .setPlatformName(strPlatformName)
                        .showChromedriverLog()
                        .setPlatformVersion(strPlatformVersion)
                        .setApp(new File(configurationProperties.androidApkFilePath()).getAbsolutePath())
                        .setAppPackage(strPackageName)
                        .setAppActivity(strLauncherActivityName)
                        .ensureWebviewsHavePages()
                        .setAppWaitActivity("*")
                        .setAppWaitForLaunch(true)
                        .setNewCommandTimeout(Duration.ofSeconds(200));
                uiAutomator2Options.setCapability(AndroidMobileCapabilityType.UNICODE_KEYBOARD, true);
                uiAutomator2Options.setCapability(AndroidMobileCapabilityType.RESET_KEYBOARD, true);

                try {
                    androidDriver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), uiAutomator2Options);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                androidDriver.setSetting(Setting.WAIT_FOR_SELECTOR_TIMEOUT, 200000);
                androidDriver.setSetting(Setting.WAIT_FOR_IDLE_TIMEOUT, 0);
                setAppiumDriver(androidDriver);
            }
            case "browserstack" -> {
                LOGGER.info("EXECUTION ENVIRONMENT NAME : " + configurationProperties.appTargetExecutionEnvironment().toUpperCase());
                String username = System.getenv("BROWSERSTACK_USERNAME");
                String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");

                LOGGER.info("BROWSERSTACK_USERNAME : " + username);
                LOGGER.info("BROWSERSTACK_ACCESS_KEY : " + accessKey);

                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setCapability("automationName", "Appium");
                capabilities.setCapability("browserstack.appium_version", "1.22.0");
                capabilities.setCapability("localIdentifier", "nativeapp:38a0bbd4-4c6e-4654-9b5f-3fd23ce86f35");

                //Device Name
                capabilities.setCapability("platformVersion", configurationProperties.appBrowserStackPlatformVersion());
                capabilities.setCapability("platformName", configurationProperties.appBrowserStackPlatformName());
                capabilities.setCapability("deviceName", configurationProperties.appBrowserStackDeviceName());

                //App Name
                String fileName = HttpClientHelper.uploadAppOnBrowserStack();
                LOGGER.info("BROWSERSTACK_FILE_URL : " + fileName);
                capabilities.setCapability("app", fileName);

                //Organize Test
                capabilities.setCapability("project", configurationProperties.appAppName());
                capabilities.setCapability("build", configurationProperties.appBrowserStackBuildName());
                capabilities.setCapability("name", configurationProperties.appBrowserStackSessionName() + "_" + UUID.randomUUID());

                //Logs
                capabilities.setCapability("browserstack.deviceLogs", "true");
                capabilities.setCapability("browserstack.appiumLogs", "true");
                capabilities.setCapability("browserstack.networkLogs", "true");
                capabilities.setCapability("browserstack.debug", "true");

//                capabilities.setCapability("browserstack.maskCommands","settings, setValues, getValues, setCookies, getCookies");

                capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, strPackageName);
                capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, strLauncherActivityName);
                capabilities.setCapability(AndroidMobileCapabilityType.APP_WAIT_ACTIVITY, "*");
                capabilities.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, true);
                capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 200);

                try {
                    androidDriver = new AndroidDriver(new URL("http://" + username + ":" + accessKey + "@hub-cloud.browserstack.com/wd/hub"), capabilities);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }

                androidDriver.setSetting(Setting.WAIT_FOR_SELECTOR_TIMEOUT, 200000);
                androidDriver.setSetting(Setting.WAIT_FOR_IDLE_TIMEOUT, 0);
                setAppiumDriver(androidDriver);
            }
        }
    }
}
