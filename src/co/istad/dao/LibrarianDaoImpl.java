package co.istad.dao;

import co.istad.connection.ConnectionDb;
import co.istad.model.*;
import co.istad.storage.Storage;
import co.istad.util.Singleton;
import co.istad.view.HelperView;
import org.apache.poi.sl.draw.geom.GuideIf;

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
        Category cate = null;
        String query = """
                INSERT INTO category(name) VALUES(?)
                RETURNING id, name,created_at;
                """;
        try( PreparedStatement preparedStatement = this.connection.prepareStatement(query) ){
            preparedStatement.setString(1, category.getName());
            ResultSet resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ){
                cate = new Category();
                cate.setId( resultSet.getLong("id") );
                cate.setName( resultSet.getString("name") );
                cate.setCreatedDate( resultSet.getDate("created_at").toLocalDate() );
            }

        }catch(Exception ex){
            HelperView.error( ex.getMessage() );
        }

        return cate;
    }

    @Override
    public Category searchCategoryById(Long id) {
        Category cate = null;
        String query = """
                SELECT * FROM category WHERE id = ?;
                """;
        try( PreparedStatement preparedStatement = this.connection.prepareStatement(query) ){
            preparedStatement.setLong( 1, id );
            ResultSet resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ){
                cate = new Category();
                cate.setId( resultSet.getLong("id") );
                cate.setName( resultSet.getString("name") );
                cate.setCreatedDate( resultSet.getDate("created_at").toLocalDate() );
            }

        }catch(Exception ex){
            HelperView.error( ex.getMessage() );
        }

        return cate;
    }
    @Override
    public List<Category> getAllCategories(){
        List<Category> categories = new ArrayList<>();
        String query = """
                SELECT * FROM category WHERE;
                """;
        try( PreparedStatement preparedStatement = this.connection.prepareStatement(query) ){
            ResultSet resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ){
                Category cate = new Category();
                cate.setId( resultSet.getLong("id") );
                cate.setName( resultSet.getString("name") );
                cate.setCreatedDate( resultSet.getDate("created_at").toLocalDate() );
                categories.add(cate);
            }

        }catch(Exception ex){
            HelperView.error( ex.getMessage() );
        }
        return categories;
    }
    @Override
    public Category updateCategoryById(Long id, Category category) {
        Category cate = null;
        String query = """
                UPDATE category
                SET name = ?
                WHERE id = ?
                RETURNING id, name,created_at;
                """;
        try( PreparedStatement preparedStatement = this.connection.prepareStatement(query) ){
            preparedStatement.setString(1, category.getName());
            preparedStatement.setLong(2, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ){
                cate = new Category();
                cate.setId( resultSet.getLong("id") );
                cate.setName( resultSet.getString("name") );
                cate.setCreatedDate( resultSet.getDate("created_at").toLocalDate() );
            }

        }catch(Exception ex){
            HelperView.error( ex.getMessage() );
        }

        return cate;
    }

    @Override
    public Book createBook(Book book) {
        String queryBook = """
                INSERT INTO books(title, author_id, quantity, user_id, description)
                VALUES( ?, ?, ?, ?, ? )
                RETURNING id, title, author_id, user_id, description, quantity;
                """;
        String queryCategoryDetails = """
            INSERT INTO category_book_details(book_id, category_id) VALUES(?, ?);
            """;
        Book bk = null;
        try(
                PreparedStatement preparedStatement = this.connection.prepareStatement( queryBook );
                PreparedStatement preparedStatement1 = this.connection.prepareStatement( queryCategoryDetails )
        ){
            this.connection.setAutoCommit(false);
            // Set Data Book
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
            System.out.println("testing 1");
            //Second
            // Set Data Category
            preparedStatement1.setLong( 1, bk.getId() );
            preparedStatement1.setLong( 2, book.getCategory().getId() );
            preparedStatement1.executeUpdate();
            connection.commit();

            return bk;

        }catch (SQLException | NullPointerException ex){
            try {
                this.connection.rollback();
                HelperView.error(ex.getMessage());
            }catch (SQLException e){
                HelperView.error( e.getMessage() );
            }
        }
        return bk;
    }

    @Override
    public Book updateBookById(Long id, Book book) {
        Book bk = null;
        String query = """
                UPDATE books
                SET title = ?,
                author_id = ?,
                quantity = ?,
                user_id = ?,
                description = ?
                WHERE id = ?
                RETURNING id, title, author_id, quantity, user_id, description;
                """;
        try(PreparedStatement preparedStatement = this.connection.prepareStatement(query)){
            //Add Data
            preparedStatement.setString( 1, book.getTitle() );
            preparedStatement.setLong( 2, book.getAuthor().getId() );
            preparedStatement.setInt( 3, book.getQuantity() );
            preparedStatement.setLong( 4, book.getUser().getId() );
            preparedStatement.setString( 5, book.getDescription() );
            preparedStatement.setLong( 6, book.getId() );
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                bk = new Book();
                Author author = new Author();
                User user = new User();
                bk.setId( resultSet.getLong("id") );
                bk.setTitle( resultSet.getString("title") );
                author.setId( resultSet.getLong("author_id") );
                user.setId( resultSet.getLong("user_id") );
                bk.setAuthor( author );
                bk.setUser( user );
                bk.setQuantity( resultSet.getInt("quantity") );
                bk.setDescription( resultSet.getString("description") );
            }
            return bk;
        }
        catch (Exception ex){
            HelperView.error( ex.getMessage() );
            return null;
        }
    }

    @Override
    public Boolean confirmBorrow(Borrow borrow) {
        String selectBorrow = """
            SELECT * FROM borrow WHERE id = ?;
            """;
        String selectBorrowWhichConfirm = """
            SELECT * FROM borrow WHERE id = ? AND is_borrow = true;
            """;
        String queryUpdateBook = """
            UPDATE books
            SET quantity = quantity - ?
            WHERE id = ?;
    """;
        String queryUpdateBorrow = """
            UPDATE borrow
            SET is_borrow = true
            WHERE id = ?
            """;
        String querySelectBook = """
                SELECT * FROM books WHERE id = ?;
                """;
        try (
                PreparedStatement preparedStatement = this.connection.prepareStatement(queryUpdateBook);
                PreparedStatement preparedStatement1 = this.connection.prepareStatement(queryUpdateBorrow);
                PreparedStatement preparedStatement2 = this.connection.prepareStatement(selectBorrow);
                PreparedStatement preparedStatement3 = this.connection.prepareStatement(selectBorrowWhichConfirm);
                PreparedStatement preparedStatement4 = this.connection.prepareStatement(querySelectBook);
        ) {
            this.connection.setAutoCommit(false);

            Borrow borrowRes = null;
            Book bookRes = null;
            User userRes = null;
            // Add Data
            preparedStatement2.setLong(1, borrow.getId());
            ResultSet selectBookResult = preparedStatement2.executeQuery();

            while (selectBookResult.next()) {
                borrowRes = new Borrow();
                User user = new User();
                Book book = new Book();
                borrowRes.setId(selectBookResult.getLong("id"));
                user.setId(selectBookResult.getLong("user_id"));
                borrowRes.setUser(user);
                book.setId(selectBookResult.getLong("book_id"));
                borrowRes.setBook(book);
                borrowRes.setId(selectBookResult.getLong("id"));
                borrowRes.setQuantity( selectBookResult.getInt("book_quantity") );
            }

            if (borrowRes == null) {
                HelperView.error("The borrow id does not exist!");
                return false;
            }

            preparedStatement4.setLong( 1, borrowRes.getBook().getId() );
            ResultSet selectBookResultSet = preparedStatement4.executeQuery();

            while ( selectBookResultSet.next() ){
                bookRes = new Book();
                bookRes.setId(selectBookResultSet.getLong("id"));
                bookRes.setQuantity(selectBookResultSet.getInt("quantity"));
            }

            if( bookRes == null ){
                HelperView.error("No this book in our system!");
                return false;
            }else{
                if( bookRes.getQuantity() < borrowRes.getQuantity() ){
                    HelperView.error("Not enough book quantity for you borrow!");
                    return false;
                }
            }

            preparedStatement3.setLong(1, borrow.getId());
            ResultSet selectBorrowConfirm = preparedStatement3.executeQuery();
            Borrow selectBookIsBorrow = null;

            if (selectBorrowConfirm.next()) {
                selectBookIsBorrow = new Borrow();
                User user = new User();
                Book book = new Book();
                selectBookIsBorrow.setId(selectBorrowConfirm.getLong("id"));
                user.setId(selectBorrowConfirm.getLong("user_id"));
                selectBookIsBorrow.setUser(user);
                book.setId(selectBorrowConfirm.getLong("book_id"));
                selectBookIsBorrow.setBook(book);
                selectBookIsBorrow.setId(selectBorrowConfirm.getLong("id"));
            }

            if (selectBookIsBorrow != null) {
                HelperView.error("This user is borrowing this book and has not returned it yet!");
                return false;
            }

            // Added Data
            preparedStatement1.setLong(1, borrow.getId());
            preparedStatement1.executeUpdate();

            // Add Data
            preparedStatement.setInt(1, borrowRes.getQuantity());
            preparedStatement.setLong(2, borrowRes.getBook().getId());
            preparedStatement.executeUpdate();

            this.connection.commit();

            return true;

        } catch (SQLException ex) {
            try {
                this.connection.rollback();
            } catch (SQLException exc) {
                System.out.println(exc);
                // HelperView.error( exc.getMessage() );
                return false;
            }
            System.out.println(ex);
            // HelperView.error(ex.getMessage());
            return false;
        }
    }

    @Override
    public Boolean returnBook(User user, Book book) {
        String selectBorrow = """
                SELECT * FROM borrow
                WHERE book_id = ? AND user_id = ? AND is_borrow = true AND is_return = false;
                """;
        String updateBookQuery = """
                UPDATE books
                SET quantity = quantity + ?
                WHERE id = ?;
                """;
        String updateReturnQuery = """
                UPDATE borrow
                SET is_return = true
                WHERE id = ?;
                """;
        try(
            PreparedStatement preparedStatement = this.connection.prepareStatement(selectBorrow);
            PreparedStatement updateBookStatement = this.connection.prepareStatement(updateBookQuery);
            PreparedStatement updateReturnStatement = this.connection.prepareStatement( updateReturnQuery )
        ){
            this.connection.setAutoCommit(false);
            //Added Data
            preparedStatement.setLong( 1, book.getId() );
            preparedStatement.setLong( 2, user.getId() );
            ResultSet resultSet = preparedStatement.executeQuery();
            Borrow borrow = null;
            while ( resultSet.next() ){
                borrow = new Borrow();
                borrow.setQuantity( resultSet.getInt("book_quantity") );
                borrow.setId( resultSet.getLong("id") );
            }

            if( borrow == null ){
                HelperView.error( String.format("This user id(%s) not borrowed to book id(%s) yet!", user.getId(), book.getId()) );
                return false;
            }

            updateBookStatement.setInt( 1, borrow.getQuantity() );
            updateBookStatement.setLong( 2, book.getId() );
            updateReturnStatement.setLong( 1, borrow.getId() );

            updateBookStatement.executeUpdate();
            updateReturnStatement.executeUpdate();
            this.connection.commit();
            return true;

        }catch(Exception ex){
            try{
                this.connection.rollback();
            }catch (SQLException e){
                HelperView.error( "Database can't rollback" );
            }
            HelperView.error( "Something went wrong!" );
            return false;
        }
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
    /**
     * Get ALl Authors
     * **/
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
    /**
     * Delete Author By Id
     */
    public Author deleteById(Long id) {
        return null;
    }

    @Override
    public List<Author> searchAuthorByName(String authorName) {
        List<Author> authors = null;
        String query = """
                SELECT * FROM authors WHERE ( firstname || lastname ) ILIKE ?;
                """;
        try(PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            // Add Data
            preparedStatement.setString(1, "%" + authorName + "%" );
            ResultSet resultSet = preparedStatement.executeQuery();
            authors = new ArrayList<>();
            while ( resultSet.next() ){
                Author author = new Author();
                author.setId( resultSet.getLong("id") );
                author.setFirstName( resultSet.getString("firstname") );
                author.setLastName( resultSet.getString("lastname") );
                author.setEmail( resultSet.getString("email") );
                authors.add(author);
            }
            return authors;
        }catch (Exception ex){
            HelperView.error(ex.getMessage());
            return authors;
        }
    }

    @Override
    public Optional<Book> searchBookById(Long id) {
        String query = """
                   SELECT b.* ,a.email as "auth_email", a.firstname as "auth_firstname", a.lastname as "auth_lastname" , a."id" as "auth_id" ,a.firstname as "auth_firstname", a.lastname as "auth_lastname", u.username as "user_username",u.id as "user_id" ,c."id" as "category_id", c."name" as "category_name" FROM books b
                   INNER JOIN authors a ON a."id" = b.author_id
                   INNER JOIN users u ON u."id" = b.user_id
                   INNER JOIN category_book_details ctb ON ctb.book_id = b."id"
                   INNER JOIN category c ON ctb.category_id = c."id"
                   WHERE b.id = ?;
                """;
        try ( PreparedStatement preparedStatement = this.connection.prepareStatement( query ) ) {
            Book book = null;
            //Add Data
            preparedStatement.setLong( 1, id );
            ResultSet resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ){
                Category category = new Category();
                book = new Book();
                Author author = new Author();
                User user = new User();
                book.setId( resultSet.getLong("id") );
                book.setTitle( resultSet.getString("title") );
                book.setDescription( resultSet.getString("description") );
                category.setId( resultSet.getLong("category_id") );
                category.setName( resultSet.getString("category_name") );
                author.setId( resultSet.getLong("auth_id") );
                author.setFirstName( resultSet.getString("auth_firstname") );
                author.setLastName( resultSet.getString("auth_lastname") );
                author.setEmail( resultSet.getString("auth_email") );
                user.setUsername( resultSet.getString("user_username") );
                user.setId( resultSet.getLong("user_id") );
                book.setCategory(category);
                book.setAuthor(author);
                book.setQuantity( resultSet.getInt("quantity") );
                book.setUser( user );
            }
            if( book == null ) {
                return Optional.empty();
            }
            return Optional.of( book );
        }catch (Exception ex){
            HelperView.error(ex.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Book> searchBooksByTitle(String title) {
        List<Book> books = new ArrayList<>();
        String query = """
                SELECT b.* ,a.email as "auth_email", a.firstname as "auth_firstname", a.lastname as "auth_lastname" , a."id" as "auth_id" ,a.firstname as "auth_firstname", a.lastname as "auth_lastname", u.username as "user_username", c."id" as "category_id", c."name" as "category_name" FROM books b
                   INNER JOIN authors a ON a."id" = b.author_id
                   INNER JOIN users u ON u."id" = b.user_id
                   INNER JOIN category_book_details ctb ON ctb.book_id = b."id"
                   INNER JOIN category c ON ctb.category_id = c."id"
                   WHERE b.title ILIKE ?;
                """;
        try(PreparedStatement preparedStatement = this.connection.prepareStatement(query) ){
            //Added data
            preparedStatement.setString(1, "%" + title + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ){
                Book book = new Book();
                Category category = new Category();
                book = new Book();
                Author author = new Author();
                User user = new User();
                book.setId( resultSet.getLong("id") );
                book.setTitle( resultSet.getString("title") );
                book.setDescription( resultSet.getString("description") );
                category.setId( resultSet.getLong("category_id") );
                category.setName( resultSet.getString("category_name") );
                author.setId( resultSet.getLong("auth_id") );
                author.setFirstName( resultSet.getString("auth_firstname") );
                author.setLastName( resultSet.getString("auth_lastname") );
                author.setEmail( resultSet.getString("auth_email") );
                user.setUsername( resultSet.getString("user_username") );
                book.setCategory(category);
                book.setAuthor(author);
                book.setQuantity( resultSet.getInt("quantity") );
                book.setUser( user );
                books.add( book );
            }
            return books;
        }catch (Exception ex){
            HelperView.error( ex.getMessage() );
            return books;
        }
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
        List<Book> books = new ArrayList<>();
        String query = """
                SELECT b.* ,a.email as "auth_email", a.firstname as "auth_firstname", a.lastname as "auth_lastname" , a."id" as "auth_id" ,a.firstname as "auth_firstname", a.lastname as "auth_lastname", u.username as "user_username", c."id" as "category_id", c."name" as "category_name" FROM books b
                   INNER JOIN authors a ON a."id" = b.author_id
                   INNER JOIN users u ON u."id" = b.user_id
                   INNER JOIN category_book_details ctb ON ctb.book_id = b."id"
                   INNER JOIN category c ON ctb.category_id = c."id";
                """;
        try(PreparedStatement preparedStatement = this.connection.prepareStatement(query) ){
            //Added data
            ResultSet resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ){
                Book book = new Book();
                Category category = new Category();
                book = new Book();
                Author author = new Author();
                User user = new User();
                book.setId( resultSet.getLong("id") );
                book.setTitle( resultSet.getString("title") );
                book.setDescription( resultSet.getString("description") );
                category.setId( resultSet.getLong("category_id") );
                category.setName( resultSet.getString("category_name") );
                author.setId( resultSet.getLong("auth_id") );
                author.setFirstName( resultSet.getString("auth_firstname") );
                author.setLastName( resultSet.getString("auth_lastname") );
                author.setEmail( resultSet.getString("auth_email") );
                user.setUsername( resultSet.getString("user_username") );
                book.setCategory(category);
                book.setAuthor(author);
                book.setQuantity( resultSet.getInt("quantity") );
                book.setUser( user );
                books.add( book );
            }
            return books;
        }catch (Exception ex){
            HelperView.error( ex.getMessage() );
            return books;
        }
    }

    @Override
    public Optional<User> searchUserById(Long id) {
        String query = """
                SELECT * FROM users WHERE id = ?;
                """;
        try( PreparedStatement preparedStatement = this.connection.prepareStatement( query ) ){
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            User user = null;
            while ( resultSet.next() ){
                user = new User();
                user.setId(resultSet.getLong("id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setDisable( resultSet.getBoolean("is_disable") );
            }
            if( user == null ){
                return Optional.empty();
            }
            return Optional.of(user);
        }catch (Exception ex){
            HelperView.error( ex.getMessage() );
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> searchUserByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public List<User> searchUsersByUsername(String username) {
        String query = """
                SELECT * FROM users WHERE username ILIKE ?;
                """;
        List<User> users = new ArrayList<>();
        try( PreparedStatement preparedStatement = this.connection.prepareStatement( query ) ){
            preparedStatement.setString(1, "%" + username + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ){
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setDisable( resultSet.getBoolean("is_disable") );
                users.add(user);
            }
        }catch (Exception ex){
            HelperView.error( ex.getMessage() );
        }
        return users;
    }

    public List<Author> authorPagination( int page, int limit ){
        List<Author> authors = new ArrayList<>();
        String query = """
                   SELECT * FROM authors LIMIT ? OFFSET ?
                """;
        try( PreparedStatement preparedStatement = this.connection.prepareStatement(query) ){
            //Add Data
            preparedStatement.setInt( 1, limit );
            preparedStatement.setInt( 2, (page < 1 ) ? page : page - 1 );
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Author author = new Author();
                author.setId( resultSet.getLong("id") );
                author.setFirstName( resultSet.getString("firstname") );
                author.setLastName( resultSet.getString("lastname") );
                author.setEmail( resultSet.getString("email") );
                authors.add(author);
            }
            return authors;
        }
        catch (Exception ex){
            HelperView.error(ex.getMessage());
            return authors;
        }
    }

    @Override
    public Optional<Book> searchBookByTitle(String title) {
        return Optional.empty();
    }
    @Override
    public List<Book> bookPagination( int page, int limit ){
        List<Book> books = new ArrayList<>();
        String query = """
                SELECT b.* ,a.email as "auth_email", a.firstname as "auth_firstname", a.lastname as "auth_lastname" , a."id" as "auth_id" ,a.firstname as "auth_firstname", a.lastname as "auth_lastname", u.username as "user_username", c."id" as "category_id", c."name" as "category_name" FROM books b
                   INNER JOIN authors a ON a."id" = b.author_id
                   INNER JOIN users u ON u."id" = b.user_id
                   INNER JOIN category_book_details ctb ON ctb.book_id = b."id"
                   INNER JOIN category c ON ctb.category_id = c."id"
                    LIMIT ?
                    OFFSET ?;
                """;
        try(PreparedStatement preparedStatement = this.connection.prepareStatement(query) ){
            //Added data
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2,  (page < 1) ? 0 : (page - 1) * limit );
            ResultSet resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ){
                Book book = new Book();
                Category category = new Category();
                book = new Book();
                Author author = new Author();
                User user = new User();
                book.setId( resultSet.getLong("id") );
                book.setTitle( resultSet.getString("title") );
                book.setDescription( resultSet.getString("description") );
                category.setId( resultSet.getLong("category_id") );
                category.setName( resultSet.getString("category_name") );
                author.setId( resultSet.getLong("auth_id") );
                author.setFirstName( resultSet.getString("auth_firstname") );
                author.setLastName( resultSet.getString("auth_lastname") );
                author.setEmail( resultSet.getString("auth_email") );
                user.setUsername( resultSet.getString("user_username") );
                book.setCategory(category);
                book.setAuthor(author);
                book.setQuantity( resultSet.getInt("quantity") );
                book.setUser( user );
                books.add( book );
            }
            return books;
        }catch (Exception ex){
            HelperView.error( ex.getMessage() );
            return books;
        }
    }

    @Override
    public List<Borrow> borrowPagination( int page, int limit ){
        List<Borrow> borrows = new ArrayList<>();
        String query = """
                SELECT * FROM borrow
                LIMIT ?
                OFFSET ?;
                """;
        try(PreparedStatement preparedStatement = this.connection.prepareStatement( query )){
            preparedStatement.setInt(1, limit );
            preparedStatement.setInt(2, page );
            ResultSet resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ){
                Borrow borrow = new Borrow();

            }

            return borrows;
        }
        catch(Exception ex){
            HelperView.error(ex.getMessage());
            return borrows;
        }
    }

    @Override
    public List<User> getReport(){
        String query = """
                    SELECT u.*,
                    		bk.id as "book_id",
                    		bk.title as "book_title",
                    		bk.quantity as "book_quantity",
                    		br.is_borrow as "is_borrow",
                    		br.is_return as "is_return",
                    		br.created_at as "borrow_at",
                    		br.book_quantity as "borrow_quantity",
                    		br.start_borrow_date,
                    		br.deadline_borrow_date
                    FROM public.users u
                    INNER JOIN public.borrow br ON br.user_id = u.id
                    INNER JOIN public.books bk ON bk.id = br.book_id
                    ORDER BY br.created_at DESC
                """;
        List<User> users = new ArrayList<>();
        try(PreparedStatement preparedStatement = this.connection.prepareStatement(query)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ){
                Borrow borrow = new Borrow();
                Book book = new Book();
                User user = new User();
                user.setId( resultSet.getLong("id") );
                user.setUsername( resultSet.getString("username") );
                user.setEmail( resultSet.getString("email") );
                borrow.setQuantity( resultSet.getInt("borrow_quantity") );
                borrow.setBorrow( resultSet.getBoolean("is_borrow") );
                borrow.setReturn( resultSet.getBoolean("is_return") );
                borrow.setBorrowDate( resultSet.getDate("borrow_at").toLocalDate() );
                book.setId( resultSet.getLong("book_id") );
                book.setTitle( resultSet.getString("book_title") );
                borrow.setStartBorrowDate( resultSet.getDate("start_borrow_date").toLocalDate() );
                borrow.setDeadlineBorrowDate( resultSet.getDate("deadline_borrow_date").toLocalDate() );
                borrow.setBook( book );
                user.setBorrow( borrow );
                users.add( user );
            }
            return users;
        }catch(Exception ex){
            HelperView.error(ex.getMessage());
            return users;
        }
    }

    @Override
    public Borrow selectBorrowById( Long id ){
        Borrow borrow = null;
        String query = """
                   SELECT
                   bk.*,
                   br.book_quantity,
                   u.username,
                   u.id as "user_id",
                   br.start_borrow_date,
                   br.deadline_borrow_date,
                   br.id as "borrow_id",
                   br.is_borrow,
                   br.is_return
                   FROM borrow br
                   INNER JOIN books bk ON bk.id = br.book_id
                   INNER JOIN users u ON u.id = br.user_id
                   WHERE br.id = ? AND br.is_return = false;
                """;
        try( PreparedStatement preparedStatement = this.connection.prepareStatement( query ) ){
            preparedStatement.setLong( 1, id );
            ResultSet resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ){
                borrow = new Borrow();
                Book book = new Book();
                User user = new User();
                book.setTitle( resultSet.getString("title") );
                book.setId( resultSet.getLong("id") );
                user.setUsername( resultSet.getString("username") );
                user.setId( resultSet.getLong("user_id") );
                borrow.setUser( user );
                borrow.setBorrow( resultSet.getBoolean("is_borrow") );
                borrow.setReturn( resultSet.getBoolean("is_return") );
                borrow.setBook( book );
                borrow.setId( resultSet.getLong("borrow_id") );
                borrow.setStartBorrowDate( resultSet.getDate("start_borrow_date").toLocalDate() );
                borrow.setDeadlineBorrowDate( resultSet.getDate("deadline_borrow_date").toLocalDate() );
                borrow.setQuantity( resultSet.getInt("book_quantity") );

            }

        }catch (Exception ex){
            HelperView.error( ex.getMessage() );
        }
        return borrow;
    }

    @Override
    public List<Borrow> getAllBorrow(){
        List<Borrow> borrows = new ArrayList<>();
        String query = """
                   SELECT
                   bk.*,
                   br.book_quantity,
                   u.username,
                   u.id as "user_id",
                   br.start_borrow_date,
                   br.deadline_borrow_date,
                   br.id as "borrow_id",
                   br.is_borrow,
                   br.is_return
                   FROM borrow br
                   INNER JOIN books bk ON bk.id = br.book_id
                   INNER JOIN users u ON u.id = br.user_id
                   WHERE br.is_return = false;
                """;
        try( PreparedStatement preparedStatement = this.connection.prepareStatement( query ) ){
            ResultSet resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ){
                Borrow borrow = new Borrow();
                Book book = new Book();
                User user = new User();
                book.setTitle( resultSet.getString("title") );
                book.setId( resultSet.getLong("id") );
                user.setUsername( resultSet.getString("username") );
                user.setId( resultSet.getLong("user_id") );
                borrow.setUser( user );
                borrow.setBook( book );
                borrow.setBorrow( resultSet.getBoolean("is_borrow") );
                borrow.setReturn( resultSet.getBoolean("is_return") );
                borrow.setId( resultSet.getLong("borrow_id") );
                borrow.setStartBorrowDate( resultSet.getDate("start_borrow_date").toLocalDate() );
                borrow.setDeadlineBorrowDate( resultSet.getDate("deadline_borrow_date").toLocalDate() );
                borrow.setQuantity( resultSet.getInt("book_quantity") );
                borrows.add( borrow );
            }

        }catch (Exception ex){
            HelperView.error( ex.getMessage() );
        }
        return borrows;
    }

    @Override
    public List<Return> getAllReturn() {
        List<Return> returns = new ArrayList<>();
        String query = """
                SELECT
                    re.*,
                     u.id as "user_id",
                     u.username,
                     b.title,
                     b.id as "book_id",
                     br.book_quantity as "borrow_quantity",
                     br.is_return,
                     br.is_borrow,
                     br.deadline_borrow_date,
                     br.start_borrow_date,
                     br.id as "borrow_id"
                FROM return re
                INNER JOIN borrow br ON br.id = re.borrow_id
                INNER JOIN users u ON u.id = br.user_id
                INNER JOIN books b ON b.id = br.book_id
                WHERE br.is_borrow = true
                """;
        try( PreparedStatement preparedStatement = this.connection.prepareStatement(query) ){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                User user = new User();
                Borrow borrow = new Borrow();
                Book book = new Book();
                Return aReturn = new Return();
                //Book
                book.setId( resultSet.getLong("book_id") );
                book.setTitle( resultSet.getString("title") );
                //User
                user.setId( resultSet.getLong("user_id") );
                user.setUsername( resultSet.getString("username") );
                //Borrow
                borrow.setUser(user);
                borrow.setBook(book);
                borrow.setQuantity( resultSet.getInt("borrow_quantity") );
                borrow.setId( resultSet.getLong("borrow_id") );
                borrow.setReturn( resultSet.getBoolean("is_return") );
                borrow.setBorrow( resultSet.getBoolean("is_borrow") );
                borrow.setStartBorrowDate( resultSet.getDate("start_borrow_date").toLocalDate() );
                borrow.setDeadlineBorrowDate( resultSet.getDate("deadline_borrow_date").toLocalDate() );

                //Return
                aReturn.setId( resultSet.getLong("id") );
                aReturn.setReturnDate( resultSet.getDate("created_at").toLocalDate() );
                aReturn.setBorrow( borrow );
                returns.add(aReturn);
            }

        }catch (Exception ex){
            HelperView.error(ex.getMessage());
        }

        return returns;
    }

    @Override
    public Boolean addUserToBlacklist(User user, Book book, String message) {
        String query = """
                INSERT INTO blacklists( user_id, book_id, quantity, message, status )
                VALUES(?, ?, ?, ?, ?);
                """;
        String borrowQuery = """
                SELECT * FROM borrow
                WHERE user_id = ? AND book_id = ? AND is_borrow = true AND is_return = false;
                """;
        try(
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
//            PreparedStatement updateBookPrepareStatement = this.connection.prepareStatement(updateBookQuery);
            PreparedStatement borrowPrepareStatement = this.connection.prepareStatement(borrowQuery);
        ){
            this.connection.setAutoCommit(false);

            borrowPrepareStatement.setLong(1, user.getId());
            borrowPrepareStatement.setLong(2, book.getId());
            ResultSet resultSet = borrowPrepareStatement.executeQuery();
            Borrow borrow = null;
            while ( resultSet.next() ){
                borrow = new Borrow();
                borrow.setId( resultSet.getLong("id") );
            }

            if( borrow == null ){
                HelperView.error(String.format("User id(%s) not borrow this book id(%s) yet", user.getId(), book.getId()));
                return false;
            }

            //Add Data
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setLong(2, book.getId());
            preparedStatement.setInt(3, book.getQuantity());
            preparedStatement.setString(4, message);
            preparedStatement.setBoolean(5, true);
            preparedStatement.executeUpdate();

            if( book.getQuantity() > 0 ){
                Boolean b = returnBook(user, book);
                if( !b ){
                    HelperView.error("Returned failed!");
                    return false;
                }
            }

            this.connection.commit();

            return true;

        }catch (Exception ex){
            try{
                this.connection.rollback();
            }catch (SQLException e){
                HelperView.error(ex.getMessage());
                return false;
            }
            HelperView.error(ex.getMessage());
            return false;
        }
    }

    @Override
    public Boolean removeUserFromBlacklist(User user, Book book) {
        String blackListQuery = """
                SELECT * FROM blacklists
                WHERE user_id = ? AND book_id = ?;
                """;
        String deleteBlackListQuery = """
                DELETE FROM blacklists
                WHERE user_id = ? AND book_id = ?;
                """;
        try(
            PreparedStatement preparedStatement = this.connection.prepareStatement( blackListQuery );
            PreparedStatement daleteBlackListPrepareStatement = this.connection.prepareStatement(deleteBlackListQuery)
        ){
            //Added Data
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setLong(2, book.getId());

            ResultSet resultSet = preparedStatement.executeQuery();
            Boolean isPresent = false;
            while (resultSet.next()){
                isPresent = true;
            }

            if( isPresent ){
                daleteBlackListPrepareStatement.setLong(1, user.getId());
                daleteBlackListPrepareStatement.setLong(2, book.getId());
                daleteBlackListPrepareStatement.executeUpdate();
                return true;
            }else{
                HelperView.error("This user without in blacklist");
                return false;
            }


        }catch(Exception ex){
            HelperView.error( ex.getMessage() );
            return false;
        }
    }

    public List<BlackList> getBlackListUser( int page, int limit ){
        List<BlackList> blackLists = new ArrayList<>();
        String query = """
                SELECT
                	bl.*,
                	us.username,
                	us.email,
                	bk.title
                FROM blacklists bl
                INNER JOIN users us ON us.id = bl.user_id
                INNER JOIn books bk ON bk.id = bl.book_id
                """;
        try(PreparedStatement preparedStatement = this.connection.prepareStatement(query)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                BlackList blackList = new BlackList();
                User user = new User();
                Book book = new Book();
                blackList.setId( resultSet.getLong("id") );
                blackList.setQuantity( resultSet.getInt("quantity") );
                blackList.setMessage( resultSet.getString("message") );
                blackList.setStatus( resultSet.getBoolean("status") );
                blackList.setDate( resultSet.getDate("created_at").toLocalDate() );
                //user
                user.setId( resultSet.getLong("user_id") );
                user.setUsername( resultSet.getString("username") );
                user.setEmail( resultSet.getString("email") );
                blackList.setUser( user  );
                //Book
                book.setId( resultSet.getLong("book_id") );
                book.setTitle( resultSet.getString("title") );
                blackList.setBook( book );
                blackLists.add( blackList );
            }
        }
        catch (Exception ex){
            HelperView.error(ex.getMessage());
        }
        return blackLists;
    }

    @Override
    public List<User> getAllUser(int page, int limit) {
        String query = """
                SELECT * FROM users
                LIMIT ?
                OFFSET ?;
                """;
        List<User> users = new ArrayList<>();
        try( PreparedStatement preparedStatement = this.connection.prepareStatement( query ) ){
            preparedStatement.setInt( 1, limit );
            preparedStatement.setInt( 2, ( page - 1 ) * limit );
            ResultSet resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ){
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setDisable( resultSet.getBoolean("is_disable") );
                users.add(user);
            }
        }catch (Exception ex){
            HelperView.error( ex.getMessage() );
        }
        return users;
    }
}
