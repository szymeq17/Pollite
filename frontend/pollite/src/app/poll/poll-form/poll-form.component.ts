import { Component, OnInit } from '@angular/core';
import {FormArray, FormBuilder, FormGroup} from "@angular/forms";
import {PollService} from "../../service/poll.service";
import {ToastrService} from "ngx-toastr";
import {Router} from "@angular/router";
import {Poll, PollAnswer} from "../../model/Poll";

@Component({
  selector: 'app-poll-form',
  templateUrl: './poll-form.component.html',
  styleUrls: ['./poll-form.component.scss']
})
export class PollFormComponent implements OnInit {

  form: FormGroup;

  constructor(private fb: FormBuilder,
              private pollService: PollService,
              private router: Router,
              private toastr: ToastrService) { }

  ngOnInit(): void {
    this.form = this.fb.group(
      {
        text: '',
        pollAnswers: this.fb.array([this.newAnswer(), this.newAnswer()]),
        startDate: null,
        endDate: null
      }
    );
  }

  get answerForms(): FormArray {
    return this.form.get('pollAnswers') as FormArray;
  }

  onSubmit() {
    this.pollService.createPoll(this.buildPollFromForm()).subscribe(
      _ => {
        this.toastr.success("Poll has been created!");
        this.router.navigate(['/polls']);
      }
    )
  }

  addAnswer() {
    this.answerForms.push(this.newAnswer());
  }

  removeAnswer(answerIndex: number) {
    if (this.answerForms.length <= 2) {
      this.toastr.error("Poll must contain at least 2 answers!");
      return;
    }

    this.answerForms.removeAt(answerIndex);
  }

  private newAnswer() {
    return this.fb.group({
      text: ''
    });
  }

  private buildPollFromForm(): Poll {
    return {
      text: this.form.get('text')?.value,
      pollAnswers: this.form.get('pollAnswers')?.value.map((formAnswer: any) => this.buildAnswerFromForm(formAnswer))
    } as Poll;
  }

  private buildAnswerFromForm(formAnswer: any): PollAnswer {
    return {
      text: formAnswer.text
    } as PollAnswer;
  }

}
