import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IMobileUser } from 'app/shared/model/mobile-user.model';

type EntityResponseType = HttpResponse<IMobileUser>;
type EntityArrayResponseType = HttpResponse<IMobileUser[]>;

@Injectable({ providedIn: 'root' })
export class MobileUserService {
  public resourceUrl = SERVER_API_URL + 'api/mobile-users';
  public registerUrl = SERVER_API_URL + 'api/mobileregister';

  constructor(protected http: HttpClient) {}

  create(mobileUser: IMobileUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mobileUser);
    return this.http
      .post<IMobileUser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(mobileUser: IMobileUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mobileUser);
    return this.http
      .put<IMobileUser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  register(mobileUser: IMobileUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mobileUser);
    return this.http
      .post<IMobileUser>(this.registerUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMobileUser>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMobileUser[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(mobileUser: IMobileUser): IMobileUser {
    const copy: IMobileUser = Object.assign({}, mobileUser, {
      dateOfBirth: mobileUser.dateOfBirth && mobileUser.dateOfBirth.isValid() ? mobileUser.dateOfBirth.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateOfBirth = res.body.dateOfBirth ? moment(res.body.dateOfBirth) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((mobileUser: IMobileUser) => {
        mobileUser.dateOfBirth = mobileUser.dateOfBirth ? moment(mobileUser.dateOfBirth) : undefined;
      });
    }
    return res;
  }
}
