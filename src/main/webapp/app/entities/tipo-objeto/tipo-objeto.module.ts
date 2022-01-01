import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TipoObjetoComponent } from './list/tipo-objeto.component';
import { TipoObjetoDetailComponent } from './detail/tipo-objeto-detail.component';
import { TipoObjetoUpdateComponent } from './update/tipo-objeto-update.component';
import { TipoObjetoDeleteDialogComponent } from './delete/tipo-objeto-delete-dialog.component';
import { TipoObjetoRoutingModule } from './route/tipo-objeto-routing.module';

@NgModule({
  imports: [SharedModule, TipoObjetoRoutingModule],
  declarations: [TipoObjetoComponent, TipoObjetoDetailComponent, TipoObjetoUpdateComponent, TipoObjetoDeleteDialogComponent],
  entryComponents: [TipoObjetoDeleteDialogComponent],
})
export class TipoObjetoModule {}
