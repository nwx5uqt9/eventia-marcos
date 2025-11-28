import { ChangeDetectionStrategy, Component, signal, OnInit } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { EventoService } from 'src/app/shared/services/evento.service';
import { TipoEventoService } from 'src/app/shared/services/tipo-evento.service';
import { EstadoEventoService } from 'src/app/shared/services/estado-evento.service';
import { OrganizadorService } from 'src/app/shared/services/organizador.service';
import { ModalStateService } from 'src/app/shared/services/modalState';

@Component({
  selector: 'app-form-add-event',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './form-add-event.html',
  styleUrl: './form-add-event.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FormAddEvent implements OnInit {
  eventoForm!: FormGroup;
  tiposEvento = signal<any[]>([]);
  estadosEvento = signal<any[]>([]);
  organizadores = signal<any[]>([]);
  loading = signal(false);
  error = signal<string | null>(null);

  constructor(
    private fb: FormBuilder,
    private eventoService: EventoService,
    private tipoEventoService: TipoEventoService,
    private estadoEventoService: EstadoEventoService,
    private organizadorService: OrganizadorService,
    private modalState: ModalStateService
  ) {
    this.eventoForm = this.fb.group({
      nombre: ['', Validators.required],
      descripcion: [''],
      fechaHora: ['', Validators.required],
      tipoEvento: [null, Validators.required],
      organizador: [null, Validators.required],
      estadoEvento: [null, Validators.required],
      ubicacion: ['', Validators.required],
      precio: ['', [Validators.required, Validators.min(0)]]
    });
  }

  ngOnInit(): void {
    this.loadCatalogos();
  }

  loadCatalogos() {
    this.tipoEventoService.getAll().subscribe({
      next: (data) => this.tiposEvento.set(data),
      error: (err) => console.error('Error loading tipos evento:', err)
    });

    this.estadoEventoService.getAll().subscribe({
      next: (data) => this.estadosEvento.set(data),
      error: (err) => console.error('Error loading estados evento:', err)
    });

    this.organizadorService.getAll().subscribe({
      next: (data) => this.organizadores.set(data),
      error: (err) => console.error('Error loading organizadores:', err)
    });
  }

  guardar() {
    if (this.eventoForm.valid) {
      this.loading.set(true);
      this.error.set(null);

      const formValue = this.eventoForm.value;
      const evento: any = {
        id: null,
        nombre: formValue.nombre,
        descripcion: formValue.descripcion,
        fechaHora: formValue.fechaHora,
        tipoEvento: { id: parseInt(formValue.tipoEvento) },
        organizador: { id: parseInt(formValue.organizador) },
        estadoEvento: { id: parseInt(formValue.estadoEvento) },
        ubicacion: formValue.ubicacion || null,
        precio: parseFloat(formValue.precio) || 0
      };

      this.eventoService.create(evento).subscribe({
        next: (response) => {
          console.log('Evento creado exitosamente:', response);
          this.loading.set(false);
          this.eventoForm.reset();
          this.modalState.close();
          alert('Evento creado exitosamente');
          window.location.reload();
        },
        error: (err) => {
          console.error('Error al crear evento:', err);
          this.error.set('Error al crear el evento. Por favor intente nuevamente.');
          this.loading.set(false);
        }
      });
    } else {
      this.error.set('Por favor complete todos los campos requeridos');
    }
  }

  closeModal() {
    this.modalState.close();
  }
}
