import { Component, OnInit } from '@angular/core';
import {FormArray, FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-survey-form',
  templateUrl: './survey-form.component.html',
  styleUrls: ['./survey-form.component.scss']
})
export class SurveyFormComponent implements OnInit {

  form: FormGroup;

  constructor(private fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.form = this.fb.group({
      description: '',
      questions: this.fb.array([this.newQuestion()])
    });
  }

  get questionForms(): FormArray {
    return this.form.get('questions') as FormArray;
  }

  questionAnswers(questionIndex: number) {
    return this.questionForms.at(questionIndex).get('answers') as FormArray;
  }

  addQuestion() {
    this.questionForms.push(this.newQuestion());
  }

  removeQuestion(questionIndex: number) {
    this.questionForms.removeAt(questionIndex);
  }

  addAnswerToQuestion(questionIndex: number) {
    this.questionAnswers(questionIndex).push(this.newAnswer());
  }

  removeAnswerFromQuestion(questionIndex: number, answerIndex: number) {
    this.questionAnswers(questionIndex).removeAt(answerIndex);
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

  saveSurvey() {

  }

}
