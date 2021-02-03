package com.project.library.DAO;

import com.project.library.Model.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookDAO extends CrudRepository<Book, Integer> {
    Book findByTitle(String title);
}
