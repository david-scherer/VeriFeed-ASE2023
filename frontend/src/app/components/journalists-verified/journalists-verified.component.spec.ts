import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JournalistsVerifiedComponent } from './journalists-verified.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('JournalistsVerifiedComponent', () => {
  let component: JournalistsVerifiedComponent;
  let fixture: ComponentFixture<JournalistsVerifiedComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [JournalistsVerifiedComponent]
    });
    fixture = TestBed.createComponent(JournalistsVerifiedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
