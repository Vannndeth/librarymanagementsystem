package co.istad.controller;

import co.istad.connection.ConnectionDb;
import co.istad.model.User;
import co.istad.service.LoginService;
import co.istad.util.Singleton;
import co.istad.view.HomepageView;

import java.sql.Connection;
import java.util.Scanner;

public class LoginController {
    private final Connection connection;
    private final Scanner scanner;
    private final LoginService loginService;
    public LoginController(){
        connection = ConnectionDb.getConnection();
        scanner = Singleton.scanner();
        loginService = Singleton.getLoginService();
    }
    public void login(){
        User user = new User();
        HomepageView.login( user, scanner );
        loginService.login(user);
    }

}
