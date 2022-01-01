jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICodigo, Codigo } from '../codigo.model';
import { CodigoService } from '../service/codigo.service';

import { CodigoRoutingResolveService } from './codigo-routing-resolve.service';

describe('Codigo routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CodigoRoutingResolveService;
  let service: CodigoService;
  let resultCodigo: ICodigo | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CodigoRoutingResolveService);
    service = TestBed.inject(CodigoService);
    resultCodigo = undefined;
  });

  describe('resolve', () => {
    it('should return ICodigo returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCodigo = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCodigo).toEqual({ id: 123 });
    });

    it('should return new ICodigo if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCodigo = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCodigo).toEqual(new Codigo());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Codigo })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCodigo = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCodigo).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
