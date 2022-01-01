import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RegimenComponent } from './list/regimen.component';
import { RegimenDetailComponent } from './detail/regimen-detail.component';
import { RegimenUpdateComponent } from './update/regimen-update.component';
import { RegimenDeleteDialogComponent } from './delete/regimen-delete-dialog.component';
import { RegimenRoutingModule } from './route/regimen-routing.module';

@NgModule({
  imports: [SharedModule, RegimenRoutingModule],
  declarations: [RegimenComponent, RegimenDetailComponent, RegimenUpdateComponent, RegimenDeleteDialogComponent],
  entryComponents: [RegimenDeleteDialogComponent],
})
export class RegimenModule {}
