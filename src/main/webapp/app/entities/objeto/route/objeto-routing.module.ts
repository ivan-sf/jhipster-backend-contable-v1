import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ObjetoComponent } from '../list/objeto.component';
import { ObjetoDetailComponent } from '../detail/objeto-detail.component';
import { ObjetoUpdateComponent } from '../update/objeto-update.component';
import { ObjetoRoutingResolveService } from './objeto-routing-resolve.service';

const objetoRoute: Routes = [
  {
    path: '',
    component: ObjetoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ObjetoDetailComponent,
    resolve: {
      objeto: ObjetoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ObjetoUpdateComponent,
    resolve: {
      objeto: ObjetoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ObjetoUpdateComponent,
    resolve: {
      objeto: ObjetoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(objetoRoute)],
  exports: [RouterModule],
})
export class ObjetoRoutingModule {}
