import {Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ErrorStateMatcher} from '@angular/material/core';
import {AffiliationsService} from "../../service/affiliations.service";
import {NotificationBannerService} from "../../service/notification-banner.service";

@Component({
  selector: 'app-affiliations-create',
  templateUrl: './affiliations-create.component.html',
  styleUrls: ['./affiliations-create.component.scss']
})
export class AffiliationsCreateComponent {
  affiliationForm: FormGroup;

  nameFormControl = new FormControl('', [Validators.required]);
  addressFormControl = new FormControl('', [Validators.pattern('^(http(s):\\/\\/.)[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)$')]);
  matcher = new ErrorStateMatcher();

  constructor(private affiliationService: AffiliationsService,
              private notificationBannerService : NotificationBannerService) {
    this.affiliationForm = new FormGroup({
      name: this.nameFormControl,
      address: this.addressFormControl
    });
  }

  onSubmit() {
    if (this.affiliationForm.valid) {
      console.log(this.affiliationForm.value)
      this.affiliationService.createAffiliation(this.affiliationForm.value).subscribe({
        next: () => {
          this.notificationBannerService.showSuccess("Success!", `Affiliation ${this.affiliationForm.get(
            'name',
          )?.value} has been created for you. Verification process is running.`)
        },
        error: (error: Error) => {
          this.notificationBannerService.errorHandling(error);
        },
      });
    }
  }

}
