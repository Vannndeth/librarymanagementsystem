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
                    int searchOption = userView.searchOption(scanner);
                    switch (searchOption) {
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
            }
        }while (true);
    }

}
