export interface GetAllEventiByOrganizzatoreResponse {
    eventoId: number;
    titolo: string;
    descrizione: string;
    dataInizio: Date;
    dataFine: Date;
    dataCreazione: Date;
    nomeLuogo: string;
    lat: string;
    lng: string;
}