package co.istad.view;

import co.istad.model.Author;
import co.istad.model.Book;
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
        table.addCell(" ".repeat(10) + "4. LOGOUT" + " ".repeat(10));
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
        table.addCell(" ".repeat(10) + "4. All Author" + " ".repeat(10));
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

    public void authorView(List<Author> authors){
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
    }

}
