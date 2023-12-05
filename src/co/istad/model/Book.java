package co.istad.model;

import java.time.LocalDate;
import java.util.Objects;

public class Book {
    private Long id;
    private String title;
    private Integer quantity;
    private String description;
    private LocalDate createdDate;
    private Author author;
    private User user;

    public Book() {
    }

    public Book(Long id, String title, Integer quantity, String description, LocalDate createdDate, Author author, User user) {
        this.id = id;
        this.title = title;
        this.quantity = quantity;
        this.description = description;
        this.createdDate = createdDate;
        this.author = author;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book book)) return false;
        return getId().equals(book.getId()) && getTitle().equals(book.getTitle()) && getQuantity().equals(book.getQuantity()) && getDescription().equals(book.getDescription()) && getCreatedDate().equals(book.getCreatedDate()) && getAuthor().equals(book.getAuthor()) && getUser().equals(book.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getQuantity(), getDescription(), getCreatedDate(), getAuthor(), getUser());
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", quantity=" + quantity +
                ", description='" + description + '\'' +
                ", createdDate=" + createdDate +
                ", author=" + author +
                ", user=" + user +
                '}';
    }
}
