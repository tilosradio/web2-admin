package hu.tilos.radio.backend;

import hu.tilos.radio.backend.data.UserInfo;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class Session {

    private UserInfo currentUser;

    public UserInfo getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserInfo currentUser) {
        this.currentUser = currentUser;
    }
}
