import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INotaContable, NotaContable } from '../nota-contable.model';
import { NotaContableService } from '../service/nota-contable.service';

@Injectable({ providedIn: 'root' })
export class NotaContableRoutingResolveService implements Resolve<INotaContable> {
  constructor(protected service: NotaContableService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INotaContable> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((notaContable: HttpResponse<NotaContable>) => {
          if (notaContable.body) {
            return of(notaContable.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new NotaContable());
  }
}
