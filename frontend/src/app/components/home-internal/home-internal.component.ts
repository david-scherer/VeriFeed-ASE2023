import {Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthenticationService } from '../../service/authentication.service';

@Component({
  selector: 'app-home-internal',
  templateUrl: './home-internal.component.html',
  styleUrls: ['./home-internal.component.scss'],
})
export class HomeInternalComponent implements OnInit {
  public content = '';

  constructor(
    private http: HttpClient,
    public loginService: AuthenticationService,
  ) {
    console.log('Is user logged in: ' + this.loginService.isLoggedIn());
  }

  ngOnInit(): void {}
}
