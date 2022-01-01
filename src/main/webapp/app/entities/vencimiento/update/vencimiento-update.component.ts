import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IVencimiento, Vencimiento } from '../vencimiento.model';
import { VencimientoService } from '../service/vencimiento.service';

@Component({
  selector: 'jhi-vencimiento-update',
  templateUrl: './vencimiento-update.component.html',
})
export class VencimientoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    fecha: [],
  });

  constructor(protected vencimientoService: VencimientoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vencimiento }) => {
      if (vencimiento.id === undefined) {
        const today = dayjs().startOf('day');
        vencimiento.fecha = today;
      }

      this.updateForm(vencimiento);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vencimiento = this.createFromForm();
    if (vencimiento.id !== undefined) {
      this.subscribeToSaveResponse(this.vencimientoService.update(vencimiento));
    } else {
      this.subscribeToSaveResponse(this.vencimientoService.create(vencimiento));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVencimiento>>): void {
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

  protected updateForm(vencimiento: IVencimiento): void {
    this.editForm.patchValue({
      id: vencimiento.id,
      fecha: vencimiento.fecha ? vencimiento.fecha.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IVencimiento {
    return {
      ...new Vencimiento(),
      id: this.editForm.get(['id'])!.value,
      fecha: this.editForm.get(['fecha'])!.value ? dayjs(this.editForm.get(['fecha'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
