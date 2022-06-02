import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDesease } from '../desease.model';

@Component({
  selector: 'jhi-desease-detail',
  templateUrl: './desease-detail.component.html',
})
export class DeseaseDetailComponent implements OnInit {
  desease: IDesease | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ desease }) => {
      this.desease = desease;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
