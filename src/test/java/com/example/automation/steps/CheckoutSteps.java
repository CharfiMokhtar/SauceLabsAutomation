package com.example.automation.steps;

import com.example.automation.configuration.ConfigReader;
import com.example.automation.configuration.DriverFactory;
import com.example.automation.pages.CartPage;
import com.example.automation.pages.CheckoutPage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;

import java.util.Map;

public class CheckoutSteps {

    CheckoutPage checkoutPage = new CheckoutPage();

    @When("il saisit ses informations:")
    public void fillUserInformation(DataTable dataTable) {
        Map<String, String> userInfo = dataTable.asMaps().get(0);

        checkoutPage.fillUserInformation(
                userInfo.get("firstname"),
                userInfo.get("lastname"),
                userInfo.get("postalcode")
        );
    }


    @And("il termine le checkout")
    public void finishCheckout() {
        checkoutPage.finishCheckout();
    }


    @Then("le message thank you for your order s'affiche")
    public void checkCheckoutFinished() {
        checkoutPage.checkCheckoutFinished();
    }



}
