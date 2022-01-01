jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RegimenService } from '../service/regimen.service';
import { IRegimen, Regimen } from '../regimen.model';

import { RegimenUpdateComponent } from './regimen-update.component';

describe('Regimen Management Update Component', () => {
  let comp: RegimenUpdateComponent;
  let fixture: ComponentFixture<RegimenUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let regimenService: RegimenService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RegimenUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(RegimenUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RegimenUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    regimenService = TestBed.inject(RegimenService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const regimen: IRegimen = { id: 456 };

      activatedRoute.data = of({ regimen });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(regimen));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Regimen>>();
      const regimen = { id: 123 };
      jest.spyOn(regimenService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ regimen });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: regimen }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(regimenService.update).toHaveBeenCalledWith(regimen);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Regimen>>();
      const regimen = new Regimen();
      jest.spyOn(regimenService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ regimen });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: regimen }));
      saveSubject.complete();

      // THEN
      expect(regimenService.create).toHaveBeenCalledWith(regimen);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Regimen>>();
      const regimen = { id: 123 };
      jest.spyOn(regimenService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ regimen });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(regimenService.update).toHaveBeenCalledWith(regimen);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
