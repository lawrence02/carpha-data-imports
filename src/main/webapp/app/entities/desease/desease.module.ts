import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DeseaseComponent } from './list/desease.component';
import { DeseaseDetailComponent } from './detail/desease-detail.component';
import { DeseaseUpdateComponent } from './update/desease-update.component';
import { DeseaseDeleteDialogComponent } from './delete/desease-delete-dialog.component';
import { DeseaseRoutingModule } from './route/desease-routing.module';

@NgModule({
  imports: [SharedModule, DeseaseRoutingModule],
  declarations: [DeseaseComponent, DeseaseDetailComponent, DeseaseUpdateComponent, DeseaseDeleteDialogComponent],
  entryComponents: [DeseaseDeleteDialogComponent],
})
export class DeseaseModule {}
