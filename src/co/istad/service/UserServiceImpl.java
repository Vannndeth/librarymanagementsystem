package co.istad.service;

import co.istad.dao.UserDao;
import co.istad.model.Book;
import co.istad.model.Borrow;
import co.istad.model.User;
import co.istad.storage.Storage;
import co.istad.util.Singleton;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService{
    private final UserDao userFeatureDao;
    private final Storage storage;
    public UserServiceImpl(){
        userFeatureDao = Singleton.getUserDaoImpl();
        storage = Singleton.getStorage();
    }
    @Override
    public Optional<Book> searchBookById(Long id) {
        var data = userFeatureDao.searchBookById(id);
        if(data.isEmpty()){
            System.out.println("Book with id " + id + "is not found");
        }
        Table table = new Table(3, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell("Book ID");
        table.addCell("Title" );
        table.addCell("Quantity");
        data.ifPresent(book -> {
            table.addCell(book.getId().toString());
            table.addCell(book.getTitle());
            table.addCell(book.getQuantity().toString());
        });
        System.out.println(table.render());
        return data;
    }

    @Override
    public Optional<Book> searchBookByTitle(String title) {

        var data = userFeatureDao.searchBookByTitle(title);
        Table table = new Table(3, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell("Book ID");
        table.addCell("Title" );
        table.addCell("Quantity");

        data.ifPresent(bt ->
                {
                    table.addCell(bt.getId().toString());
                    table.addCell(bt.getTitle());
                    table.addCell(bt.getQuantity().toString());

                }
        );
        System.out.println(table.render());
        return data;
    }

    @Override
    public Optional<List<Book>> searchBookByAuthor(String author) {
        var data = userFeatureDao.searchBookByAuthor(author);
        Table table = new Table(3, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell("Book ID");
        table.addCell("Title" );
        table.addCell("Quantity");

        data.ifPresent(bookList ->
        {
            bookList.forEach( br -> {
                        table.addCell(br.getId().toString());
                        table.addCell(br.getTitle());
                        table.addCell(br.getQuantity().toString());
                    }
            );

        }

        );
        if (data.isEmpty()) {
            System.out.println("Author is not found ");
        }
        System.out.println(table.render());
        return data;

    }

    @Override
    public Optional<List<Book>> searchBookByCategory(String category) {
        var data = userFeatureDao.searchBookByCategory(category);
        Table table = new Table(3, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell("Book ID");
        table.addCell("Title" );
        table.addCell("Quantity");
        data.ifPresent(cateList ->
                {
                    cateList.forEach( cate -> {
                                table.addCell(cate.getId().toString());
                                table.addCell(cate.getTitle());
                                table.addCell(cate.getQuantity().toString());

                            }
                    );
                }
        );
        if (data.isEmpty()) {
            System.out.println("category is not found ");
        }
        System.out.println(table.render());
        return data;
    }

    @Override
    public List<Book> getAllBook() {
        var data = userFeatureDao.getAllBook();
        Table table = new Table(3, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell("Book ID");
        table.addCell("Title" );
        table.addCell("Quantity");
        if(data != null) {
            data.forEach( books -> {
                        table.addCell(books.getId().toString());
                        table.addCell(books.getTitle());
                        table.addCell(books.getQuantity().toString());
                    }
            );
        }
        System.out.println(table.render());
        return  data;
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
    public User login(User user) {
        return null;
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
    public Book getByTitle(String title) {
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
        var data = userFeatureDao.previewBookById(id);
        Table table = new Table(6, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell("Category ID");
        table.addCell("Title" );
        table.addCell("Author");
        table.addCell("Description");
        table.addCell("Category");
        table.addCell("Stock");
        table.addCell(data.getId().toString());
        table.addCell(data.getTitle());
        table.addCell(data.getAuthor().getFirstName() + data.getAuthor().getLastName());
        table.addCell(data.getDescription());
        table.addCell(data.getBookDetail().getCategory().getName());
        if(data.getQuantity() > 0) {
            table.addCell("Book Still Available");
        }
        System.out.println(table.render());
        return data;
    }

    @Override
    public Borrow borrowBook(Long id) {
        var dataRes = userFeatureDao.borrowBook(id);
        var bookRes = userFeatureDao.getAllBook();
        try{
            if (!bookRes.stream().anyMatch(book -> book.getId().equals(id))) {

            }
        }catch (Exception e) {
            System.out.println(e.getMessage() + "Book ID: " + id + " not found");
        }
        return dataRes;
    }

    @Override
    public List<Borrow> bookHistory() {
        var bookHis = userFeatureDao.bookHistory();
        Table table = new Table(4, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell("Book ID");
        table.addCell("Title" );
        table.addCell("Borrow Date");
        table.addCell("Book Deadline");
        if(bookHis != null) {
            bookHis.forEach( books -> {
                        table.addCell(books.getBook().getId().toString());
                        table.addCell(books.getBook().getTitle());
                        table.addCell(books.getBorrowDate().toString());
                        table.addCell(books.getDeadline().toString());
                    }
            );
            System.out.println(table.render());
        }
        return  bookHis;
    }
}
