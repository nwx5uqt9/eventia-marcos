import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Evento } from 'src/app/evento';

@Injectable({
  providedIn: 'root'
})
export class EventoService {
  private api: string = 'http://localhost:8082/eventos';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Evento[]> {
    return this.http.get<Evento[]>(this.api);
  }

  getById(id: number): Observable<Evento> {
    return this.http.get<Evento>(`${this.api}/${id}`);
  }

  create(evento: Evento): Observable<Evento> {
    return this.http.post<Evento>(this.api, evento);
  }

  update(id: number, evento: Evento): Observable<Evento> {
    return this.http.put<Evento>(`${this.api}/${id}`, evento);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.api}/${id}`);
  }
}

