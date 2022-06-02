import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OrgUnitComponent } from './list/org-unit.component';
import { OrgUnitDetailComponent } from './detail/org-unit-detail.component';
import { OrgUnitUpdateComponent } from './update/org-unit-update.component';
import { OrgUnitDeleteDialogComponent } from './delete/org-unit-delete-dialog.component';
import { OrgUnitRoutingModule } from './route/org-unit-routing.module';

@NgModule({
  imports: [SharedModule, OrgUnitRoutingModule],
  declarations: [OrgUnitComponent, OrgUnitDetailComponent, OrgUnitUpdateComponent, OrgUnitDeleteDialogComponent],
  entryComponents: [OrgUnitDeleteDialogComponent],
})
export class OrgUnitModule {}
