jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CiudadService } from '../service/ciudad.service';
import { ICiudad, Ciudad } from '../ciudad.model';

import { CiudadUpdateComponent } from './ciudad-update.component';

describe('Ciudad Management Update Component', () => {
  let comp: CiudadUpdateComponent;
  let fixture: ComponentFixture<CiudadUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ciudadService: CiudadService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CiudadUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CiudadUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CiudadUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ciudadService = TestBed.inject(CiudadService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const ciudad: ICiudad = { id: 456 };

      activatedRoute.data = of({ ciudad });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(ciudad));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ciudad>>();
      const ciudad = { id: 123 };
      jest.spyOn(ciudadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ciudad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ciudad }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(ciudadService.update).toHaveBeenCalledWith(ciudad);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ciudad>>();
      const ciudad = new Ciudad();
      jest.spyOn(ciudadService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ciudad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ciudad }));
      saveSubject.complete();

      // THEN
      expect(ciudadService.create).toHaveBeenCalledWith(ciudad);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ciudad>>();
      const ciudad = { id: 123 };
      jest.spyOn(ciudadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ciudad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ciudadService.update).toHaveBeenCalledWith(ciudad);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
