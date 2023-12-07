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
      Optional<Book> bookId = userFeatureDao.searchBookById(id);
        if(bookId.isEmpty()){
            System.out.println("Book with id " + id + "is not found");
        }
        return bookId;
    }

    @Override
    public Optional<Book> searchBookByTitle(String title) {
        Optional<Book> bookByTitle = userFeatureDao.searchBookByTitle(title);
        if(bookByTitle.isEmpty()) {
            System.out.println("Book Title :" + bookByTitle.get().getTitle() + "Is Not Found");
        }
        return bookByTitle;
    }

    @Override
    public Optional<List<Book>> searchBookByAuthor(String author) {
        var data = userFeatureDao.searchBookByAuthor(author);
        if (data.isEmpty()) {
            System.out.println("Author is not found ");
        }
        return data;
    }

    @Override
    public Optional<List<Book>> searchBookByCategory(String category) {
        var data = userFeatureDao.searchBookByCategory(category);

        if (data.isEmpty()) {
            System.out.println("category is not found ");
        }
        return data;
    }

    @Override
    public List<Book> getAllBook() {
        List<Book> books = userFeatureDao.getAllBook();
        return  books;
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

       return  userFeatureDao.previewBookById(id);
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
       if(bookHis == null) {
           System.out.println("No Book Borrow");
       }
       return  bookHis;
    }
}
