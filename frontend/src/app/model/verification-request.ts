import {Address} from "./address";

export class VerificationRequest {
  constructor(
    public certificateId: string,
    public firstName: string,
    public lastName: string,
    public dateOfBirth: Date,
    public distributionReach: number,
    public mainMedium: string,
    public reference: string,
    public requestMessage: string,
    public address?: Address,
    public employer?: string
  ) {}
}
