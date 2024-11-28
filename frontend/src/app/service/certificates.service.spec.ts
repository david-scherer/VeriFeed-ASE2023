import { TestBed } from '@angular/core/testing';

import { CertificatesService } from './certificates.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('CertifcatesService', () => {
  let service: CertificatesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(CertificatesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
