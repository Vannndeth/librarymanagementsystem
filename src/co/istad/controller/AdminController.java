package co.istad.controller;

import co.istad.model.User;
import co.istad.service.AdminService;
import co.istad.storage.Storage;
import co.istad.util.RoleEnum;
import co.istad.util.Singleton;
import co.istad.view.AdminView;
import co.istad.view.HelperView;
import co.istad.view.HomepageView;
import co.istad.view.UserView;

import java.util.Scanner;

public class AdminController {

    private final Scanner scanner;
    private final Storage storage;
    private final AdminService adminService;
    public AdminController(){
        scanner = Singleton.scanner();
        adminService = Singleton.getAdminServiceImpl();
        storage = Singleton.getStorage();
    }

    public void login(){
        User user = new User();
        HomepageView.login(user, scanner);
        while (true) {
            if( storage.getId() == null ){
//                adminService.login(user);
            }else
                switch (RoleEnum.valueOf(storage.getRole().getRole().name())) {
                    case ADMIN -> {
                        HelperView.welcome("Welcome to admin dashboard");
                        admin_inner:
                        while (true) {
                            System.out.print("Please choose option: ");
                            int option = Integer.parseInt(scanner.nextLine());
                            System.out.println("Opion : " + option );
                            switch (option) {
                                case 1:
                                    System.out.println("mana admin");
                                    break;
                                case 2 :
                                    System.out.println("mana librarian");
                                    break;
                                case 3 :
                                    System.out.println("mana user");
                                    break;
                                case 4 :
                                    storage.setId(null);
                                    storage.setUsername(null);
                                    storage.setRole(null);
                                    break admin_inner;
                                default :
                                    System.err.println("please select one of menu list");
                            }
                        }
                    }
                    case USER -> {
                        HelperView.welcome("Welcome to user dashboard");
                        return;
                    }
                    case LIBRARIAN -> {
                        HelperView.welcome("Welcome to librarian dashboard");
                    }
                }
        }
    }
}
