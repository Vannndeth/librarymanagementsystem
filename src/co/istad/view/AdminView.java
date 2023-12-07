package co.istad.view;

import co.istad.model.Book;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.List;
import java.util.Scanner;

public class AdminView {
    public int adminDashboardView(Scanner scanner){
        int option = 0;
        try {
            Table table = new Table(7, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
            table.addCell("1) View total admin");
            table.addCell("2) View total user");
            table.addCell("3) View total librarian");
            table.addCell("4) View total book");
            table.addCell("5) View all admin");
            table.addCell("6) View all user");
            table.addCell("7) View all librarian");
            table.addCell("8) View total users");
            table.addCell("9) View total users");
            table.addCell("10) View report");
            table.addCell("11) Sign-out");
            System.out.println(table.render());
            System.out.print("Please choose option: ");
            option = Integer.parseInt(scanner.nextLine());
        }catch (NumberFormatException e){
            System.err.println("Only number required!");
        }
        return option;
    }

    public void allUserView(Long adminCount, String msg){
        Table table = new Table(1, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell(msg);
        table.addCell(adminCount.toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
        System.out.println(table.render());
    }

    public void dashboardOverview(){
        Table table = new Table(4, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell("ADMIN");
        table.addCell("LIBRARIAN");
        table.addCell("USER");
        table.addCell("BOOK");
        System.out.println(table.render());
    }

    public void bookView(List<Book> books){
        Table table = new Table(3, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell("Id");
        table.addCell("Title");
        table.addCell("Quantity");
        books.forEach(book -> {
            table.addCell(book.getId().toString());
            table.addCell(book.getTitle().toString());
            table.addCell(book.getQuantity().toString());
        });
        System.out.println(table.render());
    }
}
