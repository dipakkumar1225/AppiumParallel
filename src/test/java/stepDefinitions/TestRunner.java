package stepDefinitions;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        monochrome = true,
        glue = "stepDefinitions",
        tags = "@RegressionOnUAT",
        dryRun = false,
        plugin = {"summary",
                "pretty",
                "utilities.listener.CucumberListener",
                "html:target/cucumber-reports/advanced-reports.html",
                "json:target/cucumber-reports/CucumberTestReport.json",
        },
        snippets = CucumberOptions.SnippetType.CAMELCASE,
        features = {
                "classpath:features/Login.feature",
        }
)
public class TestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
