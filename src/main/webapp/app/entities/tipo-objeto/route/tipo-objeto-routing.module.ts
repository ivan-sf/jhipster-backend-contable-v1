import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TipoObjetoComponent } from '../list/tipo-objeto.component';
import { TipoObjetoDetailComponent } from '../detail/tipo-objeto-detail.component';
import { TipoObjetoUpdateComponent } from '../update/tipo-objeto-update.component';
import { TipoObjetoRoutingResolveService } from './tipo-objeto-routing-resolve.service';

const tipoObjetoRoute: Routes = [
  {
    path: '',
    component: TipoObjetoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TipoObjetoDetailComponent,
    resolve: {
      tipoObjeto: TipoObjetoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TipoObjetoUpdateComponent,
    resolve: {
      tipoObjeto: TipoObjetoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TipoObjetoUpdateComponent,
    resolve: {
      tipoObjeto: TipoObjetoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tipoObjetoRoute)],
  exports: [RouterModule],
})
export class TipoObjetoRoutingModule {}
