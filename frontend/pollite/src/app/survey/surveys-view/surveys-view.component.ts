import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from "@angular/material/paginator";
import {SurveyInfo} from "../../model/SurveyInfo";
import {SurveyService} from "../../service/survey.service";
import {AuthService} from "../../service/auth.service";
import {tap} from "rxjs";

@Component({
  selector: 'app-surveys-view',
  templateUrl: './surveys-view.component.html',
  styleUrls: ['./surveys-view.component.scss']
})
export class SurveysViewComponent implements OnInit, AfterViewInit {

  surveyInfos: SurveyInfo[];
  surveysTotal: number;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private surveyService: SurveyService,
              private authService: AuthService) { }

  ngOnInit(): void {
    this.surveyService.getSurveyInfos(this.authService.userName, 0, 5).subscribe(
      data => {
        this.surveyInfos = data['content'];
        this.surveysTotal = data['totalElements'];
      }
    );
  }

  ngAfterViewInit(): void {
    this.paginator.page
      .pipe(
        tap(() => this.loadSurveys())
      )
      .subscribe();
  }

  loadSurveys(): void {
    this.surveyService.getSurveyInfos(this.authService.userName, this.paginator.pageIndex, this.paginator.pageSize)
      .subscribe(
        data => this.surveyInfos = data['content']
      );
  }

}
