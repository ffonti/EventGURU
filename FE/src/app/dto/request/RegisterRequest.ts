import { Ruolo } from "src/app/types/Ruolo";

export class RegisterRequest {
    private nome!: string;
    private cognome!: string;
    private email!: string;
    private username!: string;
    private password!: string;
    private ruolo!: Ruolo;

    constructor(nome: string, cognome: string, email: string, username: string, password: string, ruolo: Ruolo) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.username = username;
        this.password = password;
        this.ruolo = ruolo;
    }

    public getNome(): string {
        return this.nome;
    }

    public getCognome(): string {
        return this.cognome;
    }

    public getEmail(): string {
        return this.email;
    }

    public getUsername(): string {
        return this.username;
    }

    public getPassword(): string {
        return this.password;
    }

    public getRuolo(): Ruolo {
        return this.ruolo;
    }
}
