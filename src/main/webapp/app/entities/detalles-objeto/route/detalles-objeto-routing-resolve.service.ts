import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDetallesObjeto, DetallesObjeto } from '../detalles-objeto.model';
import { DetallesObjetoService } from '../service/detalles-objeto.service';

@Injectable({ providedIn: 'root' })
export class DetallesObjetoRoutingResolveService implements Resolve<IDetallesObjeto> {
  constructor(protected service: DetallesObjetoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDetallesObjeto> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((detallesObjeto: HttpResponse<DetallesObjeto>) => {
          if (detallesObjeto.body) {
            return of(detallesObjeto.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DetallesObjeto());
  }
}
