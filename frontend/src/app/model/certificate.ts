import {Journalist} from "./journalist";

export class Certificate {
  constructor(
    public id: string,
    public reference?: string,
    public explanation?: string,
    public certificateHolder?: Journalist,
    public verifiedJournalists?: Journalist[]
  ) {}
}
