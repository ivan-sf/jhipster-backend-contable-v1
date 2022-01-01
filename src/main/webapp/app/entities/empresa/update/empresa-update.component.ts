import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IEmpresa, Empresa } from '../empresa.model';
import { EmpresaService } from '../service/empresa.service';
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
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

@Component({
  selector: 'jhi-empresa-update',
  templateUrl: './empresa-update.component.html',
})
export class EmpresaUpdateComponent implements OnInit {
  isSaving = false;

  regimenSharedCollection: IRegimen[] = [];
  municipiosSharedCollection: IMunicipio[] = [];
  ciudadsSharedCollection: ICiudad[] = [];
  departamentosSharedCollection: IDepartamento[] = [];
  paisSharedCollection: IPais[] = [];
  usuariosSharedCollection: IUsuario[] = [];

  editForm = this.fb.group({
    id: [],
    razonSocial: [],
    nombreComercial: [],
    tipoDocumento: [],
    documento: [],
    dv: [],
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
    estado: [],
    regimen: [],
    municipio: [],
    ciudad: [],
    departamento: [],
    pais: [],
    usuarios: [],
  });

  constructor(
    protected empresaService: EmpresaService,
    protected regimenService: RegimenService,
    protected municipioService: MunicipioService,
    protected ciudadService: CiudadService,
    protected departamentoService: DepartamentoService,
    protected paisService: PaisService,
    protected usuarioService: UsuarioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ empresa }) => {
      if (empresa.id === undefined) {
        const today = dayjs().startOf('day');
        empresa.fechaRegistro = today;
      }

      this.updateForm(empresa);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const empresa = this.createFromForm();
    if (empresa.id !== undefined) {
      this.subscribeToSaveResponse(this.empresaService.update(empresa));
    } else {
      this.subscribeToSaveResponse(this.empresaService.create(empresa));
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

  trackUsuarioById(index: number, item: IUsuario): number {
    return item.id!;
  }

  getSelectedUsuario(option: IUsuario, selectedVals?: IUsuario[]): IUsuario {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmpresa>>): void {
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

  protected updateForm(empresa: IEmpresa): void {
    this.editForm.patchValue({
      id: empresa.id,
      razonSocial: empresa.razonSocial,
      nombreComercial: empresa.nombreComercial,
      tipoDocumento: empresa.tipoDocumento,
      documento: empresa.documento,
      dv: empresa.dv,
      direccion: empresa.direccion,
      indicativo: empresa.indicativo,
      telefono: empresa.telefono,
      email: empresa.email,
      logo: empresa.logo,
      resolucionFacturas: empresa.resolucionFacturas,
      prefijoInicial: empresa.prefijoInicial,
      prefijoFinal: empresa.prefijoFinal,
      prefijoActual: empresa.prefijoActual,
      descripcion: empresa.descripcion,
      fechaRegistro: empresa.fechaRegistro ? empresa.fechaRegistro.format(DATE_TIME_FORMAT) : null,
      estado: empresa.estado,
      regimen: empresa.regimen,
      municipio: empresa.municipio,
      ciudad: empresa.ciudad,
      departamento: empresa.departamento,
      pais: empresa.pais,
      usuarios: empresa.usuarios,
    });

    this.regimenSharedCollection = this.regimenService.addRegimenToCollectionIfMissing(this.regimenSharedCollection, empresa.regimen);
    this.municipiosSharedCollection = this.municipioService.addMunicipioToCollectionIfMissing(
      this.municipiosSharedCollection,
      empresa.municipio
    );
    this.ciudadsSharedCollection = this.ciudadService.addCiudadToCollectionIfMissing(this.ciudadsSharedCollection, empresa.ciudad);
    this.departamentosSharedCollection = this.departamentoService.addDepartamentoToCollectionIfMissing(
      this.departamentosSharedCollection,
      empresa.departamento
    );
    this.paisSharedCollection = this.paisService.addPaisToCollectionIfMissing(this.paisSharedCollection, empresa.pais);
    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(
      this.usuariosSharedCollection,
      ...(empresa.usuarios ?? [])
    );
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

    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) =>
          this.usuarioService.addUsuarioToCollectionIfMissing(usuarios, ...(this.editForm.get('usuarios')!.value ?? []))
        )
      )
      .subscribe((usuarios: IUsuario[]) => (this.usuariosSharedCollection = usuarios));
  }

  protected createFromForm(): IEmpresa {
    return {
      ...new Empresa(),
      id: this.editForm.get(['id'])!.value,
      razonSocial: this.editForm.get(['razonSocial'])!.value,
      nombreComercial: this.editForm.get(['nombreComercial'])!.value,
      tipoDocumento: this.editForm.get(['tipoDocumento'])!.value,
      documento: this.editForm.get(['documento'])!.value,
      dv: this.editForm.get(['dv'])!.value,
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
      estado: this.editForm.get(['estado'])!.value,
      regimen: this.editForm.get(['regimen'])!.value,
      municipio: this.editForm.get(['municipio'])!.value,
      ciudad: this.editForm.get(['ciudad'])!.value,
      departamento: this.editForm.get(['departamento'])!.value,
      pais: this.editForm.get(['pais'])!.value,
      usuarios: this.editForm.get(['usuarios'])!.value,
    };
  }
}
