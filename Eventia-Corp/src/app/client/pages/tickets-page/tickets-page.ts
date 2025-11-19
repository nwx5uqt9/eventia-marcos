import { ChangeDetectionStrategy, Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { BoletaVentaService, BoletaVenta } from 'src/app/shared/services/boleta-venta.service';
import { AuthService } from 'src/app/shared/services/auth.service';

interface Boleto {
  id: number;
  eventoNombre: string;
  fecha: string;
  hora: string;
  ubicacion: string;
  cantidad: number;
  precio: number;
  codigo: string;
  estado: 'activo' | 'usado' | 'cancelado';
  metodoPago: string;
}

@Component({
  selector: 'app-client-tickets-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './tickets-page.html',
  styleUrl: './tickets-page.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class ClientTicketsPage implements OnInit {
  boletos = signal<Boleto[]>([]);
  cargando = signal<boolean>(true);

  constructor(
    private boletaVentaService: BoletaVentaService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.cargarBoletos();
  }

  cargarBoletos() {
    const usuario = this.authService.getCurrentUser();

    if (!usuario || !usuario.id) {
      console.error('No hay usuario autenticado');
      this.cargando.set(false);
      return;
    }

    // Obtener todas las boletas y filtrar por usuario
    this.boletaVentaService.getAll().subscribe({
      next: (boletas: BoletaVenta[]) => {
        // Filtrar solo las boletas del usuario actual
        const boletasUsuario = boletas.filter(b => b.usuario?.id === usuario.id);

        // Convertir a formato Boleto
        const boletosConvertidos: Boleto[] = boletasUsuario.map(b => ({
          id: b.id,
          eventoNombre: b.evento?.nombre || 'Evento sin nombre',
          fecha: this.formatearFecha(b.evento?.fechaHora),
          hora: this.formatearHora(b.evento?.fechaHora),
          ubicacion: b.ubicacion?.nombre || 'Ubicación no especificada',
          cantidad: b.cantidad || 1,
          precio: b.total || 0,
          codigo: b.codigoPago,
          estado: this.determinarEstado(b.evento?.fechaHora),
          metodoPago: b.metodoPago || 'No especificado'
        }));

        this.boletos.set(boletosConvertidos);
        this.cargando.set(false);
        console.log(`${boletosConvertidos.length} boletos cargados para el usuario`);
      },
      error: (err) => {
        console.error('Error al cargar boletos:', err);
        this.cargando.set(false);
        this.boletos.set([]);
      }
    });
  }

  formatearFecha(fechaHora: string): string {
    if (!fechaHora) return 'Fecha no disponible';
    const fecha = new Date(fechaHora);
    return fecha.toLocaleDateString('es-ES', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  }

  formatearHora(fechaHora: string): string {
    if (!fechaHora) return 'Hora no disponible';
    const fecha = new Date(fechaHora);
    return fecha.toLocaleTimeString('es-ES', {
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  determinarEstado(fechaHora: string): 'activo' | 'usado' | 'cancelado' {
    if (!fechaHora) return 'activo';
    const fechaEvento = new Date(fechaHora);
    const ahora = new Date();

    // Si el evento ya pasó, marcarlo como "usado"
    if (fechaEvento < ahora) {
      return 'usado';
    }
    return 'activo';
  }

  verDetalle(boleto: Boleto) {
    console.log('Ver detalle de boleto:', boleto);
    alert(`Detalle del Boleto\n\nCódigo: ${boleto.codigo}\nEvento: ${boleto.eventoNombre}\nFecha: ${boleto.fecha}\nHora: ${boleto.hora}\nCantidad: ${boleto.cantidad}\nTotal: S/ ${boleto.precio.toFixed(2)}\nMétodo de Pago: ${boleto.metodoPago}`);
  }

  descargarBoleto(boleto: Boleto) {
    console.log('Descargar boleto:', boleto);
    alert(`Descarga de boleto no implementada aún.\n\nCódigo: ${boleto.codigo}\nEvento: ${boleto.eventoNombre}`);
  }

  getEstadoBadgeClass(estado: string): string {
    switch (estado) {
      case 'activo':
        return 'bg-success';
      case 'usado':
        return 'bg-secondary';
      case 'cancelado':
        return 'bg-danger';
      default:
        return 'bg-secondary';
    }
  }

  getEstadoTexto(estado: string): string {
    switch (estado) {
      case 'activo':
        return 'Activo';
      case 'usado':
        return 'Usado';
      case 'cancelado':
        return 'Cancelado';
      default:
        return estado;
    }
  }
}


