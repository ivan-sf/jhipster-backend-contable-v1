jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITipoObjeto, TipoObjeto } from '../tipo-objeto.model';
import { TipoObjetoService } from '../service/tipo-objeto.service';

import { TipoObjetoRoutingResolveService } from './tipo-objeto-routing-resolve.service';

describe('TipoObjeto routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TipoObjetoRoutingResolveService;
  let service: TipoObjetoService;
  let resultTipoObjeto: ITipoObjeto | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(TipoObjetoRoutingResolveService);
    service = TestBed.inject(TipoObjetoService);
    resultTipoObjeto = undefined;
  });

  describe('resolve', () => {
    it('should return ITipoObjeto returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTipoObjeto = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTipoObjeto).toEqual({ id: 123 });
    });

    it('should return new ITipoObjeto if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTipoObjeto = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTipoObjeto).toEqual(new TipoObjeto());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TipoObjeto })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTipoObjeto = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTipoObjeto).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
