package co.istad.controller;

import co.istad.model.Book;
import co.istad.model.User;
import co.istad.service.AdminService;
import co.istad.service.LoginService;
import co.istad.storage.Storage;
import co.istad.util.RoleEnum;
import co.istad.util.Singleton;
import co.istad.view.AdminView;
import co.istad.view.HelperView;
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
                        HelperView.welcome("=".repeat(50));
                        HelperView.welcome("Welcome to user dashboard");
                        HelperView.welcome("=".repeat(50));
                        int option = userView.userDashboardView(scanner);
                        switch (option) {
                            case 1 -> {
                                System.out.println("Option Menu");
                                System.out.println("1: Search By Id");
                                System.out.println("2: Search By Title");
                                System.out.println("3: Search By Author");
                                System.out.println("4: Search By Category");
                                System.out.print("Please choose Option ");
                                int userOpt = Integer.parseInt(scanner.nextLine());
                                switch (userOpt) {
                                    case 1 -> {
                                        System.out.print("Please Enter Book ID : ");
                                        long userInput = Long.parseLong(scanner.nextLine());
                                        Singleton.getUserServiceImpl().searchBookById(userInput);
                                    }
                                    case 2 -> {
                                        System.out.print("Please Enter Book Title : ");
                                        String userInput = scanner.nextLine();
                                        Singleton.getUserServiceImpl().searchBookByTitle(userInput);
                                    }
                                    case 3 -> {
                                        System.out.print("Please Enter Author Name: ");
                                        String userInput = scanner.nextLine();
                                        Singleton.getUserServiceImpl().searchBookByAuthor(userInput);
                                    }
                                    case 4 -> {
                                        System.out.print("Please Enter Category Type: ");
                                        String userInput = scanner.nextLine();
                                        Singleton.getUserServiceImpl().searchBookByCategory(userInput);
                                    }
                                    default -> throw new IllegalStateException();
                                }
                            }
                            case 2 -> {
                                Book[] books = new Book[3];
                                System.out.println("Book Borrow Limit: 3");
                                int bookIndex = 0;
                                while (true) {
                                    System.out.print("Enter Book ID to borrow: ");
                                    Long bookId = Long.parseLong(scanner.nextLine());
                                    System.out.print("Book ID: " + bookId + ". Confirm borrowing? (Y/N): ");
                                    String userInput = scanner.nextLine();
                                    if (userInput.equalsIgnoreCase("Y")) {
                                        books[bookIndex++] = new Book(bookId);
                                        Singleton.getUserServiceImpl().borrowBook(bookId);
                                        System.out.println("Book added to your borrowed books.");
                                        if (bookIndex < books.length) {
                                            System.out.print("Borrow another book? (Y/N): ");
                                            userInput = scanner.nextLine();
                                            if (!userInput.equalsIgnoreCase("Y")) {
                                                break;
                                            }
                                        } else {
                                            System.out.println("You have reached the maximum borrow limit of 3 books.");
                                            break;
                                        }
                                    } else {
                                        System.out.println("Invalid input. Please enter Y or N.");
                                    }
                                }

                                System.out.println("Borrowing completed.");
                                System.out.println("Borrowed books:");
                                System.out.println("Waiting For Confirm");
                                for (Book book : books) {
                                    if (book != null) {
                                        System.out.println("Book ID: " + book.getId());
                                    }
                                }
                            }
                            case 3 -> Singleton.getUserServiceImpl().getAllBook();
                            case 4 -> {
                                System.out.print("Enter Book ID To Preview : ");
                                long id = Long.parseLong(scanner.nextLine());
                                Singleton.getUserServiceImpl().previewBookById(id);
                            }
                            case 5 -> {
                                Singleton.getUserServiceImpl().bookHistory();
                            }
                            case 6 -> {
                                storage.setId(null);
                                return;
                            }
                            default -> throw new IllegalStateException();
                        }
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
