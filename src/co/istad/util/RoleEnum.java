package co.istad.util;

import java.util.Set;

import static co.istad.util.Permission.*;

public enum RoleEnum {

    ADMIN(Set.of(CREATE, READ, UPDATE, DELETE)),
    LIBRARIAN(Set.of(CREATE, READ, UPDATE, DELETE)),
    USER(Set.of(CREATE, READ, UPDATE, DELETE));
    private Set<Permission> permissions;
    RoleEnum(Set<Permission> permissions) {
        this.permissions = permissions;
    }
    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }
}
