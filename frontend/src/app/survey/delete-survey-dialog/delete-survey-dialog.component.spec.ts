import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteSurveyDialogComponent } from './delete-survey-dialog.component';

describe('DeleteSurveyDialogComponent', () => {
  let component: DeleteSurveyDialogComponent;
  let fixture: ComponentFixture<DeleteSurveyDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DeleteSurveyDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DeleteSurveyDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
