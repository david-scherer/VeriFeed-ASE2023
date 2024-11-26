import {Component} from '@angular/core';
import {Router} from '@angular/router';
import {AuthenticationService} from "./service/authentication.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {

  constructor(
    public router: Router,
    public authService: AuthenticationService
  ) {
  }
}
