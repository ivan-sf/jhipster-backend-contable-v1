import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPaquete, Paquete } from '../paquete.model';
import { PaqueteService } from '../service/paquete.service';

@Injectable({ providedIn: 'root' })
export class PaqueteRoutingResolveService implements Resolve<IPaquete> {
  constructor(protected service: PaqueteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPaquete> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((paquete: HttpResponse<Paquete>) => {
          if (paquete.body) {
            return of(paquete.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Paquete());
  }
}
