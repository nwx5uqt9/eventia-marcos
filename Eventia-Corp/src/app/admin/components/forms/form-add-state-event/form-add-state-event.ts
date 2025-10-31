import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TipoEventoService } from 'src/app/shared/services/tipo-evento.service';
import { TipoEvento } from 'src/app/tipoEvento';
import { EditDataService } from 'src/app/shared/services/edit-data.service';

@Component({
  selector: 'app-form-add-state-event',
  standalone: true,
  imports: [FormsModule, HttpClientModule, CommonModule],
  templateUrl: './form-add-state-event.html',
  styleUrl: './form-add-state-event.css'
})
export class FormAddStateEvent implements OnInit {

  isEditMode: boolean = false;
  tipoId: number | null = null;
  mensaje: string = '';
  mensajeError: string = '';

  id: number | null = null;
  nombre: string = '';
  descripcion: string = '';

  constructor(
    private tipoEvento: TipoEventoService,
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
      this.tipoId = editData.id;
      this.nombre = editData.nombre || '';
      this.descripcion = editData.descripcion || '';
      this.cdr.markForCheck();
    }
  }

  agregarTipo() {
    if (!this.nombre || !this.descripcion) {
      this.mensajeError = 'Por favor completa todos los campos';
      return;
    }

    const Tipoevento = new TipoEvento(
      this.isEditMode ? this.tipoId : null,
      this.nombre,
      this.descripcion
    );

    console.log(`${this.isEditMode ? 'Actualizando' : 'Creando'} tipo:`, Tipoevento);

    const operation = this.isEditMode
      ? this.tipoEvento.update(this.tipoId!, Tipoevento)
      : this.tipoEvento.createTipo(Tipoevento);

    operation.subscribe({
      next: (res) => {
        this.id = res.id;
        this.mensaje = `Tipo ${this.isEditMode ? 'actualizado' : 'registrado'} correctamente ✅`;
        this.mensajeError = '';
        this.nombre = '';
        this.descripcion = '';
        this.editDataService.clearEditData();
        setTimeout(() => window.location.reload(), 1500);
        console.log(res);
      },
      error: (err) => {
        console.error(err);
        this.mensajeError = `Error al ${this.isEditMode ? 'actualizar' : 'registrar'} tipo ❌`;
        this.mensaje = '';
      }
    });
  }

  guardar() {
    this.agregarTipo();
  }
}
