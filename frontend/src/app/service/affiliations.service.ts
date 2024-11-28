import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../environment/environment";
import {Affiliation} from "../model/affiliation";

const baseUri = environment.apiEndpoint + '/affiliation';
const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  }),
};

@Injectable({
  providedIn: 'root'
})
export class AffiliationsService {

  constructor(
    private http: HttpClient
  ) {
  }

  /**
   * Get verified affiliations stored in the system
   *
   * @return observable list of found verified affiliations.
   */
  getVerifiedAffiliations(): Observable<Affiliation[]> {
    return this.http.get<Affiliation[]>(baseUri + '/verified');
  }

  /**
   * Get unverified affiliations stored in the system
   *
   * @return observable list of found unverified affiliations.
   */
  getUnverifiedAffiliations(): Observable<Affiliation[]> {
    return this.http.get<Affiliation[]>(baseUri + '/unverified');
  }

  /**
   * Get affiliation with specified id
   *
   * @return observable of found affiliation.
   */
  getAffiliationById(id: string): Observable<Affiliation> {
    return this.http.get<Affiliation>(baseUri + '/' + id);
  }

  /**
   * save specified affiliation in the system
   * @return observable void
   */
  createAffiliation(affiliation: Affiliation): Observable<void> {
    return this.http.post<void>(baseUri, {
      name: affiliation.name,
      address: affiliation.address,
    }, httpOptions);
  }

  /**
   * verifies an affiliation
   * @return observable void
   */
  verifyAffiliation(id: string): Observable<void> {
    return this.http.put<void>(baseUri + '/' + id + '/verify', httpOptions);
  }

  /**
   * adds a journalist to an affiliation
   * @return observable void
   */
  addJournalistToAffiliation(idJournalist: string): Observable<void> {
    return this.http.put<void>(baseUri + '/add/' + idJournalist, httpOptions);
  }

  /**
   * removes a journalist from an affiliation
   * @return observable void
   */
  removeJournalistFromAffiliation(idJournalist: string | null): Observable<void> {
    return this.http.delete<void>(baseUri + '/remove/' + idJournalist, httpOptions);
  }

  /**
   * Belongs Journalist To Affiliation
   *
   * @return observable boolean
   */
  belongsJournalistToAffiliation(id: string): Observable<boolean> {
    return this.http.get<boolean>(baseUri + '/user/' + id);
  }
}
