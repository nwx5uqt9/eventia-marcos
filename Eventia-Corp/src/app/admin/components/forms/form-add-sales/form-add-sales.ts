import { ChangeDetectionStrategy, Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { BVentasService } from 'src/app/shared/services/b-ventas.service';
import { EventoService } from 'src/app/shared/services/evento.service';
import { UbicacionService } from 'src/app/shared/services/ubicacion.service';
import { UsuarioService } from 'src/app/shared/services/usuario.service';
import { ModalStateService } from 'src/app/shared/services/modalState';
import { EditDataService } from 'src/app/shared/services/edit-data.service';
import { Bventas } from 'src/app/Bventas';
import { Evento } from 'src/app/evento';
import { Ubicacion } from 'src/app/ubicacion';
import { Usuario } from 'src/app/usuario';

@Component({
  selector: 'app-form-add-sales',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './form-add-sales.html',
  styleUrl: './form-add-sales.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FormAddSales implements OnInit {
  ventaForm: FormGroup;
  guardando = false;
  isEditMode = false;
  ventaId: number | null = null;

  usuarios: Usuario[] = [];
  eventos: Evento[] = [];
  ubicaciones: Ubicacion[] = [];

  constructor(
    private fb: FormBuilder,
    private ventasService: BVentasService,
    private eventoService: EventoService,
    private ubicacionService: UbicacionService,
    private usuarioService: UsuarioService,
    private modalState: ModalStateService,
    private editDataService: EditDataService,
    private cdr: ChangeDetectorRef
  ) {
    this.ventaForm = this.fb.group({
      idUsuario: ['', Validators.required],
      idEvento: ['', Validators.required],
      idUbicacion: ['', Validators.required],
      codigoPago: ['', Validators.required],
      total: [0, [Validators.required, Validators.min(0)]]
    });
  }

  ngOnInit(): void {
    this.cargarDatos();
    this.checkEditMode();
  }

  checkEditMode(): void {
    const editData = this.editDataService.getEditData();
    if (editData && this.editDataService.isEditMode()) {
      this.isEditMode = true;
      this.ventaId = editData.id;

      this.ventaForm.patchValue({
        idUsuario: editData.usuario?.id || '',
        idEvento: editData.evento?.id || '',
        idUbicacion: editData.ubicacion?.id || '',
        codigoPago: editData.codigoPago || '',
        total: editData.total || 0
      });

      this.cdr.markForCheck();
    }
  }

  cargarDatos(): void {
    this.usuarioService.getAll().subscribe({
      next: (data) => this.usuarios = data,
      error: (err) => console.error('Error al cargar usuarios:', err)
    });

    this.eventoService.getAll().subscribe({
      next: (data) => this.eventos = data,
      error: (err) => console.error('Error al cargar eventos:', err)
    });

    this.ubicacionService.getAll().subscribe({
      next: (data) => this.ubicaciones = data,
      error: (err) => console.error('Error al cargar ubicaciones:', err)
    });
  }

  guardar(): void {
    if (this.ventaForm.valid) {
      this.guardando = true;

      const formValues = this.ventaForm.value;

      const venta = new Bventas(
        this.isEditMode ? this.ventaId : null,
        { id: formValues.idUsuario } as Usuario,
        { id: formValues.idEvento } as Evento,
        { id: formValues.idUbicacion } as Ubicacion,
        formValues.codigoPago,
        new Date().toISOString(),
        formValues.total
      );

      const operation = this.isEditMode
        ? this.ventasService.update(this.ventaId!, venta)
        : this.ventasService.create(venta);

      operation.subscribe({
        next: (response) => {
          console.log(`Venta ${this.isEditMode ? 'actualizada' : 'guardada'} exitosamente:`, response);
          alert(`Venta ${this.isEditMode ? 'actualizada' : 'registrada'} exitosamente`);
          this.ventaForm.reset();
          this.editDataService.clearEditData();
          this.modalState.close();
          window.location.reload();
        },
        error: (err) => {
          console.error('Error al guardar la venta:', err);
          alert('Error al guardar la venta: ' + (err.error?.message || err.message));
          this.guardando = false;
        },
        complete: () => {
          this.guardando = false;
        }
      });
    } else {
      alert('Por favor complete todos los campos requeridos');
    }
  }

  cancelar(): void {
    this.editDataService.clearEditData();
    this.modalState.close();
  }
}


