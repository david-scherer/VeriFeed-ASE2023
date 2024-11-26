import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../environment/environment";
import {VerificationRequest} from "../model/verification-request";
import {VerificationRequestState} from "../model/verification-request-state";

const baseUri = environment.apiEndpoint + '/verification';
const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  }),
};

@Injectable({
  providedIn: 'root'
})
export class VerificationService {

  constructor(
    private http: HttpClient
  ) {
  }

  /**
   * Get verifications stored in the system of certificate holder
   *
   * @return observable list of verification requests.
   */
  getVerificationRequests(): Observable<VerificationRequestState[]> {
    return this.http.get<VerificationRequestState[]>(baseUri);
  }

  /**
   * Request becoming a verifeed journalist
   * @return observable void
   */
  createVerification(request: VerificationRequest): Observable<void> {
    return this.http.post<void>(baseUri, request, httpOptions);
  }

  /**
   * Grant verification request
   *
   * @return observable of deletion event
   */
  grantVerification(id: string): Observable<void> {
    return this.http.put<void>(baseUri + '/grant/' + id, null);
  }

  /**
   * Decline verification request
   *
   * @return observable of deletion event
   */
  declineVerification(id: string): Observable<void> {
    return this.http.put<void>(baseUri + '/decline/' + id, null);
  }

}
