import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITipoFactura, TipoFactura } from '../tipo-factura.model';

import { TipoFacturaService } from './tipo-factura.service';

describe('TipoFactura Service', () => {
  let service: TipoFacturaService;
  let httpMock: HttpTestingController;
  let elemDefault: ITipoFactura;
  let expectedResult: ITipoFactura | ITipoFactura[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TipoFacturaService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      prefijoInicial: 0,
      prefijoFinal: 0,
      prefijoActual: 0,
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

    it('should create a TipoFactura', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TipoFactura()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TipoFactura', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          prefijoInicial: 1,
          prefijoFinal: 1,
          prefijoActual: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TipoFactura', () => {
      const patchObject = Object.assign({}, new TipoFactura());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TipoFactura', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          prefijoInicial: 1,
          prefijoFinal: 1,
          prefijoActual: 1,
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

    it('should delete a TipoFactura', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTipoFacturaToCollectionIfMissing', () => {
      it('should add a TipoFactura to an empty array', () => {
        const tipoFactura: ITipoFactura = { id: 123 };
        expectedResult = service.addTipoFacturaToCollectionIfMissing([], tipoFactura);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoFactura);
      });

      it('should not add a TipoFactura to an array that contains it', () => {
        const tipoFactura: ITipoFactura = { id: 123 };
        const tipoFacturaCollection: ITipoFactura[] = [
          {
            ...tipoFactura,
          },
          { id: 456 },
        ];
        expectedResult = service.addTipoFacturaToCollectionIfMissing(tipoFacturaCollection, tipoFactura);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TipoFactura to an array that doesn't contain it", () => {
        const tipoFactura: ITipoFactura = { id: 123 };
        const tipoFacturaCollection: ITipoFactura[] = [{ id: 456 }];
        expectedResult = service.addTipoFacturaToCollectionIfMissing(tipoFacturaCollection, tipoFactura);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoFactura);
      });

      it('should add only unique TipoFactura to an array', () => {
        const tipoFacturaArray: ITipoFactura[] = [{ id: 123 }, { id: 456 }, { id: 37324 }];
        const tipoFacturaCollection: ITipoFactura[] = [{ id: 123 }];
        expectedResult = service.addTipoFacturaToCollectionIfMissing(tipoFacturaCollection, ...tipoFacturaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tipoFactura: ITipoFactura = { id: 123 };
        const tipoFactura2: ITipoFactura = { id: 456 };
        expectedResult = service.addTipoFacturaToCollectionIfMissing([], tipoFactura, tipoFactura2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoFactura);
        expect(expectedResult).toContain(tipoFactura2);
      });

      it('should accept null and undefined values', () => {
        const tipoFactura: ITipoFactura = { id: 123 };
        expectedResult = service.addTipoFacturaToCollectionIfMissing([], null, tipoFactura, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoFactura);
      });

      it('should return initial array if no TipoFactura is added', () => {
        const tipoFacturaCollection: ITipoFactura[] = [{ id: 123 }];
        expectedResult = service.addTipoFacturaToCollectionIfMissing(tipoFacturaCollection, undefined, null);
        expect(expectedResult).toEqual(tipoFacturaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
