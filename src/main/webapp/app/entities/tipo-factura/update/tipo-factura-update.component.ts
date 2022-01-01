import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITipoFactura, TipoFactura } from '../tipo-factura.model';
import { TipoFacturaService } from '../service/tipo-factura.service';

@Component({
  selector: 'jhi-tipo-factura-update',
  templateUrl: './tipo-factura-update.component.html',
})
export class TipoFacturaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [],
    prefijoInicial: [],
    prefijoFinal: [],
    prefijoActual: [],
  });

  constructor(protected tipoFacturaService: TipoFacturaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoFactura }) => {
      this.updateForm(tipoFactura);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tipoFactura = this.createFromForm();
    if (tipoFactura.id !== undefined) {
      this.subscribeToSaveResponse(this.tipoFacturaService.update(tipoFactura));
    } else {
      this.subscribeToSaveResponse(this.tipoFacturaService.create(tipoFactura));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITipoFactura>>): void {
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

  protected updateForm(tipoFactura: ITipoFactura): void {
    this.editForm.patchValue({
      id: tipoFactura.id,
      nombre: tipoFactura.nombre,
      prefijoInicial: tipoFactura.prefijoInicial,
      prefijoFinal: tipoFactura.prefijoFinal,
      prefijoActual: tipoFactura.prefijoActual,
    });
  }

  protected createFromForm(): ITipoFactura {
    return {
      ...new TipoFactura(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      prefijoInicial: this.editForm.get(['prefijoInicial'])!.value,
      prefijoFinal: this.editForm.get(['prefijoFinal'])!.value,
      prefijoActual: this.editForm.get(['prefijoActual'])!.value,
    };
  }
}
