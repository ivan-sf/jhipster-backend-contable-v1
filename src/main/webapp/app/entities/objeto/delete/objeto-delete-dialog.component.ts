import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IObjeto } from '../objeto.model';
import { ObjetoService } from '../service/objeto.service';

@Component({
  templateUrl: './objeto-delete-dialog.component.html',
})
export class ObjetoDeleteDialogComponent {
  objeto?: IObjeto;

  constructor(protected objetoService: ObjetoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.objetoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
