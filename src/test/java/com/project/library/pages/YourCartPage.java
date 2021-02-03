package com.project.library.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class YourCartPage extends AbstractPage{

    private final static By emptyCartButton = By.cssSelector("button.btn-danger");
    private final static By availableTableRecords = By.cssSelector("tbody>tr");

    public YourCartPage(WebDriver driver) {
        super(driver);
    }

    public void clickEmptyCart(){
        getDriver().findElement(emptyCartButton).click();
    }

    public Integer getNumberOfTableRows(){
        return getDriver().findElements(availableTableRecords).size();
    }

}
