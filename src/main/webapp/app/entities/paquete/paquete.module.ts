import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PaqueteComponent } from './list/paquete.component';
import { PaqueteDetailComponent } from './detail/paquete-detail.component';
import { PaqueteUpdateComponent } from './update/paquete-update.component';
import { PaqueteDeleteDialogComponent } from './delete/paquete-delete-dialog.component';
import { PaqueteRoutingModule } from './route/paquete-routing.module';

@NgModule({
  imports: [SharedModule, PaqueteRoutingModule],
  declarations: [PaqueteComponent, PaqueteDetailComponent, PaqueteUpdateComponent, PaqueteDeleteDialogComponent],
  entryComponents: [PaqueteDeleteDialogComponent],
})
export class PaqueteModule {}
