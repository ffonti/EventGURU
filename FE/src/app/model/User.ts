import { Ruolo } from "../types/Ruolo";

export class User {
    private userId!: number;
    private nome!: string;
    private cognome!: string;
    private email!: string;
    private username!: string;
    private password!: string;
    private ruolo!: Ruolo;
    private iscrittoNewsletter!: boolean;
    // private eventi!: string;
    // private recensioni!: string;
    // private seguiti!: string;
    // private iscrizioni!: string;

    constructor() { }

    public setUserId(userId: number): void {
        this.userId = userId;
    }

    public getUserId(): number {
        return this.userId;
    }

    public setNome(nome: string): void {
        this.nome = nome;
    }

    public getNome(): string {
        return this.nome;
    }

    public setCognome(cognome: string): void {
        this.cognome = cognome;
    }

    public getCognome(): string {
        return this.cognome;
    }

    public setEmail(email: string): void {
        this.email = email;
    }

    public getEmail(): string {
        return this.email;
    }

    public setUsername(username: string): void {
        this.username = username;
    }

    public getUsername(): string {
        return this.username;
    }

    public setPassword(password: string): void {
        this.password = password;
    }

    public getPassword(): string {
        return this.password;
    }

    public setRuolo(ruolo: Ruolo): void {
        this.ruolo = ruolo;
    }

    public getRuolo(): Ruolo {
        return this.ruolo;
    }

    public setIscrittoNewsletter(iscrittoNewsletter: boolean): void {
        this.iscrittoNewsletter = iscrittoNewsletter;
    }

    public getIscrittoNewsletter(): boolean {
        return this.iscrittoNewsletter;
    }
}