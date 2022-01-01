import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRegimen, Regimen } from '../regimen.model';
import { RegimenService } from '../service/regimen.service';

@Injectable({ providedIn: 'root' })
export class RegimenRoutingResolveService implements Resolve<IRegimen> {
  constructor(protected service: RegimenService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRegimen> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((regimen: HttpResponse<Regimen>) => {
          if (regimen.body) {
            return of(regimen.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Regimen());
  }
}
