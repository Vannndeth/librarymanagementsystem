package co.istad.dao;

import co.istad.model.Book;
import co.istad.model.Borrow;
import co.istad.model.User;

import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {

    @Override
    public Optional<Book> searchBookById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Book> searchBookByTitle(String title) {
        return Optional.empty();
    }

    @Override
    public Optional<Book> searchBookByAuthor(String author) {
        return Optional.empty();
    }

    @Override
    public List<Book> searchBookByCategory(String category) {
        return null;
    }

    @Override
    public List<Book> getAllBook() {
        return null;
    }

    @Override
    public Optional<User> searchUserById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> searchUserByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public List<User> getALl() {
        return null;
    }

    @Override
    public User getById(Long id) {
        return null;
    }

    @Override
    public User update(Long id, User user) {
        return null;
    }

    @Override
    public List<User> deleteAll() {
        return null;
    }

    @Override
    public User deleteById(Long id) {
        return null;
    }

    @Override
    public Book previewBookById(Long id) {
        return null;
    }

    @Override
    public Borrow borrowBook(Long id) {
        return null;
    }

    @Override
    public List<Borrow> bookHistory() {
        return null;
    }
}
