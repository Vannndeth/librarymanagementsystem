package co.istad.controller;

import co.istad.model.User;
import co.istad.service.AdminService;
import co.istad.service.LoginService;
import co.istad.storage.Storage;
import co.istad.util.RoleEnum;
import co.istad.util.Singleton;
import co.istad.view.AdminView;
import co.istad.view.HomepageView;
import co.istad.view.UserView;

import java.util.Scanner;

public class LoginController {
    private final Scanner scanner;
    private final LoginService loginService;
    private final Storage storage;
    private final UserController userController;
    private final UserView userView;
    private final AdminService adminService;
    private final AdminView adminView;
    private final AdminController adminController;
    private final HomepageView homepageView;
    public LoginController(){
        scanner = Singleton.scanner();
        loginService = Singleton.getLoginService();
        storage = Singleton.getStorage();
        userController = Singleton.getUserController();
        userView = Singleton.getUserView();
        adminService = Singleton.getAdminServiceImpl();
        adminView = Singleton.getAdminView();
        adminController = Singleton.getAdminController();
        homepageView = Singleton.getHomepageView();
    }
    public void login(){
        User user = new User();
        login:
        do {
            homepageView.login(user, scanner);
            loginService.login(user);
            if (storage.getId() == null) {
                System.out.println("Invalid ID. Please try again.");
                continue login;
            } else {
                switch (RoleEnum.valueOf(storage.getRole().getRole().name())) {
                    case USER -> {
                        userController.userDashboard();
                        storage.setId(null);
                        return;
                    }
                    case LIBRARIAN -> {
                        System.out.println("LIBRARIAN");
                    }
                    case ADMIN -> {
                        adminController.adminDashboard();
                        storage.setId(null);
                        return;
                    }
                }
            }
        }while (true);
    }
}
