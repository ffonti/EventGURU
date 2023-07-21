package it.polimi.iswpf.strategy;

import it.polimi.iswpf.dto.request.RegisterRequest;
import it.polimi.iswpf.exception.RegistrazioneNonRiuscitaException;
import it.polimi.iswpf.model.Ruolo;
import it.polimi.iswpf.model.Turista;
import it.polimi.iswpf.model.User;
import it.polimi.iswpf.repository.TuristaRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthTuristaStrategy implements AuthStrategy {

    private final TuristaRepository turistaRepository;

    @Override
    public Ruolo getRuolo() {
        return Ruolo.TURISTA;
    }

    @Override
    public User register(RegisterRequest request) throws Exception {

        final String nome = request.getNome().trim();
        final String cognome = request.getCognome().trim();
        final String email = request.getEmail().trim().toLowerCase();
        final String username = request.getUsername().trim().toLowerCase();
        final String password = request.getPassword();
        final Ruolo ruolo = request.getRuolo();

        //Controllo che tutti i campi non siano vuoti.
        checkUserData(List.of(nome, cognome, email, username, password, ruolo.name()));

        Turista turista = new Turista(email, password, username, nome, cognome);
        System.out.println(turista + "-------------------------------");
        turistaRepository.save(turista);

        Optional<Turista> turistaRegistered = turistaRepository.findByUsername(request.getUsername());
        if(turistaRegistered.isEmpty()) {
            throw new RegistrazioneNonRiuscitaException();
        }

        return turista;
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
