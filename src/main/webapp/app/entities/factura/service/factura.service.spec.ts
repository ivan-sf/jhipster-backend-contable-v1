import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFactura, Factura } from '../factura.model';

import { FacturaService } from './factura.service';

describe('Factura Service', () => {
  let service: FacturaService;
  let httpMock: HttpTestingController;
  let elemDefault: IFactura;
  let expectedResult: IFactura | IFactura[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FacturaService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      numero: 0,
      caja: 'AAAAAAA',
      estado: 0,
      iva19: 0,
      baseIva19: 0,
      iva5: 0,
      baseIva5: 0,
      iva0: 0,
      baseIva0: 0,
      total: 0,
      pago: 0,
      saldo: 0,
      fechaRegistro: currentDate,
      fechaActualizacion: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
          fechaActualizacion: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Factura', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
          fechaActualizacion: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaRegistro: currentDate,
          fechaActualizacion: currentDate,
        },
        returnedFromService
      );

      service.create(new Factura()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Factura', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          numero: 1,
          caja: 'BBBBBB',
          estado: 1,
          iva19: 1,
          baseIva19: 1,
          iva5: 1,
          baseIva5: 1,
          iva0: 1,
          baseIva0: 1,
          total: 1,
          pago: 1,
          saldo: 1,
          fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
          fechaActualizacion: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaRegistro: currentDate,
          fechaActualizacion: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Factura', () => {
      const patchObject = Object.assign(
        {
          numero: 1,
          caja: 'BBBBBB',
          iva19: 1,
          baseIva5: 1,
          total: 1,
          pago: 1,
          fechaActualizacion: currentDate.format(DATE_TIME_FORMAT),
        },
        new Factura()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fechaRegistro: currentDate,
          fechaActualizacion: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Factura', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          numero: 1,
          caja: 'BBBBBB',
          estado: 1,
          iva19: 1,
          baseIva19: 1,
          iva5: 1,
          baseIva5: 1,
          iva0: 1,
          baseIva0: 1,
          total: 1,
          pago: 1,
          saldo: 1,
          fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
          fechaActualizacion: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaRegistro: currentDate,
          fechaActualizacion: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Factura', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFacturaToCollectionIfMissing', () => {
      it('should add a Factura to an empty array', () => {
        const factura: IFactura = { id: 123 };
        expectedResult = service.addFacturaToCollectionIfMissing([], factura);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(factura);
      });

      it('should not add a Factura to an array that contains it', () => {
        const factura: IFactura = { id: 123 };
        const facturaCollection: IFactura[] = [
          {
            ...factura,
          },
          { id: 456 },
        ];
        expectedResult = service.addFacturaToCollectionIfMissing(facturaCollection, factura);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Factura to an array that doesn't contain it", () => {
        const factura: IFactura = { id: 123 };
        const facturaCollection: IFactura[] = [{ id: 456 }];
        expectedResult = service.addFacturaToCollectionIfMissing(facturaCollection, factura);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(factura);
      });

      it('should add only unique Factura to an array', () => {
        const facturaArray: IFactura[] = [{ id: 123 }, { id: 456 }, { id: 23402 }];
        const facturaCollection: IFactura[] = [{ id: 123 }];
        expectedResult = service.addFacturaToCollectionIfMissing(facturaCollection, ...facturaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const factura: IFactura = { id: 123 };
        const factura2: IFactura = { id: 456 };
        expectedResult = service.addFacturaToCollectionIfMissing([], factura, factura2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(factura);
        expect(expectedResult).toContain(factura2);
      });

      it('should accept null and undefined values', () => {
        const factura: IFactura = { id: 123 };
        expectedResult = service.addFacturaToCollectionIfMissing([], null, factura, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(factura);
      });

      it('should return initial array if no Factura is added', () => {
        const facturaCollection: IFactura[] = [{ id: 123 }];
        expectedResult = service.addFacturaToCollectionIfMissing(facturaCollection, undefined, null);
        expect(expectedResult).toEqual(facturaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
