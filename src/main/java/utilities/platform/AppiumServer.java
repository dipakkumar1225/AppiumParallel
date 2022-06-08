package utilities.platform;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.Miscellaneous;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class AppiumServer {
    private AppiumDriverLocalService APPIUM_SERVICE;
    private final int DEFAULT_APPIUM_SERVER_PORT = 4723;
    private static final Logger LOGGER = LogManager.getLogger(AppiumServer.class.getName());

    @SneakyThrows
    public void startServer(@Nullable String strAppiumServerIPAddress, @Nullable Integer portNumber) {
        if (!checkIsServerRunning(DEFAULT_APPIUM_SERVER_PORT)) {
            LOGGER.info("APPIUM SERVER STARTING");
            if (Objects.isNull(strAppiumServerIPAddress) && Objects.isNull(portNumber)) {
                APPIUM_SERVICE = new AppiumServiceBuilder()
                        .withIPAddress("127.0.0.1")
                        .withArgument(GeneralServerFlag.BASEPATH, "wd/hub/")
                        .usingPort(DEFAULT_APPIUM_SERVER_PORT)
                        .withArgument(GeneralServerFlag.RELAXED_SECURITY)
                        .withLogFile(new File("AppiumServerLog_" + DateTimeFormatter.ofPattern("dd_MMM_yyyy hh_mm_ss").format(LocalDateTime.now()) + ".txt"))
                        .build();
            } else {
                APPIUM_SERVICE = new AppiumServiceBuilder()
                        .withIPAddress(strAppiumServerIPAddress)
                        .withArgument(GeneralServerFlag.BASEPATH, "wd/hub/")
                        .usingPort(portNumber)
                        .withArgument(GeneralServerFlag.RELAXED_SECURITY)
                        .withLogFile(new File("AppiumServerLog_" + DateTimeFormatter.ofPattern("dd_MMM_yyyy hh_mm_ss").format(LocalDateTime.now()) + ".txt"))
                        .build();
            }
            APPIUM_SERVICE.clearOutPutStreams();
            APPIUM_SERVICE.start();
            APPIUM_SERVICE.enableDefaultSlf4jLoggingOfOutputData();

            String appiumServiceUrl = APPIUM_SERVICE.getUrl().toString();
            LOGGER.info("APPIUM SERVER STARTED ON ADDRESS : " + appiumServiceUrl);
        }
        else {
            LOGGER.info("APPIUM SERVER IS ALREADY RUNNING");
        }
    }

    private boolean checkIsServerRunning(int port) {
        boolean isServerRunning = false;
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.close();
        } catch (IOException e) {
            isServerRunning = true;
        } finally {
            serverSocket = null;
        }
        return isServerRunning;
    }

    public void stopServer() {
        LOGGER.info("APPIUM SERVER SERVICE STOPPING");
        APPIUM_SERVICE.stop();
        LOGGER.info("APPIUM SERVER SERVICE STOPPED");
        if (checkIsServerRunning(DEFAULT_APPIUM_SERVER_PORT)) {
            stopByKillingNode();
        }
    }

    @SneakyThrows
    private void stopByKillingNode() {
        String killNode = Miscellaneous.executeCommand("taskkill /F /IM node.exe");
        LOGGER.info("PROCESS \"NODE.EXE\" KILLED : " + killNode);
    }
}