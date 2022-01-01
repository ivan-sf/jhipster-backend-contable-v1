import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RegimenComponent } from '../list/regimen.component';
import { RegimenDetailComponent } from '../detail/regimen-detail.component';
import { RegimenUpdateComponent } from '../update/regimen-update.component';
import { RegimenRoutingResolveService } from './regimen-routing-resolve.service';

const regimenRoute: Routes = [
  {
    path: '',
    component: RegimenComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RegimenDetailComponent,
    resolve: {
      regimen: RegimenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RegimenUpdateComponent,
    resolve: {
      regimen: RegimenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RegimenUpdateComponent,
    resolve: {
      regimen: RegimenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(regimenRoute)],
  exports: [RouterModule],
})
export class RegimenRoutingModule {}
