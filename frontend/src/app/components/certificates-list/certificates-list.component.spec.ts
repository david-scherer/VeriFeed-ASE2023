import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CertificatesListComponent } from './certificates-list.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('CertificatesListComponent', () => {
  let component: CertificatesListComponent;
  let fixture: ComponentFixture<CertificatesListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CertificatesListComponent]
    });
    fixture = TestBed.createComponent(CertificatesListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
