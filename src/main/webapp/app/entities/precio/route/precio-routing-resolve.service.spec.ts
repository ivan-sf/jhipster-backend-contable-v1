jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPrecio, Precio } from '../precio.model';
import { PrecioService } from '../service/precio.service';

import { PrecioRoutingResolveService } from './precio-routing-resolve.service';

describe('Precio routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PrecioRoutingResolveService;
  let service: PrecioService;
  let resultPrecio: IPrecio | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(PrecioRoutingResolveService);
    service = TestBed.inject(PrecioService);
    resultPrecio = undefined;
  });

  describe('resolve', () => {
    it('should return IPrecio returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPrecio = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPrecio).toEqual({ id: 123 });
    });

    it('should return new IPrecio if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPrecio = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPrecio).toEqual(new Precio());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Precio })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPrecio = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPrecio).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
