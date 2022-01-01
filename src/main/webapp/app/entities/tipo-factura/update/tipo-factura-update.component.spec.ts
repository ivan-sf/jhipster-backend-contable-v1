jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TipoFacturaService } from '../service/tipo-factura.service';
import { ITipoFactura, TipoFactura } from '../tipo-factura.model';

import { TipoFacturaUpdateComponent } from './tipo-factura-update.component';

describe('TipoFactura Management Update Component', () => {
  let comp: TipoFacturaUpdateComponent;
  let fixture: ComponentFixture<TipoFacturaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tipoFacturaService: TipoFacturaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TipoFacturaUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(TipoFacturaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TipoFacturaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tipoFacturaService = TestBed.inject(TipoFacturaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tipoFactura: ITipoFactura = { id: 456 };

      activatedRoute.data = of({ tipoFactura });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(tipoFactura));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoFactura>>();
      const tipoFactura = { id: 123 };
      jest.spyOn(tipoFacturaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoFactura });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoFactura }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(tipoFacturaService.update).toHaveBeenCalledWith(tipoFactura);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoFactura>>();
      const tipoFactura = new TipoFactura();
      jest.spyOn(tipoFacturaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoFactura });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoFactura }));
      saveSubject.complete();

      // THEN
      expect(tipoFacturaService.create).toHaveBeenCalledWith(tipoFactura);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoFactura>>();
      const tipoFactura = { id: 123 };
      jest.spyOn(tipoFacturaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoFactura });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tipoFacturaService.update).toHaveBeenCalledWith(tipoFactura);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
