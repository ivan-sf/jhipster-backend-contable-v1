import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDepartamento } from '../departamento.model';
import { DepartamentoService } from '../service/departamento.service';

@Component({
  templateUrl: './departamento-delete-dialog.component.html',
})
export class DepartamentoDeleteDialogComponent {
  departamento?: IDepartamento;

  constructor(protected departamentoService: DepartamentoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.departamentoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
