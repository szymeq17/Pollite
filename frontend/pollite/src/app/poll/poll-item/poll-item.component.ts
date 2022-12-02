import {Component, Input, OnInit} from '@angular/core';
import {Poll} from "../../model/Poll";
import {PollService} from "../../service/poll.service";

@Component({
  selector: 'app-poll-item',
  templateUrl: './poll-item.component.html',
  styleUrls: ['./poll-item.component.scss']
})
export class PollItemComponent implements OnInit {

  @Input() poll: Poll | undefined;
  selectedAnswer: number | undefined;

  constructor(private pollService: PollService) { }

  ngOnInit(): void {
  }

  vote(): void {
    console.log(this.selectedAnswer)
    this.pollService.vote(this.poll?.id, this.selectedAnswer).subscribe();
  }

}
