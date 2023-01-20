import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {AuthService} from "../service/auth.service";
import {User} from "../model/User";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  user: User;

  constructor(private router: Router,
              private authService: AuthService) {
    authService.user.subscribe(user => this.user = user);
  }

  ngOnInit(): void {
  }

  goToLoginPage() {
    this.router.navigate(['/login']);
  }

  logout() {
    this.authService.logout();
  }

}
