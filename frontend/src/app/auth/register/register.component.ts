import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../../service/auth.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

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

  onSubmit() {
    if (this.form.invalid) {
      return;
    }

    this.authService.register(
      this.form.get('username')?.value,
      this.form.get('password')?.value).subscribe(
      _ => {
        this.router.navigate([this.returnUrl]);
      },
      _ => {
        this.toastr.error("Error occurred");
      });
  }

  ngOnInit(): void {
    this.form = this.fb.group({
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required]),
      repeatedPassword: new FormControl('', [Validators.required])
    });

    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

}
