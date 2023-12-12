package co.istad.service;

import co.istad.dao.AdminDao;
import co.istad.model.Book;
import co.istad.model.User;
import co.istad.storage.Storage;
import co.istad.util.Singleton;
import co.istad.view.HelperView;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class AdminServiceImpl implements AdminService{
    private final AdminDao adminDao;
    private final Scanner scanner;
    private final Storage storage;
    public AdminServiceImpl(){
        adminDao = Singleton.getAdminDaoImpl();
        scanner = Singleton.scanner();
        storage = Singleton.getStorage();
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

    }

    @Override
    public void resetPassword(User user) {
        List<User> users = adminDao.getAllUser();
        final String[] confirm = {""};
        AtomicInteger found = new AtomicInteger();
        found.set(0);
        users.forEach(u -> {
            if (u.getId().equals(user.getId())) {
                System.out.print("Set new password: ");
                user.setPassword(scanner.nextLine());
                System.out.print("Are you sure you want to reset new password?(Y/N): ");
                confirm[0] = scanner.nextLine();
                if (confirm[0].equalsIgnoreCase("y")) {
                    adminDao.resetPassword(user);
                    HelperView.message(String.format("Account id %d has been reset new password...!", user.getId()));
                    found.set(1);
                } else {
                    HelperView.message("You has been canceled reset new password...!");
                    found.set(2);
                }
            }
        });
        if (found.get() == 0){
            HelperView.message(String.format("Account id %d not found...!", user.getId()));
        }

    }

    @Override
    public void disableAccount(User user) {
        List<User> users = adminDao.getAllUser();
        final String[] confirm = {""};
        AtomicInteger found = new AtomicInteger();
        found.set(0);
        users.forEach(u -> {
            if (u.getId().equals(user.getId())) {
                System.out.print("Set account disable: ");
                user.setDisable(Boolean.parseBoolean(scanner.nextLine()));
                System.out.print("Are you sure you want to disable?(Y/N): ");
                confirm[0] = scanner.nextLine();
                if (confirm[0].equalsIgnoreCase("y")) {
                    adminDao.disableAccount(user);
                    HelperView.message(String.format("Account id %d disabled = %b",user.getId(), user.getDisable()));
                    found.set(1);
                } else {
                    HelperView.message("You has been canceled disable account...!");
                    found.set(2);
                }
            }
        });
        if (found.get() == 0){
            HelperView.message(String.format("Account id %d not found...!", user.getId()));
        }
    }

    @Override
    public void removeAccount(Long id) {
        List<User> users = adminDao.getAllUser();
        final String[] confirm = {""};
        AtomicInteger found = new AtomicInteger();
        found.set(0);
        users.forEach(user -> {
            if (id.equals(user.getId())) {
                System.out.print("Are you sure you want to remove?(Y/N): ");
                confirm[0] = scanner.nextLine();
                if (confirm[0].equalsIgnoreCase("y")) {
                    adminDao.removeAccount(id);
                    HelperView.message(String.format("Account id %d has been removed...!", user.getId()));
                    found.set(1);
                } else {
                    HelperView.message("You has been canceled remove...!");
                    found.set(2);
                }
            }
        });
        if (found.get() == 0){
            HelperView.message(String.format("Account id %d not found...!", id));
        }
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
