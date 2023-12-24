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
                    userPage();
                }
                case 4 -> {
                    //Backup and Recovery
                    backupAndRecoveryPage();
                }
                case 5 -> {
                    //Completed 22-12-2023
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
                            if( rownum == 0 ){
                                Row row = sheet.createRow(rownum++);
                                //User Id
                                Cell cell = row.createCell(0);
                                cell.setCellValue("Id");

                                //User Name
                                cell = row.createCell(1);
                                cell.setCellValue("Username");

                                //User Email
                                cell = row.createCell(2);
                                cell.setCellValue("Email");

                                //Book Title
                                cell = row.createCell(3);
                                cell.setCellValue("Book Title");

                                //Borrow
                                cell = row.createCell(4);
                                cell.setCellValue("Borrow");

                                //Return
                                cell = row.createCell(5);
                                cell.setCellValue("Return");

                                //Borrow At
                                cell = row.createCell(6);
                                cell.setCellValue("Borrow At");

                                //DeadLine Date
                                cell = row.createCell(7);
                                cell.setCellValue( "Deadline" );
                            }
                            Row row = sheet.createRow(rownum++);
                            createUserReport(user, row);
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
                    //Category
                    categoryPage();
                }
                case 7 -> {
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
                    if( book.getTitle() == null ||
                        book.getDescription() == null ||
                        book.getQuantity() == null ||
                        book.getAuthor() == null
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
                    // Borrow And Return Page
                    borrowAndReturnPage();
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

    private void borrowAndReturnPage(){
        HelperView.welcome("=".repeat(50));
        HelperView.welcome("Welcome to Borrow And Return page");
        HelperView.welcome("=".repeat(50));
        do {
            LibrarianUtil librarianUtil = new LibrarianUtil();
            librarianView.borrowAndReturnMenu( librarianUtil );
            switch (librarianUtil.getOption()){
                case 1 -> {
                    //Lists All Borrow;
                    librarianView.borrowView( librarianService.getAllBorrow(), 1,1,1, false );
                }
                case 2 -> {
                    //List All Return Book
                    librarianView.returnView( librarianService.getAllReturn(),1,1,1,false );
                }
                case 3 -> {
                    //Confirm Borrow
                    Borrow borrow = new Borrow();
                    librarianView.confirmBookView( borrow );
                    System.out.print( String.format("Are you want to confirm this borrow id %s ? : ", borrow.getId()) );
                    Character opt = 'c';
                    try{
                        opt = scanner.nextLine().charAt(0);
                    }catch (Exception ex){
                        opt = 'c';
                    }
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
                case 4 -> {
                    //Confirm Return Book
                    User user = new User();
                    Book book = new Book();
                    librarianView.confirmReturnView(user, book);
                    if(librarianService.returnBook(user, book)) {
                        HelperView.message(String.format("User Id(%s) returned book id(%s)", user.getId(), book.getId() ));
                    }
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

    private static void createUserReport(User user, Row row) {
        //User Id
        Cell cell = row.createCell(0);
        cell.setCellValue(user.getId());

        //User Name
        cell = row.createCell(1);
        cell.setCellValue( user.getUsername() );

        //User Email
        cell = row.createCell(2);
        cell.setCellValue( user.getEmail() );

        //Book Title
        cell = row.createCell(3);
        cell.setCellValue( user.getBorrow().getBook().getTitle() );

        //Borrow
        cell = row.createCell( 4 );
        cell.setCellValue( user.getBorrow().isBorrow() ? "Borrow" : "Not Confirm Yet" );

        //Return
        cell = row.createCell(5);
        cell.setCellValue( user.getBorrow().isReturn() ? "Return" : "Not Return Yet" );

        //Borrow At
        cell = row.createCell(6);
        cell.setCellValue( user.getBorrow().getBorrowDate().toString() );

        //Deadline
        cell = row.createCell(7);
        cell.setCellValue( user.getBorrow().getDeadlineBorrowDate().toString() );
    }

    private void userPage(){
        HelperView.welcome("=".repeat(50));
        HelperView.welcome("Welcome to User page");
        HelperView.welcome("=".repeat(50));
        LibrarianUtil librarianUtil = new LibrarianUtil();
        while (true){
            librarianView.userMenu(librarianUtil);
            switch (librarianUtil.getOption()){
                case 1 ->{
                    //Lists all user
                    int totalPage = librarianService.getAllUser(1, 1000).size();
                    Pagination pagination = new Pagination(1,1 , 3 );
                    do{
                        pagination.setTotalPage( (int)Math.ceil(( totalPage / (float)pagination.getLimit() ))  );
                        librarianView.userView(
                                librarianService.getAllUser( pagination.getCurrentPage(), pagination.getLimit() ),
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
                case 2 ->{
                    //List All BlackList User
                    int totalPage = librarianService.getBlackListUser( 1, 1000 ).size();
                    Pagination pagination = new Pagination(1,1 , 3 );
                    do{
                        pagination.setTotalPage( (int)Math.ceil(( totalPage / (float)pagination.getLimit() ))  );
                        librarianView.userBlackListView(
                                librarianService.getBlackListUser( pagination.getCurrentPage(), pagination.getLimit() ),
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
                case 3 ->{
                    //Search User
                    userSearchPage();
                }
                case 4 ->{
                    //Add User To Blacklist
                    User user = new User();
                    Book book = new Book();
                    AtomicReference<String> message = new AtomicReference<>();
                    librarianView.addUserToBlackListView( user, book, message );
                    if(
                        user.getId() != null ||
                        book.getQuantity() != null ||
                        book.getId() == null
                    ){
                        System.out.print("Are you sure? press y(es or c)ancel : ");
                        Character op = scanner.nextLine().charAt(0);
                        switch ( op.toString().toLowerCase() ){
                            case "y"->{
                                //Yes
                                if( librarianService.addUserToBlacklist(user, book, message.get()) ){
                                    HelperView.message("Added Successfully!");
                                }
                            }
                            default -> {
                                HelperView.error("Cancel");
                            }
                        }
                    }
                }
                case 5 ->{
                    //Remove User from Blacklist
                    User user = new User();
                    Book book = new Book();
                    librarianView.removeUserToBlackListView( user, book );
                    if(
                            user.getId() != null ||
                                    book.getQuantity() != null ||
                                    book.getId() == null
                    ){
                        System.out.print("Are you sure? press y(es or c)ancel : ");
                        Character op = scanner.nextLine().charAt(0);
                        switch ( op.toString().toLowerCase() ){
                            case "y"->{
                                //Yes
                                if( librarianService.removeUserFromBlacklist(user, book) ){
                                    HelperView.message("Remove Successfully!");
                                }
                            }
                            default -> {
                                HelperView.error("Cancel");
                            }
                        }
                    }
                }
                case 6 ->{
                    //Exit
                    return;
                }
            }
        }
    }

    private void userSearchPage(){
        LibrarianUtil librarianUtil = new LibrarianUtil();
        while (true){
            librarianView.userSearchMenu( librarianUtil );
            switch ( librarianUtil.getOption() ){
                case 1 ->{
                    //User Search By Id
                    User user = new User();
                    librarianView.userSearchByIdView( user );
                    if( user.getId() != null ){
                        Optional<User> us = librarianService.searchUserById( user.getId() );
                        if( us.isPresent() ){
                            List<User> users = new ArrayList<>();
                            users.add( us.get() );
                            librarianView.userView( users,1,1,1,false );
                        }else{
                            HelperView.error( String.format("User id(%s) not fount", user.getId()) );
                        }
                    }
                }
                case 2 ->{
                    //User Search By Username
                    User user = new User();
                    librarianView.userSearchByUsernameView( user );
                    if( user.getUsername() != null ){
                        List<User> uss = librarianService.searchUsersByUsername( user.getUsername() );
                        if( !uss.isEmpty() ){
                            librarianView.userView( uss,1,1,1,false );
                        }else{
                            HelperView.error( String.format("Username(%s) not fount", user.getUsername()) );
                        }
                    }
                }
                case 3 -> {
                    //Exit
                    return;
                }
                default -> HelperView.error("Please enter above number of list");
            }
        }
    }

    private void categoryPage() {
        LibrarianUtil librarianUtil = new LibrarianUtil();
        while (true) {
            librarianView.categoryMenu(librarianUtil);
            switch (librarianUtil.getOption()) {
                case 1 -> {
                    //List Category

                }
                case 2 -> {
                    //Added
                }
                case 3 -> {
                    //Search Category
                }
                case 4 -> {
                    //Update
                }
                case 5 -> {
                    //delete
                }
                case 6 -> {
                    //Exit
                    return;
                }
                default -> HelperView.error("Please enter above number of list");
            }
        }
    }

}
