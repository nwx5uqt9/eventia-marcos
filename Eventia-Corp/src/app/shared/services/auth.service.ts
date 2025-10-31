import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { Usuario } from 'src/app/usuario';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://localhost:8082/usuarios';
  private currentUserSubject = new BehaviorSubject<Usuario | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    // Cargar usuario desde localStorage si existe
    const storedUser = localStorage.getItem('currentUser');
    if (storedUser) {
      this.currentUserSubject.next(JSON.parse(storedUser));
    }
  }

  login(usuario: string, password: string): Observable<any> {
    // Buscar usuario por nombreusuario o email
    return this.http.get<Usuario[]>(`${this.baseUrl}`).pipe(
      tap((usuarios: Usuario[]) => {
        const user = usuarios.find(u =>
          (u.nombreusuario === usuario || u.email === usuario) &&
          u.password === password
        );

        if (user) {
          // Guardar usuario en localStorage
          localStorage.setItem('currentUser', JSON.stringify(user));
          this.currentUserSubject.next(user);

          // Redirigir según el rol
          this.redirectByRole(user);
        } else {
          throw new Error('Credenciales inválidas');
        }
      })
    );
  }

  private redirectByRole(user: Usuario): void {
    const rolId = user.rolUsuario?.id;

    switch(rolId) {
      case 1: // Administrador
        this.router.navigate(['/admin']);
        break;
      case 2: // Organizador
        this.router.navigate(['/control']);
        break;
      case 3: // Cliente
        this.router.navigate(['/client']);
        break;
      default:
        this.router.navigate(['/index']);
    }
  }

  logout(): void {
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
    this.router.navigate(['/index']);
  }

  getCurrentUser(): Usuario | null {
    return this.currentUserSubject.value;
  }

  isLoggedIn(): boolean {
    return this.currentUserSubject.value !== null;
  }

  isAdmin(): boolean {
    const user = this.currentUserSubject.value;
    return user?.rolUsuario?.id === 1;
  }

  isOrganizador(): boolean {
    const user = this.currentUserSubject.value;
    return user?.rolUsuario?.id === 2;
  }

  isCliente(): boolean {
    const user = this.currentUserSubject.value;
    return user?.rolUsuario?.id === 3;
  }
}

