import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NavbarIndex } from '../../components/navbar-index/navbar-index';
import { schemaCard } from '../../interfaces/schemaCard';
import dataCard from '../../../shared/data/dataCard.json';
import { Card } from "../../components/card/card";
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-index-page',
  standalone: true,
  imports: [RouterLink, NavbarIndex, Card, CommonModule, FormsModule],
  templateUrl: './index-page.html',
  styleUrl: './index-page.css'
})
export default class IndexPage implements OnInit {
  dataCard: schemaCard[] = dataCard;

  // Propiedades del formulario de login
  usuario: string = '';
  password: string = '';
  mensaje: string = '';
  mensajeError: string = '';
  cargando: boolean = false;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    // Verificar si ya está logueado
    if (this.authService.isLoggedIn()) {
      const user = this.authService.getCurrentUser();
      if (user) {
        this.redirectByRole(user.rolUsuario?.id);
      }
    }
  }

  login(): void {
    if (!this.usuario || !this.password) {
      this.mensajeError = 'Por favor ingresa usuario y contraseña';
      return;
    }

    this.cargando = true;
    this.mensajeError = '';
    this.mensaje = '';

    this.authService.login(this.usuario, this.password).subscribe({
      next: () => {
        this.mensaje = 'Inicio de sesión exitoso';
        // La redirección se hace automáticamente en el servicio
      },
      error: (err) => {
        console.error('Error al iniciar sesión:', err);
        this.mensajeError = 'Usuario o contraseña incorrectos';
        this.cargando = false;
      },
      complete: () => {
        this.cargando = false;
      }
    });
  }

  private redirectByRole(rolId: number | null | undefined): void {
    switch(rolId) {
      case 1: // Administrador
        window.location.href = '/#/admin';
        break;
      case 2: // Organizador
        window.location.href = '/#/control';
        break;
      case 3: // Cliente
        window.location.href = '/#/client';
        break;
      default:
        window.location.href = '/#/index';
    }
  }
}
