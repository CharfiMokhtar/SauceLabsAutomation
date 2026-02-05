package com.example.automation.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "com.example.automation.steps",
                "com.example.automation.utils",
                "com.example.automation.configuration"
        },
        tags = "not @Jira",
        plugin = { "pretty", "html:target/cucumber-reports.html","json:target/cucumber.json" },
        monochrome = true
)
public class TestRunner {

    /*@AfterClass
    public static void importXray() throws IOException, NoSuchAlgorithmException, KeyStoreException, InterruptedException, KeyManagementException {
        ImportXray.remonterResultatsXray();
    }*/
}
