package co.istad.controller;

import co.istad.model.User;
import co.istad.service.AdminService;
import co.istad.service.LoginService;
import co.istad.storage.Storage;
import co.istad.util.RoleEnum;
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

    /*
    do {
            homepageView.login(user, scanner);
            loginService.login(user);
            if (storage.getId() == null) {
                System.out.println("Invalid ID. Please try again.");
                continue login;
            } else
     */
//    public void adminDashboard(){
//        again:
//        while (true) {
//            if( storage.getId() == null ){
//                continue again;
//            }else
//                switch (RoleEnum.valueOf(storage.getRole().getRole().name())) {
//                    case ADMIN -> {
//                        HelperView.welcome("Welcome to admin dashboard");
//                        admin_inner:
//                        while (true) {
//                            int option = adminView.adminDashboardView(scanner);
//                            switch (option) {
//                                case 1: {
//                                    Long count = adminService.getUserCount();
//                                    System.out.println(count);
//                                }
//                                default: {
//                                    storage.setId(null);
//                                    continue again;
//                                }
//                            }
//                        }
//                    }
//                    case USER -> {
//                        HelperView.welcome("Welcome to user dashboard");
//                        return;
//                    }
//                    case LIBRARIAN -> {
//                        HelperView.welcome("Welcome to librarian dashboard");
//                    }
//                }
//        }
//    }
    public void adminDashboard(){
        User user = new User();
        do {
            int option = adminView.adminDashboardView(scanner);
            switch (option) {
                case 1 -> {
                    Long adminCount = adminService.getAdminCount();
                    System.out.println(adminCount);
                }
                case 2 -> {
                    Long userCount = adminService.getUserCount();
                    System.out.println(userCount);
                }
                default -> {
                    storage.setId(null);
                    return;
                }
            }
        }while (true);
    }
}


