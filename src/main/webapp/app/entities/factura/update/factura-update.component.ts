import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IFactura, Factura } from '../factura.model';
import { FacturaService } from '../service/factura.service';
import { ISucursal } from 'app/entities/sucursal/sucursal.model';
import { SucursalService } from 'app/entities/sucursal/service/sucursal.service';
import { ITipoFactura } from 'app/entities/tipo-factura/tipo-factura.model';
import { TipoFacturaService } from 'app/entities/tipo-factura/service/tipo-factura.service';
import { INotaContable } from 'app/entities/nota-contable/nota-contable.model';
import { NotaContableService } from 'app/entities/nota-contable/service/nota-contable.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { EmpleadoService } from 'app/entities/empleado/service/empleado.service';

@Component({
  selector: 'jhi-factura-update',
  templateUrl: './factura-update.component.html',
})
export class FacturaUpdateComponent implements OnInit {
  isSaving = false;

  sucursalsCollection: ISucursal[] = [];
  tipoFacturasCollection: ITipoFactura[] = [];
  notaContablesCollection: INotaContable[] = [];
  clientesSharedCollection: ICliente[] = [];
  empleadosSharedCollection: IEmpleado[] = [];

  editForm = this.fb.group({
    id: [],
    numero: [],
    caja: [],
    estado: [],
    iva19: [],
    baseIva19: [],
    iva5: [],
    baseIva5: [],
    iva0: [],
    baseIva0: [],
    total: [],
    pago: [],
    saldo: [],
    fechaRegistro: [],
    fechaActualizacion: [],
    sucursal: [],
    tipoFactura: [],
    notaContable: [],
    cliente: [],
    empleadp: [],
  });

  constructor(
    protected facturaService: FacturaService,
    protected sucursalService: SucursalService,
    protected tipoFacturaService: TipoFacturaService,
    protected notaContableService: NotaContableService,
    protected clienteService: ClienteService,
    protected empleadoService: EmpleadoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ factura }) => {
      if (factura.id === undefined) {
        const today = dayjs().startOf('day');
        factura.fechaRegistro = today;
        factura.fechaActualizacion = today;
      }

      this.updateForm(factura);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const factura = this.createFromForm();
    if (factura.id !== undefined) {
      this.subscribeToSaveResponse(this.facturaService.update(factura));
    } else {
      this.subscribeToSaveResponse(this.facturaService.create(factura));
    }
  }

  trackSucursalById(index: number, item: ISucursal): number {
    return item.id!;
  }

  trackTipoFacturaById(index: number, item: ITipoFactura): number {
    return item.id!;
  }

  trackNotaContableById(index: number, item: INotaContable): number {
    return item.id!;
  }

  trackClienteById(index: number, item: ICliente): number {
    return item.id!;
  }

  trackEmpleadoById(index: number, item: IEmpleado): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFactura>>): void {
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

  protected updateForm(factura: IFactura): void {
    this.editForm.patchValue({
      id: factura.id,
      numero: factura.numero,
      caja: factura.caja,
      estado: factura.estado,
      iva19: factura.iva19,
      baseIva19: factura.baseIva19,
      iva5: factura.iva5,
      baseIva5: factura.baseIva5,
      iva0: factura.iva0,
      baseIva0: factura.baseIva0,
      total: factura.total,
      pago: factura.pago,
      saldo: factura.saldo,
      fechaRegistro: factura.fechaRegistro ? factura.fechaRegistro.format(DATE_TIME_FORMAT) : null,
      fechaActualizacion: factura.fechaActualizacion ? factura.fechaActualizacion.format(DATE_TIME_FORMAT) : null,
      sucursal: factura.sucursal,
      tipoFactura: factura.tipoFactura,
      notaContable: factura.notaContable,
      cliente: factura.cliente,
      empleadp: factura.empleadp,
    });

    this.sucursalsCollection = this.sucursalService.addSucursalToCollectionIfMissing(this.sucursalsCollection, factura.sucursal);
    this.tipoFacturasCollection = this.tipoFacturaService.addTipoFacturaToCollectionIfMissing(
      this.tipoFacturasCollection,
      factura.tipoFactura
    );
    this.notaContablesCollection = this.notaContableService.addNotaContableToCollectionIfMissing(
      this.notaContablesCollection,
      factura.notaContable
    );
    this.clientesSharedCollection = this.clienteService.addClienteToCollectionIfMissing(this.clientesSharedCollection, factura.cliente);
    this.empleadosSharedCollection = this.empleadoService.addEmpleadoToCollectionIfMissing(
      this.empleadosSharedCollection,
      factura.empleadp
    );
  }

  protected loadRelationshipsOptions(): void {
    this.sucursalService
      .query({ filter: 'factura-is-null' })
      .pipe(map((res: HttpResponse<ISucursal[]>) => res.body ?? []))
      .pipe(
        map((sucursals: ISucursal[]) =>
          this.sucursalService.addSucursalToCollectionIfMissing(sucursals, this.editForm.get('sucursal')!.value)
        )
      )
      .subscribe((sucursals: ISucursal[]) => (this.sucursalsCollection = sucursals));

    this.tipoFacturaService
      .query({ filter: 'factura-is-null' })
      .pipe(map((res: HttpResponse<ITipoFactura[]>) => res.body ?? []))
      .pipe(
        map((tipoFacturas: ITipoFactura[]) =>
          this.tipoFacturaService.addTipoFacturaToCollectionIfMissing(tipoFacturas, this.editForm.get('tipoFactura')!.value)
        )
      )
      .subscribe((tipoFacturas: ITipoFactura[]) => (this.tipoFacturasCollection = tipoFacturas));

    this.notaContableService
      .query({ filter: 'factura-is-null' })
      .pipe(map((res: HttpResponse<INotaContable[]>) => res.body ?? []))
      .pipe(
        map((notaContables: INotaContable[]) =>
          this.notaContableService.addNotaContableToCollectionIfMissing(notaContables, this.editForm.get('notaContable')!.value)
        )
      )
      .subscribe((notaContables: INotaContable[]) => (this.notaContablesCollection = notaContables));

    this.clienteService
      .query()
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(
        map((clientes: ICliente[]) => this.clienteService.addClienteToCollectionIfMissing(clientes, this.editForm.get('cliente')!.value))
      )
      .subscribe((clientes: ICliente[]) => (this.clientesSharedCollection = clientes));

    this.empleadoService
      .query()
      .pipe(map((res: HttpResponse<IEmpleado[]>) => res.body ?? []))
      .pipe(
        map((empleados: IEmpleado[]) =>
          this.empleadoService.addEmpleadoToCollectionIfMissing(empleados, this.editForm.get('empleadp')!.value)
        )
      )
      .subscribe((empleados: IEmpleado[]) => (this.empleadosSharedCollection = empleados));
  }

  protected createFromForm(): IFactura {
    return {
      ...new Factura(),
      id: this.editForm.get(['id'])!.value,
      numero: this.editForm.get(['numero'])!.value,
      caja: this.editForm.get(['caja'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      iva19: this.editForm.get(['iva19'])!.value,
      baseIva19: this.editForm.get(['baseIva19'])!.value,
      iva5: this.editForm.get(['iva5'])!.value,
      baseIva5: this.editForm.get(['baseIva5'])!.value,
      iva0: this.editForm.get(['iva0'])!.value,
      baseIva0: this.editForm.get(['baseIva0'])!.value,
      total: this.editForm.get(['total'])!.value,
      pago: this.editForm.get(['pago'])!.value,
      saldo: this.editForm.get(['saldo'])!.value,
      fechaRegistro: this.editForm.get(['fechaRegistro'])!.value
        ? dayjs(this.editForm.get(['fechaRegistro'])!.value, DATE_TIME_FORMAT)
        : undefined,
      fechaActualizacion: this.editForm.get(['fechaActualizacion'])!.value
        ? dayjs(this.editForm.get(['fechaActualizacion'])!.value, DATE_TIME_FORMAT)
        : undefined,
      sucursal: this.editForm.get(['sucursal'])!.value,
      tipoFactura: this.editForm.get(['tipoFactura'])!.value,
      notaContable: this.editForm.get(['notaContable'])!.value,
      cliente: this.editForm.get(['cliente'])!.value,
      empleadp: this.editForm.get(['empleadp'])!.value,
    };
  }
}
