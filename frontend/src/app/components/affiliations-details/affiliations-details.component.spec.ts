import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AffiliationsDetailsComponent} from './affiliations-details.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {RouterModule} from "@angular/router";

describe('AffiliationsDetailsComponent', () => {
  let component: AffiliationsDetailsComponent;
  let fixture: ComponentFixture<AffiliationsDetailsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterModule.forRoot([])],
      declarations: [AffiliationsDetailsComponent]
    });
    fixture = TestBed.createComponent(AffiliationsDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
