import { CommonModule } from '@angular/common';
import { Component, signal, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ModalAddEvent } from 'src/app/shared/components/modal-add/modal-add';
import { ModalStateService } from 'src/app/shared/services/modalState';
import { EditDataService } from 'src/app/shared/services/edit-data.service';
import { UsuarioService } from 'src/app/shared/services/usuario.service';
import { Usuario } from 'src/app/usuario';

@Component({
  selector: 'app-users-page',
  standalone: true,
  imports: [FormsModule, ModalAddEvent, CommonModule],
  templateUrl: './users-page.html',
  styleUrl: './users-page.css'
})
export default class UsersPage implements OnInit {
  typeData = signal('user');

  usuario: Usuario[] = [];
  codigo: string = '';

  constructor(
    private modalState: ModalStateService,
    private usuarioService: UsuarioService,
    private editDataService: EditDataService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.modalState;
    this.listaUsuario();
  }

  openModal() {
    this.editDataService.clearEditData();
    this.modalState.open(this.typeData());
  }

  editUsuario(usuario: Usuario) {
    this.editDataService.setEditData(usuario);
    this.modalState.open(this.typeData());
  }

  listaUsuario(){
    this.usuarioService.getUsuarioLista().subscribe(
      data => {
        this.usuario = data;
        console.log(this.usuario);
        this.cdr.markForCheck();
      }
    );
  }

  deleteUsuario(id: number){
    this.usuarioService.deleteUsuarioById(id).subscribe(
      () => this.listaUsuario()
    );
  }
}
