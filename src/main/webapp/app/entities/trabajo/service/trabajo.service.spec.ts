import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITrabajo, Trabajo } from '../trabajo.model';

import { TrabajoService } from './trabajo.service';

describe('Trabajo Service', () => {
  let service: TrabajoService;
  let httpMock: HttpTestingController;
  let elemDefault: ITrabajo;
  let expectedResult: ITrabajo | ITrabajo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TrabajoService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      cargo: 'AAAAAAA',
      salario: 0,
      salud: 'AAAAAAA',
      pension: 'AAAAAAA',
      observacon: 'AAAAAAA',
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

    it('should create a Trabajo', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Trabajo()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Trabajo', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          cargo: 'BBBBBB',
          salario: 1,
          salud: 'BBBBBB',
          pension: 'BBBBBB',
          observacon: 'BBBBBB',
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

    it('should partial update a Trabajo', () => {
      const patchObject = Object.assign(
        {
          nombre: 'BBBBBB',
          cargo: 'BBBBBB',
          salario: 1,
          fechaRegistro: 'BBBBBB',
        },
        new Trabajo()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Trabajo', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          cargo: 'BBBBBB',
          salario: 1,
          salud: 'BBBBBB',
          pension: 'BBBBBB',
          observacon: 'BBBBBB',
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

    it('should delete a Trabajo', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTrabajoToCollectionIfMissing', () => {
      it('should add a Trabajo to an empty array', () => {
        const trabajo: ITrabajo = { id: 123 };
        expectedResult = service.addTrabajoToCollectionIfMissing([], trabajo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trabajo);
      });

      it('should not add a Trabajo to an array that contains it', () => {
        const trabajo: ITrabajo = { id: 123 };
        const trabajoCollection: ITrabajo[] = [
          {
            ...trabajo,
          },
          { id: 456 },
        ];
        expectedResult = service.addTrabajoToCollectionIfMissing(trabajoCollection, trabajo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Trabajo to an array that doesn't contain it", () => {
        const trabajo: ITrabajo = { id: 123 };
        const trabajoCollection: ITrabajo[] = [{ id: 456 }];
        expectedResult = service.addTrabajoToCollectionIfMissing(trabajoCollection, trabajo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trabajo);
      });

      it('should add only unique Trabajo to an array', () => {
        const trabajoArray: ITrabajo[] = [{ id: 123 }, { id: 456 }, { id: 47360 }];
        const trabajoCollection: ITrabajo[] = [{ id: 123 }];
        expectedResult = service.addTrabajoToCollectionIfMissing(trabajoCollection, ...trabajoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const trabajo: ITrabajo = { id: 123 };
        const trabajo2: ITrabajo = { id: 456 };
        expectedResult = service.addTrabajoToCollectionIfMissing([], trabajo, trabajo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trabajo);
        expect(expectedResult).toContain(trabajo2);
      });

      it('should accept null and undefined values', () => {
        const trabajo: ITrabajo = { id: 123 };
        expectedResult = service.addTrabajoToCollectionIfMissing([], null, trabajo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trabajo);
      });

      it('should return initial array if no Trabajo is added', () => {
        const trabajoCollection: ITrabajo[] = [{ id: 123 }];
        expectedResult = service.addTrabajoToCollectionIfMissing(trabajoCollection, undefined, null);
        expect(expectedResult).toEqual(trabajoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
