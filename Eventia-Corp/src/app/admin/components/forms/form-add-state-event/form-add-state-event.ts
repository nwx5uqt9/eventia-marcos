import { HttpClientModule } from '@angular/common/http';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { EstadoEvento } from 'src/app/estadoEvento';
import { TipoEventoService } from 'src/app/shared/services/tipo-evento.service';
import { TipoEvento } from 'src/app/tipoEvento';

@Component({
  selector: 'app-form-add-state-event',
  imports: [FormsModule, HttpClientModule],
  templateUrl: './form-add-state-event.html',
  styleUrl: './form-add-state-event.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FormAddStateEvent {
guardar() {
throw new Error('Method not implemented.');
}
constructor( private tipoEvento: TipoEventoService) {}
  mensaje: string = '';

  id: number | null = null;
  nombre: string = '';
  descripcion: string = '';

  agregarTipo(){
const Tipoevento = new TipoEvento(
  null,
  this.nombre,
  this.descripcion,
);

console.log(Tipoevento);

this.tipoEvento.createTipo(Tipoevento).subscribe(
  res => {
        this.id = res.id;
        this.mensaje = 'Usuario registrado correctamente';
        // Opcional: limpiar campos
        this.nombre = '';
        this.descripcion = '';


    console.log(res);
    this.id = res.id;
  },


  err => {console.error(err);
          this.mensaje = 'Error al registrar usuario';
}


);
}

}
