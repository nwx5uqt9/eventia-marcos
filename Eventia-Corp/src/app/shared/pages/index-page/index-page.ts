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
    // Verificar si ya está logueado, redirigir a eventos
    if (this.authService.isLoggedIn()) {
      window.location.href = '/#/client/events';
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
}
