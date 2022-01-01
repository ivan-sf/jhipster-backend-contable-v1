import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IInventario, Inventario } from '../inventario.model';
import { InventarioService } from '../service/inventario.service';

@Component({
  selector: 'jhi-inventario-update',
  templateUrl: './inventario-update.component.html',
})
export class InventarioUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [],
    valor: [],
    fechaRegistro: [],
  });

  constructor(protected inventarioService: InventarioService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inventario }) => {
      if (inventario.id === undefined) {
        const today = dayjs().startOf('day');
        inventario.fechaRegistro = today;
      }

      this.updateForm(inventario);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inventario = this.createFromForm();
    if (inventario.id !== undefined) {
      this.subscribeToSaveResponse(this.inventarioService.update(inventario));
    } else {
      this.subscribeToSaveResponse(this.inventarioService.create(inventario));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInventario>>): void {
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

  protected updateForm(inventario: IInventario): void {
    this.editForm.patchValue({
      id: inventario.id,
      nombre: inventario.nombre,
      valor: inventario.valor,
      fechaRegistro: inventario.fechaRegistro ? inventario.fechaRegistro.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IInventario {
    return {
      ...new Inventario(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      valor: this.editForm.get(['valor'])!.value,
      fechaRegistro: this.editForm.get(['fechaRegistro'])!.value
        ? dayjs(this.editForm.get(['fechaRegistro'])!.value, DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
