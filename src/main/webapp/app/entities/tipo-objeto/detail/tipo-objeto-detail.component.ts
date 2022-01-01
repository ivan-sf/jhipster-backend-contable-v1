import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITipoObjeto } from '../tipo-objeto.model';

@Component({
  selector: 'jhi-tipo-objeto-detail',
  templateUrl: './tipo-objeto-detail.component.html',
})
export class TipoObjetoDetailComponent implements OnInit {
  tipoObjeto: ITipoObjeto | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoObjeto }) => {
      this.tipoObjeto = tipoObjeto;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
