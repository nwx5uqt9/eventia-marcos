import { HttpClientModule } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RolUsuario } from 'src/app/rolUsuario';
import { RolusuarioService } from 'src/app/shared/services/rolusuario.service';

@Component({
  selector: 'app-form-add-role',
  imports: [FormsModule, HttpClientModule],
  templateUrl: './form-add-role.html',
  styleUrl: './form-add-role.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FormAddRole implements OnInit {
guardar() {

}
  ngOnInit(): void {

  }
constructor( private rolService: RolusuarioService) {}

  mensaje: string = '';

  id: number | null = null;
  rol: string = '';
  descripcion: string = '';

  agregarRol(){
const rolUsuario = new RolUsuario(
  null,
  this.rol,
  this.descripcion,
);

console.log(rolUsuario);

this.rolService.createRol(rolUsuario).subscribe(
  res => {
        this.id = res.id;
        this.mensaje = 'Usuario registrado correctamente ✅';
        // Opcional: limpiar campos
        this.rol = '';
        this.descripcion = '';


    console.log(res);
    this.id = res.id;
  },


  err => {console.error(err);
          this.mensaje = 'Error al registrar usuario ❌';
}


);
}
}
