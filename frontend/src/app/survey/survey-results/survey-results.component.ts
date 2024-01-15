import { Component, OnInit } from '@angular/core';
import {FormArray, FormBuilder, FormGroup} from "@angular/forms";
import {SurveyService} from "../../service/survey.service";
import {Survey, SurveyQuestionAnswer} from "../../model/Survey";
import {ActivatedRoute} from "@angular/router";
import {SurveyResults} from "../../model/SurveyResults";
import {CompletedSurveyFilter} from "../../model/CompletedSurveyFilter";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-survey-results',
  templateUrl: './survey-results.component.html',
  styleUrls: ['./survey-results.component.scss']
})
export class SurveyResultsComponent implements OnInit {

  survey: Survey;
  surveyResults: SurveyResults;
  form: FormGroup;

  constructor(private fb: FormBuilder,
              private route: ActivatedRoute,
              private surveyService: SurveyService,
              private toastr: ToastrService) { }

  ngOnInit(): void {
    this.route.params.subscribe(
      params => {
        const surveyId = params['surveyId'];
        this.surveyService.getSurvey(surveyId).subscribe(survey => this.survey = survey);
        this.surveyService.getSurveyResults(surveyId, [])
          .subscribe(surveyResults => this.surveyResults = surveyResults);
        this.form = this.fb.group({
          filters: this.fb.array([])
        });
      });
  }

  get filtersForm(): FormArray {
    return this.form.get('filters') as FormArray;
  }

  addOrRemoveFromFilters(questionId: number, answerId: number) {
    this.toggleAnswer(questionId, answerId);
    if (this.isFilterApplied(questionId, answerId)) {
      this.removeFilterOnClick(questionId, answerId);
      this.toastr.success("Filter removed!");
    } else {
      let filter = this.fb.group({
        questionId: questionId,
        answerId: answerId
      });

      this.filtersForm.push(filter);
      this.toastr.success("Filter applied!");
    }

    this.applyFilters();
  }

  private removeFilterOnClick(questionId: number, answerId: number) {
    let idx = this.filtersForm.value.findIndex((filter: { questionId: number; answerId: number; }) => {
      return filter.questionId == questionId && filter.answerId == answerId;
    });

    this.removeFilter(idx);
  }

  private isFilterApplied(questionId: number, answerId: number) {
    let filters = this.filtersForm.value
    let length = filters.filter((value: { questionId: number; answerId: number; }) =>
      value.questionId == questionId && value.answerId == answerId).length;

    return length > 0;
  }

  addFilter(): void {
    this.filtersForm.push(this.newFilter());
  }

  private removeFilter(filterIndex: number): void {
    this.filtersForm.removeAt(filterIndex)
  }

  private applyFilters() {
    if (this.form.invalid) {
      return;
    }
    const surveyId = this.survey.id;
    if (surveyId === null || surveyId === undefined) {
      return;
    }
    const filters = this.buildFiltersFromForm();
    this.surveyService.getSurveyResults(surveyId, filters).subscribe(
      results => {
        this.fixAnswers(results);
        this.surveyResults = results;
      },
      _ => {
        this.toastr.error("Error occured while applying filter!");
      });
  }

  getQuestionAndAnswerFromFilter(filterIndex: number) {
    const questionId = +this.filtersForm.at(filterIndex).get('questionId')?.value;
    const answerId = +this.filtersForm.at(filterIndex).get('answerId')?.value;
    const question = this.survey.questions.find(question => question.id === questionId);
    // @ts-ignore
    const answer = question.answers.find(answer => answer.id == answerId);

    return {
      // @ts-ignore
      questionText: question.text,
      // @ts-ignore
      answerText: answer.text
    }
  }

  getAnswersForSelectedQuestion(filterIndex: number): SurveyQuestionAnswer[] {
    const questionId = +this.filtersForm.at(filterIndex).get('questionId')?.value;
    const question = this.survey.questions.find(question => question.id === questionId);
    if (question) {
      return question.answers;
    }
    return [];
  }

  private newFilter() {
    return this.fb.group({
      questionId: null,
      answerId: null
    });
  }

  private buildFiltersFromForm(): CompletedSurveyFilter[] {
    return this.form.get('filters')?.value.map((filter: any) => {
      return {
        questionId: filter.questionId,
        answerId: filter.answerId
      } as CompletedSurveyFilter
    });
  }

  private toggleAnswer(questionId: number, answerId: number) {
    const results = this.surveyResults.questionsResults;
    const questionIndex = results.findIndex(result => result.questionId == questionId);
    const answerIndex = results[questionIndex].answersResults
      .findIndex(answerResult => answerResult.answerId == answerId);
    const answerResult = results[questionIndex].answersResults[answerIndex];
    answerResult.selected = !answerResult.selected;
  }

  private fixAnswers(surveyResults: SurveyResults) {
    // @ts-ignore
    this.filtersForm.value.forEach(filter => this.fixAnswer(filter.questionId, filter.answerId, surveyResults));
    console.log(surveyResults)
  }

  private fixAnswer(questionId: number, answerId: number, surveyResults: SurveyResults) {
    const questionResult = surveyResults.questionsResults
      .find(questionResult => questionResult.questionId == questionId);
    // @ts-ignore
    questionResult.answersResults.find(answerResult => answerResult.answerId == answerId).selected = true;
  }

}
