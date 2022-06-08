package utilities.platform;

import utilities.enums.DriverType;
import utilities.platform.mobileweb.AndroidWebAppManager;
import utilities.platform.nativeapp.AndroidNativeAppManager;
import utilities.platform.nativeapp.IOSManager;

public class GenericDriverFactory {

    public static GenericDriverManager getMobileDriverManager(DriverType mobilePlatform) {
        GenericDriverManager mobileDriverManager;
        switch (mobilePlatform) {
            case ANDROID -> mobileDriverManager = new AndroidNativeAppManager();
            case ANDROID_WEB -> mobileDriverManager = new AndroidWebAppManager();
            case IOS -> mobileDriverManager = new IOSManager();
            default -> throw new IllegalArgumentException("Provided platform \"" + mobilePlatform + "\" is not supported....");
        }
        return mobileDriverManager;
    }
}
