package utilities.helperElement;

import org.openqa.selenium.WebElement;
import org.apache.commons.validator.routines.EmailValidator;
import org.openqa.selenium.Keys;

import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.interactions.Actions;

import java.util.Objects;
import java.util.Optional;

public class TextField  {

    public TextField() {
    }

    public static void clearTextAndEnter(WebElement webElement, String strTxtField) {
        webElement.clear();
        Optional<String> stringOptional = Optional.ofNullable(strTxtField);
        if (stringOptional.isPresent()) {
            if (strTxtField.startsWith("\"") && strTxtField.endsWith("\"")){
                String newStr = strTxtField.replaceAll("^\"|\"$", "");
                webElement.sendKeys(newStr);
            }else {
                webElement.sendKeys(strTxtField);
            }
        }
    }

    //<editor-fold desc="Clear textField value if any, before entering value into text field">
    public static void clearAndInputText(WebElement webElement, String inputTxt) {
        clearTextValue(webElement);
        if (!Objects.isNull(inputTxt)) {
            if (inputTxt.startsWith("\"") && inputTxt.endsWith("\"")) {
                String strData = inputTxt.replaceAll("^\"|\"$", "");
                webElement.sendKeys(strData);
            } else {
                webElement.sendKeys(inputTxt);
            }
        }
    }
    //</editor-fold>

    public static String getTextValue(WebElement WebElement) {
        return WebElement.getAttribute("name");
    }

    public static void enterTextAndClear(WebElement WebElement, String strTxtField) {
        clearTextAndEnter(WebElement, strTxtField);
        WebElement.clear();
    }

    // <editor-fold desc="Clear input text value.">
    public static void clearTextValue(WebElement webElement) {
        webElement.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
    }
    //</editor-fold

    public static void sendArbitraryText(WebElement webElement, String strTxtField) {
        Actions keyActions = new Actions(((WrapsDriver) webElement).getWrappedDriver());
        webElement.clear();
        webElement.click();
        keyActions.sendKeys(strTxtField);
        keyActions.perform();
    }

    //<editor-fold desc="Clear textField value if any, enter value into text field and key actions.">
    public static void clearAndInputTextWithKeyAction(WebElement webElement, String inputTxt, Keys keys) {
        clearTextValue(webElement);
        if (!Objects.isNull(inputTxt)) {
            webElement.sendKeys(inputTxt.trim());
            webElement.sendKeys(keys);
        }
    }
    //</editor-fold>

    //<editor-fold desc="Get value of input type text.">
    public static String getTextValue(WebElement webElement, String attribute) {
        String strText = webElement.getAttribute(attribute);
        if (strText.length() > 0) {
            return strText;
        } else {
            return "";
        }
    }
    //</editor-fold>

    //<editor-fold desc="Check entered email address is valid">
    public static boolean isValidEmailAddress(WebElement webElement) {
        String enteredEmailAddress = getTextValue(webElement, "value");
        boolean isValidEmail = false;
        if (!enteredEmailAddress.isEmpty()) {
            EmailValidator emailValidator = EmailValidator.getInstance();
            isValidEmail = emailValidator.isValid(enteredEmailAddress);
        }
        return isValidEmail;
    }
    //</editor-fold>
}
