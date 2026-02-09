package com.example.automation.runners;

import com.example.automation.configuration.ManageXray;
import io.cucumber.java.BeforeAll;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "com.example.automation.steps",
                "com.example.automation.utils",
                "com.example.automation.configuration"
        },
        tags = "@POEI2-640",
        plugin = { "pretty", "html:target/cucumber-reports.html","json:target/cucumber.json" },
        monochrome = true
)
public class TestRunner {
    @BeforeClass
    public void test() {
        System.out.println(System.getProperty("\n\n\nSELENIUM_BROWSER\n\n\n"));
    }
}
