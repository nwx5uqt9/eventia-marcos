import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Evento } from 'src/app/evento';

@Component({
  selector: 'app-modal-compra',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './modal-compra.html',
  styleUrl: './modal-compra.css',
})
export class ModalCompra {
  @Input() evento: Evento | null = null;
  @Output() onClose = new EventEmitter<void>();
  @Output() onConfirm = new EventEmitter<{evento: Evento, cantidad: number, metodoPago: string}>();

  cantidad: number = 1;
  precioUnitario: number = 50.00;
  metodoPago: string = '';

  metodosPago = [
    { valor: 'yape', nombre: 'Yape' },
    { valor: 'visa', nombre: 'Visa' },
    { valor: 'mastercard', nombre: 'Mastercard' },
    { valor: 'american-express', nombre: 'American Express' },
    { valor: 'plin', nombre: 'Plin' },
    { valor: 'efectivo', nombre: 'Efectivo' }
  ];

  get total(): number {
    return this.cantidad * this.precioUnitario;
  }

  cerrar(): void {
    this.cantidad = 1;
    this.metodoPago = '';
    this.onClose.emit();
  }

  confirmarCompra(): void {
    if (this.evento && this.metodoPago) {
      this.onConfirm.emit({
        evento: this.evento,
        cantidad: this.cantidad,
        metodoPago: this.metodoPago
      });
      this.cantidad = 1;
      this.metodoPago = '';
    }
  }

  incrementar(): void {
    if (this.cantidad < 10) {
      this.cantidad++;
    }
  }

  decrementar(): void {
    if (this.cantidad > 1) {
      this.cantidad--;
    }
  }
}

