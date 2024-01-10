import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-delete-poll-dialog',
  templateUrl: './delete-poll-dialog.component.html',
  styleUrls: ['./delete-poll-dialog.component.scss']
})
export class DeletePollDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<DeletePollDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public shouldDelete: boolean
  ) { }

  onNoClick(): void {
    this.shouldDelete = false;
    this.dialogRef.close();
  }

}
