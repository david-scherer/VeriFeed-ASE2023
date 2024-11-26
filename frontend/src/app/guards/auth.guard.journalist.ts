import {Injectable} from '@angular/core';
import {CanActivate} from '@angular/router';
import {Location} from '@angular/common';
import {AuthenticationService} from "../service/authentication.service";
import {NotificationBannerService} from "../service/notification-banner.service";
import {UserRoles} from "./user-roles";

@Injectable({
  providedIn: 'root'
})
export class AuthGuardJournalist implements CanActivate {

  constructor(private authService: AuthenticationService,
              private location: Location,
              private alert: NotificationBannerService) {
  }

  canActivate(): boolean {
    if (this.authService.getUserRole() === UserRoles.JOURNALIST) {
      return true;
    } else {
      this.location.back();
      this.alert.showError('Unauthorized', 'The link you followed is not supposed to be for you.');
      return false;
    }
  }
}
