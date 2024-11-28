import {Component} from '@angular/core';
import {NotificationBannerService} from "../../service/notification-banner.service";

@Component({
  selector: 'app-notification-banner',
  templateUrl: './notification-banner.component.html',
  styleUrls: ['./notification-banner.component.scss'],
})
export class NotificationBannerComponent {
  constructor(public notificationBanner: NotificationBannerService) {
  }
}
