import { ChangeDetectionStrategy, Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ModalStateService } from 'src/app/shared/services/modalState';
import { CommonModule } from '@angular/common';
import { ModalAddEvent } from 'src/app/shared/components/modal-add/modal-add';
import { Bventas } from 'src/app/Bventas';
import { BVentasService } from 'src/app/shared/services/b-ventas.service';

@Component({
  selector: 'app-sales-page',
  imports: [FormsModule, ModalAddEvent, CommonModule],
  templateUrl: './sales-page.html',
  styleUrl: './sales-page.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class SalesPage {

  bventas : Bventas [] = [];
  
  typeData = signal('sales');

  constructor(private modalState: ModalStateService, private bventasService : BVentasService) {}

  ngOnInit(): void {
    this.modalState;
    this.listaVentas();
  }

  openModal() {
    this.modalState.open(this.typeData());
  }

    listaVentas(){
    this.bventasService.getUsuarioLista().subscribe(
      data => {
        this.bventas = data;
      console.log(this.bventas);
    }
  );
  }

    deleteVentas(id : number){
    this.bventasService.deleteVentasById(id).subscribe(
      () => this. listaVentas()
    );
  }
}
