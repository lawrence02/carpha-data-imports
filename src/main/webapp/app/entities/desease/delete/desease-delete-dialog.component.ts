import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDesease } from '../desease.model';
import { DeseaseService } from '../service/desease.service';

@Component({
  templateUrl: './desease-delete-dialog.component.html',
})
export class DeseaseDeleteDialogComponent {
  desease?: IDesease;

  constructor(protected deseaseService: DeseaseService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.deseaseService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
