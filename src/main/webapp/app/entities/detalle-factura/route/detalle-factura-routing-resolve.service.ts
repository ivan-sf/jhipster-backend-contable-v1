import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDetalleFactura, DetalleFactura } from '../detalle-factura.model';
import { DetalleFacturaService } from '../service/detalle-factura.service';

@Injectable({ providedIn: 'root' })
export class DetalleFacturaRoutingResolveService implements Resolve<IDetalleFactura> {
  constructor(protected service: DetalleFacturaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDetalleFactura> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((detalleFactura: HttpResponse<DetalleFactura>) => {
          if (detalleFactura.body) {
            return of(detalleFactura.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DetalleFactura());
  }
}
