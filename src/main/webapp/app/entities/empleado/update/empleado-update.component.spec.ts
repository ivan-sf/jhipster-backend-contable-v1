jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EmpleadoService } from '../service/empleado.service';
import { IEmpleado, Empleado } from '../empleado.model';
import { ITrabajo } from 'app/entities/trabajo/trabajo.model';
import { TrabajoService } from 'app/entities/trabajo/service/trabajo.service';

import { EmpleadoUpdateComponent } from './empleado-update.component';

describe('Empleado Management Update Component', () => {
  let comp: EmpleadoUpdateComponent;
  let fixture: ComponentFixture<EmpleadoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let empleadoService: EmpleadoService;
  let trabajoService: TrabajoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EmpleadoUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(EmpleadoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmpleadoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    empleadoService = TestBed.inject(EmpleadoService);
    trabajoService = TestBed.inject(TrabajoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Trabajo query and add missing value', () => {
      const empleado: IEmpleado = { id: 456 };
      const trabajo: ITrabajo = { id: 33925 };
      empleado.trabajo = trabajo;

      const trabajoCollection: ITrabajo[] = [{ id: 59159 }];
      jest.spyOn(trabajoService, 'query').mockReturnValue(of(new HttpResponse({ body: trabajoCollection })));
      const additionalTrabajos = [trabajo];
      const expectedCollection: ITrabajo[] = [...additionalTrabajos, ...trabajoCollection];
      jest.spyOn(trabajoService, 'addTrabajoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ empleado });
      comp.ngOnInit();

      expect(trabajoService.query).toHaveBeenCalled();
      expect(trabajoService.addTrabajoToCollectionIfMissing).toHaveBeenCalledWith(trabajoCollection, ...additionalTrabajos);
      expect(comp.trabajosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const empleado: IEmpleado = { id: 456 };
      const trabajo: ITrabajo = { id: 16248 };
      empleado.trabajo = trabajo;

      activatedRoute.data = of({ empleado });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(empleado));
      expect(comp.trabajosSharedCollection).toContain(trabajo);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Empleado>>();
      const empleado = { id: 123 };
      jest.spyOn(empleadoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empleado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: empleado }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(empleadoService.update).toHaveBeenCalledWith(empleado);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Empleado>>();
      const empleado = new Empleado();
      jest.spyOn(empleadoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empleado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: empleado }));
      saveSubject.complete();

      // THEN
      expect(empleadoService.create).toHaveBeenCalledWith(empleado);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Empleado>>();
      const empleado = { id: 123 };
      jest.spyOn(empleadoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empleado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(empleadoService.update).toHaveBeenCalledWith(empleado);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackTrabajoById', () => {
      it('Should return tracked Trabajo primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTrabajoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
