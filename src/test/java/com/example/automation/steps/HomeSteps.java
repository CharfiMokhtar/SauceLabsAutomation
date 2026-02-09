package com.example.automation.steps;

import com.example.automation.configuration.ConfigReader;
import com.example.automation.configuration.DriverFactory;
import com.example.automation.pages.HomePage;
import com.example.automation.utils.Basetools;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertTrue;

public class HomeSteps {

    HomePage homePage = new HomePage();

    @Then("il est redirigé vers la page d'accueil")
    public void checkHome() {
        assertTrue(homePage.isDisplayed());
    }

    @Given("l'utilisateur ajoute le produit {string} au panier")
    public void ajouterAuPanier(String productName) {
        homePage.ajouterAuPanier(productName);
    }


    @When("il accede au panier")
    public void accessCart() {
        homePage.accessCart();
    }


    @Given("l'utilisateur trie les produits par prix {string}")
    public void sortByPrice(String ordre) {
        switch (ordre) {
            case "croissant" -> homePage.sortByPriceAsc();
            case "décroissant" -> homePage.sortByPriceDesc();
            default -> throw new IllegalArgumentException("Ordre inconnu: " + ordre);
        }
    }

    @Then("les produits sont triés par prix {string}")
    public void checkProductSorted(String ordre) {
        assertTrue(homePage.checkProductSorted(ordre));

    }

}
