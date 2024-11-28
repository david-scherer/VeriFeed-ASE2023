import {Affiliation} from "./affiliation";
import {Address} from "./address";
import {JournalistVerificationDetails} from "./journalistVerificationDetails";

export class Journalist {
  constructor(
    public id: string,
    public firstName: string,
    public lastName: string,
    public publishesFor?: Affiliation[],
    public verified?: boolean,
    public adminOf?: Affiliation,
    public address?: Address,
    public journalistVerificationDetails?: JournalistVerificationDetails,
    public img?: string,
  ) {
  }
}
