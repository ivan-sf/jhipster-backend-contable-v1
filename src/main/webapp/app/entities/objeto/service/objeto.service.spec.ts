import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IObjeto, Objeto } from '../objeto.model';

import { ObjetoService } from './objeto.service';

describe('Objeto Service', () => {
  let service: ObjetoService;
  let httpMock: HttpTestingController;
  let elemDefault: IObjeto;
  let expectedResult: IObjeto | IObjeto[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ObjetoService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      cantidad: 0,
      codigoDian: 'AAAAAAA',
      detalleObjeto: 0,
      vencimiento: currentDate,
      fechaRegistro: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          vencimiento: currentDate.format(DATE_TIME_FORMAT),
          fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Objeto', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          vencimiento: currentDate.format(DATE_TIME_FORMAT),
          fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          vencimiento: currentDate,
          fechaRegistro: currentDate,
        },
        returnedFromService
      );

      service.create(new Objeto()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Objeto', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          cantidad: 1,
          codigoDian: 'BBBBBB',
          detalleObjeto: 1,
          vencimiento: currentDate.format(DATE_TIME_FORMAT),
          fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          vencimiento: currentDate,
          fechaRegistro: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Objeto', () => {
      const patchObject = Object.assign(
        {
          nombre: 'BBBBBB',
          cantidad: 1,
          fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
        },
        new Objeto()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          vencimiento: currentDate,
          fechaRegistro: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Objeto', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          cantidad: 1,
          codigoDian: 'BBBBBB',
          detalleObjeto: 1,
          vencimiento: currentDate.format(DATE_TIME_FORMAT),
          fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          vencimiento: currentDate,
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

    it('should delete a Objeto', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addObjetoToCollectionIfMissing', () => {
      it('should add a Objeto to an empty array', () => {
        const objeto: IObjeto = { id: 123 };
        expectedResult = service.addObjetoToCollectionIfMissing([], objeto);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(objeto);
      });

      it('should not add a Objeto to an array that contains it', () => {
        const objeto: IObjeto = { id: 123 };
        const objetoCollection: IObjeto[] = [
          {
            ...objeto,
          },
          { id: 456 },
        ];
        expectedResult = service.addObjetoToCollectionIfMissing(objetoCollection, objeto);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Objeto to an array that doesn't contain it", () => {
        const objeto: IObjeto = { id: 123 };
        const objetoCollection: IObjeto[] = [{ id: 456 }];
        expectedResult = service.addObjetoToCollectionIfMissing(objetoCollection, objeto);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(objeto);
      });

      it('should add only unique Objeto to an array', () => {
        const objetoArray: IObjeto[] = [{ id: 123 }, { id: 456 }, { id: 77485 }];
        const objetoCollection: IObjeto[] = [{ id: 123 }];
        expectedResult = service.addObjetoToCollectionIfMissing(objetoCollection, ...objetoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const objeto: IObjeto = { id: 123 };
        const objeto2: IObjeto = { id: 456 };
        expectedResult = service.addObjetoToCollectionIfMissing([], objeto, objeto2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(objeto);
        expect(expectedResult).toContain(objeto2);
      });

      it('should accept null and undefined values', () => {
        const objeto: IObjeto = { id: 123 };
        expectedResult = service.addObjetoToCollectionIfMissing([], null, objeto, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(objeto);
      });

      it('should return initial array if no Objeto is added', () => {
        const objetoCollection: IObjeto[] = [{ id: 123 }];
        expectedResult = service.addObjetoToCollectionIfMissing(objetoCollection, undefined, null);
        expect(expectedResult).toEqual(objetoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
