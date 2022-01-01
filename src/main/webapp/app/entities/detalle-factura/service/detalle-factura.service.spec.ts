import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDetalleFactura, DetalleFactura } from '../detalle-factura.model';

import { DetalleFacturaService } from './detalle-factura.service';

describe('DetalleFactura Service', () => {
  let service: DetalleFacturaService;
  let httpMock: HttpTestingController;
  let elemDefault: IDetalleFactura;
  let expectedResult: IDetalleFactura | IDetalleFactura[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DetalleFacturaService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      precio: 0,
      cantidad: 0,
      total: 0,
      iva: 0,
      valorImpuesto: 0,
      estado: 0,
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

    it('should create a DetalleFactura', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new DetalleFactura()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DetalleFactura', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          precio: 1,
          cantidad: 1,
          total: 1,
          iva: 1,
          valorImpuesto: 1,
          estado: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DetalleFactura', () => {
      const patchObject = Object.assign(
        {
          precio: 1,
          cantidad: 1,
          total: 1,
          iva: 1,
          estado: 1,
        },
        new DetalleFactura()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DetalleFactura', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          precio: 1,
          cantidad: 1,
          total: 1,
          iva: 1,
          valorImpuesto: 1,
          estado: 1,
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

    it('should delete a DetalleFactura', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDetalleFacturaToCollectionIfMissing', () => {
      it('should add a DetalleFactura to an empty array', () => {
        const detalleFactura: IDetalleFactura = { id: 123 };
        expectedResult = service.addDetalleFacturaToCollectionIfMissing([], detalleFactura);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(detalleFactura);
      });

      it('should not add a DetalleFactura to an array that contains it', () => {
        const detalleFactura: IDetalleFactura = { id: 123 };
        const detalleFacturaCollection: IDetalleFactura[] = [
          {
            ...detalleFactura,
          },
          { id: 456 },
        ];
        expectedResult = service.addDetalleFacturaToCollectionIfMissing(detalleFacturaCollection, detalleFactura);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DetalleFactura to an array that doesn't contain it", () => {
        const detalleFactura: IDetalleFactura = { id: 123 };
        const detalleFacturaCollection: IDetalleFactura[] = [{ id: 456 }];
        expectedResult = service.addDetalleFacturaToCollectionIfMissing(detalleFacturaCollection, detalleFactura);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(detalleFactura);
      });

      it('should add only unique DetalleFactura to an array', () => {
        const detalleFacturaArray: IDetalleFactura[] = [{ id: 123 }, { id: 456 }, { id: 94105 }];
        const detalleFacturaCollection: IDetalleFactura[] = [{ id: 123 }];
        expectedResult = service.addDetalleFacturaToCollectionIfMissing(detalleFacturaCollection, ...detalleFacturaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const detalleFactura: IDetalleFactura = { id: 123 };
        const detalleFactura2: IDetalleFactura = { id: 456 };
        expectedResult = service.addDetalleFacturaToCollectionIfMissing([], detalleFactura, detalleFactura2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(detalleFactura);
        expect(expectedResult).toContain(detalleFactura2);
      });

      it('should accept null and undefined values', () => {
        const detalleFactura: IDetalleFactura = { id: 123 };
        expectedResult = service.addDetalleFacturaToCollectionIfMissing([], null, detalleFactura, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(detalleFactura);
      });

      it('should return initial array if no DetalleFactura is added', () => {
        const detalleFacturaCollection: IDetalleFactura[] = [{ id: 123 }];
        expectedResult = service.addDetalleFacturaToCollectionIfMissing(detalleFacturaCollection, undefined, null);
        expect(expectedResult).toEqual(detalleFacturaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
