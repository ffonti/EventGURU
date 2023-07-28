import { Ruolo } from "src/app/types/Ruolo";

export interface GetUserDataResponse {
    userId: number;
    nome: string;
    cognome: string;
    email: string;
    username: string;
    password: string;
    ruolo: Ruolo;
    iscrittoNewsletter: boolean;
}