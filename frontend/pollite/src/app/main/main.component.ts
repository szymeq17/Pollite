import { Component, OnInit } from '@angular/core';
import {AuthService} from "../service/auth.service";
import {User} from "../model/User";

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class MainComponent implements OnInit {

  user: User;

  constructor(private authService: AuthService) {
    authService.user.subscribe(user => this.user = user);
  }

  ngOnInit(): void {
  }

}
