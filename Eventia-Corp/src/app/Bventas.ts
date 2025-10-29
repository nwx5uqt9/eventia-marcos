import { Evento } from "./evento";
import { Ubicacion } from "./ubicacion";
import { Usuario } from "./usuario";

export class Bventas {
  constructor(
    public id: number | null,
    public usuario: Usuario | null,
    public evento: Evento | null,
    public ubicacion: Ubicacion | null,
    public codigoPago: string,
    public fechaVenta: string,
    public total: number
  ) {}
}
