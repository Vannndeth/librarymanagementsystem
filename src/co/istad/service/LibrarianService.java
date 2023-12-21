package co.istad.service;

import co.istad.model.*;

import java.util.List;

public interface LibrarianService extends MainFeatureService {
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
    public List<Author> searchAuthorByName( String authName );
    public Author deleteById(Long id);
    public List<Author> authorPagination( int page, int limit );
    public List<Book> searchBooksByTitle(String title);
    public void backup();
    public List<Book> bookPagination( int page, int limit );
    public List<User> getReport();
}
