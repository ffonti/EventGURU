package it.polimi.iswpf.util;

import it.polimi.iswpf.exception.BadRequestException;
import it.polimi.iswpf.model.User;

/**
 * Design pattern singleton. Classe che mantiene le informazioni dell'utente in sessione.
 */
public final class SessionManager {

    private static SessionManager instance; //Attributo statico dell'istanza, come da definizione del pattern.
    private User loggedUser; //Attributo dove verrà salvato l'utente in sessione.

    /**
     * Costruttore dove viene inizializzata la variabile per l'utente in sessione.
     */
    private SessionManager() {

        this.loggedUser = null;
    }

    /**
     * Metodo statico per prendere l'istanza.
     * @return L'unica istanza della classe.
     */
    public static SessionManager getInstance() {

        //Se l'istanza non esiste, ne viene creata una.
        if(SessionManager.instance == null) {
            SessionManager.instance = new SessionManager();
        }

        return SessionManager.instance;
    }

    /**
     * Quando viene eseguito il login, i dati dell'utente vengono salvati in una variabile.
     * @param user Utente appena loggato.
     */
    public void loginUser(User user) {

        try {
            this.loggedUser = user;

            //Log con i dati dell'utente.
            System.out.println("---------------------------------------------------------");
            System.out.println("USER LOGGED: " + user.getUsername() + " - ROLE: " + user.getRuolo().toString());
            System.out.println("---------------------------------------------------------");

        } catch (BadRequestException e) {
            throw new BadRequestException("Errore nell'autenticazione");
        }
    }

    /**
     * Quando viene eseguito il logout, vengono eliminati i dati dell'utente.
     */
    public void logoutUser() {

        this.loggedUser = null;

        //Log di messaggio per il logout.
        System.out.println("---------------------------------------------------------");
        System.out.println("LOGOUT. NOW NO USER LOGGED");
        System.out.println("---------------------------------------------------------");
    }

    /**
     * Metodo di utilità per prendere i dati dell'utente in sessione.
     * @return L'utente in sessione.
     */
    public User getLoggedUser() {

        return this.loggedUser;
    }
}
