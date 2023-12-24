package co.istad.service;

import co.istad.connection.ConnectionDb;
import co.istad.dao.LibrarianDao;
import co.istad.dao.LibrarianDaoImpl;
import co.istad.model.*;
import co.istad.util.Singleton;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class LibrarianServiceImpl implements LibrarianService{
    private final LibrarianDao librarianDao;
    public LibrarianServiceImpl(){
        this.librarianDao = Singleton.getLibrarianDao();
    }
    @Override
    public Category createCategory(Category category) {
        return null;
    }

    @Override
    public Category updateCategoryById(Long id, Category category) {
        return null;
    }

    @Override
    public Category searchCategoryById(Long id) {
        return librarianDao.searchCategoryById(id);
    }

    @Override
    public List<Category> getAllCategories() {
        return librarianDao.getAllCategories();
    }

    @Override
    public Book createBook(Book book) {
        return librarianDao.createBook(book);
    }

    @Override
    public Book updateBookById(Long id, Book book) {
        return librarianDao.updateBookById(id, book);
    }

    @Override
    public Boolean confirmBorrow( Borrow borrow ) {
        return librarianDao.confirmBorrow(borrow);
    }

    @Override
    public Boolean returnBook(User user, Book book) {
        return librarianDao.returnBook( user, book );
    }

    @Override
    public List<Book> bookReport() {
        return null;
    }

    @Override
    public List<User> userReport() {
        return null;
    }

    @Override
    public Author createAuthor(Author author) {
        return this.librarianDao.createAuthor(author);
    }

    @Override
    public List<Author> getAll() {
        return librarianDao.getAll() ;
    }

    @Override
    public Author searchAuthorById(Long id) {
        return librarianDao.searchAuthorById(id);
    }

    @Override
    public Author updateAuthorById(Long id, Author author) {
        return librarianDao.updateAuthorById(id, author);
    }

    @Override
    public List<Author> searchAuthorByName(String authName) {
        return librarianDao.searchAuthorByName(authName);
    }

    @Override
    public Author deleteById(Long id) {
        return null;
    }

    @Override
    public Optional<Book> searchBookById(Long id) {
        return librarianDao.searchBookById(id);
    }

    @Override
    public Optional<Book> searchBookByTitle(String title) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Book>> searchBookByAuthor(String author) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Book>> searchBookByCategory(String category) {
        return null;
    }

    @Override
    public List<Book> getAllBook() {
        return librarianDao.getAllBook();
    }

    @Override
    public Optional<User> searchUserById(Long id) {
        return librarianDao.searchUserById( id );
    }

    @Override
    public Optional<User> searchUserByUsername(String username) {
        return librarianDao.searchUserByUsername(username);
    }

    public List<Author> authorPagination( int page, int limit ){
        return librarianDao.authorPagination(page, limit);
    }

    @Override
    public List<Book> searchBooksByTitle(String title) {
        return librarianDao.searchBooksByTitle( title );
    }

    @Override
    public void backup(){
        ConnectionDb.Backup();
    }

    @Override
    public List<Book> bookPagination( int page, int limit ){
        return librarianDao.bookPagination( page, limit );
    }

    @Override
    public List<User> getReport(){
        return librarianDao.getReport();
    }

    @Override
    public Borrow searchBorrowById(Long id) {
        return librarianDao.selectBorrowById( id );
    }

    @Override
    public List<Borrow> getAllBorrow() {
        return librarianDao.getAllBorrow();
    }

    @Override
    public List<Return> getAllReturn() {
        return librarianDao.getAllReturn();
    }

    @Override
    public List<User> searchUsersByUsername(String username) {
        return librarianDao.searchUsersByUsername( username );
    }

    @Override
    public Boolean addUserToBlacklist(User user, Book book, String message) {
        return librarianDao.addUserToBlacklist( user, book, message );
    }

    @Override
    public Boolean removeUserFromBlacklist(User user, Book book) {
        return librarianDao.removeUserFromBlacklist(user, book);
    }

    @Override
    public List<BlackList> getBlackListUser(int page, int limit){
       return librarianDao.getBlackListUser(page , limit);
    }

    @Override
    public List<User> getAllUser(int page, int limit){
        return librarianDao.getAllUser( page, limit );
    }

}
