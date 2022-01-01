jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ClienteService } from '../service/cliente.service';
import { ICliente, Cliente } from '../cliente.model';
import { IMunicipio } from 'app/entities/municipio/municipio.model';
import { MunicipioService } from 'app/entities/municipio/service/municipio.service';
import { ICiudad } from 'app/entities/ciudad/ciudad.model';
import { CiudadService } from 'app/entities/ciudad/service/ciudad.service';
import { IDepartamento } from 'app/entities/departamento/departamento.model';
import { DepartamentoService } from 'app/entities/departamento/service/departamento.service';
import { IPais } from 'app/entities/pais/pais.model';
import { PaisService } from 'app/entities/pais/service/pais.service';

import { ClienteUpdateComponent } from './cliente-update.component';

describe('Cliente Management Update Component', () => {
  let comp: ClienteUpdateComponent;
  let fixture: ComponentFixture<ClienteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let clienteService: ClienteService;
  let municipioService: MunicipioService;
  let ciudadService: CiudadService;
  let departamentoService: DepartamentoService;
  let paisService: PaisService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ClienteUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ClienteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClienteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    clienteService = TestBed.inject(ClienteService);
    municipioService = TestBed.inject(MunicipioService);
    ciudadService = TestBed.inject(CiudadService);
    departamentoService = TestBed.inject(DepartamentoService);
    paisService = TestBed.inject(PaisService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Municipio query and add missing value', () => {
      const cliente: ICliente = { id: 456 };
      const municipio: IMunicipio = { id: 57693 };
      cliente.municipio = municipio;

      const municipioCollection: IMunicipio[] = [{ id: 46638 }];
      jest.spyOn(municipioService, 'query').mockReturnValue(of(new HttpResponse({ body: municipioCollection })));
      const additionalMunicipios = [municipio];
      const expectedCollection: IMunicipio[] = [...additionalMunicipios, ...municipioCollection];
      jest.spyOn(municipioService, 'addMunicipioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cliente });
      comp.ngOnInit();

      expect(municipioService.query).toHaveBeenCalled();
      expect(municipioService.addMunicipioToCollectionIfMissing).toHaveBeenCalledWith(municipioCollection, ...additionalMunicipios);
      expect(comp.municipiosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Ciudad query and add missing value', () => {
      const cliente: ICliente = { id: 456 };
      const ciudad: ICiudad = { id: 28885 };
      cliente.ciudad = ciudad;

      const ciudadCollection: ICiudad[] = [{ id: 48856 }];
      jest.spyOn(ciudadService, 'query').mockReturnValue(of(new HttpResponse({ body: ciudadCollection })));
      const additionalCiudads = [ciudad];
      const expectedCollection: ICiudad[] = [...additionalCiudads, ...ciudadCollection];
      jest.spyOn(ciudadService, 'addCiudadToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cliente });
      comp.ngOnInit();

      expect(ciudadService.query).toHaveBeenCalled();
      expect(ciudadService.addCiudadToCollectionIfMissing).toHaveBeenCalledWith(ciudadCollection, ...additionalCiudads);
      expect(comp.ciudadsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Departamento query and add missing value', () => {
      const cliente: ICliente = { id: 456 };
      const departamento: IDepartamento = { id: 29711 };
      cliente.departamento = departamento;

      const departamentoCollection: IDepartamento[] = [{ id: 78423 }];
      jest.spyOn(departamentoService, 'query').mockReturnValue(of(new HttpResponse({ body: departamentoCollection })));
      const additionalDepartamentos = [departamento];
      const expectedCollection: IDepartamento[] = [...additionalDepartamentos, ...departamentoCollection];
      jest.spyOn(departamentoService, 'addDepartamentoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cliente });
      comp.ngOnInit();

      expect(departamentoService.query).toHaveBeenCalled();
      expect(departamentoService.addDepartamentoToCollectionIfMissing).toHaveBeenCalledWith(
        departamentoCollection,
        ...additionalDepartamentos
      );
      expect(comp.departamentosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Pais query and add missing value', () => {
      const cliente: ICliente = { id: 456 };
      const pais: IPais = { id: 82442 };
      cliente.pais = pais;

      const paisCollection: IPais[] = [{ id: 12458 }];
      jest.spyOn(paisService, 'query').mockReturnValue(of(new HttpResponse({ body: paisCollection })));
      const additionalPais = [pais];
      const expectedCollection: IPais[] = [...additionalPais, ...paisCollection];
      jest.spyOn(paisService, 'addPaisToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cliente });
      comp.ngOnInit();

      expect(paisService.query).toHaveBeenCalled();
      expect(paisService.addPaisToCollectionIfMissing).toHaveBeenCalledWith(paisCollection, ...additionalPais);
      expect(comp.paisSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const cliente: ICliente = { id: 456 };
      const municipio: IMunicipio = { id: 3802 };
      cliente.municipio = municipio;
      const ciudad: ICiudad = { id: 22019 };
      cliente.ciudad = ciudad;
      const departamento: IDepartamento = { id: 11719 };
      cliente.departamento = departamento;
      const pais: IPais = { id: 47787 };
      cliente.pais = pais;

      activatedRoute.data = of({ cliente });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cliente));
      expect(comp.municipiosSharedCollection).toContain(municipio);
      expect(comp.ciudadsSharedCollection).toContain(ciudad);
      expect(comp.departamentosSharedCollection).toContain(departamento);
      expect(comp.paisSharedCollection).toContain(pais);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cliente>>();
      const cliente = { id: 123 };
      jest.spyOn(clienteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cliente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cliente }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(clienteService.update).toHaveBeenCalledWith(cliente);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cliente>>();
      const cliente = new Cliente();
      jest.spyOn(clienteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cliente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cliente }));
      saveSubject.complete();

      // THEN
      expect(clienteService.create).toHaveBeenCalledWith(cliente);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cliente>>();
      const cliente = { id: 123 };
      jest.spyOn(clienteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cliente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(clienteService.update).toHaveBeenCalledWith(cliente);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackMunicipioById', () => {
      it('Should return tracked Municipio primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMunicipioById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCiudadById', () => {
      it('Should return tracked Ciudad primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCiudadById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackDepartamentoById', () => {
      it('Should return tracked Departamento primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDepartamentoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPaisById', () => {
      it('Should return tracked Pais primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPaisById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
