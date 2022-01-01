jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { INotaContable, NotaContable } from '../nota-contable.model';
import { NotaContableService } from '../service/nota-contable.service';

import { NotaContableRoutingResolveService } from './nota-contable-routing-resolve.service';

describe('NotaContable routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: NotaContableRoutingResolveService;
  let service: NotaContableService;
  let resultNotaContable: INotaContable | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(NotaContableRoutingResolveService);
    service = TestBed.inject(NotaContableService);
    resultNotaContable = undefined;
  });

  describe('resolve', () => {
    it('should return INotaContable returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultNotaContable = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultNotaContable).toEqual({ id: 123 });
    });

    it('should return new INotaContable if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultNotaContable = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultNotaContable).toEqual(new NotaContable());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as NotaContable })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultNotaContable = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultNotaContable).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
