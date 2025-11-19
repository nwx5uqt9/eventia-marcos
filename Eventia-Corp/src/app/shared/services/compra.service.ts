import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CompraRequest {
  idUsuario: number;
  idEvento: number;
  cantidad: number;
  metodoPago: string;
  total: number;
}

export interface CompraResponse {
  success: boolean;
  message: string;
  codigoPago?: string;
  boletaId?: number;
  total?: number;
  cantidad?: number;
  metodoPago?: string;
}

@Injectable({
  providedIn: 'root'
})
export class CompraService {
  private baseUrl = 'http://localhost:8082/api/compras';

  constructor(private http: HttpClient) { }

  registrarCompra(compra: CompraRequest): Observable<CompraResponse> {
    return this.http.post<CompraResponse>(`${this.baseUrl}/registrar`, compra);
  }
}

