package com.project.library.tests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HomePageTests extends BaseTests {

    @Test
    public void testBorrowButton() {
        homePage.clickBorrow();
        String text = homePage.alert_getText();
        assertEquals("Borrowed book!", text, "Alert Borrowed book text incorrect");
        homePage.alert_clickToAccept();
    }

    @Test
    void testAddToCartButton() {
        homePage.clickAddToCart();
        assertEquals("The book has been added to the cart!", homePage.alert_getText(), "Alert Bought book incorrect");
        homePage.alert_clickToAccept();
    }
}
