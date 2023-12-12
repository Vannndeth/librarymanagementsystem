package co.istad.util;

import co.istad.controller.*;
import co.istad.dao.AdminDaoImpl;
import co.istad.dao.LibrarianDao;
import co.istad.dao.LibrarianDaoImpl;
import co.istad.dao.UserDaoImpl;
import co.istad.service.*;
import co.istad.storage.Storage;
import co.istad.view.AdminView;
import co.istad.view.HomepageView;
import co.istad.view.LibrarianView;
import co.istad.view.UserView;

import java.util.Scanner;

public class Singleton {
    private static Scanner scanner;
    private static UserDaoImpl userDao;
    private static UserServiceImpl userService;
    private static UserController userController;
    private static AdminDaoImpl adminDao;
    private static AdminServiceImpl adminService;
    private static AdminView adminView;
    private static AdminController adminController;
    private static LoginController loginController;
    private static LoginService loginService;
    private static SignupService signupService;
    private static SignupController signupController;
    private static Seeder seeder;
    private static Storage storage;
    private static HomepageView homepageView;
    private static UserView userView;
    private static AdminUtil adminUtil;
    private static LibrarianController librarianController;
    private static LibrarianDao librarianDao;
    private static LibrarianView librarianView;
    private static LibrarianService librarianService;


    public static Scanner scanner(){
        if(scanner == null){
            scanner = new Scanner(System.in);
        }
        return scanner;
    }
    public static UserDaoImpl getUserDaoImpl(){
        if(userDao == null){
            userDao = new UserDaoImpl();
        }
        return userDao;
    }
    public static UserServiceImpl getUserServiceImpl(){
        if(userService == null){
            userService = new UserServiceImpl();
        }
        return userService;
    }
    public static AdminDaoImpl getAdminDaoImpl(){
        if(adminDao == null){
            adminDao = new AdminDaoImpl();
        }
        return adminDao;
    }
    public static AdminServiceImpl getAdminServiceImpl(){
        if(adminService == null){
            adminService = new AdminServiceImpl();
        }
        return adminService;
    }

    public static AdminController getAdminController(){
        if(adminController == null){
            adminController = new AdminController();
        }
        return adminController;
    }
    public static UserController getUserController(){
        if(userController == null){
            userController = new UserController();
        }
        return userController;
    }
    public static Seeder getSeeder(){
        if(seeder == null){
            seeder = new Seeder();
        }
        return seeder;
    }

    public static Storage getStorage() {
        if (storage == null){
            storage = new Storage();
        }
        return storage;
    }
    public static AdminView getAdminView() {
        if (adminView == null){
            adminView = new AdminView();
        }
        return adminView;
    }


    public static LoginController getLoginController(){
        if( loginController == null ) {
            loginController = new LoginController();
        }
        return loginController;
    }
    public static LoginService getLoginService(){
        if( loginService == null ) {
            loginService = new LoginService();
        }
        return loginService;
    }
    public static SignupService getSignupService(){
        if( signupService == null ) {
            signupService = new SignupService();
        }
        return signupService;
    }
    public static SignupController getSignupController(){
        if( signupController == null ) {
            signupController = new SignupController();
        }
        return signupController;
    }
    public static HomepageView getHomepageView(){
        if( homepageView == null ) {
            homepageView = new HomepageView();
        }
        return homepageView;
    }
    public static UserView getUserView(){
        if( userView == null ) {
            userView = new UserView();
        }
        return userView;
    }
    public static AdminUtil getAdminUtil(){
        if(adminUtil == null){
            adminUtil = new AdminUtil();
        }
        return adminUtil;
    }

    public static LibrarianController getLibrarianController(){
        if( librarianController == null ) librarianController = new LibrarianController();
        return librarianController;
    }

    public static LibrarianDao getLibrarianDao(){
        if( librarianDao == null ) librarianDao = new LibrarianDaoImpl();
        return librarianDao;
    }

    public static LibrarianView getLibrarianView(){
        if( librarianView == null ) librarianView = new LibrarianView();
        return librarianView;
    }
    public static LibrarianService getLibrarianService(){
        if( librarianService == null ) librarianService = new LibrarianServiceImpl();
        return librarianService;
    }

}
