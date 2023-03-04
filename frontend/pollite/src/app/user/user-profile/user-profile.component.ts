import {Component, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {PollService} from "../../service/poll.service";
import {Poll} from "../../model/Poll";
import {MatPaginator} from "@angular/material/paginator";

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {

  polls: Poll[];
  pollsTotal: number;
  @ViewChild(MatPaginator) pollsPaginator: MatPaginator;

  constructor(private route: ActivatedRoute,
              private pollService: PollService) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const username = params['username'];
      this.pollService.getUsersPolls(username, 0, 10).subscribe(data => {
        this.polls = data['content'];
        this.pollsTotal = data['totalElements'];
      });
    })
  }

}
