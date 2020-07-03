import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IMobileUser, MobileUser } from 'app/shared/model/mobile-user.model';
import { MobileUserService } from './mobile-user.service';
import { MobileUserComponent } from './mobile-user.component';
import { MobileUserDetailComponent } from './mobile-user-detail.component';
import { MobileUserUpdateComponent } from './mobile-user-update.component';

@Injectable({ providedIn: 'root' })
export class MobileUserResolve implements Resolve<IMobileUser> {
  constructor(private service: MobileUserService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMobileUser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((mobileUser: HttpResponse<MobileUser>) => {
          if (mobileUser.body) {
            return of(mobileUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MobileUser());
  }
}

export const mobileUserRoute: Routes = [
  {
    path: '',
    component: MobileUserComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'mctApp.mobileUser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MobileUserDetailComponent,
    resolve: {
      mobileUser: MobileUserResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'mctApp.mobileUser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MobileUserUpdateComponent,
    resolve: {
      mobileUser: MobileUserResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'mctApp.mobileUser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MobileUserUpdateComponent,
    resolve: {
      mobileUser: MobileUserResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'mctApp.mobileUser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
