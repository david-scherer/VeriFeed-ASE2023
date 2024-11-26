import {ComponentFixture, TestBed} from '@angular/core/testing';

import {JournalistsComponent} from './journalists.component';
import {RouterTestingModule} from "@angular/router/testing";
import {HttpClientModule} from "@angular/common/http";

describe('JournalistsComponent', () => {
  let component: JournalistsComponent;
  let fixture: ComponentFixture<JournalistsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule, HttpClientModule],
      declarations: [JournalistsComponent]
    });
    fixture = TestBed.createComponent(JournalistsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
