import { Component, OnInit } from '@angular/core';
import {AbstractControl, FormArray, FormBuilder, FormGroup} from "@angular/forms";

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
      questions: this.fb.array([])
    });
  }

  get questionForms(): FormArray {
    return this.form.get('questions') as FormArray;
  }

  questionAnswers(questionIndex: number) {
    return this.questionForms.at(questionIndex).get('answers') as FormArray;
  }

  addQuestion() {
    const question = this.fb.group({
      text: '',
      answers: this.fb.array([]),
      multiChoice: false
    });

    this.questionForms.push(question);
  }

  removeQuestion(questionIndex: number) {
    this.questionForms.removeAt(questionIndex);
  }

  addAnswerToQuestion(questionIndex: number) {
    const answer = this.fb.group({
      text: ''
    });

    this.questionAnswers(questionIndex).push(answer);
  }

  removeAnswerFromQuestion(questionIndex: number, answerIndex: number) {
    this.questionAnswers(questionIndex).removeAt(answerIndex);
  }

}
