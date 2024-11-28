import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../environment/environment";
import {Certificate} from "../model/certificate";

const baseUri = environment.apiEndpoint + '/certificate';
const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  }),
};

@Injectable({
  providedIn: 'root'
})
export class CertificatesService {

  constructor(
    private http: HttpClient
  ) {
  }

  /**
   * Get verified affiliations stored in the system
   *
   * @return observable list of found verified affiliations.
   */
  getCertificates(): Observable<Certificate[]> {
    return this.http.get<Certificate[]>(baseUri);
  }


  /**
   * save specified affiliation in the system
   * @return observable void
   */
  createCertificate(journalistId: string, reference: string, explanation: string): Observable<void> {
    return this.http.post<void>(baseUri, {
      id: journalistId,
      reference: reference,
      explanation: explanation
    }, httpOptions);
  }

  /**
   * Revoke certificate of a journalist
   *
   * @return observable of deletion event
   */
  revokeCertificate(id: string): Observable<void> {
    return this.http.delete<void>(baseUri + '/' + id);
  }

}
