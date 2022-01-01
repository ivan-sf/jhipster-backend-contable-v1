import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICodigo, Codigo } from '../codigo.model';
import { CodigoService } from '../service/codigo.service';
import { IPaquete } from 'app/entities/paquete/paquete.model';
import { PaqueteService } from 'app/entities/paquete/service/paquete.service';
import { ILote } from 'app/entities/lote/lote.model';
import { LoteService } from 'app/entities/lote/service/lote.service';
import { IVencimiento } from 'app/entities/vencimiento/vencimiento.model';
import { VencimientoService } from 'app/entities/vencimiento/service/vencimiento.service';
import { IObjeto } from 'app/entities/objeto/objeto.model';
import { ObjetoService } from 'app/entities/objeto/service/objeto.service';

@Component({
  selector: 'jhi-codigo-update',
  templateUrl: './codigo-update.component.html',
})
export class CodigoUpdateComponent implements OnInit {
  isSaving = false;

  paquetesCollection: IPaquete[] = [];
  lotesCollection: ILote[] = [];
  vencimientosCollection: IVencimiento[] = [];
  objetosSharedCollection: IObjeto[] = [];

  editForm = this.fb.group({
    id: [],
    barCode: [],
    qrCode: [],
    cantidad: [],
    fechaRegistro: [],
    paquete: [],
    lote: [],
    vencimiento: [],
    objeto: [],
  });

  constructor(
    protected codigoService: CodigoService,
    protected paqueteService: PaqueteService,
    protected loteService: LoteService,
    protected vencimientoService: VencimientoService,
    protected objetoService: ObjetoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ codigo }) => {
      if (codigo.id === undefined) {
        const today = dayjs().startOf('day');
        codigo.fechaRegistro = today;
      }

      this.updateForm(codigo);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const codigo = this.createFromForm();
    if (codigo.id !== undefined) {
      this.subscribeToSaveResponse(this.codigoService.update(codigo));
    } else {
      this.subscribeToSaveResponse(this.codigoService.create(codigo));
    }
  }

  trackPaqueteById(index: number, item: IPaquete): number {
    return item.id!;
  }

  trackLoteById(index: number, item: ILote): number {
    return item.id!;
  }

  trackVencimientoById(index: number, item: IVencimiento): number {
    return item.id!;
  }

  trackObjetoById(index: number, item: IObjeto): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICodigo>>): void {
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

  protected updateForm(codigo: ICodigo): void {
    this.editForm.patchValue({
      id: codigo.id,
      barCode: codigo.barCode,
      qrCode: codigo.qrCode,
      cantidad: codigo.cantidad,
      fechaRegistro: codigo.fechaRegistro ? codigo.fechaRegistro.format(DATE_TIME_FORMAT) : null,
      paquete: codigo.paquete,
      lote: codigo.lote,
      vencimiento: codigo.vencimiento,
      objeto: codigo.objeto,
    });

    this.paquetesCollection = this.paqueteService.addPaqueteToCollectionIfMissing(this.paquetesCollection, codigo.paquete);
    this.lotesCollection = this.loteService.addLoteToCollectionIfMissing(this.lotesCollection, codigo.lote);
    this.vencimientosCollection = this.vencimientoService.addVencimientoToCollectionIfMissing(
      this.vencimientosCollection,
      codigo.vencimiento
    );
    this.objetosSharedCollection = this.objetoService.addObjetoToCollectionIfMissing(this.objetosSharedCollection, codigo.objeto);
  }

  protected loadRelationshipsOptions(): void {
    this.paqueteService
      .query({ filter: 'codigo-is-null' })
      .pipe(map((res: HttpResponse<IPaquete[]>) => res.body ?? []))
      .pipe(
        map((paquetes: IPaquete[]) => this.paqueteService.addPaqueteToCollectionIfMissing(paquetes, this.editForm.get('paquete')!.value))
      )
      .subscribe((paquetes: IPaquete[]) => (this.paquetesCollection = paquetes));

    this.loteService
      .query({ filter: 'codigo-is-null' })
      .pipe(map((res: HttpResponse<ILote[]>) => res.body ?? []))
      .pipe(map((lotes: ILote[]) => this.loteService.addLoteToCollectionIfMissing(lotes, this.editForm.get('lote')!.value)))
      .subscribe((lotes: ILote[]) => (this.lotesCollection = lotes));

    this.vencimientoService
      .query({ filter: 'codigo-is-null' })
      .pipe(map((res: HttpResponse<IVencimiento[]>) => res.body ?? []))
      .pipe(
        map((vencimientos: IVencimiento[]) =>
          this.vencimientoService.addVencimientoToCollectionIfMissing(vencimientos, this.editForm.get('vencimiento')!.value)
        )
      )
      .subscribe((vencimientos: IVencimiento[]) => (this.vencimientosCollection = vencimientos));

    this.objetoService
      .query()
      .pipe(map((res: HttpResponse<IObjeto[]>) => res.body ?? []))
      .pipe(map((objetos: IObjeto[]) => this.objetoService.addObjetoToCollectionIfMissing(objetos, this.editForm.get('objeto')!.value)))
      .subscribe((objetos: IObjeto[]) => (this.objetosSharedCollection = objetos));
  }

  protected createFromForm(): ICodigo {
    return {
      ...new Codigo(),
      id: this.editForm.get(['id'])!.value,
      barCode: this.editForm.get(['barCode'])!.value,
      qrCode: this.editForm.get(['qrCode'])!.value,
      cantidad: this.editForm.get(['cantidad'])!.value,
      fechaRegistro: this.editForm.get(['fechaRegistro'])!.value
        ? dayjs(this.editForm.get(['fechaRegistro'])!.value, DATE_TIME_FORMAT)
        : undefined,
      paquete: this.editForm.get(['paquete'])!.value,
      lote: this.editForm.get(['lote'])!.value,
      vencimiento: this.editForm.get(['vencimiento'])!.value,
      objeto: this.editForm.get(['objeto'])!.value,
    };
  }
}
