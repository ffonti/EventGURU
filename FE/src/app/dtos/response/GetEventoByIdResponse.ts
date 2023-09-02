import { Stato } from "src/app/types/Stato";

export interface GetEventoByIdResponse {
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
}