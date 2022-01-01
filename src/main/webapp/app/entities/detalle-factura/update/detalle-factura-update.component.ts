import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDetalleFactura, DetalleFactura } from '../detalle-factura.model';
import { DetalleFacturaService } from '../service/detalle-factura.service';
import { IObjeto } from 'app/entities/objeto/objeto.model';
import { ObjetoService } from 'app/entities/objeto/service/objeto.service';
import { IFactura } from 'app/entities/factura/factura.model';
import { FacturaService } from 'app/entities/factura/service/factura.service';

@Component({
  selector: 'jhi-detalle-factura-update',
  templateUrl: './detalle-factura-update.component.html',
})
export class DetalleFacturaUpdateComponent implements OnInit {
  isSaving = false;

  objetosCollection: IObjeto[] = [];
  facturasSharedCollection: IFactura[] = [];

  editForm = this.fb.group({
    id: [],
    precio: [],
    cantidad: [],
    total: [],
    iva: [],
    valorImpuesto: [],
    estado: [],
    objeto: [],
    factura: [],
  });

  constructor(
    protected detalleFacturaService: DetalleFacturaService,
    protected objetoService: ObjetoService,
    protected facturaService: FacturaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ detalleFactura }) => {
      this.updateForm(detalleFactura);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const detalleFactura = this.createFromForm();
    if (detalleFactura.id !== undefined) {
      this.subscribeToSaveResponse(this.detalleFacturaService.update(detalleFactura));
    } else {
      this.subscribeToSaveResponse(this.detalleFacturaService.create(detalleFactura));
    }
  }

  trackObjetoById(index: number, item: IObjeto): number {
    return item.id!;
  }

  trackFacturaById(index: number, item: IFactura): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDetalleFactura>>): void {
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

  protected updateForm(detalleFactura: IDetalleFactura): void {
    this.editForm.patchValue({
      id: detalleFactura.id,
      precio: detalleFactura.precio,
      cantidad: detalleFactura.cantidad,
      total: detalleFactura.total,
      iva: detalleFactura.iva,
      valorImpuesto: detalleFactura.valorImpuesto,
      estado: detalleFactura.estado,
      objeto: detalleFactura.objeto,
      factura: detalleFactura.factura,
    });

    this.objetosCollection = this.objetoService.addObjetoToCollectionIfMissing(this.objetosCollection, detalleFactura.objeto);
    this.facturasSharedCollection = this.facturaService.addFacturaToCollectionIfMissing(
      this.facturasSharedCollection,
      detalleFactura.factura
    );
  }

  protected loadRelationshipsOptions(): void {
    this.objetoService
      .query({ filter: 'detallefactura-is-null' })
      .pipe(map((res: HttpResponse<IObjeto[]>) => res.body ?? []))
      .pipe(map((objetos: IObjeto[]) => this.objetoService.addObjetoToCollectionIfMissing(objetos, this.editForm.get('objeto')!.value)))
      .subscribe((objetos: IObjeto[]) => (this.objetosCollection = objetos));

    this.facturaService
      .query()
      .pipe(map((res: HttpResponse<IFactura[]>) => res.body ?? []))
      .pipe(
        map((facturas: IFactura[]) => this.facturaService.addFacturaToCollectionIfMissing(facturas, this.editForm.get('factura')!.value))
      )
      .subscribe((facturas: IFactura[]) => (this.facturasSharedCollection = facturas));
  }

  protected createFromForm(): IDetalleFactura {
    return {
      ...new DetalleFactura(),
      id: this.editForm.get(['id'])!.value,
      precio: this.editForm.get(['precio'])!.value,
      cantidad: this.editForm.get(['cantidad'])!.value,
      total: this.editForm.get(['total'])!.value,
      iva: this.editForm.get(['iva'])!.value,
      valorImpuesto: this.editForm.get(['valorImpuesto'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      objeto: this.editForm.get(['objeto'])!.value,
      factura: this.editForm.get(['factura'])!.value,
    };
  }
}
