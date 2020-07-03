import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMobileUser } from 'app/shared/model/mobile-user.model';

@Component({
  selector: 'mct-mobile-user-detail',
  templateUrl: './mobile-user-detail.component.html',
})
export class MobileUserDetailComponent implements OnInit {
  mobileUser: IMobileUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mobileUser }) => (this.mobileUser = mobileUser));
  }

  previousState(): void {
    window.history.back();
  }
}
