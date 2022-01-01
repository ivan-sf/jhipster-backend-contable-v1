jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IObjeto, Objeto } from '../objeto.model';
import { ObjetoService } from '../service/objeto.service';

import { ObjetoRoutingResolveService } from './objeto-routing-resolve.service';

describe('Objeto routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ObjetoRoutingResolveService;
  let service: ObjetoService;
  let resultObjeto: IObjeto | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ObjetoRoutingResolveService);
    service = TestBed.inject(ObjetoService);
    resultObjeto = undefined;
  });

  describe('resolve', () => {
    it('should return IObjeto returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultObjeto = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultObjeto).toEqual({ id: 123 });
    });

    it('should return new IObjeto if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultObjeto = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultObjeto).toEqual(new Objeto());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Objeto })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultObjeto = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultObjeto).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
