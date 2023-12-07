package co.istad.controller;


import co.istad.model.Book;
import co.istad.model.Role;
import co.istad.model.User;
import co.istad.service.LoginService;
import co.istad.service.SignupService;
import co.istad.service.UserService;
import co.istad.storage.Storage;
import co.istad.util.Singleton;
import co.istad.view.HomepageView;
import co.istad.view.HelperView;
import co.istad.view.UserView;

import java.util.Scanner;

public class UserController {
    private final Scanner scanner;
    private Storage storage;
    private final SignupService signupService;
    private final LoginService loginService;
    private final HomepageView homepageView;
    private final UserView userView;
    public UserController(){
        loginService = Singleton.getLoginService();
        scanner = Singleton.scanner();
        userService = Singleton.getUserServiceImpl();
        storage = Singleton.getStorage();
        signupService = Singleton.getSignupService();
        homepageView = Singleton.getHomepageView();
        userView = Singleton.getUserView();
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
    public void userDashboard() {
        User user = new User();
        login:
        do {
            HelperView.welcome("=".repeat(50));
            HelperView.welcome("Welcome to user dashboard");
            HelperView.welcome("=".repeat(50));
            int option = userView.userDashboardView(scanner);
            switch (option) {
                case 1 -> {
                    int searchOption = userView.viewSearchOption(scanner);
                    switch (searchOption) {
                        case 1 -> {
                            do {
                                System.out.print("Please Enter Book ID : ");
                                try {
                                    long userInput = Long.parseLong(scanner.nextLine());
                                    userView.viewSearchBookById(userService.searchBookById(userInput));
                                    break;
                                } catch (NumberFormatException e) {
                                    System.out.println("Require Only Number");
                                }
                            } while (true);
                        }
                        case 2 -> {
                                System.out.print("Please Enter Book Title : ");
                                String userInput = scanner.nextLine();
                                userView.viewSearchBookByTitle(userService.searchBookByTitle(userInput));
                        }
                        case 3 -> {
                            System.out.print("Please Enter Author Name: ");
                            String userInput = scanner.nextLine();
                             userView.viewSearchBookByAuhtor(userService.searchBookByAuthor(userInput));
                        }
                        case 4 -> {
                            System.out.print("Please Enter Category Type: ");
                            String userInput = scanner.nextLine();
                            userView.viewSearchBookByCategory(userService.searchBookByCategory(userInput));
                        }
                        default -> throw new IllegalStateException();
                    }
                }
                case 2 -> {
                    Book[] books = new Book[3];
                    int bookIndex = 0;
                   do {
                     try{
                         System.out.println("Book Borrow Limit: 3");
                         userView.viewBorrowBook(scanner , bookIndex, books);
                         break ;
                     }catch (NumberFormatException e) {
                         System.out.println("Require Only Number");
                     }
                   } while (true);
                }
                case 3 -> userView.viewAllBook(userService.getAllBook());
                case 4 -> {
                    do {
                        System.out.print("Enter Book ID To Preview : ");
                        try{
                            long id = Long.parseLong(scanner.nextLine());
                            userView.viewPreviewBook(userService.previewBookById(id));
                            break ;
                        }catch (NumberFormatException e) {
                            System.out.println("Require Only Number");
                        }
                    } while (true);
                }
                case 5 -> {
                    userView.viewBookHistory(userService.bookHistory());
                }
                case 6 -> {
                    storage.setId(null);
                    return;
                }
            }
        }while (true);
    }

}
