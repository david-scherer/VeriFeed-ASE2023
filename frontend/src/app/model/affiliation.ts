import {Journalist} from "./journalist";

export class Affiliation {
  constructor(
    public name: string,
    public id?: string,
    public address?: string,
    public verified?: boolean,
    public journalists?: Journalist[],
    public owner?: Journalist
  ) {}
}
