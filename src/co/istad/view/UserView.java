package co.istad.view;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.Scanner;

public class UserView {
    public int userDashboardView(Scanner scanner) {
        int option = 0;
        try{
            Table table = new Table(6, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
            table.addCell("     1) Search Book   ");
            table.addCell("     2) Borrow   ");
            table.addCell("     3) View   ");
            table.addCell("     4) Preview   ");
            table.addCell("     5) History   ");
            table.addCell("     6) Logout   ");
            System.out.println(table.render());
            System.out.println(" ".repeat(50));
            System.out.print("Please choose option : ");
            option = Integer.parseInt(scanner.nextLine());
        }catch ( NumberFormatException e ){
            System.err.println("Only number required!");
        }
        return option;
    }

    public int searchOption(Scanner scanner){
        int option = 0;
        try {
            System.out.println("Option Menu");
            System.out.println("1: Search By Id");
            System.out.println("2: Search By Title");
            System.out.println("3: Search By Author");
            System.out.println("4: Search By Category");
            System.out.print("Please choose Option: ");
            option = Integer.parseInt(scanner.nextLine());
        }catch (NumberFormatException e){
            HelperView.message("Please choose option above...!");
        }
        return option;
    }
}
