package co.istad.dao;

import co.istad.model.*;

import java.util.List;
import java.util.Optional;

public interface LibrarianDao extends MainFeatureDao {
    public Category createCategory(Category category);
    public Category updateCategoryById(Long id, Category category);
    public Book createBook(Book book);
    public Book updateBookById(Long id, Book book);
    public Boolean confirmBorrow(Borrow borrow);
    public Boolean returnBook(User user, Book book);
    public List<Book> bookReport();
    public List<User> userReport();
    public Author createAuthor(Author author);
    public List<Author> getAll();
    public Author searchAuthorById(Long id);
    public Author updateAuthorById(Long id, Author author);
    public Author deleteById(Long id);
    public List<Author> searchAuthorByName(String authorName);
    public List<Author> authorPagination( int page, int limit );
    public Optional<Book> searchBookByTitle(String title );
    public List<Book> searchBooksByTitle( String title );
    public List<Book> bookPagination(int page, int limit);
    public List<Borrow> borrowPagination(int page, int limit);
}
