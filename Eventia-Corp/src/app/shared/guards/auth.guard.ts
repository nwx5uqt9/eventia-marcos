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

    // Si no está autenticado, redirigir a login
    if (!currentUser) {
      console.log('No autenticado, redirigiendo a login');
      this.authService.setRedirectUrl(state.url);
      this.router.navigate(['/index']);
      return false;
    }

    // Verificar roles requeridos si existen
    const requiredRoles = route.data['roles'] as number[] | undefined;

    if (requiredRoles && requiredRoles.length > 0) {
      const userRoleId = currentUser.rolUsuario?.id;

      if (requiredRoles.includes(userRoleId)) {
        console.log('Acceso permitido - Rol válido:', userRoleId);
        return true;
      } else {
        console.log('Acceso denegado - Rol insuficiente:', userRoleId);
        // Redirigir según el rol del usuario
        this.redirectByRole(userRoleId);
        return false;
      }
    }

    // Si no hay roles requeridos, permitir acceso
    return true;
  }

  private redirectByRole(rolId: number | null | undefined): void {
    switch(rolId) {
      case 1: // Administrador
        this.router.navigate(['/admin/events']);
        break;
      case 2: // Organizador
      case 3: // Cliente
      default:
        this.router.navigate(['/client/events']);
        break;
    }
  }
}

