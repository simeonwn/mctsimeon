import {Component, OnInit, OnDestroy} from '@angular/core';
import {Subscription} from 'rxjs';

import {AccountService} from 'app/core/auth/account.service';
import {Account} from 'app/core/user/account.model';
import {HttpResponse} from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import {FormBuilder, Validators, FormGroup} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs';
import {MobileUserService} from "app/entities/mobile-user/mobile-user.service";
import {IMobileUser, MobileUser} from "app/shared/model/mobile-user.model";
import {MobileLoginModalService} from "app/core/login/mobilelogin-modal.service";

@Component({
  selector: 'mct-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  authSubscription?: Subscription;
  isSaving = false;
  registerSuccess = false;
  dateOfBirthDp: any;

  editForm!: FormGroup;

  constructor(private accountService: AccountService, private mobileLoginModalService: MobileLoginModalService, private fb: FormBuilder, protected mobileUserService: MobileUserService, protected activatedRoute: ActivatedRoute) {
    this.resetForm();
  }

  resetForm(): void {
    this.editForm = this.fb.group({
      id: [],
      mobileNumber: [null, [Validators.required, Validators.pattern('^(\\+62|62|0){1}[8]{1}\\d{8,10}$')]],
      firstName: [null, [Validators.required, Validators.maxLength(50)]],
      lastName: [null, [Validators.required, Validators.maxLength(50)]],
      dateOfBirth: [],
      gender: [],
      email: [
        null,
        [
          Validators.required,
          Validators.minLength(5),
          Validators.maxLength(254),
          Validators.pattern('^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$'),
        ],
      ],
    });
  }

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  save(): void {
    this.isSaving = true;
    const mobileUser = this.createFromForm();
    this.subscribeToSaveResponse(this.mobileUserService.register(mobileUser));

  }

  private createFromForm(): IMobileUser {
    return {
      ...new MobileUser(),
      id: this.editForm.get(['id'])!.value,
      mobileNumber: this.editForm.get(['mobileNumber'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      dateOfBirth: this.editForm.get(['dateOfBirth'])!.value,
      gender: this.editForm.get(['gender'])!.value,
      email: this.editForm.get(['email'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMobileUser>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.registerSuccess = true;
    this.resetForm();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  login(): void {
    this.mobileLoginModalService.open();
  }
}
