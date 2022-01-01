jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TipoObjetoService } from '../service/tipo-objeto.service';
import { ITipoObjeto, TipoObjeto } from '../tipo-objeto.model';

import { TipoObjetoUpdateComponent } from './tipo-objeto-update.component';

describe('TipoObjeto Management Update Component', () => {
  let comp: TipoObjetoUpdateComponent;
  let fixture: ComponentFixture<TipoObjetoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tipoObjetoService: TipoObjetoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TipoObjetoUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(TipoObjetoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TipoObjetoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tipoObjetoService = TestBed.inject(TipoObjetoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tipoObjeto: ITipoObjeto = { id: 456 };

      activatedRoute.data = of({ tipoObjeto });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(tipoObjeto));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoObjeto>>();
      const tipoObjeto = { id: 123 };
      jest.spyOn(tipoObjetoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoObjeto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoObjeto }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(tipoObjetoService.update).toHaveBeenCalledWith(tipoObjeto);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoObjeto>>();
      const tipoObjeto = new TipoObjeto();
      jest.spyOn(tipoObjetoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoObjeto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoObjeto }));
      saveSubject.complete();

      // THEN
      expect(tipoObjetoService.create).toHaveBeenCalledWith(tipoObjeto);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoObjeto>>();
      const tipoObjeto = { id: 123 };
      jest.spyOn(tipoObjetoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoObjeto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tipoObjetoService.update).toHaveBeenCalledWith(tipoObjeto);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
