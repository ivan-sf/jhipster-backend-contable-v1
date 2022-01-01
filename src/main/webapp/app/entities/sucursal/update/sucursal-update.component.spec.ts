jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SucursalService } from '../service/sucursal.service';
import { ISucursal, Sucursal } from '../sucursal.model';
import { IRegimen } from 'app/entities/regimen/regimen.model';
import { RegimenService } from 'app/entities/regimen/service/regimen.service';
import { IMunicipio } from 'app/entities/municipio/municipio.model';
import { MunicipioService } from 'app/entities/municipio/service/municipio.service';
import { ICiudad } from 'app/entities/ciudad/ciudad.model';
import { CiudadService } from 'app/entities/ciudad/service/ciudad.service';
import { IDepartamento } from 'app/entities/departamento/departamento.model';
import { DepartamentoService } from 'app/entities/departamento/service/departamento.service';
import { IPais } from 'app/entities/pais/pais.model';
import { PaisService } from 'app/entities/pais/service/pais.service';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';

import { SucursalUpdateComponent } from './sucursal-update.component';

describe('Sucursal Management Update Component', () => {
  let comp: SucursalUpdateComponent;
  let fixture: ComponentFixture<SucursalUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sucursalService: SucursalService;
  let regimenService: RegimenService;
  let municipioService: MunicipioService;
  let ciudadService: CiudadService;
  let departamentoService: DepartamentoService;
  let paisService: PaisService;
  let empresaService: EmpresaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SucursalUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SucursalUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SucursalUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sucursalService = TestBed.inject(SucursalService);
    regimenService = TestBed.inject(RegimenService);
    municipioService = TestBed.inject(MunicipioService);
    ciudadService = TestBed.inject(CiudadService);
    departamentoService = TestBed.inject(DepartamentoService);
    paisService = TestBed.inject(PaisService);
    empresaService = TestBed.inject(EmpresaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Regimen query and add missing value', () => {
      const sucursal: ISucursal = { id: 456 };
      const regimen: IRegimen = { id: 71438 };
      sucursal.regimen = regimen;

      const regimenCollection: IRegimen[] = [{ id: 70422 }];
      jest.spyOn(regimenService, 'query').mockReturnValue(of(new HttpResponse({ body: regimenCollection })));
      const additionalRegimen = [regimen];
      const expectedCollection: IRegimen[] = [...additionalRegimen, ...regimenCollection];
      jest.spyOn(regimenService, 'addRegimenToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sucursal });
      comp.ngOnInit();

      expect(regimenService.query).toHaveBeenCalled();
      expect(regimenService.addRegimenToCollectionIfMissing).toHaveBeenCalledWith(regimenCollection, ...additionalRegimen);
      expect(comp.regimenSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Municipio query and add missing value', () => {
      const sucursal: ISucursal = { id: 456 };
      const municipio: IMunicipio = { id: 39080 };
      sucursal.municipio = municipio;

      const municipioCollection: IMunicipio[] = [{ id: 14728 }];
      jest.spyOn(municipioService, 'query').mockReturnValue(of(new HttpResponse({ body: municipioCollection })));
      const additionalMunicipios = [municipio];
      const expectedCollection: IMunicipio[] = [...additionalMunicipios, ...municipioCollection];
      jest.spyOn(municipioService, 'addMunicipioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sucursal });
      comp.ngOnInit();

      expect(municipioService.query).toHaveBeenCalled();
      expect(municipioService.addMunicipioToCollectionIfMissing).toHaveBeenCalledWith(municipioCollection, ...additionalMunicipios);
      expect(comp.municipiosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Ciudad query and add missing value', () => {
      const sucursal: ISucursal = { id: 456 };
      const ciudad: ICiudad = { id: 62989 };
      sucursal.ciudad = ciudad;

      const ciudadCollection: ICiudad[] = [{ id: 23107 }];
      jest.spyOn(ciudadService, 'query').mockReturnValue(of(new HttpResponse({ body: ciudadCollection })));
      const additionalCiudads = [ciudad];
      const expectedCollection: ICiudad[] = [...additionalCiudads, ...ciudadCollection];
      jest.spyOn(ciudadService, 'addCiudadToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sucursal });
      comp.ngOnInit();

      expect(ciudadService.query).toHaveBeenCalled();
      expect(ciudadService.addCiudadToCollectionIfMissing).toHaveBeenCalledWith(ciudadCollection, ...additionalCiudads);
      expect(comp.ciudadsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Departamento query and add missing value', () => {
      const sucursal: ISucursal = { id: 456 };
      const departamento: IDepartamento = { id: 78802 };
      sucursal.departamento = departamento;

      const departamentoCollection: IDepartamento[] = [{ id: 29904 }];
      jest.spyOn(departamentoService, 'query').mockReturnValue(of(new HttpResponse({ body: departamentoCollection })));
      const additionalDepartamentos = [departamento];
      const expectedCollection: IDepartamento[] = [...additionalDepartamentos, ...departamentoCollection];
      jest.spyOn(departamentoService, 'addDepartamentoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sucursal });
      comp.ngOnInit();

      expect(departamentoService.query).toHaveBeenCalled();
      expect(departamentoService.addDepartamentoToCollectionIfMissing).toHaveBeenCalledWith(
        departamentoCollection,
        ...additionalDepartamentos
      );
      expect(comp.departamentosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Pais query and add missing value', () => {
      const sucursal: ISucursal = { id: 456 };
      const pais: IPais = { id: 23374 };
      sucursal.pais = pais;

      const paisCollection: IPais[] = [{ id: 49068 }];
      jest.spyOn(paisService, 'query').mockReturnValue(of(new HttpResponse({ body: paisCollection })));
      const additionalPais = [pais];
      const expectedCollection: IPais[] = [...additionalPais, ...paisCollection];
      jest.spyOn(paisService, 'addPaisToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sucursal });
      comp.ngOnInit();

      expect(paisService.query).toHaveBeenCalled();
      expect(paisService.addPaisToCollectionIfMissing).toHaveBeenCalledWith(paisCollection, ...additionalPais);
      expect(comp.paisSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Empresa query and add missing value', () => {
      const sucursal: ISucursal = { id: 456 };
      const empresa: IEmpresa = { id: 45055 };
      sucursal.empresa = empresa;

      const empresaCollection: IEmpresa[] = [{ id: 2081 }];
      jest.spyOn(empresaService, 'query').mockReturnValue(of(new HttpResponse({ body: empresaCollection })));
      const additionalEmpresas = [empresa];
      const expectedCollection: IEmpresa[] = [...additionalEmpresas, ...empresaCollection];
      jest.spyOn(empresaService, 'addEmpresaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sucursal });
      comp.ngOnInit();

      expect(empresaService.query).toHaveBeenCalled();
      expect(empresaService.addEmpresaToCollectionIfMissing).toHaveBeenCalledWith(empresaCollection, ...additionalEmpresas);
      expect(comp.empresasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sucursal: ISucursal = { id: 456 };
      const regimen: IRegimen = { id: 90056 };
      sucursal.regimen = regimen;
      const municipio: IMunicipio = { id: 76804 };
      sucursal.municipio = municipio;
      const ciudad: ICiudad = { id: 17494 };
      sucursal.ciudad = ciudad;
      const departamento: IDepartamento = { id: 12247 };
      sucursal.departamento = departamento;
      const pais: IPais = { id: 72207 };
      sucursal.pais = pais;
      const empresa: IEmpresa = { id: 5564 };
      sucursal.empresa = empresa;

      activatedRoute.data = of({ sucursal });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(sucursal));
      expect(comp.regimenSharedCollection).toContain(regimen);
      expect(comp.municipiosSharedCollection).toContain(municipio);
      expect(comp.ciudadsSharedCollection).toContain(ciudad);
      expect(comp.departamentosSharedCollection).toContain(departamento);
      expect(comp.paisSharedCollection).toContain(pais);
      expect(comp.empresasSharedCollection).toContain(empresa);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Sucursal>>();
      const sucursal = { id: 123 };
      jest.spyOn(sucursalService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sucursal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sucursal }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sucursalService.update).toHaveBeenCalledWith(sucursal);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Sucursal>>();
      const sucursal = new Sucursal();
      jest.spyOn(sucursalService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sucursal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sucursal }));
      saveSubject.complete();

      // THEN
      expect(sucursalService.create).toHaveBeenCalledWith(sucursal);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Sucursal>>();
      const sucursal = { id: 123 };
      jest.spyOn(sucursalService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sucursal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sucursalService.update).toHaveBeenCalledWith(sucursal);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackRegimenById', () => {
      it('Should return tracked Regimen primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackRegimenById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

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

    describe('trackEmpresaById', () => {
      it('Should return tracked Empresa primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEmpresaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
