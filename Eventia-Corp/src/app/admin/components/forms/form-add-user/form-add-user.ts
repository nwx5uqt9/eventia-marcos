import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RolUsuario } from 'src/app/rolUsuario';
import { RolusuarioService } from 'src/app/shared/services/rolusuario.service';
import { UsuarioService } from 'src/app/shared/services/usuario.service';
import { EditDataService } from 'src/app/shared/services/edit-data.service';
import { Usuario } from 'src/app/usuario';

@Component({
  selector: 'app-form-add-user',
  standalone: true,
  imports: [FormsModule, HttpClientModule, CommonModule],
  templateUrl: './form-add-user.html',
  styleUrl: './form-add-user.css'
})
export class FormAddUser implements OnInit {

  isEditMode: boolean = false;
  usuarioId: number | null = null;

  id: number | null = null;
  nombre: string = '';
  apellidos: string = '';
  nombreusuario: string = '';
  password: string = '';
  dni: string = '';
  edad: number = 0;
  sexo: string = '';
  email: string = '';
  telefono: string = '';
  direccion: string = '';
  rolIdSeleccionado: number | null = null;

  roles: RolUsuario[] = [];
  mensaje: string = '';
  mensajeError: string = '';

  constructor(
    private usuarioService: UsuarioService,
    private rolService: RolusuarioService,
    private editDataService: EditDataService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarRoles();
    this.checkEditMode();
  }

  checkEditMode(): void {
    const editData = this.editDataService.getEditData();
    if (editData && this.editDataService.isEditMode()) {
      this.isEditMode = true;
      this.usuarioId = editData.id;

      this.nombre = editData.nombre || '';
      this.apellidos = editData.apellidos || '';
      this.nombreusuario = editData.nombreusuario || '';
      this.password = editData.password || '';
      this.dni = editData.dni || '';
      this.edad = editData.edad || 0;
      this.sexo = editData.sexo || '';
      this.email = editData.email || '';
      this.telefono = editData.telefono || '';
      this.direccion = editData.direccion || '';
      this.rolIdSeleccionado = editData.rolUsuario?.id || null;

      this.cdr.markForCheck();
    }
  }

  cargarRoles(): void {
    this.rolService.getAll().subscribe(
      (roles) => {
        this.roles = roles;
        // Seleccionar el primer rol por defecto si existe
        if (roles.length > 0 && !this.rolIdSeleccionado) {
          this.rolIdSeleccionado = roles[0].id;
        }
      },
      (error) => {
        console.error('Error al cargar roles:', error);
        this.mensajeError = 'No se pudieron cargar los roles. Verifica que existan roles en la base de datos.';
      }
    );
  }

  agregarUsuario(): void {
    if (!this.rolIdSeleccionado) {
      this.mensajeError = 'Por favor selecciona un rol';
      return;
    }

    const rolIdNumerico = Number(this.rolIdSeleccionado);
    const rolSeleccionado = this.roles.find(r => r.id === rolIdNumerico);

    if (!rolSeleccionado) {
      this.mensajeError = 'Rol no válido';
      return;
    }

    const usuario = new Usuario(
      this.isEditMode ? this.usuarioId : null,
      this.nombre,
      this.apellidos,
      this.nombreusuario,
      this.password,
      this.dni,
      this.edad,
      this.telefono,
      this.sexo,
      this.email,
      this.direccion,
      rolSeleccionado
    );

    console.log(`${this.isEditMode ? 'Actualizando' : 'Creando'} usuario:`, usuario);

    const operation = this.isEditMode
      ? this.usuarioService.update(this.usuarioId!, usuario)
      : this.usuarioService.createUsuario(usuario);

    operation.subscribe({
      next: (res) => {
        this.id = res.id;
        this.mensaje = `Usuario ${this.isEditMode ? 'actualizado' : 'registrado'} correctamente ✅`;
        this.mensajeError = '';
        this.limpiarFormulario();
        this.editDataService.clearEditData();
        setTimeout(() => window.location.reload(), 1500);
      },
      error: (err) => {
        console.error(`Error al ${this.isEditMode ? 'actualizar' : 'crear'} usuario:`, err);
        this.mensajeError = `Error al ${this.isEditMode ? 'actualizar' : 'registrar'} usuario ❌`;
        this.mensaje = '';
      }
    });
  }

  limpiarFormulario(): void {
    this.nombre = '';
    this.apellidos = '';
    this.nombreusuario = '';
    this.password = '';
    this.dni = '';
    this.edad = 0;
    this.sexo = '';
    this.email = '';
    this.telefono = '';
    this.direccion = '';
    // No limpiar rolIdSeleccionado para mantener la selección
  }

  guardar(): void {
    this.agregarUsuario();
  }
}
