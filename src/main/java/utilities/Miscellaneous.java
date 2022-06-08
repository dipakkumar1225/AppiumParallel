package utilities;

import io.appium.java_client.AppiumDriver;
import io.cucumber.core.backend.TestCaseState;
import io.cucumber.java.Scenario;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import utilities.listener.CucumberListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Miscellaneous {
    private static final Logger LOGGER = LogManager.getLogger(Miscellaneous.class.getName());

    @SneakyThrows
    public static String executeCommand(String command) {
        Process process = Runtime.getRuntime().exec(command);
        String strValue = "";
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String strLines = "";
        while ((strLines = stdInput.readLine()) != null) {
            strValue = strLines;
        }
        if (process.exitValue() != 0) {
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String strConnectedDeviceNameError;
            while ((strConnectedDeviceNameError = stdError.readLine()) != null) {
                System.out.println("Error: " + strConnectedDeviceNameError + "\n" + command);
            }
            System.exit(0);
        }
        process.destroy();
        return strValue;
    }

    public static void setBrowserStackPassStatus(AppiumDriver appiumDriver, String strMessage) {
        ((JavascriptExecutor) appiumDriver).executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"" + strMessage + "\"}}");
    }

    public static void setBrowserStackFailStatus(AppiumDriver appiumDriver, String strMessage) {
        ((JavascriptExecutor) appiumDriver).executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"" + strMessage + "\"}}");
    }

    public static void setBrowserStackSetSessionName(AppiumDriver appiumDriver, String strTestName) {
        ((JavascriptExecutor) appiumDriver).executeScript("browserstack_executor: {\"action\": \"setSessionName\", \"arguments\": {\"name\":\"" + strTestName + "\"}}");
    }

    public static void setBrowserStackTestLogType(AppiumDriver appiumDriver, String strData, String strLevel) {
        ((JavascriptExecutor) appiumDriver).executeScript("browserstack_executor: {\"action\": \"annotate\", \"arguments\": {\"data\": \"" + strData + "\", \"level\": \"" + strLevel + "\"}}");
    }

}
