package com.project.library.tests;

import com.project.library.pages.HomePage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTests {

    private WebDriver driver;
    protected HomePage homePage;

    @BeforeAll
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:/Program Files/chromedriver.exe");
        driver = new ChromeDriver();
        goHome();
    }

    @BeforeEach
    public void goHome() {
        driver.get("http://localhost:4200/books");
        homePage = new HomePage(driver);
    }

    @AfterAll
    public void quitWebDriver() {
        driver.quit();
    }

}
