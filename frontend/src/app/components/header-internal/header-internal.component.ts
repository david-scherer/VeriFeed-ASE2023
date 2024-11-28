import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from "../../service/authentication.service";
import {Router} from "@angular/router";
import {UserService} from "../../service/user.service";
import {User} from "../../model/user";
import {DropDownOption} from "../../model/drop-down-option";
import {NotificationBannerService} from "../../service/notification-banner.service";
import {UserRoles} from "../../guards/user-roles";

@Component({
  selector: 'app-header-internal',
  templateUrl: './header-internal.component.html',
  styleUrls: ['./header-internal.component.scss'],
})
export class HeaderInternalComponent implements OnInit {

  public user?: User
  submenuOpen = false
  private userDropDownOptions = [
    new DropDownOption("Start", "/internal", [UserRoles.USER, UserRoles.JOURNALIST, UserRoles.ADMIN]),
    new DropDownOption("Affiliations", "/affiliations", [UserRoles.USER, UserRoles.JOURNALIST, UserRoles.ADMIN]),
    new DropDownOption("Journalists", "/journalists", [UserRoles.USER, UserRoles.JOURNALIST, UserRoles.ADMIN]),
    new DropDownOption("Certificates", "/certificates", [UserRoles.ADMIN]),
    new DropDownOption("Account", "/account", [UserRoles.USER, UserRoles.JOURNALIST, UserRoles.ADMIN]),
  ]

  public userDropDownPermission = this.userDropDownOptions.filter(option => option.role?.includes(this.authService.getUserRole()));

  constructor(
    public authService: AuthenticationService,
    public router: Router,
    public userService: UserService,
    private notificationBannerService: NotificationBannerService
  ) {
  }

  ngOnInit(): void {
    this.userService.getUserById(this.authService.getLoggedInUserId())
      .subscribe({
        next: (user: User) => {
          this.user = user
          console.log(user)
        },
        error: (error: Error) => {
          this.notificationBannerService.errorHandling(error);
        },
      });
  }

  public handleOptionSelection(option: DropDownOption): void {
    this.submenuOpen = false;
    if (option.target) {
      this.router.navigateByUrl(option.target);
    }
  }

  public logout(): void {
    this.authService.logout()
    this.router.navigateByUrl('/login')
  }

  protected readonly DropDownOption = DropDownOption;

  getProfileImg() {
    if (this.user?.img) {
      return this.user.img;
    }
    return 'assets/user.png';
  }
}
