jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PrecioService } from '../service/precio.service';
import { IPrecio, Precio } from '../precio.model';
import { IPaquete } from 'app/entities/paquete/paquete.model';
import { PaqueteService } from 'app/entities/paquete/service/paquete.service';

import { PrecioUpdateComponent } from './precio-update.component';

describe('Precio Management Update Component', () => {
  let comp: PrecioUpdateComponent;
  let fixture: ComponentFixture<PrecioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let precioService: PrecioService;
  let paqueteService: PaqueteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PrecioUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(PrecioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PrecioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    precioService = TestBed.inject(PrecioService);
    paqueteService = TestBed.inject(PaqueteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Paquete query and add missing value', () => {
      const precio: IPrecio = { id: 456 };
      const paquete: IPaquete = { id: 32260 };
      precio.paquete = paquete;

      const paqueteCollection: IPaquete[] = [{ id: 13543 }];
      jest.spyOn(paqueteService, 'query').mockReturnValue(of(new HttpResponse({ body: paqueteCollection })));
      const additionalPaquetes = [paquete];
      const expectedCollection: IPaquete[] = [...additionalPaquetes, ...paqueteCollection];
      jest.spyOn(paqueteService, 'addPaqueteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ precio });
      comp.ngOnInit();

      expect(paqueteService.query).toHaveBeenCalled();
      expect(paqueteService.addPaqueteToCollectionIfMissing).toHaveBeenCalledWith(paqueteCollection, ...additionalPaquetes);
      expect(comp.paquetesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const precio: IPrecio = { id: 456 };
      const paquete: IPaquete = { id: 27444 };
      precio.paquete = paquete;

      activatedRoute.data = of({ precio });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(precio));
      expect(comp.paquetesSharedCollection).toContain(paquete);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Precio>>();
      const precio = { id: 123 };
      jest.spyOn(precioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ precio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: precio }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(precioService.update).toHaveBeenCalledWith(precio);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Precio>>();
      const precio = new Precio();
      jest.spyOn(precioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ precio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: precio }));
      saveSubject.complete();

      // THEN
      expect(precioService.create).toHaveBeenCalledWith(precio);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Precio>>();
      const precio = { id: 123 };
      jest.spyOn(precioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ precio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(precioService.update).toHaveBeenCalledWith(precio);
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
  });
});
