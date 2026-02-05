package com.example.automation.steps;

import com.example.automation.configuration.ConfigReader;
import com.example.automation.configuration.DriverFactory;
import com.example.automation.pages.LoginPage;
import com.example.automation.pages.HomePage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertTrue;

public class LoginSteps {

    LoginPage loginPage = new LoginPage();
    public ConfigReader settings = new ConfigReader();

    @Then("il est redirigé vers la page de connexion")
    public void checkHome() {
        assertTrue(loginPage.isDisplayed());
    }

    @Given("l'utilisateur est sur la page de connexion")
    public void openLogin() {
        String url = settings.getProperty("url");
        loginPage.open(url);
    }

    @When("il saisit le login {string} et le mot de passe {string}")
    public void login(String username, String password) {
        loginPage.login(username, password);
    }

    @Then("le message d'erreur s'affiche")
    public void checkErrorMessage() {
        assertTrue("Le message d'erreur devrait être affiché", loginPage.isErrorMessageDisplayed());
    }
}
