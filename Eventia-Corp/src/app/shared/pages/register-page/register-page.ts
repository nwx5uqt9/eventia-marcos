import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { NavbarIndex } from "../../components/navbar-index/navbar-index";
import { Router, RouterLink } from "@angular/router";
import { FormsModule } from "@angular/forms";
import { Usuario } from 'src/app/usuario';
import { UsuarioService } from  '../../services/usuario.service'
import { RolUsuario } from 'src/app/rolUsuario';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register-page',
  standalone: true,
  imports: [NavbarIndex, RouterLink, FormsModule, HttpClientModule, CommonModule],
  templateUrl: './register-page.html',
  styleUrl: './register-page.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class RegisterPage implements OnInit {


  id: number | null = null;
  nombre: string = '';
  apellidos: string = '';
  nombreUsuario: string = '';
  contrasena: string = '';
  dni: string = '';
  edad: number = 0;
  sexo: string = '';
  correo: string = '';
  telefono: string = '';
  direccion: string = '';
  rolUsuario: RolUsuario = new RolUsuario(3, 'CLIENTE', 'Cliente/Usuario final');

  mensaje: string = '';
  registrando: boolean = false;


  constructor(
    private usuarioService: UsuarioService,
    private cdr: ChangeDetectorRef,
    private router: Router
  ) {}

  ngOnInit(): void {

  }

  agregarUsuario(){
    // Prevenir doble click
    if (this.registrando) {
      console.log('Ya hay un registro en proceso...');
      return;
    }

    // Validar campos requeridos
    if (!this.nombre || !this.apellidos || !this.nombreUsuario || !this.contrasena || !this.correo) {
      this.mensaje = 'Por favor completa todos los campos obligatorios';
      this.cdr.markForCheck();
      return;
    }

    // Validar email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(this.correo)) {
      this.mensaje = 'Por favor ingresa un correo electrónico válido';
      this.cdr.markForCheck();
      return;
    }

    this.registrando = true;
    this.mensaje = 'Registrando...';
    this.cdr.markForCheck();

    const usuario = new Usuario(
      null,
      this.nombre,
      this.apellidos,
      this.nombreUsuario,
      this.contrasena,
      this.dni,
      this.edad,
      this.telefono,
      this.sexo,
      this.correo,
      this.direccion,
      this.rolUsuario
    );

    console.log('Usuario a registrar:', usuario);

    this.usuarioService.createUsuario(usuario).subscribe({
      next: (res) => {
        console.log('Usuario registrado exitosamente:', res);
        this.id = res.id;
        this.mensaje = 'Usuario registrado correctamente';
        this.registrando = false;
        this.cdr.markForCheck();

        // Limpiar campos
        this.nombre = '';
        this.apellidos = '';
        this.nombreUsuario = '';
        this.contrasena = '';
        this.dni = '';
        this.edad = 0;
        this.sexo = '';
        this.correo = '';
        this.telefono = '';
        this.direccion = '';

        // Redirigir al login después de 2 segundos
        setTimeout(() => {
          this.router.navigate(['/index']);
        }, 2000);
      },
      error: (err) => {
        console.error('Error completo al registrar:', err);
        console.error('Status:', err.status);
        console.error('Error body:', err.error);

        this.registrando = false;

        if (err.status === 409 || err.status === 400) {
          // Error de duplicados o validación
          if (err.error && err.error.message) {
            this.mensaje = 'Error: ' + err.error.message;
          } else if (err.error && typeof err.error === 'string') {
            this.mensaje = 'Error: ' + err.error;
          } else if (err.message) {
            this.mensaje = 'Error: ' + err.message;
          } else {
            this.mensaje = 'Error: Ya existe un usuario con esos datos (email, nombre de usuario o DNI)';
          }
        } else if (err.status === 500) {
          this.mensaje = 'Error: Error en el servidor. Por favor contacta al administrador';
        } else if (err.status === 0) {
          this.mensaje = 'Error: No se puede conectar con el servidor. Verifica que Spring Boot esté corriendo';
        } else {
          this.mensaje = 'Error: Error al registrar usuario. Intenta nuevamente';
        }

        this.cdr.markForCheck();
      }
    });
  }

}
