import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RolUsuario } from 'src/app/rolUsuario';
import { RolusuarioService } from 'src/app/shared/services/rolusuario.service';
import { UsuarioService } from 'src/app/shared/services/usuario.service';
import { Usuario } from 'src/app/usuario';

@Component({
  selector: 'app-form-add-user',
  imports: [FormsModule, HttpClientModule, CommonModule],
  templateUrl: './form-add-user.html',
  styleUrl: './form-add-user.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FormAddUser implements OnInit {

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
    private rolService: RolusuarioService
  ) {}

  ngOnInit(): void {
    this.cargarRoles();
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
    // Validar que se haya seleccionado un rol
    if (!this.rolIdSeleccionado) {
      this.mensajeError = 'Por favor selecciona un rol';
      return;
    }

    // Buscar el rol seleccionado
    const rolSeleccionado = this.roles.find(r => r.id === this.rolIdSeleccionado);

    if (!rolSeleccionado) {
      this.mensajeError = 'Rol no válido';
      return;
    }

    const usuario = new Usuario(
      null,
      this.nombre,
      this.apellidos,
      this.nombreusuario,
      this.password,
      this.dni,
      this.edad,
      this.sexo,
      this.email,
      this.telefono,
      this.direccion,
      rolSeleccionado
    );

    console.log('Creando usuario:', usuario);

    this.usuarioService.createUsuario(usuario).subscribe(
      (res) => {
        this.id = res.id;
        this.mensaje = 'Usuario registrado correctamente ✅';
        this.mensajeError = '';

        // Limpiar campos
        this.limpiarFormulario();

        console.log('Usuario creado:', res);
      },
      (err) => {
        console.error('Error al crear usuario:', err);

        // Mensajes de error específicos
        if (err.status === 500 && err.error?.message?.includes('foreign key constraint')) {
          this.mensajeError = 'Error: El rol seleccionado no existe en la base de datos. Por favor, ejecuta el script insert_roles_basicos.sql';
        } else if (err.status === 400) {
          this.mensajeError = 'Error: Datos inválidos. Verifica que todos los campos estén correctos.';
        } else {
          this.mensajeError = 'Error al registrar usuario ❌. ' + (err.error?.message || 'Error desconocido');
        }

        this.mensaje = '';
      }
    );
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
