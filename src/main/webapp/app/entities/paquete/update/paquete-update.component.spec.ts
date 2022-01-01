jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PaqueteService } from '../service/paquete.service';
import { IPaquete, Paquete } from '../paquete.model';

import { PaqueteUpdateComponent } from './paquete-update.component';

describe('Paquete Management Update Component', () => {
  let comp: PaqueteUpdateComponent;
  let fixture: ComponentFixture<PaqueteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let paqueteService: PaqueteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PaqueteUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(PaqueteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PaqueteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paqueteService = TestBed.inject(PaqueteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const paquete: IPaquete = { id: 456 };

      activatedRoute.data = of({ paquete });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(paquete));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Paquete>>();
      const paquete = { id: 123 };
      jest.spyOn(paqueteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paquete });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paquete }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(paqueteService.update).toHaveBeenCalledWith(paquete);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Paquete>>();
      const paquete = new Paquete();
      jest.spyOn(paqueteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paquete });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paquete }));
      saveSubject.complete();

      // THEN
      expect(paqueteService.create).toHaveBeenCalledWith(paquete);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Paquete>>();
      const paquete = { id: 123 };
      jest.spyOn(paqueteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paquete });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paqueteService.update).toHaveBeenCalledWith(paquete);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
