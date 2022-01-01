import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ISucursal, Sucursal } from '../sucursal.model';
import { SucursalService } from '../service/sucursal.service';
import { IRegimen } from 'app/entities/regimen/regimen.model';
import { RegimenService } from 'app/entities/regimen/service/regimen.service';
import { IMunicipio } from 'app/entities/municipio/municipio.model';
import { MunicipioService } from 'app/entities/municipio/service/municipio.service';
import { ICiudad } from 'app/entities/ciudad/ciudad.model';
import { CiudadService } from 'app/entities/ciudad/service/ciudad.service';
import { IDepartamento } from 'app/entities/departamento/departamento.model';
import { DepartamentoService } from 'app/entities/departamento/service/departamento.service';
import { IPais } from 'app/entities/pais/pais.model';
import { PaisService } from 'app/entities/pais/service/pais.service';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';

@Component({
  selector: 'jhi-sucursal-update',
  templateUrl: './sucursal-update.component.html',
})
export class SucursalUpdateComponent implements OnInit {
  isSaving = false;

  regimenSharedCollection: IRegimen[] = [];
  municipiosSharedCollection: IMunicipio[] = [];
  ciudadsSharedCollection: ICiudad[] = [];
  departamentosSharedCollection: IDepartamento[] = [];
  paisSharedCollection: IPais[] = [];
  empresasSharedCollection: IEmpresa[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [],
    direccion: [],
    indicativo: [],
    telefono: [],
    email: [],
    logo: [],
    resolucionFacturas: [],
    prefijoInicial: [],
    prefijoFinal: [],
    prefijoActual: [],
    descripcion: [],
    fechaRegistro: [],
    regimen: [],
    municipio: [],
    ciudad: [],
    departamento: [],
    pais: [],
    empresa: [],
  });

  constructor(
    protected sucursalService: SucursalService,
    protected regimenService: RegimenService,
    protected municipioService: MunicipioService,
    protected ciudadService: CiudadService,
    protected departamentoService: DepartamentoService,
    protected paisService: PaisService,
    protected empresaService: EmpresaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sucursal }) => {
      if (sucursal.id === undefined) {
        const today = dayjs().startOf('day');
        sucursal.fechaRegistro = today;
      }

      this.updateForm(sucursal);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sucursal = this.createFromForm();
    if (sucursal.id !== undefined) {
      this.subscribeToSaveResponse(this.sucursalService.update(sucursal));
    } else {
      this.subscribeToSaveResponse(this.sucursalService.create(sucursal));
    }
  }

  trackRegimenById(index: number, item: IRegimen): number {
    return item.id!;
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

  trackEmpresaById(index: number, item: IEmpresa): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISucursal>>): void {
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

  protected updateForm(sucursal: ISucursal): void {
    this.editForm.patchValue({
      id: sucursal.id,
      nombre: sucursal.nombre,
      direccion: sucursal.direccion,
      indicativo: sucursal.indicativo,
      telefono: sucursal.telefono,
      email: sucursal.email,
      logo: sucursal.logo,
      resolucionFacturas: sucursal.resolucionFacturas,
      prefijoInicial: sucursal.prefijoInicial,
      prefijoFinal: sucursal.prefijoFinal,
      prefijoActual: sucursal.prefijoActual,
      descripcion: sucursal.descripcion,
      fechaRegistro: sucursal.fechaRegistro ? sucursal.fechaRegistro.format(DATE_TIME_FORMAT) : null,
      regimen: sucursal.regimen,
      municipio: sucursal.municipio,
      ciudad: sucursal.ciudad,
      departamento: sucursal.departamento,
      pais: sucursal.pais,
      empresa: sucursal.empresa,
    });

    this.regimenSharedCollection = this.regimenService.addRegimenToCollectionIfMissing(this.regimenSharedCollection, sucursal.regimen);
    this.municipiosSharedCollection = this.municipioService.addMunicipioToCollectionIfMissing(
      this.municipiosSharedCollection,
      sucursal.municipio
    );
    this.ciudadsSharedCollection = this.ciudadService.addCiudadToCollectionIfMissing(this.ciudadsSharedCollection, sucursal.ciudad);
    this.departamentosSharedCollection = this.departamentoService.addDepartamentoToCollectionIfMissing(
      this.departamentosSharedCollection,
      sucursal.departamento
    );
    this.paisSharedCollection = this.paisService.addPaisToCollectionIfMissing(this.paisSharedCollection, sucursal.pais);
    this.empresasSharedCollection = this.empresaService.addEmpresaToCollectionIfMissing(this.empresasSharedCollection, sucursal.empresa);
  }

  protected loadRelationshipsOptions(): void {
    this.regimenService
      .query()
      .pipe(map((res: HttpResponse<IRegimen[]>) => res.body ?? []))
      .pipe(map((regimen: IRegimen[]) => this.regimenService.addRegimenToCollectionIfMissing(regimen, this.editForm.get('regimen')!.value)))
      .subscribe((regimen: IRegimen[]) => (this.regimenSharedCollection = regimen));

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

    this.empresaService
      .query()
      .pipe(map((res: HttpResponse<IEmpresa[]>) => res.body ?? []))
      .pipe(
        map((empresas: IEmpresa[]) => this.empresaService.addEmpresaToCollectionIfMissing(empresas, this.editForm.get('empresa')!.value))
      )
      .subscribe((empresas: IEmpresa[]) => (this.empresasSharedCollection = empresas));
  }

  protected createFromForm(): ISucursal {
    return {
      ...new Sucursal(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      direccion: this.editForm.get(['direccion'])!.value,
      indicativo: this.editForm.get(['indicativo'])!.value,
      telefono: this.editForm.get(['telefono'])!.value,
      email: this.editForm.get(['email'])!.value,
      logo: this.editForm.get(['logo'])!.value,
      resolucionFacturas: this.editForm.get(['resolucionFacturas'])!.value,
      prefijoInicial: this.editForm.get(['prefijoInicial'])!.value,
      prefijoFinal: this.editForm.get(['prefijoFinal'])!.value,
      prefijoActual: this.editForm.get(['prefijoActual'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      fechaRegistro: this.editForm.get(['fechaRegistro'])!.value
        ? dayjs(this.editForm.get(['fechaRegistro'])!.value, DATE_TIME_FORMAT)
        : undefined,
      regimen: this.editForm.get(['regimen'])!.value,
      municipio: this.editForm.get(['municipio'])!.value,
      ciudad: this.editForm.get(['ciudad'])!.value,
      departamento: this.editForm.get(['departamento'])!.value,
      pais: this.editForm.get(['pais'])!.value,
      empresa: this.editForm.get(['empresa'])!.value,
    };
  }
}
