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
      return;
    }

    let filter = this.fb.group({
      questionId: questionId,
      answerId: answerId
    });

    this.filtersForm.push(filter);

  }

  private removeFilterOnClick(questionId: number, answerId: number) {
    console.log(this.filtersForm.value)
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

  removeFilter(filterIndex: number): void {
    this.filtersForm.removeAt(filterIndex)
  }

  applyFilters() {
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
        this.toastr.info("Filters applied!");
        this.surveyResults = results;
      },
      _ => {
        this.toastr.error("Error occured while applying filters!");
      });
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

}
