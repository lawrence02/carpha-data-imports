import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'org-unit',
        data: { pageTitle: 'caphaApp.orgUnit.home.title' },
        loadChildren: () => import('./org-unit/org-unit.module').then(m => m.OrgUnitModule),
      },
      {
        path: 'desease',
        data: { pageTitle: 'caphaApp.desease.home.title' },
        loadChildren: () => import('./desease/desease.module').then(m => m.DeseaseModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
