import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITipoObjeto, TipoObjeto } from '../tipo-objeto.model';
import { TipoObjetoService } from '../service/tipo-objeto.service';

@Component({
  selector: 'jhi-tipo-objeto-update',
  templateUrl: './tipo-objeto-update.component.html',
})
export class TipoObjetoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [],
    fechaRegistro: [],
    codigoDian: [],
  });

  constructor(protected tipoObjetoService: TipoObjetoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoObjeto }) => {
      this.updateForm(tipoObjeto);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tipoObjeto = this.createFromForm();
    if (tipoObjeto.id !== undefined) {
      this.subscribeToSaveResponse(this.tipoObjetoService.update(tipoObjeto));
    } else {
      this.subscribeToSaveResponse(this.tipoObjetoService.create(tipoObjeto));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITipoObjeto>>): void {
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

  protected updateForm(tipoObjeto: ITipoObjeto): void {
    this.editForm.patchValue({
      id: tipoObjeto.id,
      nombre: tipoObjeto.nombre,
      fechaRegistro: tipoObjeto.fechaRegistro,
      codigoDian: tipoObjeto.codigoDian,
    });
  }

  protected createFromForm(): ITipoObjeto {
    return {
      ...new TipoObjeto(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      fechaRegistro: this.editForm.get(['fechaRegistro'])!.value,
      codigoDian: this.editForm.get(['codigoDian'])!.value,
    };
  }
}
