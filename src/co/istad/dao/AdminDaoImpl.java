package co.istad.dao;

import co.istad.connection.ConnectionDb;
import co.istad.model.Book;
import co.istad.model.User;
import co.istad.util.PasswordEncoder;

import java.sql.*;
import java.util.ArrayList;
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
        String query = """
                    SELECT COUNT(*) FROM users WHERE role_id = 3
                """;
        return getCount(query);
    }

    @Override
    public Long getAdminCount() {
        String query = """
                    SELECT COUNT(*) FROM users WHERE role_id = 1
                """;
        return getCount(query);
    }

    private Long getCount(String query) {
        Long count = 0L;
        try (PreparedStatement statement = connection.prepareStatement(query)){
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                count += rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return count;
    }

    @Override
    public Long getLibrarianCount() {
        String query = """
                    SELECT COUNT(*) FROM users WHERE role_id = 2
                """;
        return getCount(query);
    }

    @Override
    public Long getBooksCount() {

        String query = """
                    SELECT COUNT(*) FROM BOOK
                """;
        return getCount(query);
    }

    @Override
    public List<User> getAllUser() {
        List<User> userResponse = new ArrayList<>();
        String query = """
                    SELECT * FROM user WHERE role_id = 2
                """;
        return getUsers(userResponse, query);
    }

    @Override
    public List<User> getAllAdmin() {
        List<User> userResponse = new ArrayList<>();
        String query = """
                    SELECT * FROM user WHERE role_id = 1
                """;
        return getUsers(userResponse, query);
    }

    private List<User> getUsers(List<User> userResponse, String query) {
        try (PreparedStatement statement = connection.prepareStatement(query)){
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                User userRes = new User();
                userRes.setId(rs.getLong("id"));
                userRes.setUsername(rs.getString("username"));
                userRes.setDisable(rs.getBoolean("is_Disable"));
                userResponse.add(userRes);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userResponse;
    }

    @Override
    public List<User> getAllLibrarian() {
        List<User> userResponse = new ArrayList<>();
        String query = """
                    SELECT * FROM user WHERE role = LIBRARIAN
                """;
        return getUsers(userResponse, query);
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
    public Optional<List<Book>> searchBookByAuthor(String author) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Book>> searchBookByCategory(String category) {
        return null;
    }

    @Override
    public List<Book> getAllBook() {
        List<Book> booksResp = new ArrayList<>();
        String query = """
           SELECT * FROM books;
        """;
        try ( PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Book bookRes = new Book();
                bookRes.setId(rs.getLong("id"));
                bookRes.setTitle(rs.getString("title"));
                bookRes.setQuantity(rs.getInt("Quantity"));
                booksResp.add(bookRes);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return booksResp;
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
