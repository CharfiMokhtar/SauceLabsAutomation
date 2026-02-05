package com.example.automation.pages;

import com.example.automation.configuration.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    @FindBy(xpath = "//input[@type = 'submit']")
    WebElement submitButton;

    public BasePage() {
        this.driver = DriverFactory.getDriver();;
        PageFactory.initElements(driver, this);
    }

    public void submitForm() {
        submitButton.click();
    }
}
