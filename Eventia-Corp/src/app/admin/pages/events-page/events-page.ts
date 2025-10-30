import { ChangeDetectionStrategy, Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ModalStateService } from 'src/app/shared/services/modalState';
import { CommonModule } from '@angular/common';
import { ModalAddEvent } from 'src/app/shared/components/modal-add/modal-add';
import { schemaCard } from 'src/app/shared/interfaces/schemaCard';
import dataCard from '../../../shared/data/dataCard.json';
import { Card } from 'src/app/shared/components/card/card';
import { EventoService } from 'src/app/shared/services/evento.service';
import { Evento } from 'src/app/evento';

@Component({
  selector: 'app-events-page',
  imports: [FormsModule, ModalAddEvent, CommonModule, Card],
  templateUrl: './events-page.html',
  styleUrl: './events-page.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class EventsPage implements OnInit {
  eventos = signal<Evento[]>([]);
  empleados: any;

  /* Develop */
  typeData = signal('event');

  dataCard: schemaCard[] = dataCard;

  constructor(private modalState: ModalStateService, private eventoService: EventoService) {}

  ngOnInit(): void {
    this.modalState;
    this.cargarEventos();
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

  openModal() {
    this.modalState.open(this.typeData());
  }

}
