import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MctSharedModule } from 'app/shared/shared.module';
import { MobileUserComponent } from './mobile-user.component';
import { MobileUserDetailComponent } from './mobile-user-detail.component';
import { MobileUserUpdateComponent } from './mobile-user-update.component';
import { MobileUserDeleteDialogComponent } from './mobile-user-delete-dialog.component';
import { mobileUserRoute } from './mobile-user.route';

@NgModule({
  imports: [MctSharedModule, RouterModule.forChild(mobileUserRoute)],
  declarations: [MobileUserComponent, MobileUserDetailComponent, MobileUserUpdateComponent, MobileUserDeleteDialogComponent],
  entryComponents: [MobileUserDeleteDialogComponent],
})
export class MctMobileUserModule {}
