package it.polimi.iswpf.util;

import it.polimi.iswpf.exception.BadRequestException;
import it.polimi.iswpf.model.User;

public final class SessionManager {

    private static SessionManager instance;
    private User loggedUser;

    private SessionManager() {
        this.loggedUser = null;
    }

    public static SessionManager getInstance() {
        if(SessionManager.instance == null) {
            SessionManager.instance = new SessionManager();
        }

        return SessionManager.instance;
    }

    public void loginUser(User user) {

        try {
            this.loggedUser = user;

            System.out.println("---------------------------------------------------------");
            System.out.println("USER LOGGED: " + user.getUsername() + " - ROLE: " + user.getRuolo().toString());
            System.out.println("---------------------------------------------------------");

        } catch (BadRequestException e) {
            throw new BadRequestException("Errore nell'autenticazione");
        }
    }

//    public void logoutUser() {
//        this.loggedUser = null;
//
//        System.out.println("---------------------------------------------------------");
//        System.out.println("LOGOUT. NOW NO USER LOGGED");
//        System.out.println("---------------------------------------------------------");
//    }

    public User getLoggedUser() {
        return this.loggedUser;
    }
}
