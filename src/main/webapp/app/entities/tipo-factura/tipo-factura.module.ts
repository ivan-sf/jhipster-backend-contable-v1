import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TipoFacturaComponent } from './list/tipo-factura.component';
import { TipoFacturaDetailComponent } from './detail/tipo-factura-detail.component';
import { TipoFacturaUpdateComponent } from './update/tipo-factura-update.component';
import { TipoFacturaDeleteDialogComponent } from './delete/tipo-factura-delete-dialog.component';
import { TipoFacturaRoutingModule } from './route/tipo-factura-routing.module';

@NgModule({
  imports: [SharedModule, TipoFacturaRoutingModule],
  declarations: [TipoFacturaComponent, TipoFacturaDetailComponent, TipoFacturaUpdateComponent, TipoFacturaDeleteDialogComponent],
  entryComponents: [TipoFacturaDeleteDialogComponent],
})
export class TipoFacturaModule {}
