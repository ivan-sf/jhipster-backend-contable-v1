import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DetallesObjetoComponent } from '../list/detalles-objeto.component';
import { DetallesObjetoDetailComponent } from '../detail/detalles-objeto-detail.component';
import { DetallesObjetoUpdateComponent } from '../update/detalles-objeto-update.component';
import { DetallesObjetoRoutingResolveService } from './detalles-objeto-routing-resolve.service';

const detallesObjetoRoute: Routes = [
  {
    path: '',
    component: DetallesObjetoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DetallesObjetoDetailComponent,
    resolve: {
      detallesObjeto: DetallesObjetoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DetallesObjetoUpdateComponent,
    resolve: {
      detallesObjeto: DetallesObjetoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DetallesObjetoUpdateComponent,
    resolve: {
      detallesObjeto: DetallesObjetoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(detallesObjetoRoute)],
  exports: [RouterModule],
})
export class DetallesObjetoRoutingModule {}
