import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDepartamento, Departamento } from '../departamento.model';

import { DepartamentoService } from './departamento.service';

describe('Departamento Service', () => {
  let service: DepartamentoService;
  let httpMock: HttpTestingController;
  let elemDefault: IDepartamento;
  let expectedResult: IDepartamento | IDepartamento[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DepartamentoService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      codigoDIAN: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Departamento', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Departamento()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Departamento', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          codigoDIAN: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Departamento', () => {
      const patchObject = Object.assign(
        {
          nombre: 'BBBBBB',
          codigoDIAN: 'BBBBBB',
        },
        new Departamento()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Departamento', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          codigoDIAN: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Departamento', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDepartamentoToCollectionIfMissing', () => {
      it('should add a Departamento to an empty array', () => {
        const departamento: IDepartamento = { id: 123 };
        expectedResult = service.addDepartamentoToCollectionIfMissing([], departamento);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(departamento);
      });

      it('should not add a Departamento to an array that contains it', () => {
        const departamento: IDepartamento = { id: 123 };
        const departamentoCollection: IDepartamento[] = [
          {
            ...departamento,
          },
          { id: 456 },
        ];
        expectedResult = service.addDepartamentoToCollectionIfMissing(departamentoCollection, departamento);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Departamento to an array that doesn't contain it", () => {
        const departamento: IDepartamento = { id: 123 };
        const departamentoCollection: IDepartamento[] = [{ id: 456 }];
        expectedResult = service.addDepartamentoToCollectionIfMissing(departamentoCollection, departamento);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(departamento);
      });

      it('should add only unique Departamento to an array', () => {
        const departamentoArray: IDepartamento[] = [{ id: 123 }, { id: 456 }, { id: 18388 }];
        const departamentoCollection: IDepartamento[] = [{ id: 123 }];
        expectedResult = service.addDepartamentoToCollectionIfMissing(departamentoCollection, ...departamentoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const departamento: IDepartamento = { id: 123 };
        const departamento2: IDepartamento = { id: 456 };
        expectedResult = service.addDepartamentoToCollectionIfMissing([], departamento, departamento2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(departamento);
        expect(expectedResult).toContain(departamento2);
      });

      it('should accept null and undefined values', () => {
        const departamento: IDepartamento = { id: 123 };
        expectedResult = service.addDepartamentoToCollectionIfMissing([], null, departamento, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(departamento);
      });

      it('should return initial array if no Departamento is added', () => {
        const departamentoCollection: IDepartamento[] = [{ id: 123 }];
        expectedResult = service.addDepartamentoToCollectionIfMissing(departamentoCollection, undefined, null);
        expect(expectedResult).toEqual(departamentoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
