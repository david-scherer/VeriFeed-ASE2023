import {Component, OnInit} from '@angular/core';
import {UserService} from "../../service/user.service";
import {AuthenticationService} from "../../service/authentication.service";
import {Router} from "@angular/router";
import {NotificationBannerService} from "../../service/notification-banner.service";
import {User} from "../../model/user";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {ErrorStateMatcher} from "@angular/material/core";
import {CustomValidators} from "../../validators/customValidators";

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss']
})
export class AccountComponent implements OnInit {
  public user = new User('','','');

  userForm = new FormGroup({
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', []),
      repeatPassword: new FormControl('', [])
    },
    [CustomValidators.MatchValidator('password', 'repeatPassword')]);

  matcher = new ErrorStateMatcher();
  file: File | undefined;

  constructor(
    private userService: UserService,
    private authService: AuthenticationService,
    private router: Router,
    private notificationBannerService: NotificationBannerService
  ) {
  }

  get passwordMatchError() {
    return (
      this.userForm.getError('mismatch') &&
      this.userForm.get('repeatPassword')?.touched
    );
  }

  ngOnInit(): void {
    this.userService.getUserById(this.authService.getLoggedInUserId())
      .subscribe({
        next: (user) => {
          console.log(user)
          this.userForm.get("username")?.setValue(user.username);
          this.user = user;
        },
        error: (error: Error) => {
          this.notificationBannerService.errorHandling(error);
        },
      });
  }

  updateAccount() {
    if (this.userForm.valid && this.userForm.value.username != undefined) {
      this.user.username = this.userForm.value.username;
      this.user.password = this.userForm.value.password ?? '';
      this.userService.updateUser(this.user)
        .subscribe({
          next: () => {
            this.notificationBannerService.showSuccess('Change successfully!', 'You are logged out.')
            this.authService.logout()
            this.router.navigateByUrl('/login');
          },
          error: (error: Error) => {
            this.notificationBannerService.errorHandling(error);
          },
        });
    }
  }

  public deleteAccount() {
    this.userService.deleteAccount(this.authService.getLoggedInUserId())
      .subscribe({
        next: () => {
          this.authService.logout()
          this.router.navigateByUrl('/login')
        },
        error: (error: Error) => {
          this.notificationBannerService.errorHandling(error);
        },
      });
  }

  uploadImage() {
    if (this.file && this.user.id) {
      const reader = new FileReader();

      reader.readAsDataURL(this.file);

      reader.onload = (event) => {
        let imageURL = event.target?.result;

        if (imageURL) {
          this.userService.uploadImage({img: imageURL}, this.user.id!).subscribe({
            next: () => {
              this.notificationBannerService.showSuccess("Image updated", "Profile image has been successfully changed");
            },
            error: (err) => {
              this.notificationBannerService.errorHandling(err);
            }
          })
        }
      };
    }
  }

    onFileSelected(event: Event): void {
        const inputEvent = event as Event & { target: HTMLInputElement };
        this.file = inputEvent.target.files ? inputEvent.target.files[0] : undefined;
    }
}

