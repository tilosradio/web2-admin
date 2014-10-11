package hu.tilos.radio.backend;

import javax.enterprise.context.RequestScoped;
import javax.inject.Scope;

@RequestScoped
public class UserInfo {
    private String username;
    private int role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
