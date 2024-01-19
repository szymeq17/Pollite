import {Component, Input, OnInit} from '@angular/core';
import {PollInfo} from "../../model/PollInfo";
import {PollService} from "../../service/poll.service";
import {PollResults} from "../../model/Poll";
import {MatDialog} from "@angular/material/dialog";
import {DeleteSurveyDialogComponent} from "../../survey/delete-survey-dialog/delete-survey-dialog.component";
import {DeletePollDialogComponent} from "../delete-poll-dialog/delete-poll-dialog.component";
import {Clipboard} from "@angular/cdk/clipboard";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-poll-info',
  templateUrl: './poll-info.component.html',
  styleUrls: ['./poll-info.component.scss']
})
export class PollInfoComponent implements OnInit {

  @Input() pollInfo: PollInfo;
  showResults: boolean = false;
  pollResults: PollResults | undefined;

  constructor(private pollService: PollService,
              private deletePollDialog: MatDialog,
              private clipboard: Clipboard,
              private toastr: ToastrService) { }

  ngOnInit(): void {
  }

  getAndShowResults(collapse: any): void {
    if (this.showResults) {
      collapse.toggle();
      this.showResults = false;
      return;
    }

    if (!this.pollResults) {
      this.pollService.getPollResults(this.pollInfo.id).subscribe(
        data => {
          this.pollResults = data;
          collapse.toggle();
          this.showResults = true;
        }
      );
    } else {
      collapse.toggle();
      this.showResults = true;
    }
  }

  deletePoll() {
    this.openDeletePollDialog();
  }

  private openDeletePollDialog() {
    const dialogRef = this.deletePollDialog.open(DeletePollDialogComponent,
      {data: this.pollInfo.id});

    dialogRef.afterClosed().subscribe(shouldDelete => {
      if (shouldDelete) {
        this.pollService.deletePoll(this.pollInfo.id).subscribe(
          _ => window.location.reload()
        );
      }
    });
  }

  copyPollUrlToClipboard() {
    this.clipboard.copy(window.location.host + `/polls/${this.pollInfo.id}`);
    this.toastr.success("Copied URL to clipboard");
  }

}
