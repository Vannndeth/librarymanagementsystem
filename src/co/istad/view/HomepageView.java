package co.istad.view;

import co.istad.model.User;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.Scanner;

public class HomepageView {
    public static void logo(){
        System.out.println("""
                                
                 ██████╗███████╗████████╗ █████╗ ██████╗     ██╗     ██╗██████╗ ██████╗  █████╗ ██████╗ ██╗   ██╗
                ██╔════╝██╔════╝╚══██╔══╝██╔══██╗██╔══██╗    ██║     ██║██╔══██╗██╔══██╗██╔══██╗██╔══██╗╚██╗ ██╔╝
                ██║     ███████╗   ██║   ███████║██║  ██║    ██║     ██║██████╔╝██████╔╝███████║██████╔╝ ╚████╔╝\s
                ██║     ╚════██║   ██║   ██╔══██║██║  ██║    ██║     ██║██╔══██╗██╔══██╗██╔══██║██╔══██╗  ╚██╔╝ \s
                ╚██████╗███████║   ██║   ██║  ██║██████╔╝    ███████╗██║██████╔╝██║  ██║██║  ██║██║  ██║   ██║  \s
                 ╚═════╝╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═════╝     ╚══════╝╚═╝╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝  \s
                """);
        System.out.println("WELCOME TO CSTAD LIBRARY\n");
    }
    public int registerOption(Scanner scanner){
        int option = 0;
        try {
            Table table = new Table(15, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
            table.addCell(" 1) Sign in", 5);
            table.addCell(" 2) Sign up", 5);
            table.addCell(" 3) Exit", 5);
            System.out.println(table.render());
            System.out.print("Please choose option: ");
            option = Integer.parseInt(scanner.nextLine());
        }catch (NumberFormatException e){
            HelperView.message("Please choose option above...!");
        }
        return option;
    }
    public void login(User user, Scanner scanner){
        System.out.print("Please enter username: ");
        user.setUsername(scanner.nextLine());
        System.out.print("Please enter password: ");
        user.setPassword(scanner.nextLine());
    }
    public static void signup(User user, Scanner scanner){
        System.out.print("Enter username: ");
        user.setUsername(scanner.nextLine());
        System.out.print("Enter email: ");
        user.setEmail(scanner.nextLine());
        System.out.print("Enter password: ");
        user.setPassword(scanner.nextLine());
        System.out.print("Enter confirm password: ");
        user.setConfirmPassword(scanner.nextLine());
    }
}
