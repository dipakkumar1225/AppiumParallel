package stepDefinitions.screens.login;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import testutilities.context.TestContext;

public class TestLoginStepDef {
    TestContext testContext;

    public TestLoginStepDef(TestContext testContext) {
        this.testContext = testContext;
    }

    @When("Enter incorrect or incomplete username {string}, {string} and submit")
    public void enterIncorrectOrIncompleteUsernameAndSubmit(String strUserName, String strPwd) {
        testContext.getPageObjectManager().getLogin().enterCredentialsAndSubmit(strUserName, strPwd);
    }

    @Then("Verify displayed error message {string}")
    public void verifyDisplayedErrorMessage(String strExpectedErrorMessage) {
        String strActualErrorMessage = testContext.getPageObjectManager().getLogin().getLoginErrorMessage();
        System.out.println("strActualErrorMessage " + strActualErrorMessage);
        System.out.println("strExpectedErrorMessage " + strExpectedErrorMessage);
    }
}
