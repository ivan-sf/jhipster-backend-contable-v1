import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { INotaContable, NotaContable } from '../nota-contable.model';

import { NotaContableService } from './nota-contable.service';

describe('NotaContable Service', () => {
  let service: NotaContableService;
  let httpMock: HttpTestingController;
  let elemDefault: INotaContable;
  let expectedResult: INotaContable | INotaContable[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(NotaContableService);
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

    it('should create a NotaContable', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new NotaContable()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a NotaContable', () => {
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

    it('should partial update a NotaContable', () => {
      const patchObject = Object.assign({}, new NotaContable());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of NotaContable', () => {
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

    it('should delete a NotaContable', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addNotaContableToCollectionIfMissing', () => {
      it('should add a NotaContable to an empty array', () => {
        const notaContable: INotaContable = { id: 123 };
        expectedResult = service.addNotaContableToCollectionIfMissing([], notaContable);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(notaContable);
      });

      it('should not add a NotaContable to an array that contains it', () => {
        const notaContable: INotaContable = { id: 123 };
        const notaContableCollection: INotaContable[] = [
          {
            ...notaContable,
          },
          { id: 456 },
        ];
        expectedResult = service.addNotaContableToCollectionIfMissing(notaContableCollection, notaContable);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a NotaContable to an array that doesn't contain it", () => {
        const notaContable: INotaContable = { id: 123 };
        const notaContableCollection: INotaContable[] = [{ id: 456 }];
        expectedResult = service.addNotaContableToCollectionIfMissing(notaContableCollection, notaContable);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(notaContable);
      });

      it('should add only unique NotaContable to an array', () => {
        const notaContableArray: INotaContable[] = [{ id: 123 }, { id: 456 }, { id: 3222 }];
        const notaContableCollection: INotaContable[] = [{ id: 123 }];
        expectedResult = service.addNotaContableToCollectionIfMissing(notaContableCollection, ...notaContableArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const notaContable: INotaContable = { id: 123 };
        const notaContable2: INotaContable = { id: 456 };
        expectedResult = service.addNotaContableToCollectionIfMissing([], notaContable, notaContable2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(notaContable);
        expect(expectedResult).toContain(notaContable2);
      });

      it('should accept null and undefined values', () => {
        const notaContable: INotaContable = { id: 123 };
        expectedResult = service.addNotaContableToCollectionIfMissing([], null, notaContable, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(notaContable);
      });

      it('should return initial array if no NotaContable is added', () => {
        const notaContableCollection: INotaContable[] = [{ id: 123 }];
        expectedResult = service.addNotaContableToCollectionIfMissing(notaContableCollection, undefined, null);
        expect(expectedResult).toEqual(notaContableCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
