import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDetallesObjeto, DetallesObjeto } from '../detalles-objeto.model';

import { DetallesObjetoService } from './detalles-objeto.service';

describe('DetallesObjeto Service', () => {
  let service: DetallesObjetoService;
  let httpMock: HttpTestingController;
  let elemDefault: IDetallesObjeto;
  let expectedResult: IDetallesObjeto | IDetallesObjeto[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DetallesObjetoService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      descripcion: 0,
      fechaRegistro: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a DetallesObjeto', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaRegistro: currentDate,
        },
        returnedFromService
      );

      service.create(new DetallesObjeto()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DetallesObjeto', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          descripcion: 1,
          fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaRegistro: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DetallesObjeto', () => {
      const patchObject = Object.assign(
        {
          descripcion: 1,
        },
        new DetallesObjeto()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fechaRegistro: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DetallesObjeto', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          descripcion: 1,
          fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaRegistro: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a DetallesObjeto', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDetallesObjetoToCollectionIfMissing', () => {
      it('should add a DetallesObjeto to an empty array', () => {
        const detallesObjeto: IDetallesObjeto = { id: 123 };
        expectedResult = service.addDetallesObjetoToCollectionIfMissing([], detallesObjeto);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(detallesObjeto);
      });

      it('should not add a DetallesObjeto to an array that contains it', () => {
        const detallesObjeto: IDetallesObjeto = { id: 123 };
        const detallesObjetoCollection: IDetallesObjeto[] = [
          {
            ...detallesObjeto,
          },
          { id: 456 },
        ];
        expectedResult = service.addDetallesObjetoToCollectionIfMissing(detallesObjetoCollection, detallesObjeto);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DetallesObjeto to an array that doesn't contain it", () => {
        const detallesObjeto: IDetallesObjeto = { id: 123 };
        const detallesObjetoCollection: IDetallesObjeto[] = [{ id: 456 }];
        expectedResult = service.addDetallesObjetoToCollectionIfMissing(detallesObjetoCollection, detallesObjeto);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(detallesObjeto);
      });

      it('should add only unique DetallesObjeto to an array', () => {
        const detallesObjetoArray: IDetallesObjeto[] = [{ id: 123 }, { id: 456 }, { id: 37596 }];
        const detallesObjetoCollection: IDetallesObjeto[] = [{ id: 123 }];
        expectedResult = service.addDetallesObjetoToCollectionIfMissing(detallesObjetoCollection, ...detallesObjetoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const detallesObjeto: IDetallesObjeto = { id: 123 };
        const detallesObjeto2: IDetallesObjeto = { id: 456 };
        expectedResult = service.addDetallesObjetoToCollectionIfMissing([], detallesObjeto, detallesObjeto2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(detallesObjeto);
        expect(expectedResult).toContain(detallesObjeto2);
      });

      it('should accept null and undefined values', () => {
        const detallesObjeto: IDetallesObjeto = { id: 123 };
        expectedResult = service.addDetallesObjetoToCollectionIfMissing([], null, detallesObjeto, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(detallesObjeto);
      });

      it('should return initial array if no DetallesObjeto is added', () => {
        const detallesObjetoCollection: IDetallesObjeto[] = [{ id: 123 }];
        expectedResult = service.addDetallesObjetoToCollectionIfMissing(detallesObjetoCollection, undefined, null);
        expect(expectedResult).toEqual(detallesObjetoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
