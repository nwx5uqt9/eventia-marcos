import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const currentUser = this.authService.getCurrentUser();

    if (currentUser) {
      // Verificar el rol requerido si existe
      const requiredRole = route.data['role'];

      if (requiredRole) {
        const userRoleId = currentUser.rolUsuario?.id;

        if (requiredRole.includes(userRoleId)) {
          return true;
        } else {
          // Redirigir según el rol del usuario
          this.redirectByRole(userRoleId);
          return false;
        }
      }

      return true;
    }

    // No está logueado, redirigir al login
    this.router.navigate(['/index']);
    return false;
  }

  private redirectByRole(rolId: number | null | undefined): void {
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
}

