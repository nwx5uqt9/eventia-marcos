import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavbarClient } from '../../components/navbar/navbar';

@Component({
  selector: 'app-client-index-page',
  imports: [RouterOutlet, NavbarClient],
  templateUrl: './index-page.html',
  styleUrl: './index-page.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class ClientIndexPage { }
