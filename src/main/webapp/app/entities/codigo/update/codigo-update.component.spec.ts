jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CodigoService } from '../service/codigo.service';
import { ICodigo, Codigo } from '../codigo.model';
import { IPaquete } from 'app/entities/paquete/paquete.model';
import { PaqueteService } from 'app/entities/paquete/service/paquete.service';
import { ILote } from 'app/entities/lote/lote.model';
import { LoteService } from 'app/entities/lote/service/lote.service';
import { IVencimiento } from 'app/entities/vencimiento/vencimiento.model';
import { VencimientoService } from 'app/entities/vencimiento/service/vencimiento.service';
import { IObjeto } from 'app/entities/objeto/objeto.model';
import { ObjetoService } from 'app/entities/objeto/service/objeto.service';

import { CodigoUpdateComponent } from './codigo-update.component';

describe('Codigo Management Update Component', () => {
  let comp: CodigoUpdateComponent;
  let fixture: ComponentFixture<CodigoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let codigoService: CodigoService;
  let paqueteService: PaqueteService;
  let loteService: LoteService;
  let vencimientoService: VencimientoService;
  let objetoService: ObjetoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CodigoUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CodigoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CodigoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    codigoService = TestBed.inject(CodigoService);
    paqueteService = TestBed.inject(PaqueteService);
    loteService = TestBed.inject(LoteService);
    vencimientoService = TestBed.inject(VencimientoService);
    objetoService = TestBed.inject(ObjetoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call paquete query and add missing value', () => {
      const codigo: ICodigo = { id: 456 };
      const paquete: IPaquete = { id: 15299 };
      codigo.paquete = paquete;

      const paqueteCollection: IPaquete[] = [{ id: 69706 }];
      jest.spyOn(paqueteService, 'query').mockReturnValue(of(new HttpResponse({ body: paqueteCollection })));
      const expectedCollection: IPaquete[] = [paquete, ...paqueteCollection];
      jest.spyOn(paqueteService, 'addPaqueteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ codigo });
      comp.ngOnInit();

      expect(paqueteService.query).toHaveBeenCalled();
      expect(paqueteService.addPaqueteToCollectionIfMissing).toHaveBeenCalledWith(paqueteCollection, paquete);
      expect(comp.paquetesCollection).toEqual(expectedCollection);
    });

    it('Should call lote query and add missing value', () => {
      const codigo: ICodigo = { id: 456 };
      const lote: ILote = { id: 56764 };
      codigo.lote = lote;

      const loteCollection: ILote[] = [{ id: 30872 }];
      jest.spyOn(loteService, 'query').mockReturnValue(of(new HttpResponse({ body: loteCollection })));
      const expectedCollection: ILote[] = [lote, ...loteCollection];
      jest.spyOn(loteService, 'addLoteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ codigo });
      comp.ngOnInit();

      expect(loteService.query).toHaveBeenCalled();
      expect(loteService.addLoteToCollectionIfMissing).toHaveBeenCalledWith(loteCollection, lote);
      expect(comp.lotesCollection).toEqual(expectedCollection);
    });

    it('Should call vencimiento query and add missing value', () => {
      const codigo: ICodigo = { id: 456 };
      const vencimiento: IVencimiento = { id: 14911 };
      codigo.vencimiento = vencimiento;

      const vencimientoCollection: IVencimiento[] = [{ id: 25405 }];
      jest.spyOn(vencimientoService, 'query').mockReturnValue(of(new HttpResponse({ body: vencimientoCollection })));
      const expectedCollection: IVencimiento[] = [vencimiento, ...vencimientoCollection];
      jest.spyOn(vencimientoService, 'addVencimientoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ codigo });
      comp.ngOnInit();

      expect(vencimientoService.query).toHaveBeenCalled();
      expect(vencimientoService.addVencimientoToCollectionIfMissing).toHaveBeenCalledWith(vencimientoCollection, vencimiento);
      expect(comp.vencimientosCollection).toEqual(expectedCollection);
    });

    it('Should call Objeto query and add missing value', () => {
      const codigo: ICodigo = { id: 456 };
      const objeto: IObjeto = { id: 33165 };
      codigo.objeto = objeto;

      const objetoCollection: IObjeto[] = [{ id: 29838 }];
      jest.spyOn(objetoService, 'query').mockReturnValue(of(new HttpResponse({ body: objetoCollection })));
      const additionalObjetos = [objeto];
      const expectedCollection: IObjeto[] = [...additionalObjetos, ...objetoCollection];
      jest.spyOn(objetoService, 'addObjetoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ codigo });
      comp.ngOnInit();

      expect(objetoService.query).toHaveBeenCalled();
      expect(objetoService.addObjetoToCollectionIfMissing).toHaveBeenCalledWith(objetoCollection, ...additionalObjetos);
      expect(comp.objetosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const codigo: ICodigo = { id: 456 };
      const paquete: IPaquete = { id: 30507 };
      codigo.paquete = paquete;
      const lote: ILote = { id: 9898 };
      codigo.lote = lote;
      const vencimiento: IVencimiento = { id: 95641 };
      codigo.vencimiento = vencimiento;
      const objeto: IObjeto = { id: 3383 };
      codigo.objeto = objeto;

      activatedRoute.data = of({ codigo });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(codigo));
      expect(comp.paquetesCollection).toContain(paquete);
      expect(comp.lotesCollection).toContain(lote);
      expect(comp.vencimientosCollection).toContain(vencimiento);
      expect(comp.objetosSharedCollection).toContain(objeto);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Codigo>>();
      const codigo = { id: 123 };
      jest.spyOn(codigoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ codigo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: codigo }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(codigoService.update).toHaveBeenCalledWith(codigo);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Codigo>>();
      const codigo = new Codigo();
      jest.spyOn(codigoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ codigo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: codigo }));
      saveSubject.complete();

      // THEN
      expect(codigoService.create).toHaveBeenCalledWith(codigo);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Codigo>>();
      const codigo = { id: 123 };
      jest.spyOn(codigoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ codigo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(codigoService.update).toHaveBeenCalledWith(codigo);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPaqueteById', () => {
      it('Should return tracked Paquete primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPaqueteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackLoteById', () => {
      it('Should return tracked Lote primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackLoteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackVencimientoById', () => {
      it('Should return tracked Vencimiento primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackVencimientoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackObjetoById', () => {
      it('Should return tracked Objeto primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackObjetoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
