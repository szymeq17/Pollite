import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompleteSurveyFormComponent } from './complete-survey-form.component';

describe('CompleteSurveyFormComponent', () => {
  let component: CompleteSurveyFormComponent;
  let fixture: ComponentFixture<CompleteSurveyFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CompleteSurveyFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CompleteSurveyFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
