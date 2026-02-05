package com.example.automation.steps;

import com.example.automation.configuration.ConfigReader;
import com.example.automation.configuration.DriverFactory;
import com.example.automation.pages.CartPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertTrue;

public class CartStep {

    CartPage cartPage = new CartPage();
    public ConfigReader settings = new ConfigReader();


    @Then("le produit {string} est dans le panier")
    public void checkProductIsInCart(String productName) {
        assertTrue(cartPage.checkProductIsInCart(productName));
    }

    @Given("l'utilisateur il accede au checkout")
    public void accessCheckout() {
        cartPage.accessCheckout();
    }
}
