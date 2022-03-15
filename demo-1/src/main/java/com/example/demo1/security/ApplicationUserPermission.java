package com.example.demo1.security;

public enum ApplicationUserPermission {
    STUDENT_READ("student:read"),
    STUDENT_WRITE("student:write"),
    COURSE_READ("course:read"),
    COURSE_WRITE("course:write");

    private final String prmission;

    ApplicationUserPermission(String s) {
        prmission = s;
    }

    public String getPrmission() {
        return prmission;
    }
}
