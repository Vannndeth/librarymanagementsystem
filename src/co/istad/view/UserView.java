package co.istad.view;

import co.istad.model.Book;
import co.istad.model.Borrow;
import co.istad.util.Singleton;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.List;
import java.util.Optional;
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

    public int viewSearchOption(Scanner scanner){
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
    public void viewAllBook (List<Book> books) {
        Table table = new Table(3, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell("Id");
        table.addCell("Title");
        table.addCell("Quantity");
        books.forEach(book -> {
            table.addCell(book.getId().toString());
            table.addCell(book.getTitle());
            table.addCell(book.getQuantity().toString());
        });
        System.out.println(table.render());
    }
    public void viewSearchBookById (Optional<Book> bookId) {
        Table table = new Table(3, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell("Book ID");
        table.addCell("Title" );
        table.addCell("Quantity");
        bookId.ifPresent(book -> {
            table.addCell(book.getId().toString());
            table.addCell(book.getTitle());
            table.addCell(book.getQuantity().toString());
        });
        System.out.println(table.render());
    }
    public void viewSearchBookByTitle (Optional<Book> bookTitle) {
        Table table = new Table(3, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell("Book ID");
        table.addCell("Title" );
        table.addCell("Quantity");
        bookTitle.ifPresent(bt ->
                {
                    table.addCell(bt.getId().toString());
                    table.addCell(bt.getTitle());
                    table.addCell(bt.getQuantity().toString());
                }
        );
        System.out.println(table.render());
    }
    public void viewSearchBookByAuhtor (Optional<List<Book>> bookAuthor) {
        Table table = new Table(3, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell("Book ID");
        table.addCell("Title" );
        table.addCell("Quantity");
        bookAuthor.ifPresent(bookList ->
                {
                    bookList.forEach( br -> {
                                table.addCell(br.getId().toString());
                                table.addCell(br.getTitle());
                                table.addCell(br.getQuantity().toString());
                            }
                    );
                }
        );
        System.out.println(table.render());
    }
    public void viewSearchBookByCategory (Optional<List<Book>> bookCategory) {
        Table table = new Table(3, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell("Book ID");
        table.addCell("Title" );
        table.addCell("Quantity");
        bookCategory.ifPresent(cateList ->
                {
                    cateList.forEach( cate -> {
                                table.addCell(cate.getId().toString());
                                table.addCell(cate.getTitle());
                                table.addCell(cate.getQuantity().toString());

                            }
                    );
                }
        );
        System.out.println(table.render());
    }
    public void viewBorrowBook (Scanner scanner , int bookIndex, Book[] books) {
        while (true) {
            System.out.print("Enter Book ID to borrow: ");
            Long bookId = Long.parseLong(scanner.nextLine());
            System.out.print("Book ID: " + bookId + ". Confirm borrowing? (Y/N): ");
            String userInput = scanner.nextLine();
            if (userInput.equalsIgnoreCase("Y")) {
                books[bookIndex++] = new Book(bookId);
                Singleton.getUserServiceImpl().borrowBook(bookId);
                System.out.println("Book added to your borrowed books.");
                if (bookIndex < books.length) {
                    System.out.print("Borrow another book? (Y/N): ");
                    userInput = scanner.nextLine();
                    if (!userInput.equalsIgnoreCase("Y")) {
                        break;
                    }
                } else {
                    System.out.println("You have reached the maximum borrow limit of 3 books.");
                    break;
                }
            } else {
                System.out.println("Invalid input. Please enter Y or N.");
            }
        }
        System.out.println("Borrowing completed.");
        System.out.println("Borrowed books:");
        System.out.println("Waiting For Confirm");
        for (Book book : books) {
            if (book != null) {
                System.out.println("Book ID: " + book.getId());
            }
        }
    }
    public void viewPreviewBook(Book book) {
        Table table = new Table(6, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell("Category ID");
        table.addCell("Title" );
        table.addCell("Author");
        table.addCell("Description");
        table.addCell("Category");
        table.addCell("Stock");
        table.addCell(book.getId().toString());
        table.addCell(book.getTitle());
        table.addCell(book.getAuthor().getFirstName() + book.getAuthor().getLastName());
        table.addCell(book.getDescription());
        table.addCell(book.getBookDetail().getCategory().getName());
        if(book.getQuantity() > 0) {
            table.addCell("Book Still Available");
        }
        System.out.println(table.render());
    }
    public void viewBookHistory(List<Borrow> bookHis) {
        Table table = new Table(4, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
        table.addCell("Book ID");
        table.addCell("Title" );
        table.addCell("Borrow Date");
        table.addCell("Book Deadline");
        bookHis.forEach( books -> {
                    table.addCell(books.getBook().getId().toString());
                    table.addCell(books.getBook().getTitle());
                    table.addCell(books.getBorrowDate().toString());
                    table.addCell(books.getDeadline().toString());
                }
        );
        System.out.println(table.render());
    }
}
