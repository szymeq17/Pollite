import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../service/auth.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  form: FormGroup;
  returnUrl: string;

  constructor(private fb: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private authService: AuthService,
              private toastr: ToastrService) {
    if (this.authService.userValue) {
      this.router.navigate(['/']);
    }
  }

  ngOnInit(): void {
    this.form = this.fb.group({
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required])
    });

    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  onSubmit() {
    if (this.form.invalid) {
      return;
    }
    this.authService.login(
      this.form.get('username')?.value,
      this.form.get('password')?.value).subscribe(
        _ => {
          this.router.navigate([this.returnUrl]);
        },
        _ => {
          this.toastr.error("Username or password is incorrect!");
        });
  }

}
