import {ComponentFixture, TestBed} from '@angular/core/testing';

import {JournalistsDetailsComponent} from './journalists-details.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {RouterModule} from "@angular/router";

describe('JournalistsDetailsComponent', () => {
  let component: JournalistsDetailsComponent;
  let fixture: ComponentFixture<JournalistsDetailsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterModule.forRoot([])],
      declarations: [JournalistsDetailsComponent]
    });
    fixture = TestBed.createComponent(JournalistsDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
