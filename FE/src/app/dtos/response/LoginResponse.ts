export interface LoginResponse {
    userId: number;
    nome: string;
    cognome: string;
    username: string;
    ruolo: string;
    email: string;
    iscrittoNewsletter: boolean;
    message: string;
    jwt: string;
}