package utilities.helperElement;

import org.openqa.selenium.WebElement;


import java.util.Optional;

public class CheckBox {

    private CheckBox() {
    }


    public static void selectCheckBox(WebElement webElement) {
        if (!isCheckBoxSelected(webElement)) {
            webElement.click();
        }
    }

    public static void deSelectCheckBox(WebElement webElement) {
        boolean isSelected = webElement.isSelected();
        if (isSelected) {
            webElement.click();
        }
    }

    public static void checkUnCheckBox(WebElement webElement, String strCheckBoxArgs) {
        Optional.ofNullable(strCheckBoxArgs).ifPresent(chkBox -> {
            switch (chkBox.toLowerCase()) {
                case "yes", "true", "checked", "y" -> selectCheckBox(webElement);
                case "no", "false", "unchecked", "n", "na" -> deSelectCheckBox(webElement);
            }
        });
    }

       public static boolean isCheckBoxSelected(WebElement webElement) {
        return webElement.isSelected();
    }
}
