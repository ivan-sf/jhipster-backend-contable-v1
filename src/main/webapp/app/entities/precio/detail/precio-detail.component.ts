import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrecio } from '../precio.model';

@Component({
  selector: 'jhi-precio-detail',
  templateUrl: './precio-detail.component.html',
})
export class PrecioDetailComponent implements OnInit {
  precio: IPrecio | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ precio }) => {
      this.precio = precio;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
