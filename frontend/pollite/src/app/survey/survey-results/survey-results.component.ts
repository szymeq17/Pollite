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

}
