import { Evento } from "./evento";
import { Ubicacion } from "./ubicacion";
import { Usuario } from "./usuario";

export class Bventas {
  id?: number | null;
  usuario?: Usuario;
  evento?: Evento;
  ubicacion?: Ubicacion;
  codigoPago?: string;
  fechaVenta?: string;
  total?: number;

  constructor(
    id: number | null,
    usuario: Usuario | { id: number } | null,
    evento: Evento | { id: number } | null,
    ubicacion: Ubicacion | { id: number } | null,
    codigoPago: string,
    fechaVenta: string,
    total: number
  ) {
    this.id = id;
    // Convertir { id: number } a objeto parcial si es necesario
    this.usuario = usuario as Usuario || undefined;
    this.evento = evento as Evento || undefined;
    this.ubicacion = ubicacion as Ubicacion || undefined;
    this.codigoPago = codigoPago;
    this.fechaVenta = fechaVenta;
    this.total = total;
  }
}

