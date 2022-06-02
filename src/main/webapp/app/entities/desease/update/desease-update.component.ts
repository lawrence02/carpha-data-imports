import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IDesease, Desease } from '../desease.model';
import { DeseaseService } from '../service/desease.service';

@Component({
  selector: 'jhi-desease-update',
  templateUrl: './desease-update.component.html',
})
export class DeseaseUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    country: [],
    deseaseId: [],
    caseInfo: [],
    year: [],
    week: [],
    weekEnding: [],
  });

  constructor(protected deseaseService: DeseaseService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ desease }) => {
      this.updateForm(desease);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const desease = this.createFromForm();
    if (desease.id !== undefined) {
      this.subscribeToSaveResponse(this.deseaseService.update(desease));
    } else {
      this.subscribeToSaveResponse(this.deseaseService.create(desease));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDesease>>): void {
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

  protected updateForm(desease: IDesease): void {
    this.editForm.patchValue({
      id: desease.id,
      country: desease.country,
      deseaseId: desease.deseaseId,
      caseInfo: desease.caseInfo,
      year: desease.year,
      week: desease.week,
      weekEnding: desease.weekEnding,
    });
  }

  protected createFromForm(): IDesease {
    return {
      ...new Desease(),
      id: this.editForm.get(['id'])!.value,
      country: this.editForm.get(['country'])!.value,
      deseaseId: this.editForm.get(['deseaseId'])!.value,
      caseInfo: this.editForm.get(['caseInfo'])!.value,
      year: this.editForm.get(['year'])!.value,
      week: this.editForm.get(['week'])!.value,
      weekEnding: this.editForm.get(['weekEnding'])!.value,
    };
  }
}
