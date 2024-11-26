import { Injectable } from '@angular/core';
import { tap } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { LoginCredentials } from '../model/LoginCredentials';
import { environment } from '../environment/environment';
import { jwtDecode } from 'jwt-decode';
import {UserRoles} from "../guards/user-roles";

const loginUrl = environment.apiEndpoint + '/login';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  constructor(
    private http: HttpClient
  ) {}

  public login(login: LoginCredentials) {
    return this.http.post(loginUrl, login, {responseType: 'text'})
      .pipe(
        tap((authResponse: string) => this.setToken(authResponse))
      );
  }

  isLoggedIn() {
    if (this.getToken()) {
      return this.getTokenExpirationDate(this.getToken()!)!.valueOf() > new Date().valueOf()
    }
    return false;
  }

  logout() {
    console.log('Logout');
    localStorage.removeItem('authToken');
  }

  getToken() {
    return localStorage.getItem('authToken');
  }

  /**
   * Returns the user role based on the current token
   */
  getUserRole(): UserRoles {
    if (this.getToken() != null) {
      const decoded: any = jwtDecode(this.getToken()!);
      const authInfo: string[] = decoded.rol;
      if (authInfo.includes('ROLE_ADMIN')) {
        return UserRoles.ADMIN;
      } else if (authInfo.includes('ROLE_JOURNALIST')) {
        return UserRoles.JOURNALIST;
      } else if (authInfo.includes('ROLE_USER')) {
        return UserRoles.USER;
      }
    }
    return UserRoles.UNDEFINED;
  }

  private setToken(authResponse: string) {
    localStorage.setItem('authToken', authResponse);
  }

  private getTokenExpirationDate(token: string): Date | null {

    const decoded: any = jwtDecode(token);
    if (decoded.exp === undefined) {
      return null;
    }

    const date = new Date(0);
    date.setUTCSeconds(decoded.exp);
    return date;
  }

  getLoggedInUserId() {
    if (this.getToken()) {
      const decoded: any = jwtDecode(this.getToken()!);
      return decoded.sub
    }
    return 'UNDEFINED';
  }
}
