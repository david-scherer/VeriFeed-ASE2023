import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopicDetailsComponent } from './topic-details.component';

describe('TopicDetailsComponent', () => {
  let component: TopicDetailsComponent;
  let fixture: ComponentFixture<TopicDetailsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TopicDetailsComponent]
    });
    fixture = TestBed.createComponent(TopicDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
