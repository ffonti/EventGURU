export interface AdminCreaEventoRequest {
    titolo: string;
    descrizione: string;
    dataInizio: Date;
    dataFine: Date;
    lat: number;
    lng: number;
    nomeLuogo: string;
    usernameOrganizzatore: string;
}