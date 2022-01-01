import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PaqueteComponent } from '../list/paquete.component';
import { PaqueteDetailComponent } from '../detail/paquete-detail.component';
import { PaqueteUpdateComponent } from '../update/paquete-update.component';
import { PaqueteRoutingResolveService } from './paquete-routing-resolve.service';

const paqueteRoute: Routes = [
  {
    path: '',
    component: PaqueteComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PaqueteDetailComponent,
    resolve: {
      paquete: PaqueteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PaqueteUpdateComponent,
    resolve: {
      paquete: PaqueteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaqueteUpdateComponent,
    resolve: {
      paquete: PaqueteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(paqueteRoute)],
  exports: [RouterModule],
})
export class PaqueteRoutingModule {}
