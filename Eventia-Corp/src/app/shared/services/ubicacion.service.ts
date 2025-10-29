import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Ubicacion } from 'src/app/ubicacion';

@Injectable({
  providedIn: 'root'
})
export class UbicacionService {
  private api: string = 'http://localhost:8082/ubicaciones';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Ubicacion[]> {
    return this.http.get<Ubicacion[]>(this.api);
  }

  getById(id: number): Observable<Ubicacion> {
    return this.http.get<Ubicacion>(`${this.api}/${id}`);
  }

  create(ubicacion: Ubicacion): Observable<Ubicacion> {
    return this.http.post<Ubicacion>(this.api, ubicacion);
  }

  update(id: number, ubicacion: Ubicacion): Observable<Ubicacion> {
    return this.http.put<Ubicacion>(`${this.api}/${id}`, ubicacion);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.api}/${id}`);
  }
}
