import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../environment/environment";
import {Journalist} from "../model/journalist";

const baseUri = environment.apiEndpoint + '/journalist';
const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  }),
};

@Injectable({
  providedIn: 'root'
})
export class JournalistsService {

  constructor(
    private http: HttpClient
  ) { }

  /**
   * Get verified journalists stored in the system
   *
   * @return observable list of found verified journalists.
   */
  getVerifiedJournalists(): Observable<Journalist[]> {
    return this.http.get<Journalist[]>(baseUri + '/verified');
  }

  /**
   * Get verified and certified journalists stored in the system
   *
   * @return observable list of found verified and certified journalists.
   */
  getVerifiedCertifiedJournalists(): Observable<Journalist[]> {
    return this.http.get<Journalist[]>(baseUri + '/verified&certified');
  }

  /**
   * Get verified and uncertified journalists stored in the system
   *
   * @return observable list of found verified and uncertified journalists.
   */
  getVerifiedUncertifiedJournalists(): Observable<Journalist[]> {
    return this.http.get<Journalist[]>(baseUri + '/verified&uncertified');
  }

  /**
   * Get unverified journalists stored in the system
   *
   * @return observable list of found unverified journalists.
   */
  getUnverifiedJournalists(): Observable<Journalist[]> {
    return this.http.get<Journalist[]>(baseUri + '/unverified');
  }

  /**
   * Get journalist with specified id
   *
   * @return observable of found journalist.
   */
  getJournalistById(id: string): Observable<Journalist> {
    return this.http.get<Journalist>(baseUri + '/' + id);
  }

  /**
   * Verify journalist
   *
   * @return void
   */
  verify(id: string | null): Observable<void> {
    return this.http.put<void>(baseUri + '/verify/' + id, null);
  }

  /**
   * Unverify journalist
   *
   * @return void
   */
  unverify(id: string | null): Observable<void> {
    return this.http.put<void>(baseUri + '/unverify/' + id, null);
  }

}
