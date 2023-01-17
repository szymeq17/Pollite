import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Params, Router} from "@angular/router";
import {SurveyService} from "../service/survey.service";
import {Survey, SurveyQuestion} from "../../model/Survey";
import {AbstractControl, FormArray, FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {MatCheckboxChange} from "@angular/material/checkbox";
import {MatRadioChange} from "@angular/material/radio";
import {CompletedSurvey, CompletedSurveyQuestion} from "../../model/CompletedSurvey";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-complete-survey-form',
  templateUrl: './complete-survey-form.component.html',
  styleUrls: ['./complete-survey-form.component.scss']
})
export class CompleteSurveyFormComponent implements OnInit {

  constructor(private fb: FormBuilder,
              private route: ActivatedRoute,
              private surveyService: SurveyService,
              private router: Router,
              private toastr: ToastrService) { }
  survey: Survey;
  questionsToAnswer: SurveyQuestion[];
  excludedQuestionsOrders: number[] = [];
  form: FormGroup = new FormGroup({});

  ngOnInit(): void {
    this.route.params.subscribe((params: Params) => {
      const surveyId = params['surveyId']
      this.surveyService.getSurvey(surveyId).subscribe(response => {
        this.survey = response;
        this.survey.description = "Przykładowy opis ankiety. Bla bla bla bla";
        this.questionsToAnswer = response.questions;

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
    console.log(this.buildCompletedSurveyFromForm())
    this.surveyService.submitCompletedSurvey(this.buildCompletedSurveyFromForm()).subscribe(response => {
      this.toastr.success("Survey submitted!");
    })
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
    this.updateQuestionsToAnswer();
  }

  onRadioChange(e: MatRadioChange, questionIndex: number) {
    const answers: FormArray = this.completedQuestionAnswersForms(questionIndex);
    answers.clear();
    answers.push(new FormControl(e.value));
    this.updateQuestionsToAnswer();
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

  private updateQuestionsToAnswer() {
    const exclusions = this.survey.configuration.exclusions;
    const questionOrdersToRemove = exclusions
      .filter(exclusion => this.isAnswerChecked(exclusion.questionOrder, exclusion.answerOrder))
      .map(exclusion => exclusion.excludedQuestionOrder);

    this.questionsToAnswer = this.survey.questions.filter(question => !questionOrdersToRemove.includes(question.order));
    this.excludedQuestionsOrders = questionOrdersToRemove;
  }

  private isAnswerChecked(questionOrder: number, answerOrder: number): boolean {
    const completedQuestions = this.completedQuestionForms.controls;
    return completedQuestions.some(control => this.hasQuestionAnswerChecked(control, questionOrder, answerOrder));
  }

  private hasQuestionAnswerChecked(question: AbstractControl, questionOrder: number, answerOrder: number): boolean {
    const answeredQuestionOrder = question.get('order')?.value as number;
    if (questionOrder !== answeredQuestionOrder) {
      return false;
    }
    const answers = question.get('answers') as FormArray;
    return answers.controls.some(answer => this.findAnswerOrderById(questionOrder, answer.value) === answerOrder);
  }

  private findAnswerOrderById(questionOrder: number, answerId: number): number | null | undefined {
    const question = this.survey.questions.find(question => question.order === questionOrder);
    return question ? question.answers.find(answer => answer.id === answerId)?.order : null;
  }

  private buildCompletedSurveyFromForm(): CompletedSurvey {
    return {
      surveyId: this.survey.id,
      completedQuestions: this.form.get('completedQuestions')?.value
        .filter(
          (question: any) =>
            !this.excludedQuestionsOrders.includes(question.order))
        .map((question: SurveyQuestion) => this.buildCompletedQuestionFromForm(question))
    } as CompletedSurvey;
  }

  private buildCompletedQuestionFromForm(question: any): CompletedSurveyQuestion {
    return {
      questionId: question.questionId,
      questionAnswerIds: question.answers
    } as CompletedSurveyQuestion;
  }

}
