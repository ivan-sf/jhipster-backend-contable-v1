import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPaquete } from '../paquete.model';

@Component({
  selector: 'jhi-paquete-detail',
  templateUrl: './paquete-detail.component.html',
})
export class PaqueteDetailComponent implements OnInit {
  paquete: IPaquete | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paquete }) => {
      this.paquete = paquete;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
