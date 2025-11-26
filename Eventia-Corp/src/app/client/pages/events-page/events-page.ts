import { ChangeDetectionStrategy, Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Card } from 'src/app/shared/components/card/card';
import { ModalCompra } from '../../components/modal-compra/modal-compra';
import { EventoService } from 'src/app/shared/services/evento.service';
import { CompraService } from 'src/app/shared/services/compra.service';
import { AuthService } from 'src/app/shared/services/auth.service';
import { Evento } from 'src/app/evento';

@Component({
  selector: 'app-client-events-page',
  standalone: true,
  imports: [FormsModule, CommonModule, Card, ModalCompra],
  templateUrl: './events-page.html',
  styleUrl: './events-page.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class ClientEventsPage implements OnInit {
  eventos = signal<Evento[]>([]);
  searchTerm = '';
  selectedEstado = '';
  selectedUbicacion = '';

  // Modal de compra
  eventoSeleccionado: Evento | null = null;
  mostrarModal = signal<boolean>(false);

  constructor(
    private eventoService: EventoService,
    private compraService: CompraService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarEventos();

    // Verificar si hay una compra pendiente después del login
    const pendingPurchase = localStorage.getItem('pendingPurchase');
    if (pendingPurchase && this.authService.isLoggedIn()) {
      const eventoId = parseInt(pendingPurchase);
      localStorage.removeItem('pendingPurchase');

      // Buscar el evento y abrir el modal
      const evento = this.eventos().find(e => e.id === eventoId);
      if (evento) {
        setTimeout(() => {
          this.eventoSeleccionado = evento;
          this.mostrarModal.set(true);
        }, 500);
      }
    }
  }

  cargarEventos() {
    this.eventoService.getAll().subscribe({
      next: (data) => {
        // Filtrar solo eventos activos para usuarios
        const eventosActivos = data.filter((evento: Evento) =>
          evento.estadoEvento?.id === 1 || evento.estadoEvento?.nombre?.toLowerCase() === 'activo'
        );
        this.eventos.set(eventosActivos);
        console.log('Eventos activos cargados para cliente');
      },
      error: (err) => {
        console.error('Error al cargar eventos:', err);
        const eventosGuardados = localStorage.getItem('eventos');
        if (eventosGuardados) {
          const eventos = JSON.parse(eventosGuardados);
          const eventosActivos = eventos.filter((evento: Evento) =>
            evento.estadoEvento?.id === 1 || evento.estadoEvento?.nombre?.toLowerCase() === 'activo'
          );
          this.eventos.set(eventosActivos);
        }
      },
    });
  }

  buscarEventos() {
    // Implementar logica de busqueda si es necesario
    console.log('Buscando eventos...');
  }

  abrirModalCompra(evento: Evento): void {
    // Verificar si el usuario está autenticado
    if (!this.authService.isLoggedIn()) {
      // Guardar el evento que quiere comprar
      localStorage.setItem('pendingPurchase', evento.id!.toString());

      // Guardar la URL actual para regresar después del login
      this.authService.setRedirectUrl('/client/events');

      // Mostrar mensaje y redirigir al login
      alert('Debes iniciar sesión para realizar una compra');
      this.router.navigate(['/index']);
      return;
    }

    // Si está autenticado, abrir el modal
    this.eventoSeleccionado = evento;
    this.mostrarModal.set(true);
  }

  cerrarModal(): void {
    this.mostrarModal.set(false);
    this.eventoSeleccionado = null;
  }

  confirmarCompra(data: {evento: Evento, cantidad: number, metodoPago: string}): void {
    const usuario = this.authService.getCurrentUser();

    if (!usuario || !usuario.id) {
      alert('Error: No hay usuario autenticado');
      return;
    }

    const compraRequest = {
      idUsuario: usuario.id,
      idEvento: data.evento.id!,
      cantidad: data.cantidad,
      metodoPago: data.metodoPago,
      total: data.cantidad * 50.00
    };

    this.compraService.registrarCompra(compraRequest).subscribe({
      next: (response: any) => {
        if (response.success) {
          alert(`Compra exitosa\n\nEvento: ${data.evento.nombre}\nCantidad: ${data.cantidad} boleto(s)\nMetodo de Pago: ${data.metodoPago}\nTotal: S/ ${response.total?.toFixed(2)}\nCodigo de Pago: ${response.codigoPago}\n\nGracias por tu compra`);
          this.cerrarModal();
        } else {
          alert('Error: ' + response.message);
        }
      },
      error: (err: any) => {
        console.error('Error al procesar la compra:', err);
        alert('Error al procesar la compra. Por favor, intente nuevamente.');
      }
    });
  }
}


