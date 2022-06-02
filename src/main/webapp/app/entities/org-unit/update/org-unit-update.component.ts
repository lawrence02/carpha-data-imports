import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IOrgUnit, OrgUnit } from '../org-unit.model';
import { OrgUnitService } from '../service/org-unit.service';

@Component({
  selector: 'jhi-org-unit-update',
  templateUrl: './org-unit-update.component.html',
})
export class OrgUnitUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    orgUnitName: [],
    dhisOrgUnitName: [],
    dhisOrgUnitCode: [],
  });

  constructor(protected orgUnitService: OrgUnitService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orgUnit }) => {
      this.updateForm(orgUnit);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const orgUnit = this.createFromForm();
    if (orgUnit.id !== undefined) {
      this.subscribeToSaveResponse(this.orgUnitService.update(orgUnit));
    } else {
      this.subscribeToSaveResponse(this.orgUnitService.create(orgUnit));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrgUnit>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(orgUnit: IOrgUnit): void {
    this.editForm.patchValue({
      id: orgUnit.id,
      orgUnitName: orgUnit.orgUnitName,
      dhisOrgUnitName: orgUnit.dhisOrgUnitName,
      dhisOrgUnitCode: orgUnit.dhisOrgUnitCode,
    });
  }

  protected createFromForm(): IOrgUnit {
    return {
      ...new OrgUnit(),
      id: this.editForm.get(['id'])!.value,
      orgUnitName: this.editForm.get(['orgUnitName'])!.value,
      dhisOrgUnitName: this.editForm.get(['dhisOrgUnitName'])!.value,
      dhisOrgUnitCode: this.editForm.get(['dhisOrgUnitCode'])!.value,
    };
  }
}
