import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITipoFactura } from '../tipo-factura.model';

@Component({
  selector: 'jhi-tipo-factura-detail',
  templateUrl: './tipo-factura-detail.component.html',
})
export class TipoFacturaDetailComponent implements OnInit {
  tipoFactura: ITipoFactura | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoFactura }) => {
      this.tipoFactura = tipoFactura;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
