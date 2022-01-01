jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { VencimientoService } from '../service/vencimiento.service';
import { IVencimiento, Vencimiento } from '../vencimiento.model';

import { VencimientoUpdateComponent } from './vencimiento-update.component';

describe('Vencimiento Management Update Component', () => {
  let comp: VencimientoUpdateComponent;
  let fixture: ComponentFixture<VencimientoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vencimientoService: VencimientoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [VencimientoUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(VencimientoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VencimientoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vencimientoService = TestBed.inject(VencimientoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const vencimiento: IVencimiento = { id: 456 };

      activatedRoute.data = of({ vencimiento });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(vencimiento));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Vencimiento>>();
      const vencimiento = { id: 123 };
      jest.spyOn(vencimientoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vencimiento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vencimiento }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(vencimientoService.update).toHaveBeenCalledWith(vencimiento);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Vencimiento>>();
      const vencimiento = new Vencimiento();
      jest.spyOn(vencimientoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vencimiento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vencimiento }));
      saveSubject.complete();

      // THEN
      expect(vencimientoService.create).toHaveBeenCalledWith(vencimiento);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Vencimiento>>();
      const vencimiento = { id: 123 };
      jest.spyOn(vencimientoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vencimiento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vencimientoService.update).toHaveBeenCalledWith(vencimiento);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
