package it.polimi.iswpf.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe dove inserire vari metodi di utilità.
 * Implementato tramite il pattern Singleton.
 */
public class UtilMethods {

    //Unica istanza della classe;
    private static UtilMethods instance;

    /**
     * Costruttore vuoto per il Singleton pattern.
     */
    private UtilMethods() { }

    /**
     * Metodo del Singleton pattern per prendere sempre la stessa istanza e non crearne una nuova.
     * @return L'unica istanza della classe.
     */
    public static UtilMethods getInstance() {

        //Se non esiste ancora un'istanza, la crea.
        if(instance == null) {
            instance = new UtilMethods();
        }

        return instance;
    }

    /**
     * Metodo che trasforma una data in una stringa formattata.
     * @param date Data da formattare.
     * @return Stringa con la data formattata.
     */
    public String dateToString(LocalDateTime date) {

        //Specifico il pattern con il quale formattare la stringa contenente la data.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        //Formatto l'oggetto LocalDateTime in una stringa e lo ritorno.
        return date.format(formatter);
    }
}
