import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderInternalComponent } from './header-internal.component';
import {HttpClientModule} from "@angular/common/http";
import {MatIconModule} from "@angular/material/icon";
import {MatMenuModule} from "@angular/material/menu";

describe('HeaderInternalComponent', () => {
  let component: HeaderInternalComponent;
  let fixture: ComponentFixture<HeaderInternalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HeaderInternalComponent],
      imports: [HttpClientModule, MatIconModule, MatMenuModule]
    });
    fixture = TestBed.createComponent(HeaderInternalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
