package co.istad.dao;

import co.istad.model.Book;
import co.istad.model.Role;
import co.istad.model.User;
import co.istad.util.PasswordEncoder;
import co.istad.util.RoleEnum;
import co.istad.view.HelperView;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public interface MainFeatureDao {
    public static User login(User user, Connection connection){
        String query = """
                    SELECT u.*, r.name as role_name, r.id as role_id FROM users u
                        INNER JOIN roles r
                        ON r.id = u.role_id
                        WHERE (username = ? OR email = ?) AND password = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword() );
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Role role = new Role();
                role.setId( rs.getLong("role_id") );
                role.setRole( RoleEnum.valueOf( rs.getString("role_name") ) );
                user.setUsername( rs.getString("username") );
//                user.setEmail( rs.getString("email") );
                user.setId(rs.getLong("id") );
                user.setRole( role );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }
    public static User create(User user, Connection connection){
        String query = """
                    INSERT INTO users (username, email, password, created_at, role_id, salt)
                    VALUES(?, ?, ?, ?, ?, ?)
                """;
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            byte[] salt = PasswordEncoder.generateSalt();
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setDate(4, new Date(System.currentTimeMillis()));
            statement.setLong(5, user.getRole().getId());
            statement.setBytes(6, salt);
            int affectedRow = statement.executeUpdate();
            System.out.println("Signup successfully...!");
            System.out.println(affectedRow);
        } catch (SQLException e) {
            System.out.println("Username already exist. " + e.getMessage());
        }
        return user;
    }
    public Optional<Book> searchBookById (Long id);
    public Optional<Book> searchBookByTitle (String title);
    public Optional<Book> searchBookByAuthor (String author);
    public List<Book> searchBookByCategory (String category);
    public List<Book> getAllBook();
    public Optional<User> searchUserById(Long id);
    public Optional<User> searchUserByUsername(String username);
}
