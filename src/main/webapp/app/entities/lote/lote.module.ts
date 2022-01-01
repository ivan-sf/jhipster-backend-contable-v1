import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LoteComponent } from './list/lote.component';
import { LoteDetailComponent } from './detail/lote-detail.component';
import { LoteUpdateComponent } from './update/lote-update.component';
import { LoteDeleteDialogComponent } from './delete/lote-delete-dialog.component';
import { LoteRoutingModule } from './route/lote-routing.module';

@NgModule({
  imports: [SharedModule, LoteRoutingModule],
  declarations: [LoteComponent, LoteDetailComponent, LoteUpdateComponent, LoteDeleteDialogComponent],
  entryComponents: [LoteDeleteDialogComponent],
})
export class LoteModule {}
