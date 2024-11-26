import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AffiliationsVerifiedComponent } from './affiliations-verified.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('AffiliationsVerifiedComponent', () => {
  let component: AffiliationsVerifiedComponent;
  let fixture: ComponentFixture<AffiliationsVerifiedComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AffiliationsVerifiedComponent]
    });
    fixture = TestBed.createComponent(AffiliationsVerifiedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
