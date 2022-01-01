import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEmpleado, Empleado } from '../empleado.model';
import { EmpleadoService } from '../service/empleado.service';
import { ITrabajo } from 'app/entities/trabajo/trabajo.model';
import { TrabajoService } from 'app/entities/trabajo/service/trabajo.service';

@Component({
  selector: 'jhi-empleado-update',
  templateUrl: './empleado-update.component.html',
})
export class EmpleadoUpdateComponent implements OnInit {
  isSaving = false;

  trabajosSharedCollection: ITrabajo[] = [];

  editForm = this.fb.group({
    id: [],
    nombres: [],
    apellidos: [],
    tipoPersona: [],
    tipoDocumento: [],
    documento: [],
    dv: [],
    estado: [],
    trabajo: [],
  });

  constructor(
    protected empleadoService: EmpleadoService,
    protected trabajoService: TrabajoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ empleado }) => {
      this.updateForm(empleado);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const empleado = this.createFromForm();
    if (empleado.id !== undefined) {
      this.subscribeToSaveResponse(this.empleadoService.update(empleado));
    } else {
      this.subscribeToSaveResponse(this.empleadoService.create(empleado));
    }
  }

  trackTrabajoById(index: number, item: ITrabajo): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmpleado>>): void {
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

  protected updateForm(empleado: IEmpleado): void {
    this.editForm.patchValue({
      id: empleado.id,
      nombres: empleado.nombres,
      apellidos: empleado.apellidos,
      tipoPersona: empleado.tipoPersona,
      tipoDocumento: empleado.tipoDocumento,
      documento: empleado.documento,
      dv: empleado.dv,
      estado: empleado.estado,
      trabajo: empleado.trabajo,
    });

    this.trabajosSharedCollection = this.trabajoService.addTrabajoToCollectionIfMissing(this.trabajosSharedCollection, empleado.trabajo);
  }

  protected loadRelationshipsOptions(): void {
    this.trabajoService
      .query()
      .pipe(map((res: HttpResponse<ITrabajo[]>) => res.body ?? []))
      .pipe(
        map((trabajos: ITrabajo[]) => this.trabajoService.addTrabajoToCollectionIfMissing(trabajos, this.editForm.get('trabajo')!.value))
      )
      .subscribe((trabajos: ITrabajo[]) => (this.trabajosSharedCollection = trabajos));
  }

  protected createFromForm(): IEmpleado {
    return {
      ...new Empleado(),
      id: this.editForm.get(['id'])!.value,
      nombres: this.editForm.get(['nombres'])!.value,
      apellidos: this.editForm.get(['apellidos'])!.value,
      tipoPersona: this.editForm.get(['tipoPersona'])!.value,
      tipoDocumento: this.editForm.get(['tipoDocumento'])!.value,
      documento: this.editForm.get(['documento'])!.value,
      dv: this.editForm.get(['dv'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      trabajo: this.editForm.get(['trabajo'])!.value,
    };
  }
}
