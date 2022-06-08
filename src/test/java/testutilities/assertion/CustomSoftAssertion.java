package testutilities.assertion;

import io.cucumber.java.Scenario;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.OutputType;
import stepDefinitions.hooks.InitDriver;
import utilities.image.ImageUtilities;

public final class CustomSoftAssertion extends SoftAssertions {

    private final Scenario scenario;

    public CustomSoftAssertion(Scenario scenario) {
        this.scenario = scenario;
    }

    @Override
    public void onAssertionErrorCollected(AssertionError assertionError) {
        byte[] screenshot = InitDriver.appiumDriver.getScreenshotAs(OutputType.BYTES);
        scenario.attach(ImageUtilities.resizeBytesImage(screenshot), "image/png", scenario.getName());
    }
}
