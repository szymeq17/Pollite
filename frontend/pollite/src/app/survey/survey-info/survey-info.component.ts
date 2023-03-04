import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {SurveyInfo} from "../../model/SurveyInfo";

@Component({
  selector: 'app-survey-info',
  templateUrl: './survey-info.component.html',
  styleUrls: ['./survey-info.component.scss']
})
export class SurveyInfoComponent implements OnInit {

  @Input() surveyInfo: SurveyInfo;

  constructor(private router: Router) {
  }

  ngOnInit(): void {
  }

  showResulsts() {
    this.router.navigate([`/survey/${this.surveyInfo.surveyId}/results`]);
  }

}
