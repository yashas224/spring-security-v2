package com.example.demo1.jwt;

public class ApplicationAuthenticationRequest {
    private String username;
    private String password;

    public ApplicationAuthenticationRequest() {
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
