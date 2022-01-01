jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { InventarioService } from '../service/inventario.service';
import { IInventario, Inventario } from '../inventario.model';

import { InventarioUpdateComponent } from './inventario-update.component';

describe('Inventario Management Update Component', () => {
  let comp: InventarioUpdateComponent;
  let fixture: ComponentFixture<InventarioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let inventarioService: InventarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [InventarioUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(InventarioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InventarioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    inventarioService = TestBed.inject(InventarioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const inventario: IInventario = { id: 456 };

      activatedRoute.data = of({ inventario });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(inventario));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Inventario>>();
      const inventario = { id: 123 };
      jest.spyOn(inventarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inventario }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(inventarioService.update).toHaveBeenCalledWith(inventario);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Inventario>>();
      const inventario = new Inventario();
      jest.spyOn(inventarioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inventario }));
      saveSubject.complete();

      // THEN
      expect(inventarioService.create).toHaveBeenCalledWith(inventario);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Inventario>>();
      const inventario = { id: 123 };
      jest.spyOn(inventarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(inventarioService.update).toHaveBeenCalledWith(inventario);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
