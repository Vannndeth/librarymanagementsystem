package co.istad.view;

import co.istad.model.User;
import co.istad.util.Singleton;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.Scanner;

public class HomepageView {
    private final AdminView adminView;
    public HomepageView(){
        adminView = Singleton.getAdminView();
    }
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
    public void registerOption(Scanner scanner){
        Table table = new Table(3, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.SURROUND);
        table.addCell("    1) Sign in   ");
        table.addCell("    2) Sign up   ");
        table.addCell("    3) Exit   ");
        System.out.println(table.render());
        System.out.println("Don't have an account? Sign up");
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
