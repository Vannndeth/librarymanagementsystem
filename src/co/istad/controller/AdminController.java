package co.istad.controller;

import co.istad.model.User;
import co.istad.service.AdminService;
import co.istad.service.LoginService;
import co.istad.storage.Storage;
import co.istad.util.Singleton;
import co.istad.view.AdminView;
import co.istad.view.HelperView;
import co.istad.view.HomepageView;

import java.util.Scanner;

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
        HelperView.welcome("=".repeat(50));
        HelperView.welcome("Welcome to admin dashboard");
        HelperView.welcome("=".repeat(50));
        adminView.dashboardOverview();
        do {
            int option = adminView.adminDashboardView(scanner);
            switch (option) {
                case 1 -> {
                    getAdminCount();
                }
                case 2 -> {
                    getLibrarianCount();
                }
                case 3 -> {
                    getUserCount();
                }
                case 4 -> {
                    getAllBook();
                }
                case 5 -> {
                    getAllAdmin();
                }
                case 6 -> {
                    getAllLibrarian();
                }
                case 7 -> {
                    getAllUser();
                }
                case 8 -> {
                    System.out.println("");
                }
                case 9 -> {
                    resetPassword();
                }
                case 10 -> {
                    disableAccount();
                }
                case 11 -> {
                    System.out.println("lll");
                }
                case 12 -> {
                    System.out.println(";;");
                }
                case 13 -> {
                    System.out.println("pp");
                }
                case 14 -> {
                    System.out.println("k");
                }

                default -> {
                    storage.setId(null);
                    return;
                }
            }
        }while (true);
    }

    public void getAdminCount(){
        Long adminCount = adminService.getAdminCount();
        adminView.countUserView(adminCount, "Total Admin");
    }
    public void getLibrarianCount(){
        Long userCount = adminService.getLibrarianCount();
        adminView.countUserView(userCount,"Total Librarian");
    }
    public void getUserCount(){
        Long librarianCount = adminService.getUserCount();
        adminView.countUserView(librarianCount, "Total User");
    }
    public void getAllBook(){
        adminView.bookView(adminService.getAllBook());
    }
    public void getAllAdmin(){
        adminView.usersView(adminService.getAllAdmin());
    }public void getAllLibrarian(){
        adminView.usersView(adminService.getAllLibrarian());
    }public void getAllUser(){
        adminView.usersView(adminService.getAllUser());
    }

    public void disableAccount(){
        User user = new User();
        adminView.disableAccountView(user, scanner);
        adminService.disableAccount(user);
        HelperView.message(String.format("Account id %d disabled = %b",user.getId(), user.getDisable()));
    }
    public void resetPassword(){
        User user = new User();
        adminView.resetPasswordView(user, scanner);
        adminService.resetPassword(user);
        HelperView.message(String.format("Account id %d reset password successfully...",user.getId()));
    }
}


