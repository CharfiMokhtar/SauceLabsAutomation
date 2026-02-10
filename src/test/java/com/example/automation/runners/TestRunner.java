package com.example.automation.runners;
import io.cucumber.java.Before;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "com.example.automation.steps",
                "com.example.automation.utils",
                "com.example.automation.configuration"
        },
        plugin = { "pretty", "html:target/cucumber-reports.html","json:target/cucumber.json" },
        monochrome = true
)
public class TestRunner {
    @BeforeClass
    public static void test() {
        System.out.println("\n\n\n\n" + System.getProperty("urlGrid") + "\n\n\n\n");
    }
}
