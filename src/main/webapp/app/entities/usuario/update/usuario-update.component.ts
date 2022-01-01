import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IUsuario, Usuario } from '../usuario.model';
import { UsuarioService } from '../service/usuario.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { EmpleadoService } from 'app/entities/empleado/service/empleado.service';
import { IMunicipio } from 'app/entities/municipio/municipio.model';
import { MunicipioService } from 'app/entities/municipio/service/municipio.service';
import { ICiudad } from 'app/entities/ciudad/ciudad.model';
import { CiudadService } from 'app/entities/ciudad/service/ciudad.service';
import { IDepartamento } from 'app/entities/departamento/departamento.model';
import { DepartamentoService } from 'app/entities/departamento/service/departamento.service';
import { IPais } from 'app/entities/pais/pais.model';
import { PaisService } from 'app/entities/pais/service/pais.service';

@Component({
  selector: 'jhi-usuario-update',
  templateUrl: './usuario-update.component.html',
})
export class UsuarioUpdateComponent implements OnInit {
  isSaving = false;

  clientesCollection: ICliente[] = [];
  empleadosCollection: IEmpleado[] = [];
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
    rol: [],
    rut: [],
    nombreComercial: [],
    nit: [],
    direccion: [],
    indicativo: [],
    telefono: [],
    email: [],
    genero: [],
    edad: [],
    foto: [],
    descripcion: [],
    fechaRegistro: [],
    cliente: [],
    empleado: [],
    municipio: [],
    ciudad: [],
    departamento: [],
    pais: [],
  });

  constructor(
    protected usuarioService: UsuarioService,
    protected clienteService: ClienteService,
    protected empleadoService: EmpleadoService,
    protected municipioService: MunicipioService,
    protected ciudadService: CiudadService,
    protected departamentoService: DepartamentoService,
    protected paisService: PaisService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ usuario }) => {
      if (usuario.id === undefined) {
        const today = dayjs().startOf('day');
        usuario.edad = today;
        usuario.fechaRegistro = today;
      }

      this.updateForm(usuario);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const usuario = this.createFromForm();
    if (usuario.id !== undefined) {
      this.subscribeToSaveResponse(this.usuarioService.update(usuario));
    } else {
      this.subscribeToSaveResponse(this.usuarioService.create(usuario));
    }
  }

  trackClienteById(index: number, item: ICliente): number {
    return item.id!;
  }

  trackEmpleadoById(index: number, item: IEmpleado): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUsuario>>): void {
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

  protected updateForm(usuario: IUsuario): void {
    this.editForm.patchValue({
      id: usuario.id,
      nombres: usuario.nombres,
      apellidos: usuario.apellidos,
      tipoPersona: usuario.tipoPersona,
      tipoDocumento: usuario.tipoDocumento,
      documento: usuario.documento,
      dv: usuario.dv,
      estado: usuario.estado,
      rol: usuario.rol,
      rut: usuario.rut,
      nombreComercial: usuario.nombreComercial,
      nit: usuario.nit,
      direccion: usuario.direccion,
      indicativo: usuario.indicativo,
      telefono: usuario.telefono,
      email: usuario.email,
      genero: usuario.genero,
      edad: usuario.edad ? usuario.edad.format(DATE_TIME_FORMAT) : null,
      foto: usuario.foto,
      descripcion: usuario.descripcion,
      fechaRegistro: usuario.fechaRegistro ? usuario.fechaRegistro.format(DATE_TIME_FORMAT) : null,
      cliente: usuario.cliente,
      empleado: usuario.empleado,
      municipio: usuario.municipio,
      ciudad: usuario.ciudad,
      departamento: usuario.departamento,
      pais: usuario.pais,
    });

    this.clientesCollection = this.clienteService.addClienteToCollectionIfMissing(this.clientesCollection, usuario.cliente);
    this.empleadosCollection = this.empleadoService.addEmpleadoToCollectionIfMissing(this.empleadosCollection, usuario.empleado);
    this.municipiosSharedCollection = this.municipioService.addMunicipioToCollectionIfMissing(
      this.municipiosSharedCollection,
      usuario.municipio
    );
    this.ciudadsSharedCollection = this.ciudadService.addCiudadToCollectionIfMissing(this.ciudadsSharedCollection, usuario.ciudad);
    this.departamentosSharedCollection = this.departamentoService.addDepartamentoToCollectionIfMissing(
      this.departamentosSharedCollection,
      usuario.departamento
    );
    this.paisSharedCollection = this.paisService.addPaisToCollectionIfMissing(this.paisSharedCollection, usuario.pais);
  }

  protected loadRelationshipsOptions(): void {
    this.clienteService
      .query({ filter: 'usuario-is-null' })
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(
        map((clientes: ICliente[]) => this.clienteService.addClienteToCollectionIfMissing(clientes, this.editForm.get('cliente')!.value))
      )
      .subscribe((clientes: ICliente[]) => (this.clientesCollection = clientes));

    this.empleadoService
      .query({ filter: 'usuario-is-null' })
      .pipe(map((res: HttpResponse<IEmpleado[]>) => res.body ?? []))
      .pipe(
        map((empleados: IEmpleado[]) =>
          this.empleadoService.addEmpleadoToCollectionIfMissing(empleados, this.editForm.get('empleado')!.value)
        )
      )
      .subscribe((empleados: IEmpleado[]) => (this.empleadosCollection = empleados));

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

  protected createFromForm(): IUsuario {
    return {
      ...new Usuario(),
      id: this.editForm.get(['id'])!.value,
      nombres: this.editForm.get(['nombres'])!.value,
      apellidos: this.editForm.get(['apellidos'])!.value,
      tipoPersona: this.editForm.get(['tipoPersona'])!.value,
      tipoDocumento: this.editForm.get(['tipoDocumento'])!.value,
      documento: this.editForm.get(['documento'])!.value,
      dv: this.editForm.get(['dv'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      rol: this.editForm.get(['rol'])!.value,
      rut: this.editForm.get(['rut'])!.value,
      nombreComercial: this.editForm.get(['nombreComercial'])!.value,
      nit: this.editForm.get(['nit'])!.value,
      direccion: this.editForm.get(['direccion'])!.value,
      indicativo: this.editForm.get(['indicativo'])!.value,
      telefono: this.editForm.get(['telefono'])!.value,
      email: this.editForm.get(['email'])!.value,
      genero: this.editForm.get(['genero'])!.value,
      edad: this.editForm.get(['edad'])!.value ? dayjs(this.editForm.get(['edad'])!.value, DATE_TIME_FORMAT) : undefined,
      foto: this.editForm.get(['foto'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      fechaRegistro: this.editForm.get(['fechaRegistro'])!.value
        ? dayjs(this.editForm.get(['fechaRegistro'])!.value, DATE_TIME_FORMAT)
        : undefined,
      cliente: this.editForm.get(['cliente'])!.value,
      empleado: this.editForm.get(['empleado'])!.value,
      municipio: this.editForm.get(['municipio'])!.value,
      ciudad: this.editForm.get(['ciudad'])!.value,
      departamento: this.editForm.get(['departamento'])!.value,
      pais: this.editForm.get(['pais'])!.value,
    };
  }
}
