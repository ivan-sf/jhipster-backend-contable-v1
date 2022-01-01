import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DetalleFacturaComponent } from '../list/detalle-factura.component';
import { DetalleFacturaDetailComponent } from '../detail/detalle-factura-detail.component';
import { DetalleFacturaUpdateComponent } from '../update/detalle-factura-update.component';
import { DetalleFacturaRoutingResolveService } from './detalle-factura-routing-resolve.service';

const detalleFacturaRoute: Routes = [
  {
    path: '',
    component: DetalleFacturaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DetalleFacturaDetailComponent,
    resolve: {
      detalleFactura: DetalleFacturaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DetalleFacturaUpdateComponent,
    resolve: {
      detalleFactura: DetalleFacturaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DetalleFacturaUpdateComponent,
    resolve: {
      detalleFactura: DetalleFacturaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(detalleFacturaRoute)],
  exports: [RouterModule],
})
export class DetalleFacturaRoutingModule {}
