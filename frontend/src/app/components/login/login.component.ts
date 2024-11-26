import {Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ErrorStateMatcher} from '@angular/material/core';
import {Router} from '@angular/router';
import {AuthenticationService} from '../../service/authentication.service';
import {NotificationBannerService} from "../../service/notification-banner.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  loginForm: FormGroup;

  emailFormControl = new FormControl('', [
    Validators.required,
    Validators.email,
  ]);
  passwordFormControl = new FormControl('', [Validators.required]);

  matcher = new ErrorStateMatcher();

  constructor(
    private loginService: AuthenticationService,
    private router: Router,
    private notificationBannerService: NotificationBannerService
  ) {
    this.loginForm = new FormGroup({
      email: this.emailFormControl,
      password: this.passwordFormControl,
    });
  }

  onSubmit() {
    if (this.loginService.isLoggedIn()) {
      this.loginService.logout();
    }
    if (this.loginForm.valid) {
      this.loginService.login(this.loginForm.value).subscribe({
        next: () => {
          this.router.navigateByUrl('/internal');
        },
        error: (error: Error) => {
          this.notificationBannerService.errorHandling(error);
        },
      });
    }
  }
}
