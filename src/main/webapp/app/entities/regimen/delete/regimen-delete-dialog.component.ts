import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRegimen } from '../regimen.model';
import { RegimenService } from '../service/regimen.service';

@Component({
  templateUrl: './regimen-delete-dialog.component.html',
})
export class RegimenDeleteDialogComponent {
  regimen?: IRegimen;

  constructor(protected regimenService: RegimenService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.regimenService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
