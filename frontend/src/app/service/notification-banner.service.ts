import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class NotificationBannerService {

  title = 'No title provided';
  message = '';
  messageType = '';
  show = false;

  vanishAlert() {
    this.show = false;
  }

  errorHandling(error: any) {
    console.log(error);
    this.show = true;
    this.messageType = 'error';
    this.title = 'Error';
    if (typeof error.error === 'object') {
      this.message = error.error.error;
    } else {
      this.message = error.error;
    }
    setTimeout(() => {
      this.vanishAlert();
    }, 9000);
  }

  errorHandlingWithTitle(title: string, error: any) {
    console.log(error);
    this.show = true;
    this.messageType = 'error';
    this.title = title;
    if (typeof error.error === 'object') {
      this.message = error.error.error;
    } else {
      this.message = error.error;
    }
    setTimeout(() => {
      this.vanishAlert();
    }, 9000);
  }

  showError(title: string, message: string) {
    console.log(title + ': ' + message);
    this.show = true;
    this.messageType = 'error';
    this.title = title;
    this.message = message;
    setTimeout(() => {
      this.vanishAlert();
    }, 9000);
  }

  showSuccess(title: string, message: string) {
    console.log(title + ': ' + message);
    this.show = true;
    this.messageType = 'success';
    this.title = title;
    this.message = message;
    setTimeout(() => {
      this.vanishAlert();
    }, 9000);
  }
}
