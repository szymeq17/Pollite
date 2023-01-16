import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Params} from "@angular/router";
import {SurveyService} from "../service/survey.service";
import {Survey, SurveyQuestion} from "../../model/Survey";
import {AbstractControl, FormArray, FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {MatCheckboxChange} from "@angular/material/checkbox";
import {MatRadioChange} from "@angular/material/radio";

@Component({
  selector: 'app-complete-survey-form',
  templateUrl: './complete-survey-form.component.html',
  styleUrls: ['./complete-survey-form.component.scss']
})
export class CompleteSurveyFormComponent implements OnInit {

  constructor(private fb: FormBuilder,
              private route: ActivatedRoute,
              private surveyService: SurveyService) { }
  survey: Survey;
  form: FormGroup = new FormGroup({});

  ngOnInit(): void {
    this.route.params.subscribe((params: Params) => {
      const surveyId = params['surveyId']
      this.surveyService.getSurvey(surveyId).subscribe(response => {
        this.survey = response;
        this.survey.description = "Przykładowy opis ankiety. Bla bla bla bla";

        this.form = this.fb.group({
          surveyId: surveyId,
          completedQuestions: this.fb.array(this.buildQuestionGroups())
        })
      });
    });
  }

  get completedQuestionForms(): FormArray {
    return this.form.get('completedQuestions') as FormArray;
  }

  completedQuestionAnswersForms(questionIndex: number): FormArray {
    return this.completedQuestionForms.at(questionIndex).get('answers') as FormArray
  }

  onSubmit() {
    console.log(this.form.value);
  }

  onCheckboxChange(e: MatCheckboxChange, questionIndex: number) {
    const answers: FormArray = this.completedQuestionAnswersForms(questionIndex);
    const value: number = Number.parseInt(e.source.value);
    if (e.checked) {
      answers.push(new FormControl(value));
    } else {
      let i = 0;
      answers.controls.forEach((item: AbstractControl) => {
        if (item.value === value) {
          answers.removeAt(i);
          return;
        }
        i++;
      })
    }
  }

  onRadioChange(e: MatRadioChange, questionIndex: number) {
    const answers: FormArray = this.completedQuestionAnswersForms(questionIndex);
    answers.clear();
    answers.push(new FormControl(e.value));
  }

  private buildQuestionGroups(): FormGroup[] {
    return this.survey.questions.map(question => this.buildCompletedQuestion(question));
  }

  private buildCompletedQuestion(question: SurveyQuestion) {
    return this.fb.group({
      questionId: question.id,
      text: question.text,
      order: question.order,
      type: question.type,
      answers: this.fb.array([])
    });
  }

}
