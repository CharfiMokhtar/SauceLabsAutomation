package com.example.automation.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SideBarPage extends BasePage {

    @FindBy(id = "logout_sidebar_link")
    WebElement logoutLink;
    @FindBy(id = "react-burger-menu-btn")
    WebElement burgerMenuLink;

    public void openSideBar() {
        burgerMenuLink.click();
    }

    public void logout() {
        logoutLink.click();
    }
}
