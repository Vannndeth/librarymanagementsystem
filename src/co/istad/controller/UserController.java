package co.istad.controller;


import co.istad.model.Book;
import co.istad.model.Role;
import co.istad.model.User;
import co.istad.service.LoginService;
import co.istad.service.MainFeatureService;
import co.istad.service.SignupService;
import co.istad.service.UserService;
import co.istad.storage.Storage;
import co.istad.util.RoleEnum;
import co.istad.util.Singleton;
import co.istad.view.HomepageView;
import co.istad.view.LibrarianView;
import co.istad.view.HelperView;
import co.istad.view.UserView;

import java.util.Scanner;

public class UserController {
    private final Scanner scanner;
    private Storage storage;
    private final SignupService signupService;
    private final LoginService loginService;
    private final HomepageView homepageView;
    public UserController(){
        loginService = Singleton.getLoginService();
        scanner = Singleton.scanner();
        userService = Singleton.getUserServiceImpl();
        storage = Singleton.getStorage();
        signupService = Singleton.getSignupService();
        homepageView = Singleton.getHomepageView();
    }
    private final UserService userService;
    public void signup(){
        User user = new User();
        user.setRole(new Role());
        user.getRole().setId(3L);
        homepageView.signup(user, scanner);
        user = signupService.signup(user);
        System.out.println(user);
    }



}
