import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { INotaContable, NotaContable } from '../nota-contable.model';
import { NotaContableService } from '../service/nota-contable.service';

@Component({
  selector: 'jhi-nota-contable-update',
  templateUrl: './nota-contable-update.component.html',
})
export class NotaContableUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    numero: [],
  });

  constructor(protected notaContableService: NotaContableService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notaContable }) => {
      this.updateForm(notaContable);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const notaContable = this.createFromForm();
    if (notaContable.id !== undefined) {
      this.subscribeToSaveResponse(this.notaContableService.update(notaContable));
    } else {
      this.subscribeToSaveResponse(this.notaContableService.create(notaContable));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INotaContable>>): void {
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

  protected updateForm(notaContable: INotaContable): void {
    this.editForm.patchValue({
      id: notaContable.id,
      numero: notaContable.numero,
    });
  }

  protected createFromForm(): INotaContable {
    return {
      ...new NotaContable(),
      id: this.editForm.get(['id'])!.value,
      numero: this.editForm.get(['numero'])!.value,
    };
  }
}
