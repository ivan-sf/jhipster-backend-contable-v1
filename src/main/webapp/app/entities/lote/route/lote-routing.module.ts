import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LoteComponent } from '../list/lote.component';
import { LoteDetailComponent } from '../detail/lote-detail.component';
import { LoteUpdateComponent } from '../update/lote-update.component';
import { LoteRoutingResolveService } from './lote-routing-resolve.service';

const loteRoute: Routes = [
  {
    path: '',
    component: LoteComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LoteDetailComponent,
    resolve: {
      lote: LoteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LoteUpdateComponent,
    resolve: {
      lote: LoteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LoteUpdateComponent,
    resolve: {
      lote: LoteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(loteRoute)],
  exports: [RouterModule],
})
export class LoteRoutingModule {}
