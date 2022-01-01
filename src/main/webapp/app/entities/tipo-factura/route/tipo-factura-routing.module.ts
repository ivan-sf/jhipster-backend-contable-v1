import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TipoFacturaComponent } from '../list/tipo-factura.component';
import { TipoFacturaDetailComponent } from '../detail/tipo-factura-detail.component';
import { TipoFacturaUpdateComponent } from '../update/tipo-factura-update.component';
import { TipoFacturaRoutingResolveService } from './tipo-factura-routing-resolve.service';

const tipoFacturaRoute: Routes = [
  {
    path: '',
    component: TipoFacturaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TipoFacturaDetailComponent,
    resolve: {
      tipoFactura: TipoFacturaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TipoFacturaUpdateComponent,
    resolve: {
      tipoFactura: TipoFacturaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TipoFacturaUpdateComponent,
    resolve: {
      tipoFactura: TipoFacturaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tipoFacturaRoute)],
  exports: [RouterModule],
})
export class TipoFacturaRoutingModule {}
