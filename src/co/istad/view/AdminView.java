package co.istad.view;

import co.istad.model.Book;
import co.istad.model.User;
import co.istad.service.AdminService;
import co.istad.util.Singleton;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.List;
import java.util.Scanner;

public class AdminView {

    private final AdminService adminService;
    public AdminView(){
        adminService = Singleton.getAdminServiceImpl();
    }
    public int option(Scanner scanner){
        int option = 0;
        try {
            System.out.print("Please choose option: ");
            option = Integer.parseInt(scanner.nextLine());
        }catch (NumberFormatException e){
            HelperView.message("Required only number...!");
        }
        return option;
    }
    public void adminDashboardView(Scanner scanner){
        Table table = new Table(2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.SURROUND);
        table.addCell("     1) View Admin Detail       ");
        table.addCell("     2) View Librarian Detail     ");
        table.addCell("     3) View User Detail     ");
        table.addCell("     4) View Book Detail     ");
        table.addCell("     5) Reset Password       ");
        table.addCell("     6) Disable Account      ");
        table.addCell("     7) Remove Account       ");
        table.addCell("     8) Save Report as Excel     ");
        table.addCell("     9) Backup      ");
        table.addCell("     10) Restore      ");
        table.addCell("     11) View Report    ");
        table.addCell("     12) Sign Out        ");
        System.out.println(table.render());
    }
    public void dashboardOverview(){
        System.out.println(" ".repeat(50) + "Admin Dashboard Overview");
        Table table = new Table(4, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell(" ".repeat(10) + "ADMIN" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "LIBRARIAN" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "USER" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "BOOK" + " ".repeat(10));
        table.addCell(adminService.getAdminCount().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell(adminService.getLibrarianCount().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell(adminService.getUserCount().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell(adminService.getBooksCount().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
        System.out.println(table.render());
    }
    public void bookView(List<Book> books){
        Table table = new Table(6, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell(" ".repeat(10) + "Id" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "Title" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "Category" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "Description" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "Author" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "Quantity" + " ".repeat(10));
        books.forEach(book -> {
            table.addCell(book.getId().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(book.getTitle().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(book.getCategory().getName(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(book.getDescription().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(book.getAuthor().getFirstName() + book.getAuthor().getLastName(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(book.getQuantity().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
        });
        System.out.println(table.render());
    }
    public void usersView(List<User> users){
        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell(" ".repeat(10) + "Id" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "Username" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "Email" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "Role" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "Disable" + " ".repeat(10));
        users.forEach(user -> {
            table.addCell(user.getId().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(user.getUsername(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(user.getEmail(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(user.getRole().getRole().name(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(user.getDisable().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
        });
        System.out.println(table.render());
    }

    public void searchById(User user, Scanner scanner){
        System.out.print("Enter account id: ");
        user.setId(Long.parseLong(scanner.nextLine()));
    }

}
