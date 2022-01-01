import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPaquete, Paquete } from '../paquete.model';
import { PaqueteService } from '../service/paquete.service';

@Component({
  selector: 'jhi-paquete-update',
  templateUrl: './paquete-update.component.html',
})
export class PaqueteUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    cantidad: [],
  });

  constructor(protected paqueteService: PaqueteService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paquete }) => {
      this.updateForm(paquete);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paquete = this.createFromForm();
    if (paquete.id !== undefined) {
      this.subscribeToSaveResponse(this.paqueteService.update(paquete));
    } else {
      this.subscribeToSaveResponse(this.paqueteService.create(paquete));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaquete>>): void {
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

  protected updateForm(paquete: IPaquete): void {
    this.editForm.patchValue({
      id: paquete.id,
      cantidad: paquete.cantidad,
    });
  }

  protected createFromForm(): IPaquete {
    return {
      ...new Paquete(),
      id: this.editForm.get(['id'])!.value,
      cantidad: this.editForm.get(['cantidad'])!.value,
    };
  }
}
