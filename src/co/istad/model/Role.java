package co.istad.model;

import co.istad.util.RoleEnum;

import java.time.LocalDate;
import java.util.Objects;

public class Role {
    private Long id;
    private RoleEnum role;
    private LocalDate createdDate;
    public Role() {
    }

    public Role(Long id, RoleEnum role, LocalDate createdDate) {
        this.id = id;
        this.role = role;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role role1)) return false;
        return getId().equals(role1.getId()) && getRole() == role1.getRole() && getCreatedDate().equals(role1.getCreatedDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRole(), getCreatedDate());
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", role=" + role +
                ", createdDate=" + createdDate +
                '}';
    }
}
