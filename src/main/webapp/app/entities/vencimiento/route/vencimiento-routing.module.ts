import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VencimientoComponent } from '../list/vencimiento.component';
import { VencimientoDetailComponent } from '../detail/vencimiento-detail.component';
import { VencimientoUpdateComponent } from '../update/vencimiento-update.component';
import { VencimientoRoutingResolveService } from './vencimiento-routing-resolve.service';

const vencimientoRoute: Routes = [
  {
    path: '',
    component: VencimientoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VencimientoDetailComponent,
    resolve: {
      vencimiento: VencimientoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VencimientoUpdateComponent,
    resolve: {
      vencimiento: VencimientoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VencimientoUpdateComponent,
    resolve: {
      vencimiento: VencimientoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(vencimientoRoute)],
  exports: [RouterModule],
})
export class VencimientoRoutingModule {}
