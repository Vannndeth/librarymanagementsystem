package co.istad.controller;

import co.istad.model.User;
import co.istad.service.AdminService;
import co.istad.service.LoginService;
import co.istad.storage.Storage;
import co.istad.util.Singleton;
import co.istad.view.AdminView;
import co.istad.view.HelperView;
import co.istad.view.HomepageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class AdminController {

    private final Scanner scanner;
    private final Storage storage;
    private final AdminService adminService;
    private final AdminView adminView;
    private final LoginService loginService;
    private final HomepageView homepageView;
    public AdminController(){
        scanner = Singleton.scanner();
        adminService = Singleton.getAdminServiceImpl();
        storage = Singleton.getStorage();
        adminView = Singleton.getAdminView();
        loginService = Singleton.getLoginService();
        homepageView = Singleton.getHomepageView();
    }
    public void adminDashboard(){
        User user = new User();
        HelperView.welcome("=".repeat(115));
        HelperView.welcome(" ".repeat(57) + "Welcome");
        HelperView.welcome("=".repeat(115));
        adminView.dashboardOverview();
        again:
        do {
            adminView.adminDashboardView(scanner);
            int option = adminView.option(scanner);
            switch (option) {
                case 1 -> {
                    getAllAdmin();
                }
                case 2 -> {
                    getAllLibrarian();
                }
                case 3 -> {
                    getAllUser();
                }
                case 4 -> {
                    getAllBook();
                }
                case 5 -> {
                    resetPassword();
                }
                case 6 -> {
                    disableAccount();
                }
                case 7 -> {
                    removeAccount();
                }
                case 8 -> {
                    saveReportExcel();
                }
                case 9 -> {
                    backup();
                }
                case 10 -> {
                    restore();
                }
                case 11 -> {
                    viewReport();
                }
                case 12 -> {
                    storage.setId(null);
                    return;
                }
                default -> {
                    HelperView.message("Please choose option above...!");
                    continue again;
                }
            }
        }while (true);
    }
    public void getAllBook(){
        adminView.bookView(adminService.getAllBook());
    }
    public void getAllAdmin(){
        adminView.usersView(adminService.getAllAdmin());
    }
    public void getAllLibrarian(){
        adminView.usersView(adminService.getAllLibrarian());
    }
    public void getAllUser(){
        adminView.usersView(adminService.getAllUser());
    }

    public void disableAccount(){
        User user = new User();
        adminView.searchById(user, scanner);
        List<User> users = adminService.getLibrarianAndUser();
        final String[] confirm = {""};
        AtomicInteger found = new AtomicInteger();
        found.set(0);
        users.forEach(u -> {
            if (u.getId().equals(user.getId())) {
                HelperView.message(String.format("Account %d Information",u.getId()));
                List<User> userViews = new ArrayList<>();
                userViews.add(u);
                adminView.usersView(userViews);
                System.out.print("Set account disable: ");
                user.setDisable(Boolean.parseBoolean(scanner.nextLine()));
                System.out.print("Are you sure you want to disable?(Y/N): ");
                confirm[0] = scanner.nextLine();
                if (confirm[0].equalsIgnoreCase("y")) {
                    adminService.disableAccount(user);
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
    public void resetPassword(){
        User user = new User();
        adminView.searchById(user, scanner);
        List<User> users = adminService.getLibrarianAndUser();
        final String[] confirm = {""};
        AtomicInteger found = new AtomicInteger();
        found.set(0);
        users.forEach(u -> {
            if (u.getId().equals(user.getId())) {
                HelperView.message(String.format("Account %d Information",u.getId()));
                List<User> userViews = new ArrayList<>();
                userViews.add(u);
                adminView.usersView(userViews);
                System.out.print("Set new password: ");
                user.setPassword(scanner.nextLine());
                System.out.print("Are you sure you want to reset new password?(Y/N): ");
                confirm[0] = scanner.nextLine();
                if (confirm[0].equalsIgnoreCase("y")) {
                    adminService.resetPassword(user);
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
    public void removeAccount(){
        User user = new User();
        adminView.searchById(user, scanner);
        List<User> users = adminService.getLibrarianAndUser();
        final String[] confirm = {""};
        AtomicInteger found = new AtomicInteger();
        found.set(0);
        users.forEach(u -> {
            if (user.getId().equals(u.getId())) {
                HelperView.message(String.format("Account %d Information",u.getId()));
                List<User> userViews = new ArrayList<>();
                userViews.add(u);
                adminView.usersView(userViews);
                System.out.print("Are you sure you want to remove?(Y/N): ");
                confirm[0] = scanner.nextLine();
                if (confirm[0].equalsIgnoreCase("y")) {
                    adminService.removeAccount(user.getId());
                    HelperView.message(String.format("Account id %d has been removed...!", user.getId()));
                    found.set(1);
                } else {
                    HelperView.message("You has been canceled remove...!");
                    found.set(2);
                }
            }
        });
        if (found.get() == 0){
            HelperView.message(String.format("Account id %d not found...!", user.getId()));
        }
    }
    public void getLibrarianAndUser(){
        adminView.usersView(adminService.getLibrarianAndUser());
    }
    public void saveReportExcel(){

    }
    public void backup(){

    }
    public void restore(){

    }
    public void viewReport(){

    }
}


