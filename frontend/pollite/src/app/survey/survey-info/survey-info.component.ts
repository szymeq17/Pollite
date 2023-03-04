import {Component, Input, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {SurveyInfo} from "../../model/SurveyInfo";
import {MatDialog} from "@angular/material/dialog";
import {DeleteSurveyDialogComponent} from "../delete-survey-dialog/delete-survey-dialog.component";
import {SurveyService} from "../../service/survey.service";

@Component({
  selector: 'app-survey-info',
  templateUrl: './survey-info.component.html',
  styleUrls: ['./survey-info.component.scss']
})
export class SurveyInfoComponent implements OnInit {

  @Input() surveyInfo: SurveyInfo;

  constructor(private router: Router,
              private deleteSurveyDialog: MatDialog,
              private surveyService: SurveyService) {
  }

  ngOnInit(): void {
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
        console.log("TU")
        this.surveyService.deleteSurvey(this.surveyInfo.surveyId).subscribe(
          _ => window.location.reload()
        );
      }
    });
  }

}
