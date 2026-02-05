package com.example.automation.configuration;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.io.File;
import java.io.IOException;

public class Hooks {

    @Before
    public void setUp() {
        DriverFactory.getDriver();
    }

    @After(order = 1)
    public void takeScreenshot(Scenario scenario) throws IOException {
        if (scenario.isFailed()) {
            TakesScreenshot ts = (TakesScreenshot) DriverFactory.getDriver();
            File source = ts.getScreenshotAs(OutputType.FILE);
            File destination = new File("target/screenshots/capture" + scenario.getName() + ".png");
            FileUtils.copyFile(source, destination);
            byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Failure Screenshot");
        }

    }

    @After(order = 0)
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
