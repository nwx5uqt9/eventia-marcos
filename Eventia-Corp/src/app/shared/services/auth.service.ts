import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, throwError } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';
import { Router } from '@angular/router';

// Interface para la respuesta del login
interface LoginResponse {
  success: boolean;
  message: string;
  usuario?: {
    id: number;
    nombre: string;
    apellidos: string;
    nombreusuario: string;
    email: string;
    dni: string;
    edad: number;
    telefono: string;
    sexo: string;
    direccion: string;
    rolUsuario: {
      id: number;
      rol: string;
      descripcion: string;
    };
  };
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://localhost:8082';
  private currentUserSubject = new BehaviorSubject<any | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();
  private redirectUrl: string | null = null;

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

  setRedirectUrl(url: string): void {
    this.redirectUrl = url;
    localStorage.setItem('redirectUrl', url);
  }

  getRedirectUrl(): string | null {
    return this.redirectUrl || localStorage.getItem('redirectUrl');
  }

  clearRedirectUrl(): void {
    this.redirectUrl = null;
    localStorage.removeItem('redirectUrl');
  }

  /**
   * Login usando el endpoint correcto con BCrypt
   * @param usuario - Nombre de usuario o email
   * @param password - Contraseña sin encriptar
   */
  login(usuario: string, password: string): Observable<LoginResponse> {
    const loginData = {
      username: usuario,
      password: password
    };

    return this.http.post<LoginResponse>(`${this.baseUrl}/auth/login`, loginData).pipe(
      tap((response: LoginResponse) => {
        if (response.success && response.usuario) {
          // Guardar usuario en localStorage
          localStorage.setItem('currentUser', JSON.stringify(response.usuario));
          localStorage.setItem('isLoggedIn', 'true');
          this.currentUserSubject.next(response.usuario);

          // Verificar si hay una URL de redirección guardada
          const redirectUrl = this.getRedirectUrl();
          if (redirectUrl) {
            this.clearRedirectUrl();
            this.router.navigateByUrl(redirectUrl);
          } else {
            // Redirigir según el rol
            this.redirectByRole(response.usuario);
          }
        }
      }),
      catchError(error => {
        console.error('Error en login:', error);
        return throwError(() => new Error(error.error?.message || 'Error al iniciar sesión'));
      })
    );
  }

  private redirectByRole(user: any): void {
    const rolId = user.rolUsuario?.id;

    // Redirigir según el rol
    // Solo ADMIN (id: 2) va a /admin/users
    // Todos los demás (id: 3 = CLIENTE, id: 4 = ORGANIZADOR, etc.) van a /client/events
    if (rolId === 2) {
      console.log('Redirigiendo a zona administrativa - Users');
      this.router.navigate(['/admin/users']);
    } else {
      console.log('Redirigiendo a zona cliente - Events');
      this.router.navigate(['/client/events']);
    }
  }

  logout(): void {
    localStorage.removeItem('currentUser');
    localStorage.removeItem('isLoggedIn');
    this.currentUserSubject.next(null);
    this.router.navigate(['/client/events']);
  }

  getCurrentUser(): any {
    return this.currentUserSubject.value;
  }

  isLoggedIn(): boolean {
    return this.currentUserSubject.value !== null && localStorage.getItem('isLoggedIn') === 'true';
  }

  isAdmin(): boolean {
    const user = this.currentUserSubject.value;
    return user?.rolUsuario?.id === 2;
  }

  isOrganizador(): boolean {
    const user = this.currentUserSubject.value;
    return user?.rolUsuario?.id === 4;
  }

  isCliente(): boolean {
    const user = this.currentUserSubject.value;
    return user?.rolUsuario?.id === 3;
  }

  /**
   * Verifica si el usuario tiene un rol específico por ID o nombre
   * Útil para roles dinámicos agregados desde utilities
   */
  hasRole(rolIdOrName: number | string): boolean {
    const user = this.currentUserSubject.value;
    if (!user || !user.rolUsuario) return false;

    if (typeof rolIdOrName === 'number') {
      return user.rolUsuario.id === rolIdOrName;
    } else {
      return user.rolUsuario.rol?.toLowerCase() === rolIdOrName.toLowerCase();
    }
  }

  /**
   * Obtiene el rol actual del usuario
   */
  getCurrentRole(): { id: number; rol: string; descripcion: string } | null {
    const user = this.currentUserSubject.value;
    return user?.rolUsuario || null;
  }
}

