import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ILote, Lote } from '../lote.model';
import { LoteService } from '../service/lote.service';

@Component({
  selector: 'jhi-lote-update',
  templateUrl: './lote-update.component.html',
})
export class LoteUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    numero: [],
  });

  constructor(protected loteService: LoteService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lote }) => {
      this.updateForm(lote);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lote = this.createFromForm();
    if (lote.id !== undefined) {
      this.subscribeToSaveResponse(this.loteService.update(lote));
    } else {
      this.subscribeToSaveResponse(this.loteService.create(lote));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILote>>): void {
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

  protected updateForm(lote: ILote): void {
    this.editForm.patchValue({
      id: lote.id,
      numero: lote.numero,
    });
  }

  protected createFromForm(): ILote {
    return {
      ...new Lote(),
      id: this.editForm.get(['id'])!.value,
      numero: this.editForm.get(['numero'])!.value,
    };
  }
}
