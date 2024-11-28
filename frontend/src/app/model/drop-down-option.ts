import {UserRoles} from "../guards/user-roles";

export class DropDownOption {
  constructor(
    public name: string,
    public target?: string,
    public role?: UserRoles[],
    public mat_icon?: string
  ) { }
}
