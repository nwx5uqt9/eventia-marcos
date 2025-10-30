import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RolUsuario } from 'src/app/rolUsuario';
import { RolusuarioService } from 'src/app/shared/services/rolusuario.service';
import { EditDataService } from 'src/app/shared/services/edit-data.service';

@Component({
  selector: 'app-form-add-role',
  standalone: true,
  imports: [FormsModule, HttpClientModule, CommonModule],
  templateUrl: './form-add-role.html',
  styleUrl: './form-add-role.css'
})
export class FormAddRole implements OnInit {

  isEditMode: boolean = false;
  rolId: number | null = null;
  mensaje: string = '';
  mensajeError: string = '';

  id: number | null = null;
  rol: string = '';
  descripcion: string = '';

  constructor(
    private rolService: RolusuarioService,
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
      this.rolId = editData.id;
      this.rol = editData.rol || '';
      this.descripcion = editData.descripcion || '';
      this.cdr.markForCheck();
    }
  }

  agregarRol() {
    if (!this.rol || !this.descripcion) {
      this.mensajeError = 'Por favor completa todos los campos';
      return;
    }

    const rolUsuario = new RolUsuario(
      this.isEditMode ? this.rolId : null,
      this.rol,
      this.descripcion
    );

    console.log(`${this.isEditMode ? 'Actualizando' : 'Creando'} rol:`, rolUsuario);

    const operation = this.isEditMode
      ? this.rolService.update(this.rolId!, rolUsuario)
      : this.rolService.createRol(rolUsuario);

    operation.subscribe({
      next: (res) => {
        this.id = res.id;
        this.mensaje = `Rol ${this.isEditMode ? 'actualizado' : 'registrado'} correctamente ✅`;
        this.mensajeError = '';
        this.rol = '';
        this.descripcion = '';
        this.editDataService.clearEditData();
        setTimeout(() => window.location.reload(), 1500);
        console.log(res);
      },
      error: (err) => {
        console.error(err);
        this.mensajeError = `Error al ${this.isEditMode ? 'actualizar' : 'registrar'} rol ❌`;
        this.mensaje = '';
      }
    });
  }

  guardar() {
    this.agregarRol();
  }
}
