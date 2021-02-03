package com.project.library.Service;

import com.project.library.DAO.BookDAO;
import com.project.library.Enum.Status;
import com.project.library.Helper.ExcelHelper;
import com.project.library.Message.ResponseMessage;
import com.project.library.Model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDAO bookDAO;

    @Autowired
    private ExcelHelper excelHelper;

    @Autowired
    ResponseMessage responseMessage;

    @Override
    public List<Book> getBooks() {
        return (List<Book>) bookDAO.findAll();
    }

    @Override
    public Book getBookById(Integer id) {
        return bookDAO.findById(id).orElse(null);
    }

    @Override
    public Integer addBook(String title, String author, Integer pieces, Double price, MultipartFile image,
                           Status status) {
        responseMessage.setMessage("");
        Book newBook = new Book();
        if (bookDAO.findByTitle(title) != null) {
            responseMessage.setMessage("The book with the title \"" + title + "\" already exists!");
            return null;
        }
        newBook.setTitle(title);
        newBook.setAuthor(author);
        newBook.setPieces(pieces);
        newBook.setPrice(price);
        newBook.setStatus(status);
        try {
            if (image != null)
                newBook.setImage(image.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        bookDAO.save(newBook);
        responseMessage.setMessage("The book with the title \"" + title + "\" was added!");
        return newBook.getBookId();
    }

    @Override
    public Integer deleteBook(Integer bookId) {
        Book deletedBook = getBookById(bookId);
        if (deletedBook == null) {
            return null;
        }
        bookDAO.delete(deletedBook);
        return bookId;
    }

    @Override
    public byte[] getBookImage(Integer bookId) {
        Book book = getBookById(bookId);
        if (book == null) {
            return null;
        }
        return book.getImage();
    }

    @Override
    public Integer updateBook(Book updatedBook) {
        if (bookDAO.findByTitle(updatedBook.getTitle()) == null) {
            return null;
        }
        Book oldBook = bookDAO.findByTitle(updatedBook.getTitle());
        oldBook.setAuthor(updatedBook.getAuthor());
        oldBook.setPieces(updatedBook.getPieces());
        if (updatedBook.getPieces() == 0) {
            oldBook.setStatus(Status.SOLD);
        } else oldBook.setStatus(Status.AVAILABLE);
        oldBook.setPrice(updatedBook.getPrice());
        oldBook.setImage(updatedBook.getImage());
        bookDAO.save(oldBook);
        return oldBook.getBookId();
    }

    @Override
    public Book buyBook(Integer boughtBookId) {
        if (getBookById(boughtBookId) == null) {
            return null;
        }
        Book boughtBook = getBookById(boughtBookId);
        if (boughtBook.getPieces() == 1) {
            boughtBook.setStatus(Status.SOLD);
        } else if (boughtBook.getPieces() == 0) {
            return null;
        }
        boughtBook.setPieces(boughtBook.getPieces() - 1);
        bookDAO.save(boughtBook);
        return boughtBook;
    }

    @Override
    public Book borrowBook(Integer borrowedBookId) {
        if (getBookById(borrowedBookId) == null) {
            return null;
        }
        Book borrowedBook = getBookById(borrowedBookId);
        if (borrowedBook.getPieces() == 1) {
            borrowedBook.setStatus(Status.BORROWED);
        } else if (borrowedBook.getPieces() == 0) {
            return null;
        }
        borrowedBook.setPieces(borrowedBook.getPieces() - 1);
        bookDAO.save(borrowedBook);
        return borrowedBook;
    }

    @Override
    public Book returnBook(Integer returnedBookId) {
        if (getBookById(returnedBookId) == null) {
            return null;
        }
        Book returnedBook = getBookById(returnedBookId);
        returnedBook.setStatus(Status.AVAILABLE);
        returnedBook.setPieces(returnedBook.getPieces() + 1);
        bookDAO.save(returnedBook);
        return returnedBook;
    }

    @Override
    public List<Map<String, Object>> generateReport() {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (Book book : bookDAO.findAll()) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("id", book.getBookId());
            item.put("title", book.getTitle());
            item.put("price", book.getPrice());
            item.put("pieces", book.getPieces());
            item.put("author", book.getAuthor());
            result.add(item);
        }
        return result;
    }

    @Override
    public Map<Integer, Object[]> exportExcel() {
        Map<Integer, Object[]> data = new TreeMap<>();
        List<Book> bookList = getBooks();
        data.put(1, new Object[]{"TITLE", "AUTHOR", "PRICE", "PIECES", "STATUS"});
        for (int i = 0; i < bookList.size(); i++) {
            Book book = bookList.get(i);
            data.put(i + 2, new Object[]{book.getTitle(), book.getAuthor(), book.getPrice(), book.getPieces(), book.getStatus()});
        }
        return data;
    }

    @Override
    public void saveExcelDataToDB(MultipartFile file) {
        try {
            List<Book> books = excelHelper.excelToBooks(file.getInputStream());
            for (Book book : books) {
                if (bookDAO.findByTitle(book.getTitle()) == null) {
                    System.out.println("Add Book: " + book.getTitle());
                    bookDAO.save(book);
                } else {
                    System.out.println("Update Book: " + book.getTitle());
                    updateBook(book);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Fail to store excel data: " + e.getMessage());
        }
    }

    @Override
    public void updatePrice() {
        List<Book> books = getBooks();

        for (Book book : books) {
            if (book.getStatus() == Status.SOLD) {
                book.setPrice(book.getPrice() + 1);
                System.out.println("Updated price for book: " + book.getTitle() + " price: " + book.getPrice());
                bookDAO.save(book);
            }
        }
    }

}
