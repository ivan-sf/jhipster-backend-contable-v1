import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICliente, Cliente } from '../cliente.model';
import { ClienteService } from '../service/cliente.service';
import { IMunicipio } from 'app/entities/municipio/municipio.model';
import { MunicipioService } from 'app/entities/municipio/service/municipio.service';
import { ICiudad } from 'app/entities/ciudad/ciudad.model';
import { CiudadService } from 'app/entities/ciudad/service/ciudad.service';
import { IDepartamento } from 'app/entities/departamento/departamento.model';
import { DepartamentoService } from 'app/entities/departamento/service/departamento.service';
import { IPais } from 'app/entities/pais/pais.model';
import { PaisService } from 'app/entities/pais/service/pais.service';

@Component({
  selector: 'jhi-cliente-update',
  templateUrl: './cliente-update.component.html',
})
export class ClienteUpdateComponent implements OnInit {
  isSaving = false;

  municipiosSharedCollection: IMunicipio[] = [];
  ciudadsSharedCollection: ICiudad[] = [];
  departamentosSharedCollection: IDepartamento[] = [];
  paisSharedCollection: IPais[] = [];

  editForm = this.fb.group({
    id: [],
    nombres: [],
    apellidos: [],
    tipoPersona: [],
    tipoDocumento: [],
    documento: [],
    dv: [],
    estado: [],
    municipio: [],
    ciudad: [],
    departamento: [],
    pais: [],
  });

  constructor(
    protected clienteService: ClienteService,
    protected municipioService: MunicipioService,
    protected ciudadService: CiudadService,
    protected departamentoService: DepartamentoService,
    protected paisService: PaisService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cliente }) => {
      this.updateForm(cliente);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cliente = this.createFromForm();
    if (cliente.id !== undefined) {
      this.subscribeToSaveResponse(this.clienteService.update(cliente));
    } else {
      this.subscribeToSaveResponse(this.clienteService.create(cliente));
    }
  }

  trackMunicipioById(index: number, item: IMunicipio): number {
    return item.id!;
  }

  trackCiudadById(index: number, item: ICiudad): number {
    return item.id!;
  }

  trackDepartamentoById(index: number, item: IDepartamento): number {
    return item.id!;
  }

  trackPaisById(index: number, item: IPais): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICliente>>): void {
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

  protected updateForm(cliente: ICliente): void {
    this.editForm.patchValue({
      id: cliente.id,
      nombres: cliente.nombres,
      apellidos: cliente.apellidos,
      tipoPersona: cliente.tipoPersona,
      tipoDocumento: cliente.tipoDocumento,
      documento: cliente.documento,
      dv: cliente.dv,
      estado: cliente.estado,
      municipio: cliente.municipio,
      ciudad: cliente.ciudad,
      departamento: cliente.departamento,
      pais: cliente.pais,
    });

    this.municipiosSharedCollection = this.municipioService.addMunicipioToCollectionIfMissing(
      this.municipiosSharedCollection,
      cliente.municipio
    );
    this.ciudadsSharedCollection = this.ciudadService.addCiudadToCollectionIfMissing(this.ciudadsSharedCollection, cliente.ciudad);
    this.departamentosSharedCollection = this.departamentoService.addDepartamentoToCollectionIfMissing(
      this.departamentosSharedCollection,
      cliente.departamento
    );
    this.paisSharedCollection = this.paisService.addPaisToCollectionIfMissing(this.paisSharedCollection, cliente.pais);
  }

  protected loadRelationshipsOptions(): void {
    this.municipioService
      .query()
      .pipe(map((res: HttpResponse<IMunicipio[]>) => res.body ?? []))
      .pipe(
        map((municipios: IMunicipio[]) =>
          this.municipioService.addMunicipioToCollectionIfMissing(municipios, this.editForm.get('municipio')!.value)
        )
      )
      .subscribe((municipios: IMunicipio[]) => (this.municipiosSharedCollection = municipios));

    this.ciudadService
      .query()
      .pipe(map((res: HttpResponse<ICiudad[]>) => res.body ?? []))
      .pipe(map((ciudads: ICiudad[]) => this.ciudadService.addCiudadToCollectionIfMissing(ciudads, this.editForm.get('ciudad')!.value)))
      .subscribe((ciudads: ICiudad[]) => (this.ciudadsSharedCollection = ciudads));

    this.departamentoService
      .query()
      .pipe(map((res: HttpResponse<IDepartamento[]>) => res.body ?? []))
      .pipe(
        map((departamentos: IDepartamento[]) =>
          this.departamentoService.addDepartamentoToCollectionIfMissing(departamentos, this.editForm.get('departamento')!.value)
        )
      )
      .subscribe((departamentos: IDepartamento[]) => (this.departamentosSharedCollection = departamentos));

    this.paisService
      .query()
      .pipe(map((res: HttpResponse<IPais[]>) => res.body ?? []))
      .pipe(map((pais: IPais[]) => this.paisService.addPaisToCollectionIfMissing(pais, this.editForm.get('pais')!.value)))
      .subscribe((pais: IPais[]) => (this.paisSharedCollection = pais));
  }

  protected createFromForm(): ICliente {
    return {
      ...new Cliente(),
      id: this.editForm.get(['id'])!.value,
      nombres: this.editForm.get(['nombres'])!.value,
      apellidos: this.editForm.get(['apellidos'])!.value,
      tipoPersona: this.editForm.get(['tipoPersona'])!.value,
      tipoDocumento: this.editForm.get(['tipoDocumento'])!.value,
      documento: this.editForm.get(['documento'])!.value,
      dv: this.editForm.get(['dv'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      municipio: this.editForm.get(['municipio'])!.value,
      ciudad: this.editForm.get(['ciudad'])!.value,
      departamento: this.editForm.get(['departamento'])!.value,
      pais: this.editForm.get(['pais'])!.value,
    };
  }
}
