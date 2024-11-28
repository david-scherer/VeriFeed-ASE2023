import { Component } from '@angular/core';
import {AuthenticationService} from "../../service/authentication.service";
import {UserRoles} from "../../guards/user-roles";

@Component({
  selector: 'app-certificates',
  templateUrl: './certificates.component.html',
  styleUrls: ['./certificates.component.scss']
})
export class CertificatesComponent {
  constructor(public authService: AuthenticationService) {
  }

  protected readonly UserRoles = UserRoles;
}
