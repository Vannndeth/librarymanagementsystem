package co.istad.view;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.Scanner;

public class UserView {
//    public static void login(User user, Scanner scanner){
//        System.out.print("Please enter username: ");
//        user.setUsername(scanner.nextLine());
//        System.out.print("Please enter password: ");
//        user.setPassword(scanner.nextLine());
//    }
//    public static void signup(User user, Scanner scanner){
//        System.out.print("Enter username: ");
//        user.setUsername(scanner.nextLine());
//        System.out.print("Enter email: ");
//        user.setEmail(scanner.nextLine());
//        System.out.print("Enter password: ");
//        user.setPassword(scanner.nextLine());
//        System.out.print("Enter confirm password: ");
//        user.setConfirmPassword(scanner.nextLine());
//    }
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
}
