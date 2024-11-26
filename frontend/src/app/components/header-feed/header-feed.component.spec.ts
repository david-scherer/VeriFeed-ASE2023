import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderFeedComponent } from './header-feed.component';

describe('HeaderFeedComponent', () => {
  let component: HeaderFeedComponent;
  let fixture: ComponentFixture<HeaderFeedComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HeaderFeedComponent]
    });
    fixture = TestBed.createComponent(HeaderFeedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
