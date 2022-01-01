import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITipoFactura, TipoFactura } from '../tipo-factura.model';
import { TipoFacturaService } from '../service/tipo-factura.service';

@Injectable({ providedIn: 'root' })
export class TipoFacturaRoutingResolveService implements Resolve<ITipoFactura> {
  constructor(protected service: TipoFacturaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITipoFactura> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tipoFactura: HttpResponse<TipoFactura>) => {
          if (tipoFactura.body) {
            return of(tipoFactura.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TipoFactura());
  }
}
