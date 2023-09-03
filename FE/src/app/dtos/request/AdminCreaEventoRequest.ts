export interface AdminCreaEventoRequest {
    titolo: string;
    descrizione: string;
    dataInizio: Date;
    dataFine: Date;
    lat: string;
    lng: string;
    nomeLuogo: string;
    usernameOrganizzatore: string;
}