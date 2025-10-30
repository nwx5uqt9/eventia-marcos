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
  selector: 'app-form-add-client',
  standalone: true,
  imports: [FormsModule, HttpClientModule, CommonModule],
  templateUrl: './form-add-client.html',
  styleUrl: './form-add-client.css'
})
export class FormAddClient implements OnInit {

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
        const rolCliente = roles.find(r => r.rol.toLowerCase().includes('cliente'));
        if (rolCliente) {
          this.rolIdSeleccionado = rolCliente.id;
        } else if (roles.length > 0) {
          this.rolIdSeleccionado = roles[0].id;
        }
      },
      (error) => {
        console.error('Error al cargar roles:', error);
        this.mensajeError = 'No se pudieron cargar los roles.';
      }
    );
  }

  agregarUsuario(): void {
    // Validar que se haya seleccionado un rol
    if (!this.rolIdSeleccionado || this.rolIdSeleccionado === null) {
      this.mensajeError = 'Por favor selecciona un rol';
      return;
    }

    // Convertir a número si viene como string del select
    const rolIdNumerico = Number(this.rolIdSeleccionado);

    console.log('Buscando rol con ID:', rolIdNumerico);
    console.log('Roles disponibles:', this.roles);

    // Buscar el rol seleccionado
    const rolSeleccionado = this.roles.find(r => r.id === rolIdNumerico);

    console.log('Rol seleccionado:', rolSeleccionado);

    if (!rolSeleccionado) {
      this.mensajeError = `Rol no válido. ID seleccionado: ${rolIdNumerico}`;
      console.error('No se encontró el rol con ID:', rolIdNumerico);
      return;
    }

    // Crear el objeto usuario con el rol completo
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

    console.log('Usuario a enviar:', usuario);

    const operation = this.isEditMode
      ? this.usuarioService.update(this.usuarioId!, usuario)
      : this.usuarioService.createUsuario(usuario);

    operation.subscribe({
      next: (res) => {
        this.id = res.id;
        this.mensaje = `Cliente ${this.isEditMode ? 'actualizado' : 'registrado'} correctamente`;
        this.mensajeError = '';
        console.log(`Cliente ${this.isEditMode ? 'actualizado' : 'creado'} exitosamente:`, res);
        this.limpiarFormulario();
        this.editDataService.clearEditData();
        setTimeout(() => window.location.reload(), 1500);
      },
      error: (err) => {
        console.error('Error completo al crear cliente:', err);
        console.error('Error response:', err.error);
        console.error('Error status:', err.status);

        if (err.status === 500) {
          if (err.error?.message?.includes('foreign key constraint')) {
            this.mensajeError = 'Error: El rol no existe en la base de datos';
          } else if (err.error?.message?.includes('email')) {
            this.mensajeError = 'Error: El email ya está registrado';
          } else if (err.error?.message?.includes('nombreusuario')) {
            this.mensajeError = 'Error: El nombre de usuario ya está registrado';
          } else if (err.error?.message?.includes('dni')) {
            this.mensajeError = 'Error: El DNI ya está registrado';
          } else {
            this.mensajeError = `Error al registrar cliente: ${err.error?.message || err.message}`;
          }
        } else if (err.status === 400) {
          this.mensajeError = `Datos inválidos: ${err.error?.message || err.message}`;
        } else {
          this.mensajeError = `Error al registrar cliente: ${err.message}`;
        }
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
  }
}
