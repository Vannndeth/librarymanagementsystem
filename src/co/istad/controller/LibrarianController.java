package co.istad.controller;

import co.istad.model.Author;
import co.istad.model.Book;
import co.istad.model.Borrow;
import co.istad.model.User;
import co.istad.service.LibrarianService;
import co.istad.storage.Storage;
import co.istad.util.Helper;
import co.istad.util.LibrarianUtil;
import co.istad.util.Pagination;
import co.istad.util.Singleton;
import co.istad.view.HelperView;
import co.istad.view.LibrarianView;
import org.apache.logging.log4j.simple.SimpleLoggerContextFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.Configurator;
import org.apache.poi.xssf.model.Styles;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.LogManager;

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
                    //Backup and Recovery
                    backupAndRecoveryPage();
                }
                case 5 -> {
                    //Generate Report
                    System.setProperty("log4j2.loggerContextFactory", SimpleLoggerContextFactory.class.getName());


                    try {
                        // Your code
                        org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(LibrarianController.class);
                        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
                        XSSFSheet sheet = xssfWorkbook.createSheet("sheet1");
                        System.out.println( librarianService.getReport().size() );
                        librarianView.reportView( librarianService.getReport(), 1,1,1, false );
                        int rownum = 0;
                        for (User user : librarianService.getReport()) {
                            Row row = sheet.createRow(rownum++);
                            if( rownum == 1 ){
                                Cell cell = row.createCell(0);
                                cell.setCellValue("Id");
                                cell = row.createCell(1);
                                cell.setCellValue("Title");
                            }else{
                                createUserReport(user, row);
                            }
                        }
                        FileOutputStream out = new FileOutputStream(new File("/home/sunlyhuor/dir/test.xlsx"));
                        xssfWorkbook.write(out);
                        out.close();
                        HelperView.message("Generated Successfully");
                    } catch (IOException e) {
                        HelperView.error(e.getMessage());
                        return;
                    }
                }
                case 6 -> {
                    //Logout
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
                    bookUpdatePage();
                }
                case 3 -> {
                    //Search Book
                    searchBookPage();
                }
                case 4 -> {
                    // Show Book
                    int totalPage = librarianService.getAllBook().size();
                    Pagination pagination = new Pagination(1,1 , 3 );
                    do{
                        pagination.setTotalPage( (int)Math.ceil(( totalPage / (float)pagination.getLimit() ))  );
                        librarianView.bookView(
                                librarianService.bookPagination( pagination.getCurrentPage(), pagination.getLimit() ),
                                pagination.getCurrentPage(),
                                pagination.getTotalPage(),
                                (pagination.getLimit()),
                                true
                        );
                        librarianView.showBookMenu( librarianUtil );
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
                                //Report Book

                            }
                            case 8 -> {
                                return;
                            }
                            default -> {
                                HelperView.error("Enter number above menu!");
                            }
                        }
                    }while (true);
                }
                case 5 -> {
                    // Borrow Page
                    borrowPage();
                }
                case 6 -> {
                    //Exit
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
                    List<Book> books = librarianService.searchBooksByTitle( title.get() );
                    librarianView.bookView( books, 1,1,1, false );
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

    private void backupAndRecoveryPage(){
        HelperView.welcome("=".repeat(50));
        HelperView.welcome("Welcome to Backup and Recovery page");
        HelperView.welcome("=".repeat(50));
        do {
            LibrarianUtil librarianUtil = new LibrarianUtil();
            librarianView.backupAndRecoveryView( librarianUtil );
            switch (librarianUtil.getOption()){
                case 1 -> {
                    //Backup
                    librarianService.backup();
                }
                case 2 -> {
                    //Recovery
                }
                case 3 -> {
                    //Exit
                    return;
                }
                default -> {
                    HelperView.error("Please enter number above of menu!");
                }
            }
        }while (true);
    }

    private void borrowPage(){
        HelperView.welcome("=".repeat(50));
        HelperView.welcome("Welcome to Borrow page");
        HelperView.welcome("=".repeat(50));
        do {
            LibrarianUtil librarianUtil = new LibrarianUtil();
            librarianView.borrowMenu( librarianUtil );
            switch (librarianUtil.getOption()){
                case 1 -> {
                    //Lists All Borrow

                }
                case 2 -> {
                    //Confirm Borrow
                    Borrow borrow = new Borrow();
                    librarianView.confirmBookView( borrow );
                    System.out.print( String.format("Are you want to confirm this borrow id %s ?", borrow.getId()) );
                    Character opt = scanner.nextLine().charAt(0);
                    switch (opt.toString().toLowerCase()){
                        case "y" -> {
                            if( librarianService.confirmBorrow( borrow ) ){
                                HelperView.message("Confirm Successfully");
                            }
                        }
                        case "c" -> {
                            HelperView.error("Canceled");
                        }
                    }
                }
                case 3 -> {
                    //Exit
                    return;
                }
                default -> {
                    HelperView.error("Please enter number above of menu!");
                }
            }
        }while (true);
    }

    private void bookUpdatePage(){
        HelperView.welcome("=".repeat(50));
        HelperView.welcome("Welcome to Borrow page");
        HelperView.welcome("=".repeat(50));
        do {
            LibrarianUtil librarianUtil = new LibrarianUtil();
            Book book = new Book();
            librarianView.bookConfirmBookId( book );
            if( book.getId() == null ){
                return;
            }
            librarianView.bookUpdateMenu( librarianUtil );
            switch (librarianUtil.getOption()){
                case 1 -> {
                    //Update Book Title
                    librarianView.bookUpdateTitleView( book );
                    if( !book.getTitle().isEmpty() ){
                        Book res = librarianService.updateBookById(book.getId(), book);
                        if( res != null ) HelperView.message("Book updated successfully!");
                    }else return;

                }
                case 2 -> {
                    //Update Book Quantity
                    librarianView.bookUpdateQuantityView( book );
                    if( book.getQuantity() > 0 ){
                        Book res = librarianService.updateBookById(book.getId(), book);
                        if( res != null ) HelperView.message("Book updated successfully!");
                    }else return;
                }
                case 3 -> {
                    //Update Book Author
                    Author author = new Author();
                    librarianView.authConfirmId( author );
                    if( author.getId() == null ){
                        return;
                    }
                    book.setAuthor(author);
                    Book res = librarianService.updateBookById(book.getId(), book);
                    if( res != null ) HelperView.message("Book updated successfully!");
                }
                case 4 -> {
                    //Exit
                    return;
                }
                default -> {
                    HelperView.error("Please enter number above of menu!");
                }
            }
        }while (true);
    }

    private static void createBookList(Book book, Row row) {
        Cell cell = row.createCell(0);
        cell.setCellValue(book.getId());

        cell = row.createCell(1);
        cell.setCellValue( book.getTitle() );
    }

    private static void createUserReport(User user, Row row) {
        Cell cell = row.createCell(0);
        cell.setCellValue(user.getId());

        cell = row.createCell(1);
        cell.setCellValue( user.getBorrow().getBook().getTitle() );
    }

}
