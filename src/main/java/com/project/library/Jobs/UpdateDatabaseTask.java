package com.project.library.Jobs;

import com.project.library.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UpdateDatabaseTask {

    @Autowired
    BookService bookService;

    @Scheduled(cron = "*/3 * * * * *")
    public void updatePrice() {
        bookService.updatePrice();
    }
}
