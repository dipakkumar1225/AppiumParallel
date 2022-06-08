package utilities.helperElement;

import org.openqa.selenium.WebElement;


import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class RadioButton {

    private RadioButton() {
    }

    public static void selectRadioButton(List<WebElement> mobileElements, String strRadioButtonName) {
        Predicate<WebElement> elementPredicate = WebElement -> WebElement.getAttribute("name").equalsIgnoreCase(strRadioButtonName) && WebElement.getAttribute("checked").equalsIgnoreCase("false");
        mobileElements.stream().filter(elementPredicate).findAny().ifPresent(WebElement::click);
    }


    public static void selectWebRadioButton(List<WebElement> webElements, String strRadioButtonName) {
        Predicate<WebElement> elementPredicate = webElement -> webElement.getText().equals(strRadioButtonName);
        webElements.stream().filter(elementPredicate).findAny().ifPresent(WebElement::click);
    }

    public static void selectRadioButton(WebElement webElement) {
        if (!webElement.isSelected()) {
            webElement.click();
        }
    }

    public static String getSelectedRadioButtonName(List<WebElement> WebElement) {
        WebElement mobileElementSelectedIDType = WebElement.stream().filter((element) -> element.getAttribute("checked").equalsIgnoreCase("true")).findFirst().orElse(null);
        return Objects.requireNonNull(mobileElementSelectedIDType).getAttribute("name");
    }

    public static String getSelectedWebRadioButtonName(List<WebElement> webElement) {
        WebElement webElementSelectedIDType = webElement.stream().filter((element) -> element.getAttribute("checked").equalsIgnoreCase("true")).findFirst().orElse(null);
        return Objects.requireNonNull(webElementSelectedIDType).getAttribute("name");
    }


}
