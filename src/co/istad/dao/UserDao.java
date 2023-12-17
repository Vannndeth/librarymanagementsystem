package co.istad.dao;

import co.istad.model.Book;
import co.istad.model.Borrow;
import co.istad.model.Return;
import co.istad.model.User;

import java.util.List;

public interface UserDao extends MainFeatureDao {

    public List<User> getALl();
    public User getById(Long id);
    public User update(Long id, User user);
    public List<User> deleteAll();
    public User deleteById(Long id);
    public Book previewBookById(Long id);
    public Borrow borrowBook(Long id);
    public List<Borrow> bookHistory();
    public List<Borrow> borrowHistory();
    public Return bookReturn (Long id);
    public List<Borrow> allBorrow ();
    public Return returnBook (long id);
    public int countBorrowBook();
}
