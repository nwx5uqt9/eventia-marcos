import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Boleto } from 'src/app/boleto';

@Injectable({
  providedIn: 'root'
})
export class BoletoService {
  private api: string = 'http://localhost:8082/boletos';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Boleto[]> {
    return this.http.get<Boleto[]>(this.api);
  }

  getById(id: number): Observable<Boleto> {
    return this.http.get<Boleto>(`${this.api}/${id}`);
  }

  create(boleto: Boleto): Observable<Boleto> {
    return this.http.post<Boleto>(this.api, boleto);
  }

  update(id: number, boleto: Boleto): Observable<Boleto> {
    return this.http.put<Boleto>(`${this.api}/${id}`, boleto);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.api}/${id}`);
  }
}

