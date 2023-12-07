package co.istad.service;

import co.istad.dao.AdminDao;
import co.istad.model.Book;
import co.istad.model.User;
import co.istad.util.Singleton;

import java.util.List;
import java.util.Optional;

public class AdminServiceImpl implements AdminService{
    private final AdminDao adminDao;
    public AdminServiceImpl(){
        adminDao = Singleton.getAdminDaoImpl();
    }
    @Override
    public void backUp() {

    }

    @Override
    public void restore() {

    }

    @Override
    public Long getUserCount() {
        return adminDao.getUserCount();
    }

    @Override
    public Long getAdminCount() {

        return adminDao.getAdminCount();
    }

    @Override
    public Long getLibrarianCount() {
        return adminDao.getLibrarianCount();
    }

    @Override
    public Long getBooksCount() {
        return null;
    }

    @Override
    public List<User> getAllUser() {
        return null;
    }

    @Override
    public List<User> getAllAdmin() {
        return null;
    }

    @Override
    public List<User> getAllLibrarian() {
        return null;
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
