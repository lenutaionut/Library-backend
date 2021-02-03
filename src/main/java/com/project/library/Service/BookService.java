package com.project.library.Service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.library.Enum.Status;
import com.project.library.Model.Book;

import net.sf.jasperreports.engine.JRException;

@Service
public interface BookService {

    /**
     * get all book
     *
     * @return list of books
     */
    List<Book> getBooks();

    /**
     * get book with that id
     *
     * @param id
     * @return book
     */
    Book getBookById(Integer id);

    /**
     * Save a new book
     *
     * @param title
     * @param author
     * @param pieces
     * @param price
     * @param image
     * @param status
     * @return The new book's Id
     */
    Integer addBook(String title, String author, Integer pieces, Double price, MultipartFile image,
                           Status status);

    /**
     * Delete book with id {@param bookId}
     *
     * @param bookId
     * @return deleted book id
     */
    Integer deleteBook(Integer bookId);

    /**
     * Retrieve only the image for the book
     *
     * @param bookId
     * @return the image as a byte[] or null if doesn't exist
     */
    byte[] getBookImage(Integer bookId);

    /**
     * update the book
     *
     * @param updatedBook
     * @return the id of the updated book or {@literal null} if the update fails
     */
    Integer updateBook(Book updatedBook);

    /**
     * Update the status and number of books
     *
     * @param boughtBookId
     * @return the bought book
     */
    Book buyBook(Integer boughtBookId);

    /**
     * Update the status of the borrowed book
     *
     * @param borrowedBookId
     * @return the id of the book
     */
    Book borrowBook(Integer borrowedBookId);

    /**
     * @param returnedBookId
     * @return
     */
    Book returnBook(Integer returnedBookId);

    List<Map<String, Object>> generateReport() throws JRException, FileNotFoundException;

    Map<Integer, Object[]> exportExcel();

    void saveExcelDataToDB(MultipartFile file);

    void updatePrice();
}
