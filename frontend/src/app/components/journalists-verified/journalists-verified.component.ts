import { Component } from '@angular/core';
import {NotificationBanner} from "../../model/notification-banner";
import {Journalist} from "../../model/journalist";
import {JournalistsService} from "../../service/journalists.service";
import {NotificationBannerService} from "../../service/notification-banner.service";

@Component({
  selector: 'app-journalists-verified',
  templateUrl: './journalists-verified.component.html',
  styleUrls: ['./journalists-verified.component.scss']
})
export class JournalistsVerifiedComponent {
  journalists: Journalist[] | undefined;

  constructor(private journalistsService: JournalistsService,
              private notificationBannerService : NotificationBannerService) {
    this.getVerifiedJournalists();
  }


  private getVerifiedJournalists() {
    this.journalistsService.getVerifiedJournalists().subscribe({
      next: (journalists: Journalist[]) => {
        this.journalists = journalists;
      },
      error: (error: Error) => {
        this.notificationBannerService.errorHandling(error);
      }
    });
  }

    getProfileImg(journalist: Journalist) {
      if (journalist?.img) {
        return journalist.img;
      }
      return 'assets/user.png';
    }
}
