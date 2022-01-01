jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DetalleFacturaService } from '../service/detalle-factura.service';
import { IDetalleFactura, DetalleFactura } from '../detalle-factura.model';
import { IObjeto } from 'app/entities/objeto/objeto.model';
import { ObjetoService } from 'app/entities/objeto/service/objeto.service';
import { IFactura } from 'app/entities/factura/factura.model';
import { FacturaService } from 'app/entities/factura/service/factura.service';

import { DetalleFacturaUpdateComponent } from './detalle-factura-update.component';

describe('DetalleFactura Management Update Component', () => {
  let comp: DetalleFacturaUpdateComponent;
  let fixture: ComponentFixture<DetalleFacturaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let detalleFacturaService: DetalleFacturaService;
  let objetoService: ObjetoService;
  let facturaService: FacturaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DetalleFacturaUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(DetalleFacturaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DetalleFacturaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    detalleFacturaService = TestBed.inject(DetalleFacturaService);
    objetoService = TestBed.inject(ObjetoService);
    facturaService = TestBed.inject(FacturaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call objeto query and add missing value', () => {
      const detalleFactura: IDetalleFactura = { id: 456 };
      const objeto: IObjeto = { id: 17714 };
      detalleFactura.objeto = objeto;

      const objetoCollection: IObjeto[] = [{ id: 13363 }];
      jest.spyOn(objetoService, 'query').mockReturnValue(of(new HttpResponse({ body: objetoCollection })));
      const expectedCollection: IObjeto[] = [objeto, ...objetoCollection];
      jest.spyOn(objetoService, 'addObjetoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ detalleFactura });
      comp.ngOnInit();

      expect(objetoService.query).toHaveBeenCalled();
      expect(objetoService.addObjetoToCollectionIfMissing).toHaveBeenCalledWith(objetoCollection, objeto);
      expect(comp.objetosCollection).toEqual(expectedCollection);
    });

    it('Should call Factura query and add missing value', () => {
      const detalleFactura: IDetalleFactura = { id: 456 };
      const factura: IFactura = { id: 41106 };
      detalleFactura.factura = factura;

      const facturaCollection: IFactura[] = [{ id: 94856 }];
      jest.spyOn(facturaService, 'query').mockReturnValue(of(new HttpResponse({ body: facturaCollection })));
      const additionalFacturas = [factura];
      const expectedCollection: IFactura[] = [...additionalFacturas, ...facturaCollection];
      jest.spyOn(facturaService, 'addFacturaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ detalleFactura });
      comp.ngOnInit();

      expect(facturaService.query).toHaveBeenCalled();
      expect(facturaService.addFacturaToCollectionIfMissing).toHaveBeenCalledWith(facturaCollection, ...additionalFacturas);
      expect(comp.facturasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const detalleFactura: IDetalleFactura = { id: 456 };
      const objeto: IObjeto = { id: 23084 };
      detalleFactura.objeto = objeto;
      const factura: IFactura = { id: 16629 };
      detalleFactura.factura = factura;

      activatedRoute.data = of({ detalleFactura });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(detalleFactura));
      expect(comp.objetosCollection).toContain(objeto);
      expect(comp.facturasSharedCollection).toContain(factura);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DetalleFactura>>();
      const detalleFactura = { id: 123 };
      jest.spyOn(detalleFacturaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detalleFactura });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: detalleFactura }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(detalleFacturaService.update).toHaveBeenCalledWith(detalleFactura);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DetalleFactura>>();
      const detalleFactura = new DetalleFactura();
      jest.spyOn(detalleFacturaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detalleFactura });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: detalleFactura }));
      saveSubject.complete();

      // THEN
      expect(detalleFacturaService.create).toHaveBeenCalledWith(detalleFactura);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DetalleFactura>>();
      const detalleFactura = { id: 123 };
      jest.spyOn(detalleFacturaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detalleFactura });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(detalleFacturaService.update).toHaveBeenCalledWith(detalleFactura);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackObjetoById', () => {
      it('Should return tracked Objeto primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackObjetoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackFacturaById', () => {
      it('Should return tracked Factura primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFacturaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
