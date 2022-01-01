import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NotaContableComponent } from '../list/nota-contable.component';
import { NotaContableDetailComponent } from '../detail/nota-contable-detail.component';
import { NotaContableUpdateComponent } from '../update/nota-contable-update.component';
import { NotaContableRoutingResolveService } from './nota-contable-routing-resolve.service';

const notaContableRoute: Routes = [
  {
    path: '',
    component: NotaContableComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NotaContableDetailComponent,
    resolve: {
      notaContable: NotaContableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NotaContableUpdateComponent,
    resolve: {
      notaContable: NotaContableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NotaContableUpdateComponent,
    resolve: {
      notaContable: NotaContableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(notaContableRoute)],
  exports: [RouterModule],
})
export class NotaContableRoutingModule {}
