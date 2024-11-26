import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JournalistsUnverifiedComponent } from './journalists-unverified.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('JournalistsUnverifiedComponent', () => {
  let component: JournalistsUnverifiedComponent;
  let fixture: ComponentFixture<JournalistsUnverifiedComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [JournalistsUnverifiedComponent]
    });
    fixture = TestBed.createComponent(JournalistsUnverifiedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
