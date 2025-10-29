import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Bventas } from 'src/app/Bventas';

@Injectable({
  providedIn: 'root'
})
export class BVentasService {

  private baseUrl: string = 'http://localhost:8082/ventas';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Bventas[]> {
    return this.http.get<Bventas[]>(this.baseUrl);
  }

  getById(id: number): Observable<Bventas> {
    return this.http.get<Bventas>(`${this.baseUrl}/${id}`);
  }

  create(venta: Bventas): Observable<Bventas> {
    return this.http.post<Bventas>(this.baseUrl, venta);
  }

  update(id: number, venta: Bventas): Observable<Bventas> {
    return this.http.put<Bventas>(`${this.baseUrl}/${id}`, venta);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }

  // Métodos legacy para compatibilidad
  getUsuarioLista(): Observable<Bventas[]> {
    return this.getAll();
  }

  deleteVentasById(id: number): Observable<any> {
    return this.delete(id);
  }
}
