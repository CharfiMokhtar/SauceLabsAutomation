package com.example.automation.pages;

import com.example.automation.utils.Basetools;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends BasePage {

    @FindBy(xpath = "//a[@data-test='shopping-cart-link']")
    WebElement openCartLink;
    @FindBy(xpath = "//select[@data-test = 'product-sort-container']")
    WebElement productSort;
    @FindBy(xpath = "//div[@data-test = 'inventory-item-price']")
    List<WebElement> inventoryItemPrices;

    public boolean isDisplayed() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.urlContains("inventory"));
    }

    public void ajouterAuPanier(String productName) {
        driver.findElement(By.xpath(
                "//div[@data-test='inventory-item-name' and contains(text(), '" + productName + "')]//ancestor::div[@data-test='inventory-item-description']//button[contains(@data-test, 'add-to-cart')]"
        )).click();
    }

    public void accessCart() {
        openCartLink.click();
    }

    public void sortByPriceAsc() {
        Select productSortSelect = new Select(productSort);
        productSortSelect.selectByValue("lohi");
    }

    public void sortByPriceDesc() {
        Select productSortSelect = new Select(productSort);
        productSortSelect.selectByValue("hilo");
    }

    public boolean checkProductSorted(String ordre) {
        List<Double> itemPrices = new ArrayList<>();
        for(WebElement e : inventoryItemPrices) {
            itemPrices.add(Double.parseDouble(e.getText().replace("$", "")));
        }

        return ordre.equals("croissant") ? Basetools.isAsc(itemPrices) : Basetools.isDesc(itemPrices);
    }

}
