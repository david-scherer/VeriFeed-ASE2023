import { TestBed } from '@angular/core/testing';

import { AffiliationsService } from './affiliations.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('AffiliationsService', () => {
  let service: AffiliationsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(AffiliationsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
