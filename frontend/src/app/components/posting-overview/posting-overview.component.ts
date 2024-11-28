import { Component, OnInit } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { Posting } from 'src/app/model/posting';
import { NotificationBannerService } from 'src/app/service/notification-banner.service';
import { PostingService } from 'src/app/service/posting.service';

@Component({
  selector: 'app-posting-overview',
  templateUrl: './posting-overview.component.html',
  styleUrls: ['./posting-overview.component.scss'],
})
export class PostingOverviewComponent {
  /*postingsByTopic: Map<string, Posting[]> = new Map<string, Posting[]>();*/

  postings: Posting[] = [];

  onlyCommented = true;

  constructor(
    private postingService: PostingService,
    private notificationBannerService: NotificationBannerService,
  ) {}
}
