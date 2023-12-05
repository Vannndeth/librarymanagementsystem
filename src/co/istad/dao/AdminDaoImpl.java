package co.istad.dao;

import co.istad.connection.ConnectionDb;
import co.istad.model.Book;
import co.istad.model.User;
import co.istad.util.PasswordEncoder;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AdminDaoImpl implements AdminDao {

    private final Connection connection;
    public AdminDaoImpl(){
        connection = ConnectionDb.getConnection();
    }
    @Override
    public void backUp() {

    }

    @Override
    public void restore() {

    }

    @Override
    public Long getUserCount() {
        return null;
    }

    @Override
    public Long getAdminCount() {
        return null;
    }

    @Override
    public Long getLibrariansCount() {
        return null;
    }

    @Override
    public Long getBooksCount() {
        return null;
    }

    @Override
    public List<User> getAllUser() {
        return null;
    }

    @Override
    public List<User> getAllAdmin() {
        return null;
    }

    @Override
    public List<User> getAllLibrarians() {
        return null;
    }

    @Override
    public void getReport() {

    }

    @Override
    public void resetPassword() {

    }

    @Override
    public void disableAccount() {

    }

    @Override
    public User removeAccount() {
        return null;
    }

    @Override
    public void saveReportAsExcel() {

    }

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
}
