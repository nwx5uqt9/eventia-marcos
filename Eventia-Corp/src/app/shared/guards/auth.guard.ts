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

      // Solo verificar por ID de rol
      // Roles: 1 = ADMIN, 2 = CLIENTE, 3 = ORGANIZADOR
      if (requiredRoles.includes(userRoleId)) {
        console.log('acceso permitido', userRoleId);
        return true;
      } else {
        console.log('acceso denegado ', userRoleId);
        // Redirigir según el rol del usuario
        this.redirectByRole(userRoleId);
        return false;
      }
    }

    // Si no hay roles requeridos, permitir acceso
    return true;
  }

  private redirectByRole(rolId: number): void {
    // Solo ADMIN (id: 2) va a /admin/users
    // Todos los demás (id: 1, 3, etc.) van a /client/events
    if (rolId === 2) {
      this.router.navigate(['/admin/users']);
    } else {
      this.router.navigate(['/client/events']);
    }
  }
}

