import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environment/environment';
import { Posting } from '../model/posting';
import { Commentary } from '../model/commentary';
import { NewCommentary } from '../model/newCommentary';

const baseUri = environment.apiEndpoint + '/post';
const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  }),
};

@Injectable({
  providedIn: 'root',
})
export class PostingService {
  constructor(private http: HttpClient) {}

  /**
   * Get all postings stored in the system
   *
   * @return observable list of found postings.
   */
  getAll(): Observable<Posting[]> {
    return this.http.get<Posting[]>(baseUri);
  }

  getPosts(start: number, numberOfPosts: number, onlyCommented: boolean, q: string) {
    return this.http.get<Posting[]>(
      baseUri +
        '?offset=' +
        start +
        '&limit=' +
        numberOfPosts +
        '&onlyCommented=' +
        onlyCommented +
        '&q=' +
        q,

    );
  }

  blockPost(id: string) {
    return this.http.delete<void>(`${baseUri}/${id}`);
  }

  postCommentary(
    id: string,
    commentary: NewCommentary,
  ): Observable<Commentary> {
    return this.http.post<Commentary>(
      baseUri + '/' + id + '/commentaries',
      commentary,
    );
  }

  editCommentary(
    id: string,
    commentaryId: string,
    commentary: NewCommentary,
  ): Observable<Commentary> {
    return this.http.patch<Commentary>(
      baseUri + '/' + id + '/commentaries/' + commentaryId,
      commentary,
    );
  }

  getCommentaries(id: string): Observable<Commentary[]> {
    return this.http.get<Commentary[]>(baseUri + '/' + id + '/commentaries');
  }

  deleteCommentary(postId: string, commentId: string): Observable<void> {
    return this.http.delete<void>(
      baseUri + '/' + postId + '/commentaries/' + commentId,
    );
  }

  upvoteCommentary(postId: string, commentId: string): Observable<void> {
    return this.http.post<void>(
      baseUri + '/' + postId + '/commentaries/' + commentId + '/up',
      null,
    );
  }

  downvoteCommentary(postId: string, commentId: string): Observable<void> {
    return this.http.post<void>(
      baseUri + '/' + postId + '/commentaries/' + commentId + '/down',
      null,
    );
  }

  unvoteCommentary(postId: string, commentId: string): Observable<void> {
    return this.http.post<void>(
      baseUri + '/' + postId + '/commentaries/' + commentId + '/un',
      null,
    );
  }
}
