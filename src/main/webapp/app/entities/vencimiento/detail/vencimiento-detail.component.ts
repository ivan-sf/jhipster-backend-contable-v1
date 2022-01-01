import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVencimiento } from '../vencimiento.model';

@Component({
  selector: 'jhi-vencimiento-detail',
  templateUrl: './vencimiento-detail.component.html',
})
export class VencimientoDetailComponent implements OnInit {
  vencimiento: IVencimiento | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vencimiento }) => {
      this.vencimiento = vencimiento;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
