import { Component } from '@angular/core';
import {AuthenticationService} from "../../service/authentication.service";
import {UserRoles} from "../../guards/user-roles";

@Component({
  selector: 'app-affiliations',
  templateUrl: './affiliations.component.html',
  styleUrls: ['./affiliations.component.scss']
})
export class AffiliationsComponent {

  constructor(public authService: AuthenticationService,) {
  }

  protected readonly UserRoles = UserRoles;
}
