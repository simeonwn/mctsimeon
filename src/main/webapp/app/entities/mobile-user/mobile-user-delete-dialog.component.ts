import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMobileUser } from 'app/shared/model/mobile-user.model';
import { MobileUserService } from './mobile-user.service';

@Component({
  templateUrl: './mobile-user-delete-dialog.component.html',
})
export class MobileUserDeleteDialogComponent {
  mobileUser?: IMobileUser;

  constructor(
    protected mobileUserService: MobileUserService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mobileUserService.delete(id).subscribe(() => {
      this.eventManager.broadcast('mobileUserListModification');
      this.activeModal.close();
    });
  }
}
