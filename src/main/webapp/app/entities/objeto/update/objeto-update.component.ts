import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IObjeto, Objeto } from '../objeto.model';
import { ObjetoService } from '../service/objeto.service';
import { ITipoObjeto } from 'app/entities/tipo-objeto/tipo-objeto.model';
import { TipoObjetoService } from 'app/entities/tipo-objeto/service/tipo-objeto.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { ISector } from 'app/entities/sector/sector.model';
import { SectorService } from 'app/entities/sector/service/sector.service';

@Component({
  selector: 'jhi-objeto-update',
  templateUrl: './objeto-update.component.html',
})
export class ObjetoUpdateComponent implements OnInit {
  isSaving = false;

  tipoObjetosCollection: ITipoObjeto[] = [];
  usuariosCollection: IUsuario[] = [];
  sectorsSharedCollection: ISector[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [],
    cantidad: [],
    codigoDian: [],
    detalleObjeto: [],
    vencimiento: [],
    fechaRegistro: [],
    tipoObjeto: [],
    usuario: [],
    sector: [],
  });

  constructor(
    protected objetoService: ObjetoService,
    protected tipoObjetoService: TipoObjetoService,
    protected usuarioService: UsuarioService,
    protected sectorService: SectorService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ objeto }) => {
      if (objeto.id === undefined) {
        const today = dayjs().startOf('day');
        objeto.vencimiento = today;
        objeto.fechaRegistro = today;
      }

      this.updateForm(objeto);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const objeto = this.createFromForm();
    if (objeto.id !== undefined) {
      this.subscribeToSaveResponse(this.objetoService.update(objeto));
    } else {
      this.subscribeToSaveResponse(this.objetoService.create(objeto));
    }
  }

  trackTipoObjetoById(index: number, item: ITipoObjeto): number {
    return item.id!;
  }

  trackUsuarioById(index: number, item: IUsuario): number {
    return item.id!;
  }

  trackSectorById(index: number, item: ISector): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IObjeto>>): void {
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

  protected updateForm(objeto: IObjeto): void {
    this.editForm.patchValue({
      id: objeto.id,
      nombre: objeto.nombre,
      cantidad: objeto.cantidad,
      codigoDian: objeto.codigoDian,
      detalleObjeto: objeto.detalleObjeto,
      vencimiento: objeto.vencimiento ? objeto.vencimiento.format(DATE_TIME_FORMAT) : null,
      fechaRegistro: objeto.fechaRegistro ? objeto.fechaRegistro.format(DATE_TIME_FORMAT) : null,
      tipoObjeto: objeto.tipoObjeto,
      usuario: objeto.usuario,
      sector: objeto.sector,
    });

    this.tipoObjetosCollection = this.tipoObjetoService.addTipoObjetoToCollectionIfMissing(this.tipoObjetosCollection, objeto.tipoObjeto);
    this.usuariosCollection = this.usuarioService.addUsuarioToCollectionIfMissing(this.usuariosCollection, objeto.usuario);
    this.sectorsSharedCollection = this.sectorService.addSectorToCollectionIfMissing(this.sectorsSharedCollection, objeto.sector);
  }

  protected loadRelationshipsOptions(): void {
    this.tipoObjetoService
      .query({ filter: 'objeto-is-null' })
      .pipe(map((res: HttpResponse<ITipoObjeto[]>) => res.body ?? []))
      .pipe(
        map((tipoObjetos: ITipoObjeto[]) =>
          this.tipoObjetoService.addTipoObjetoToCollectionIfMissing(tipoObjetos, this.editForm.get('tipoObjeto')!.value)
        )
      )
      .subscribe((tipoObjetos: ITipoObjeto[]) => (this.tipoObjetosCollection = tipoObjetos));

    this.usuarioService
      .query({ filter: 'objeto-is-null' })
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) => this.usuarioService.addUsuarioToCollectionIfMissing(usuarios, this.editForm.get('usuario')!.value))
      )
      .subscribe((usuarios: IUsuario[]) => (this.usuariosCollection = usuarios));

    this.sectorService
      .query()
      .pipe(map((res: HttpResponse<ISector[]>) => res.body ?? []))
      .pipe(map((sectors: ISector[]) => this.sectorService.addSectorToCollectionIfMissing(sectors, this.editForm.get('sector')!.value)))
      .subscribe((sectors: ISector[]) => (this.sectorsSharedCollection = sectors));
  }

  protected createFromForm(): IObjeto {
    return {
      ...new Objeto(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      cantidad: this.editForm.get(['cantidad'])!.value,
      codigoDian: this.editForm.get(['codigoDian'])!.value,
      detalleObjeto: this.editForm.get(['detalleObjeto'])!.value,
      vencimiento: this.editForm.get(['vencimiento'])!.value
        ? dayjs(this.editForm.get(['vencimiento'])!.value, DATE_TIME_FORMAT)
        : undefined,
      fechaRegistro: this.editForm.get(['fechaRegistro'])!.value
        ? dayjs(this.editForm.get(['fechaRegistro'])!.value, DATE_TIME_FORMAT)
        : undefined,
      tipoObjeto: this.editForm.get(['tipoObjeto'])!.value,
      usuario: this.editForm.get(['usuario'])!.value,
      sector: this.editForm.get(['sector'])!.value,
    };
  }
}
