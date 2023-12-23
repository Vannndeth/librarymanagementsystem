package co.istad.view;

import co.istad.model.*;
import co.istad.service.LibrarianService;
import co.istad.util.Helper;
import co.istad.util.LibrarianUtil;
import co.istad.util.Singleton;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class LibrarianView {
    private final Scanner scanner;
    private final LibrarianService librarianService;

    public LibrarianView(){
        scanner = Singleton.scanner();
        librarianService = Singleton.getLibrarianService();
    }
    public void welcome(){
        System.out.println("\n" +
                "  _      _ _                    _             \n" +
                " | |    (_) |                  (_)            \n" +
                " | |     _| |__  _ __ __ _ _ __ _  __ _ _ __  \n" +
                " | |    | | '_ \\| '__/ _` | '__| |/ _` | '_ \\ \n" +
                " | |____| | |_) | | | (_| | |  | | (_| | | | |\n" +
                " |______|_|_.__/|_|  \\__,_|_|  |_|\\__,_|_| |_|\n" +
                "                                              \n" +
                "                                              \n");
    }
    public void mainMenu(LibrarianUtil librarianUtil){
        Table table = new Table(2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.SURROUND);
        table.addCell(" ".repeat(5) + "1. Book" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "2. Author" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "3. USER" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "4. Backup and Recovery" + " ".repeat(5));
        table.addCell( " ".repeat(5) + "5. Generate Report" + " ".repeat(5) );
        table.addCell(" ".repeat(5) + "6. Category" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "7. Logout" + " ".repeat(5));
        System.out.println();
        System.out.println(table.render());
        System.out.println();
        System.out.print("\t--> Enter your option : ");
        try{
            librarianUtil.setOption( Integer.parseInt(scanner.nextLine()) );
        }
        catch (NumberFormatException ex){
            librarianUtil.setOption(0);
            HelperView.error("Only number allowed!\n");
        }
    }

    public void authorMenu( LibrarianUtil librarianUtil ){
        Table table = new Table(2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.SURROUND);
        table.addCell(" ".repeat(5) + "1. Add Author" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "2. Update Author" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "3. Search Author" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "4. Show Author" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "5. Exit" + " ".repeat(5));
        System.out.println();
        System.out.println(table.render());
        System.out.println();
        System.out.print("\t-->Enter your option : ");
        try{
            librarianUtil.setOption( Integer.parseInt(scanner.nextLine()) );
        }
        catch (NumberFormatException ex){
            System.err.println(ex.getMessage());
            librarianUtil.setOption(0);
        }
    }

    public void updateAuthorByIdView( Author author ){
        Table table = new Table(1, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell(" ".repeat(5) + "Update Author By Id" + " ".repeat(5));
        System.out.println();
        System.out.println(table.render());
        System.out.println();
        try{
            System.out.print("\t-->Enter Author Id You Want To Update : ");
            author.setId( Long.parseLong( scanner.nextLine() ) );
            Author a = librarianService.searchAuthorById(author.getId());
            if( a == null ){
                HelperView.error(String.format("Author Id : %s Not Found!\n", author.getId()));
                author.setId(null);
            }else{
                System.out.print( String.format("\t-->Enter New Author FirstName(%s) : ", a.getFirstName() ) );
                author.setFirstName( scanner.nextLine() );
                System.out.print( String.format("\t-->Enter New Author LastName(%s) : ", a.getLastName() ) );
                author.setLastName( scanner.nextLine() );
                System.out.print( String.format("\t-->Enter New Author Email(%s) : ", a.getEmail() ) );
                author.setEmail( scanner.nextLine() );
            }


        }
        catch (Exception ex){
            System.err.println(ex.getMessage());
            author.setId(null);
            author.setEmail(null);
            author.setLastName(null);
            author.setFirstName(null);
        }
    }

    public void addAuthorView(Author author){
        try{
            System.out.print("\t-->Enter author firstname : ");
            author.setFirstName( scanner.nextLine() );
            System.out.print("\t-->Enter author lastname : ");
            author.setLastName( scanner.nextLine() );
            System.out.print("\t-->Enter author email : ");
            author.setEmail( scanner.nextLine() );
        }catch ( Exception ex ){
            System.err.println(ex.getMessage());
        }
    }

    public void authorView(List<Author> authors, int currentPage, int totalPage, int limit, boolean display ){
        if (authors == null || authors.isEmpty()){
            Table table = new Table(1, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
            table.addCell("No Author Yet!", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        }else{
            Table table = new Table(3, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
            table.addCell(" ".repeat(5) + "Id" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Firstname" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Lastname" + " ".repeat(5));
            authors.forEach(author -> {
                table.addCell(author.getId().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(author.getFirstName(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(author.getLastName(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            });
            System.out.println(table.render());
        }
        if( display ){
            Table table = new Table( 2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL );
            table.addCell( " ".repeat(5) + "Current Page" + " ".repeat(5) );
            table.addCell( " ".repeat(5) + currentPage + " ".repeat(5) );
            table.addCell( " ".repeat(5) + "Total Of Page" + " ".repeat(5) );
            table.addCell( " ".repeat(5) + totalPage + " ".repeat(5) );
            table.addCell( " ".repeat(5) + "Limit Display" + " ".repeat(5) );
            table.addCell( " ".repeat(5) + limit + " ".repeat(5) );
            System.out.println();
            System.out.println(table.render());
            System.out.println();
        }
    }

    public void searchAuthView(LibrarianUtil librarianUtil){
        Table table = new Table(4, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell(" ".repeat(5) + "1. Search Author By Id" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "2. Search Author By Name" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "3. Exit" + " ".repeat(5));
        System.out.println();
        System.out.println(table.render());
        System.out.println();
        System.out.print("\t-->Enter your option : ");
        try{
            librarianUtil.setOption( Integer.parseInt(scanner.nextLine()) );
        }
        catch (NumberFormatException ex){
            System.err.println(ex.getMessage());
            librarianUtil.setOption(0);
        }
    }

    public void searchAuthorByIdView( Author author ){
        try{
            System.out.print("\t-->Enter Author Id For Search : ");
            author.setId( Long.parseLong( scanner.nextLine() ) );
        }catch (Exception ex){
            HelperView.error("Enter only number!");
        }
    }

    public void searchAuthorByNameView( Author author ){
        try{
            System.out.print("\t-->Enter Author Name For Search : ");
            author.setFirstName( scanner.nextLine() );
        }catch (Exception ex){
            HelperView.error(ex.getMessage());
        }
    }
    public void showAuthorMenu( LibrarianUtil librarianUtil ){
        Table table = new Table(2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.SURROUND);
        table.addCell(" ".repeat(5) + "1. Next" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "2. Previous" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "3. Goto" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "4. First" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "5. Last" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "6. Set Limit" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "7. Exit" + " ".repeat(5));
        System.out.println();
        System.out.println(table.render());
        System.out.println();
        System.out.print("\t-->Enter your option : ");
        try{
            librarianUtil.setOption( Integer.parseInt(scanner.nextLine()) );
        }
        catch (NumberFormatException ex){
            System.err.println(ex.getMessage());
            librarianUtil.setOption(0);
        }
    }

    public void setLimitView( AtomicInteger limit ){
        try{
            System.out.print("Set display limit : ");
            limit.set( Integer.parseInt(scanner.nextLine()) );
            System.out.println(String.format("Set limit to %s successfully", limit.get()));
        }catch (Exception ex){
            HelperView.error("Only number can enter!");
            limit.set( 10 );
        }
    }

    public void createBookView(Book book){
        try{
            Author author = new Author();
            Category category = new Category();
            System.out.print("\t-->Enter book title : ");
            book.setTitle( scanner.nextLine() );
            System.out.print("\t-->Enter book quantity : ");
            book.setQuantity( Integer.parseInt( scanner.nextLine() ) );
            System.out.print("\t-->Enter book description : ");
            book.setDescription( scanner.nextLine() );
            System.out.print("\t-->Enter author id : ");
            author.setId( Long.parseLong(scanner.nextLine()) );
            Author auth = librarianService.searchAuthorById( author.getId() );
            if( auth == null ){
                HelperView.error("Author id not exist!");
                return;
            }else{
                List<Author> auths = new ArrayList<>();
                auths.add( auth );
                authorView( auths, 1,1,1,false );
            }
            System.out.print("\t-->Enter category id : ");
            category.setId( Long.parseLong( scanner.nextLine() ) );
            Category cate = librarianService.searchCategoryById( category.getId() );
            if( cate == null ){
                HelperView.error("Category id not exist!");
                return;
            }else{
                List<Category> categories = new ArrayList<>();
                categories.add( cate );
                categoryView( categories, 1,1,1,false );
            }
            book.setAuthor( author );
            book.setCategory( category );
        }catch ( Exception ex ){
            HelperView.error( "Something wrong please check again before submit! Thanks you" );
        }
    }

    public void bookMenu( LibrarianUtil librarianUtil ){
        Table table = new Table(2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.SURROUND);
        table.addCell(" ".repeat(5) + "1. Add Book" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "2. Update Book" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "3. Search Book" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "4. Show Book" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "5. Borrow And Return Book" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "6. Exit" + " ".repeat(5));
        System.out.println();
        System.out.println(table.render());
        System.out.println();
        System.out.print("\t-->Enter your option : ");
        try{
            librarianUtil.setOption( Integer.parseInt(scanner.nextLine()) );
        }
        catch (NumberFormatException ex){
            System.err.println(ex.getMessage());
            librarianUtil.setOption(0);
        }
    }

    public void bookView(List<Book> books, int currentPage, int totalPage, int limit, boolean display ){
        if (books == null || books.isEmpty()){
            Table table = new Table(1, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
            table.addCell("No Book Yet!", new CellStyle(CellStyle.HorizontalAlign.CENTER));
            System.out.println();
            System.out.println(table.render());
            System.out.println();
        }else{
            Table table = new Table(7, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
            table.addCell(" ".repeat(5) + "Id" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Title" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Quantity" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Author Name" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Added By" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Category" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Description" + " ".repeat(5));
            books.forEach(book -> {
                table.addCell(book.getId().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(book.getTitle(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(book.getQuantity().toString() , new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName() , new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(book.getUser().getUsername().toUpperCase() , new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(book.getCategory().getName() , new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(book.getDescription() , new CellStyle(CellStyle.HorizontalAlign.CENTER));
            });
            System.out.println(table.render());
        }
        if( display ){
            Table table = new Table( 2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL );
            table.addCell( " ".repeat(5) + "Current Page" + " ".repeat(5) );
            table.addCell( " ".repeat(5) + currentPage + " ".repeat(5) );
            table.addCell( " ".repeat(5) + "Total Of Page" + " ".repeat(5) );
            table.addCell( " ".repeat(5) + totalPage + " ".repeat(5) );
            table.addCell( " ".repeat(5) + "Limit Display" + " ".repeat(5) );
            table.addCell( " ".repeat(5) + limit + " ".repeat(5) );
            System.out.println();
            System.out.println(table.render());
            System.out.println();
        }
    }

    public void searchBookView(LibrarianUtil librarianUtil){
        Table table = new Table(4, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell(" ".repeat(5) + "1. Search Book By Id" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "2. Search Book By Title" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "3. Search Book By Author" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "4. Search Book By Category" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "5. Exit" + " ".repeat(5));
        System.out.println();
        System.out.println(table.render());
        System.out.println();
        System.out.print("\t-->Enter your option : ");
        try{
            librarianUtil.setOption( Integer.parseInt(scanner.nextLine()) );
        }
        catch (NumberFormatException ex){
            System.err.println(ex.getMessage());
            librarianUtil.setOption(0);
        }
    }

    public void searchBookByIdView( AtomicLong id ){
        try{
            System.out.print("\t-->Enter Author Id For Search : ");
            id.set( Long.parseLong( scanner.nextLine() ) );
        }catch (Exception ex){
            HelperView.error("Enter only number!");
        }
    }

    public void searchBookByTitle( AtomicReference<String> title ){
        System.out.print("\t-->Enter book title : ");
        title.set( scanner.nextLine() );
    }

    public void backupAndRecoveryView(LibrarianUtil librarianUtil){
        Table table = new Table(4, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell(" ".repeat(5) + "1. Backup" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "2. Recovery" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "3. Exit" + " ".repeat(5));
        System.out.println();
        System.out.println(table.render());
        System.out.println();
        System.out.print("\t-->Enter your option : ");
        try{
            librarianUtil.setOption( Integer.parseInt(scanner.nextLine()) );
        }
        catch (NumberFormatException ex){
            System.err.println(ex.getMessage());
            librarianUtil.setOption(0);
        }
    }

    public void showBookMenu( LibrarianUtil librarianUtil ){
        Table table = new Table(2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.SURROUND);
        table.addCell(" ".repeat(5) + "1. Next" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "2. Previous" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "3. Goto" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "4. First" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "5. Last" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "6. Set Limit" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "7. Report Book" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "8. Exit" + " ".repeat(5));
        System.out.println();
        System.out.println(table.render());
        System.out.println();
        System.out.print("\t-->Enter your option : ");
        try{
            librarianUtil.setOption( Integer.parseInt(scanner.nextLine()) );
        }
        catch (NumberFormatException ex){
            System.err.println(ex.getMessage());
            librarianUtil.setOption(0);
        }
    }

    public void confirmBookView(Borrow borrow){

        try{
            System.out.print("\t-->Enter borrow id : ");
            borrow.setId( Long.parseLong(scanner.nextLine()) );
        }catch ( Exception ex ){
            HelperView.error( "Enter only number!" );
        }

    }

    public void borrowAndReturnMenu( LibrarianUtil librarianUtil ){
        Table table = new Table(2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.SURROUND);
        table.addCell(" ".repeat(5) + "1. List Borrow" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "2. List Return" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "3. Confirm Borrow" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "4. Confirm Return" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "5. Exit" + " ".repeat(5));
        System.out.println();
        System.out.println(table.render());
        System.out.println();
        System.out.print("\t-->Enter your option : ");
        try{
            librarianUtil.setOption( Integer.parseInt(scanner.nextLine()) );
        }
        catch (NumberFormatException ex){
            System.err.println(ex.getMessage());
            librarianUtil.setOption(0);
        }
    }

    public void bookUpdateMenu(LibrarianUtil librarianUtil){
        Table table = new Table(2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.SURROUND);
        table.addCell(" ".repeat(5) + "1. Update Book Title" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "2. Update Book Quantity" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "3. Update Book Author" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "4. Exit" + " ".repeat(5));
        System.out.println();
        System.out.println(table.render());
        System.out.println();
        System.out.print("\t-->Enter your option : ");
        try{
            librarianUtil.setOption( Integer.parseInt(scanner.nextLine()) );
        }
        catch (NumberFormatException ex){
            System.err.println(ex.getMessage());
            librarianUtil.setOption(0);
        }
    }

    public void bookConfirmBookId(Book book){
        try{
            System.out.print("\t-->Enter book id you're want to update : ");
            book.setId(Long.parseLong( scanner.nextLine() ));
        }catch (Exception ex){
            HelperView.error("Book Id only number!");
            return;
        }

        Optional<Book> bk = librarianService.searchBookById(book.getId());
        List<Book> books = new ArrayList<>();
        if( bk.isPresent() ){
            book.setId( bk.get().getId() );
            book.setUser(bk.get().getUser());
            book.setTitle(bk.get().getTitle());
            book.setQuantity( bk.get().getQuantity() );
            book.setAuthor(bk.get().getAuthor());
            book.setDescription(bk.get().getDescription());
            book.setCategory(bk.get().getCategory());
            books.add(bk.get());
        }else{
            book.setId(null);
        }
        bookView( books, 1,1,1,false );

    }
    public void bookUpdateTitleView( Book book ){
        try{
            System.out.print("\t-->Enter new book title : ");
            book.setTitle( scanner.nextLine() );
            if( book.getTitle().isEmpty() ){
                HelperView.error("Book new title is required");
                book.setTitle("");
                return;
            }
        }catch (Exception ex){
            HelperView.error(ex.getMessage());
        }
    }

    public void bookUpdateQuantityView( Book book ){
        try{
            System.out.print("\t-->Enter new book quantity : ");
            book.setQuantity( Integer.parseInt(scanner.nextLine()) );
            if( book.getQuantity() < 1 ){
                HelperView.error("Book quantity must be more than 0");
                book.setQuantity(0);
                return;
            }
        }catch (Exception ex){
            HelperView.error("Quantity only number!");
        }
    }

    public void authConfirmId(Author author){
        try{
            System.out.print("\t-->Enter author id you're want to update : ");
            author.setId(Long.parseLong( scanner.nextLine() ));
            Author auth = librarianService.searchAuthorById(author.getId() );
            List<Author> authors = new ArrayList<>();
            if( auth != null ){
                authors.add( auth );
                author.setId(auth.getId() );
                author.setEmail( auth.getEmail() );
                author.setLastName(auth.getLastName());
                author.setFirstName(auth.getFirstName());
            }else{
                HelperView.error("Author not exist,Please select another author id!");
                author.setId(null);
            }
            authorView(authors, 1,1,1, false);
        }catch (Exception ex){
            HelperView.error("Author Id only number!");
            return;
        }
    }

    public void reportView( List<User> users, int currentPage, int totalPage, int limit, boolean display  ){
        if (users == null || users.isEmpty()){
            Table table = new Table(1, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
            table.addCell("No Book Yet!", new CellStyle(CellStyle.HorizontalAlign.CENTER));
            System.out.println();
            System.out.println(table.render());
            System.out.println();
        }else{
            Table table = new Table(7, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
            table.addCell(" ".repeat(5) + "User Id" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Username" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Email" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Book Title" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Borrow" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Return" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Borrow At" + " ".repeat(5));
            users.forEach(user -> {
                table.addCell(user.getId().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(user.getUsername() , new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(user.getEmail() , new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(user.getBorrow().getBook().getTitle() , new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell( user.getBorrow().isBorrow() ? "Borrow" : "Not Confirm Yet" , new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(user.getBorrow().isReturn() ? "Return" : "Not Return Yet" , new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(user.getBorrow().getBorrowDate().toString() , new CellStyle(CellStyle.HorizontalAlign.CENTER));
            });
            System.out.println(table.render());
        }
        if( display ){
            Table table = new Table( 2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL );
            table.addCell( " ".repeat(5) + "Current Page" + " ".repeat(5) );
            table.addCell( " ".repeat(5) + currentPage + " ".repeat(5) );
            table.addCell( " ".repeat(5) + "Total Of Page" + " ".repeat(5) );
            table.addCell( " ".repeat(5) + totalPage + " ".repeat(5) );
            table.addCell( " ".repeat(5) + "Limit Display" + " ".repeat(5) );
            table.addCell( " ".repeat(5) + limit + " ".repeat(5) );
            System.out.println();
            System.out.println(table.render());
            System.out.println();
        }
    }
    public void borrowView( List<Borrow> borrows, int currentPage, int totalPage, int limit, boolean display ){
        if (borrows == null || borrows.isEmpty()){
            Table table = new Table(1, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
            table.addCell("No Brrow Book Yet!", new CellStyle(CellStyle.HorizontalAlign.CENTER));
            System.out.println();
            System.out.println(table.render());
            System.out.println();
        }else{
            Table table = new Table(9, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
            table.addCell(" ".repeat(5) + "Borrow Id" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "User Id" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Username" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Book Title" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Quantity" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Borrow Status" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Return Status" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Borrow at" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Deadline at" + " ".repeat(5));
            borrows.forEach(borrow -> {
                table.addCell(borrow.getId().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(borrow.getUser().getId().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(borrow.getUser().getUsername(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(borrow.getBook().getTitle(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(borrow.getQuantity().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                //Borrow Status
                table.addCell( borrow.isBorrow() ? "Is Borrow" : "Not Confirm Yet" , new CellStyle(CellStyle.HorizontalAlign.CENTER));
                //Return Status
                table.addCell(borrow.isReturn() ? "Is Return" : "Not Return Yet", new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(borrow.getStartBorrowDate().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(borrow.getDeadlineBorrowDate().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            });
            System.out.println(table.render());
        }
        if( display ){
            Table table = new Table( 2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL );
            table.addCell( " ".repeat(5) + "Current Page" + " ".repeat(5) );
            table.addCell( " ".repeat(5) + currentPage + " ".repeat(5) );
            table.addCell( " ".repeat(5) + "Total Of Page" + " ".repeat(5) );
            table.addCell( " ".repeat(5) + totalPage + " ".repeat(5) );
            table.addCell( " ".repeat(5) + "Limit Display" + " ".repeat(5) );
            table.addCell( " ".repeat(5) + limit + " ".repeat(5) );
            System.out.println();
            System.out.println(table.render());
            System.out.println();
        }
    }
    public void returnView( List<Return> returns, int currentPage, int totalPage, int limit, boolean display ){
        if (returns == null || returns.isEmpty()){
            Table table = new Table(1, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
            table.addCell("No Return Book Yet!", new CellStyle(CellStyle.HorizontalAlign.CENTER));
            System.out.println();
            System.out.println(table.render());
            System.out.println();
        }else{
            Table table = new Table(9, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
            table.addCell(" ".repeat(5) + "Return Id" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Book Id" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "User Id" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Username" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Book Title" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Quantity" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Return Status" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Return at" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Deadline at" + " ".repeat(5));
            returns.forEach(res -> {
                table.addCell(res.getId().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));//Return Id
                table.addCell(res.getBorrow().getBook().getId().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));//Book Id
                table.addCell(res.getBorrow().getUser().getId().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));//User Id
                table.addCell(res.getBorrow().getUser().getUsername(), new CellStyle(CellStyle.HorizontalAlign.CENTER));//Username
                table.addCell(res.getBorrow().getBook().getTitle(), new CellStyle(CellStyle.HorizontalAlign.CENTER));//Book Title
                table.addCell(res.getBorrow().getQuantity().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));//Borrow Quantity
                table.addCell(res.getBorrow().isReturn() ? "Returned" : "Not Return yet" , new CellStyle(CellStyle.HorizontalAlign.CENTER));//Borrow Status
                table.addCell(res.getReturnDate().toString() , new CellStyle(CellStyle.HorizontalAlign.CENTER));//Return Date
                table.addCell(res.getBorrow().getDeadlineBorrowDate().toString() , new CellStyle(CellStyle.HorizontalAlign.CENTER));//Deadline
            });
            System.out.println(table.render());
        }
        if( display ){
            Table table = new Table( 2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL );
            table.addCell( " ".repeat(5) + "Current Page" + " ".repeat(5) );
            table.addCell( " ".repeat(5) + currentPage + " ".repeat(5) );
            table.addCell( " ".repeat(5) + "Total Of Page" + " ".repeat(5) );
            table.addCell( " ".repeat(5) + totalPage + " ".repeat(5) );
            table.addCell( " ".repeat(5) + "Limit Display" + " ".repeat(5) );
            table.addCell( " ".repeat(5) + limit + " ".repeat(5) );
            System.out.println();
            System.out.println(table.render());
            System.out.println();
        }
    }
    public void categoryView( List<Category> categories, int currentPage, int totalPage, int limit, boolean display ){
        if (categories == null || categories.isEmpty()){
            Table table = new Table(1, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
            table.addCell("No Book Yet!", new CellStyle(CellStyle.HorizontalAlign.CENTER));
            System.out.println();
            System.out.println(table.render());
            System.out.println();
        }else{
            Table table = new Table(3, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
            table.addCell(" ".repeat(5) + "Id" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Name" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Created at" + " ".repeat(5));
            categories.forEach(category -> {
                table.addCell(category.getId().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(category.getName() , new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(category.getCreatedDate().toString() , new CellStyle(CellStyle.HorizontalAlign.CENTER));
            });
            System.out.println(table.render());
        }
        if( display ){
            Table table = new Table( 2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL );
            table.addCell( " ".repeat(5) + "Current Page" + " ".repeat(5) );
            table.addCell( " ".repeat(5) + currentPage + " ".repeat(5) );
            table.addCell( " ".repeat(5) + "Total Of Page" + " ".repeat(5) );
            table.addCell( " ".repeat(5) + totalPage + " ".repeat(5) );
            table.addCell( " ".repeat(5) + "Limit Display" + " ".repeat(5) );
            table.addCell( " ".repeat(5) + limit + " ".repeat(5) );
            System.out.println();
            System.out.println(table.render());
            System.out.println();
        }
    }

    public void userView( List<User> users, int currentPage, int totalPage, int limit, boolean display ){
        if (users == null || users.isEmpty()){
            Table table = new Table(1, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
            table.addCell("No Book Yet!", new CellStyle(CellStyle.HorizontalAlign.CENTER));
            System.out.println();
            System.out.println(table.render());
            System.out.println();
        }else{
            Table table = new Table(4, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
            table.addCell(" ".repeat(5) + "Id" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Name" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Email" + " ".repeat(5));
            table.addCell(" ".repeat(5) + "Disable status" + " ".repeat(5));
            users.forEach(user -> {
                table.addCell(user.getId().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(user.getUsername() , new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(user.getEmail() , new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(user.getDisable() ? "Disabled" : "false" , new CellStyle(CellStyle.HorizontalAlign.CENTER));
            });
            System.out.println(table.render());
        }
        if( display ){
            Table table = new Table( 2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL );
            table.addCell( " ".repeat(5) + "Current Page" + " ".repeat(5) );
            table.addCell( " ".repeat(5) + currentPage + " ".repeat(5) );
            table.addCell( " ".repeat(5) + "Total Of Page" + " ".repeat(5) );
            table.addCell( " ".repeat(5) + totalPage + " ".repeat(5) );
            table.addCell( " ".repeat(5) + "Limit Display" + " ".repeat(5) );
            table.addCell( " ".repeat(5) + limit + " ".repeat(5) );
            System.out.println();
            System.out.println(table.render());
            System.out.println();
        }
    }
    public void confirmReturnView(User user, Book book){
        try{
            System.out.print("\t-->Enter user id : ");
            user.setId( Long.parseLong(scanner.nextLine()) );
            Optional<User> u = librarianService.searchUserById( user.getId() );
            if( u.isEmpty() ){
                HelperView.error(String.format("User id(%s) not exist" , user.getId()));
                return;
            }else{
                List<User> users = new ArrayList<>();
                users.add(u.get());
                userView( users, 1,1,1,false );
            }
            System.out.print("\t-->Enter book id : ");
            book.setId( Long.parseLong(scanner.nextLine()) );
            Optional<Book> b = librarianService.searchBookById( book.getId() );
            if( b.isEmpty() ){
                HelperView.error(String.format("Book id(%s) not exist" , user.getId()));
                return;
            }else{
                List<Book> books = new ArrayList<>();
                books.add(b.get());
                bookView( books, 1,1,1,false );
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            HelperView.error("Something wrong with your input!\n*Note id number only");
        }

    }

    public void userMenu(LibrarianUtil librarianUtil){
        Table table = new Table(2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.SURROUND);
        table.addCell(" ".repeat(5) + "1. List user" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "2. Search User" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "3. Add User to blacklist" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "4. Remove User from blacklist" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "5. Exit" + " ".repeat(5));
        System.out.println();
        System.out.println(table.render());
        System.out.println();
        System.out.print("\t-->Enter your option : ");
        try{
            librarianUtil.setOption( Integer.parseInt(scanner.nextLine()) );
        }
        catch (NumberFormatException ex){
            System.err.println(ex.getMessage());
            librarianUtil.setOption(0);
        }

    }

    public void userSearchMenu(LibrarianUtil librarianUtil){
        Table table = new Table(2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.SURROUND);
        table.addCell(" ".repeat(5) + "1. Search User By Id" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "2. Search User By Username" + " ".repeat(5));
        table.addCell(" ".repeat(5) + "3. Exit" + " ".repeat(5));
        System.out.println();
        System.out.println(table.render());
        System.out.println();
        System.out.print("\t-->Enter your option : ");
        try{
            librarianUtil.setOption( Integer.parseInt(scanner.nextLine()) );
        }
        catch (NumberFormatException ex){
            System.err.println(ex.getMessage());
            librarianUtil.setOption(0);
        }
    }
    public void userSearchByIdView(User user){
        try{
            System.out.print("\t--> Search user id : ");
            user.setId( Long.parseLong( scanner.nextLine() ) );
        }
        catch (Exception ex){
            HelperView.error("User Id enter only number");
        }
    }
    public void userSearchByUsernameView(User user){
        try{
            System.out.print("\t--> Search username : ");
            user.setUsername( scanner.nextLine() );
        }
        catch (Exception ex){
            HelperView.error("Something went wrong");
        }
    }

    public void addUserToBlackListView(User user, Book book, AtomicReference<String> message){
        try{
            //Enter User Id
            System.out.print("\t-->Enter user id : ");
            user.setId( Long.parseLong( scanner.nextLine() ) );
            Optional<User> us = librarianService.searchUserById(user.getId() );
            if( us.isPresent() ){
                List<User> users = new ArrayList<>();
                users.add( us.get() );
                userView(users, 1,1,1, false);
            }else{
                HelperView.error(String.format("User id(%s) not exist", user.getId()));
                return;
            }
            //Enter Book Id
            System.out.print("\t-->Enter book id : ");
            book.setId( Long.parseLong( scanner.nextLine() ) );
            Optional<Book> bk = librarianService.searchBookById(book.getId() );
            if( bk.isPresent() ){
                List<Book> books = new ArrayList<>();
                books.add( bk.get() );
                bookView(books, 1,1,1, false);
            }else{
                HelperView.error(String.format("Book id(%s) not exist", book.getId()));
                return;
            }
            //Book Qty
            System.out.print("\t-->Enter book quantity : ");
            book.setQuantity( Integer.parseInt( scanner.nextLine() ) );
            //Message
            System.out.print("\t-->Enter message : ");
            message.set( scanner.nextLine() );
        }catch (Exception ex){
            HelperView.error("Something went wrong!");
        }
    }

}
