import {Component} from '@angular/core';
import {Affiliation} from "../../model/affiliation";
import {AffiliationsService} from "../../service/affiliations.service";
import {ActivatedRoute, ParamMap} from "@angular/router";
import {NotificationBannerService} from "../../service/notification-banner.service";

@Component({
  selector: 'app-affiliations-details',
  templateUrl: './affiliations-details.component.html',
  styleUrls: ['./affiliations-details.component.scss']
})
export class AffiliationsDetailsComponent {
  affiliation: Affiliation | undefined;
  id: string | null = '';

  constructor(private affiliationService: AffiliationsService,
              private route: ActivatedRoute,
              private notificationBannerService : NotificationBannerService) {
    this.route.paramMap.subscribe((params: ParamMap) => {
      this.id = this.route.snapshot.paramMap.get('id');
    });
    this.getAffiliation();
  }


  private getAffiliation() {
    if(this.id == null) return;
    this.affiliationService.getAffiliationById(this.id).subscribe({
      next: (affiliation: Affiliation) => {
        console.log(affiliation);
        this.affiliation = affiliation;
      },
      error: (error: Error) => {
        this.notificationBannerService.errorHandling(error);
      }
    });
  }

}
