import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PollsViewComponent } from './polls-view.component';

describe('PollsViewComponent', () => {
  let component: PollsViewComponent;
  let fixture: ComponentFixture<PollsViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PollsViewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PollsViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
