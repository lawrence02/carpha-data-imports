import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrgUnit } from '../org-unit.model';

@Component({
  selector: 'jhi-org-unit-detail',
  templateUrl: './org-unit-detail.component.html',
})
export class OrgUnitDetailComponent implements OnInit {
  orgUnit: IOrgUnit | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orgUnit }) => {
      this.orgUnit = orgUnit;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
