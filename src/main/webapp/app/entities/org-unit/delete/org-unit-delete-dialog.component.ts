import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrgUnit } from '../org-unit.model';
import { OrgUnitService } from '../service/org-unit.service';

@Component({
  templateUrl: './org-unit-delete-dialog.component.html',
})
export class OrgUnitDeleteDialogComponent {
  orgUnit?: IOrgUnit;

  constructor(protected orgUnitService: OrgUnitService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.orgUnitService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
