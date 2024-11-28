import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomeInternalComponent } from './home-internal.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('HomeInternalComponent', () => {
  let component: HomeInternalComponent;
  let fixture: ComponentFixture<HomeInternalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [HomeInternalComponent],
    });
    fixture = TestBed.createComponent(HomeInternalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
