import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITrabajo, Trabajo } from '../trabajo.model';
import { TrabajoService } from '../service/trabajo.service';

@Component({
  selector: 'jhi-trabajo-update',
  templateUrl: './trabajo-update.component.html',
})
export class TrabajoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [],
    cargo: [],
    salario: [],
    salud: [],
    pension: [],
    observacon: [],
    fechaRegistro: [],
  });

  constructor(protected trabajoService: TrabajoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trabajo }) => {
      this.updateForm(trabajo);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const trabajo = this.createFromForm();
    if (trabajo.id !== undefined) {
      this.subscribeToSaveResponse(this.trabajoService.update(trabajo));
    } else {
      this.subscribeToSaveResponse(this.trabajoService.create(trabajo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrabajo>>): void {
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

  protected updateForm(trabajo: ITrabajo): void {
    this.editForm.patchValue({
      id: trabajo.id,
      nombre: trabajo.nombre,
      cargo: trabajo.cargo,
      salario: trabajo.salario,
      salud: trabajo.salud,
      pension: trabajo.pension,
      observacon: trabajo.observacon,
      fechaRegistro: trabajo.fechaRegistro,
    });
  }

  protected createFromForm(): ITrabajo {
    return {
      ...new Trabajo(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      cargo: this.editForm.get(['cargo'])!.value,
      salario: this.editForm.get(['salario'])!.value,
      salud: this.editForm.get(['salud'])!.value,
      pension: this.editForm.get(['pension'])!.value,
      observacon: this.editForm.get(['observacon'])!.value,
      fechaRegistro: this.editForm.get(['fechaRegistro'])!.value,
    };
  }
}
