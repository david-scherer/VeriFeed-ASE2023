import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JournalistsRequestsComponent } from './journalists-requests.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('JournalistsRequestsComponent', () => {
  let component: JournalistsRequestsComponent;
  let fixture: ComponentFixture<JournalistsRequestsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [JournalistsRequestsComponent]
    });
    fixture = TestBed.createComponent(JournalistsRequestsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
