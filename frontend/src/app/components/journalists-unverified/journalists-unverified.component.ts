import { Component } from '@angular/core';
import {Journalist} from "../../model/journalist";
import {JournalistsService} from "../../service/journalists.service";
import {NotificationBannerService} from "../../service/notification-banner.service";

@Component({
  selector: 'app-journalists-unverified',
  templateUrl: './journalists-unverified.component.html',
  styleUrls: ['./journalists-unverified.component.scss']
})
export class JournalistsUnverifiedComponent {
  journalists: Journalist[] | undefined;

  constructor(private journalistService: JournalistsService, private notificationBannerService : NotificationBannerService) {
    this.getUnverifiedAffiliations();
  }


  private getUnverifiedAffiliations() {
    this.journalistService.getUnverifiedJournalists().subscribe({
      next: (journalists: Journalist[]) => {
        this.journalists = journalists;
      },
      error: (error: Error) => {
        this.notificationBannerService.errorHandling(error)
      }
    });
  }

  protected verifyAgain(id: string) {
    this.journalistService.verify(id).subscribe({
      next: () => {
        this.notificationBannerService.showSuccess('Journalist verified again!', 'Thanks for reviewing.');
        this.getUnverifiedAffiliations();
      },
      error: (error: Error) => {
        this.notificationBannerService.errorHandling(error);
      },
    });
  }
}
