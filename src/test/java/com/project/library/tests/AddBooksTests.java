package com.project.library.tests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddBooksTests extends BaseTests{

    @Test
    public void testSuccessAddBook(){
        var addBookPage = homePage.clickAddBook();
        addBookPage.completeForm("ther new book", "Author", 23.0,8, "D:/Images/Aventurile lui Tom.png");
        String response = addBookPage.getResponse();
        assertTrue(response.contains("Well done!"));
    }

    @Test
    public void testFailBookExists(){
        var addBookPage = homePage.clickAddBook();
        addBookPage.completeForm("Another new book", "Author", 23.0,8, "D:/Images/Aventurile lui Tom.png");
        String response = addBookPage.getResponse();
        assertTrue(response.contains("Uups!"));
    }
}
