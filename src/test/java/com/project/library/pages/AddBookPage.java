package com.project.library.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AddBookPage extends AbstractPage {

    private final static By titleField = By.id("title");
    private final static By authorField = By.id("author");
    private final static By priceField = By.id("price");
    private final static By piecesFiled = By.id("pieces");
    private final static By uploadImage = By.id("customFile");
    private final static By addBookButton = By.xpath("//div/div/button");
    private final static By alertMessage = By.className("alert");
    private final static By uploadExcel = By.cssSelector("div.input-group>input[type = 'file']");
    private final static By addFileButton = By.cssSelector("div.input-group>button");
//    private final static By successExcel

    public AddBookPage(WebDriver driver) {
        super(driver);
    }

    public void completeForm(String title, String author, Double price, Integer pieces, String absoluteFilePath) {
        getDriver().findElement(titleField).sendKeys(title);
        getDriver().findElement(authorField).sendKeys(author);
        getDriver().findElement(priceField).sendKeys(price.toString());
        getDriver().findElement(piecesFiled).sendKeys(pieces.toString());
        getDriver().findElement(uploadImage).sendKeys(absoluteFilePath);
        clickAddBookButton();
    }

    private void clickAddBookButton() {
        getDriver().findElement(addBookButton).click();
    }

    public String getResponse() {
        getWait().until(ExpectedConditions.visibilityOfElementLocated(alertMessage));
        return getDriver().findElement(alertMessage).getText();
    }
}
