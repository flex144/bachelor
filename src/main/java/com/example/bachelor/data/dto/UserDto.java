package com.example.bachelor.data.dto;

import com.example.bachelor.data.enums.UserRoles;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class UserDto {

    private Long userId;
    private String firstName;
    private String lastName;
    private UserRoles role;
    private String email;
    private String password;
    private String confirmationPassword;
    private boolean active;
    private boolean confirmed;
    private String localbranch;

    public UserDto() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getConfirmationPassword() {
        return confirmationPassword;
    }

    public void setConfirmationPassword(String confirmationPassword) {
        this.confirmationPassword = confirmationPassword;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getLocalbranch() {
        return localbranch;
    }

    public void setLocalbranch(String localbranch) {
        this.localbranch = localbranch;
    }

    public String getUserRoleString() {
        switch (getRole()) {
            case ROLE_USER:
                return "User";
            case ROLE_ADMIN:
                return "Admin";
            default:
                throw new IllegalStateException("Nutzer hat keine oder unbekannte Rolle!");
        }
    }
}
