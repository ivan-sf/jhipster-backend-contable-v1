jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRegimen, Regimen } from '../regimen.model';
import { RegimenService } from '../service/regimen.service';

import { RegimenRoutingResolveService } from './regimen-routing-resolve.service';

describe('Regimen routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RegimenRoutingResolveService;
  let service: RegimenService;
  let resultRegimen: IRegimen | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(RegimenRoutingResolveService);
    service = TestBed.inject(RegimenService);
    resultRegimen = undefined;
  });

  describe('resolve', () => {
    it('should return IRegimen returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRegimen = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRegimen).toEqual({ id: 123 });
    });

    it('should return new IRegimen if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRegimen = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRegimen).toEqual(new Regimen());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Regimen })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRegimen = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRegimen).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
