import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPrecio, Precio } from '../precio.model';
import { PrecioService } from '../service/precio.service';
import { IPaquete } from 'app/entities/paquete/paquete.model';
import { PaqueteService } from 'app/entities/paquete/service/paquete.service';

@Component({
  selector: 'jhi-precio-update',
  templateUrl: './precio-update.component.html',
})
export class PrecioUpdateComponent implements OnInit {
  isSaving = false;

  paquetesSharedCollection: IPaquete[] = [];

  editForm = this.fb.group({
    id: [],
    valorVenta: [],
    valorCompra: [],
    paquete: [],
  });

  constructor(
    protected precioService: PrecioService,
    protected paqueteService: PaqueteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ precio }) => {
      this.updateForm(precio);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const precio = this.createFromForm();
    if (precio.id !== undefined) {
      this.subscribeToSaveResponse(this.precioService.update(precio));
    } else {
      this.subscribeToSaveResponse(this.precioService.create(precio));
    }
  }

  trackPaqueteById(index: number, item: IPaquete): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrecio>>): void {
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

  protected updateForm(precio: IPrecio): void {
    this.editForm.patchValue({
      id: precio.id,
      valorVenta: precio.valorVenta,
      valorCompra: precio.valorCompra,
      paquete: precio.paquete,
    });

    this.paquetesSharedCollection = this.paqueteService.addPaqueteToCollectionIfMissing(this.paquetesSharedCollection, precio.paquete);
  }

  protected loadRelationshipsOptions(): void {
    this.paqueteService
      .query()
      .pipe(map((res: HttpResponse<IPaquete[]>) => res.body ?? []))
      .pipe(
        map((paquetes: IPaquete[]) => this.paqueteService.addPaqueteToCollectionIfMissing(paquetes, this.editForm.get('paquete')!.value))
      )
      .subscribe((paquetes: IPaquete[]) => (this.paquetesSharedCollection = paquetes));
  }

  protected createFromForm(): IPrecio {
    return {
      ...new Precio(),
      id: this.editForm.get(['id'])!.value,
      valorVenta: this.editForm.get(['valorVenta'])!.value,
      valorCompra: this.editForm.get(['valorCompra'])!.value,
      paquete: this.editForm.get(['paquete'])!.value,
    };
  }
}
