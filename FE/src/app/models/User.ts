import { Ruolo } from "../types/Ruolo";

export interface User {
    userId: number;
    nome: string;
    cognome: string;
    email: string;
    username: string;
    password: string;
    ruolo: Ruolo;
    iscrittoNewsletter: boolean;
}