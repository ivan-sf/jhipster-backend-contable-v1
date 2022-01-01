import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IVencimiento, Vencimiento } from '../vencimiento.model';

import { VencimientoService } from './vencimiento.service';

describe('Vencimiento Service', () => {
  let service: VencimientoService;
  let httpMock: HttpTestingController;
  let elemDefault: IVencimiento;
  let expectedResult: IVencimiento | IVencimiento[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VencimientoService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      fecha: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fecha: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Vencimiento', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fecha: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.create(new Vencimiento()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Vencimiento', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fecha: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Vencimiento', () => {
      const patchObject = Object.assign({}, new Vencimiento());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Vencimiento', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fecha: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Vencimiento', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addVencimientoToCollectionIfMissing', () => {
      it('should add a Vencimiento to an empty array', () => {
        const vencimiento: IVencimiento = { id: 123 };
        expectedResult = service.addVencimientoToCollectionIfMissing([], vencimiento);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vencimiento);
      });

      it('should not add a Vencimiento to an array that contains it', () => {
        const vencimiento: IVencimiento = { id: 123 };
        const vencimientoCollection: IVencimiento[] = [
          {
            ...vencimiento,
          },
          { id: 456 },
        ];
        expectedResult = service.addVencimientoToCollectionIfMissing(vencimientoCollection, vencimiento);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Vencimiento to an array that doesn't contain it", () => {
        const vencimiento: IVencimiento = { id: 123 };
        const vencimientoCollection: IVencimiento[] = [{ id: 456 }];
        expectedResult = service.addVencimientoToCollectionIfMissing(vencimientoCollection, vencimiento);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vencimiento);
      });

      it('should add only unique Vencimiento to an array', () => {
        const vencimientoArray: IVencimiento[] = [{ id: 123 }, { id: 456 }, { id: 50383 }];
        const vencimientoCollection: IVencimiento[] = [{ id: 123 }];
        expectedResult = service.addVencimientoToCollectionIfMissing(vencimientoCollection, ...vencimientoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vencimiento: IVencimiento = { id: 123 };
        const vencimiento2: IVencimiento = { id: 456 };
        expectedResult = service.addVencimientoToCollectionIfMissing([], vencimiento, vencimiento2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vencimiento);
        expect(expectedResult).toContain(vencimiento2);
      });

      it('should accept null and undefined values', () => {
        const vencimiento: IVencimiento = { id: 123 };
        expectedResult = service.addVencimientoToCollectionIfMissing([], null, vencimiento, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vencimiento);
      });

      it('should return initial array if no Vencimiento is added', () => {
        const vencimientoCollection: IVencimiento[] = [{ id: 123 }];
        expectedResult = service.addVencimientoToCollectionIfMissing(vencimientoCollection, undefined, null);
        expect(expectedResult).toEqual(vencimientoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
