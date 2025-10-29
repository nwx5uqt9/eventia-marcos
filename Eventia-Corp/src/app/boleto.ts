import { Evento } from "./evento";

export class Boleto {
  constructor(
    public id: number | null,
    public nombre: string,
    public precio: number,
    public evento: Evento | null
  ) {}
}
