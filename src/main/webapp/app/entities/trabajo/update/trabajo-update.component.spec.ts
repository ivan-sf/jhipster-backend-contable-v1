jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TrabajoService } from '../service/trabajo.service';
import { ITrabajo, Trabajo } from '../trabajo.model';

import { TrabajoUpdateComponent } from './trabajo-update.component';

describe('Trabajo Management Update Component', () => {
  let comp: TrabajoUpdateComponent;
  let fixture: ComponentFixture<TrabajoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let trabajoService: TrabajoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TrabajoUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(TrabajoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TrabajoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    trabajoService = TestBed.inject(TrabajoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const trabajo: ITrabajo = { id: 456 };

      activatedRoute.data = of({ trabajo });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(trabajo));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Trabajo>>();
      const trabajo = { id: 123 };
      jest.spyOn(trabajoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trabajo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trabajo }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(trabajoService.update).toHaveBeenCalledWith(trabajo);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Trabajo>>();
      const trabajo = new Trabajo();
      jest.spyOn(trabajoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trabajo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trabajo }));
      saveSubject.complete();

      // THEN
      expect(trabajoService.create).toHaveBeenCalledWith(trabajo);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Trabajo>>();
      const trabajo = { id: 123 };
      jest.spyOn(trabajoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trabajo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(trabajoService.update).toHaveBeenCalledWith(trabajo);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
