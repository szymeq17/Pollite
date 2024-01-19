import {Component, Input, OnInit} from '@angular/core';
import {Poll, PollResults} from "../../model/Poll";
import {PollService} from "../../service/poll.service";
import {ActivatedRoute, Params} from "@angular/router";
import {Clipboard} from "@angular/cdk/clipboard";
import {ToastrService} from "ngx-toastr";

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

  constructor(private pollService: PollService,
              private route: ActivatedRoute,
              private clipboard: Clipboard,
              private toastr: ToastrService) { }

  ngOnInit(): void {
    console.log(this.poll);
    if (this.poll) {
      return;
    }

    this.route.params.subscribe((params: Params) => {
      const pollId = params['pollId'];
      this.pollService.getPoll(pollId).subscribe(response => {
        this.poll = response;
      });
    });
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

  copyPollUrlToClipboard() {
    this.clipboard.copy(window.location.href + `/${this.poll?.id}`);
    this.toastr.success("Copied URL to clipboard");
  }

}
