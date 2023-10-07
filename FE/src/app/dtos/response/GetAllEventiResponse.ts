import { Stato } from "src/app/types/Stato";

export interface GetAllEventiResponse {
    eventoId: number;
    titolo: string;
    descrizione: string;
    dataInizio: Date;
    dataFine: Date;
    dataCreazione: Date;
    stato: Stato;
    nomeLuogo: string;
    lat: number;
    lng: number;
    isRecensioneInviata: boolean;
    recensioni: {
        usernameTurista: string;
        voto: number;
        testo: string;
    }[];
    usernameTuristi: string[];
}