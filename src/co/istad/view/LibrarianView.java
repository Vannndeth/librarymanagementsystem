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

import java.util.List;
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
        Table table = new Table(4, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell(" ".repeat(10) + "1. Book" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "2. Author" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "3. USER" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "4. Backup and Recovery" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "5. Logout" + " ".repeat(10));
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
        Table table = new Table(4, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell(" ".repeat(10) + "1. Add Author" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "2. Update Author" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "3. Search Author" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "4. Show Author" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "5. Exit" + " ".repeat(10));
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
        table.addCell(" ".repeat(10) + "Update Author By Id" + " ".repeat(10));
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
            table.addCell(" ".repeat(10) + "Id" + " ".repeat(10));
            table.addCell(" ".repeat(10) + "Firstname" + " ".repeat(10));
            table.addCell(" ".repeat(10) + "Lastname" + " ".repeat(10));
            authors.forEach(author -> {
                table.addCell(author.getId().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(author.getFirstName(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(author.getLastName(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            });
            System.out.println(table.render());
        }
        if( display ){
            Table table = new Table( 2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL );
            table.addCell( " ".repeat(10) + "Current Page" + " ".repeat(10) );
            table.addCell( " ".repeat(10) + currentPage + " ".repeat(10) );
            table.addCell( " ".repeat(10) + "Total Of Page" + " ".repeat(10) );
            table.addCell( " ".repeat(10) + totalPage + " ".repeat(10) );
            table.addCell( " ".repeat(10) + "Limit Display" + " ".repeat(10) );
            table.addCell( " ".repeat(10) + limit + " ".repeat(10) );
            System.out.println();
            System.out.println(table.render());
            System.out.println();
        }
    }

    public void searchAuthView(LibrarianUtil librarianUtil){
        Table table = new Table(4, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell(" ".repeat(10) + "1. Search Author By Id" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "2. Search Author By Name" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "3. Exit" + " ".repeat(10));
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
        Table table = new Table(4, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell(" ".repeat(10) + "1. Next" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "2. Previous" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "3. Goto" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "4. First" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "5. Last" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "6. Set Limit" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "7. Exit" + " ".repeat(10));
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
            System.out.print("\t-->Enter category id : ");
            category.setId( Long.parseLong( scanner.nextLine() ) );
            book.setAuthor( author );
            book.setCategory( category );
        }catch ( Exception ex ){
            HelperView.error( "Something wrong please check again before submit! Thanks you" );
        }
    }

    public void bookMenu( LibrarianUtil librarianUtil ){
        Table table = new Table(4, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell(" ".repeat(10) + "1. Add Book" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "2. Update Book" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "3. Search Book" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "4. Show Book" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "5. Borrow Book" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "6. Exit" + " ".repeat(10));
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
            table.addCell(" ".repeat(8) + "Id" + " ".repeat(8));
            table.addCell(" ".repeat(8) + "Title" + " ".repeat(8));
            table.addCell(" ".repeat(8) + "Quantity" + " ".repeat(8));
            table.addCell(" ".repeat(8) + "Author Name" + " ".repeat(8));
            table.addCell(" ".repeat(8) + "Added By" + " ".repeat(8));
            table.addCell(" ".repeat(8) + "Category" + " ".repeat(8));
            table.addCell(" ".repeat(8) + "Description" + " ".repeat(8));
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
            table.addCell( " ".repeat(10) + "Current Page" + " ".repeat(10) );
            table.addCell( " ".repeat(10) + currentPage + " ".repeat(10) );
            table.addCell( " ".repeat(10) + "Total Of Page" + " ".repeat(10) );
            table.addCell( " ".repeat(10) + totalPage + " ".repeat(10) );
            table.addCell( " ".repeat(10) + "Limit Display" + " ".repeat(10) );
            table.addCell( " ".repeat(10) + limit + " ".repeat(10) );
            System.out.println();
            System.out.println(table.render());
            System.out.println();
        }
    }

    public void searchBookView(LibrarianUtil librarianUtil){
        Table table = new Table(4, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell(" ".repeat(10) + "1. Search Book By Id" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "2. Search Book By Title" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "3. Search Book By Author" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "4. Search Book By Category" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "5. Exit" + " ".repeat(10));
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
        table.addCell(" ".repeat(10) + "1. Backup" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "2. Recovery" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "3. Exit" + " ".repeat(10));
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
        Table table = new Table(4, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell(" ".repeat(10) + "1. Next" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "2. Previous" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "3. Goto" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "4. First" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "5. Last" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "6. Set Limit" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "7. Exit" + " ".repeat(10));
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

    public void borrowMenu( LibrarianUtil librarianUtil ){
        Table table = new Table(4, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell(" ".repeat(10) + "1. List Borrow" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "2. Confirm Borrow" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "3. Exit" + " ".repeat(10));
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

}
