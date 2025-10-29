import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TipoEvento } from 'src/app/tipoEvento';

@Injectable({
  providedIn: 'root'
})
export class TipoEventoService {

  private baseUrl: string = 'http://localhost:8082/tipo';

  constructor(private http: HttpClient) {}

  getAll(): Observable<TipoEvento[]> {
    return this.http.get<TipoEvento[]>(this.baseUrl);
  }

  getById(id: number): Observable<TipoEvento> {
    return this.http.get<TipoEvento>(`${this.baseUrl}/${id}`);
  }

  create(tipoEvento: TipoEvento): Observable<TipoEvento> {
    return this.http.post<TipoEvento>(`${this.baseUrl}/create`, tipoEvento);
  }

  update(id: number, tipoEvento: TipoEvento): Observable<TipoEvento> {
    return this.http.put<TipoEvento>(`${this.baseUrl}/${id}`, tipoEvento);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }

  // Métodos legacy para compatibilidad
  getTipoLista(): Observable<TipoEvento[]> {
    return this.getAll();
  }

  createTipo(tipoEvento: TipoEvento): Observable<TipoEvento> {
    return this.create(tipoEvento);
  }

  deleteTipoById(id: number): Observable<any> {
    return this.delete(id);
  }
}
