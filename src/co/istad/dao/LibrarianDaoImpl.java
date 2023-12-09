package co.istad.dao;

import co.istad.connection.ConnectionDb;
import co.istad.model.*;
import co.istad.storage.Storage;
import co.istad.util.Singleton;
import co.istad.view.HelperView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LibrarianDaoImpl implements LibrarianDao {
    private final Connection connection;
    private final Storage storage;
    public LibrarianDaoImpl(){
        connection = ConnectionDb.getConnection();
        storage = Singleton.getStorage();
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
    public Book createBook(Book book) {
        String query = """
                INSERT INTO books(title, author_id, quantity, user_id, description)
                VALUES( ?, ?, ?, ?, ? )
                RETURNING id, title, author_id, user_id, description, quantity;
                """;
        Book bk = null;
        try(PreparedStatement preparedStatement = this.connection.prepareStatement( query ) ){
            // Set Data
            preparedStatement.setString( 1, book.getTitle() );
            preparedStatement.setLong( 2, book.getAuthor().getId() );
            preparedStatement.setInt( 3, book.getQuantity() );
            preparedStatement.setLong( 4, storage.getId() );
            preparedStatement.setString( 5, book.getDescription() );

            // Response Values
            Author author = new Author();
            User user = new User();
            ResultSet resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ){

                bk = new Book();
                author.setId( resultSet.getLong("author_id") );
                user.setId( resultSet.getLong("user_id") );
                bk.setId( resultSet.getLong("id") );
                bk.setQuantity( resultSet.getInt( "quantity" ) );
                bk.setAuthor( author );
                bk.setUser( user );

            }

            return bk;

        }catch (SQLException ex){
            System.err.println(ex.getMessage());
            return bk;
        }
    }

    @Override
    public Book updateBookById(Long id, Book book) {
        return null;
    }

    @Override
    public Boolean confirmBorrow(User user, Book book) {
        return null;
    }

    @Override
    public Boolean returnBook(User user, Book book) {
        return null;
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
        Author auth = null;
        String query = """
                INSERT INTO authors( firstname, lastname, email )
                VALUES( ?, ?, ? )
                RETURNING id, firstname, lastname, email;
                """;
        try ( PreparedStatement preparedStatement = this.connection.prepareStatement(query) ){
            //Set Data
            preparedStatement.setString(1, author.getFirstName());
            preparedStatement.setString(2, author.getLastName());
            preparedStatement.setString(3, author.getEmail());

            ResultSet rs = preparedStatement.executeQuery();
            while ( rs.next() ){
                auth = new Author();
                auth.setId( rs.getLong("id") );
                auth.setFirstName( rs.getString("firstname") );
                auth.setLastName( rs.getString("lastname") );
            }
            return auth;

        }catch (SQLException | NullPointerException ex){
            System.out.println(ex);
//            System.err.println( ex.getMessage() );
            return auth;
        }
    }

    @Override
    public List<Author> getAll() {
        List<Author> authors = null;
        String query = """
                SELECT * FROM authors;
                """;
        try( PreparedStatement preparedStatement = this.connection.prepareStatement(query) ){
            ResultSet resultSet = preparedStatement.executeQuery();
            authors = new ArrayList<>();
            while ( resultSet.next() ){
                Author author = new Author();
                author.setId( resultSet.getLong("id") );
                author.setFirstName( resultSet.getString("firstname") );
                author.setLastName( resultSet.getString("lastname") );
                authors.add( author );
            }
            return authors;
        }catch (Exception ex){
            HelperView.error(ex.getMessage());
            return authors;
        }
    }

    @Override
    public Author searchAuthorById(Long id) {
        Author author = null;
        String query = """
               SELECT * FROM authors WHERE id = ?;
                """;
        try(PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            //Add Data
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                author = new Author();
                author.setId( resultSet.getLong("id") );
                author.setFirstName( resultSet.getString("firstname") );
                author.setLastName( resultSet.getString("lastname") );
                author.setEmail( resultSet.getString("email") );
            }
            return author;
        }
        catch(Exception ex){
            HelperView.error(ex.getMessage());
            return author;
        }
    }

    @Override
    public Author updateAuthorById(Long id, Author author) {
        Author auth = null;
        String query = """
                UPDATE authors
                SET firstname = ?, lastname = ?, email = ? WHERE id = ?
                RETURNING id, firstname, lastname, email;
                """;
        try(PreparedStatement preparedStatement = this.connection.prepareStatement(query)){
            //Add Data
            preparedStatement.setString(1, author.getFirstName());
            preparedStatement.setString(2, author.getLastName());
            preparedStatement.setString(3, author.getEmail());
            preparedStatement.setLong( 4, id );
            ResultSet resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ){
                auth = new Author();
                auth.setEmail( resultSet.getString("email") );
                auth.setFirstName( resultSet.getString("firstname") );
                auth.setLastName( resultSet.getString("lastname") );
                auth.setId( resultSet.getLong("id") );
            }
            return auth;
        }
        catch ( Exception ex ){
            HelperView.error(ex.getMessage());
            return auth;
        }

    }

    @Override
    public Author deleteById(Long id) {
        return null;
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
