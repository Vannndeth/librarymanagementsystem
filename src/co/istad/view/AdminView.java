package co.istad.view;

import co.istad.model.Book;
import co.istad.model.User;
import co.istad.service.AdminService;
import co.istad.util.Pagination;
import co.istad.util.Singleton;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

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
    public void bookView(List<Book> books, int rowPerPage, int currentPage){
        int size = adminService.getAllBook().size();
        int startIndex = (currentPage - 1) * rowPerPage;
        int endIndex = Math.min(startIndex + rowPerPage, size);
        Table table = new Table(6, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.SURROUND_HEADER_AND_COLUMNS);
        table.addCell(" ".repeat(10) + "Id" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "Title" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "Category" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "Description" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "Author" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "Quantity" + " ".repeat(10));
        for (int i = startIndex; i < endIndex; i++){
            table.addCell(books.get(i).getId().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(books.get(i).getTitle().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(books.get(i).getCategory().getName(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(books.get(i).getDescription().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(books.get(i).getAuthor().getFirstName() + books.get(i).getAuthor().getLastName(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(books.get(i).getQuantity().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
        }
        System.out.println(table.render());
        Table tablePage = new Table(2, BorderStyle.DESIGN_CURTAIN);
        int totalPages = (int) Math.ceil((double) size / rowPerPage);
        tablePage.addCell("Page : %d of %s".formatted(currentPage, totalPages + " ".repeat(145)));
        tablePage.addCell("Total Record : %d".formatted(size));
        System.out.println(tablePage.render());
    }
    public void usersView(List<User> users, int rowPerPage, int currentPage) {
        int size = adminService.getAllUser().size();
        int startIndex = (currentPage - 1) * rowPerPage;
        int endIndex = Math.min(startIndex + rowPerPage, size);
        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.SURROUND_HEADER_AND_COLUMNS);
        userTableHeader(table);
        for (int i = startIndex; i < endIndex; i++) {
            table.addCell(users.get(i).getId().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(users.get(i).getUsername(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(users.get(i).getEmail(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(users.get(i).getRole().getRole().name(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(users.get(i).getDisable().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
        }
        System.out.println(table.render());
        Table tablePage = new Table(2, BorderStyle.DESIGN_CURTAIN);
        int totalPages = (int) Math.ceil((double) size / rowPerPage);
        tablePage.addCell("Page : %d of %s".formatted(currentPage, totalPages + " ".repeat(108)));
        tablePage.addCell("Total Record : %d".formatted(size));
        System.out.println(tablePage.render());
    }
    public void paginationOption(){
        System.out.println();
        Table tableMenu = new Table(7, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.SURROUND);
        tableMenu.addCell("   | D)isplay");
        tableMenu.addCell("   | F)irst");
        tableMenu.addCell("   | P)revious");
        tableMenu.addCell("   | N)ext");
        tableMenu.addCell("   | L)ast   ");
        tableMenu.addCell("   | Se)t row");
        tableMenu.addCell("   | M)ain Menu  ");
        System.out.println(tableMenu.render());
        System.out.println();
    }
    public void searchById(User user, Scanner scanner){
        System.out.print("Enter account id: ");
        user.setId(Long.parseLong(scanner.nextLine()));
    }
    public void usersView(List<User> users) {
        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.SURROUND_HEADER_AND_COLUMNS);
        userTableHeader(table);
        users.forEach(user ->{
            table.addCell(user.getId().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(user.getUsername(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(user.getEmail(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(user.getRole().getRole().name(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(user.getDisable().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
        });
        System.out.println(table.render());
    }
    private void userTableHeader(Table table) {
        table.addCell(" ".repeat(10) + "Id" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "Username" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "Email" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "Role" + " ".repeat(10));
        table.addCell(" ".repeat(10) + "Disable" + " ".repeat(10));
    }
}
