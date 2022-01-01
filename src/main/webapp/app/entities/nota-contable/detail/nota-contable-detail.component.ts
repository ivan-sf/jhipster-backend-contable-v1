import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INotaContable } from '../nota-contable.model';

@Component({
  selector: 'jhi-nota-contable-detail',
  templateUrl: './nota-contable-detail.component.html',
})
export class NotaContableDetailComponent implements OnInit {
  notaContable: INotaContable | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notaContable }) => {
      this.notaContable = notaContable;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
