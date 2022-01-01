import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DetalleFacturaComponent } from './list/detalle-factura.component';
import { DetalleFacturaDetailComponent } from './detail/detalle-factura-detail.component';
import { DetalleFacturaUpdateComponent } from './update/detalle-factura-update.component';
import { DetalleFacturaDeleteDialogComponent } from './delete/detalle-factura-delete-dialog.component';
import { DetalleFacturaRoutingModule } from './route/detalle-factura-routing.module';

@NgModule({
  imports: [SharedModule, DetalleFacturaRoutingModule],
  declarations: [
    DetalleFacturaComponent,
    DetalleFacturaDetailComponent,
    DetalleFacturaUpdateComponent,
    DetalleFacturaDeleteDialogComponent,
  ],
  entryComponents: [DetalleFacturaDeleteDialogComponent],
})
export class DetalleFacturaModule {}
