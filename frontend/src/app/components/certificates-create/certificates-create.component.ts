import {Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {ErrorStateMatcher} from "@angular/material/core";
import {NotificationBannerService} from "../../service/notification-banner.service";
import {JournalistsService} from "../../service/journalists.service";
import {Journalist} from "../../model/journalist";
import {CertificatesService} from "../../service/certificates.service";

@Component({
  selector: 'app-certificates-create',
  templateUrl: './certificates-create.component.html',
  styleUrls: ['./certificates-create.component.scss']
})
export class CertificatesCreateComponent {
  journalists: Journalist[] | undefined;
  certificateForm: FormGroup;
  journalistFormControl = new FormControl('', [Validators.required]);
  explanationFormControl = new FormControl('');
  addressFormControl = new FormControl('', [Validators.pattern('^(http(s):\\/\\/.)[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)$')]);
  matcher = new ErrorStateMatcher();

  constructor(private journalistsService: JournalistsService,
              private certificateService: CertificatesService,
              private notificationBannerService: NotificationBannerService) {
    this.certificateForm = new FormGroup({
      journalistId: this.journalistFormControl,
      explanation: this.explanationFormControl,
      address: this.addressFormControl
    });
    this.getVerifiedUncertifiedJournalists();
  }

  private getVerifiedUncertifiedJournalists() {
    this.journalistsService.getVerifiedUncertifiedJournalists().subscribe({
      next: (journalists: Journalist[]) => {
        this.journalists = journalists;
      },
      error: (error: Error) => {
        this.notificationBannerService.errorHandling(error);
      }
    });
  }

  onSubmit() {
    console.log(this.certificateForm.value.journalistId + this.certificateForm.value.explanation + this.certificateForm.value.address)
    if (this.certificateForm.valid) {
      this.certificateService.createCertificate(this.certificateForm.value.journalistId, this.certificateForm.value.reference, this.certificateForm.value.address).subscribe({
        next: () => {
          this.notificationBannerService.showSuccess("Success!", "Certificate has been created.")
        },
        error: (error: Error) => {
          this.notificationBannerService.errorHandling(error);
        },
      });
    }
  }

}
