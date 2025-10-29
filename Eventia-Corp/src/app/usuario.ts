import { RolUsuario } from "./rolUsuario";

export class Usuario {
  constructor(
    public id: number | null,
    public nombre: string,
    public apellidos: string,
    public nombreusuario: string,
    public password: string,
    public dni: string,
    public edad: number,
    public sexo: string,
    public email: string,
    public telefono: string,
    public direccion: string,
    public rolUsuario: RolUsuario | null
  ) {}
}
