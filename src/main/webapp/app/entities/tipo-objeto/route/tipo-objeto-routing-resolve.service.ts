import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITipoObjeto, TipoObjeto } from '../tipo-objeto.model';
import { TipoObjetoService } from '../service/tipo-objeto.service';

@Injectable({ providedIn: 'root' })
export class TipoObjetoRoutingResolveService implements Resolve<ITipoObjeto> {
  constructor(protected service: TipoObjetoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITipoObjeto> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tipoObjeto: HttpResponse<TipoObjeto>) => {
          if (tipoObjeto.body) {
            return of(tipoObjeto.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TipoObjeto());
  }
}
