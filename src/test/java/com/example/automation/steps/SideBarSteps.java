package com.example.automation.steps;

import com.example.automation.configuration.DriverFactory;
import com.example.automation.pages.SideBarPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;

public class SideBarSteps {

    SideBarPage sideBarPage = new SideBarPage();

    @Given("l'utilisateur ouvre la sidebar")
    public void openSideBar() {
        sideBarPage.openSideBar();
    }

    @When("il se déconnecte")
    public void logout() {
        sideBarPage.logout();
    }
}
