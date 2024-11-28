import { Injectable } from '@angular/core';
import {User} from "../model/user";
import {Observable} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../environment/environment";

const baseUri = environment.apiEndpoint + '/user';
const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  }),
};

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(
    private http: HttpClient
  ) { }

  /**
   * Get all users stored in the system
   *
   * @return observable list of found users.
   */
  getAll(): Observable<User[]> {
    return this.http.get<User[]>(baseUri);
  }

  /**
   * Get user with specified email
   *
   * @return observable of found users.
   */
  getUserById(id: string): Observable<User> {
    return this.http.get<User>(baseUri + '/' + id);
  }

  /**
   * Delete the user with the specified id
   *
   * @return observable of deletion event
   */
  deleteAccount(id: string): Observable<void> {
    return this.http.delete<void>(baseUri + '/' + id);
  }

  /**
   * Delete the user with the specified id
   *
   * @return observable of deletion event
   */
  updateUser(user: User): Observable<void> {
    return this.http.put<void>(baseUri + '/' + user.id, user);
  }

  uploadImage(profileImage: {img: string | ArrayBuffer}, id: string) {
    return this.http.put<void>(baseUri + '/' + id + '/img', profileImage);
  }
}
