package co.istad.model;

import java.time.LocalDate;
import java.util.Objects;

public class BookDetail {
    private Long id;
    private LocalDate createdDate;
    private Book book;
    private Category category;

    public BookDetail() {
    }

    public BookDetail(Long id, LocalDate createdDate, Book book, Category category) {
        this.id = id;
        this.createdDate = createdDate;
        this.book = book;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookDetail that)) return false;
        return getId().equals(that.getId()) && getCreatedDate().equals(that.getCreatedDate()) && getBook().equals(that.getBook()) && getCategory().equals(that.getCategory());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCreatedDate(), getBook(), getCategory());
    }

    @Override
    public String toString() {
        return "BookDetail{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                ", book=" + book +
                ", category=" + category +
                '}';
    }
}
