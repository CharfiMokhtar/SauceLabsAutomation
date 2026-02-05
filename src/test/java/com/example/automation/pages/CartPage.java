package com.example.automation.pages;

import com.example.automation.utils.Basetools;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CartPage extends BasePage {

    @FindBy(id = "checkout")
    WebElement checkoutButton;

    public boolean checkProductIsInCart(String productName) {
        return Basetools.isElementDisplayed(
                driver.findElement(By.xpath(
                        "//div[@data-test='inventory-item-name' and contains(text(), '" + productName + "')]"))
        );
    }


    public void accessCheckout() {
        checkoutButton.click();
    }
}
