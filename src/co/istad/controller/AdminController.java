package co.istad.controller;

import co.istad.model.Book;
import co.istad.model.User;
import co.istad.service.AdminService;
import co.istad.service.LoginService;
import co.istad.storage.Storage;
import co.istad.util.Singleton;
import co.istad.view.AdminView;
import co.istad.view.HelperView;
import co.istad.view.HomepageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class AdminController {

    private final Scanner scanner;
    private final Storage storage;
    private final AdminService adminService;
    private final AdminView adminView;
    private final LoginService loginService;
    private final HomepageView homepageView;
    public AdminController(){
        scanner = Singleton.scanner();
        adminService = Singleton.getAdminServiceImpl();
        storage = Singleton.getStorage();
        adminView = Singleton.getAdminView();
        loginService = Singleton.getLoginService();
        homepageView = Singleton.getHomepageView();
    }
    public void adminDashboard(){
        HelperView.welcome("=".repeat(115));
        HelperView.welcome(" ".repeat(57) + "Welcome");
        HelperView.welcome("=".repeat(115));
        adminView.dashboardOverview();
        again:
        do {
            adminView.adminDashboardView(scanner);
            int option = adminView.option(scanner);
            switch (option) {
                case 1 -> {
                    getAllAdmin();
                }
                case 2 -> {
                    getAllLibrarian();
                }
                case 3 -> {
                    getAllUser();
                }
                case 4 -> {
                    getAllBook();
                }
                case 5 -> {
                    resetPassword();
                }
                case 6 -> {
                    disableAccount();
                }
                case 7 -> {
                    removeAccount();
                }
                case 8 -> {
                    saveReportExcel();
                }
                case 9 -> {
                    backup();
                }
                case 10 -> {
                    restore();
                }
                case 11 -> {
                    viewReport();
                }
                case 12 -> {
                    storage.setId(null);
                    return;
                }
                default -> {
                    HelperView.message("Please choose option above...!");
                    continue again;
                }
            }
        }while (true);
    }
    public void getAllAdmin(){
        adminView.usersView(adminService.getAllAdmin());
    }
    public void getAllLibrarian(){
        adminView.usersView(adminService.getAllLibrarian());
    }
    public void getAllUser(){
        int currentPage = 1;
        int rowPerPage = 3;
        adminView.usersView(adminService.getAllUser(), rowPerPage, currentPage);
        again:
        do {
            adminView.paginationOption();
            System.out.print("Command ———> ");
            String option=scanner.nextLine();
            switch (option.toUpperCase()){
                case "D" -> {
                    adminView.usersView(adminService.getAllUser(), rowPerPage, currentPage);
                }
                case "F" -> {
                    currentPage = first(adminService.getAllUser(), rowPerPage, currentPage);
                }
                case "P" -> {
                    currentPage = previous(adminService.getAllUser(), rowPerPage, currentPage);
                }
                case "N" -> {
                    currentPage = next(adminService.getAllUser(), rowPerPage, currentPage);
                }
                case "L" -> {
                    currentPage = last(adminService.getAllUser(), rowPerPage, currentPage);
                }
                case "SE" -> {
                    rowPerPage = setRecord();
                }
                case "M" -> {
                    return;
                }
                default -> {
                    HelperView.message("Please choose option above...!");
                    continue again;
                }
            }
        }while (true);
    }
    public void getAllBook(){
        int currentPage = 1;
        int rowPerPage = 3;
        adminView.bookView(adminService.getAllBook(), rowPerPage, currentPage);
        again:
        do {
            adminView.paginationOption();
            System.out.print("Command ———> ");
            String option=scanner.nextLine();
            switch (option.toUpperCase()){
                case "D" -> {
                    adminView.bookView(adminService.getAllBook(), rowPerPage, currentPage);
                }
                case "F" -> {
                    currentPage = firstBook(adminService.getAllBook(), rowPerPage, currentPage);
                }
                case "P" -> {
                    currentPage = previousBook(adminService.getAllBook(), rowPerPage, currentPage);
                }
                case "N" -> {
                    currentPage = nextBook(adminService.getAllBook(), rowPerPage, currentPage);
                }
                case "L" -> {
                    currentPage = lastBook(adminService.getAllBook(), rowPerPage, currentPage);
                }
                case "SE" -> {
                    rowPerPage = setRecordBook();
                }
                case "M" -> {
                    return;
                }
                default -> {
                    HelperView.message("Please choose option above...!");
                    continue again;
                }
            }
        }while (true);
    }
    public void disableAccount(){
        User user = new User();
        adminView.searchById(user, scanner);
        List<User> users = adminService.getLibrarianAndUser();
        final String[] confirm = {""};
        AtomicInteger found = new AtomicInteger();
        found.set(0);
        users.forEach(u -> {
            if (u.getId().equals(user.getId())) {
                HelperView.message(String.format("Account id %d Information",u.getId()));
                List<User> userViews = new ArrayList<>();
                userViews.add(u);
                adminView.usersView(userViews);
                System.out.print("Set account disable: ");
                user.setDisable(Boolean.parseBoolean(scanner.nextLine()));
                System.out.print("Are you sure you want to disable?(Y/N): ");
                confirm[0] = scanner.nextLine();
                if (confirm[0].equalsIgnoreCase("y")) {
                    adminService.disableAccount(user);
                    HelperView.message(String.format("Account id %d disabled = %b",user.getId(), user.getDisable()));
                    found.set(1);
                } else {
                    HelperView.message("You has been canceled disable account...!");
                    found.set(2);
                }
            }
        });
        if (found.get() == 0){
            HelperView.message(String.format("Account id %d not found...!", user.getId()));
        }
    }
    public void resetPassword(){
        User user = new User();
        adminView.searchById(user, scanner);
        List<User> users = adminService.getLibrarianAndUser();
        final String[] confirm = {""};
        AtomicInteger found = new AtomicInteger();
        found.set(0);
        users.forEach(u -> {
            if (u.getId().equals(user.getId())) {
                HelperView.message(String.format("Account %d Information",u.getId()));
                List<User> userViews = new ArrayList<>();
                userViews.add(u);
                adminView.usersView(userViews);
                System.out.print("Set new password: ");
                user.setPassword(scanner.nextLine());
                System.out.print("Are you sure you want to reset new password?(Y/N): ");
                confirm[0] = scanner.nextLine();
                if (confirm[0].equalsIgnoreCase("y")) {
                    adminService.resetPassword(user);
                    HelperView.message(String.format("Account id %d has been reset new password...!", user.getId()));
                    found.set(1);
                } else {
                    HelperView.message("You has been canceled reset new password...!");
                    found.set(2);
                }
            }
        });
        if (found.get() == 0){
            HelperView.message(String.format("Account id %d not found...!", user.getId()));
        }
    }
    public void removeAccount(){
        User user = new User();
        adminView.searchById(user, scanner);
        List<User> users = adminService.getLibrarianAndUser();
        final String[] confirm = {""};
        AtomicInteger found = new AtomicInteger();
        found.set(0);
        users.forEach(u -> {
            if (user.getId().equals(u.getId())) {
                HelperView.message(String.format("Account %d Information",u.getId()));
                List<User> userViews = new ArrayList<>();
                userViews.add(u);
                adminView.usersView(userViews);
                System.out.print("Are you sure you want to remove?(Y/N): ");
                confirm[0] = scanner.nextLine();
                if (confirm[0].equalsIgnoreCase("y")) {
                    adminService.removeAccount(user.getId());
                    HelperView.message(String.format("Account id %d has been removed...!", user.getId()));
                    found.set(1);
                } else {
                    HelperView.message("You has been canceled remove...!");
                    found.set(2);
                }
            }
        });
        if (found.get() == 0){
            HelperView.message(String.format("Account id %d not found...!", user.getId()));
        }
    }
    public void getLibrarianAndUser(){
        adminView.usersView(adminService.getLibrarianAndUser());
    }
    public void saveReportExcel(){

    }
    public void backup(){

    }
    public void restore(){

    }
    public void viewReport(){

    }

    public int first(List<User> users, int rowPerPage, int currentPage) {
        if (currentPage == 1) {
            HelperView.message("Now you stand on the first page...!");
            adminView.usersView(users, rowPerPage, currentPage);
        } else {
            currentPage = 1;
            adminView.usersView(users, rowPerPage, currentPage);
        }
        return currentPage;
    }
    public int previous(List<User> users, int rowPerPage, int currentPage) {
        if (currentPage > 1) {
            currentPage--;
            adminView.usersView(users, rowPerPage, currentPage);
        }else {
            currentPage = (int) Math.ceil((double) adminService.getAllUser().size() / rowPerPage);
            adminView.usersView(users, rowPerPage, currentPage);
        }
        return currentPage;
    }
    public int next(List<User> users, int rowPerPage, int currentPage) {
        int totalPages = (int) Math.ceil((double) adminService.getAllUser().size() / rowPerPage);
        if (currentPage < totalPages) {
            currentPage++;
            adminView.usersView(users, rowPerPage, currentPage);
        }else {
            currentPage = 1;
            adminView.usersView(users, rowPerPage, currentPage);
        }
        return currentPage;
    }
    public int last(List<User> users, int rowPerPage, int currentPage) {
        int totalPages = (int) Math.ceil((double) adminService.getAllUser().size() / rowPerPage);
        if (currentPage == totalPages) {
            HelperView.message("Now you stand on the last page...!");
            adminView.usersView(users, rowPerPage, currentPage);
        } else {
            currentPage = totalPages;
            adminView.usersView(users, rowPerPage, currentPage);
        }
        return currentPage;
    }
    public int setRecord(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter record for display: ");
        int record = Integer.parseInt(sc.nextLine());
        HelperView.message(String.format("    Set page to %d record successfully...!    ",record));
        return record;
    }

    public int firstBook(List<Book> books, int rowPerPage, int currentPage) {
        if (currentPage == 1) {
            HelperView.message("Now you stand on the first page...!");
            adminView.bookView(books, rowPerPage, currentPage);
        } else {
            currentPage = 1;
            adminView.bookView(books, rowPerPage, currentPage);
        }
        return currentPage;
    }
    public int previousBook(List<Book> books, int rowPerPage, int currentPage) {
        if (currentPage > 1) {
            currentPage--;
            adminView.bookView(books, rowPerPage, currentPage);
        }else {
            currentPage = (int) Math.ceil((double) adminService.getAllBook().size() / rowPerPage);
            adminView.bookView(books, rowPerPage, currentPage);
        }
        return currentPage;
    }
    public int nextBook(List<Book> books, int rowPerPage, int currentPage) {
        int totalPages = (int) Math.ceil((double) adminService.getAllBook().size() / rowPerPage);
        if (currentPage < totalPages) {
            currentPage++;
            adminView.bookView(books, rowPerPage, currentPage);
        }else {
            currentPage = 1;
            adminView.bookView(books, rowPerPage, currentPage);
        }
        return currentPage;
    }
    public int lastBook(List<Book> books, int rowPerPage, int currentPage) {
        int totalPages = (int) Math.ceil((double) adminService.getAllBook().size() / rowPerPage);
        if (currentPage == totalPages) {
            HelperView.message("Now you stand on the last page...!");
            adminView.bookView(books, rowPerPage, currentPage);
        } else {
            currentPage = totalPages;
            adminView.bookView(books, rowPerPage, currentPage);
        }
        return currentPage;
    }
    public int setRecordBook(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter record for display: ");
        int record = Integer.parseInt(sc.nextLine());
        HelperView.message(String.format("    Set page to %d record successfully...!    ",record));
        return record;
    }
}


