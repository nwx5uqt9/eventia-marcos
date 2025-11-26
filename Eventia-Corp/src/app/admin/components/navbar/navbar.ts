import { ChangeDetectionStrategy, Component } from '@angular/core';
import { SidebarCollapseService } from 'src/app/shared/services/sidebar-collapse';
import { AuthService } from '../../../shared/services/auth.service';

@Component({
  selector: 'app-navbar-admin',
  standalone: true,
  imports: [],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Navbar {
location: any;
  constructor(private sidebarService: SidebarCollapseService,
              private closeService: AuthService) { }

  onToggleClick(): void {
    this.sidebarService.toggleSidebar();
  }
  logout() {
    this.closeService.logout()
  }
}
