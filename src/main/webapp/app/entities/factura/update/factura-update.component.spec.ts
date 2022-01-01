jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FacturaService } from '../service/factura.service';
import { IFactura, Factura } from '../factura.model';
import { ISucursal } from 'app/entities/sucursal/sucursal.model';
import { SucursalService } from 'app/entities/sucursal/service/sucursal.service';
import { ITipoFactura } from 'app/entities/tipo-factura/tipo-factura.model';
import { TipoFacturaService } from 'app/entities/tipo-factura/service/tipo-factura.service';
import { INotaContable } from 'app/entities/nota-contable/nota-contable.model';
import { NotaContableService } from 'app/entities/nota-contable/service/nota-contable.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { EmpleadoService } from 'app/entities/empleado/service/empleado.service';

import { FacturaUpdateComponent } from './factura-update.component';

describe('Factura Management Update Component', () => {
  let comp: FacturaUpdateComponent;
  let fixture: ComponentFixture<FacturaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let facturaService: FacturaService;
  let sucursalService: SucursalService;
  let tipoFacturaService: TipoFacturaService;
  let notaContableService: NotaContableService;
  let clienteService: ClienteService;
  let empleadoService: EmpleadoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FacturaUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(FacturaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FacturaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    facturaService = TestBed.inject(FacturaService);
    sucursalService = TestBed.inject(SucursalService);
    tipoFacturaService = TestBed.inject(TipoFacturaService);
    notaContableService = TestBed.inject(NotaContableService);
    clienteService = TestBed.inject(ClienteService);
    empleadoService = TestBed.inject(EmpleadoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call sucursal query and add missing value', () => {
      const factura: IFactura = { id: 456 };
      const sucursal: ISucursal = { id: 95845 };
      factura.sucursal = sucursal;

      const sucursalCollection: ISucursal[] = [{ id: 82832 }];
      jest.spyOn(sucursalService, 'query').mockReturnValue(of(new HttpResponse({ body: sucursalCollection })));
      const expectedCollection: ISucursal[] = [sucursal, ...sucursalCollection];
      jest.spyOn(sucursalService, 'addSucursalToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factura });
      comp.ngOnInit();

      expect(sucursalService.query).toHaveBeenCalled();
      expect(sucursalService.addSucursalToCollectionIfMissing).toHaveBeenCalledWith(sucursalCollection, sucursal);
      expect(comp.sucursalsCollection).toEqual(expectedCollection);
    });

    it('Should call tipoFactura query and add missing value', () => {
      const factura: IFactura = { id: 456 };
      const tipoFactura: ITipoFactura = { id: 64907 };
      factura.tipoFactura = tipoFactura;

      const tipoFacturaCollection: ITipoFactura[] = [{ id: 76520 }];
      jest.spyOn(tipoFacturaService, 'query').mockReturnValue(of(new HttpResponse({ body: tipoFacturaCollection })));
      const expectedCollection: ITipoFactura[] = [tipoFactura, ...tipoFacturaCollection];
      jest.spyOn(tipoFacturaService, 'addTipoFacturaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factura });
      comp.ngOnInit();

      expect(tipoFacturaService.query).toHaveBeenCalled();
      expect(tipoFacturaService.addTipoFacturaToCollectionIfMissing).toHaveBeenCalledWith(tipoFacturaCollection, tipoFactura);
      expect(comp.tipoFacturasCollection).toEqual(expectedCollection);
    });

    it('Should call notaContable query and add missing value', () => {
      const factura: IFactura = { id: 456 };
      const notaContable: INotaContable = { id: 43505 };
      factura.notaContable = notaContable;

      const notaContableCollection: INotaContable[] = [{ id: 81179 }];
      jest.spyOn(notaContableService, 'query').mockReturnValue(of(new HttpResponse({ body: notaContableCollection })));
      const expectedCollection: INotaContable[] = [notaContable, ...notaContableCollection];
      jest.spyOn(notaContableService, 'addNotaContableToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factura });
      comp.ngOnInit();

      expect(notaContableService.query).toHaveBeenCalled();
      expect(notaContableService.addNotaContableToCollectionIfMissing).toHaveBeenCalledWith(notaContableCollection, notaContable);
      expect(comp.notaContablesCollection).toEqual(expectedCollection);
    });

    it('Should call Cliente query and add missing value', () => {
      const factura: IFactura = { id: 456 };
      const cliente: ICliente = { id: 98815 };
      factura.cliente = cliente;

      const clienteCollection: ICliente[] = [{ id: 59464 }];
      jest.spyOn(clienteService, 'query').mockReturnValue(of(new HttpResponse({ body: clienteCollection })));
      const additionalClientes = [cliente];
      const expectedCollection: ICliente[] = [...additionalClientes, ...clienteCollection];
      jest.spyOn(clienteService, 'addClienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factura });
      comp.ngOnInit();

      expect(clienteService.query).toHaveBeenCalled();
      expect(clienteService.addClienteToCollectionIfMissing).toHaveBeenCalledWith(clienteCollection, ...additionalClientes);
      expect(comp.clientesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Empleado query and add missing value', () => {
      const factura: IFactura = { id: 456 };
      const empleadp: IEmpleado = { id: 70113 };
      factura.empleadp = empleadp;

      const empleadoCollection: IEmpleado[] = [{ id: 28912 }];
      jest.spyOn(empleadoService, 'query').mockReturnValue(of(new HttpResponse({ body: empleadoCollection })));
      const additionalEmpleados = [empleadp];
      const expectedCollection: IEmpleado[] = [...additionalEmpleados, ...empleadoCollection];
      jest.spyOn(empleadoService, 'addEmpleadoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factura });
      comp.ngOnInit();

      expect(empleadoService.query).toHaveBeenCalled();
      expect(empleadoService.addEmpleadoToCollectionIfMissing).toHaveBeenCalledWith(empleadoCollection, ...additionalEmpleados);
      expect(comp.empleadosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const factura: IFactura = { id: 456 };
      const sucursal: ISucursal = { id: 40209 };
      factura.sucursal = sucursal;
      const tipoFactura: ITipoFactura = { id: 15579 };
      factura.tipoFactura = tipoFactura;
      const notaContable: INotaContable = { id: 8743 };
      factura.notaContable = notaContable;
      const cliente: ICliente = { id: 70014 };
      factura.cliente = cliente;
      const empleadp: IEmpleado = { id: 55453 };
      factura.empleadp = empleadp;

      activatedRoute.data = of({ factura });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(factura));
      expect(comp.sucursalsCollection).toContain(sucursal);
      expect(comp.tipoFacturasCollection).toContain(tipoFactura);
      expect(comp.notaContablesCollection).toContain(notaContable);
      expect(comp.clientesSharedCollection).toContain(cliente);
      expect(comp.empleadosSharedCollection).toContain(empleadp);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Factura>>();
      const factura = { id: 123 };
      jest.spyOn(facturaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factura });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: factura }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(facturaService.update).toHaveBeenCalledWith(factura);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Factura>>();
      const factura = new Factura();
      jest.spyOn(facturaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factura });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: factura }));
      saveSubject.complete();

      // THEN
      expect(facturaService.create).toHaveBeenCalledWith(factura);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Factura>>();
      const factura = { id: 123 };
      jest.spyOn(facturaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factura });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(facturaService.update).toHaveBeenCalledWith(factura);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSucursalById', () => {
      it('Should return tracked Sucursal primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSucursalById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackTipoFacturaById', () => {
      it('Should return tracked TipoFactura primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTipoFacturaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackNotaContableById', () => {
      it('Should return tracked NotaContable primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackNotaContableById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackClienteById', () => {
      it('Should return tracked Cliente primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackClienteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackEmpleadoById', () => {
      it('Should return tracked Empleado primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEmpleadoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
