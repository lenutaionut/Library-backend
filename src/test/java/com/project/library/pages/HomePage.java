package com.project.library.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.concurrent.TimeUnit;

public class HomePage extends AbstractPage {

    private final static By firstAddButton = By.xpath("(//button[contains(@class, 'btn-outline-danger')])[2]");
    private final static By firstBorrowButton = By.xpath("(//button[contains(@class, 'btn-outline-primary')])[2]");
    private final static By yourCartButton = By.id("cart");
    private final static By addBookButton = By.id("add");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void clickBorrow() {
        getWait().until(ExpectedConditions.visibilityOfElementLocated(firstBorrowButton));
        getDriver().findElement(firstBorrowButton).click();
    }

    public void clickAddToCart() {
        getWait().until(ExpectedConditions.visibilityOfElementLocated(firstAddButton));
        getDriver().findElement(firstAddButton).click();
    }

    public YourCartPage clickYourCart() {
        getWait().until(ExpectedConditions.visibilityOfElementLocated(yourCartButton));
        getDriver().findElement(yourCartButton).click();
        return new YourCartPage(getDriver());
    }

    public AddBookPage clickAddBook() {
        getDriver().findElement(addBookButton).click();
        return new AddBookPage(getDriver());
    }

    public void alert_clickToAccept() {
        getDriver().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        getDriver().switchTo().alert().accept();
    }

    public String alert_getText() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return getDriver().switchTo().alert().getText();
    }

}
