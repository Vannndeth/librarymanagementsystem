package co.istad.dao;

import co.istad.connection.ConnectionDb;
import co.istad.model.*;
import co.istad.storage.Storage;
import co.istad.util.Singleton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
    private final Connection connection;
    private final Storage storage;
    public UserDaoImpl(){
        storage = Singleton.getStorage();
        connection = ConnectionDb.getConnection();
    }

    @Override
    public Optional<Book> searchBookById(Long id) {
        Book bookRes = new Book();
        Author authorRes = new Author();
        BookDetail bookDetail = new BookDetail();
        Category category = new Category();
        String query = """
                    SELECT b.*, c."id" as "cate_id", c.name, a.firstname, a.lastname FROM books b INNER JOIN category_book_details cb ON b."id" = cb.book_id INNER JOIN category c ON c."id" = cb.category_id INNER JOIN authors a ON a."id" = b.author_id WHERE b.id = ?;
                """;
        try ( PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            preparedStatement.setLong(1,id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                category.setId( rs.getLong( "cate_id" ) );
                category.setName( rs.getString("name") );
                bookDetail.setCategory( category );
                bookRes.setBookDetail(bookDetail);
                authorRes.setFirstName(rs.getString("firstname"));
                authorRes.setLastName(rs.getString("lastname"));
                bookRes.setId( rs.getLong("id") );
                bookRes.setDescription(rs.getString("description"));
                bookRes.setTitle(rs.getString("title"));
                bookRes.setQuantity(rs.getInt("quantity"));
                bookRes.setAuthor(authorRes);
                return Optional.of(bookRes);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Book> searchBookByTitle(String title) {
        Book bookRes = new Book();
        Author authorRes = new Author();
        BookDetail bookDetail = new BookDetail();
        Category category = new Category();
        String query = """
           SELECT b.*, c."id" as "cate_id", c.name, a.firstname, a.lastname FROM books b INNER JOIN category_book_details cb ON b."id" = cb.book_id INNER JOIN category c ON c."id" = cb.category_id INNER JOIN authors a ON a."id" = b.author_id WHERE b.title ILIKE ?;
        """;
        try ( PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            preparedStatement.setString(1,"%" + title + "%");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                category.setId( rs.getLong( "cate_id" ) );
                category.setName( rs.getString("name") );
                bookDetail.setCategory( category );
                bookRes.setBookDetail(bookDetail);
                authorRes.setFirstName(rs.getString("firstname"));
                authorRes.setLastName(rs.getString("lastname"));
                bookRes.setId( rs.getLong("id") );
                bookRes.setDescription(rs.getString("description"));
                bookRes.setTitle(rs.getString("title"));
                bookRes.setQuantity(rs.getInt("quantity"));
                bookRes.setAuthor(authorRes);
                return Optional.of(bookRes);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
    @Override
    public Optional<List<Book>> searchBookByAuthor(String author) {
        List<Book> booksResp = new ArrayList<>();
        Author authorRes = new Author();
        BookDetail bookDetail = new BookDetail();
        Category category = new Category();
        String query = """
                    SELECT b.*,c.name,c."id" as "cate_id", a.lastname, a.firstname from authors a inner JOIN books b ON b.author_id = a."id"
                                    INNER JOIN category_book_details cb ON b."id" = cb.book_id INNER JOIN category c ON c."id" = cb.category_id WHERE (a.firstname  || a.lastname) ILIKE ? ORDER BY id ASC;                                                                                                                                                            
                """;
        try ( PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + author + "%" );
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Book bookRes = new Book();
                category.setId( rs.getLong( "cate_id" ) );
                category.setName( rs.getString("name") );
                bookDetail.setCategory( category );
                bookRes.setBookDetail(bookDetail);
                authorRes.setFirstName(rs.getString("firstname"));
                authorRes.setLastName(rs.getString("lastname"));
                bookRes.setId( rs.getLong("id") );
                bookRes.setDescription(rs.getString("description"));
                bookRes.setTitle(rs.getString("title"));
                bookRes.setQuantity(rs.getInt("quantity"));
                bookRes.setAuthor(authorRes);
                booksResp.add( bookRes);
            }
            return Optional.of( booksResp );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Optional<List<Book>> searchBookByCategory(String category) {
        List<Book> booksResp = new ArrayList<>();
        Author authorRes = new Author();
        BookDetail bookDetail = new BookDetail();
        Category cate = new Category();
        String query = """
                SELECT c."id" as "cate_id",  * FROM category c INNER JOIN category_book_details cb ON c."id" = cb.category_id INNER JOIN books b ON b."id" = cb.book_id INNER JOIN authors a ON a."id" = b.author_id WHERE c.name ILIKE  ? ;
                """;
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query);) {
            preparedStatement.setString(1, "%" + category + "%");
            ResultSet rs =preparedStatement.executeQuery();
            while (rs.next()) {
                Book bookRes = new Book();
                cate.setId( rs.getLong( "cate_id" ) );
                cate.setName( rs.getString("name") );
                bookDetail.setCategory( cate );
                bookRes.setBookDetail(bookDetail);
                authorRes.setFirstName(rs.getString("firstname"));
                authorRes.setLastName(rs.getString("lastname"));
                bookRes.setId( rs.getLong("id") );
                bookRes.setDescription(rs.getString("description"));
                bookRes.setTitle(rs.getString("title"));
                bookRes.setQuantity(rs.getInt("quantity"));
                bookRes.setAuthor(authorRes);
                booksResp.add(bookRes);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(booksResp);
    }

    @Override
    public List<Book> getAllBook() {
        List<Book> books = new ArrayList<>();
        String query = """
       SELECT b.*, c."id" as "cate_id", c.name, a.firstname, a.lastname FROM books b INNER JOIN category_book_details cb ON b."id" = cb.book_id INNER JOIN category c ON c."id" = cb.category_id INNER JOIN authors a ON a."id" = b.author_id ORDER BY id ASC
    """;
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Book bookRes = new Book();  // Create a new Book object for each iteration

                Category category = new Category();
                category.setId(rs.getLong("cate_id"));
                category.setName(rs.getString("name"));

                BookDetail bookDetail = new BookDetail();
                bookDetail.setCategory(category);

                Author authorRes = new Author();
                authorRes.setFirstName(rs.getString("firstname"));
                authorRes.setLastName(rs.getString("lastname"));

                bookRes.setBookDetail(bookDetail);
                bookRes.setId(rs.getLong("id"));
                bookRes.setDescription(rs.getString("description"));
                bookRes.setTitle(rs.getString("title"));
                bookRes.setQuantity(rs.getInt("quantity"));
                bookRes.setAuthor(authorRes);
                books.add(bookRes);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return books;
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
        Book bookRes = new Book();
        Author authorRes = new Author();
        BookDetail bookDetail = new BookDetail();
        Category category = new Category();
        String query = """
                    SELECT b.*, c."id" as "cate_id", c.name, a.firstname, a.lastname FROM books b INNER JOIN category_book_details cb ON b."id" = cb.book_id INNER JOIN category c ON c."id" = cb.category_id INNER JOIN authors a ON a."id" = b.author_id WHERE b.id = ?;
                """;
        try ( PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            preparedStatement.setLong(1,id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                category.setId( rs.getLong( "cate_id" ) );
                category.setName( rs.getString("name") );
                bookDetail.setCategory( category );
                bookRes.setBookDetail(bookDetail);
                authorRes.setFirstName(rs.getString("firstname"));
                authorRes.setLastName(rs.getString("lastname"));
                bookRes.setId( rs.getLong("id") );
                bookRes.setDescription(rs.getString("description"));
                bookRes.setTitle(rs.getString("title"));
                bookRes.setQuantity(rs.getInt("quantity"));
                bookRes.setAuthor(authorRes);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bookRes;


    }

    @Override
    public Borrow borrowBook(Long id) {
        String query = "INSERT INTO borrow  (user_id, book_id, book_quantity ,start_borrow_date, deadline_borrow_date ) VALUES (?, ? , ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '7' DAY  )";
        try(PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setLong(1, storage.getId() );
            statement.setLong(2, id);
            statement.setLong(3, 1);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Not ID Was Found " + e.getMessage());
        }
        return null;
    }


    @Override
    public List<Borrow> bookHistory() {
        List<Borrow> borrowList = new ArrayList<>();
        String query = "SELECT  b.id, b.title , bw.is_borrow, bw.start_borrow_date, bw.deadline_borrow_date  FROM borrow bw INNER JOIN books b ON bw.book_id = b.id WHERE bw.user_id = ? ";
        try (PreparedStatement preparedStatement = ConnectionDb.getConnection().prepareStatement(query)) {
            preparedStatement.setLong(1, storage.getId());
            ResultSet rs =preparedStatement.executeQuery();
            while (rs.next()) {
                Borrow borrow = new Borrow();
                Book book = new Book();
                book.setId( rs.getLong("id"));
                borrow.setBook(book);
                book.setTitle(rs.getString("title"));
                borrow.setBorrowDate(rs.getTimestamp("start_borrow_date").toLocalDateTime().toLocalDate());
                borrow.setDeadline(rs.getTimestamp("deadline_borrow_date").toLocalDateTime().toLocalDate());
                borrow.setBorrow(rs.getBoolean("is_borrow"));
                borrowList.add(borrow);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return borrowList;
    }

    @Override
    public List<Borrow> borrowHistory() {
        List<Borrow> borrowList = new ArrayList<>();
       String query = """
                 select bk.id , bk.title, bw.start_borrow_date, bw.deadline_borrow_date from borrow bw inner join books bk on bw.book_id = bk.id where bw.is_return = false AND bw.user_id = ?
               """;
        try(PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setLong(1, storage.getId());
            ResultSet rs =statement.executeQuery();
            while (rs.next()) {
                Borrow borrow = new Borrow();
                borrow.setBorrowDate(rs.getTimestamp("start_borrow_date").toLocalDateTime().toLocalDate());
                borrow.setDeadline(rs.getTimestamp("deadline_borrow_date").toLocalDateTime().toLocalDate());
                Book book = new Book();
                borrow.setBook(book);
                book.setTitle(rs.getString("title"));
                book.setId(rs.getLong("id"));
                borrowList.add(borrow);
            }
        } catch (SQLException e) {
            System.out.println("No Book Borrow " + e.getMessage());
        }
        return borrowList ;
    }

    @Override
    public Return bookReturn(Long id ) {
        String query = """
                SELECT * FROM borrow bw where bw.book_id = ? AND bw.user_id = ? AND bw.is_return = false;
                """;
        try ( PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            preparedStatement.setLong(1,id);
            preparedStatement.setLong(2, storage.getId());
            preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public List<Borrow> allBorrow(){
        List<Borrow> borrowList = new ArrayList<>();
        String query = """
                SELECT * FROM borrow bw WHERE bw.user_id = ? AND is_return = false ;
                """;
        try ( PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
                preparedStatement.setLong(1 , storage.getId());
             ResultSet rs = preparedStatement.executeQuery();
             while (rs.next()) {
                 Borrow borrow = new Borrow();
                 Book book = new Book();
                 book.setId( rs.getLong("book_id"));
                 borrow.setBook(book);
                 borrowList.add(borrow);
             }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return borrowList;
    }

    @Override
    public Return returnBook(long id) {
        String query = """
                INSERT INTO return (borrow_id)
                VALUES (
                    (SELECT id FROM borrow WHERE book_id = ? LIMIT 1)
                );
                """;
        try ( PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            preparedStatement.setLong(1,id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public int countBorrowBook() {
        String query = """
                SELECT COUNT(*) FROM borrow bw WHERE bw.is_return = false AND bw.user_id = ?
                """;
        try ( PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            preparedStatement.setLong(1 , storage.getId());
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

}
