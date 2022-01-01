import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVencimiento, Vencimiento } from '../vencimiento.model';
import { VencimientoService } from '../service/vencimiento.service';

@Injectable({ providedIn: 'root' })
export class VencimientoRoutingResolveService implements Resolve<IVencimiento> {
  constructor(protected service: VencimientoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVencimiento> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((vencimiento: HttpResponse<Vencimiento>) => {
          if (vencimiento.body) {
            return of(vencimiento.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Vencimiento());
  }
}
