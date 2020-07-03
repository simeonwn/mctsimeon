import { Moment } from 'moment';
import { Gender } from 'app/shared/model/enumerations/gender.model';

export interface IMobileUser {
  id?: number;
  mobileNumber?: string;
  firstName?: string;
  lastName?: string;
  dateOfBirth?: Moment;
  gender?: Gender;
  email?: string;
}

export class MobileUser implements IMobileUser {
  constructor(
    public id?: number,
    public mobileNumber?: string,
    public firstName?: string,
    public lastName?: string,
    public dateOfBirth?: Moment,
    public gender?: Gender,
    public email?: string
  ) {}
}
