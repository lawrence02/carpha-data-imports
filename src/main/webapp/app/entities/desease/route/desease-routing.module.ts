import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DeseaseComponent } from '../list/desease.component';
import { DeseaseDetailComponent } from '../detail/desease-detail.component';
import { DeseaseUpdateComponent } from '../update/desease-update.component';
import { DeseaseRoutingResolveService } from './desease-routing-resolve.service';

const deseaseRoute: Routes = [
  {
    path: '',
    component: DeseaseComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DeseaseDetailComponent,
    resolve: {
      desease: DeseaseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DeseaseUpdateComponent,
    resolve: {
      desease: DeseaseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DeseaseUpdateComponent,
    resolve: {
      desease: DeseaseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(deseaseRoute)],
  exports: [RouterModule],
})
export class DeseaseRoutingModule {}
