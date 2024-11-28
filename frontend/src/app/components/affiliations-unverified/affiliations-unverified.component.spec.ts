import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AffiliationsUnverifiedComponent } from './affiliations-unverified.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('AffiliationsUnverifiedComponent', () => {
  let component: AffiliationsUnverifiedComponent;
  let fixture: ComponentFixture<AffiliationsUnverifiedComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AffiliationsUnverifiedComponent]
    });
    fixture = TestBed.createComponent(AffiliationsUnverifiedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
