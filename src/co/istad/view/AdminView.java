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
    public int adminDashboardView(Scanner scanner){
        int option = 0;
        try {
            Table table = new Table(7, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
            table.addCell("1) View total admin");
            table.addCell("2) View total Librarian");
            table.addCell("3) View total user");
            table.addCell("4) View book detail");
            table.addCell("5) View admin detail");
            table.addCell("6) View user detail");
            table.addCell("7) View librarian detail");
            table.addCell("8) View report");
            table.addCell("9) Reset password");
            table.addCell("10) Disable account");
            table.addCell("11) Remove account");
            table.addCell("12) Save report as excel");
            table.addCell("13) Backup");
            table.addCell("14) Restore");

            System.out.println(table.render());
            System.out.print("Please choose option: ");
            option = Integer.parseInt(scanner.nextLine());
        }catch (NumberFormatException e){
            System.err.println("Only number required!");
        }
        return option;
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

    public void countUserView(Long count, String msg){
        Table table = new Table(1, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell(msg);
        table.addCell(count.toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
        System.out.println(table.render());
    }

    public void bookView(List<Book> books){
        Table table = new Table(3, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell(" ".repeat(10) + "Id" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "Title" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "Quantity" + " ".repeat(10));
        books.forEach(book -> {
            table.addCell(book.getId().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(book.getTitle().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
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
    public void disableAccountView(User user, Scanner scanner){
        System.out.print("Enter account id: ");
        user.setId(Long.parseLong(scanner.nextLine()));
        System.out.print("Set account disable: ");
        user.setDisable(Boolean.parseBoolean(scanner.nextLine()));
    }
    public void resetPasswordView(User user, Scanner scanner){
        System.out.print("Enter account id: ");
        user.setId(Long.parseLong(scanner.nextLine()));
        System.out.print("Set new password: ");
        user.setPassword(scanner.nextLine());
    }

}
