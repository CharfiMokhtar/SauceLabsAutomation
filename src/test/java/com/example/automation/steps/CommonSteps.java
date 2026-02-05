package com.example.automation.steps;

import com.example.automation.configuration.ConfigReader;
import com.example.automation.configuration.DriverFactory;
import com.example.automation.pages.BasePage;
import io.cucumber.java.en.And;
import org.openqa.selenium.WebDriver;

public class CommonSteps {

    BasePage basePage = new BasePage();
    public ConfigReader settings = new ConfigReader();

    @And("il valide la saisie")
    public void submitForm() {
        basePage.submitForm();
    }
}
