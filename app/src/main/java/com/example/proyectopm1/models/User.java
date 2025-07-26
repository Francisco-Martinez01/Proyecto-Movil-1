package com.example.proyectopm1.models;

public class User {
    private String id;
    private String name;
    private String email;
    private String password; // Si lo usas para registro

    // Getter y Setter para email
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter y Setter para name
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    // Getter y Setter para password
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    // ... otros getters y setters
}
