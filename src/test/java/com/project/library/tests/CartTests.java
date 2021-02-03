package com.project.library.tests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CartTests extends BaseTests{

    @Test
    public void testEmptyButton(){
        homePage.clickAddToCart();
        homePage.alert_clickToAccept();
        var cartPage = homePage.clickYourCart();
        cartPage.clickEmptyCart();
        assertEquals(0, cartPage.getNumberOfTableRows(), "Number of rows incorrect");
    }
}
