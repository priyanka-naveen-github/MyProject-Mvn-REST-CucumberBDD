package Runners;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "./src/test/resources/APIFeatureTests",
        glue = "stepDefinitions",
        plugin = {"html:target/cucumber-html-report", "json:target/cucumber-json-reports/CucumberTestReport.json",
                "pretty:target/cucumber-pretty.txt",
                "usage:target/cucumber-usage.json"},
        monochrome = true
        //strict = false
)

public class TestRunner {

}
