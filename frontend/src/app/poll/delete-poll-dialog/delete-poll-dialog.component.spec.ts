import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeletePollDialogComponent } from './delete-poll-dialog.component';

describe('DeletePollDialogComponent', () => {
  let component: DeletePollDialogComponent;
  let fixture: ComponentFixture<DeletePollDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DeletePollDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DeletePollDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
