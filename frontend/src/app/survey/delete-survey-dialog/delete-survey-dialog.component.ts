import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-delete-survey-dialog',
  templateUrl: './delete-survey-dialog.component.html',
  styleUrls: ['./delete-survey-dialog.component.scss']
})
export class DeleteSurveyDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<DeleteSurveyDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public shouldDelete: boolean,
  ) { }

  onNoClick(): void {
    this.shouldDelete = false;
    this.dialogRef.close();
  }

}
