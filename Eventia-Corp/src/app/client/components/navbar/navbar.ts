import { ChangeDetectionStrategy, Component, OnInit, signal, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from 'src/app/shared/services/auth.service';

@Component({
  selector: 'app-navbar-client',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class NavbarClient implements OnInit {
  currentUser = signal<any>(null);
  userName = signal<string>('');

  constructor(
    private authService: AuthService
  ) {
    // Use effect to update signals when currentUser changes
    effect(() => {
      const user = this.currentUser();
      if (user) {
        this.userName.set(user.nombre || user.nombreusuario || 'Usuario');
      } else {
        this.userName.set('');
      }
    });
  }

  ngOnInit(): void {
    // Set initial value
    const user = this.authService.getCurrentUser();
    this.currentUser.set(user);

    // Subscribe to changes
    this.authService.currentUser$.subscribe(user => {
      this.currentUser.set(user);
    });
  }

  logout(): void {
    this.authService.logout();
  }

  goToProfile(): void {
    // Implementar navegación a perfil
    console.log('Ir a perfil');
  }
}

