jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { NotaContableService } from '../service/nota-contable.service';
import { INotaContable, NotaContable } from '../nota-contable.model';

import { NotaContableUpdateComponent } from './nota-contable-update.component';

describe('NotaContable Management Update Component', () => {
  let comp: NotaContableUpdateComponent;
  let fixture: ComponentFixture<NotaContableUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let notaContableService: NotaContableService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [NotaContableUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(NotaContableUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NotaContableUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    notaContableService = TestBed.inject(NotaContableService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const notaContable: INotaContable = { id: 456 };

      activatedRoute.data = of({ notaContable });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(notaContable));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NotaContable>>();
      const notaContable = { id: 123 };
      jest.spyOn(notaContableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notaContable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: notaContable }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(notaContableService.update).toHaveBeenCalledWith(notaContable);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NotaContable>>();
      const notaContable = new NotaContable();
      jest.spyOn(notaContableService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notaContable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: notaContable }));
      saveSubject.complete();

      // THEN
      expect(notaContableService.create).toHaveBeenCalledWith(notaContable);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NotaContable>>();
      const notaContable = { id: 123 };
      jest.spyOn(notaContableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notaContable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(notaContableService.update).toHaveBeenCalledWith(notaContable);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
