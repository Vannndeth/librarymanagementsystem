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
        do {
            HelperView.welcome("=".repeat(50));
            HelperView.welcome("Welcome to admin dashboard");
            HelperView.welcome("=".repeat(50));
            int option = adminView.adminDashboardView(scanner);
            switch (option) {
                case 1 -> {
                    Long adminCount = adminService.getAdminCount();
                    adminView.allUserView(adminCount, "Total Admin");
                }
                case 2 -> {
                    Long userCount = adminService.getUserCount();
                    adminView.allUserView(userCount,"Total User");
                }
                case 3 -> {
                    Long librarianCount = adminService.getLibrarianCount();
                    adminView.allUserView(librarianCount, "Total Librarian");
                }
                case 4 -> {
                    getAllBook();
                }
                case 11 -> {
                    storage.setId(null);
                    return;
                }
            }
        }while (true);
    }
    public void getAllBook(){

        adminView.bookView(adminService.getAllBook());
    }
}


