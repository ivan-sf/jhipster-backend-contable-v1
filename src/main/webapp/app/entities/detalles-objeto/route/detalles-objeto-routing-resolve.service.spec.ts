jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDetallesObjeto, DetallesObjeto } from '../detalles-objeto.model';
import { DetallesObjetoService } from '../service/detalles-objeto.service';

import { DetallesObjetoRoutingResolveService } from './detalles-objeto-routing-resolve.service';

describe('DetallesObjeto routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DetallesObjetoRoutingResolveService;
  let service: DetallesObjetoService;
  let resultDetallesObjeto: IDetallesObjeto | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(DetallesObjetoRoutingResolveService);
    service = TestBed.inject(DetallesObjetoService);
    resultDetallesObjeto = undefined;
  });

  describe('resolve', () => {
    it('should return IDetallesObjeto returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDetallesObjeto = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDetallesObjeto).toEqual({ id: 123 });
    });

    it('should return new IDetallesObjeto if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDetallesObjeto = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDetallesObjeto).toEqual(new DetallesObjeto());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DetallesObjeto })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDetallesObjeto = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDetallesObjeto).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
