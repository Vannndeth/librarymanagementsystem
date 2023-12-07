package co.istad.controller;

import co.istad.connection.ConnectionDb;
import co.istad.model.Book;
import co.istad.model.User;
import co.istad.service.LoginService;
import co.istad.storage.Storage;
import co.istad.util.RoleEnum;
import co.istad.util.Singleton;
import co.istad.view.HelperView;
import co.istad.view.HomepageView;
import co.istad.view.UserView;

import java.sql.Connection;
import java.util.Scanner;

public class LoginController {
    private final Scanner scanner;
    private final LoginService loginService;
    private final Storage storage;
    private final UserController userController;
    public LoginController(){
        scanner = Singleton.scanner();
        loginService = Singleton.getLoginService();
        storage = Singleton.getStorage();
        userController = Singleton.getUserController();
    }
    public void login(){
        User user = new User();
        login:
        do {
            HomepageView.login(user, scanner);
            loginService.login(user);
            if (storage.getId() == null) {
                System.out.println("Invalid ID. Please try again.");
                continue login;
            } else
                switch (RoleEnum.valueOf(storage.getRole().getRole().name())) {
                    case USER -> userController.getUserView();

                }
        }while (true);
    }

}
