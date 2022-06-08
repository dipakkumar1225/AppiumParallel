package utilities.helperElement;

import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SelectDropDown {

    private SelectDropDown() {
    }

    public static List<String> getDropDownList(List<WebElement> webElements) {
        return webElements.stream().parallel().map(WebElement -> String.join(" ", WebElement.getAttribute("name").split("\n"))).collect(Collectors.toList());
    }

    public static void selectDropDownByValue(List<WebElement> webElements, String strDropDownValue) {
        if (Objects.nonNull(strDropDownValue)) {
            Predicate<WebElement> elementPredicate = element -> element.getAttribute("name").contains(strDropDownValue) || String.join(" ", element.getAttribute("content-desc").split("\n")).contains(strDropDownValue);
            webElements.stream().filter(elementPredicate).findAny().ifPresent(WebElement::click);
        }
    }

    public static void selectDropDownByValueEquals(List<WebElement> webElements, String strDropDownValue) {
        if (Objects.nonNull(strDropDownValue)) {
            Predicate<WebElement> elementPredicate = element -> element.getText().trim().equals(strDropDownValue);
            webElements.stream().filter(elementPredicate).findAny().ifPresent(WebElement::click);
        }
    }

    public static void selectDropDownByIndex(List<WebElement> webElements, int index) {
        if (webElements.size() != 0) {
            webElements.get(index).click();
        }
    }

    public static void selectDropDownByIndex(List<WebElement> webElements) {
        if (webElements.size() != 0) {
            webElements.get(new Random().nextInt(webElements.size())).click();
        }
    }

    public static void selectDropDownByValue(WebElement webElement, String strDropDownValue) {
        Select select = new Select(webElement);
        select.selectByValue(strDropDownValue);
    }

    public static void selectDropDownByVisibleText(WebElement webElement, String strVisibleText) {
        Select select = new Select(webElement);
        select.selectByVisibleText(strVisibleText);
    }

    public static void selectDropDownByIndex(WebElement webElement) {
        Select select = new Select(webElement);
        Random random = new Random();
        int randIndex = random.nextInt(select.getOptions().size());
        select.selectByIndex(randIndex);
    }

    public static List<String> getWebDropDownList(List<WebElement> webElements) {
        return webElements.stream().parallel().map(WebElement::getText).collect(Collectors.toList());
    }

    public static void selectWebDropDownByValue(List<WebElement> webElements, String strDropDownValue) {
        if (Objects.nonNull(strDropDownValue)) {
            Predicate<WebElement> elementPredicate = element -> element.getText().contains(strDropDownValue);
            webElements.stream().filter(elementPredicate).findAny().ifPresent(WebElement::click);
        }
    }

    public static void selectWebDropDownByValueEquals(List<WebElement> webElements, String strDropDownValue) {
        if (Objects.nonNull(strDropDownValue)) {
            Predicate<WebElement> elementPredicate = element -> element.getText().trim().equals(strDropDownValue);
            webElements.stream().filter(elementPredicate).findAny().ifPresent(WebElement::click);
        }
    }

    public static void selectWebDropDownByIndex(List<WebElement> webElements, int index) {
        if (webElements.size() != 0) {
            webElements.get(index).click();
        }
    }

    public static void selectWebDropDownByIndex(List<WebElement> webElements) {
        if (webElements.size() != 0) {
            webElements.get(new Random().nextInt(webElements.size())).click();
        }
    }
}
