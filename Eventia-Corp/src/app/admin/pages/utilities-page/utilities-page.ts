import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { EstadoEvento } from 'src/app/estadoEvento';
import { RolUsuario } from 'src/app/rolUsuario';
import { ModalAddEvent } from 'src/app/shared/components/modal-add/modal-add';
import { EstadoEventoService } from 'src/app/shared/services/estado-evento.service';
import { ModalStateService } from 'src/app/shared/services/modalState';
import { EditDataService } from 'src/app/shared/services/edit-data.service';
import { RolusuarioService } from 'src/app/shared/services/rolusuario.service';
import { TipoEventoService } from 'src/app/shared/services/tipo-evento.service';
import { TipoEvento } from 'src/app/tipoEvento';

@Component({
  selector: 'app-utilities-page',
  imports: [FormsModule, ModalAddEvent, CommonModule],
  templateUrl: './utilities-page.html',
  styleUrl: './utilities-page.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class UtilitiesPage {
  optionRoleUser = signal('role_user');
  optionTypeEvent = signal('type_event');
  optionStateEvent = signal('state_event');


  rolusuario : RolUsuario [] = [];
  tipoevento : TipoEvento [] = [];
  estadoevento : EstadoEvento [] = [];

  id: number | null = null;
  rol: string = '';
  descripcion: string = '';




  option = signal('');

  constructor(
    private modalState: ModalStateService,
    private rolService: RolusuarioService,
    private tipoService: TipoEventoService,
    private estadoService: EstadoEventoService,
    private editDataService: EditDataService
  ) {}

  ngOnInit(): void {
    this.modalState;
    this.listaRoles();
    this.listaTipo();
    this.listaEstado();
  }

  openModal(optionSelected: string) {
    this.editDataService.clearEditData();
    this.option.set(optionSelected);
    this.modalState.open(optionSelected);
  }

  editRol(rol: RolUsuario) {
    this.editDataService.setEditData(rol);
    this.option.set(this.optionRoleUser());
    this.modalState.open(this.optionRoleUser());
  }

  editTipo(tipo: TipoEvento) {
    this.editDataService.setEditData(tipo);
    this.option.set(this.optionTypeEvent());
    this.modalState.open(this.optionTypeEvent());
  }

  editEstado(estado: EstadoEvento) {
    this.editDataService.setEditData(estado);
    this.option.set(this.optionStateEvent());
    this.modalState.open(this.optionStateEvent());
  }

    listaRoles(){
    this.rolService.getrolesLista().subscribe(
      data => {
        this.rolusuario = data;
      console.log(this.rolusuario);
    }
  );
  }

    listaTipo(){
    this.tipoService.getTipoLista().subscribe(
      data => {
        this.tipoevento = data;
      console.log(this.tipoevento);
    }
  );
  }

    listaEstado(){
    this.estadoService.getEstadoLista().subscribe(
      data => {
        this.estadoevento = data;
      console.log(this.estadoevento);
    }
  );
  }
  deleteRol(id : number){
    this.rolService.deleteRolById(id).subscribe(
      () => this. listaRoles()
    );
}

  deleteEstado(id : number){
    this.estadoService.deleteEstadoById(id).subscribe(
      () => this. listaEstado()
    );
}

  deleteTipo(id : number){
    this.tipoService.deleteTipoById(id).subscribe(
      () => this. listaTipo()
    );
}
}
