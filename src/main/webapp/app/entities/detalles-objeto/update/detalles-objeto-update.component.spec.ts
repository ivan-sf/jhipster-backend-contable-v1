jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DetallesObjetoService } from '../service/detalles-objeto.service';
import { IDetallesObjeto, DetallesObjeto } from '../detalles-objeto.model';

import { DetallesObjetoUpdateComponent } from './detalles-objeto-update.component';

describe('DetallesObjeto Management Update Component', () => {
  let comp: DetallesObjetoUpdateComponent;
  let fixture: ComponentFixture<DetallesObjetoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let detallesObjetoService: DetallesObjetoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DetallesObjetoUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(DetallesObjetoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DetallesObjetoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    detallesObjetoService = TestBed.inject(DetallesObjetoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const detallesObjeto: IDetallesObjeto = { id: 456 };

      activatedRoute.data = of({ detallesObjeto });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(detallesObjeto));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DetallesObjeto>>();
      const detallesObjeto = { id: 123 };
      jest.spyOn(detallesObjetoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detallesObjeto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: detallesObjeto }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(detallesObjetoService.update).toHaveBeenCalledWith(detallesObjeto);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DetallesObjeto>>();
      const detallesObjeto = new DetallesObjeto();
      jest.spyOn(detallesObjetoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detallesObjeto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: detallesObjeto }));
      saveSubject.complete();

      // THEN
      expect(detallesObjetoService.create).toHaveBeenCalledWith(detallesObjeto);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DetallesObjeto>>();
      const detallesObjeto = { id: 123 };
      jest.spyOn(detallesObjetoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detallesObjeto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(detallesObjetoService.update).toHaveBeenCalledWith(detallesObjeto);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
