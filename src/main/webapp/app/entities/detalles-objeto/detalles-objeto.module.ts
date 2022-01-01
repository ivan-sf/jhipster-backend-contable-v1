import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DetallesObjetoComponent } from './list/detalles-objeto.component';
import { DetallesObjetoDetailComponent } from './detail/detalles-objeto-detail.component';
import { DetallesObjetoUpdateComponent } from './update/detalles-objeto-update.component';
import { DetallesObjetoDeleteDialogComponent } from './delete/detalles-objeto-delete-dialog.component';
import { DetallesObjetoRoutingModule } from './route/detalles-objeto-routing.module';

@NgModule({
  imports: [SharedModule, DetallesObjetoRoutingModule],
  declarations: [
    DetallesObjetoComponent,
    DetallesObjetoDetailComponent,
    DetallesObjetoUpdateComponent,
    DetallesObjetoDeleteDialogComponent,
  ],
  entryComponents: [DetallesObjetoDeleteDialogComponent],
})
export class DetallesObjetoModule {}
