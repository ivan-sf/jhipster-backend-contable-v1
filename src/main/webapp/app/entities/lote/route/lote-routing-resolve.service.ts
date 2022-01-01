import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILote, Lote } from '../lote.model';
import { LoteService } from '../service/lote.service';

@Injectable({ providedIn: 'root' })
export class LoteRoutingResolveService implements Resolve<ILote> {
  constructor(protected service: LoteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILote> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((lote: HttpResponse<Lote>) => {
          if (lote.body) {
            return of(lote.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Lote());
  }
}
