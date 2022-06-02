import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OrgUnitComponent } from '../list/org-unit.component';
import { OrgUnitDetailComponent } from '../detail/org-unit-detail.component';
import { OrgUnitUpdateComponent } from '../update/org-unit-update.component';
import { OrgUnitRoutingResolveService } from './org-unit-routing-resolve.service';

const orgUnitRoute: Routes = [
  {
    path: '',
    component: OrgUnitComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrgUnitDetailComponent,
    resolve: {
      orgUnit: OrgUnitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrgUnitUpdateComponent,
    resolve: {
      orgUnit: OrgUnitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrgUnitUpdateComponent,
    resolve: {
      orgUnit: OrgUnitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(orgUnitRoute)],
  exports: [RouterModule],
})
export class OrgUnitRoutingModule {}
