import {Component, OnInit, ViewChild} from '@angular/core';
import {PollInfo} from "../../model/PollInfo";
import {MatPaginator} from "@angular/material/paginator";
import {PollService} from "../../service/poll.service";
import {AuthService} from "../../service/auth.service";
import {tap} from "rxjs";

@Component({
  selector: 'app-polls-view',
  templateUrl: './polls-view.component.html',
  styleUrls: ['./polls-view.component.scss']
})
export class PollsViewComponent implements OnInit {

  pollInfos: PollInfo[];
  pollsTotal: number;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private pollService: PollService,
              private authService: AuthService) { }

  ngOnInit(): void {
    this.pollService.getUsersPollInfos(this.authService.userName, 0, 5)
      .subscribe(
        data => {
          this.pollInfos = data['content'];
          this.pollsTotal = data['totalElements'];
        }
      );
  }

  ngAfterViewInit(): void {
    this.paginator.page
      .pipe(
        tap(() => this.loadPollInfos())
      )
      .subscribe();
  }

  loadPollInfos(): void {
    this.pollService.getUsersPollInfos(this.authService.userName, this.paginator.pageIndex, this.paginator.pageSize)
      .subscribe(
        data => this.pollInfos = data['content']
      );
  }
}
