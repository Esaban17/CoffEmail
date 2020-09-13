package com.coffemail.models;

import java.util.*;
/**
 *
 * @author Estuardo Sabán
 */
public class User {
    String user;
    String name;
    String lastName;
    String password;
    boolean role;
    Date birthDate;
    String email;
    int phone;
    String pathPhoto;
    boolean status;
    
    public User() {
    }
    
    public User(String user, String name, String lastName, String password, boolean role, Date birthDate, String email, int phone, String pathPhoto, boolean status) {
        this.user = user;
        this.name = name;
        this.lastName = lastName;
        this.password = password;
        this.role = role;
        this.birthDate = birthDate;
        this.email = email;
        this.phone = phone;
        this.pathPhoto = pathPhoto;
        this.status = status;
    }
    
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getRole() {
        return role;
    }

    public void setRole(boolean role) {
        this.role = role;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getPathPhoto() {
        return pathPhoto;
    }

    public void setPathPhoto(String pathPhoto) {
        this.pathPhoto = pathPhoto;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    } 
}
