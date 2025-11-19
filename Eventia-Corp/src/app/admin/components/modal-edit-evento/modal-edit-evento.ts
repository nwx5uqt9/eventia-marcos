import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Evento } from 'src/app/evento';
import { TipoEvento } from 'src/app/tipoEvento';
import { EstadoEvento } from 'src/app/estadoEvento';
import { Organizador } from 'src/app/organizador';

@Component({
  selector: 'app-modal-edit-evento',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './modal-edit-evento.html',
  styleUrl: './modal-edit-evento.css',
})
export class ModalEditEvento implements OnInit {
  @Input() evento: Evento | null = null;
  @Input() tiposEvento: TipoEvento[] = [];
  @Input() estadosEvento: EstadoEvento[] = [];
  @Input() organizadores: Organizador[] = [];

  @Output() onClose = new EventEmitter<void>();
  @Output() onSave = new EventEmitter<Evento>();

  eventoEditado: Evento | null = null;

  ngOnInit(): void {
    if (this.evento) {
      // Crear una copia del evento para editar
      this.eventoEditado = { ...this.evento };
    }
  }

  cerrar(): void {
    this.onClose.emit();
  }

  guardar(): void {
    if (this.eventoEditado) {
      this.onSave.emit(this.eventoEditado);
    }
  }
}

