import { Ruolo } from "src/app/types/Ruolo";

export interface RegisterRequest {
    nome: string;
    cognome: string;
    email: string;
    username: string;
    password: string;
    ruolo: Ruolo;
}
