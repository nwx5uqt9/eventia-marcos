import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RolUsuario } from 'src/app/rolUsuario';
import { RolusuarioService } from 'src/app/shared/services/rolusuario.service';
import { UsuarioService } from 'src/app/shared/services/usuario.service';
import { Usuario } from 'src/app/usuario';

@Component({
  selector: 'app-form-add-client',
  imports: [FormsModule, HttpClientModule, CommonModule],
  templateUrl: './form-add-client.html',
  styleUrl: './form-add-client.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FormAddClient implements OnInit {

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
    if (!this.rolIdSeleccionado) {
      this.mensajeError = 'Por favor selecciona un rol';
      return;
    }

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

    this.usuarioService.createUsuario(usuario).subscribe(
      (res) => {
        this.id = res.id;
        this.mensaje = 'Cliente registrado correctamente ✅';
        this.mensajeError = '';
        this.limpiarFormulario();
      },
      (err) => {
        console.error('Error al crear cliente:', err);
        if (err.status === 500 && err.error?.message?.includes('foreign key constraint')) {
          this.mensajeError = 'Error: El rol no existe. Ejecuta insert_roles_basicos.sql';
        } else {
          this.mensajeError = 'Error al registrar cliente ❌';
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
  }

  guardar(): void {
    this.agregarUsuario();
  }
}
