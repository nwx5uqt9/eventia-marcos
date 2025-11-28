import { ChangeDetectionStrategy, Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ModalStateService } from 'src/app/shared/services/modalState';
import { CommonModule } from '@angular/common';
import { ModalAddEvent } from 'src/app/shared/components/modal-add/modal-add';
import { ModalEditEvento } from '../../components/modal-edit-evento/modal-edit-evento';
import { AdminEventCard } from '../../components/admin-event-card/admin-event-card';
import { EventoService } from 'src/app/shared/services/evento.service';
import { TipoEventoService } from 'src/app/shared/services/tipo-evento.service';
import { EstadoEventoService } from 'src/app/shared/services/estado-evento.service';
import { OrganizadorService } from 'src/app/shared/services/organizador.service';
import { Evento } from 'src/app/evento';
import { TipoEvento } from 'src/app/tipoEvento';
import { EstadoEvento } from 'src/app/estadoEvento';
import { Organizador } from 'src/app/organizador';

@Component({
  selector: 'app-events-page',
  standalone: true,
  imports: [FormsModule, ModalAddEvent, ModalEditEvento, CommonModule, AdminEventCard],
  templateUrl: './events-page.html',
  styleUrl: './events-page.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class EventsPage implements OnInit {
  eventos = signal<Evento[]>([]);
  tiposEvento = signal<TipoEvento[]>([]);
  estadosEvento = signal<EstadoEvento[]>([]);
  organizadores = signal<Organizador[]>([]);

  eventoSeleccionado: Evento | null = null;
  mostrarModalEditar = signal<boolean>(false);

  typeData = signal('event');
  codigo: string = ''; // Variable para el filtro de búsqueda

  constructor(
    private modalState: ModalStateService,
    private eventoService: EventoService,
    private tipoEventoService: TipoEventoService,
    private estadoEventoService: EstadoEventoService,
    private organizadorService: OrganizadorService
  ) {}

  ngOnInit(): void {
    this.cargarEventos();
    this.cargarCatalogos();
  }

  cargarEventos() {
    this.eventoService.getAll().subscribe({
      next: (data) => {
        this.eventos.set(data);
        localStorage.setItem('eventos', JSON.stringify(data));
        console.log('Eventos cargados desde el servidor');
      },
      error: (err) => {
        console.error('Error al cargar eventos desde el servidor:', err);
        const eventosGuardados = localStorage.getItem('eventos');
        if (eventosGuardados) {
          this.eventos.set(JSON.parse(eventosGuardados));
          console.log('Eventos cargados desde LocalStorage por error del servidor');
        } else {
          console.warn('No hay eventos guardados en LocalStorage.');
        }
      },
    });
  }

  cargarCatalogos() {
    this.tipoEventoService.getAll().subscribe({
      next: (data) => this.tiposEvento.set(data),
      error: (err) => console.error('Error al cargar tipos de evento:', err)
    });

    this.estadoEventoService.getAll().subscribe({
      next: (data) => this.estadosEvento.set(data),
      error: (err) => console.error('Error al cargar estados de evento:', err)
    });

    this.organizadorService.getAll().subscribe({
      next: (data) => this.organizadores.set(data),
      error: (err) => console.error('Error al cargar organizadores:', err)
    });
  }

  openModal() {
    this.modalState.open(this.typeData());
  }

  editarEvento(id: number) {
    const evento = this.eventos().find(e => e.id === id);
    if (evento) {
      this.eventoSeleccionado = evento;
      this.mostrarModalEditar.set(true);
    }
  }

  cerrarModalEditar() {
    this.mostrarModalEditar.set(false);
    this.eventoSeleccionado = null;
  }

  guardarEvento(evento: Evento) {
    if (evento.id) {
      this.eventoService.update(evento.id, evento).subscribe({
        next: (data) => {
          console.log('Evento actualizado:', data);
          this.cargarEventos();
          this.cerrarModalEditar();
          alert('Evento actualizado exitosamente');
        },
        error: (err) => {
          console.error('Error al actualizar evento:', err);
          alert('Error al actualizar el evento');
        }
      });
    }
  }

  eliminarEvento(id: number) {
    const evento = this.eventos().find(e => e.id === id);
    if (evento && confirm(`¿Está seguro de eliminar el evento "${evento.nombre}"?`)) {
      this.eventoService.delete(id).subscribe({
        next: () => {
          console.log('Evento eliminado:', id);
          this.cargarEventos();
          alert('Evento eliminado exitosamente');
        },
        error: (err) => {
          console.error('Error al eliminar evento:', err);
          alert('Error al eliminar el evento');
        }
      });
    }
  }
}


