import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITipoObjeto, TipoObjeto } from '../tipo-objeto.model';

import { TipoObjetoService } from './tipo-objeto.service';

describe('TipoObjeto Service', () => {
  let service: TipoObjetoService;
  let httpMock: HttpTestingController;
  let elemDefault: ITipoObjeto;
  let expectedResult: ITipoObjeto | ITipoObjeto[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TipoObjetoService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      fechaRegistro: 'AAAAAAA',
      codigoDian: 'AAAAAAA',
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

    it('should create a TipoObjeto', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TipoObjeto()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TipoObjeto', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          fechaRegistro: 'BBBBBB',
          codigoDian: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TipoObjeto', () => {
      const patchObject = Object.assign(
        {
          codigoDian: 'BBBBBB',
        },
        new TipoObjeto()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TipoObjeto', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          fechaRegistro: 'BBBBBB',
          codigoDian: 'BBBBBB',
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

    it('should delete a TipoObjeto', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTipoObjetoToCollectionIfMissing', () => {
      it('should add a TipoObjeto to an empty array', () => {
        const tipoObjeto: ITipoObjeto = { id: 123 };
        expectedResult = service.addTipoObjetoToCollectionIfMissing([], tipoObjeto);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoObjeto);
      });

      it('should not add a TipoObjeto to an array that contains it', () => {
        const tipoObjeto: ITipoObjeto = { id: 123 };
        const tipoObjetoCollection: ITipoObjeto[] = [
          {
            ...tipoObjeto,
          },
          { id: 456 },
        ];
        expectedResult = service.addTipoObjetoToCollectionIfMissing(tipoObjetoCollection, tipoObjeto);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TipoObjeto to an array that doesn't contain it", () => {
        const tipoObjeto: ITipoObjeto = { id: 123 };
        const tipoObjetoCollection: ITipoObjeto[] = [{ id: 456 }];
        expectedResult = service.addTipoObjetoToCollectionIfMissing(tipoObjetoCollection, tipoObjeto);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoObjeto);
      });

      it('should add only unique TipoObjeto to an array', () => {
        const tipoObjetoArray: ITipoObjeto[] = [{ id: 123 }, { id: 456 }, { id: 21680 }];
        const tipoObjetoCollection: ITipoObjeto[] = [{ id: 123 }];
        expectedResult = service.addTipoObjetoToCollectionIfMissing(tipoObjetoCollection, ...tipoObjetoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tipoObjeto: ITipoObjeto = { id: 123 };
        const tipoObjeto2: ITipoObjeto = { id: 456 };
        expectedResult = service.addTipoObjetoToCollectionIfMissing([], tipoObjeto, tipoObjeto2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoObjeto);
        expect(expectedResult).toContain(tipoObjeto2);
      });

      it('should accept null and undefined values', () => {
        const tipoObjeto: ITipoObjeto = { id: 123 };
        expectedResult = service.addTipoObjetoToCollectionIfMissing([], null, tipoObjeto, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoObjeto);
      });

      it('should return initial array if no TipoObjeto is added', () => {
        const tipoObjetoCollection: ITipoObjeto[] = [{ id: 123 }];
        expectedResult = service.addTipoObjetoToCollectionIfMissing(tipoObjetoCollection, undefined, null);
        expect(expectedResult).toEqual(tipoObjetoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
