package co.istad.controller;

import co.istad.model.User;
import co.istad.service.SignupService;
import co.istad.util.Singleton;
import co.istad.view.HomepageView;
import co.istad.view.UserView;

import java.util.Scanner;

public class SignupController {
    private final Scanner scanner;
    private final SignupService signupService;
    public SignupController(){
        scanner = Singleton.scanner();
        signupService = Singleton.getSignupService();
    }
    public void signup(){
        User user = new User();
        user.setRole(new co.istad.model.Role());
        user.getRole().setId(3L);
        HomepageView.signup(user, scanner);
        signupService.signup(user);
    }
}
