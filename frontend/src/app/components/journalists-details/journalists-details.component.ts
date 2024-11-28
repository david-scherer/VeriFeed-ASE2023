import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, ParamMap} from "@angular/router";
import {JournalistsService} from "../../service/journalists.service";
import {Journalist} from "../../model/journalist";
import {AffiliationsService} from "../../service/affiliations.service";
import {NotificationBannerService} from "../../service/notification-banner.service";
import {AuthenticationService} from "../../service/authentication.service";
import {UserRoles} from "../../guards/user-roles";
import {UserService} from "../../service/user.service";

@Component({
  selector: 'app-journalists-details',
  templateUrl: './journalists-details.component.html',
  styleUrls: ['./journalists-details.component.scss']
})
export class JournalistsDetailsComponent {
  isAdminOfAffiliation = false;
  belongsJournalistAffiliation = false;
  journalist: Journalist | undefined;
  id: string | null = '';

  constructor(private journalistsService: JournalistsService,
              private affiliationService: AffiliationsService,
              private route: ActivatedRoute,
              private notificationBannerService: NotificationBannerService,
              private userService: UserService,
              private journalistService: JournalistsService,
              protected authenticationService: AuthenticationService) {
    this.route.paramMap.subscribe((params: ParamMap) => {
      this.id = this.route.snapshot.paramMap.get('id');
    });
    this.getJournalist();
    this.isUserAdminOfAffiliation();
    this.belongsJournalistToAffiliation();
  }

  isUserAdminOfAffiliation(): void {
    this.userService.getUserById(this.authenticationService.getLoggedInUserId())
      .subscribe({
          next: (user: any) => {
            this.isAdminOfAffiliation = user.adminOf;
          }
        }
      );
  }

  belongsJournalistToAffiliation(): void {
    if (this.id != null) {
      this.affiliationService.belongsJournalistToAffiliation(this.id)
        .subscribe({
          next: (belongsTo: boolean) => {
            this.belongsJournalistAffiliation = belongsTo;
          }
        });
    }
  }

  private getJournalist() {
    if (this.id == null) return;
    this.journalistsService.getJournalistById(this.id).subscribe({
      next: (journalist: Journalist) => {
        console.log(journalist);
        this.journalist = journalist;
      },
      error: (error: Error) => {
        this.notificationBannerService.errorHandling(error);
      }
    });
  }

  addToAffiliation() {
    if (this.id == null) return;
    this.affiliationService.addJournalistToAffiliation(this.id).subscribe({
      next: () => {
        this.notificationBannerService.showSuccess("Success!", "Journalist " + this.journalist?.lastName + " " + this.journalist?.firstName + " added to your affiliation.");
      },
      error: (error: Error) => {
        this.notificationBannerService.errorHandling(error);
      },
    });
  }

  removeFromAffiliation() {
    this.affiliationService.removeJournalistFromAffiliation(this.id).subscribe({
      next: () => {
        this.notificationBannerService.showSuccess("Success!", "You removed " + this.journalist?.firstName + " " + this.journalist?.lastName + " from your affiliation.")
      },
      error: (error: Error) => {
        this.notificationBannerService.errorHandling(error);
      }
    });
  }

  protected verify() {
    this.journalistService.verify(this.id).subscribe({
      next: () => {
        this.notificationBannerService.showSuccess('Journalist verified!', 'Thanks for reviewing.');
      },
      error: (error: Error) => {
        this.notificationBannerService.errorHandling(error);
      },
    });
  }

  revokeVerification() {
    this.journalistService.unverify(this.id).subscribe({
      next: () => {
        this.notificationBannerService.showSuccess('Journalist unverified!', 'Thanks for reviewing.');
      },
      error: (error: Error) => {
        this.notificationBannerService.errorHandling(error);
      },
    });
  }

  getProfileImg() {
    if (this.journalist?.img) {
      return this.journalist.img;
    }
    return 'assets/user.png';
  }

  protected readonly UserRoles = UserRoles;
}
