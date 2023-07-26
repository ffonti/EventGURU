import { Ruolo } from "../types/Ruolo";

export interface UserRegisterDTO {
  nome: string;
  cognome: string;
  email: string;
  username: string;
  password: string;
  ruolo: Ruolo;
}
