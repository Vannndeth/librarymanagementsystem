package co.istad.dao;

import co.istad.model.Author;
import co.istad.model.Book;
import co.istad.model.Category;
import co.istad.model.User;

import java.util.List;

public interface LibrarianDao extends MainFeatureDao {
    public Category createCategory(Category category);
    public Category updateCategoryById(Long id, Category category);
    public Book createBook(Book book);
    public Book updateBookById(Long id, Book book);
    public Boolean confirmBorrow(User user, Book book);
    public Boolean returnBook(User user, Book book);
    public List<Book> bookReport();
    public List<User> userReport();
    public Author createAuthor(Author author);
    public List<Author> getAll();
    public Author searchAuthorById(Long id);
    public Author updateAuthorById(Long id, Author author);
    public Author deleteById(Long id);
}
