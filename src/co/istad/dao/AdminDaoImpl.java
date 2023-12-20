package co.istad.dao;

import co.istad.connection.ConnectionDb;
import co.istad.model.*;
import co.istad.util.AdminUtil;
import co.istad.util.Helper;
import co.istad.util.PasswordEncoder;
import co.istad.util.Singleton;
import co.istad.view.HelperView;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdminDaoImpl implements AdminDao {

    private final Connection connection;
    private final AdminUtil adminUtil;
    public AdminDaoImpl(){
        connection = ConnectionDb.getConnection();
        adminUtil = Singleton.getAdminUtil();
    }
    @Override
    public void backup() {

    }

    @Override
    public void restore() {

    }

    @Override
    public Long getUserCount() {
        String query = """
                    SELECT COUNT(*) FROM users WHERE role_id = 3
                """;
        return adminUtil.getCount(query);
    }

    @Override
    public Long getAdminCount() {
        String query = """
                    SELECT COUNT(*) FROM users WHERE role_id = 1
                """;
        return adminUtil.getCount(query);
    }

    @Override
    public Long getLibrarianCount() {
        String query = """
                    SELECT COUNT(*) FROM users WHERE role_id = 2
                """;
        return adminUtil.getCount(query);
    }

    @Override
    public Long getBooksCount() {
        String query = """
                    SELECT COUNT(*) FROM books
                """;
        return adminUtil.getCount(query);
    }

    @Override
    public List<User> getAllUser() {
        List<User> userResponse = new ArrayList<>();
        String query = """
                    SELECT u.*, r.name AS role FROM users u
                    INNER JOIN roles r ON u.role_id = r.id WHERE role_id = 3
                """;
        return adminUtil.getUsers(userResponse, query);
    }

    @Override
    public List<User> getAllAdmin() {
        List<User> userResponse = new ArrayList<>();
        String query = """
                    SELECT u.*, r.name AS role FROM users u
                    INNER JOIN roles r ON u.role_id = r.id WHERE role_id = 1
                """;
        return adminUtil.getUsers(userResponse, query);
    }

    @Override
    public List<User> getAllLibrarian() {
        List<User> userResponse = new ArrayList<>();
        String query = """
                    SELECT u.*, r.name AS role FROM users u
                    INNER JOIN roles r ON u.role_id = r.id WHERE role_id = 2                                     
                """;
        return adminUtil.getUsers(userResponse, query);
    }

    @Override
    public void getReport() {

    }

    @Override
    public void resetPassword(User user) {
        String query = """
                    UPDATE users
                    SET password = ?
                    WHERE id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, user.getPassword());
            statement.setLong(2, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void disableAccount(User user) {
        String query = """
                    UPDATE users
                    SET is_disable = ?
                    WHERE id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)){
            statement.setBoolean(1, user.getDisable());
            statement.setLong(2, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeAccount(Long id) {
        String query = """
                     DELETE FROM users WHERE id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)){
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            HelperView.error(e.getMessage());
        }
    }

    @Override
    public void saveReportAsExcel() {

    }

    @Override
    public List<User> getLibrarianAndUser() {
        List<User> userResponse = new ArrayList<>();
        String query = """
                    SELECT u.*, r.name AS role FROM users u
                    INNER JOIN roles r ON u.role_id = r.id WHERE role_id IN (2, 3)
                """;
        return adminUtil.getUsers(userResponse, query);
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
        BookDetail bookDetail = new BookDetail();
        Category category = new Category();
        String query = """
                    SELECT b.*, c."id" as "cate_id", c.name as "category", a.firstname, a.lastname FROM books b 
                    INNER JOIN category_book_details cb ON b."id" = cb.book_id INNER JOIN category c ON c."id" = cb.category_id 
                    INNER JOIN authors a ON a."id" = b.author_id WHERE quantity > 0
                """;
        try ( PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Author authorRes = new Author();

                Book bookRes = new Book();
                category.setId( rs.getLong( "cate_id" ) );
                category.setName( rs.getString("category") );
                bookDetail.setCategory( category );
                bookRes.setBookDetail(bookDetail);
                authorRes.setFirstName(rs.getString("firstname"));
                authorRes.setLastName(rs.getString("lastname"));

                Category category1 = new Category();
                category1.setName(rs.getString("category"));
                bookRes.setId( rs.getLong("id") );
                bookRes.setTitle(rs.getString("title"));
                bookRes.setCategory(category1);
                bookRes.setDescription(rs.getString("description"));
                bookRes.setBookDetail(bookDetail);
                bookRes.setQuantity(rs.getInt("quantity"));
                bookRes.setAuthor(authorRes);
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
