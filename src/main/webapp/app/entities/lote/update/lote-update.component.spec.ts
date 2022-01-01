jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LoteService } from '../service/lote.service';
import { ILote, Lote } from '../lote.model';

import { LoteUpdateComponent } from './lote-update.component';

describe('Lote Management Update Component', () => {
  let comp: LoteUpdateComponent;
  let fixture: ComponentFixture<LoteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let loteService: LoteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LoteUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(LoteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LoteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    loteService = TestBed.inject(LoteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const lote: ILote = { id: 456 };

      activatedRoute.data = of({ lote });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(lote));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Lote>>();
      const lote = { id: 123 };
      jest.spyOn(loteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lote });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: lote }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(loteService.update).toHaveBeenCalledWith(lote);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Lote>>();
      const lote = new Lote();
      jest.spyOn(loteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lote });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: lote }));
      saveSubject.complete();

      // THEN
      expect(loteService.create).toHaveBeenCalledWith(lote);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Lote>>();
      const lote = { id: 123 };
      jest.spyOn(loteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lote });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(loteService.update).toHaveBeenCalledWith(lote);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
