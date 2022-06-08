package utilities;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import utilities.config.ConfigurationManager;
import utilities.config.ConfigurationProperties;
import utilities.platform.nativeapp.AndroidNativeAppManager;

import java.io.File;

public class HttpClientHelper {
    static final Logger LOGGER = LogManager.getLogger(AndroidNativeAppManager.class.getName());
    static ConfigurationProperties configurationProperties = ConfigurationManager.getConfiguration();

    public static String uploadAppOnBrowserStack() {
        String filePath = "/" + new File(configurationProperties.androidApkFilePath()).getAbsolutePath().replaceAll("\\\\", "/");
        Unirest.setTimeouts(0, 0);
        LOGGER.info("APP UPLOAD STARTED ON BROWSERSTACK CLOUD");
        HttpResponse<String> response;
        try {
            response = Unirest.post("https://api-cloud.browserstack.com/app-automate/upload")
                    .header("Authorization", "Basic " + configurationProperties.appBrowserStackAuthorization())
                    .field("file", new File(filePath))
                    .asString();
        } catch (UnirestException exception) {
            LOGGER.error(exception);
            throw new RuntimeException(exception);
        }
        LOGGER.info("APP SUCCESSFULLY UPLOADED ON BROWSERSTACK CLOUD");
        JSONObject jsonObject = new JSONObject(response.getBody());
        return jsonObject.getString("app_url");
    }
}
