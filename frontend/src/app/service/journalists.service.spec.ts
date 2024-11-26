import { TestBed } from '@angular/core/testing';

import { JournalistsService } from './journalists.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('JournalistsService', () => {
  let service: JournalistsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(JournalistsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
