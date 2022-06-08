package utilities.miscellanious;

import io.appium.java_client.AppiumDriver;
import utilities.config.ConfigurationManager;
import utilities.config.ConfigurationProperties;
import utilities.platform.AndroidGetDeviceInfo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class WriteProperties {

    public static void writeProperties(AppiumDriver appiumDriver) {

        ConfigurationProperties configurationProperties = ConfigurationManager.getConfiguration();
        File directory = new File(System.getProperty("user.dir") + "\\src\\test\\resources\\classifications");
        if (!directory.exists()) {
            directory.mkdir();
        }

        File fileName = new File(directory + File.separator + "DevicesDetails.properties");

        try {

            String strDeviceUUID = configurationProperties.androidDeviceUDID().isEmpty() ? AndroidGetDeviceInfo.getConnectedDeviceModeName() : configurationProperties.androidDeviceUDID();
            String[] platformInfo = configurationProperties.androidPlatformAndVersion().split(" ");
            String strPlatformName = configurationProperties.androidPlatformAndVersion().isEmpty() ? "Android" : platformInfo.length == 1 ? platformInfo[0] : "Android";

            String strPlatformVersion = configurationProperties.androidPlatformAndVersion().isEmpty() ? AndroidGetDeviceInfo.getConnectedDevicePlatformVersion() : platformInfo.length == 2 ? platformInfo[1] : AndroidGetDeviceInfo.getConnectedDevicePlatformVersion();

            Properties prop = new Properties();
            prop.setProperty("DeviceName", appiumDriver.getCapabilities().getCapability("deviceManufacturer") + " " + strDeviceUUID);
            prop.setProperty("PlatformNameAndVersion", strPlatformName + " " + strPlatformVersion);
            prop.setProperty("ScreenSize", AndroidGetDeviceInfo.getConnectedDeviceResolution());
            FileWriter fileInput = new FileWriter(fileName, false);
            prop.store(fileInput, "Device properties added successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
