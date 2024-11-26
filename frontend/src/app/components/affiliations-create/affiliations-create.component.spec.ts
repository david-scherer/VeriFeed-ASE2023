import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AffiliationsCreateComponent} from './affiliations-create.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {ReactiveFormsModule} from "@angular/forms";
import {MatInputModule} from "@angular/material/input";

describe('AffiliationsCreateComponent', () => {
  let component: AffiliationsCreateComponent;
  let fixture: ComponentFixture<AffiliationsCreateComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ReactiveFormsModule, MatInputModule],
      declarations: [AffiliationsCreateComponent]
    });
    fixture = TestBed.createComponent(AffiliationsCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
