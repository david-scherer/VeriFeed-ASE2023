import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CertificatesCreateComponent} from './certificates-create.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {ReactiveFormsModule} from "@angular/forms";
import {MatInputModule} from "@angular/material/input";

describe('CertificatesCreateComponent', () => {
  let component: CertificatesCreateComponent;
  let fixture: ComponentFixture<CertificatesCreateComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ReactiveFormsModule, MatInputModule],
      declarations: [CertificatesCreateComponent]
    });
    fixture = TestBed.createComponent(CertificatesCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
