import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDetallesObjeto } from '../detalles-objeto.model';

@Component({
  selector: 'jhi-detalles-objeto-detail',
  templateUrl: './detalles-objeto-detail.component.html',
})
export class DetallesObjetoDetailComponent implements OnInit {
  detallesObjeto: IDetallesObjeto | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ detallesObjeto }) => {
      this.detallesObjeto = detallesObjeto;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
