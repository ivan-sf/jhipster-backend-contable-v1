jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SectorService } from '../service/sector.service';
import { ISector, Sector } from '../sector.model';
import { IInventario } from 'app/entities/inventario/inventario.model';
import { InventarioService } from 'app/entities/inventario/service/inventario.service';

import { SectorUpdateComponent } from './sector-update.component';

describe('Sector Management Update Component', () => {
  let comp: SectorUpdateComponent;
  let fixture: ComponentFixture<SectorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sectorService: SectorService;
  let inventarioService: InventarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SectorUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SectorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SectorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sectorService = TestBed.inject(SectorService);
    inventarioService = TestBed.inject(InventarioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Inventario query and add missing value', () => {
      const sector: ISector = { id: 456 };
      const inventario: IInventario = { id: 59791 };
      sector.inventario = inventario;

      const inventarioCollection: IInventario[] = [{ id: 93238 }];
      jest.spyOn(inventarioService, 'query').mockReturnValue(of(new HttpResponse({ body: inventarioCollection })));
      const additionalInventarios = [inventario];
      const expectedCollection: IInventario[] = [...additionalInventarios, ...inventarioCollection];
      jest.spyOn(inventarioService, 'addInventarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sector });
      comp.ngOnInit();

      expect(inventarioService.query).toHaveBeenCalled();
      expect(inventarioService.addInventarioToCollectionIfMissing).toHaveBeenCalledWith(inventarioCollection, ...additionalInventarios);
      expect(comp.inventariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sector: ISector = { id: 456 };
      const inventario: IInventario = { id: 23807 };
      sector.inventario = inventario;

      activatedRoute.data = of({ sector });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(sector));
      expect(comp.inventariosSharedCollection).toContain(inventario);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Sector>>();
      const sector = { id: 123 };
      jest.spyOn(sectorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sector });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sector }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sectorService.update).toHaveBeenCalledWith(sector);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Sector>>();
      const sector = new Sector();
      jest.spyOn(sectorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sector });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sector }));
      saveSubject.complete();

      // THEN
      expect(sectorService.create).toHaveBeenCalledWith(sector);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Sector>>();
      const sector = { id: 123 };
      jest.spyOn(sectorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sector });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sectorService.update).toHaveBeenCalledWith(sector);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackInventarioById', () => {
      it('Should return tracked Inventario primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackInventarioById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
