import { Stato } from "src/app/types/Stato";

export interface GetAllEventiByOrganizzatoreResponse {
    eventoId: number;
    titolo: string;
    descrizione: string;
    dataInizio: Date;
    dataFine: Date;
    dataCreazione: Date;
    stato: Stato;
    nomeLuogo: string;
    lat: string;
    lng: string;
    isRecensioneInviata: boolean;
    recensioni: {
        usernameTurista: string;
        voto: number;
        testo: string;
    }[];
    usernameTuristi: string[];
}