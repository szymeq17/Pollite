import {Component, Input, OnInit} from '@angular/core';
import {PollInfo} from "../../model/PollInfo";
import {PollService} from "../../service/poll.service";
import {PollResults} from "../../model/Poll";

@Component({
  selector: 'app-poll-info',
  templateUrl: './poll-info.component.html',
  styleUrls: ['./poll-info.component.scss']
})
export class PollInfoComponent implements OnInit {

  @Input() pollInfo: PollInfo;
  showResults: boolean = false;
  pollResults: PollResults | undefined;

  constructor(private pollService: PollService) { }

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

}
