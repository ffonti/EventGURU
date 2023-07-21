package it.polimi.iswpf.strategy;

import it.polimi.iswpf.dto.request.RegisterRequest;
import it.polimi.iswpf.model.Ruolo;
import it.polimi.iswpf.model.User;
import lombok.NonNull;

import java.util.List;

public class AuthOrganizzatoreStrategy implements AuthStrategy {

    @Override
    public Ruolo getRuolo() {
        return Ruolo.ORGANIZZATORE;
    }

    @Override
    public User register(RegisterRequest request) {
        System.out.println("--------------ORGANIZZATORE");
        return null;
    }

    /**
     * Per ogni elemento della lista, viene controllato che non sia vuoto.
     * @param dataList lista con i campi inseriti dal client.
     * @throws Exception eccezione causata dal campo vuoto.
     */
    @Override
    public void checkUserData(@NonNull List<String> dataList) throws Exception {
        for(String data : dataList) {
            if(data.isEmpty() || data.isBlank()) {
                throw new Exception("Inserire tutti i campi");
            }
        }
    }
}
