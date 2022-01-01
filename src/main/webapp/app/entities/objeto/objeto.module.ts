import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ObjetoComponent } from './list/objeto.component';
import { ObjetoDetailComponent } from './detail/objeto-detail.component';
import { ObjetoUpdateComponent } from './update/objeto-update.component';
import { ObjetoDeleteDialogComponent } from './delete/objeto-delete-dialog.component';
import { ObjetoRoutingModule } from './route/objeto-routing.module';

@NgModule({
  imports: [SharedModule, ObjetoRoutingModule],
  declarations: [ObjetoComponent, ObjetoDetailComponent, ObjetoUpdateComponent, ObjetoDeleteDialogComponent],
  entryComponents: [ObjetoDeleteDialogComponent],
})
export class ObjetoModule {}
