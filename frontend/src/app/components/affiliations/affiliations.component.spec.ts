import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AffiliationsComponent} from './affiliations.component';
import {RouterTestingModule} from "@angular/router/testing";
import {HttpClientModule} from "@angular/common/http";

describe('AffiliationsComponent', () => {
  let component: AffiliationsComponent;
  let fixture: ComponentFixture<AffiliationsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule, HttpClientModule],
      declarations: [AffiliationsComponent]
    });
    fixture = TestBed.createComponent(AffiliationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
