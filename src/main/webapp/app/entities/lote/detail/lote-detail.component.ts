import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILote } from '../lote.model';

@Component({
  selector: 'jhi-lote-detail',
  templateUrl: './lote-detail.component.html',
})
export class LoteDetailComponent implements OnInit {
  lote: ILote | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lote }) => {
      this.lote = lote;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
