import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IRegimen, Regimen } from '../regimen.model';
import { RegimenService } from '../service/regimen.service';

@Component({
  selector: 'jhi-regimen-update',
  templateUrl: './regimen-update.component.html',
})
export class RegimenUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    tipoRegimen: [],
    nombreRegimen: [],
    fechaRegistro: [],
  });

  constructor(protected regimenService: RegimenService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ regimen }) => {
      this.updateForm(regimen);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const regimen = this.createFromForm();
    if (regimen.id !== undefined) {
      this.subscribeToSaveResponse(this.regimenService.update(regimen));
    } else {
      this.subscribeToSaveResponse(this.regimenService.create(regimen));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRegimen>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(regimen: IRegimen): void {
    this.editForm.patchValue({
      id: regimen.id,
      tipoRegimen: regimen.tipoRegimen,
      nombreRegimen: regimen.nombreRegimen,
      fechaRegistro: regimen.fechaRegistro,
    });
  }

  protected createFromForm(): IRegimen {
    return {
      ...new Regimen(),
      id: this.editForm.get(['id'])!.value,
      tipoRegimen: this.editForm.get(['tipoRegimen'])!.value,
      nombreRegimen: this.editForm.get(['nombreRegimen'])!.value,
      fechaRegistro: this.editForm.get(['fechaRegistro'])!.value,
    };
  }
}
