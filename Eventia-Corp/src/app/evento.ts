import { EstadoEvento } from "./estadoEvento";
import { Organizador } from "./organizador";
import { TipoEvento } from "./tipoEvento";

export class Evento {
  constructor(
    public id: number | null,
    public nombre: string,
    public descripcion: string,
    public fechaHora: string,
    public tipoEvento: TipoEvento | null,
    public organizador: Organizador | null,
    public estadoEvento: EstadoEvento | null
  ) {}
}
