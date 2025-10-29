import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ModalAddEvent } from 'src/app/shared/components/modal-add/modal-add';
import { ModalStateService } from 'src/app/shared/services/modalState';
import { UsuarioService } from 'src/app/shared/services/usuario.service';
import { Usuario } from 'src/app/usuario';

@Component({
  selector: 'app-users-page',
  imports: [FormsModule, ModalAddEvent, CommonModule],
  templateUrl: './users-page.html',
  styleUrl: './users-page.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class UsersPage {
  typeData = signal('user');

  usuario : Usuario [] = [];

  constructor(private modalState: ModalStateService, private usuarioService : UsuarioService ) {}

  ngOnInit(): void {
    this.modalState;
    this.listaUsuario();
  }

  openModal() {
    this.modalState.open(this.typeData());
  }

    listaUsuario(){
    this.usuarioService.getUsuarioLista().subscribe(
      data => {
        this.usuario = data;
      console.log(this.usuario);
    }
  );
  }

  deleteUsuario(id : number){
    this.usuarioService.deleteUsuarioById(id).subscribe(
      () => this. listaUsuario()
    );
  }
}
