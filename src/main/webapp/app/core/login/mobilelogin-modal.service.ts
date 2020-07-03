import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

import {MobileLoginModalComponent} from 'app/shared/mobilelogin/mobilelogin.component';

@Injectable({ providedIn: 'root' })
export class MobileLoginModalService {
  private isOpen = false;

  constructor(private modalService: NgbModal) {}

  open(): void {
    if (this.isOpen) {
      return;
    }
    this.isOpen = true;
    const modalRef: NgbModalRef = this.modalService.open(MobileLoginModalComponent);
    modalRef.result.finally(() => (this.isOpen = false));
  }
}
