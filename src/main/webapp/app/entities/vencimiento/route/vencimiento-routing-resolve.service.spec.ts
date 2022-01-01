jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IVencimiento, Vencimiento } from '../vencimiento.model';
import { VencimientoService } from '../service/vencimiento.service';

import { VencimientoRoutingResolveService } from './vencimiento-routing-resolve.service';

describe('Vencimiento routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: VencimientoRoutingResolveService;
  let service: VencimientoService;
  let resultVencimiento: IVencimiento | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(VencimientoRoutingResolveService);
    service = TestBed.inject(VencimientoService);
    resultVencimiento = undefined;
  });

  describe('resolve', () => {
    it('should return IVencimiento returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVencimiento = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVencimiento).toEqual({ id: 123 });
    });

    it('should return new IVencimiento if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVencimiento = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultVencimiento).toEqual(new Vencimiento());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Vencimiento })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVencimiento = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVencimiento).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
