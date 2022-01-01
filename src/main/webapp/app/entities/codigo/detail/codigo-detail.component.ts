import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICodigo } from '../codigo.model';

@Component({
  selector: 'jhi-codigo-detail',
  templateUrl: './codigo-detail.component.html',
})
export class CodigoDetailComponent implements OnInit {
  codigo: ICodigo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ codigo }) => {
      this.codigo = codigo;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
