import { Component, OnInit } from '@angular/core';
import {FormArray, FormBuilder, FormGroup} from "@angular/forms";
import {Survey, SurveyQuestion, SurveyQuestionAnswer, SurveyQuestionExclusion} from "../../model/Survey";
import {SurveyService} from "../../service/survey.service";
import {ToastrService} from "ngx-toastr";
import {Router} from "@angular/router";

@Component({
  selector: 'app-survey-form',
  templateUrl: './survey-form.component.html',
  styleUrls: ['./survey-form.component.scss']
})
export class SurveyFormComponent implements OnInit {

  form: FormGroup;

  constructor(private fb: FormBuilder,
              private surveyService: SurveyService,
              private router: Router,
              private toastr: ToastrService) {
  }

  ngOnInit(): void {
    let surveyIdAsString = localStorage.getItem('surveyId');
    if (surveyIdAsString) {
      let surveyId = Number.parseInt(surveyIdAsString);
      this.surveyService.getSurvey(surveyId).subscribe(
        survey => this.form = this.buildFormFromSurvey(survey)
      );
      localStorage.removeItem('surveyId');
    } else {
      this.form = this.fb.group({
        description: '',
        questions: this.fb.array([this.newQuestion()]),
        exclusions: this.fb.array([]),
        isActive: true,
        startDate: null,
        endDate: null
      });
    }
  }

  get questionForms(): FormArray {
    return this.form.get('questions') as FormArray;
  }

  get exclusionForms() {
    return this.form.get('exclusions') as FormArray;
  }

  questionAnswerForms(questionIndex: number) {
    return this.questionForms.at(questionIndex).get('answers') as FormArray;
  }

  addQuestion() {
    this.questionForms.push(this.newQuestion());
  }

  removeQuestion(questionIndex: number) {
    if (this.questionForms.length === 1) {
      this.toastr.error("Survey must contain at least 1 question!");
      return;
    }
    this.questionForms.removeAt(questionIndex);
  }

  addAnswerToQuestion(questionIndex: number) {
    this.questionAnswerForms(questionIndex).push(this.newAnswer());
  }

  removeAnswerFromQuestion(questionIndex: number, answerIndex: number) {
    const answers = this.questionAnswerForms(questionIndex);
    if (answers.length === 2) {
      this.toastr.error("Question must have at least 2 answers!")
      return;
    }
    answers.removeAt(answerIndex);
  }

  addExclusion() {
    this.exclusionForms.push(this.newExclusion());
  }

  removeExclusion(exclusionIndex: number) {
    this.exclusionForms.removeAt(exclusionIndex);
  }

  questionNumbers() {
    return Array.from(this.questionForms.controls.keys()).map(key => key + 1);
  }

  answerNumbers(exclusionIndex: number) {
    const questionNumber = this.exclusionForms.at(exclusionIndex).get('questionOrder')?.value as number;
    if (questionNumber === null) {
      return [];
    }
    return Array.from(this.questionAnswerForms(questionNumber - 1).controls.keys()).map(key => key + 1);
  }

  questionNumbersPossibleToExclude(exclusionIndex: number) {
    const questionNumber = this.exclusionForms.at(exclusionIndex).get('questionOrder')?.value as number;
    if (questionNumber === null) {
      return [];
    }
    return this.questionNumbers().filter(number => number > questionNumber);
  }

  moveQuestionUp(questionIndex: number) {
    if (questionIndex > 0) {
      const array = this.questionForms.value;
      const newArray = this.swap(array, questionIndex - 1, questionIndex);
      const controls1 = this.questionAnswerForms(questionIndex);
      const controls1values = [...this.questionAnswerForms(questionIndex).value];
      const controls2 = this.questionAnswerForms(questionIndex - 1);
      const controls2values = [...this.questionAnswerForms(questionIndex - 1).value];
      while (controls1.length) {

        controls1.removeAt(0);
      }
      controls2values.forEach(control => controls1.push(this.fb.group(control)))
      while (controls2.length) {
        controls2.removeAt(0);
      }
      controls1values.forEach(control => controls2.push(this.fb.group(control)))

      this.questionForms.setValue(newArray);
    }
  }

  moveQuestionDown(questionIndex: number) {
    const array = this.questionForms.value;
    if (questionIndex < array.length - 1) {
      const array = this.questionForms.value;
      const newArray = this.swap(array, questionIndex, questionIndex + 1);
      const controls1 = this.questionAnswerForms(questionIndex);
      const controls1values = [...this.questionAnswerForms(questionIndex).value];
      const controls2 = this.questionAnswerForms(questionIndex + 1);
      const controls2values = [...this.questionAnswerForms(questionIndex + 1).value];
      while (controls1.length) {

        controls1.removeAt(0);
      }
      controls2values.forEach(control => controls1.push(this.fb.group(control)))
      while (controls2.length) {
        controls2.removeAt(0);
      }
      controls1values.forEach(control => controls2.push(this.fb.group(control)))

      this.questionForms.setValue(newArray);
    }
  }

  moveAnswerUp(questionIndex: number, answerIndex: number) {
    if (answerIndex > 0) {
      const array = this.questionAnswerForms(questionIndex).value;
      const newArray = this.swap(array, answerIndex - 1, answerIndex);
      this.questionAnswerForms(questionIndex).setValue(newArray);
    }
  }

  moveAnswerDown(questionIndex: number, answerIndex: number) {
    const array = this.questionAnswerForms(questionIndex).value;
    if (answerIndex < array.length - 1) {
      const newArray = this.swap(array, answerIndex, answerIndex + 1);
      this.questionAnswerForms(questionIndex).setValue(newArray);
    }
  }

  onSubmit() {
    this.surveyService.createSurvey(this.buildSurveyFromForm()).subscribe(response => {
      this.toastr.success("Survey published!");
      this.router.navigate([`/survey/${response}`]);
    });
  }

  private swap(arr: any[], index1: number, index2: number): any[] {
    arr = [...arr];
    const temp = arr[index1];
    arr[index1] = arr[index2];
    arr[index2] = temp;
    return arr;
  }

  private newQuestion() {
    return this.fb.group({
      text: '',
      answers: this.fb.array([this.newAnswer(), this.newAnswer()]),
      multiChoice: false
    });
  }

  private newAnswer() {
    return this.fb.group({
      text: ''
    });
  }

  private newExclusion() {
    return this.fb.group({
      questionOrder: null,
      answerOrder: null,
      excludedQuestionOrder: null
    });
  }

  private buildSurveyFromForm(): Survey {
    return {
      id: this.form.get('id')?.value,
      description: this.form.get('description')?.value,
      questions: this.form.get('questions')?.value
        .map((question: any, index: number) => this.buildQuestionFromForm(question, index)),
      configuration: {
        id: this.form.get('configurationId')?.value,
        isActive: this.form.get('isActive')?.value,
        startDate: this.form.get('startDate')?.value,
        endDate: this.form.get('endDate')?.value,
        exclusions: this.form.get('exclusions')?.value || []
      }
    } as Survey
  }

  private buildQuestionFromForm(formQuestion: any, index: number) {
    return {
      id: formQuestion.id,
      type: formQuestion.multiChoice ? 'MULTI_CHOICE' : 'SINGLE_CHOICE',
      text: formQuestion.text,
      order: index + 1,
      answers: formQuestion.answers.map((answer: any, index: number) => this.buildAnswerFromForm(answer, index))
    } as SurveyQuestion
  }

  private buildAnswerFromForm(formAnswer: any, index: number): SurveyQuestionAnswer {
    return {
      id: formAnswer.id,
      text: formAnswer.text,
      order: index + 1
    } as SurveyQuestionAnswer
  }

  private buildFormFromSurvey(survey: Survey): FormGroup {
    return this.fb.group({
      id: survey.id,
      description: survey.description,
      questions: this.fb.array(survey.questions.map(question => this.buildQuestionFormFromQuestion(question))),
      configurationId: survey.configuration.id,
      exclusions: this.fb.array(
        survey.configuration.exclusions.map(exclusion => this.buildExclusionFormFromQuestion(exclusion))),
      isActive: survey.configuration.isActive,
      startDate: survey.configuration.startDate,
      endDate: survey.configuration.endDate
    });
  }

  private buildExclusionFormFromQuestion(exclusion: SurveyQuestionExclusion): FormGroup {
    return this.fb.group({
      id: exclusion.id,
      questionOrder: exclusion.questionOrder,
      answerOrder: exclusion.answerOrder,
      excludedQuestionOrder: exclusion.excludedQuestionOrder
    });
  }

  private buildQuestionFormFromQuestion(question: SurveyQuestion) {
    return this.fb.group({
      id: question.id,
      text: question.text,
      multiChoice: question.type === "MULTI_CHOICE",
      order: question.order,
      answers: this.fb.array(question.answers.map(answer => this.buildAnswerFormFromAnswer(answer)))
    });
  }

  private buildAnswerFormFromAnswer(answer: SurveyQuestionAnswer): FormGroup {
    return this.fb.group({
      id: answer.id,
      text: answer.text,
      order: answer.order
    });
  }


}
