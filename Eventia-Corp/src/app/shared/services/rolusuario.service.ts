import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RolUsuario } from 'src/app/rolUsuario';

@Injectable({
  providedIn: 'root'
})
export class RolusuarioService {

  private baseUrl: string = 'http://localhost:8082/roles';

  constructor(private http: HttpClient) {}

  getAll(): Observable<RolUsuario[]> {
    return this.http.get<RolUsuario[]>(this.baseUrl);
  }

  getById(id: number): Observable<RolUsuario> {
    return this.http.get<RolUsuario>(`${this.baseUrl}/${id}`);
  }

  create(rolUsuario: RolUsuario): Observable<RolUsuario> {
    return this.http.post<RolUsuario>(`${this.baseUrl}/create`, rolUsuario);
  }

  update(id: number, rolUsuario: RolUsuario): Observable<RolUsuario> {
    return this.http.put<RolUsuario>(`${this.baseUrl}/${id}`, rolUsuario);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }

  // Métodos legacy para compatibilidad
  getrolesLista(): Observable<RolUsuario[]> {
    return this.getAll();
  }

  createRol(rolUsuario: RolUsuario): Observable<RolUsuario> {
    return this.create(rolUsuario);
  }

  deleteRolById(id: number): Observable<any> {
    return this.delete(id);
  }
}
