import { Component, OnInit, signal, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ModalStateService } from 'src/app/shared/services/modalState';
import { EditDataService } from 'src/app/shared/services/edit-data.service';
import { CommonModule } from '@angular/common';
import { ModalAddEvent } from 'src/app/shared/components/modal-add/modal-add';
import { Bventas } from 'src/app/Bventas';
import { BVentasService } from 'src/app/shared/services/b-ventas.service';

@Component({
  selector: 'app-sales-page',
  standalone: true,
  imports: [FormsModule, ModalAddEvent, CommonModule],
  templateUrl: './sales-page.html',
  styleUrl: './sales-page.css'
})
export default class SalesPage implements OnInit {

  bventas: Bventas[] = [];
  codigo: string = '';
  typeData = signal('sales');

  constructor(
    private modalState: ModalStateService,
    private bventasService: BVentasService,
    private editDataService: EditDataService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.modalState;
    this.listaVentas();
  }

  openModal() {
    this.editDataService.clearEditData();
    this.modalState.open(this.typeData());
  }

  editVenta(venta: Bventas) {
    this.editDataService.setEditData(venta);
    this.modalState.open(this.typeData());
  }

  listaVentas(){
    this.bventasService.getUsuarioLista().subscribe(
      data => {
        this.bventas = data;
        console.log(this.bventas);
        this.cdr.markForCheck();
      }
    );
  }

  deleteVentas(id: number){
    this.bventasService.deleteVentasById(id).subscribe(
      () => this.listaVentas()
    );
  }

  generarReportePdf(): void {
    window.open('http://localhost:8082/ventas/reporte/pdf', '_blank');
  }
}
