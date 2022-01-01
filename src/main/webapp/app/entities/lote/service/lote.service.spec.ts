import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILote, Lote } from '../lote.model';

import { LoteService } from './lote.service';

describe('Lote Service', () => {
  let service: LoteService;
  let httpMock: HttpTestingController;
  let elemDefault: ILote;
  let expectedResult: ILote | ILote[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LoteService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      numero: 0,
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

    it('should create a Lote', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Lote()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Lote', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          numero: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Lote', () => {
      const patchObject = Object.assign({}, new Lote());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Lote', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          numero: 1,
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

    it('should delete a Lote', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLoteToCollectionIfMissing', () => {
      it('should add a Lote to an empty array', () => {
        const lote: ILote = { id: 123 };
        expectedResult = service.addLoteToCollectionIfMissing([], lote);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(lote);
      });

      it('should not add a Lote to an array that contains it', () => {
        const lote: ILote = { id: 123 };
        const loteCollection: ILote[] = [
          {
            ...lote,
          },
          { id: 456 },
        ];
        expectedResult = service.addLoteToCollectionIfMissing(loteCollection, lote);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Lote to an array that doesn't contain it", () => {
        const lote: ILote = { id: 123 };
        const loteCollection: ILote[] = [{ id: 456 }];
        expectedResult = service.addLoteToCollectionIfMissing(loteCollection, lote);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(lote);
      });

      it('should add only unique Lote to an array', () => {
        const loteArray: ILote[] = [{ id: 123 }, { id: 456 }, { id: 77968 }];
        const loteCollection: ILote[] = [{ id: 123 }];
        expectedResult = service.addLoteToCollectionIfMissing(loteCollection, ...loteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const lote: ILote = { id: 123 };
        const lote2: ILote = { id: 456 };
        expectedResult = service.addLoteToCollectionIfMissing([], lote, lote2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(lote);
        expect(expectedResult).toContain(lote2);
      });

      it('should accept null and undefined values', () => {
        const lote: ILote = { id: 123 };
        expectedResult = service.addLoteToCollectionIfMissing([], null, lote, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(lote);
      });

      it('should return initial array if no Lote is added', () => {
        const loteCollection: ILote[] = [{ id: 123 }];
        expectedResult = service.addLoteToCollectionIfMissing(loteCollection, undefined, null);
        expect(expectedResult).toEqual(loteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
