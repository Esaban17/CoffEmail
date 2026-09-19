package com.coffemail.models;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Datos de un usuario registrado.
 *
 * <p>Esta clase nunca guarda la contraseña en claro: {@link #getPasswordHash()}
 * contiene el hash autocontenido producido por
 * {@code com.coffemail.security.PasswordHasher}, que ya incluye el salt.
 *
 * @author Estuardo Sabán
 */
public class User {

    private String username;
    private String name;
    private String lastName;
    private String passwordHash;
    private Role role;
    private LocalDate birthDate;
    private String email;
    private String phone;
    private String photoPath;
    private boolean active;

    public User() {
        this.role = Role.USER;
        this.active = true;
    }

    public User(String username, String name, String lastName, String passwordHash, Role role,
                LocalDate birthDate, String email, String phone, String photoPath, boolean active) {
        this.username = username;
        this.name = name;
        this.lastName = lastName;
        this.passwordHash = passwordHash;
        this.role = Objects.requireNonNullElse(role, Role.USER);
        this.birthDate = birthDate;
        this.email = email;
        this.phone = phone;
        this.photoPath = photoPath;
        this.active = active;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = Objects.requireNonNullElse(role, Role.USER);
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return el teléfono como texto: un {@code int} desbordaba con código de
     *         país y eliminaba los ceros a la izquierda
     */
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "User{username=" + username + ", email=" + email + ", role=" + role + "}";
    }
}
