package com.example.automation.pages;

import com.example.automation.utils.Basetools;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CheckoutPage extends BasePage {

    @FindBy(id = "first-name")
    WebElement firstnameInput;
    @FindBy(id = "last-name")
    WebElement lastnameInput;
    @FindBy(id = "postal-code")
    WebElement postalcodeInput;
    @FindBy(id = "finish")
    WebElement finishButton;
    @FindBy(xpath = "//h2[@data-test = 'complete-header' and contains(text(), 'Thank you')]")
    WebElement endCheckoutMessage;

    public void fillUserInformation(String firstname, String lastname, String postalcode) {
        firstnameInput.sendKeys(firstname);
        lastnameInput.sendKeys(lastname);
        postalcodeInput.sendKeys(postalcode);
    }

    public void finishCheckout() {
        finishButton.click();
    }

    public boolean checkCheckoutFinished() {
        return Basetools.isElementDisplayed(endCheckoutMessage);
    }
}
