import { Component } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material/core';
import { RegistrationService } from '../../service/registration-service';
import {NotificationBannerService} from "../../service/notification-banner.service";
import {CustomValidators} from "../../validators/customValidators";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent {
  userForm = new FormGroup({
    username:  new FormControl('', [Validators.required]),
    email:  new FormControl('', [Validators.required, Validators.email]),
    password:  new FormControl('', [Validators.required]),
    repeatPassword:  new FormControl('', [Validators.required])
  },
    [CustomValidators.MatchValidator('password', 'repeatPassword')]);

  matcher = new ErrorStateMatcher();

  constructor(private userService: RegistrationService,
              private notificationBannerService : NotificationBannerService
  ) {}

  get passwordMatchError() {
    return (
      this.userForm.getError('mismatch') &&
      this.userForm.get('repeatPassword')?.touched
    );
  }


  onSubmit() {
    if (this.userForm.valid) {
      console.log(this.userForm.value)
      this.userService.createUser(this.userForm.value).subscribe({
        next: () => {
          this.notificationBannerService.showSuccess("Success", `Welcome ${this.userForm.get(
            'userName',
          )?.value}! We've sent you a confirmation mail to ${this.userForm.get(
            'email',
          )
            ?.value} to activate your account. The activation link is valid for 15 minutes.`);
        },
        error: (error: Error) => {
          this.notificationBannerService.errorHandling(error);
        },
      });
    }
  }
}
