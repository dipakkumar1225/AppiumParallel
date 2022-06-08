package activitiesobject.screens.login;

import activitiesobject.BaseScreen;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;
import utilities.helperElement.TextField;

public class Login extends BaseScreen {
    public Login(AppiumDriver appiumDriver) {
        super(appiumDriver);
    }

    @AndroidFindBy(accessibility = "test-Username")
    private WebElement webElementTxtFieldUserName;

    @AndroidFindBy(accessibility = "test-Password")
    private WebElement webElementPwdFieldPassword;

    @AndroidFindBy(accessibility = "test-LOGIN")
    private WebElement webElementBtnLOGIN;

    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.view.ViewGroup\").description(\"test-Error message\")")
    private WebElement webElementTextErrorMessage;

    public void enterUserName(String strUserName) {
        TextField.clearAndInputText(webElementTxtFieldUserName, strUserName);
    }

    public void enterPassword(String strPwd) {
        TextField.clearAndInputText(webElementPwdFieldPassword, strPwd);
    }

    public void clickOnLoginButton() {
        webElementBtnLOGIN.click();
    }

    public String getLoginErrorMessage() {
        return isElementPresent(webElementTextErrorMessage) ? String.join("", webElementTextErrorMessage.getText().split("\n")) : null;
    }

    public void enterCredentialsAndSubmit(String strUserName, String strPwd) {
        enterUserName(strUserName);
        enterPassword(strPwd);
        clickOnLoginButton();
    }
}
