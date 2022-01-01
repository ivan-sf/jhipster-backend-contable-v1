jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITipoFactura, TipoFactura } from '../tipo-factura.model';
import { TipoFacturaService } from '../service/tipo-factura.service';

import { TipoFacturaRoutingResolveService } from './tipo-factura-routing-resolve.service';

describe('TipoFactura routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TipoFacturaRoutingResolveService;
  let service: TipoFacturaService;
  let resultTipoFactura: ITipoFactura | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(TipoFacturaRoutingResolveService);
    service = TestBed.inject(TipoFacturaService);
    resultTipoFactura = undefined;
  });

  describe('resolve', () => {
    it('should return ITipoFactura returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTipoFactura = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTipoFactura).toEqual({ id: 123 });
    });

    it('should return new ITipoFactura if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTipoFactura = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTipoFactura).toEqual(new TipoFactura());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TipoFactura })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTipoFactura = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTipoFactura).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
