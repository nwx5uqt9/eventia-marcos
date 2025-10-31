import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { EstadoEvento } from 'src/app/estadoEvento';
import { EstadoEventoService } from 'src/app/shared/services/estado-evento.service';
import { EditDataService } from 'src/app/shared/services/edit-data.service';

@Component({
  selector: 'app-form-add-type-event',
  standalone: true,
  imports: [FormsModule, HttpClientModule, CommonModule],
  templateUrl: './form-add-type-event.html',
  styleUrl: './form-add-type-event.css',
})
export class FormAddTypeEvent implements OnInit {
  isEditMode: boolean = false;
  estadoId: number | null = null;
  mensaje: string = '';
  mensajeError: string = '';

  id: number | null = null;
  nombre: string = '';
  descripcion: string = '';

  constructor(
    private estadoService: EstadoEventoService,
    private editDataService: EditDataService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.checkEditMode();
  }

  checkEditMode(): void {
    const editData = this.editDataService.getEditData();
    if (editData && this.editDataService.isEditMode()) {
      this.isEditMode = true;
      this.estadoId = editData.id;
      this.nombre = editData.nombre || '';
      this.descripcion = editData.descripcion || '';
      this.cdr.markForCheck();
    }
  }

  agregarEstado() {
    if (!this.nombre || !this.descripcion) {
      this.mensajeError = 'Por favor completa todos los campos';
      return;
    }

    const estadoEvento = new EstadoEvento(
      this.isEditMode ? this.estadoId : null,
      this.nombre,
      this.descripcion
    );

    console.log(
      `${this.isEditMode ? 'Actualizando' : 'Creando'} estado:`,
      estadoEvento
    );

    const operation = this.isEditMode
      ? this.estadoService.update(this.estadoId!, estadoEvento)
      : this.estadoService.createEstado(estadoEvento);

    operation.subscribe({
      next: res => {
        this.id = res.id;
        this.mensaje = `Estado ${
          this.isEditMode ? 'actualizado' : 'registrado'
        } correctamente ✅`;
        this.mensajeError = '';
        this.nombre = '';
        this.descripcion = '';
        this.editDataService.clearEditData();
        setTimeout(() => window.location.reload(), 1500);
        console.log(res);
      },
      error: err => {
        console.error(err);
        this.mensajeError = `Error al ${
          this.isEditMode ? 'actualizar' : 'registrar'
        } estado ❌`;
        this.mensaje = '';
      },
    });
  }

  guardar() {
    this.agregarEstado();
  }
}
