import { ChangeDetectionStrategy, Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-admin-event-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-event-card.html',
  styleUrl: './admin-event-card.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AdminEventCard {
  @Input() title: string = 'Título del Evento';
  @Input() description: string = 'Descripción del evento';
  @Input() imageUrl: string = 'img/micro-eventos.png';
  @Input() eventoId: number | null = null;

  @Output() onEdit = new EventEmitter<number>();
  @Output() onDelete = new EventEmitter<number>();

  editarEvento(): void {
    if (this.eventoId) {
      this.onEdit.emit(this.eventoId);
    }
  }

  eliminarEvento(): void {
    if (this.eventoId) {
      this.onDelete.emit(this.eventoId);
    }
  }
}

