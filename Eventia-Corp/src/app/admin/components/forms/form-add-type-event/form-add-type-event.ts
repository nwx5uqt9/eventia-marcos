import { HttpClientModule } from '@angular/common/http';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { EstadoEvento } from 'src/app/estadoEvento';
import { EstadoEventoService } from 'src/app/shared/services/estado-evento.service';

@Component({
  selector: 'app-form-add-type-event',
  imports: [FormsModule, HttpClientModule],
  templateUrl: './form-add-type-event.html',
  styleUrl: './form-add-type-event.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FormAddTypeEvent {
  mensaje: string = '';
  id: number | null = null;
  nombre: string = '';
  descripcion: string = '';

  constructor(private estadoService: EstadoEventoService) {}

  guardar() {
    this.agregarEstado();
  }

  agregarEstado() {
    const estadoEvento = new EstadoEvento(
      null,
      this.nombre,
      this.descripcion
    );

    console.log(estadoEvento);

    this.estadoService.createEstado(estadoEvento).subscribe(
      res => {
        this.id = res.id;
        this.mensaje = 'Estado registrado correctamente ✅';
        // Limpiar campos
        this.nombre = '';
        this.descripcion = '';
        console.log(res);
      },
      err => {
        console.error(err);
        this.mensaje = 'Error al registrar estado ❌';
      }
    );
  }
}
