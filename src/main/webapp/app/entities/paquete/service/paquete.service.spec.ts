import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPaquete, Paquete } from '../paquete.model';

import { PaqueteService } from './paquete.service';

describe('Paquete Service', () => {
  let service: PaqueteService;
  let httpMock: HttpTestingController;
  let elemDefault: IPaquete;
  let expectedResult: IPaquete | IPaquete[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PaqueteService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      cantidad: 0,
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

    it('should create a Paquete', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Paquete()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Paquete', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          cantidad: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Paquete', () => {
      const patchObject = Object.assign(
        {
          cantidad: 1,
        },
        new Paquete()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Paquete', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          cantidad: 1,
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

    it('should delete a Paquete', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPaqueteToCollectionIfMissing', () => {
      it('should add a Paquete to an empty array', () => {
        const paquete: IPaquete = { id: 123 };
        expectedResult = service.addPaqueteToCollectionIfMissing([], paquete);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(paquete);
      });

      it('should not add a Paquete to an array that contains it', () => {
        const paquete: IPaquete = { id: 123 };
        const paqueteCollection: IPaquete[] = [
          {
            ...paquete,
          },
          { id: 456 },
        ];
        expectedResult = service.addPaqueteToCollectionIfMissing(paqueteCollection, paquete);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Paquete to an array that doesn't contain it", () => {
        const paquete: IPaquete = { id: 123 };
        const paqueteCollection: IPaquete[] = [{ id: 456 }];
        expectedResult = service.addPaqueteToCollectionIfMissing(paqueteCollection, paquete);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(paquete);
      });

      it('should add only unique Paquete to an array', () => {
        const paqueteArray: IPaquete[] = [{ id: 123 }, { id: 456 }, { id: 26529 }];
        const paqueteCollection: IPaquete[] = [{ id: 123 }];
        expectedResult = service.addPaqueteToCollectionIfMissing(paqueteCollection, ...paqueteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const paquete: IPaquete = { id: 123 };
        const paquete2: IPaquete = { id: 456 };
        expectedResult = service.addPaqueteToCollectionIfMissing([], paquete, paquete2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(paquete);
        expect(expectedResult).toContain(paquete2);
      });

      it('should accept null and undefined values', () => {
        const paquete: IPaquete = { id: 123 };
        expectedResult = service.addPaqueteToCollectionIfMissing([], null, paquete, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(paquete);
      });

      it('should return initial array if no Paquete is added', () => {
        const paqueteCollection: IPaquete[] = [{ id: 123 }];
        expectedResult = service.addPaqueteToCollectionIfMissing(paqueteCollection, undefined, null);
        expect(expectedResult).toEqual(paqueteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
