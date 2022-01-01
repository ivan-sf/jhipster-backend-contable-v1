import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IObjeto } from '../objeto.model';

@Component({
  selector: 'jhi-objeto-detail',
  templateUrl: './objeto-detail.component.html',
})
export class ObjetoDetailComponent implements OnInit {
  objeto: IObjeto | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ objeto }) => {
      this.objeto = objeto;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
