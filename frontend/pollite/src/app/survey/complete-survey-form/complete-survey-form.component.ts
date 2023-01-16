import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Params} from "@angular/router";
import {SurveyService} from "../service/survey.service";
import {Survey} from "../../model/Survey";
import {FormBuilder, FormGroup} from "@angular/forms";

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
  form: FormGroup;

  ngOnInit(): void {
    this.route.params.subscribe((params: Params) => {
      const surveyId = params['surveyId']
      this.surveyService.getSurvey(surveyId).subscribe(response => {
        this.survey = response;
        this.survey.description = "Przykładowy opis ankiety. Bla bla bla bla";
      });

      this.form = this.fb.group({
        surveyId: surveyId,
        completedQuestions: this.fb.array([])
      })
    });
  }

  onSubmit() {
    console.log(this.form.value);
  }

  // private buildQuestions(): FormArray {
  //   return this.survey.questions.map()
  // }
  //
  // private buildQuestionAnswer(answer: SurveyQuestionAnswer) {
  //   return this.fb.control()
  // }

}
