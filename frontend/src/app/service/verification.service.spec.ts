import { TestBed } from '@angular/core/testing';

import { VerificationService } from './verification.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('VerificationService', () => {
  let service: VerificationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(VerificationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
