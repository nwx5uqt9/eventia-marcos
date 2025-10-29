import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { EstadoEvento } from 'src/app/estadoEvento';

@Injectable({
  providedIn: 'root'
})
export class EstadoEventoService {

  private baseUrl: string = 'http://localhost:8082/estado';

  constructor(private http: HttpClient) {}

  getAll(): Observable<EstadoEvento[]> {
    return this.http.get<EstadoEvento[]>(this.baseUrl);
  }

  getById(id: number): Observable<EstadoEvento> {
    return this.http.get<EstadoEvento>(`${this.baseUrl}/${id}`);
  }

  create(estadoEvento: EstadoEvento): Observable<EstadoEvento> {
    return this.http.post<EstadoEvento>(`${this.baseUrl}/create`, estadoEvento);
  }

  update(id: number, estadoEvento: EstadoEvento): Observable<EstadoEvento> {
    return this.http.put<EstadoEvento>(`${this.baseUrl}/${id}`, estadoEvento);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }

  // Métodos legacy para compatibilidad
  getEstadoLista(): Observable<EstadoEvento[]> {
    return this.getAll();
  }

  createEstado(estadoEvento: EstadoEvento): Observable<EstadoEvento> {
    return this.create(estadoEvento);
  }

  deleteEstadoById(id: number): Observable<any> {
    return this.delete(id);
  }
}
