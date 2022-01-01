import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDetallesObjeto, DetallesObjeto } from '../detalles-objeto.model';
import { DetallesObjetoService } from '../service/detalles-objeto.service';

@Component({
  selector: 'jhi-detalles-objeto-update',
  templateUrl: './detalles-objeto-update.component.html',
})
export class DetallesObjetoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [],
    descripcion: [],
    fechaRegistro: [],
  });

  constructor(
    protected detallesObjetoService: DetallesObjetoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ detallesObjeto }) => {
      if (detallesObjeto.id === undefined) {
        const today = dayjs().startOf('day');
        detallesObjeto.fechaRegistro = today;
      }

      this.updateForm(detallesObjeto);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const detallesObjeto = this.createFromForm();
    if (detallesObjeto.id !== undefined) {
      this.subscribeToSaveResponse(this.detallesObjetoService.update(detallesObjeto));
    } else {
      this.subscribeToSaveResponse(this.detallesObjetoService.create(detallesObjeto));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDetallesObjeto>>): void {
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

  protected updateForm(detallesObjeto: IDetallesObjeto): void {
    this.editForm.patchValue({
      id: detallesObjeto.id,
      nombre: detallesObjeto.nombre,
      descripcion: detallesObjeto.descripcion,
      fechaRegistro: detallesObjeto.fechaRegistro ? detallesObjeto.fechaRegistro.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IDetallesObjeto {
    return {
      ...new DetallesObjeto(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      fechaRegistro: this.editForm.get(['fechaRegistro'])!.value
        ? dayjs(this.editForm.get(['fechaRegistro'])!.value, DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
