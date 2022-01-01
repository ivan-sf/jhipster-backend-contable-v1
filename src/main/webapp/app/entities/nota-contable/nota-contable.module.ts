import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { NotaContableComponent } from './list/nota-contable.component';
import { NotaContableDetailComponent } from './detail/nota-contable-detail.component';
import { NotaContableUpdateComponent } from './update/nota-contable-update.component';
import { NotaContableDeleteDialogComponent } from './delete/nota-contable-delete-dialog.component';
import { NotaContableRoutingModule } from './route/nota-contable-routing.module';

@NgModule({
  imports: [SharedModule, NotaContableRoutingModule],
  declarations: [NotaContableComponent, NotaContableDetailComponent, NotaContableUpdateComponent, NotaContableDeleteDialogComponent],
  entryComponents: [NotaContableDeleteDialogComponent],
})
export class NotaContableModule {}
