import { Component } from '@angular/core';
import {AffiliationsService} from "../../service/affiliations.service";
import {Affiliation} from "../../model/affiliation";
import {NotificationBannerService} from "../../service/notification-banner.service";

@Component({
  selector: 'app-affiliations-unverified',
  templateUrl: './affiliations-unverified.component.html',
  styleUrls: ['./affiliations-unverified.component.scss']
})
export class AffiliationsUnverifiedComponent {
  affiliations: Affiliation[] | undefined;

  constructor(private affiliationService: AffiliationsService,
              private notificationBannerService : NotificationBannerService) {
    this.getUnverifiedAffiliations();
    }


  private getUnverifiedAffiliations() {
    this.affiliationService.getUnverifiedAffiliations().subscribe({
      next: (affiliations: Affiliation[]) => {
        this.affiliations = affiliations;
      },
      error: (error: Error) => {
        this.notificationBannerService.errorHandling(error);
      }
    });
  }

  verifyAffiliation(id: string | undefined) {
    if (id == undefined) {
      return;
    }
      this.affiliationService.verifyAffiliation(id).subscribe({
        next: () => {
          this.notificationBannerService.showSuccess("Success!", "Affiliation verified.")
        },
        error: (error: Error) => {
          this.notificationBannerService.errorHandling(error)
        },
      });
  }
}
