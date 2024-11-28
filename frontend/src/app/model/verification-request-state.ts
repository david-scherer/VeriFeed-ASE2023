import {Address} from "./address";
import {Certificate} from "./certificate";
import {User} from "./user";

export class VerificationRequestState {
  constructor(
    public id: string,
    public certificate: Certificate,
    public requester: User,
    public firstName: string,
    public lastName: string,
    public dateOfBirth: Date,
    public distributionReach: number,
    public mainMedium: string,
    public reference: string,
    public requestMessage: string,
    public address?: Address,
    public employer?: string,
    public status?: boolean
  ) {}
}
