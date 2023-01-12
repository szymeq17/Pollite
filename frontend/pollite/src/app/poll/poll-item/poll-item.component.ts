import {Component, Input, OnInit} from '@angular/core';
import {Poll, PollResults} from "../../model/Poll";
import {PollService} from "../service/poll.service";

@Component({
  selector: 'app-poll-item',
  templateUrl: './poll-item.component.html',
  styleUrls: ['./poll-item.component.scss']
})
export class PollItemComponent implements OnInit {

  @Input() poll: Poll | undefined;
  selectedAnswer: number | undefined;
  pollResults: PollResults | undefined;
  voted: boolean = false;

  constructor(private pollService: PollService) { }

  ngOnInit(): void {
  }

  vote(collapse: any): void {
    this.pollService.vote(this.poll?.id, this.selectedAnswer).subscribe(
      data => {
        this.pollResults = data;
        collapse.toggle();
        this.voted = true;
      }
    );
  }

}
