package utilities.miscellanious;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ConfigReader {

    private Properties properties = null;

    public ConfigReader(String strConfigFileName) {
        try {
            FileInputStream fileInputStream = new FileInputStream(strConfigFileName);
            properties = new Properties();
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public Map<String, String> getProperties() {
        Map<String, String> mapProperties = new HashMap<>();

        for (Object key : properties.stringPropertyNames()) {
            String strKey = key.toString();
            if (strKey.equalsIgnoreCase("DESIRED_CAPABILITIES_FILEPATH")) {
                continue;
            }
            if (strKey.equalsIgnoreCase("ENVIRONMENT")) {
                continue;
            }
            mapProperties.put(strKey, properties.getProperty(strKey));
        }
        return mapProperties;
    }

    public Map<String, String> getDesiredCapabilities() {
        Map<String, String> mapDesiredCapabilities = new HashMap<>();
        File fileDir = new File(properties.getProperty("DESIRED_CAPABILITIES_FILEPATH"));
        if (fileDir.isDirectory()) {
            File fileName = new File(fileDir + File.separator + properties.getProperty("ENVIRONMENT"));
            if (fileName.isFile() && fileName.exists()) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(fileName);
                    properties = new Properties();
                    properties.load(fileInputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                properties.stringPropertyNames().forEach(k -> {
                    mapDesiredCapabilities.put(k, properties.getProperty(k));
                });
            }
        }
        return mapDesiredCapabilities;
    }

    public Map<String, Properties> getMultiDesiredCapabilities() {
        List<String> environmentList = new ArrayList<>(Arrays.asList(properties.getProperty("DEVICES").split(",")));
        Map<String, Properties> multiMapDesiredCapabilities = new HashMap<>();
        int envSize = environmentList.size();
        File fileDir = new File(properties.getProperty("DESIRED_CAPABILITIES_FILEPATH"));
        for (int i = 0; i < envSize; i++) {
            File fileName = new File(fileDir + File.separator + environmentList.get(i));
            try {
                FileInputStream fileInputStream = new FileInputStream(fileName);
                properties = new Properties();
                properties.load(fileInputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
           multiMapDesiredCapabilities.put(environmentList.get(i), properties);
        }
        /*multiMapDesiredCapabilities.entrySet().forEach(a->{
            System.out.println(a.getKey() + "\t\t" + a.getValue());
        });*/
        return multiMapDesiredCapabilities;
    }
}
