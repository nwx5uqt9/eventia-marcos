import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Organizador } from 'src/app/organizador';

@Injectable({
  providedIn: 'root'
})
export class OrganizadorService {
  private api: string = 'http://localhost:8082/organizadores';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Organizador[]> {
    return this.http.get<Organizador[]>(this.api);
  }

  getById(id: number): Observable<Organizador> {
    return this.http.get<Organizador>(`${this.api}/${id}`);
  }

  create(organizador: Organizador): Observable<Organizador> {
    return this.http.post<Organizador>(this.api, organizador);
  }

  update(id: number, organizador: Organizador): Observable<Organizador> {
    return this.http.put<Organizador>(`${this.api}/${id}`, organizador);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.api}/${id}`);
  }
}

