import {Component, Input, OnInit} from '@angular/core';
import {Poll} from "../../model/Poll";

@Component({
  selector: 'app-poll-item',
  templateUrl: './poll-item.component.html',
  styleUrls: ['./poll-item.component.scss']
})
export class PollItemComponent implements OnInit {

  @Input() poll: Poll | undefined;

  constructor() { }

  ngOnInit(): void {
  }

}
