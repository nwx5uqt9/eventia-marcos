import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface BoletaVenta {
  id: number;
  codigoPago: string;
  fechaVenta: string;
  total: number;
  cantidad: number;
  metodoPago: string;
  usuario: {
    id: number;
    nombre: string;
    nombreusuario: string;
  };
  evento: {
    id: number;
    nombre: string;
    descripcion: string;
    fechaHora: string;
  };
  ubicacion?: {
    id: number;
    nombre: string;
  };
}

@Injectable({
  providedIn: 'root'
})
export class BoletaVentaService {
  private baseUrl = 'http://localhost:8082/ventas';

  constructor(private http: HttpClient) { }

  // Obtener todas las boletas
  getAll(): Observable<BoletaVenta[]> {
    return this.http.get<BoletaVenta[]>(this.baseUrl);
  }

  // Obtener boletas por usuario
  getByUsuario(idUsuario: number): Observable<BoletaVenta[]> {
    return this.http.get<BoletaVenta[]>(`${this.baseUrl}/usuario/${idUsuario}`);
  }

  // Obtener una boleta por ID
  getById(id: number): Observable<BoletaVenta> {
    return this.http.get<BoletaVenta>(`${this.baseUrl}/${id}`);
  }
}

