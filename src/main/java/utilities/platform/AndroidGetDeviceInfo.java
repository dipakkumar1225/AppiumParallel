package utilities.platform;

import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.Miscellaneous;
import utilities.config.ConfigurationManager;
import utilities.config.ConfigurationProperties;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AndroidGetDeviceInfo {

    private static final ConfigurationProperties configurationProperties = ConfigurationManager.getConfiguration();
    private static final Runtime runtime = Runtime.getRuntime();
    private static BufferedReader stdInput;
    private static final Logger LOGGER = LogManager.getLogger(AndroidGetDeviceInfo.class.getName());

    private AndroidGetDeviceInfo() {
    }

    @SneakyThrows
    public static List<String> getPackageAndLauncherName() {
        Process process = runtime.exec(System.getProperty("user.dir") + File.separator + "\\lib\\ApkInfo\\AppInfo.bat " + configurationProperties.androidApkFilePath());
        stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        stdInput.readLine();
        Stream<String> lines = stdInput.lines().skip(1);
        return lines.sorted().collect(Collectors.toList());
    }

    public static String getConnectedDeviceNameOrUIID() {
        return Miscellaneous.executeCommand("adb get-serialno");
    }

    public static String getConnectedDeviceModeName() {
        return Miscellaneous.executeCommand("adb shell getprop ro.product.product.model");
    }

    public static String getConnectedDeviceResolution() {
        String strResolution = Miscellaneous.executeCommand("adb shell wm size");
        return strResolution.substring(strResolution.indexOf(":") + 1);
    }

    public static String getConnectedDevicePlatformVersion() {
        return Miscellaneous.executeCommand("adb shell getprop ro.build.version.release");
    }

}
