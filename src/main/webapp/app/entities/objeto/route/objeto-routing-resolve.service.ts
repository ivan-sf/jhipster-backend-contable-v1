import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IObjeto, Objeto } from '../objeto.model';
import { ObjetoService } from '../service/objeto.service';

@Injectable({ providedIn: 'root' })
export class ObjetoRoutingResolveService implements Resolve<IObjeto> {
  constructor(protected service: ObjetoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IObjeto> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((objeto: HttpResponse<Objeto>) => {
          if (objeto.body) {
            return of(objeto.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Objeto());
  }
}
