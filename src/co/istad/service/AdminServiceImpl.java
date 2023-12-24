package co.istad.service;

import co.istad.dao.AdminDao;
import co.istad.dao.LibrarianDao;
import co.istad.model.Book;
import co.istad.model.User;
import co.istad.storage.Storage;
import co.istad.util.Singleton;
import co.istad.view.AdminView;
import co.istad.view.HelperView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class AdminServiceImpl implements AdminService{
    private final AdminDao adminDao;
    private final Scanner scanner;
    private final Storage storage;
    private final LibrarianDao librarianDao;
    public AdminServiceImpl(){
        adminDao = Singleton.getAdminDaoImpl();
        scanner = Singleton.scanner();
        storage = Singleton.getStorage();
        librarianDao = Singleton.getLibrarianDao();
    }
    @Override
    public void backUp() {
        adminDao.backup();
    }

    @Override
    public void restore() {
        adminDao.restore();
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
        return adminDao.getBooksCount();
    }

    @Override
    public List<User> getAllUser() {
        return adminDao.getAllUser();
    }

    @Override
    public List<User> getAllAdmin() {
        return adminDao.getAllAdmin();
    }

    @Override
    public List<User> getAllLibrarian() {
        return adminDao.getAllLibrarian();
    }

    @Override
    public void getReport() {
        librarianDao.getReport();
    }

    @Override
    public void resetPassword(User user) {
        adminDao.resetPassword(user);

    }

    @Override
    public void disableAccount(User user) {
        adminDao.disableAccount(user);
    }

    @Override
    public void removeAccount(Long id) {
        adminDao.removeAccount(id);
    }
    @Override
    public void saveReportAsExcel() {

    }

    @Override
    public List<User> getLibrarianAndUser() {
        return adminDao.getLibrarianAndUser();
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
        List<Book> books = adminDao.getAllBook();
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
}
