import { Component } from '@angular/core';
import {UserRoles} from "../../guards/user-roles";
import {AuthenticationService} from "../../service/authentication.service";

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss'],
})
export class FooterComponent {
  constructor(public authService: AuthenticationService) {
  }

  protected readonly UserRoles = UserRoles;
}
