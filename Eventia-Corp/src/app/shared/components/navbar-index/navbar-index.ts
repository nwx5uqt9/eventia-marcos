import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-navbar-index',
  standalone: true,
  imports: [],
  templateUrl: './navbar-index.html',
  styleUrl: './navbar-index.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class NavbarIndex { }
