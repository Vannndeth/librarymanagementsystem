package co.istad.util;

import java.util.Objects;

public class Pagination{
    private int currentPage, totalPage, limit;

    public Pagination() {
    }

    public Pagination(int currentPage, int totalPage, int limit) {
        this.currentPage = currentPage;
        this.totalPage = totalPage;
        this.limit = limit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pagination that = (Pagination) o;
        return currentPage == that.currentPage && totalPage == that.totalPage && limit == that.limit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentPage, totalPage, limit);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "Pagination{" +
                "currentPage=" + currentPage +
                ", totalPage=" + totalPage +
                ", limit=" + limit +
                '}';
    }
}
