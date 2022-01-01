import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VencimientoComponent } from './list/vencimiento.component';
import { VencimientoDetailComponent } from './detail/vencimiento-detail.component';
import { VencimientoUpdateComponent } from './update/vencimiento-update.component';
import { VencimientoDeleteDialogComponent } from './delete/vencimiento-delete-dialog.component';
import { VencimientoRoutingModule } from './route/vencimiento-routing.module';

@NgModule({
  imports: [SharedModule, VencimientoRoutingModule],
  declarations: [VencimientoComponent, VencimientoDetailComponent, VencimientoUpdateComponent, VencimientoDeleteDialogComponent],
  entryComponents: [VencimientoDeleteDialogComponent],
})
export class VencimientoModule {}
