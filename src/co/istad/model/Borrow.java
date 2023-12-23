package co.istad.model;

import java.time.LocalDate;
import java.util.Objects;

public class Borrow {
    private Long id;
    private Integer quantity;
    private LocalDate borrowDate;
    private LocalDate deadline;
    private boolean isReturn;
    private boolean isBorrow;
    private User user;
    private Book book;
    private LocalDate startBorrowDate;
    private LocalDate deadlineBorrowDate;

    public Borrow() {
    }

    public Borrow(Long id, Integer quantity, LocalDate borrowDate, LocalDate deadline, boolean isReturn, boolean isBorrow, User user, Book book, LocalDate startBorrowDate, LocalDate deadlineBorrowDate) {
        this.id = id;
        this.quantity = quantity;
        this.borrowDate = borrowDate;
        this.deadline = deadline;
        this.isReturn = isReturn;
        this.isBorrow = isBorrow;
        this.user = user;
        this.book = book;
        this.startBorrowDate = startBorrowDate;
        this.deadlineBorrowDate = deadlineBorrowDate;
    }

    public Borrow(Long id, Integer quantity, LocalDate borrowDate, LocalDate deadline, boolean isReturn, boolean isBorrow, User user, Book book) {
        this.id = id;
        this.quantity = quantity;
        this.borrowDate = borrowDate;
        this.deadline = deadline;
        this.isReturn = isReturn;
        this.isBorrow = isBorrow;
        this.user = user;
        this.book = book;
    }

    public LocalDate getStartBorrowDate() {
        return startBorrowDate;
    }

    public void setStartBorrowDate(LocalDate startBorrowDate) {
        this.startBorrowDate = startBorrowDate;
    }

    public LocalDate getDeadlineBorrowDate() {
        return deadlineBorrowDate;
    }

    public void setDeadlineBorrowDate(LocalDate deadlineBorrowDate) {
        this.deadlineBorrowDate = deadlineBorrowDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public boolean isReturn() {
        return isReturn;
    }

    public void setReturn(boolean aReturn) {
        isReturn = aReturn;
    }

    public boolean isBorrow() {
        return isBorrow;
    }

    public void setBorrow(boolean borrow) {
        isBorrow = borrow;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Borrow borrow)) return false;
        return isReturn() == borrow.isReturn() && isBorrow() == borrow.isBorrow() && getId().equals(borrow.getId()) && getQuantity().equals(borrow.getQuantity()) && getBorrowDate().equals(borrow.getBorrowDate()) && getDeadline().equals(borrow.getDeadline()) && getUser().equals(borrow.getUser()) && getBook().equals(borrow.getBook());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getQuantity(), getBorrowDate(), getDeadline(), isReturn(), isBorrow(), getUser(), getBook());
    }


    @Override
    public String toString() {
        return "Borrow{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", borrowDate=" + borrowDate +
                ", deadline=" + deadline +
                ", isReturn=" + isReturn +
                ", isBorrow=" + isBorrow +
                ", user=" + user +
                ", book=" + book +
                '}';
    }
}
