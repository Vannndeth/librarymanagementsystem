package co.istad.init;

import co.istad.controller.AdminController;
import co.istad.controller.LoginController;
import co.istad.controller.SignupController;
import co.istad.controller.UserController;
import co.istad.storage.Storage;
import co.istad.util.Seeder;
import co.istad.util.Singleton;
import co.istad.view.AdminView;
import co.istad.view.HelperView;
import co.istad.view.HomepageView;
import java.util.Scanner;

public class MainApplication {
    private final Scanner scanner;
    private final UserController userController;
    private final Seeder seeder;
    private final Storage storage;
    private final AdminController adminController;
    private final LoginController loginController;
    private final SignupController signupController;
    private final HomepageView homepageView;
    private final AdminView adminView;
    public MainApplication(){
        scanner = Singleton.scanner();
        userController = Singleton.getUserController();
        seeder = Singleton.getSeeder();
        seeder.roleSeeder();
        seeder.adminSeeder();
        seeder.librarianSeeder();
        storage = Singleton.getStorage();
        adminController = Singleton.getAdminController();
        loginController = Singleton.getLoginController();
        signupController = Singleton.getSignupController();
        homepageView = Singleton.getHomepageView();
        adminView = Singleton.getAdminView();
    }
    public void initialize(){
        HomepageView.logo();
        homepageView.registerOption(scanner);
        again:
        do {
            int option = adminView.option(scanner);
            switch (option){
                case 1 -> {
                    loginController.login();
                }
                case 2 -> {
                    signupController.signup();
                }
                case 3 -> {
                    System.exit(0);
                }
                default -> {
                    HelperView.message("Please choose option above...!");
                    continue again;
                }
            }
        }while (true);
    }
    public static void main(String[] args) {
        new MainApplication().initialize();
    }
}
