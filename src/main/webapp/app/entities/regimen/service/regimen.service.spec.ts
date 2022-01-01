import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRegimen, Regimen } from '../regimen.model';

import { RegimenService } from './regimen.service';

describe('Regimen Service', () => {
  let service: RegimenService;
  let httpMock: HttpTestingController;
  let elemDefault: IRegimen;
  let expectedResult: IRegimen | IRegimen[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RegimenService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      tipoRegimen: 'AAAAAAA',
      nombreRegimen: 'AAAAAAA',
      fechaRegistro: 'AAAAAAA',
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

    it('should create a Regimen', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Regimen()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Regimen', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          tipoRegimen: 'BBBBBB',
          nombreRegimen: 'BBBBBB',
          fechaRegistro: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Regimen', () => {
      const patchObject = Object.assign(
        {
          tipoRegimen: 'BBBBBB',
          fechaRegistro: 'BBBBBB',
        },
        new Regimen()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Regimen', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          tipoRegimen: 'BBBBBB',
          nombreRegimen: 'BBBBBB',
          fechaRegistro: 'BBBBBB',
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

    it('should delete a Regimen', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRegimenToCollectionIfMissing', () => {
      it('should add a Regimen to an empty array', () => {
        const regimen: IRegimen = { id: 123 };
        expectedResult = service.addRegimenToCollectionIfMissing([], regimen);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(regimen);
      });

      it('should not add a Regimen to an array that contains it', () => {
        const regimen: IRegimen = { id: 123 };
        const regimenCollection: IRegimen[] = [
          {
            ...regimen,
          },
          { id: 456 },
        ];
        expectedResult = service.addRegimenToCollectionIfMissing(regimenCollection, regimen);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Regimen to an array that doesn't contain it", () => {
        const regimen: IRegimen = { id: 123 };
        const regimenCollection: IRegimen[] = [{ id: 456 }];
        expectedResult = service.addRegimenToCollectionIfMissing(regimenCollection, regimen);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(regimen);
      });

      it('should add only unique Regimen to an array', () => {
        const regimenArray: IRegimen[] = [{ id: 123 }, { id: 456 }, { id: 37397 }];
        const regimenCollection: IRegimen[] = [{ id: 123 }];
        expectedResult = service.addRegimenToCollectionIfMissing(regimenCollection, ...regimenArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const regimen: IRegimen = { id: 123 };
        const regimen2: IRegimen = { id: 456 };
        expectedResult = service.addRegimenToCollectionIfMissing([], regimen, regimen2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(regimen);
        expect(expectedResult).toContain(regimen2);
      });

      it('should accept null and undefined values', () => {
        const regimen: IRegimen = { id: 123 };
        expectedResult = service.addRegimenToCollectionIfMissing([], null, regimen, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(regimen);
      });

      it('should return initial array if no Regimen is added', () => {
        const regimenCollection: IRegimen[] = [{ id: 123 }];
        expectedResult = service.addRegimenToCollectionIfMissing(regimenCollection, undefined, null);
        expect(expectedResult).toEqual(regimenCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
