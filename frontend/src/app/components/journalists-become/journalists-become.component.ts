import {Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {ErrorStateMatcher} from "@angular/material/core";
import {NotificationBannerService} from "../../service/notification-banner.service";
import {Certificate} from "../../model/certificate";
import {CertificatesService} from "../../service/certificates.service";
import {UserRoles} from "../../guards/user-roles";
import {AuthenticationService} from "../../service/authentication.service";
import {VerificationService} from "../../service/verification.service";
import {VerificationRequest} from "../../model/verification-request";
import {Address} from "../../model/address";

@Component({
  selector: 'app-journalists-become',
  templateUrl: './journalists-become.component.html',
  styleUrls: ['./journalists-become.component.scss']
})
export class JournalistsBecomeComponent {
  certificates: Certificate[] | undefined;
  submitted = false;
  verificationForm = new FormGroup({
    certificate: new FormControl('', [Validators.required]),
    firstName: new FormControl('', [Validators.required]),
    lastName: new FormControl('', [Validators.required]),
    dateOfBirth: new FormControl('', [Validators.required]),
    street: new FormControl(''),
    city: new FormControl(''),
    postalCode: new FormControl(''),
    country: new FormControl(''),
    state: new FormControl(''),
    employer: new FormControl(''),
    distributionReach: new FormControl('', [Validators.required]),
    mainMedium: new FormControl('', [Validators.required]),
    urlMedium: new FormControl('', [Validators.required, Validators.pattern('^(http(s):\\/\\/.)[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)$')]),
    requestMessage: new FormControl('', [Validators.required]),
  });

  matcher = new ErrorStateMatcher();

  constructor(private certificateService: CertificatesService,
              private verificationService: VerificationService,
              private notificationBannerService: NotificationBannerService,
              public authService: AuthenticationService) {
    this.getCertificates();
  }

  private getCertificates() {
    this.certificateService.getCertificates().subscribe({
      next: (certificates: Certificate[]) => {
        console.log(certificates);
        this.certificates = certificates;
      },
      error: (error: Error) => {
        this.notificationBannerService.errorHandling(error)
      }
    });
  }

  onSubmit() {
    if (this.verificationForm.valid) {
      console.log(this.verificationForm.value)
      let request = new VerificationRequest(this.verificationForm.value.certificate ?? '',
        this.verificationForm.value.firstName ?? '', this.verificationForm.value.lastName ?? '',
        new Date(this.verificationForm.value.dateOfBirth ?? '1900/01/01'),
        Number(this.verificationForm.value.distributionReach ?? 0),
        this.verificationForm.value.mainMedium ?? '',
        this.verificationForm.value.urlMedium ?? '',
        this.verificationForm.value.requestMessage ?? '',
        new Address(this.verificationForm.value.country ?? '',
          this.verificationForm.value.city ?? '',
          this.verificationForm.value.postalCode ?? '',
          this.verificationForm.value.street ?? '',
          undefined,
          this.verificationForm.value.state ?? ''),
        this.verificationForm.value.employer ?? '')
      console.log(request)
      this.verificationService.createVerification(request).subscribe({
        next: () => {
          this.notificationBannerService.showSuccess("Success!", `Your verification request was submitted. Verification process is running.`)
          this.submitted = true;
        },
        error: (error: Error) => {
          this.notificationBannerService.errorHandling(error);
        },
      });
    }
  }

  protected readonly UserRoles = UserRoles;
}
