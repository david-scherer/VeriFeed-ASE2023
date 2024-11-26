import {ComponentFixture, TestBed} from '@angular/core/testing';

import {JournalistsBecomeComponent} from './journalists-become.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {ReactiveFormsModule} from "@angular/forms";
import {MatInputModule} from "@angular/material/input";

describe('JournalistsBecomeComponent', () => {
  let component: JournalistsBecomeComponent;
  let fixture: ComponentFixture<JournalistsBecomeComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ReactiveFormsModule, MatInputModule],
      declarations: [JournalistsBecomeComponent]
    });
    fixture = TestBed.createComponent(JournalistsBecomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
