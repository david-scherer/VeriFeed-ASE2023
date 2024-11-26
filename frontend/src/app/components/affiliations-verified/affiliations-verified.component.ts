import { Component } from '@angular/core';
import {Affiliation} from "../../model/affiliation";
import {AffiliationsService} from "../../service/affiliations.service";
import {NotificationBannerService} from "../../service/notification-banner.service";

@Component({
  selector: 'app-affiliations-verified',
  templateUrl: './affiliations-verified.component.html',
  styleUrls: ['./affiliations-verified.component.scss']
})
export class AffiliationsVerifiedComponent {
  affiliations: Affiliation[] | undefined;

  constructor(private affiliationService: AffiliationsService,
              private notificationBannerService : NotificationBannerService
              ) {
    this.getVerifiedAffiliations();
  }


  private getVerifiedAffiliations() {
    this.affiliationService.getVerifiedAffiliations().subscribe({
      next: (affiliations: Affiliation[]) => {
        this.affiliations = affiliations;
      },
      error: (error: Error) => {
        this.notificationBannerService.errorHandling(error)
      }
    });
  }
}
