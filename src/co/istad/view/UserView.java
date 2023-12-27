package co.istad.view;

import co.istad.model.Book;
import co.istad.model.Borrow;
import co.istad.service.UserServiceImpl;
import co.istad.util.Singleton;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class UserView {
    private final Scanner scanner;
    private final UserServiceImpl userService;

    public UserView() {
        scanner = Singleton.scanner();
        userService = Singleton.getUserServiceImpl();
    }

    public int userDashboardView(Scanner scanner) {
        int option = 0;
        try {
            Table table = new Table(3, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.SURROUND);
            table.addCell("     1) Search Book     ");
            table.addCell("     2) Borrow     ");
            table.addCell("     3) ViewBorrow     ");
            table.addCell("     4) Return     ");
            table.addCell("     5) History     ");
            table.addCell("     6) Logout     ");
            System.out.println(table.render());
            System.out.println(" ".repeat(50));
            System.out.print("Please choose option : ");
            option = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Only number required!");
        }
        return option;
    }

    public int searchOption(Scanner scanner) {
        int option;
           Table table = new Table(2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
           table.addCell(" ".repeat(10) + "1. Search Book By Id" + " ".repeat(10));
           table.addCell(" ".repeat(10) + "2. Search Book By Title" + " ".repeat(10));
           table.addCell(" ".repeat(10) + "3. Search Book By Author" + " ".repeat(10));
           table.addCell(" ".repeat(10) + "4. Search Book By Category" + " ".repeat(10));
           table.addCell(" ".repeat(42) + "5. Exit" + " ".repeat(42) , 2 );
            System.out.println();
            System.out.println(table.render());
            System.out.println();
            System.out.print("Enter Option :");
           option = Integer.parseInt(scanner.nextLine());
           return option;
    }

    public void dashboardOverview() {
        System.out.println();
        HelperView.welcome("=".repeat(117));
        HelperView.welcome(" ".repeat(55) + "Welcome");
        HelperView.welcome("=".repeat(118));
        System.out.println(" ".repeat(45)+"Here are books in library");

        List<Book> data =  userService.getAllBook();
        Table table = new Table(6, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell("   Category ID   ");
        table.addCell("      Title   ");
        table.addCell("     Author   ");
        table.addCell("   Description   ");
        table.addCell("   Category   ");
        table.addCell("      Stock   ");
        for (Book books : data) {
            if (books.getQuantity() > 0) {
                table.addCell(books.getId().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(books.getTitle(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(books.getAuthor().getFirstName() + books.getAuthor().getLastName(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(books.getDescription(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(books.getBookDetail().getCategory().getName(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell("Book Still Avaibile", new CellStyle(CellStyle.HorizontalAlign.CENTER));
            }
        }
        System.out.println(table.render());
    }

        public void bookHistory() {
        List<Borrow> bookHis = userService.bookHistory();
        Table table = new Table(4, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell("Book ID");
        table.addCell("Title");
        table.addCell("Borrow Date");
        table.addCell("Book Deadline");
        if (bookHis == null || bookHis.isEmpty()) {
            HelperView.message("No Data");
        } else {
            bookHis.forEach(books -> {
                table.addCell(books.getBook().getId().toString());
                table.addCell(books.getBook().getTitle());
                table.addCell(books.getBorrowDate().toString());
                table.addCell(books.getDeadline().toString());
            });
            System.out.println(table.render());
        }
    }

    public void viewBorrowHistory() {
        List<Borrow> bookHis = userService.borrowHistory();
        Table table = new Table(4, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell("Book ID");
        table.addCell("Title");
        table.addCell("Borrow Date");
        table.addCell("Book Deadline");
        if (bookHis == null || bookHis.isEmpty()) {
            HelperView.message("All Book Return ");
        } else {
            bookHis.forEach(books -> {
                table.addCell(books.getBook().getId().toString());
                table.addCell(books.getBook().getTitle());
                table.addCell(books.getBorrowDate().toString());
                table.addCell(books.getDeadline().toString());
            });
            System.out.println(table.render());
        }
    }

    public Long returnBook(Scanner scanner) {
        long bookId = 0;
        List<Borrow> bookHis = userService.borrowHistory();
        try {
            if (bookHis.isEmpty()) {
                System.out.println("No books to return.");
            } else {
                System.out.println("Books you have borrowed:");
                for (Borrow borrow : bookHis) {
                    System.out.println("Book ID: " + borrow.getBook().getId() + ", Title: " + borrow.getBook().getTitle());
                }
                // Prompt user for input
                while (true) {
                    System.out.print("Enter Book ID You Want to Return : ");
                    long userInput = Long.parseLong(scanner.nextLine());
                    boolean isValidBookId = false;
                    for (Borrow borrow : bookHis) {
                        if (userInput == borrow.getBook().getId()) {
                            isValidBookId = true;
                            bookId = userInput;
                            System.out.println("Waiting For Confirm");
                            userService.returnBook(userInput);
                            break;
                        }
                    }
                    if (!isValidBookId) {
                        System.out.println("Invalid Book ID. Please enter a valid Book ID.");
                    } else {
                        break;
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid input. Please enter a valid number.");
        }

        return bookId;
    }
    public void borrowBook(Scanner scanner) {
        int borrowCount = userService.countBorrowBook();
        List<Book> books = userService.getAllBook();
        System.out.println("Book Limit is 3 ");
        boolean bookFound = false;
        if(borrowCount == 3) {
            System.out.println("You Have Reach Borrow Limit");
            return;
        }
            while (true) {
                System.out.print("Enter Book ID You Want To Borrow ");
                Long option = Long.parseLong(scanner.nextLine());
                if (option == 0) {
                    System.out.println("Exiting book borrowing process.");
                    break;
                }
                for (Book book : books) {
                    if (option.equals(book.getId())) {
                        bookFound = true;
                        userService.borrowBook(option);
                        System.out.println("Book ID : " + book.getId() + " Waiting For Confirm");
                        break;
                    }
                }
                if (!bookFound) {
                    System.out.println("No Book ID Found. Please try again.");
                } else {
                    // If the book is found, exit the loop
                    break;
                }
            }

    }
    public void searchBookById (){
        System.out.print("Please Enter Book ID : ");
        try {
            long option = Long.parseLong(scanner.nextLine());
            Optional<Book> searchBook = userService.searchBookById(option);
            if(searchBook.isEmpty()) {
                System.out.println("Book with id " + option + " is not found");
            }
            Table table = new Table(3, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
            table.addCell("Book ID");
            table.addCell("Title" );
            table.addCell("Quantity");
            searchBook.ifPresent(book -> {
                table.addCell(book.getId().toString());
                table.addCell(book.getTitle());
               if(book.getQuantity() == 0) {
                   table.addCell("Book Out Of Stock");
               } else {
                   table.addCell(book.getQuantity().toString());
               }

            });
            System.out.println(table.render());
        }catch (NumberFormatException e) {
            HelperView.message("require Only Number!");
        }

    }
    public void searchBookByTitle (){
        System.out.print("Please Enter Book Title : ");
        String option = scanner.nextLine().toLowerCase();
        Optional<Book> searchBook = userService.searchBookByTitle(option);
        if(searchBook.isEmpty()) {
            System.out.println("Book with Title " + option + " is not found");
        }
        Table table = new Table(3, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell("Book ID");
        table.addCell("Title" );
        table.addCell("Quantity");
        searchBook.ifPresent(book -> {
            table.addCell(book.getId().toString());
            table.addCell(book.getTitle());
            if(book.getQuantity() == 0) {
                table.addCell("Book Out Of Stock");
            } else {
                table.addCell(book.getQuantity().toString());
            }
        });
        System.out.println(table.render());
    }
        public void searchBookByAuthor (){
            System.out.print("Please Enter Book Author : ");
            String option = scanner.nextLine().toLowerCase();
            Optional<List<Book>> searchBook = userService.searchBookByAuthor(option);
            if(searchBook.isEmpty()) {
                System.out.println("Book with Author " + option + " is not found");
            }
            Table table = new Table(3, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
            table.addCell("Book ID");
            table.addCell("Title" );
            table.addCell("Quantity");
            searchBook.ifPresent(bookList ->
                    bookList.forEach( br -> {
                                table.addCell(br.getId().toString());
                                table.addCell(br.getTitle());
                        if(br.getQuantity() == 0) {
                            table.addCell("Book Out Of Stock");
                        } else {
                            table.addCell(br.getQuantity().toString());
                        }
                            }
                    )

            );
            System.out.println(table.render());
        }
    public void searchBookByCategory (){
        System.out.print("Please Enter Book Category : ");
        String option = scanner.nextLine();
        Optional<List<Book>> searchBook = userService.searchBookByCategory(option);
        if(searchBook.isEmpty()) {
            System.out.println("Book with Author " + option + " is not found");
        }
        Table table = new Table(3, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell("Book ID");
        table.addCell("Title" );
        table.addCell("Quantity");
        searchBook.ifPresent(bookList ->
                bookList.forEach( br -> {
                            table.addCell(br.getId().toString());
                            table.addCell(br.getTitle());
                    if(br.getQuantity() == 0) {
                        table.addCell("Book Out Of Stock");
                    } else {
                        table.addCell(br.getQuantity().toString());
                    }
                        }
                )

        );
        System.out.println(table.render());
    }
}



