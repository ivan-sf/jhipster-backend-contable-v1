import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRegimen } from '../regimen.model';

@Component({
  selector: 'jhi-regimen-detail',
  templateUrl: './regimen-detail.component.html',
})
export class RegimenDetailComponent implements OnInit {
  regimen: IRegimen | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ regimen }) => {
      this.regimen = regimen;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
