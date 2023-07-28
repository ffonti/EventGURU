import { User } from "src/app/models/User";

export interface LoginResponse {
    user: User;
    message: string;
    jwt: string;
}