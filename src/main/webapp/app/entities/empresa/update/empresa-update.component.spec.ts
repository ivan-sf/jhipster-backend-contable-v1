jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EmpresaService } from '../service/empresa.service';
import { IEmpresa, Empresa } from '../empresa.model';
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
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

import { EmpresaUpdateComponent } from './empresa-update.component';

describe('Empresa Management Update Component', () => {
  let comp: EmpresaUpdateComponent;
  let fixture: ComponentFixture<EmpresaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let empresaService: EmpresaService;
  let regimenService: RegimenService;
  let municipioService: MunicipioService;
  let ciudadService: CiudadService;
  let departamentoService: DepartamentoService;
  let paisService: PaisService;
  let usuarioService: UsuarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EmpresaUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(EmpresaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmpresaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    empresaService = TestBed.inject(EmpresaService);
    regimenService = TestBed.inject(RegimenService);
    municipioService = TestBed.inject(MunicipioService);
    ciudadService = TestBed.inject(CiudadService);
    departamentoService = TestBed.inject(DepartamentoService);
    paisService = TestBed.inject(PaisService);
    usuarioService = TestBed.inject(UsuarioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Regimen query and add missing value', () => {
      const empresa: IEmpresa = { id: 456 };
      const regimen: IRegimen = { id: 34121 };
      empresa.regimen = regimen;

      const regimenCollection: IRegimen[] = [{ id: 90383 }];
      jest.spyOn(regimenService, 'query').mockReturnValue(of(new HttpResponse({ body: regimenCollection })));
      const additionalRegimen = [regimen];
      const expectedCollection: IRegimen[] = [...additionalRegimen, ...regimenCollection];
      jest.spyOn(regimenService, 'addRegimenToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ empresa });
      comp.ngOnInit();

      expect(regimenService.query).toHaveBeenCalled();
      expect(regimenService.addRegimenToCollectionIfMissing).toHaveBeenCalledWith(regimenCollection, ...additionalRegimen);
      expect(comp.regimenSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Municipio query and add missing value', () => {
      const empresa: IEmpresa = { id: 456 };
      const municipio: IMunicipio = { id: 11335 };
      empresa.municipio = municipio;

      const municipioCollection: IMunicipio[] = [{ id: 2671 }];
      jest.spyOn(municipioService, 'query').mockReturnValue(of(new HttpResponse({ body: municipioCollection })));
      const additionalMunicipios = [municipio];
      const expectedCollection: IMunicipio[] = [...additionalMunicipios, ...municipioCollection];
      jest.spyOn(municipioService, 'addMunicipioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ empresa });
      comp.ngOnInit();

      expect(municipioService.query).toHaveBeenCalled();
      expect(municipioService.addMunicipioToCollectionIfMissing).toHaveBeenCalledWith(municipioCollection, ...additionalMunicipios);
      expect(comp.municipiosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Ciudad query and add missing value', () => {
      const empresa: IEmpresa = { id: 456 };
      const ciudad: ICiudad = { id: 45342 };
      empresa.ciudad = ciudad;

      const ciudadCollection: ICiudad[] = [{ id: 53709 }];
      jest.spyOn(ciudadService, 'query').mockReturnValue(of(new HttpResponse({ body: ciudadCollection })));
      const additionalCiudads = [ciudad];
      const expectedCollection: ICiudad[] = [...additionalCiudads, ...ciudadCollection];
      jest.spyOn(ciudadService, 'addCiudadToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ empresa });
      comp.ngOnInit();

      expect(ciudadService.query).toHaveBeenCalled();
      expect(ciudadService.addCiudadToCollectionIfMissing).toHaveBeenCalledWith(ciudadCollection, ...additionalCiudads);
      expect(comp.ciudadsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Departamento query and add missing value', () => {
      const empresa: IEmpresa = { id: 456 };
      const departamento: IDepartamento = { id: 6427 };
      empresa.departamento = departamento;

      const departamentoCollection: IDepartamento[] = [{ id: 94509 }];
      jest.spyOn(departamentoService, 'query').mockReturnValue(of(new HttpResponse({ body: departamentoCollection })));
      const additionalDepartamentos = [departamento];
      const expectedCollection: IDepartamento[] = [...additionalDepartamentos, ...departamentoCollection];
      jest.spyOn(departamentoService, 'addDepartamentoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ empresa });
      comp.ngOnInit();

      expect(departamentoService.query).toHaveBeenCalled();
      expect(departamentoService.addDepartamentoToCollectionIfMissing).toHaveBeenCalledWith(
        departamentoCollection,
        ...additionalDepartamentos
      );
      expect(comp.departamentosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Pais query and add missing value', () => {
      const empresa: IEmpresa = { id: 456 };
      const pais: IPais = { id: 46838 };
      empresa.pais = pais;

      const paisCollection: IPais[] = [{ id: 76147 }];
      jest.spyOn(paisService, 'query').mockReturnValue(of(new HttpResponse({ body: paisCollection })));
      const additionalPais = [pais];
      const expectedCollection: IPais[] = [...additionalPais, ...paisCollection];
      jest.spyOn(paisService, 'addPaisToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ empresa });
      comp.ngOnInit();

      expect(paisService.query).toHaveBeenCalled();
      expect(paisService.addPaisToCollectionIfMissing).toHaveBeenCalledWith(paisCollection, ...additionalPais);
      expect(comp.paisSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Usuario query and add missing value', () => {
      const empresa: IEmpresa = { id: 456 };
      const usuarios: IUsuario[] = [{ id: 26351 }];
      empresa.usuarios = usuarios;

      const usuarioCollection: IUsuario[] = [{ id: 39155 }];
      jest.spyOn(usuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioCollection })));
      const additionalUsuarios = [...usuarios];
      const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
      jest.spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ empresa });
      comp.ngOnInit();

      expect(usuarioService.query).toHaveBeenCalled();
      expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(usuarioCollection, ...additionalUsuarios);
      expect(comp.usuariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const empresa: IEmpresa = { id: 456 };
      const regimen: IRegimen = { id: 98444 };
      empresa.regimen = regimen;
      const municipio: IMunicipio = { id: 41957 };
      empresa.municipio = municipio;
      const ciudad: ICiudad = { id: 50970 };
      empresa.ciudad = ciudad;
      const departamento: IDepartamento = { id: 47268 };
      empresa.departamento = departamento;
      const pais: IPais = { id: 33949 };
      empresa.pais = pais;
      const usuarios: IUsuario = { id: 25336 };
      empresa.usuarios = [usuarios];

      activatedRoute.data = of({ empresa });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(empresa));
      expect(comp.regimenSharedCollection).toContain(regimen);
      expect(comp.municipiosSharedCollection).toContain(municipio);
      expect(comp.ciudadsSharedCollection).toContain(ciudad);
      expect(comp.departamentosSharedCollection).toContain(departamento);
      expect(comp.paisSharedCollection).toContain(pais);
      expect(comp.usuariosSharedCollection).toContain(usuarios);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Empresa>>();
      const empresa = { id: 123 };
      jest.spyOn(empresaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empresa });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: empresa }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(empresaService.update).toHaveBeenCalledWith(empresa);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Empresa>>();
      const empresa = new Empresa();
      jest.spyOn(empresaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empresa });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: empresa }));
      saveSubject.complete();

      // THEN
      expect(empresaService.create).toHaveBeenCalledWith(empresa);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Empresa>>();
      const empresa = { id: 123 };
      jest.spyOn(empresaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empresa });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(empresaService.update).toHaveBeenCalledWith(empresa);
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

    describe('trackUsuarioById', () => {
      it('Should return tracked Usuario primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUsuarioById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedUsuario', () => {
      it('Should return option if no Usuario is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedUsuario(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Usuario for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedUsuario(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Usuario is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedUsuario(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
