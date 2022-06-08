package utilities.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "file:./config/androidApk.properties",
        "file:./config/IOSIPA.properties",
        "file:./config/generic.properties",
        "file:./config/devices/deviceDetails.properties",
        "file:./src/test/resources/classifications/DevicesDetails.properties",
})
public interface ConfigurationProperties extends Config {

    @Key("app.web.base.url")
    String appWebBaseURL();

    @Key("app.browserStack.buildName")
    String appBrowserStackBuildName();

    @Key("app.browserStack.sessionName")
    String appBrowserStackSessionName();

    @Key("app.appName")
    String appAppName();

    @Key("app.appium.serverDefaultAddress")
    String appAppiumServerDefaultAddress();

    @Key("app.appium.serverDefaultPort")
    String appAppiumServerDefaultPort();

    @Key("app.web.secondBase.url")
    String appWebSecondBaseURL();

    @Key("app.testData.path")
    String filePathTestData();

    /*@Key("app.android.app.fullReset")
    Boolean fullReset();

    @Key("app.android.app.noReset")
    Boolean noReset();*/

    @Key("app.android.app.resetAppState")
    String androidResetAppState();

    @Key("app.android.app.autoLaunch")
    Boolean autoLaunch();

    @Key("app.android.apk.filePath")
    String androidApkFilePath();

    @Key("app.android.appPackage")
    String androidAppPackage();

    @Key("app.android.appActivity")
    String androidAppActivity();

    @Key("app.android.platformNameAndVersion")
    String androidPlatformAndVersion();

    @Key("app.android.udid")
    String androidDeviceUDID();

    @Key("app.android.systemPort")
    String androidSystemPort();

    @Key("app.ios.path")
    String iosIPAPath();

    @Key("app.ios.bundleId")
    String iosBundleID();

    @Key("app.ios.platformNameAndVersion")
    String iosPlatformNameAndVersion();

    @Key("app.ios.udid")
    String iosUDID();

    @Key(("app.delayToQuit"))
    int appDelayToQuit();

    @Key("app.ios.systemPort")
    String iosSystemPort();

    @Key("app.ios.app.noReset")
    Boolean noIOSReset();

    @Key("app.ios.showLog")
    Boolean iosShowLog();

    @Key("app.ios.showXcodeLog")
    Boolean iosShowXcodeLog();

    @Key("app.webDriverWait.explicit")
    Integer webDriverWaitExplicit();

    @Key("app.mobile.executionPlatformName")
    String getExecutionPlatformEnvironment();

    @Key("app.targeted.executionEnvironment")
    String appTargetExecutionEnvironment();

    @Key("app.deprecatedAPI")
    String appDeprecatedAPI();

    @Key("app.browserStack.authorization")
    String appBrowserStackAuthorization();

    @Key("app.mobile.applicationType")
    String appMobileApplicationType();

    @Key("app.mobile.browserName")
    String appMobileBrowserType();

    @Key("app.browserStack.deviceName")
    String appBrowserStackDeviceName();

    @Key("app.browserStack.platformName")
    String appBrowserStackPlatformName();

    @Key("app.browserStack.platformVersion")
    String appBrowserStackPlatformVersion();

    @Key("app.mobile.browserName")
    String appMobileBrowserName();

    @Key("app.mobile.browserVersion")
    String appMobileBrowserVersion();

    @Key("PlatformNameAndVersion")
    String getDevicePlatformNameAndVersion();

    @Key("ScreenSize")
    String getDeviceScreenSize();

    @Key("DeviceName")
    String getDeviceName();

    @Key("app.android.appWaitActivity")
    String getAppAndroidAppWaitActivityName();

    @Key("app.web.delay_before_each_element")
    boolean isDelayBetweenElement();

    @Key("app.web.delay_duration_before_each_element")
    Integer appWebElementWaitBeforeDuration();

    @Key("app.web.highlight.element")
    boolean appWebElementIsHighlight();

    @Key("app.onFailure.captureScreenShot")
    boolean appOnFailureCaptureScreenShots();

}
