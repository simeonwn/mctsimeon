import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IMobileUser, MobileUser } from 'app/shared/model/mobile-user.model';
import { MobileUserService } from './mobile-user.service';

@Component({
  selector: 'mct-mobile-user-update',
  templateUrl: './mobile-user-update.component.html',
})
export class MobileUserUpdateComponent implements OnInit {
  isSaving = false;
  dateOfBirthDp: any;

  editForm = this.fb.group({
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

  constructor(protected mobileUserService: MobileUserService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mobileUser }) => {
      this.updateForm(mobileUser);
    });
  }

  updateForm(mobileUser: IMobileUser): void {
    this.editForm.patchValue({
      id: mobileUser.id,
      mobileNumber: mobileUser.mobileNumber,
      firstName: mobileUser.firstName,
      lastName: mobileUser.lastName,
      dateOfBirth: mobileUser.dateOfBirth,
      gender: mobileUser.gender,
      email: mobileUser.email,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mobileUser = this.createFromForm();
    if (mobileUser.id !== undefined) {
      this.subscribeToSaveResponse(this.mobileUserService.update(mobileUser));
    } else {
      this.subscribeToSaveResponse(this.mobileUserService.create(mobileUser));
    }
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
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
