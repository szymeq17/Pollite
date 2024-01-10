import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PollInfoComponent } from './poll-info.component';

describe('PollInfoComponent', () => {
  let component: PollInfoComponent;
  let fixture: ComponentFixture<PollInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PollInfoComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PollInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
