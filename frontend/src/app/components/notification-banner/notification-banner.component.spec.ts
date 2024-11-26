import {ComponentFixture, TestBed} from '@angular/core/testing';
import {NotificationBannerComponent} from './notification-banner.component';

describe('NotificationBannerComponent', () => {
  let component: NotificationBannerComponent;
  let fixture: ComponentFixture<NotificationBannerComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NotificationBannerComponent],
    });
    fixture = TestBed.createComponent(NotificationBannerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
