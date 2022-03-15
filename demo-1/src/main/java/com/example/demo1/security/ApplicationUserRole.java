package com.example.demo1.security;

import com.google.common.collect.Sets;

import java.util.Set;

import static com.example.demo1.security.ApplicationUserPermission.*;

public enum ApplicationUserRole {
    STUDENT(Sets.newHashSet()),
    ADMIN(Sets.newHashSet(COURSE_READ, COURSE_WRITE, STUDENT_READ, STUDENT_WRITE));

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    private Set<ApplicationUserPermission> permissions;

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }
}
