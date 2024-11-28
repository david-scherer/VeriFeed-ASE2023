import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthenticationService } from '../service/authentication.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private authService: AuthenticationService) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler,
  ): Observable<HttpEvent<any>> {
    if (!this.authService.isLoggedIn()) {
      return next.handle(req);
    }

    const authReq = req.clone({
      headers: req.headers.set(
        'Authorization',
        'Bearer ' + this.authService.getToken(),
      ),
    });

    return next.handle(authReq);
  }
}
