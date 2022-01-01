jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ObjetoService } from '../service/objeto.service';
import { IObjeto, Objeto } from '../objeto.model';
import { ITipoObjeto } from 'app/entities/tipo-objeto/tipo-objeto.model';
import { TipoObjetoService } from 'app/entities/tipo-objeto/service/tipo-objeto.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { ISector } from 'app/entities/sector/sector.model';
import { SectorService } from 'app/entities/sector/service/sector.service';

import { ObjetoUpdateComponent } from './objeto-update.component';

describe('Objeto Management Update Component', () => {
  let comp: ObjetoUpdateComponent;
  let fixture: ComponentFixture<ObjetoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let objetoService: ObjetoService;
  let tipoObjetoService: TipoObjetoService;
  let usuarioService: UsuarioService;
  let sectorService: SectorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ObjetoUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ObjetoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ObjetoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    objetoService = TestBed.inject(ObjetoService);
    tipoObjetoService = TestBed.inject(TipoObjetoService);
    usuarioService = TestBed.inject(UsuarioService);
    sectorService = TestBed.inject(SectorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call tipoObjeto query and add missing value', () => {
      const objeto: IObjeto = { id: 456 };
      const tipoObjeto: ITipoObjeto = { id: 16919 };
      objeto.tipoObjeto = tipoObjeto;

      const tipoObjetoCollection: ITipoObjeto[] = [{ id: 84163 }];
      jest.spyOn(tipoObjetoService, 'query').mockReturnValue(of(new HttpResponse({ body: tipoObjetoCollection })));
      const expectedCollection: ITipoObjeto[] = [tipoObjeto, ...tipoObjetoCollection];
      jest.spyOn(tipoObjetoService, 'addTipoObjetoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ objeto });
      comp.ngOnInit();

      expect(tipoObjetoService.query).toHaveBeenCalled();
      expect(tipoObjetoService.addTipoObjetoToCollectionIfMissing).toHaveBeenCalledWith(tipoObjetoCollection, tipoObjeto);
      expect(comp.tipoObjetosCollection).toEqual(expectedCollection);
    });

    it('Should call usuario query and add missing value', () => {
      const objeto: IObjeto = { id: 456 };
      const usuario: IUsuario = { id: 19548 };
      objeto.usuario = usuario;

      const usuarioCollection: IUsuario[] = [{ id: 26800 }];
      jest.spyOn(usuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioCollection })));
      const expectedCollection: IUsuario[] = [usuario, ...usuarioCollection];
      jest.spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ objeto });
      comp.ngOnInit();

      expect(usuarioService.query).toHaveBeenCalled();
      expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(usuarioCollection, usuario);
      expect(comp.usuariosCollection).toEqual(expectedCollection);
    });

    it('Should call Sector query and add missing value', () => {
      const objeto: IObjeto = { id: 456 };
      const sector: ISector = { id: 67521 };
      objeto.sector = sector;

      const sectorCollection: ISector[] = [{ id: 82704 }];
      jest.spyOn(sectorService, 'query').mockReturnValue(of(new HttpResponse({ body: sectorCollection })));
      const additionalSectors = [sector];
      const expectedCollection: ISector[] = [...additionalSectors, ...sectorCollection];
      jest.spyOn(sectorService, 'addSectorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ objeto });
      comp.ngOnInit();

      expect(sectorService.query).toHaveBeenCalled();
      expect(sectorService.addSectorToCollectionIfMissing).toHaveBeenCalledWith(sectorCollection, ...additionalSectors);
      expect(comp.sectorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const objeto: IObjeto = { id: 456 };
      const tipoObjeto: ITipoObjeto = { id: 26149 };
      objeto.tipoObjeto = tipoObjeto;
      const usuario: IUsuario = { id: 24936 };
      objeto.usuario = usuario;
      const sector: ISector = { id: 87231 };
      objeto.sector = sector;

      activatedRoute.data = of({ objeto });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(objeto));
      expect(comp.tipoObjetosCollection).toContain(tipoObjeto);
      expect(comp.usuariosCollection).toContain(usuario);
      expect(comp.sectorsSharedCollection).toContain(sector);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Objeto>>();
      const objeto = { id: 123 };
      jest.spyOn(objetoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ objeto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: objeto }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(objetoService.update).toHaveBeenCalledWith(objeto);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Objeto>>();
      const objeto = new Objeto();
      jest.spyOn(objetoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ objeto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: objeto }));
      saveSubject.complete();

      // THEN
      expect(objetoService.create).toHaveBeenCalledWith(objeto);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Objeto>>();
      const objeto = { id: 123 };
      jest.spyOn(objetoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ objeto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(objetoService.update).toHaveBeenCalledWith(objeto);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackTipoObjetoById', () => {
      it('Should return tracked TipoObjeto primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTipoObjetoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackUsuarioById', () => {
      it('Should return tracked Usuario primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUsuarioById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSectorById', () => {
      it('Should return tracked Sector primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSectorById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
