package co.istad.service;

import co.istad.dao.UserDao;
import co.istad.model.Book;
import co.istad.model.Borrow;
import co.istad.model.Return;
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
        return userFeatureDao.searchBookById(id);
    }

    @Override
    public Optional<Book> searchBookByTitle(String title) {
       return  userFeatureDao.searchBookByTitle(title);

    }

    @Override
    public Optional<List<Book>> searchBookByAuthor(String author) {
       return userFeatureDao.searchBookByAuthor(author);
    }

    @Override
    public Optional<List<Book>> searchBookByCategory(String category) {
        return userFeatureDao.searchBookByCategory(category);
    }

    @Override
    public List<Book> getAllBook() {
      return userFeatureDao.getAllBook();
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
      return  userFeatureDao.borrowBook(id);
    }

    @Override
    public List<Borrow> bookHistory() {
        return userFeatureDao.bookHistory();
    }

    @Override
    public List<Borrow> borrowHistory() {
        return  userFeatureDao.borrowHistory();
    }

    @Override
    public Return bookReturn(Long id) {
        return  userFeatureDao.bookReturn(id);
    }

    @Override
    public List<Borrow> allBorrow() {
       return  userFeatureDao.allBorrow();
    }

    @Override
    public Return returnBook(long id) {
        return  userFeatureDao.returnBook(id);
    }

    @Override
    public int countBorrowBook() {
        return userFeatureDao.countBorrowBook();
    }


}
