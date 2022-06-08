package activitiesobject;

import activitiesobject.screens.login.Login;
import io.appium.java_client.AppiumDriver;

public class PageObjectManager {

    private AppiumDriver appiumDriver;

    public PageObjectManager(AppiumDriver appiumDriver) {
        this.appiumDriver = appiumDriver;
    }

    private Login login;

    public Login getLogin() {
        return (login == null) ? login = new Login(appiumDriver) : login;
    }
}
