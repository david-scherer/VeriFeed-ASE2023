import {Component} from '@angular/core';
import {VerificationService} from "../../service/verification.service";
import {VerificationRequestState} from "../../model/verification-request-state";
import {NotificationBannerService} from "../../service/notification-banner.service";

@Component({
  selector: 'app-journalists-requests',
  templateUrl: './journalists-requests.component.html',
  styleUrls: ['./journalists-requests.component.scss']
})
export class JournalistsRequestsComponent {
  verificationRequestsStates: VerificationRequestState[] | undefined;

  constructor(private verificationService: VerificationService,
              private notificationBannerService: NotificationBannerService) {
    this.getRequests();
  }

  private getRequests() {
    this.verificationService.getVerificationRequests().subscribe({
      next: (verificationRequestStates: VerificationRequestState[]) => {
        console.log(verificationRequestStates)
        this.verificationRequestsStates = verificationRequestStates;
      },
      error: (error: Error) => {
        this.notificationBannerService.errorHandling(error)
      }
    });
  }

  protected grantRequest(id: string) {
    this.verificationService.grantVerification(id).subscribe({
      next: () => {
        this.notificationBannerService.showSuccess('Journalist verified!', 'Thanks for granting a new journalist access to our platform.');
        this.getRequests();
      },
      error: (error: Error) => {
        this.notificationBannerService.errorHandling(error);
      },
    });
  }

  protected declineRequest(id: string) {
    this.verificationService.declineVerification(id).subscribe({
      next: () => {
        this.notificationBannerService.showSuccess('Journalist declined!', 'You have declined the request ' + id);
        this.getRequests();
      },
      error: (error: Error) => {
        this.notificationBannerService.errorHandling(error);
      },
    });
  }
}
