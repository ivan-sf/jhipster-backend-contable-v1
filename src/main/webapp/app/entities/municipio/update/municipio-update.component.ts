import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMunicipio, Municipio } from '../municipio.model';
import { MunicipioService } from '../service/municipio.service';

@Component({
  selector: 'jhi-municipio-update',
  templateUrl: './municipio-update.component.html',
})
export class MunicipioUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [],
    codigoDIAN: [],
  });

  constructor(protected municipioService: MunicipioService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ municipio }) => {
      this.updateForm(municipio);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const municipio = this.createFromForm();
    if (municipio.id !== undefined) {
      this.subscribeToSaveResponse(this.municipioService.update(municipio));
    } else {
      this.subscribeToSaveResponse(this.municipioService.create(municipio));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMunicipio>>): void {
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

  protected updateForm(municipio: IMunicipio): void {
    this.editForm.patchValue({
      id: municipio.id,
      nombre: municipio.nombre,
      codigoDIAN: municipio.codigoDIAN,
    });
  }

  protected createFromForm(): IMunicipio {
    return {
      ...new Municipio(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      codigoDIAN: this.editForm.get(['codigoDIAN'])!.value,
    };
  }
}
