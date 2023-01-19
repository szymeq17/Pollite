import { Injectable } from '@angular/core';
import {BehaviorSubject, map, Observable} from "rxjs";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {User} from "../model/User";
import {Constants} from "../config/constants";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private userSubject: BehaviorSubject<User>;
  public user: Observable<User>;

  constructor(
    private router: Router,
    private http: HttpClient
  ) {
    // @ts-ignore
    this.userSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('user')));
    this.user = this.userSubject.asObservable();
  }

  public get userValue(): User {
    return this.userSubject.value;
  }

  login(username: string, password: string) {
    return this.http.post<User>(`${Constants.API_ENDPOINT}auth/login`, { username, password })
      .pipe(map(user => {
        user.authdata = window.btoa(username + ':' + password);
        localStorage.setItem('user', JSON.stringify(user));
        this.userSubject.next(user);
        return user;
      }));
  }

  logout() {
    localStorage.removeItem('user');
    // @ts-ignore
    this.userSubject.next(null);
    this.router.navigate(['/login']);
  }

}
