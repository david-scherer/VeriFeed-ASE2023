import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environment/environment';
import { User } from '../model/user';
import {AuthenticationService} from "./authentication.service";

const baseUri = environment.apiEndpoint + '/registration';
const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  }),
};

@Injectable({
  providedIn: 'root',
})
export class RegistrationService {
  constructor(
    private http: HttpClient,
    private authService: AuthenticationService
  ) {}

  /**
   * save specified user in the system
   * @return observable with this user
   */
  createUser(user: any): Observable<User> {
    this.authService.logout()
    return this.http.post<User>(baseUri, {
      username: user.username,
      email: user.email,
      password: user.password
    }, httpOptions);
  }

}
