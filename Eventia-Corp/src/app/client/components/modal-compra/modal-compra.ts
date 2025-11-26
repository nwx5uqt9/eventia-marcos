import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Evento } from 'src/app/evento';

@Component({
  selector: 'app-modal-compra',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './modal-compra.html',
  styleUrl: './modal-compra.css',
})
export class ModalCompra {
  @Input() evento: Evento | null = null;
  @Output() onClose = new EventEmitter<void>();
  @Output() onConfirm = new EventEmitter<{evento: Evento, cantidad: number, metodoPago: string}>();

  pasoActual: number = 1;
  cantidad: number = 1;
  precioUnitario: number = 50.00;
  metodoPagoSeleccionado: string = '';
  codigoReserva: string = '';

  pagoForm: FormGroup;
  procesandoPago: boolean = false;

  metodosPago = [
    {
      valor: 'tarjeta',
      nombre: 'Tarjeta de Crédito/Débito',
      icono: 'bi-credit-card',
      descripcion: 'Visa, Mastercard, American Express'
    },
    {
      valor: 'yape',
      nombre: 'Yape',
      icono: 'bi-phone',
      descripcion: 'Pago móvil instantáneo'
    },
    {
      valor: 'plin',
      nombre: 'Plin',
      icono: 'bi-phone-fill',
      descripcion: 'Pago móvil instantáneo'
    },
    {
      valor: 'efectivo',
      nombre: 'Efectivo',
      icono: 'bi-cash',
      descripcion: 'Pago en punto de venta'
    }
  ];

  constructor(private fb: FormBuilder) {
    this.pagoForm = this.fb.group({
      numeroTarjeta: ['', [Validators.required, Validators.pattern(/^\d{16}$/)]],
      nombreTitular: ['', [Validators.required, Validators.minLength(3)]],
      fechaExpiracion: ['', [Validators.required, Validators.pattern(/^(0[1-9]|1[0-2])\/\d{2}$/)]],
      cvv: ['', [Validators.required, Validators.pattern(/^\d{3,4}$/)]],
      email: ['', [Validators.required, Validators.email]],
      telefono: ['', [Validators.pattern(/^\d{9}$/)]]
    });

    // Generar código de reserva
    this.codigoReserva = 'RES-' + Date.now().toString().slice(-6);
  }

  getNombreMetodoPago(): string {
    const metodo = this.metodosPago.find(m => m.valor === this.metodoPagoSeleccionado);
    return metodo ? metodo.nombre : '';
  }

  get total(): number {
    return this.cantidad * this.precioUnitario;
  }

  get subtotal(): number {
    return this.cantidad * this.precioUnitario;
  }

  get comision(): number {
    return this.subtotal * 0.05; // 5% de comisión
  }

  cerrar(): void {
    this.resetear();
    this.onClose.emit();
  }

  resetear(): void {
    this.pasoActual = 1;
    this.cantidad = 1;
    this.metodoPagoSeleccionado = '';
    this.pagoForm.reset();
    this.procesandoPago = false;
  }

  seleccionarMetodoPago(metodo: string): void {
    this.metodoPagoSeleccionado = metodo;
  }

  siguientePaso(): void {
    if (this.pasoActual === 1 && this.cantidad > 0) {
      this.pasoActual = 2;
    } else if (this.pasoActual === 2 && this.metodoPagoSeleccionado) {
      this.pasoActual = 3;
    }
  }

  pasoAnterior(): void {
    if (this.pasoActual > 1) {
      this.pasoActual--;
    }
  }

  confirmarCompra(): void {
    if (this.evento && this.metodoPagoSeleccionado) {
      // Validar formulario solo si es tarjeta
      if (this.metodoPagoSeleccionado === 'tarjeta' && !this.pagoForm.valid) {
        this.marcarErrores();
        return;
      }

      this.procesandoPago = true;

      // Simular procesamiento
      setTimeout(() => {
        this.onConfirm.emit({
          evento: this.evento!,
          cantidad: this.cantidad,
          metodoPago: this.metodoPagoSeleccionado
        });
        this.resetear();
      }, 1500);
    }
  }

  marcarErrores(): void {
    Object.keys(this.pagoForm.controls).forEach(key => {
      this.pagoForm.get(key)?.markAsTouched();
    });
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

  formatearTarjeta(event: any): void {
    let value = event.target.value.replace(/\s/g, '');
    if (value.length > 16) {
      value = value.slice(0, 16);
    }
    event.target.value = value.match(/.{1,4}/g)?.join(' ') || value;
    this.pagoForm.patchValue({ numeroTarjeta: value });
  }

  formatearFecha(event: any): void {
    let value = event.target.value.replace(/\D/g, '');
    if (value.length >= 2) {
      value = value.slice(0, 2) + '/' + value.slice(2, 4);
    }
    event.target.value = value;
    this.pagoForm.patchValue({ fechaExpiracion: value });
  }
}

