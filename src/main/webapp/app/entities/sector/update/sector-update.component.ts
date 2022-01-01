import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ISector, Sector } from '../sector.model';
import { SectorService } from '../service/sector.service';
import { IInventario } from 'app/entities/inventario/inventario.model';
import { InventarioService } from 'app/entities/inventario/service/inventario.service';

@Component({
  selector: 'jhi-sector-update',
  templateUrl: './sector-update.component.html',
})
export class SectorUpdateComponent implements OnInit {
  isSaving = false;

  inventariosSharedCollection: IInventario[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [],
    valor: [],
    fechaRegistro: [],
    inventario: [],
  });

  constructor(
    protected sectorService: SectorService,
    protected inventarioService: InventarioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sector }) => {
      if (sector.id === undefined) {
        const today = dayjs().startOf('day');
        sector.fechaRegistro = today;
      }

      this.updateForm(sector);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sector = this.createFromForm();
    if (sector.id !== undefined) {
      this.subscribeToSaveResponse(this.sectorService.update(sector));
    } else {
      this.subscribeToSaveResponse(this.sectorService.create(sector));
    }
  }

  trackInventarioById(index: number, item: IInventario): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISector>>): void {
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

  protected updateForm(sector: ISector): void {
    this.editForm.patchValue({
      id: sector.id,
      nombre: sector.nombre,
      valor: sector.valor,
      fechaRegistro: sector.fechaRegistro ? sector.fechaRegistro.format(DATE_TIME_FORMAT) : null,
      inventario: sector.inventario,
    });

    this.inventariosSharedCollection = this.inventarioService.addInventarioToCollectionIfMissing(
      this.inventariosSharedCollection,
      sector.inventario
    );
  }

  protected loadRelationshipsOptions(): void {
    this.inventarioService
      .query()
      .pipe(map((res: HttpResponse<IInventario[]>) => res.body ?? []))
      .pipe(
        map((inventarios: IInventario[]) =>
          this.inventarioService.addInventarioToCollectionIfMissing(inventarios, this.editForm.get('inventario')!.value)
        )
      )
      .subscribe((inventarios: IInventario[]) => (this.inventariosSharedCollection = inventarios));
  }

  protected createFromForm(): ISector {
    return {
      ...new Sector(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      valor: this.editForm.get(['valor'])!.value,
      fechaRegistro: this.editForm.get(['fechaRegistro'])!.value
        ? dayjs(this.editForm.get(['fechaRegistro'])!.value, DATE_TIME_FORMAT)
        : undefined,
      inventario: this.editForm.get(['inventario'])!.value,
    };
  }
}
