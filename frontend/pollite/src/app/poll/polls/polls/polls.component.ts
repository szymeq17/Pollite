import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {Poll} from "../../../model/Poll";
import {PollService} from "../../../service/poll.service";
import {MatPaginator} from "@angular/material/paginator";
import {tap} from "rxjs";

@Component({
  selector: 'app-polls',
  templateUrl: './polls.component.html',
  styleUrls: ['./polls.component.scss']
})
export class PollsComponent implements OnInit, AfterViewInit {

  polls: Poll[] = [];
  pollsTotal: number;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private pollService: PollService) { }

  ngOnInit(): void {
    this.pollService.getPolls(0, 3).subscribe(
      data => {
        this.polls = data['content'];
        this.pollsTotal = data['totalElements'];
      }
    );
  }

  ngAfterViewInit(): void {
    this.paginator.page
      .pipe(
        tap(() => this.loadPolls())
      )
      .subscribe();
  }

  loadPolls(): void {
    this.pollService.getPolls(this.paginator.pageIndex, this.paginator.pageSize).subscribe(
      data => this.polls = data['content']
    );
  }

}
