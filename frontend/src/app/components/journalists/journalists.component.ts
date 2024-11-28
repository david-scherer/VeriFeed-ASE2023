import { Component } from '@angular/core';
import {UserRoles} from "../../guards/user-roles";
import {AuthenticationService} from "../../service/authentication.service";

@Component({
  selector: 'app-journalists',
  templateUrl: './journalists.component.html',
  styleUrls: ['./journalists.component.scss']
})
export class JournalistsComponent {

  constructor(public authService: AuthenticationService,) {
  }

  protected readonly UserRoles = UserRoles;
}
