import { Component } from '@angular/core';
import {Certificate} from "../../model/certificate";
import {CertificatesService} from "../../service/certificates.service";
import {NotificationBannerService} from "../../service/notification-banner.service";

@Component({
  selector: 'app-certificates-list',
  templateUrl: './certificates-list.component.html',
  styleUrls: ['./certificates-list.component.scss']
})
export class CertificatesListComponent {
  certificates: Certificate[] | undefined;

  constructor(private certificatesService: CertificatesService, private notificationBannerService : NotificationBannerService) {
    this.getCertificates();
  }

  revokeCertificate(id: string) {
    this.certificatesService.revokeCertificate(id).subscribe({
      next: () => {
        this.notificationBannerService.showSuccess("Success!", "Certificate revoked.")
      },
      error: (error: Error) => {
        this.notificationBannerService.errorHandling(error)
      }
    });
  }

  private getCertificates() {
    this.certificatesService.getCertificates().subscribe({
      next: (certificates: Certificate[]) => {
        this.certificates = certificates;
      },
      error: (error: Error) => {
        this.notificationBannerService.errorHandling(error)
      }
    });
  }
}
