package co.istad.controller;

import co.istad.model.Author;
import co.istad.model.Book;
import co.istad.service.LibrarianService;
import co.istad.storage.Storage;
import co.istad.util.Helper;
import co.istad.util.LibrarianUtil;
import co.istad.util.Pagination;
import co.istad.util.Singleton;
import co.istad.view.HelperView;
import co.istad.view.LibrarianView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

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
                    //Book
                    bookPage();
                }
                case 2 -> {
                    //Completed - 10/12/2023
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

    //Author Page
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
                    addAuthorPage();
                }
                case 2 -> {
                    //Update Author
                    updateAuthorPage();
                }
                case 3 -> {
                    //Search Author
                    searchAuthorPage();
                }
                case 4 -> {
                    // Show Authors
                    int totalPage = librarianService.getAll().size();
                    Pagination pagination = new Pagination(1,1 , 3 );
                    do{
                        pagination.setTotalPage( (int)Math.ceil((float) ( totalPage / (float)pagination.getLimit() ))  );
                        librarianView.authorView(
                            librarianService.authorPagination( pagination.getCurrentPage(), pagination.getLimit() ),
                            pagination.getCurrentPage(),
                            pagination.getTotalPage(),
                            (pagination.getLimit()),
                            true
                        );
                        librarianView.showAuthorMenu( librarianUtil );
                        switch ( librarianUtil.getOption() ){
                            case 1 -> {
                                //Next
                                pagination.setCurrentPage( pagination.getCurrentPage() + 1 );
                                if( pagination.getCurrentPage() > pagination.getTotalPage() ){
                                    pagination.setCurrentPage(pagination.getTotalPage() );
                                }
                            }
                            case 2 -> {
                                //Previous
                                pagination.setCurrentPage( pagination.getCurrentPage() - 1 );
                                if( pagination.getCurrentPage() < 1){
                                    pagination.setCurrentPage( 1 );
                                }
                            }
                            case 3 -> {
                                //Goto
                                try{
                                    System.out.print("\t-->Enter Page You're Want To Go : ");
                                    pagination.setCurrentPage( Integer.parseInt(scanner.nextLine()) );
                                    if( pagination.getCurrentPage() > pagination.getTotalPage() ){
                                        pagination.setCurrentPage(pagination.getTotalPage() );
                                    }
                                }catch (Exception ex){
                                    pagination.setCurrentPage( pagination.getCurrentPage() );
                                }
                            }
                            case 4 -> {
                                //First
                                pagination.setCurrentPage( 1 );
                            }
                            case 5 -> {
                                //Last
                                pagination.setCurrentPage( pagination.getTotalPage() );
                            }
                            case 6 -> {
                                // Set Limit
                                AtomicInteger limit = new AtomicInteger();
                                librarianView.setLimitView( limit );
                                pagination.setLimit( limit.get() );
                            }
                            case 7 -> {
                                return;
                            }
                            default -> {
                                HelperView.error("Enter number above menu!");
                            }
                        }
                    }while (true);
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

    //Author Component
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

    //Author Component
    private void addAuthorPage(){
        HelperView.welcome("=".repeat(50));
        HelperView.welcome("Welcome to Author Update page");
        HelperView.welcome("=".repeat(50));
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

    //Author Component
    private void searchAuthorPage(){
        HelperView.welcome("=".repeat(50));
        HelperView.welcome("Welcome to Author Search page");
        HelperView.welcome("=".repeat(50));
        LibrarianUtil librarianUtil = new LibrarianUtil();
        do{
            librarianView.searchAuthView( librarianUtil );
            switch ( librarianUtil.getOption() ){
                case 1 -> {
                    Author author = new Author();
                    librarianView.searchAuthorByIdView( author );
                    if( author.getId() != null ){
                        Author auth = librarianService.searchAuthorById(author.getId());
                        List<Author> authors = new ArrayList<>();
                        authors.add(auth);
                        librarianView.authorView( authors, 1, 1, 5, false  );
                    }
                }
                case 2 -> {
                    Author author = new Author();
                    librarianView.searchAuthorByNameView( author );
                    if( author.getFirstName() != null ){
                        List<Author> auths = librarianService.searchAuthorByName(author.getFirstName());
                        librarianView.authorView( auths, 1, 1, 1,false );
                    }
                }
                case 3 -> {
                    storage.setId(null);
                    return;
                }
                default -> {
                    HelperView.error("Enter number of above menu!");
                }
            }
        }while (true);
    }

    //Book Page
    private void bookPage(){
        HelperView.welcome("=".repeat(50));
        HelperView.welcome("Welcome to Book page");
        HelperView.welcome("=".repeat(50));
        do{
            LibrarianUtil librarianUtil = new LibrarianUtil();
            librarianView.bookMenu(librarianUtil);
            switch ( librarianUtil.getOption() ){
                case 1 -> {
                    //Add Book
                    Book book = new Book();
                    librarianView.createBookView( book );
                    if( book.getTitle().isEmpty() ||
                        book.getDescription().isEmpty() ||
                        book.getQuantity() == null ||
                        book.getAuthor().getId() == null
                    ){
                        HelperView.error("All fields are required!");
                    }else if( book.getTitle().length() > 50 ) {
                        HelperView.error("The book title field must be less than 50characters");
                    }else{
                        if( librarianService.createBook( book ) != null ){
                            HelperView.message("Book created successfully!");
                        }
                    }
                }
                case 2 -> {
                    //Update Book
                }
                case 3 -> {
                    //Search Book
                    searchBookPage();
                }
                case 4 -> {
                    // Show Book
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

    //Book Component
    private void searchBookPage(){
        HelperView.welcome("=".repeat(50));
        HelperView.welcome("Welcome to Search Book page");
        HelperView.welcome("=".repeat(50));
        do {
            LibrarianUtil librarianUtil = new LibrarianUtil();
            librarianView.searchBookView( librarianUtil );
            switch (librarianUtil.getOption()){
                case 1 -> {
                    //Search Book By Id
                    List<Book> books = new ArrayList<>();
                    AtomicLong id = new AtomicLong();
                    librarianView.searchBookByIdView( id );
                    Optional<Book> book = librarianService.searchBookById(id.get());
                    if( book.isEmpty() ){
                        librarianView.bookView(books, 1,1,1,false);
                    }else{
                        books.add(book.get());
                        librarianView.bookView(books, 1,1,1,false);
                    }

                }
                case 2 -> {
                    //Search Book By Title
                    AtomicReference<String> title = new AtomicReference<>();
                    librarianView.searchBookByTitle( title );
                    System.out.println( title.get() );
                }
                case 3 -> {
                    //Search Book By Author
                }
                case 4 -> {
                    //Search Book By Category
                }
                case 5 -> {
                    //Exit
                    return;
                }
                default -> {
                    HelperView.error("Please enter number above of menu!");
                }
            }
        }while (true);
    }

}
