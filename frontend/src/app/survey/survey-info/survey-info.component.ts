import {Component, Input, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {SurveyInfo} from "../../model/SurveyInfo";
import {MatDialog} from "@angular/material/dialog";
import {DeleteSurveyDialogComponent} from "../delete-survey-dialog/delete-survey-dialog.component";
import {SurveyService} from "../../service/survey.service";
import {AuthService} from "../../service/auth.service";

@Component({
  selector: 'app-survey-info',
  templateUrl: './survey-info.component.html',
  styleUrls: ['./survey-info.component.scss']
})
export class SurveyInfoComponent implements OnInit {

  @Input() surveyInfo: SurveyInfo;
  isOwnedByLoggedInUser: boolean;

  constructor(private router: Router,
              private authService: AuthService,
              private deleteSurveyDialog: MatDialog,
              private surveyService: SurveyService) {
  }

  ngOnInit(): void {
    this.authService.user.subscribe(user =>
    {
      this.isOwnedByLoggedInUser = user && user.username === this.surveyInfo.owner
    });
  }

  editSurvey() {
    localStorage.setItem('surveyId', this.surveyInfo.surveyId.toString());
    this.router.navigate(['/survey'])
  }

  showResulsts() {
    this.router.navigate([`/survey/${this.surveyInfo.surveyId}/results`]);
  }

  deleteSurvey() {
    this.openDeleteSurveyDialog();
  }

  private openDeleteSurveyDialog() {
    const dialogRef = this.deleteSurveyDialog.open(DeleteSurveyDialogComponent,
      {data: this.surveyInfo.surveyId});

    dialogRef.afterClosed().subscribe(shouldDelete => {
      if (shouldDelete) {
        this.surveyService.deleteSurvey(this.surveyInfo.surveyId).subscribe(
          _ => window.location.reload()
        );
      }
    });
  }

}
