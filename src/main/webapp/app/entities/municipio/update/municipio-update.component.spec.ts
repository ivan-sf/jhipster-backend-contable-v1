jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MunicipioService } from '../service/municipio.service';
import { IMunicipio, Municipio } from '../municipio.model';

import { MunicipioUpdateComponent } from './municipio-update.component';

describe('Municipio Management Update Component', () => {
  let comp: MunicipioUpdateComponent;
  let fixture: ComponentFixture<MunicipioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let municipioService: MunicipioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MunicipioUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(MunicipioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MunicipioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    municipioService = TestBed.inject(MunicipioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const municipio: IMunicipio = { id: 456 };

      activatedRoute.data = of({ municipio });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(municipio));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Municipio>>();
      const municipio = { id: 123 };
      jest.spyOn(municipioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ municipio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: municipio }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(municipioService.update).toHaveBeenCalledWith(municipio);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Municipio>>();
      const municipio = new Municipio();
      jest.spyOn(municipioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ municipio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: municipio }));
      saveSubject.complete();

      // THEN
      expect(municipioService.create).toHaveBeenCalledWith(municipio);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Municipio>>();
      const municipio = { id: 123 };
      jest.spyOn(municipioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ municipio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(municipioService.update).toHaveBeenCalledWith(municipio);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
