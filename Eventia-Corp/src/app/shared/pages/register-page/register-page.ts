import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { NavbarIndex } from "../../components/navbar-index/navbar-index";
import { RouterLink } from "@angular/router";
import { FormsModule } from "@angular/forms";
import { Usuario } from 'src/app/usuario';
import { UsuarioService } from  '../../services/usuario.service'
import { RolUsuario } from 'src/app/rolUsuario';
import { HttpClientModule } from '@angular/common/http';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-register-page',
  imports: [NavbarIndex, RouterLink, FormsModule, HttpClientModule, NgIf],
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
rolUsuario: RolUsuario = new RolUsuario(2, 'Rol por defecto', 'usuario');

  mensaje: string = ''; 


  constructor(private usuarioService : UsuarioService ) {}

  ngOnInit(): void {

  } 

  agregarUsuario(){
const usuario = new Usuario(
  null,
  this.nombre,
  this.apellidos,
  this.nombreUsuario,
  this.contrasena,
  this.dni,
  this.edad,
  this.sexo,
  this.correo,
  this.telefono,
  this.direccion,
  this.rolUsuario 
);

console.log(usuario);

this.usuarioService.createUsuario(usuario).subscribe(
  res => {
        this.id = res.id;
        this.mensaje = 'Usuario registrado correctamente ✅';
        // Opcional: limpiar campos
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

    console.log(res);  
    this.id = res.id; 
  },

  
  err => {console.error(err);
          this.mensaje = 'Error al registrar usuario ❌';
}
          

);
  }

}
