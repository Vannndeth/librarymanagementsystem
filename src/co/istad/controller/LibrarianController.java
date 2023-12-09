package co.istad.controller;

import co.istad.model.Author;
import co.istad.service.LibrarianService;
import co.istad.storage.Storage;
import co.istad.util.Helper;
import co.istad.util.LibrarianUtil;
import co.istad.util.Singleton;
import co.istad.view.HelperView;
import co.istad.view.LibrarianView;

import java.util.List;
import java.util.Scanner;

public class LibrarianController {
    private final LibrarianView librarianView;
    private final LibrarianService librarianService;
    private final Scanner scanner;
    private final Storage storage;

    public LibrarianController(){
        librarianView = Singleton.getLibrarianView();
        librarianService = Singleton.getLibrarianService();
        scanner = Singleton.scanner();
        storage = Singleton.getStorage();
    }
    public void librarianDashboard(){
        librarianView.welcome();
        do{
            LibrarianUtil librarianUtil = new LibrarianUtil();
            librarianView.mainMenu( librarianUtil );
            switch ( librarianUtil.getOption() ){
                case 1 -> {
                    System.out.println("Book");
                }
                case 2 -> {
                    authorPage();
                }
                case 3 -> {
                    System.out.println("User");
                }
                case 4 -> {
                    HelperView.message("Logout Successfully");
                    storage.setId(null);
                    return;
                }
                default -> {
                    HelperView.error("Please enter option above menu!");
                }
            }
        }while (true);
    }

    private void authorPage(){
        HelperView.welcome("=".repeat(50));
        HelperView.welcome("Welcome to Author page");
        HelperView.welcome("=".repeat(50));
        do{
            LibrarianUtil librarianUtil = new LibrarianUtil();
            librarianView.authorMenu(librarianUtil);
            switch ( librarianUtil.getOption() ){
                case 1 -> {
                    //Add Author
                    Author author = new Author();
                    librarianView.addAuthorView( author );
                    if( author.getLastName().isEmpty() || author.getFirstName().isEmpty() || author.getEmail().isEmpty() ){
                        HelperView.error("All fields are required!");
                    }else if( author.getFirstName().length() > 50 ){
                        HelperView.error("The firstname field must be less than 50 characters!\n");
                    }else if( author.getLastName().length() > 50 ){
                        HelperView.error("The lastname field must be less than 50 characters!\n");
                    }else if( !Helper.isEmail(author.getEmail()) ){
                        HelperView.error("Invalided Email!\n");
                    }else{
                        if( librarianService.createAuthor(author) != null ){
                            HelperView.message("Added Successfully");
                        }
                    }

                }
                case 2 -> {
                    //Update Author
                    updateAuthorPage();
                }
                case 3 -> {
                    //Search User
                    System.out.println("Search Author");
                }
                case 4 -> {
                    // Get All Authors
                    List<Author> authors = librarianService.getAll();
                    librarianView.authorView(authors);
                }
                case 5 -> {
                    //Logout
                    storage.setId(null);
                    return;
                }
                default -> {
                    HelperView.error("Please enter option above menu!\n\n");
                }
            }
        }while (true);
    }

    private void updateAuthorPage(){
        HelperView.welcome("=".repeat(50));
        HelperView.welcome("Welcome to Author Update page");
        HelperView.welcome("=".repeat(50));
        Author author = new Author();
        librarianView.updateAuthorByIdView( author );
        if( author.getId() != null ){
            System.out.print("Press Y to update and N to cancel update : ");
            Character op = scanner.nextLine().charAt(0);
            if(op.toString().equalsIgnoreCase("y")){
                if( librarianService.updateAuthorById( author.getId(), author ) != null){
                    HelperView.message("\nAuthor Updated Successfully!");
                }else{
                    HelperView.error("\nAuthor Updated Failed!");
                }
            }else{
                HelperView.error("You have cancel for update!");
            }
        }else{
            HelperView.error("\nAuthor Updated Failed!");
        }
    }



}
