import { Component, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NavbarIndex } from '../../components/navbar-index/navbar-index';
import { Card } from "../../components/card/card";
import { AuthService } from '../../services/auth.service';
import { EventoService } from '../../services/evento.service';
import { Evento } from 'src/app/evento';

@Component({
  selector: 'app-index-page',
  standalone: true,
  imports: [RouterLink, NavbarIndex, Card, CommonModule, FormsModule],
  templateUrl: './index-page.html',
  styleUrl: './index-page.css'
})
export default class IndexPage implements OnInit {
  eventos = signal<Evento[]>([]);

  // Propiedades del formulario de login
  usuario: string = '';
  password: string = '';
  mensaje: string = '';
  mensajeError: string = '';
  cargando: boolean = false;

  constructor(
    private authService: AuthService,
    private eventoService: EventoService
  ) {}

  ngOnInit(): void {
    // Cargar eventos desde la base de datos
    this.cargarEventos();

    // Verificar si ya está logueado, redirigir a eventos
    if (this.authService.isLoggedIn()) {
      window.location.href = '/#/client/events';
    }
  }

  cargarEventos(): void {
    console.log('Cargando eventos para index desde el backend...');
    this.eventoService.getAll().subscribe({
      next: (data) => {
        console.log('Eventos recibidos en index:', data.length);

        // Filtrar solo eventos activos (igual que en client/events)
        const eventosActivos = data.filter((evento: Evento) =>
          evento.estadoEvento?.id === 1 || evento.estadoEvento?.nombre?.toLowerCase() === 'activo'
        );

        console.log('Eventos activos en index:', eventosActivos.length);

        // Tomar solo los primeros 6 eventos para mostrar en el index
        const eventosLimitados = eventosActivos.slice(0, 6);

        this.eventos.set(eventosLimitados);
      },
      error: (err) => {
        console.error('Error al cargar eventos en index:', err);
        this.eventos.set([]);
      },
    });
  }

  getPrecio(evento: any): number | undefined {
    return evento?.precio;
  }

  getUbicacion(evento: any): string | undefined {
    return evento?.ubicacion;
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
