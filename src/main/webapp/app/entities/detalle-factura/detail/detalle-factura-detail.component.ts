import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDetalleFactura } from '../detalle-factura.model';

@Component({
  selector: 'jhi-detalle-factura-detail',
  templateUrl: './detalle-factura-detail.component.html',
})
export class DetalleFacturaDetailComponent implements OnInit {
  detalleFactura: IDetalleFactura | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ detalleFactura }) => {
      this.detalleFactura = detalleFactura;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
